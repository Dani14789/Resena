package com.EcoMarket.Resena.Controller;

import com.EcoMarket.Resena.controller.ResenaController;
import com.EcoMarket.Resena.model.CrearSolicitudResenaDTO;
import com.EcoMarket.Resena.model.Resena;
import com.EcoMarket.Resena.service.ResenaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ResenaController.class)
class ResenaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResenaService resenaService;

    @Autowired
    private ObjectMapper objectMapper;

    private Resena resena1;
    private Resena resena2;
    private CrearSolicitudResenaDTO solicitudDTO;

    @BeforeEach
    void setUp() {
        resena1 = new Resena();
        resena1.setId(1L);
        resena1.setIdProducto("1");
        resena1.setIdUsuario("100");
        resena1.setCalificacion(5);
        resena1.setComentario("¡Muy bueno!");

        resena2 = new Resena();
        resena2.setId(2L);
        resena2.setIdProducto("2");
        resena2.setIdUsuario("101");
        resena2.setCalificacion(4);
        resena2.setComentario("Buen producto.");

        solicitudDTO = new CrearSolicitudResenaDTO();
        solicitudDTO.setIdProducto("1");
        solicitudDTO.setIdUsuario("100");
        solicitudDTO.setCalificacion(5);
        solicitudDTO.setComentario("¡Muy bueno!");
    }

    @Test
    void testCrearResena() throws Exception {
        when(resenaService.agregarResena(any(CrearSolicitudResenaDTO.class))).thenReturn(resena1);

        mockMvc.perform(post("/api/resenas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(solicitudDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.comentario", is("¡Muy bueno!")));
    }

    @Test
    void testCrearResena_Error() throws Exception {
        when(resenaService.agregarResena(any(CrearSolicitudResenaDTO.class))).thenThrow(new RuntimeException("Error al guardar"));

        mockMvc.perform(post("/api/resenas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(solicitudDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testObtenerTodasLasResenas() throws Exception {
    // ... Configuración del Mock ...
    Resena resena1 = new Resena(1L, "1", "100", 5, "¡Muy bueno!", null);
    Resena resena2 = new Resena(2L, "2", "101", 4, "Buen producto.", null);
    List<Resena> listaResenas = Arrays.asList(resena1, resena2);

    when(resenaService.obtenerTodasLasResenas()).thenReturn(listaResenas);

    // Ejecución y Aserción
    mockMvc.perform(get("/api/resenas"))
            .andExpect(status().isOk())
            // 1. Verifica que el Content-Type ahora sea APPLICATION_HAL_JSON
            .andExpect(content().contentType("application/hal+json"))
            // 2. Verifica que el array anidado 'resenaList' existe
            .andExpect(jsonPath("$._embedded.resenaList").exists())
            // 3. Verifica que el array anidado tiene el tamaño esperado
            .andExpect(jsonPath("$._embedded.resenaList", hasSize(2)));
}

    @Test
    void testObtenerResenaPorId_Existente() throws Exception {
        when(resenaService.obtenerResenaPorId(1L)).thenReturn(Optional.of(resena1));

        mockMvc.perform(get("/api/resenas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.comentario", is("¡Muy bueno!")));
    }

    @Test
    void testObtenerResenaPorId_NoEncontrada() throws Exception {
        when(resenaService.obtenerResenaPorId(3L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/resenas/3"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarResena_Existente() throws Exception {
        doNothing().when(resenaService).eliminarResena(1L);

        mockMvc.perform(delete("/api/resenas/1"))
                .andExpect(status().isNoContent());

        verify(resenaService, times(1)).eliminarResena(1L);
    }

    @Test
    void testEliminarResena_NoEncontrada() throws Exception {
        doThrow(new RuntimeException("Reseña no encontrada")).when(resenaService).eliminarResena(3L);

        mockMvc.perform(delete("/api/resenas/3"))
                .andExpect(status().isNotFound());

        verify(resenaService, times(1)).eliminarResena(3L);
    }
}
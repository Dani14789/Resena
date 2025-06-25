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

import java.util.Collections;

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

    private Resena resena;
    private CrearSolicitudResenaDTO solicitudDTO;

    @BeforeEach
    void setUp() {
        resena = new Resena();
        resena.setId(1L);
        resena.setIdProducto("1");
        resena.setIdUsuario("100");
        resena.setCalificacion(5);
        resena.setComentario("¡Muy bueno!");

        solicitudDTO = new CrearSolicitudResenaDTO();
        solicitudDTO.setIdProducto("1");
        solicitudDTO.setIdUsuario("100");
        solicitudDTO.setCalificacion(5);
        solicitudDTO.setComentario("¡Muy bueno!");
    }

    @Test
    void testCrearResena() throws Exception {
        when(resenaService.agregarResena(any(CrearSolicitudResenaDTO.class))).thenReturn(resena);

        mockMvc.perform(post("/api/resenas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(solicitudDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("comentario", is("¡Muy bueno!")));
    }

    @Test
    void testObtenerTodasLasResenas() throws Exception {
        when(resenaService.obtenerTodasLasResenas()).thenReturn(Collections.singletonList(resena));

        mockMvc.perform(get("/api/resenas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("", hasSize(1)))
                .andExpect(jsonPath("[0].id", is(1)));
    }

    @Test
    void testEliminarResena_Existente() throws Exception {
        doNothing().when(resenaService).eliminarResena(1L);

        mockMvc.perform(delete("/api/resenas/1"))
                .andExpect(status().isNoContent());

        verify(resenaService, times(1)).eliminarResena(1L);
    }
}
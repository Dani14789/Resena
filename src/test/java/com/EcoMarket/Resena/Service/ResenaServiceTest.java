package com.EcoMarket.Resena.Service;

import com.EcoMarket.Resena.model.CrearSolicitudResenaDTO;
import com.EcoMarket.Resena.model.Resena;
import com.EcoMarket.Resena.repository.ResenaRepository;
import com.EcoMarket.Resena.service.ResenaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) 
class ResenaServiceTest {

    @Mock 
    private ResenaRepository resenaRepository;

    @InjectMocks 
    private ResenaService resenaService;

    private Resena resena;
    private CrearSolicitudResenaDTO solicitudDTO;

    @BeforeEach
    void setUp() {
        solicitudDTO = new CrearSolicitudResenaDTO();
        solicitudDTO.setIdProducto("1");
        solicitudDTO.setIdUsuario("100");
        solicitudDTO.setCalificacion(5);
        solicitudDTO.setComentario("Excelente producto!");

        resena = new Resena();
        resena.setId(1L);
        resena.setIdProducto(solicitudDTO.getIdProducto());
        resena.setIdUsuario(solicitudDTO.getIdUsuario());
        resena.setCalificacion(solicitudDTO.getCalificacion());
        resena.setComentario(solicitudDTO.getComentario());
    }

    @Test
    void testAgregarResena() {

        when(resenaRepository.save(any(Resena.class))).thenReturn(resena);

        Resena resenaGuardada = resenaService.agregarResena(solicitudDTO);

        assertNotNull(resenaGuardada);
        assertEquals(5 , resenaGuardada.getCalificacion());
        assertEquals("Excelente producto!", resenaGuardada.getComentario());
        assertEquals("1", resenaGuardada.getIdProducto());
        verify(resenaRepository, times(1)).save(any(Resena.class));
    }

    @Test
    void testEliminarResena() {
        Long idResenaExistente = 1L;
        when(resenaRepository.existsById(idResenaExistente)).thenReturn(true);
        doNothing().when(resenaRepository).deleteById(idResenaExistente);

        assertDoesNotThrow(() -> resenaService.eliminarResena(idResenaExistente));
        verify(resenaRepository, times(1)).deleteById(idResenaExistente);
    }

    @Test
    void testObtenerResenaPorId() {
        Long idResena = 1L;
        when(resenaRepository.findById(idResena)).thenReturn(Optional.of(resena));

        Optional<Resena> resenaEncontrada = resenaService.obtenerResenaPorId(idResena);

        assertTrue(resenaEncontrada.isPresent()); 
        assertEquals(idResena, resenaEncontrada.get().getId());
        verify(resenaRepository, times(1)).findById(idResena);
    }

    @Test
    void testObtenerTodasLasResenas() {
        when(resenaRepository.findAll()).thenReturn(Collections.singletonList(resena));

        List<Resena> listaResenas = resenaService.obtenerTodasLasResenas();

        assertNotNull(listaResenas);
        assertEquals(1, listaResenas.size());
        verify(resenaRepository, times(1)).findAll();
    }


}
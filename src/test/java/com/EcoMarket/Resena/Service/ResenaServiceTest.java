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

import java.util.Arrays;
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

    private Resena resena1;
    private Resena resena2;
    private CrearSolicitudResenaDTO solicitudDTO;

    @BeforeEach
    void setUp() {
        solicitudDTO = new CrearSolicitudResenaDTO();
        solicitudDTO.setIdProducto("1");
        solicitudDTO.setIdUsuario("100");
        solicitudDTO.setCalificacion(5);
        solicitudDTO.setComentario("Excelente producto!");

        resena1 = new Resena();
        resena1.setId(1L);
        resena1.setIdProducto(solicitudDTO.getIdProducto());
        resena1.setIdUsuario(solicitudDTO.getIdUsuario());
        resena1.setCalificacion(solicitudDTO.getCalificacion());
        resena1.setComentario(solicitudDTO.getComentario());

        resena2 = new Resena();
        resena2.setId(2L);
        resena2.setIdProducto("2");
        resena2.setIdUsuario("101");
        resena2.setCalificacion(4);
        resena2.setComentario("Buen producto.");
    }

    @Test
    void testAgregarResena() {
        when(resenaRepository.save(any(Resena.class))).thenReturn(resena1);

        Resena resenaGuardada = resenaService.agregarResena(solicitudDTO);

        assertNotNull(resenaGuardada);
        assertEquals(5, resenaGuardada.getCalificacion());
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
    void testEliminarResena_NoEncontrada() {
        Long idResenaNoExistente = 3L;
        when(resenaRepository.existsById(idResenaNoExistente)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            resenaService.eliminarResena(idResenaNoExistente);
        });

        assertEquals("Reseña no encontrada con id: " + idResenaNoExistente, exception.getMessage());
        verify(resenaRepository, never()).deleteById(idResenaNoExistente);
    }

    @Test
    void testObtenerResenaPorId() {
        Long idResena = 1L;
        when(resenaRepository.findById(idResena)).thenReturn(Optional.of(resena1));

        Optional<Resena> resenaEncontrada = resenaService.obtenerResenaPorId(idResena);

        assertTrue(resenaEncontrada.isPresent());
        assertEquals(idResena, resenaEncontrada.get().getId());
        verify(resenaRepository, times(1)).findById(idResena);
    }

    @Test
    void testObtenerTodasLasResenas() {
        when(resenaRepository.findAll()).thenReturn(Arrays.asList(resena1, resena2));

        List<Resena> listaResenas = resenaService.obtenerTodasLasResenas();

        assertNotNull(listaResenas);
        assertEquals(2, listaResenas.size());
        verify(resenaRepository, times(1)).findAll();
    }

    @Test
    void testGuardarResena() {
        when(resenaRepository.save(any(Resena.class))).thenReturn(resena1);

        Resena resenaGuardada = resenaService.guardarResena(resena1);

        assertNotNull(resenaGuardada);
        assertEquals(resena1.getId(), resenaGuardada.getId());
        verify(resenaRepository, times(1)).save(resena1);
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> afb39610529be3d210a0817fdb0824ebace69da6

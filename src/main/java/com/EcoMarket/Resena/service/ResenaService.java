package com.EcoMarket.Resena.service;

import com.EcoMarket.Resena.model.CrearSolicitudResenaDTO;
import com.EcoMarket.Resena.model.Resena;
import com.EcoMarket.Resena.repository.ResenaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;

    @SuppressWarnings("unused")
    @Autowired
    private RestTemplate restTemplate;

    public Resena agregarResena(CrearSolicitudResenaDTO requestDTO) {
        Resena nuevaResena = new Resena();
        nuevaResena.setIdProducto(requestDTO.getIdProducto());
        nuevaResena.setIdUsuario(requestDTO.getIdUsuario());
        nuevaResena.setCalificacion(requestDTO.getCalificacion());
        nuevaResena.setComentario(requestDTO.getComentario());

        return resenaRepository.save(nuevaResena);
    }

    public void eliminarResena(Long idResena) { 
        if (!resenaRepository.existsById(idResena)) {
            throw new RuntimeException("Reseña no encontrada con id: " + idResena);
        }
        resenaRepository.deleteById(idResena);
    }

    public Optional<Resena> obtenerResenaPorId(Long idResena) { 
        return resenaRepository.findById(idResena);
    }

    public List<Resena> obtenerTodasLasResenas() {
        return resenaRepository.findAll();
    }

    public Resena guardarResena(Resena resena) {
        return resenaRepository.save(resena);
    }
}
package com.EcoMarket.Resena.controller;

import com.EcoMarket.Resena.model.CrearSolicitudResenaDTO;
import com.EcoMarket.Resena.model.Resena;
import com.EcoMarket.Resena.service.ResenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    @PostMapping
    public ResponseEntity<Resena> agregarResena(@RequestBody CrearSolicitudResenaDTO requestDTO) {
        try {
            Resena nuevaResena = resenaService.agregarResena(requestDTO);
            return new ResponseEntity<>(nuevaResena, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{idResena}")
    public ResponseEntity<Void> eliminarResena(@PathVariable Long idResena) { 
        try {
            resenaService.eliminarResena(idResena);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{idResena}")
    public ResponseEntity<Resena> obtenerResenaPorId(@PathVariable Long idResena) { 
        return resenaService.obtenerResenaPorId(idResena)
                .map(resena -> new ResponseEntity<>(resena, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Resena>> obtenerTodasLasResenas() {
        List<Resena> resenas = resenaService.obtenerTodasLasResenas();
        return new ResponseEntity<>(resenas, HttpStatus.OK);
    }
}
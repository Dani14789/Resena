package com.EcoMarket.Resena.controller;

import com.EcoMarket.Resena.model.CrearSolicitudResenaDTO;
import com.EcoMarket.Resena.model.Resena;
import com.EcoMarket.Resena.service.ResenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

// Importar los métodos estáticos para crear enlaces
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    @PostMapping
    public ResponseEntity<EntityModel<Resena>> agregarResena(@RequestBody CrearSolicitudResenaDTO requestDTO) {
        try {
            Resena nuevaResena = resenaService.agregarResena(requestDTO);

            // Crear enlaces para la nueva reseña
            Link selfLink = linkTo(methodOn(ResenaController.class).obtenerResenaPorId(nuevaResena.getId())).withSelfRel();
            Link allResenasLink = linkTo(methodOn(ResenaController.class).obtenerTodasLasResenas()).withRel("all-resenas");
            
            // Envolver en EntityModel para añadir enlaces
            EntityModel<Resena> resource = EntityModel.of(nuevaResena, selfLink, allResenasLink);

            return new ResponseEntity<>(resource, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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
    public ResponseEntity<EntityModel<Resena>> obtenerResenaPorId(@PathVariable Long idResena) {
        return resenaService.obtenerResenaPorId(idResena)
                .map(resena -> {
                    // Crear enlace a sí mismo
                    Link selfLink = linkTo(methodOn(ResenaController.class).obtenerResenaPorId(idResena)).withSelfRel();
                    // Crear enlace a la colección
                    Link allResenasLink = linkTo(methodOn(ResenaController.class).obtenerTodasLasResenas()).withRel("all-resenas");
                    
                    return EntityModel.of(resena, selfLink, allResenasLink);
                })
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Resena>>> obtenerTodasLasResenas() {
        List<Resena> resenas = resenaService.obtenerTodasLasResenas();
        
        // Añadir enlace "self" a cada reseña individual
        List<EntityModel<Resena>> resenaResources = resenas.stream()
                .map(resena -> {
                    Link selfLink = linkTo(methodOn(ResenaController.class).obtenerResenaPorId(resena.getId())).withSelfRel();
                    return EntityModel.of(resena, selfLink);
                })
                .collect(Collectors.toList());

        // Añadir enlace "self" a la colección completa
        Link selfLink = linkTo(methodOn(ResenaController.class).obtenerTodasLasResenas()).withSelfRel();
        CollectionModel<EntityModel<Resena>> resources = CollectionModel.of(resenaResources, selfLink);

        return new ResponseEntity<>(resources, HttpStatus.OK);
    }
}
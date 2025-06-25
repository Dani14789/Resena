package com.EcoMarket.Resena.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist; 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id; 

    @Column(nullable = false)
    private String idProducto;

    @Column(nullable = false)
    private String idUsuario;

    @Column(nullable = false)
    private int calificacion;

    @Column(length = 1000)
    private String comentario;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @PrePersist
    public void prePersist() {
        this.fecha = LocalDateTime.now(); 
    }
}

package com.EcoMarket.Resena.model;

import lombok.Data;

@Data
public class CrearSolicitudResenaDTO {
    private String idProducto;
    private String idUsuario;
    private int calificacion;
    private String comentario;
}

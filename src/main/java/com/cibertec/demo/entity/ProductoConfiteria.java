package com.cibertec.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "productosconfiteria")
public class ProductoConfiteria {

    @Id
    @Column(name = "IdProducto")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProducto;

    @Column(name = "NombreProducto")
    private String nombreProducto;

    @Column(name = "Categoria")
    private String categoria;

    @Column(name = "Precio")
    private Double precio;

    @Column(name = "Stock")
    private Integer stock;

    @Column(name = "Descripcion")
    private String descripcion;
}
package com.cibertec.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "detalle_ventas")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdDetalle")
    private Integer idDetalle;

    // Relación con la Venta (Muchos detalles pertenecen a una venta)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdVenta", nullable = false)
    private Venta venta;

    // Relación con el Producto
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdProducto", nullable = false)
    private ProductoConfiteria producto;

    @Column(name = "Cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "PrecioUnitario", nullable = false)
    private Double precioUnitario;

    @Column(name = "Subtotal", nullable = false)
    private Double subtotal;
}
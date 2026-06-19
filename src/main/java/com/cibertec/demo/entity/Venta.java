package com.cibertec.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdVenta")
    private Integer idVenta;

    @Column(name = "Username", nullable = false, length = 50)
    private String username;

    @Column(name = "FechaVenta", nullable = false)
    private LocalDateTime fechaVenta;

    @Column(name = "Total", nullable = false)
    private Double total;

    @Column(name = "Estado", nullable = false, length = 20)
    private String estado; // "PENDIENTE", "APROBADO", "RECHAZADO"

    @Column(name = "MetodoPago", length = 20)
    private String metodoPago; // "TARJETA", "YAPE", "PLIN"

    @Column(name = "TipoCompra", nullable = false, length = 30)
    private String tipoCompra; // "INDEPENDIENTE" o "VINCULADA_A_ENTRADA"

    // Campo preparado para el futuro (Escalabilidad)
    // Cuando tu compañero termine entradas, aquí guardaremos el ID de la entrada.
    @Column(name = "IdEntradaVinculada")
    private Integer idEntradaVinculada; 
}
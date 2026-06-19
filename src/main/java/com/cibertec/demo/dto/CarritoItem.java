package com.cibertec.demo.dto;

import com.cibertec.demo.entity.ProductoConfiteria;

/**
 * DTO que representa un ítem en el carrito de compras.
 * Agrupa el producto con su cantidad para evitar duplicados en la sesión.
 */
public class CarritoItem {
    
    private ProductoConfiteria producto;
    private Integer cantidad;

    public CarritoItem(ProductoConfiteria producto, Integer cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public ProductoConfiteria getProducto() {
        return producto;
    }

    public void setProducto(ProductoConfiteria producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    // Método útil para calcular el subtotal de esta fila
    public Double getSubtotal() {
        return this.producto.getPrecio() * this.cantidad;
    }
}
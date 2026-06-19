package com.cibertec.demo.service;

import com.cibertec.demo.dto.ValidationResult;

/**
 * Servicio encargado de validar el contexto completo de una compra de snacks.
 * 
 * Este servicio centraliza toda la lógica de validación:
 * - ¿El usuario está logueado?
 * - ¿El producto existe?
 * - ¿Hay stock suficiente?
 * - ¿El usuario tiene entrada activa? (Para vincular snacks a entradas)
 * 
 * Retorna un ValidationResult que el controlador usará para decidir
 * qué acción tomar y qué mensaje mostrar al usuario.
 */
public interface SnackPurchaseValidatorService {
    
    /**
     * Valida si un usuario puede agregar un producto al carrito.
     * 
     * @param username El nombre de usuario de la sesión actual (puede ser null si no está logueado)
     * @param idProducto El ID del producto que se quiere agregar
     * @param cantidad La cantidad que se quiere agregar
     * @return ValidationResult indicando el resultado de la validación
     */
    ValidationResult validarCompra(String username, Integer idProducto, Integer cantidad);
}
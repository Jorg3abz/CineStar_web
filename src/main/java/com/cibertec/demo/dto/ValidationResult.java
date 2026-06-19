package com.cibertec.demo.dto;

/**
 * Enum que representa los posibles resultados de validación
 * al intentar agregar un producto de confitería al carrito.
 * 
 * Usado para determinar qué acción debe tomar el controlador
 * y qué mensaje de guía mostrar al usuario.
 */
public enum ValidationResult {
    
    /**
     * El usuario no está autenticado.
     * Debe iniciar sesión antes de continuar.
     */
    NO_AUTENTICADO,
    
    /**
     * El producto no existe en la base de datos.
     */
    PRODUCTO_NO_ENCONTRADO,
    
    /**
     * El producto existe pero no hay stock suficiente.
     */
    STOCK_INSUFICIENTE,
    
    /**
     * Validación exitosa. El usuario tiene entrada activa
     * y el snack se vinculará a esa entrada (Futuro).
     */
    VINCULADO_A_ENTRADA,
    
    /**
     * Validación exitosa. El usuario NO tiene entrada activa,
     * pero puede comprar snacks de forma independiente.
     */
    COMPRA_INDEPENDIENTE
}
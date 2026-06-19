package com.cibertec.demo.service.impl;

import org.springframework.stereotype.Service;
import com.cibertec.demo.entity.ProductoConfiteria;
import com.cibertec.demo.service.ProductoConfiteriaService;
import com.cibertec.demo.service.SnackPurchaseValidatorService;
import com.cibertec.demo.service.TicketService;
import com.cibertec.demo.dto.ValidationResult;

/**
 * Implementación del validador de compras de snacks.
 * 
 * Este servicio actúa como un "orquestador" que consulta a otros servicios
 * para determinar si una compra es válida y bajo qué contexto.
 */
@Service
public class SnackPurchaseValidatorServiceImpl implements SnackPurchaseValidatorService {

    private final TicketService ticketService;
    private final ProductoConfiteriaService productoService;

    // Inyección por constructor (Estándar Senior)
    public SnackPurchaseValidatorServiceImpl(
            TicketService ticketService,
            ProductoConfiteriaService productoService) {
        this.ticketService = ticketService;
        this.productoService = productoService;
    }

    @Override
    public ValidationResult validarCompra(String username, Integer idProducto, Integer cantidad) {
        
        // 1. Validar que el usuario esté autenticado
        if (username == null || username.trim().isEmpty()) {
            return ValidationResult.NO_AUTENTICADO;
        }

        // 2. Validar que el producto exista
        ProductoConfiteria producto = productoService.buscarPorId(idProducto);
        if (producto == null) {
            return ValidationResult.PRODUCTO_NO_ENCONTRADO;
        }

        // 3. Validar que haya stock suficiente
        if (producto.getStock() < cantidad) {
            return ValidationResult.STOCK_INSUFICIENTE;
        }

        // 4. Validar si el usuario tiene entrada activa
        boolean tieneEntradaActiva = ticketService.tieneEntradaActiva(username);

        // 5. Retornar el contexto apropiado
        if (tieneEntradaActiva) {
            return ValidationResult.VINCULADO_A_ENTRADA;
        } else {
            return ValidationResult.COMPRA_INDEPENDIENTE;
        }
    }
}
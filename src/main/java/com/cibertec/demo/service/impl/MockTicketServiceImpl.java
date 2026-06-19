package com.cibertec.demo.service.impl;

import org.springframework.stereotype.Service;
import com.cibertec.demo.service.TicketService;

/**
 * Versión SIMULADA (Mock) del servicio de Entradas.
 * 
 * Esto te permite probar tu lógica de confitería HOY, 
 * sin esperar a que tu compañero termine la base de datos de boletos.
 */
@Service
public class MockTicketServiceImpl implements TicketService {

    @Override
    public boolean tieneEntradaActiva(String username) {
        // LÓGICA MOCKEADA (Temporal)
        // Si el usuario no está logueado, obviamente no tiene entrada.
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        // SIMULACIÓN:
        // Por ahora, retornamos 'true' para que puedas probar el flujo 
        // de "Snack Vinculado a Entrada".
        // (Si quieres probar el flujo de "Compra Independiente / Sin Entrada", 
        // simplemente cambia este 'return true;' por 'return false;').
        return true; 
    }
}
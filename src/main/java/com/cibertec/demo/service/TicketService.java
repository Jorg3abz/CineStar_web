package com.cibertec.demo.service;

/**
 * Interfaz que define los servicios relacionados con los boletos/entradas de cine.
 * 
 * NOTA DE ARQUITECTURA:
 * Esta interfaz es el "contrato". En el futuro, cuando tu compañero termine 
 * su módulo, él creará una clase (ej. EntradaServiceImpl) que implementará 
 * esta misma interfaz consultando la base de datos real.
 * Por ahora, usaremos una versión "Mockeada" (simulada) para no bloquear tu avance.
 */
public interface TicketService {
    
    /**
     * Verifica si un usuario tiene al menos una entrada activa 
     * (comprada y para una función que aún no ha pasado).
     * 
     * @param username El nombre de usuario de la sesión actual.
     * @return true si tiene entrada activa, false en caso contrario.
     */
    boolean tieneEntradaActiva(String username);
}
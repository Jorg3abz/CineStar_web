package com.cibertec.demo.service;

/**
 * Interfaz que simula la comunicación con una Pasarela de Pago externa (PP).
 * En un entorno real, aquí se harían llamadas HTTP a APIs como Culqi, Stripe o PayPal.
 */
public interface PasarelaPagoService {
    
    /**
     * Procesa el pago y retorna si fue aprobado o no.
     * 
     * @param metodoPago "TARJETA", "YAPE", "PLIN"
     * @param numeroTarjeta El número ingresado (solo para tarjetas)
     * @param monto El total a cobrar
     * @return true si el pago fue aprobado, false si fue rechazado
     */
    boolean procesarPago(String metodoPago, String numeroTarjeta, Double monto);
}
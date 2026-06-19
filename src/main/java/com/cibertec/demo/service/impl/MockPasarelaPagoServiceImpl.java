package com.cibertec.demo.service.impl;

import org.springframework.stereotype.Service;

import com.cibertec.demo.service.PasarelaPagoService;

/**
 * Simulación de Pasarela de Pago (Mock)
 * Simula Culqi / Yape / Plin
 */
@Service
public class MockPasarelaPagoServiceImpl implements PasarelaPagoService {

    @Override
    public boolean procesarPago(String metodoPago, String codigo, Double monto) {
        // Simular latencia de red (como una API real)
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (codigo == null || codigo.isEmpty()) {
            return false;
        }

        String limpio = codigo.replaceAll("\\D", "");

        if ("TARJETA".equals(metodoPago)) {
            // Tarjetas Visa (empiezan con 4) son aprobadas
            return limpio.startsWith("4");
        } else if ("YAPE".equals(metodoPago) || "PLIN".equals(metodoPago)) {
            // El código 123456 simula un pago aprobado
            // Cualquier otro código es rechazado (simula error de validación)
            return "123456".equals(limpio);
        }

        return false;
    }
}
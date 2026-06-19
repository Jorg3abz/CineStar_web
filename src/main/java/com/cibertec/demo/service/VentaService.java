package com.cibertec.demo.service;

import java.util.List;
import com.cibertec.demo.entity.ProductoConfiteria;
import com.cibertec.demo.entity.Venta;

/**
 * Servicio responsable de gestionar todo el ciclo de vida de una venta.
 */
public interface VentaService {
    
    /**
     * Procesa una compra completa recibiendo el carrito de la sesión.
     * Registra la venta, crea los detalles y descuenta el stock transaccionalmente.
     */
    Venta procesarCompraConCarrito(
        String username, 
        String metodoPago, 
        String tipoCompra, 
        Integer idEntradaVinculada,
        List<ProductoConfiteria> carrito
    );
    
    /**
     * Busca una venta por su ID.
     */
    Venta buscarPorId(Integer idVenta);
    
    /**
     * Lista todas las ventas de un usuario específico.
     */
    List<Venta> listarPorUsuario(String username);
}
package com.cibertec.demo.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cibertec.demo.entity.DetalleVenta;
import com.cibertec.demo.entity.ProductoConfiteria;
import com.cibertec.demo.entity.Venta;
import com.cibertec.demo.repository.DetalleVentaRepository;
import com.cibertec.demo.repository.ProductoConfiteriaRepository;
import com.cibertec.demo.repository.VentaRepository;
import com.cibertec.demo.service.VentaService;

/**
 * Implementación del servicio de ventas con manejo transaccional.
 */
@Service
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleRepository;
    private final ProductoConfiteriaRepository productoRepository;

    public VentaServiceImpl(
            VentaRepository ventaRepository,
            DetalleVentaRepository detalleRepository,
            ProductoConfiteriaRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.detalleRepository = detalleRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Venta procesarCompraConCarrito(
            String username, 
            String metodoPago,
            String tipoCompra,
            Integer idEntradaVinculada,
            List<ProductoConfiteria> carrito) {
        
        // PASO 1: Validar stock de todos los productos ANTES de crear la venta
        validarStockCompleto(carrito);
        
        // PASO 2: Crear la entidad Venta
        Venta venta = new Venta();
        venta.setUsername(username);
        venta.setFechaVenta(LocalDateTime.now());
        venta.setEstado("APROBADO"); 
        venta.setMetodoPago(metodoPago);
        venta.setTipoCompra(tipoCompra);
        venta.setIdEntradaVinculada(idEntradaVinculada);
        
        // PASO 3: Calcular total
        double total = carrito.stream()
            .mapToDouble(ProductoConfiteria::getPrecio)
            .sum();
        venta.setTotal(total);
        
        // PASO 4: Guardar la venta primero (para obtener el ID)
        venta = ventaRepository.save(venta);
        
        // PASO 5: Crear los detalles y descontar stock
        List<DetalleVenta> detalles = new ArrayList<>();
        
        var productosAgrupados = carrito.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                ProductoConfiteria::getIdProducto,
                java.util.stream.Collectors.counting()
            ));
        
        for (var entry : productosAgrupados.entrySet()) {
            Integer idProducto = entry.getKey();
            Integer cantidad = entry.getValue().intValue();
            
            ProductoConfiteria producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + idProducto));
            
            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(producto.getPrecio() * cantidad);
            
            detalles.add(detalle);
            
            // Descontar stock
            producto.setStock(producto.getStock() - cantidad);
            productoRepository.save(producto);
        }
        
        // PASO 6: Guardar todos los detalles
        detalleRepository.saveAll(detalles);
        
        return venta;
    }

    /**
     * Valida que haya stock suficiente para todos los productos del carrito.
     */
    private void validarStockCompleto(List<ProductoConfiteria> carrito) {
        var productosAgrupados = carrito.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                ProductoConfiteria::getIdProducto,
                java.util.stream.Collectors.counting()
            ));
        
        for (var entry : productosAgrupados.entrySet()) {
            Integer idProducto = entry.getKey();
            Integer cantidadRequerida = entry.getValue().intValue();
            
            ProductoConfiteria producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
            if (producto.getStock() < cantidadRequerida) {
                throw new IllegalStateException(
                    "Stock insuficiente para: " + producto.getNombreProducto() + 
                    ". Disponible: " + producto.getStock() + 
                    ", Requerido: " + cantidadRequerida
                );
            }
        }
    }

    @Override
    public Venta buscarPorId(Integer idVenta) {
        return ventaRepository.findById(idVenta).orElse(null);
    }

    @Override
    public List<Venta> listarPorUsuario(String username) {
        return ventaRepository.findAll();
    }
}
package com.cibertec.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cibertec.demo.entity.DetalleVenta;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {
    
    /**
     * Método robusto: Busca los detalles usando solo el ID de la venta.
     * Spring Data JPA traduce esto a: SELECT * FROM detalle_ventas WHERE IdVenta = ?
     * Evita problemas de entidades "desconectadas" de Hibernate.
     */
    List<DetalleVenta> findByVentaIdVenta(Integer idVenta);
}
package com.cibertec.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.cibertec.demo.entity.Reclamo;
import com.cibertec.demo.entity.Usuario;

@Repository
public interface ReclamoRepository extends JpaRepository<Reclamo, Integer> {

    // Para que el cliente vea solo sus reclamos
    List<Reclamo> findByUsuario(Usuario usuario);

    // Para el dashboard - contar reclamos del día
    @Query("SELECT COUNT(r) FROM Reclamo r WHERE DATE(r.fechaRegistro) = CURRENT_DATE")
    Long contarReclamosHoy();

    // Para el dashboard - contar por mes
    @Query("SELECT COUNT(r) FROM Reclamo r WHERE MONTH(r.fechaRegistro) = MONTH(CURRENT_DATE) AND YEAR(r.fechaRegistro) = YEAR(CURRENT_DATE)")
    Long contarReclamosMes();

    // Para el dashboard - contar por año
    @Query("SELECT COUNT(r) FROM Reclamo r WHERE YEAR(r.fechaRegistro) = YEAR(CURRENT_DATE)")
    Long contarReclamosAnio();
}
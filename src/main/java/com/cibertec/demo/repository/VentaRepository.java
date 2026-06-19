package com.cibertec.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cibertec.demo.entity.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {
}
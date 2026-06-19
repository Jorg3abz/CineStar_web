package com.cibertec.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cibertec.demo.entity.ProductoConfiteria;

@Repository
public interface ProductoConfiteriaRepository extends JpaRepository<ProductoConfiteria, Integer> {
    // Al extender de JpaRepository ya tenemos el findAll(), save(), delete(), etc. gratis!
}
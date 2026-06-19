package com.cibertec.demo.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.cibertec.demo.entity.ProductoConfiteria;
import com.cibertec.demo.repository.ProductoConfiteriaRepository;
import com.cibertec.demo.service.ProductoConfiteriaService;

@Service
public class ProductoConfiteriaServiceImpl implements ProductoConfiteriaService {

    private final ProductoConfiteriaRepository repository;

    public ProductoConfiteriaServiceImpl(ProductoConfiteriaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ProductoConfiteria> listarTodos() {
        return repository.findAll();
    }

    @Override
    public ProductoConfiteria buscarPorId(Integer id) {
        return repository.findById(id).orElse(null);
    }
}
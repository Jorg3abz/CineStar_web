package com.cibertec.demo.service;

import java.util.List;
import com.cibertec.demo.entity.ProductoConfiteria;

public interface ProductoConfiteriaService {
    
    public List<ProductoConfiteria> listarTodos();
    
    public ProductoConfiteria buscarPorId(Integer id);
    
}
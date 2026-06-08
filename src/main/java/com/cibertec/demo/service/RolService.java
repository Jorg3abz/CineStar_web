package com.cibertec.demo.service;

import java.util.List;

import com.cibertec.demo.entity.Rol;

public interface RolService {
	
	public List<Rol> listarTodosRol();
	
	public Rol buscarById(Integer id);
}

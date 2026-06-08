package com.cibertec.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.demo.entity.Rol;
import com.cibertec.demo.repository.RolRepository;
import com.cibertec.demo.service.RolService;

@Service
public class RolServiceImpl implements RolService {
	
	@Autowired
	RolRepository rolRepository;

	@Override
	public List<Rol> listarTodosRol() {
		return rolRepository.findAll();
	}

	@Override
	public Rol buscarById(Integer id) {
		return rolRepository.findById(id).get();
	}
}

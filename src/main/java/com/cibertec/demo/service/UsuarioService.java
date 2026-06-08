package com.cibertec.demo.service;

import java.util.List;

import com.cibertec.demo.entity.Usuario;

public interface UsuarioService {
	
	public Usuario guardarUsuario(Usuario registroDTO);
	
	public List<Usuario> listarTodosUsuario();
	
	public boolean login(Usuario usuario);
	
	public Usuario buscarByUsuario(String username);
}

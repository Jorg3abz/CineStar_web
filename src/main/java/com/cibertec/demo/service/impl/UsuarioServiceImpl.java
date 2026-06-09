package com.cibertec.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cibertec.demo.entity.Usuario;
import com.cibertec.demo.repository.UsuarioRepository;
import com.cibertec.demo.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepositorio;

	public UsuarioServiceImpl(UsuarioRepository usuarioRepositorio) {
		super();
		this.usuarioRepositorio = usuarioRepositorio;
	}

	@Override
	public Usuario guardarUsuario(Usuario objUsuario) {
		Usuario usuario = new Usuario(objUsuario.getNombres(),objUsuario.getApellidos(),
										objUsuario.getUsername(),objUsuario.getClave(), objUsuario.getRol()) ;				
		return usuarioRepositorio.save(usuario);
	}

	@Override
	public List<Usuario> listarTodosUsuario() {
		return usuarioRepositorio.findAll();
	}

	@Override
	public boolean login(Usuario usuario) {
		Usuario entidad=usuarioRepositorio.findByUsuarioAndClave(usuario.getUsername(), usuario.getClave());
		System.out.println("usuario.getUsername()--> "+usuario.getUsername());
		System.out.println("usuario.getClave()--> "+usuario.getClave());
		if(entidad==null) 
			return false;
		else 
			return true;		
	}

	@Override
	public Usuario buscarByUsuario(String username) {
		Usuario entidad=usuarioRepositorio.findByUsuario(username);
		return entidad;
	}
	
	//Nuevo Agregado 08-06
	@Override
	public Usuario loginConDatos(String username, String clave) {
	    return usuarioRepositorio.findByUsuarioAndClave(username, clave);
	}
}

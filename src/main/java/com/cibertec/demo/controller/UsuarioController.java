package com.cibertec.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.cibertec.demo.entity.Rol;
import com.cibertec.demo.entity.Usuario;
import com.cibertec.demo.repository.RolRepository;
import com.cibertec.demo.service.RolService;
import com.cibertec.demo.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private RolRepository rolRepository;
	
	
	// Página principal
	@GetMapping("/")
    public String index() {
        return "index";
    }
	
	// Login desde el modal
    @PostMapping("/login")
    public String iniciarSesion(Usuario usuario, HttpSession session) {
        boolean band = usuarioService.login(usuario);
        if (band) {
            session.setAttribute("usuarioLogueado", usuario.getUsername());
            return "redirect:/home";
        } else {
            return "redirect:/?error=login";
        }
    }

    // Home tras login
    @GetMapping("/home")
    public String home() {
        return "home";
    }

 // Registro
    @PostMapping("/register/save")
    public String registro(Usuario usuario) {
    	Rol rolCliente = rolRepository.findById(2).orElse(null);
    	usuario.setRol(rolCliente);
    	usuarioService.guardarUsuario(usuario);
    	return "redirect:/";
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}

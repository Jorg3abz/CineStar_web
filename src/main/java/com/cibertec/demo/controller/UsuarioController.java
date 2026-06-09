package com.cibertec.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cibertec.demo.entity.Rol;
import com.cibertec.demo.entity.Usuario;
import com.cibertec.demo.repository.RolRepository;
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
	public String index(HttpSession session, Model model) {

	    String usuario =
	            (String) session.getAttribute("usuarioLogueado");

	    model.addAttribute("usuario", usuario);

	    return "index";
	}
	
	//Nuevo Agregado 08-06
	@PostMapping("/login")
	public String iniciarSesion(@RequestParam String username,
	                             @RequestParam String clave,
	                             HttpSession session) {
	    Usuario encontrado = usuarioService.loginConDatos(username, clave);
	    if (encontrado != null) {
	        session.setAttribute("usuarioLogueado", encontrado.getUsername());
	        session.setAttribute("rolUsuario", encontrado.getRol().getId());
	        if (encontrado.getRol().getId() == 1) {
	            return "redirect:/admin/dashboard";
	        } else {
	            return "redirect:/";
	        }
	    } else {
	        return "redirect:/?error=login";
	    }
	}

    // Home tras login
    @GetMapping("/home")
    public String home() {
        return "home";
    }

    // Registro //Cambio
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

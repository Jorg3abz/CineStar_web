package com.cibertec.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cibertec.demo.service.ReclamoService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private ReclamoService reclamoService;

	@GetMapping("/dashboard")
	public String dashboard(HttpSession session, Model model) {
	    String usuario = (String) session.getAttribute("usuarioLogueado");
	    if (usuario == null) {
	        return "redirect:/";
	    }
	    model.addAttribute("usuario", usuario);
	    model.addAttribute("reclamosHoy", reclamoService.contarReclamosHoy());
	    model.addAttribute("reclamosMes", reclamoService.contarReclamosMes());
	    model.addAttribute("reclamosAnio", reclamoService.contarReclamosAnio());
	    return "admin/dashboard";
	}
}
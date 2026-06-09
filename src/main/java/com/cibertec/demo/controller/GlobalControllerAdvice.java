package com.cibertec.demo.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("usuario")
    public String agregarUsuario(HttpSession session) {
        return (String) session.getAttribute("usuarioLogueado");
    }

    @ModelAttribute("rolUsuario")
    public Integer agregarRol(HttpSession session) {
        return (Integer) session.getAttribute("rolUsuario");
    }
}
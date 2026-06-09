package com.cibertec.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.cibertec.demo.entity.Reclamo;
import com.cibertec.demo.entity.Usuario;
import com.cibertec.demo.service.ReclamoService;
import com.cibertec.demo.service.UsuarioService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/reclamos")
public class ReclamoController {

    @Autowired
    private ReclamoService reclamoService;

    @Autowired
    private UsuarioService usuarioService;

    // Cliente - ver formulario de registro
    @GetMapping("/nuevo")
    public String formularioNuevo(HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/";
        }
        model.addAttribute("reclamo", new Reclamo());
        return "reclamos/nuevo";
    }

    // Cliente - guardar reclamo
    @PostMapping("/guardar")
    public String guardarReclamo(Reclamo reclamo, HttpSession session) {
        String username = (String) session.getAttribute("usuarioLogueado");
        Usuario usuario = usuarioService.buscarByUsuario(username);
        reclamo.setUsuario(usuario);
        reclamoService.guardarReclamo(reclamo);
        return "redirect:/reclamos/mis-reclamos";
    }

    // Cliente - ver sus reclamos
    @GetMapping("/mis-reclamos")
    public String misReclamos(HttpSession session, Model model) {
        String username = (String) session.getAttribute("usuarioLogueado");
        if (username == null) {
            return "redirect:/";
        }
        Usuario usuario = usuarioService.buscarByUsuario(username);
        List<Reclamo> reclamos = reclamoService.listarPorUsuario(usuario);
        model.addAttribute("reclamos", reclamos);
        return "reclamos/mis-reclamos";
    }

    // Admin - ver todos los reclamos (buzón)
    @GetMapping("/admin/buzon")
    public String buzon(Model model) {
        List<Reclamo> reclamos = reclamoService.listarTodos();
        model.addAttribute("reclamos", reclamos);
        return "admin/buzon";
    }

    // Admin - ver detalle de un reclamo
    @GetMapping("/admin/detalle/{id}")
    public String detalle(@PathVariable Integer id, Model model) {
        Reclamo reclamo = reclamoService.buscarPorId(id);
        model.addAttribute("reclamo", reclamo);
        return "admin/detalle-reclamo";
    }

    // Admin - actualizar estado y respuesta
    @PostMapping("/admin/actualizar")
    public String actualizarReclamo(@RequestParam Integer id,
                                    @RequestParam String estado,
                                    @RequestParam String respuesta) {
        Reclamo reclamo = reclamoService.buscarPorId(id);
        reclamo.setEstado(estado);
        reclamo.setRespuesta(respuesta);
        reclamoService.actualizarReclamo(reclamo);
        return "redirect:/reclamos/admin/buzon";
    }
}
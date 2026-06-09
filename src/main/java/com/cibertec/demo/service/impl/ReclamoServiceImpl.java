package com.cibertec.demo.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cibertec.demo.entity.Reclamo;
import com.cibertec.demo.entity.Usuario;
import com.cibertec.demo.repository.ReclamoRepository;
import com.cibertec.demo.service.ReclamoService;

@Service
public class ReclamoServiceImpl implements ReclamoService {

    @Autowired
    private ReclamoRepository reclamoRepository;

    @Override
    public Reclamo guardarReclamo(Reclamo reclamo) {
        reclamo.setEstado("Pendiente");
        reclamo.setFechaRegistro(LocalDateTime.now());
        return reclamoRepository.save(reclamo);
    }

    @Override
    public List<Reclamo> listarTodos() {
        return reclamoRepository.findAll();
    }

    @Override
    public List<Reclamo> listarPorUsuario(Usuario usuario) {
        return reclamoRepository.findByUsuario(usuario);
    }

    @Override
    public Reclamo buscarPorId(Integer id) {
        return reclamoRepository.findById(id).orElse(null);
    }

    @Override
    public Reclamo actualizarReclamo(Reclamo reclamo) {
        reclamo.setFechaActualizacion(LocalDateTime.now());
        return reclamoRepository.save(reclamo);
    }

    @Override
    public Long contarReclamosHoy() {
        return reclamoRepository.contarReclamosHoy();
    }

    @Override
    public Long contarReclamosMes() {
        return reclamoRepository.contarReclamosMes();
    }

    @Override
    public Long contarReclamosAnio() {
        return reclamoRepository.contarReclamosAnio();
    }
    
    @Override
    public Long contarPorEstado(String estado) {
        return reclamoRepository.contarPorEstado(estado);
    }
}
package com.cibertec.demo.service;

import java.util.List;
import com.cibertec.demo.entity.Reclamo;
import com.cibertec.demo.entity.Usuario;

public interface ReclamoService {

    public Reclamo guardarReclamo(Reclamo reclamo);
    public List<Reclamo> listarTodos();
    public List<Reclamo> listarPorUsuario(Usuario usuario);
    public Reclamo buscarPorId(Integer id);
    public Reclamo actualizarReclamo(Reclamo reclamo);
    public Long contarReclamosHoy();
    public Long contarReclamosMes();
    public Long contarReclamosAnio();
}
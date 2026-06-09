package com.cibertec.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cibertec.demo.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	@Query(value="SELECT u.idusuario, u.username, u.nombres, u.apellidos, u.clave, u.idrol "
			+ "FROM usuario u where u.username = :username", nativeQuery = true)
	public Usuario findByUsuario(@Param("username") String username);
	
	@Query("SELECT u FROM Usuario u JOIN FETCH u.rol WHERE u.username = :username AND u.clave = :clave")
	public Usuario findByUsuarioAndClave(@Param("username") String username, @Param("clave") String clave);
}

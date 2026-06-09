package com.cibertec.demo.entity;

import com.cibertec.demo.entity.Rol;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario {
	
	@Id
	@Column(name = "idusuario")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nombres;
	private String apellidos;
	
	@Column(name = "username") //Cambio
	private String username;

	private String clave;
	
	@ManyToOne
	@JoinColumn(name= "idrol")
	private Rol rol;
	
	public Usuario(String nombres, String apellidos,String username ,String clave, Rol rol) {
		this.nombres=nombres;
		this.apellidos=apellidos;
		this.username=username;
		this.clave=clave;
		this.rol=rol;	
	}
}

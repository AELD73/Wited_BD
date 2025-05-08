/**
 * 
 */
package mx.uam.azc.comunidad00.Wited.data;

import java.io.Serializable;

/**
 * 
 */
public class InstitucionDTO implements Serializable {
	
	
	private static final long serialVersionUID = 1L;

	private String id_institucion;
	private String nombre_institucion;
	private String direccion;
	
	
	
	public String getId_institucion() {
		return id_institucion;
	}
	public void setId_institucion(String id_institucion) {
		this.id_institucion = id_institucion;
	}
	public String getNombre_institucion() {
		return nombre_institucion;
	}
	public void setNombre_institucion(String nombre_institucion) {
		this.nombre_institucion = nombre_institucion;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
	
	
	



}

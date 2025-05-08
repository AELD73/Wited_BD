/**
 * 
 */
package mx.uam.azc.comunidad00.Wited.data;

import java.io.Serializable;

/**
 * 
 */
public class AlumnoDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String num_cuenta_alumno;
	private String nombre_completo;
	private String grado;
	private String id_institucion;
	public String getNum_cuenta_alumno() {
		return num_cuenta_alumno;
	}
	public void setNum_cuenta_alumno(String num_cuenta_alumno) {
		this.num_cuenta_alumno = num_cuenta_alumno;
	}
	public String getNombre_completo() {
		return nombre_completo;
	}
	public void setNombre_completo(String nombre_completo) {
		this.nombre_completo = nombre_completo;
	}
	public String getGrado() {
		return grado;
	}
	public void setGrado(String grado) {
		this.grado = grado;
	}
	public String getId_institucion() {
		return id_institucion;
	}
	public void setId_institucion(String id_institucion) {
		this.id_institucion = id_institucion;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	

}

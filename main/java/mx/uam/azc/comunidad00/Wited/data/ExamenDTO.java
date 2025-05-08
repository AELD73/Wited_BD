/**
 * 
 */
package mx.uam.azc.comunidad00.Wited.data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 */
public class ExamenDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private int id_examen;
	private String nombre_examen;
	private BigDecimal calificacion_examen;
	private String num_cuenta_alumno_examen;
	private String nombre_alumno;
	private String nombre_institucion;
	
	
	public int getId_examen() {
		return id_examen;
	}
	public void setId_examen(int id_examen) {
		this.id_examen = id_examen;
	}
	public String getNombre_examen() {
		return nombre_examen;
	}
	public void setNombre_examen(String nombre_examen) {
		this.nombre_examen = nombre_examen;
	}
	public BigDecimal getCalificacion_examen() {
		return calificacion_examen;
	}
	public void setCalificacion_examen(BigDecimal calificacion_examen) {
		this.calificacion_examen = calificacion_examen;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getNum_cuenta_alumno_examen() {
		return num_cuenta_alumno_examen;
	}
	public void setNum_cuenta_alumno_examen(String num_cuenta_alumno_examen) {
		this.num_cuenta_alumno_examen = num_cuenta_alumno_examen;
	}
	public String getNombre_alumno() {
		return nombre_alumno;
	}
	public void setNombre_alumno(String nombre_alumno) {
		this.nombre_alumno = nombre_alumno;
	}
	public String getNombre_institucion() {
		return nombre_institucion;
	}
	public void setNombre_institucion(String nombre_institucion) {
		this.nombre_institucion = nombre_institucion;
	}
	
	
	
	
	
	
	
	
	
	

}

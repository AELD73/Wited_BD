/**
 * 
 */
package mx.uam.azc.comunidad00.Wited.data;

import java.io.Serializable;

/**
 * 
 */
public class CursoDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String nombreCurso;
	private String gradoCurso;
	private String descripcionCurso;
	private String idCurso;
	
	public String getNombreCurso() {
		return nombreCurso;
	}
	public void setNombreCurso(String nombreCurso) {
		this.nombreCurso = nombreCurso;
	}
	public String getGradoCurso() {
		return gradoCurso;
	}
	public void setGradoCurso(String gradoCurso) {
		this.gradoCurso = gradoCurso;
	}
	public String getDescripcionCurso() {
		return descripcionCurso;
	}
	public void setDescripcionCurso(String descripcioCurso) {
		this.descripcionCurso = descripcioCurso;
	}
	public String getIdCurso() {
		return idCurso;
	}
	public void setIdCurso(String idCurso) {
		this.idCurso = idCurso;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

}

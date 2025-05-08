package mx.uam.azc.comunidad00.Wited.controller;

import java.awt.Cursor;
import java.awt.Taskbar.State;
import java.beans.Statement;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import mx.uam.azc.comunidad00.Wited.data.AlumnoDTO;
import mx.uam.azc.comunidad00.Wited.data.CursoDTO;

/**
 * Servlet implementation class UpdateServlet
 */
@WebServlet(name = "UpdateAlumno", urlPatterns = { "/UpdateAlumno" })
public class UpdateServletAlumno extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String accion = request.getParameter("accion");

		if (accion.equals("Modificar")) {
			try {
				updateAlumno(request, response);
			} catch (Exception e) {
				// TODO: handle exception
				throw new ServletException(e);
			}
		} else if (accion.equals("Borrar")) {
			try {
				deleteAlumno(request, response);
			} catch (Exception e) {
				// TODO: handle exception
				throw new ServletException(e);
			}
		} else if (accion.equals("Agregar")) {
			try {
				insertAlumno(request, response);
			} catch (Exception e) {
				// TODO: handle exception

				log("no se pudo insertar");
			}

		}

		String base = request.getContextPath();

		response.sendRedirect(base + "/alumno_update_form.jsp");

	}

	private void updateAlumno(Connection connection, AlumnoDTO alumno) throws Exception {

		String sql = "UPDATE alumno SET nombre_completo = ? WHERE num_cuenta_alumno = ?";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, alumno.getNombre_completo());
			statement.setString(2, alumno.getNum_cuenta_alumno());
			statement.executeUpdate();
		}
	}

	private void updateAlumno(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String numCuentaAlumno = request.getParameter("num_cuenta_alumno");
		String nombreCompleto = request.getParameter("nombre_completo");
		String gradoAlumno = request.getParameter("grado");
		String nombreInstitucion = request.getParameter("nombre_institucion");
		AlumnoDTO alumno = new AlumnoDTO();

		alumno.setNum_cuenta_alumno(numCuentaAlumno);
		alumno.setNombre_completo(nombreCompleto);
		alumno.setGrado(gradoAlumno);

		Context context = new InitialContext();
		DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

		Connection connection = source.getConnection();

		try {
			updateAlumno(connection, alumno);
		} finally {
			connection.close();
		}

	}

	private void deleteAlumno(Connection connection, AlumnoDTO alumno) throws Exception {

		String sql = "DELETE FROM alumno WHERE num_cuenta_alumno = ?;";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, alumno.getNum_cuenta_alumno());
			statement.executeUpdate();
		}
	}

	private void deleteAlumno(HttpServletRequest request, HttpServletResponse response) throws Exception {

		AlumnoDTO alumno = getAlumno_Eliminar(request);

		Context context = new InitialContext();
		DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

		Connection connection = source.getConnection();

		try {
			deleteAlumno(connection, alumno);
		} finally {
			connection.close();
		}

	}

	private AlumnoDTO getAlumno_Eliminar(HttpServletRequest request) {
		String idAlumno = request.getParameter("num_cuenta_alumno");
		String nombreAlumno = request.getParameter("nombre_completo");
		AlumnoDTO alumno = new AlumnoDTO();

		alumno.setNum_cuenta_alumno(idAlumno);
		alumno.setNombre_completo(nombreAlumno);

		return alumno;

	}

	private AlumnoDTO getAlumno_insert(HttpServletRequest request) {
		String idAlumno = request.getParameter("num_cuenta_alumno");
		String nombreAlumno = request.getParameter("nombre_completo");
		String grado = request.getParameter("grado");
		String idInstitucion = request.getParameter("nombre_institucion");

		AlumnoDTO alumno = new AlumnoDTO();

		alumno.setNum_cuenta_alumno(idAlumno);
		alumno.setNombre_completo(nombreAlumno);
		alumno.setGrado(grado);
		alumno.setId_institucion(idInstitucion);
		return alumno;

	}

	private void insertAlumno(Connection connection, AlumnoDTO alumno) throws Exception {
	    
	    String sql = "INSERT INTO alumno(num_cuenta_alumno, nombre_completo, grado, id_institucion) VALUES(?,?,?,?);";

	    try (PreparedStatement statement = connection.prepareStatement(sql)) {
	     
	        statement.setLong(1, Long.parseLong(alumno.getNum_cuenta_alumno())); 
	        statement.setString(2, alumno.getNombre_completo()); 
	        statement.setInt(3, Integer.parseInt(alumno.getGrado()));
	        statement.setInt(4, Integer.parseInt(alumno.getId_institucion())); 

	       
	        statement.executeUpdate();
	    } catch (NumberFormatException e) {
	       
	        throw new Exception("Error al convertir los valores de String a los tipos requeridos: " + e.getMessage(), e);
	    } catch (SQLException e) {
	       
	        throw new Exception("Error en la operación de inserción: " + e.getMessage(), e);
	    }
	}

	private void insertAlumno(HttpServletRequest request, HttpServletResponse response) throws Exception {
		AlumnoDTO alumno = getAlumno_insert(request);

		Context context = new InitialContext();
		DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

		Connection connection = source.getConnection();

		try {
			insertAlumno(connection, alumno);
		} finally {
			connection.close();
		}

	}

}

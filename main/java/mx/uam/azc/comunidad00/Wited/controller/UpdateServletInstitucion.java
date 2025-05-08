package mx.uam.azc.comunidad00.Wited.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


import mx.uam.azc.comunidad00.Wited.data.InstitucionDTO;

/**
 * Servlet implementation class UpdateServletInstitucion
 */
@WebServlet(name = "UpdateInstitucion", urlPatterns = { "/UpdateInstitucion" })
public class UpdateServletInstitucion extends HttpServlet {
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
				updateInstitucion(request, response);
			} catch (Exception e) {
				// TODO: handle exception
				throw new ServletException(e);
			}
		} else if (accion.equals("Borrar")) {
			try {
				deleteInstitucion(request, response);
			} catch (Exception e) {
				// TODO: handle exception
				throw new ServletException(e);
			}
		} else if (accion.equals("Agregar")) {
			try {
				insertInstitucion(request, response);
			} catch (Exception e) {
				// TODO: handle exception

				log("no se pudo insertar");
			}

		}

		String base = request.getContextPath();

		response.sendRedirect(base + "/institucion_update_form.jsp");

	}

	private void updateInstitucion(Connection connection, InstitucionDTO institucion) throws Exception {

		String sql = "UPDATE institucion SET nombre_institucion = ?, direccion = ? WHERE id_institucion = ?";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
		    statement.setString(1, institucion.getNombre_institucion()); // nombre_institucion
		    statement.setString(2, institucion.getDireccion());          // direccion
		    statement.setString(3, institucion.getId_institucion());     // id_institucion (en WHERE)
		    statement.executeUpdate();
		}
	}

	private void updateInstitucion(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String idInstitucion = request.getParameter("id_institucion");
		String nombreInstitucion = request.getParameter("nombre_institucion");
		String direccion = request.getParameter("direccion");
		
		InstitucionDTO institucion = new InstitucionDTO();

		institucion.setId_institucion(idInstitucion);
		institucion.setNombre_institucion(nombreInstitucion);
		institucion.setDireccion(direccion);

		Context context = new InitialContext();
		DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

		Connection connection = source.getConnection();

		try {
			updateInstitucion(connection, institucion);
		} finally {
			connection.close();
		}

	}

	private void deleteInstitucion(Connection connection, InstitucionDTO institucion) throws Exception {

		String sql = "DELETE FROM institucion WHERE id_institucion = ?;";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, institucion.getId_institucion());
			statement.executeUpdate();
		}
	}

	private void deleteInstitucion(HttpServletRequest request, HttpServletResponse response) throws Exception {

		InstitucionDTO institucion = getInstitucion_Eliminar(request);

		Context context = new InitialContext();
		DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

		Connection connection = source.getConnection();

		try {
			deleteInstitucion(connection, institucion);
		} finally {
			connection.close();
		}

	}

	private InstitucionDTO getInstitucion_Eliminar(HttpServletRequest request) {
		String idInstitucion = request.getParameter("id_institucion");
		String nombreInstitucion = request.getParameter("nombre_institucion");
		InstitucionDTO institucion = new InstitucionDTO();

		institucion.setId_institucion(idInstitucion);
		institucion.setNombre_institucion(nombreInstitucion);

		return institucion;

	}

	private InstitucionDTO getInstitucion_insert(HttpServletRequest request) {
		//String idInstitucion = request.getParameter("id_institucion");
		String nombreInstitucion = request.getParameter("nombre_institucion");
		String direccion = request.getParameter("direccion");

		InstitucionDTO institucion = new InstitucionDTO();
		
		//institucion.setId_institucion(idInstitucion);
		institucion.setNombre_institucion(nombreInstitucion);
		institucion.setDireccion(direccion);

		return institucion;

	}

	private void insertInstitucion(Connection connection, InstitucionDTO institucion) throws Exception {
	    
	    String sql = "INSERT INTO institucion( nombre_institucion, direccion) VALUES(?,?);";

	    try (PreparedStatement statement = connection.prepareStatement(sql)) {
	        statement.setString(1, institucion.getNombre_institucion());
	        statement.setString(2, institucion.getDireccion());
	        statement.executeUpdate();
	    } catch (SQLException e) {
	        throw new Exception("Error en la operación de inserción: " + e.getMessage(), e);
	    }
	}

	private void insertInstitucion(HttpServletRequest request, HttpServletResponse response) throws Exception {
		InstitucionDTO institucion = getInstitucion_insert(request);

		Context context = new InitialContext();
		DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

		Connection connection = source.getConnection();

		try {
			insertInstitucion(connection, institucion);
		} finally {
			connection.close();
		}

	}

}

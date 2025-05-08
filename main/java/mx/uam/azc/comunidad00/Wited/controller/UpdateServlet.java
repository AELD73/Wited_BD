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
@WebServlet(name = "Update", urlPatterns = { "/Update" })
public class UpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String accion = request.getParameter("accion");

		if (accion.equals("Modificar")) {
			try {
				updateCurso(request, response);
			} catch (Exception e) {
				// TODO: handle exception
				throw new ServletException(e);
			}
		} else if (accion.equals("Borrar")) {
			try {
				deleteCurso(request, response);
			} catch (Exception e) {
				// TODO: handle exception
				throw new ServletException(e);
			}
		} else if (accion.equals("Agregar")) {
			try {
				insertCurso(request, response);
			} catch (Exception e) {
				// TODO: handle exception

				log("no se pudo insertar");
			}

		}
		
		String base = request.getContextPath();
		
		response.sendRedirect(base + "/cursos_update_form.jsp");
	
	
	}
	
	
	private void updateCurso(Connection connection, CursoDTO curso) throws Exception {
	    
		String sql = "UPDATE curso SET nombre_curso = ?, grado_curso=?, descripcion_curso=? WHERE id_curso = ?";

	    
	    try (PreparedStatement statement = connection.prepareStatement(sql)) {
	        statement.setString(1, curso.getNombreCurso());
	        statement.setString(2, curso.getGradoCurso()); 
	        statement.setString(3, curso.getDescripcionCurso());
	        statement.setString(4, curso.getIdCurso());
	        
	        statement.executeUpdate();
	    }
	}

	
	private void updateCurso(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String idCurso = request.getParameter("id_curso");
		String nombreCurso = request.getParameter("nombre");
		String gradoCurso = request.getParameter("grado");
		String descripcionCurso = request.getParameter("descripcion");
		CursoDTO curso = new CursoDTO();
		curso.setIdCurso(idCurso);
		curso.setNombreCurso(nombreCurso);
		curso.setGradoCurso(gradoCurso);
		curso.setDescripcionCurso(descripcionCurso);
		
		
		Context context = new InitialContext();
		DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
		
		Connection connection = source.getConnection();
		
		try {
			updateCurso(connection, curso);
		}finally {
			connection.close();
		}
		
	}
	
	
	private void deleteCurso(Connection connection, CursoDTO curso) throws Exception {
	    String sql = "DELETE FROM curso WHERE id_curso = ?;";

	    try (PreparedStatement statement = connection.prepareStatement(sql)) {
	        statement.setInt(1, Integer.parseInt(curso.getIdCurso()));
	        statement.executeUpdate();
	    } catch (SQLException | NumberFormatException e) {
	        throw new Exception("Error al eliminar el curso: " + e.getMessage(), e);
	    }
	}


	private void deleteCurso(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    CursoDTO curso = getCurso_Eliminar(request);

	    Context context = new InitialContext();
	    DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

	    try (Connection connection = source.getConnection()) {
	        deleteCurso(connection, curso);
	    }
	}


	private CursoDTO getCurso_Eliminar(HttpServletRequest request) {
	    String idCurso = request.getParameter("id_curso");

	    CursoDTO curso = new CursoDTO();
	    curso.setIdCurso(idCurso);

	    return curso;
	}


	private CursoDTO getCurso_insert(HttpServletRequest request) {
	    String nombreCurso = request.getParameter("nombre_curso");
	    String gradoCurso = request.getParameter("grado_curso");
	    String descripcionCurso = request.getParameter("descripcion_curso");

	    CursoDTO curso = new CursoDTO();
	    curso.setNombreCurso(nombreCurso);
	    curso.setGradoCurso(gradoCurso);
	    curso.setDescripcionCurso(descripcionCurso);

	    return curso;
	}


	private void insertCurso(Connection connection, CursoDTO curso) throws Exception {
	    String sql = "INSERT INTO curso(nombre_curso, grado_curso, descripcion_curso) VALUES (?, ?, ?);";

	    try (PreparedStatement statement = connection.prepareStatement(sql)) {
	        statement.setString(1, curso.getNombreCurso());
	        statement.setInt(2, Integer.parseInt(curso.getGradoCurso()));
	        statement.setString(3, curso.getDescripcionCurso());

	        statement.executeUpdate();
	    } catch (NumberFormatException e) {
	        throw new Exception("Error al convertir el grado a entero: " + e.getMessage(), e);
	    } catch (SQLException e) {
	        throw new Exception("Error al insertar el curso: " + e.getMessage(), e);
	    }
	}


	private void insertCurso(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    CursoDTO curso = getCurso_insert(request);

	    Context context = new InitialContext();
	    DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

	    try (Connection connection = source.getConnection()) {
	        insertCurso(connection, curso);
	    }
	}

	

	

}

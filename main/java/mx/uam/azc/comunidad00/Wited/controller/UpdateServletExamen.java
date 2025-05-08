package mx.uam.azc.comunidad00.Wited.controller;

import java.awt.Cursor;
import java.awt.Taskbar.State;
import java.beans.Statement;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import mx.uam.azc.comunidad00.Wited.data.ExamenDTO;

/**
 * Servlet implementation class UpdateServlet
 */
@WebServlet(name = "UpdateExamen", urlPatterns = { "/UpdateExamen" })
public class UpdateServletExamen extends HttpServlet {
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
				updateCalificacion(request, response);
			} catch (Exception e) {
				// TODO: handle exception
				throw new ServletException(e);
			}
		} else if (accion.equals("Borrar")) {
			try {
				deleteExamen(request, response);
			} catch (Exception e) {
				// TODO: handle exception throw new

			}
		} else if (accion.equals("Agregar")) {
			try {
				insertExamen(request, response);
			} catch (Exception e) {
				// TODO: handle
				log("no se pudo insertar"); 

			}
		}

		String base = request.getContextPath();

		response.sendRedirect(base + "/examen_update_form.jsp");

	}

	private void updateCalificacion(Connection connection, ExamenDTO examen) throws Exception {
		String sql = """
				    UPDATE examen e
				    JOIN alumno_examen ea ON e.id_examen = ea.id_examen
				    SET e.calificacion_examen = ?
				    WHERE ea.num_cuenta_alumno = ?;
				""";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setBigDecimal(1, examen.getCalificacion_examen());
			statement.setString(2, examen.getNum_cuenta_alumno_examen());
			statement.executeUpdate();
		}
	}

	private void updateCalificacion(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String numCuenta = request.getParameter("num_cuenta_alumno");
		String nuevaCalificacion = request.getParameter("calificacion");


		if (numCuenta != null && nuevaCalificacion != null) {
		
			ExamenDTO examen = new ExamenDTO();
			examen.setNum_cuenta_alumno_examen(numCuenta);
			examen.setCalificacion_examen(new BigDecimal(nuevaCalificacion));

			
			Context context = new InitialContext();
			DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

			try (Connection connection = source.getConnection()) {
		
				updateCalificacion(connection, examen);
			} catch (Exception e) {
				e.printStackTrace();
			
			}
		}
	}

	private void deleteExamen(Connection connection, ExamenDTO examen) throws Exception {

		String sql = """
				DELETE e
				FROM examen e
				JOIN alumno_examen ae ON e.id_examen = ae.id_examen
				WHERE ae.num_cuenta_alumno = ?;
				""";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, examen.getNum_cuenta_alumno_examen());
			statement.executeUpdate();
		}
	}

	private void deleteExamen(HttpServletRequest request, HttpServletResponse response) throws Exception {

		ExamenDTO alumno = getExamen_Eliminar(request);

		Context context = new InitialContext();
		DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

		Connection connection = source.getConnection();

		try {
			deleteExamen(connection, alumno);
		} finally {
			connection.close();
		}

	}

	private ExamenDTO getExamen_Eliminar(HttpServletRequest request) {
		String idAlumno = request.getParameter("num_cuenta_alumno");

		ExamenDTO alumno = new ExamenDTO();

		alumno.setNum_cuenta_alumno_examen(idAlumno);

		return alumno;

	}
	
	private ExamenDTO getExamen_insert(HttpServletRequest request) {
	    String idAlumno = request.getParameter("num_cuenta_alumno");
	
	    String nombreExamen = request.getParameter("nombre_examen");
	    String calificacionExamen = request.getParameter("calificacion_examen");

	    ExamenDTO examen = new ExamenDTO();

	    examen.setNum_cuenta_alumno_examen(idAlumno);  
	    examen.setNombre_examen(nombreExamen); 
	    examen.setCalificacion_examen(new BigDecimal(calificacionExamen));  

	    return examen;
	}

	private void insertExamen(Connection connection, ExamenDTO examen) throws Exception {
	   
	    String sqlExamen = "INSERT INTO examen (nombre_examen, calificacion_examen) VALUES (?, ?)";
	    try (PreparedStatement stmtExamen = connection.prepareStatement(sqlExamen)) {
	        stmtExamen.setString(1, examen.getNombre_examen());
	        stmtExamen.setBigDecimal(2, examen.getCalificacion_examen());
	        stmtExamen.executeUpdate();  
	    }

	   
	    String sqlIdExamen = "SELECT LAST_INSERT_ID()";
	    try (java.sql.Statement stmt = connection.createStatement()) {
	        ResultSet rs = stmt.executeQuery(sqlIdExamen);
	        if (rs.next()) {
	            int idExamen = rs.getInt(1); 
	            System.out.println("ID Examen generado: " + idExamen);  
	            
	            String sqlRelacional = "INSERT INTO alumno_examen (num_cuenta_alumno, id_examen) VALUES (?, ?)";
	            try (PreparedStatement stmtRelacional = connection.prepareStatement(sqlRelacional)) {
	                stmtRelacional.setString(1, examen.getNum_cuenta_alumno_examen());
	                stmtRelacional.setInt(2, idExamen);
	                stmtRelacional.executeUpdate();
	            }
	        }
	    }
	}


	private void insertExamen(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    
	    ExamenDTO examen = getExamen_insert(request);

	   
	    Context context = new InitialContext();
	    DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

	    try (Connection connection = source.getConnection()) {
	       
	        insertExamen(connection, examen);
	    } catch (SQLException e) {
	        e.printStackTrace();
	       
	    }
	}
}

package mx.uam.azc.comunidad00.Wited.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // ✅ Importación correcta
import java.util.ArrayList;
import java.util.List; // ✅ Importación correcta

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
 * Servlet implementation class UpdateFormServlet
 */
@WebServlet(name = "AlumnoForm", urlPatterns = { "/AlumnoForm" })
public class AlumnoFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AlumnoFormServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		long key = Long.parseLong(request.getParameter("llave"));


		try {
			List<AlumnoDTO> alumnos=  getAlumnos(key);
			request.setAttribute("alumno", alumnos);
			
			
		}catch (Exception e) {
			// TODO: handle exception
			throw new ServletException(e);
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("/alumno_view.jsp");
        dispatcher.forward(request, response);
	}
	
	private List<AlumnoDTO> getAlumnos(long key) throws NamingException, SQLException{
		Context context = new InitialContext();
		DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
		Connection connection = source.getConnection();

		try {
			return getAlumnos(connection, key);
		} finally {
			connection.close();
		}
	}



	private List<AlumnoDTO> getAlumnos(Connection connection, long key) throws SQLException {
		String sql = "SELECT a.num_cuenta_alumno, a.nombre_completo, a.grado, i.nombre_institucion FROM alumno a JOIN institucion i ON i.id_institucion = a.id_institucion WHERE a.num_cuenta_alumno = ?;";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setLong(1, key);
			try (ResultSet cursor = statement.executeQuery()) {
				List<AlumnoDTO> alumnos = new ArrayList<>();
				while (cursor.next()) {
					
					AlumnoDTO alumno = new AlumnoDTO();
					alumno.setNum_cuenta_alumno(cursor.getString(1));
					alumno.setNombre_completo(cursor.getString(2));
					alumno.setGrado(cursor.getString(3));
					alumno.setId_institucion(cursor.getString(4));
					
					
					alumnos.add(alumno);
				}
				return alumnos;
			}
		}
	}
	
}

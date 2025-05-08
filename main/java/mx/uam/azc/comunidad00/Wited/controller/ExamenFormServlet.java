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

import mx.uam.azc.comunidad00.Wited.data.ExamenDTO;
import mx.uam.azc.comunidad00.Wited.data.CursoDTO;

/**
 * Servlet implementation class UpdateFormServlet
 */
@WebServlet(name = "ExamenForm", urlPatterns = { "/ExamenForm" })
public class ExamenFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ExamenFormServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		long key = Long.parseLong(request.getParameter("llave"));


		try {
			List<ExamenDTO> examenes=  getExamenes(key);
			request.setAttribute("examen", examenes);
			
			
		}catch (Exception e) {
			// TODO: handle exception
			throw new ServletException(e);
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("/examen_view.jsp");
        dispatcher.forward(request, response);
	}
	
	private List<ExamenDTO> getExamenes(long key) throws NamingException, SQLException{
		Context context = new InitialContext();
		DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
		Connection connection = source.getConnection();

		try {
			return getExamenes(connection, key);
		} finally {
			connection.close();
		}
	}



	private List<ExamenDTO> getExamenes(Connection connection, long key) throws SQLException {
		String sql = "SELECT a.num_cuenta_alumno,a.nombre_completo,i.nombre_institucion,e.nombre_examen, e.calificacion_examen FROM alumno a JOIN institucion i ON a.id_institucion = i.id_institucion JOIN alumno_examen ae ON a.num_cuenta_alumno = ae.num_cuenta_alumno JOIN examen e ON ae.id_examen = e.id_examen WHERE a.num_cuenta_alumno = ?;";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setLong(1, key);
			try (ResultSet cursor = statement.executeQuery()) {
				List<ExamenDTO> examenes = new ArrayList<>();
				while (cursor.next()) {
					
					ExamenDTO examen = new ExamenDTO();
					
					examen.setNum_cuenta_alumno_examen(cursor.getString(1));
					examen.setNombre_alumno(cursor.getString(2));
					examen.setNombre_institucion(cursor.getString(3));
					examen.setNombre_examen(cursor.getString(4));
					examen.setCalificacion_examen(cursor.getBigDecimal(5));

					examenes.add(examen);
				}
				return examenes;
			}
		}
	}
	
}

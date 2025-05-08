package mx.uam.azc.comunidad00.Wited.controller;

import java.io.IOException;
import java.sql.Connection;
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

import mx.uam.azc.comunidad00.Wited.data.CursoDTO;

/**
 * Servlet implementation class UpdateFormServlet
 */
@WebServlet(name = "UpdateForm", urlPatterns = { "/UpdateForm" })
public class UpdateFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UpdateFormServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			List<CursoDTO> cursos = getCurso();
			request.setAttribute("Curso", cursos);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}

		RequestDispatcher dispatcher = request.getRequestDispatcher("/cursos_update_form.jsp");
		dispatcher.forward(request, response);
	}

	private List<CursoDTO> getCurso() throws NamingException, SQLException {
		Context context = new InitialContext();
		DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
		Connection connection = source.getConnection();

		try {
			return getCurso(connection);
		} finally {
			connection.close();
		}
	}

	private List<CursoDTO> getCurso(Connection connection) throws SQLException {
		Statement statement = connection.createStatement();
		ResultSet cursor = statement.executeQuery("SELECT nombre_curso, grado_curso, descripcion_curso FROM curso;");

		try {
			List<CursoDTO> cursos = new ArrayList<>();

			while (cursor.next()) {
				CursoDTO curso = new CursoDTO();
				curso.setNombreCurso(cursor.getString(1));
				curso.setGradoCurso(cursor.getString(2));
				curso.setDescripcionCurso(cursor.getString(3));
				cursos.add(curso);
			}
			return cursos;

		} finally {
			cursor.close();
			statement.close(); 
		}
	}
}

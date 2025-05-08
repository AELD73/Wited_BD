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

import mx.uam.azc.comunidad00.Wited.data.CursoDTO;

/**
 * Servlet implementation class UpdateFormServlet
 */
@WebServlet(name = "CursoForm", urlPatterns = { "/CursoForm" })
public class CursoFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CursoFormServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int key = Integer.parseInt(request.getParameter("llave"));

		try {
			List<CursoDTO> cursos =  getCurso(key);
			request.setAttribute("cursos", cursos);
			
			
		}catch (Exception e) {
			// TODO: handle exception
			throw new ServletException(e);
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("/curso_view.jsp");
        dispatcher.forward(request, response);
	}
	
	private List<CursoDTO> getCurso(int key) throws NamingException, SQLException{
		Context context = new InitialContext();
		DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
		Connection connection = source.getConnection();

		try {
			return getCurso(connection, key);
		} finally {
			connection.close();
		}
	}



	private List<CursoDTO> getCurso(Connection connection, int key) throws SQLException {
		String sql = "SELECT id_curso, nombre_curso, grado_curso, descripcion_curso FROM curso WHERE id_curso = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, key);
			try (ResultSet cursor = statement.executeQuery()) {
				List<CursoDTO> cursos = new ArrayList<>();
				while (cursor.next()) {
					CursoDTO curso = new CursoDTO();
					curso.setIdCurso(cursor.getString(1));
					curso.setNombreCurso(cursor.getString(2));
					curso.setGradoCurso(cursor.getString(3));
					curso.setDescripcionCurso(cursor.getString(4));
					cursos.add(curso);
				}
				return cursos;
			}
		}
	}
}

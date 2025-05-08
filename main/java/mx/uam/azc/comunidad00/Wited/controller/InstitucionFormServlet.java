package mx.uam.azc.comunidad00.Wited.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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


import mx.uam.azc.comunidad00.Wited.data.InstitucionDTO;

/**
 * Servlet implementation class InstitucionFormServlet
 */
@WebServlet(name = "InstitucionForm", urlPatterns = { "/InstitucionForm" })
public class InstitucionFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public InstitucionFormServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		long key = Long.parseLong(request.getParameter("llave"));


		try {
			List<InstitucionDTO> instituciones=  getInstituciones(key);
			request.setAttribute("institucion", instituciones);
			
			
		}catch (Exception e) {
			// TODO: handle exception
			throw new ServletException(e);
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("/institucion_view.jsp");
        dispatcher.forward(request, response);
	}
	
	private List<InstitucionDTO> getInstituciones(long key) throws NamingException, SQLException{
		Context context = new InitialContext();
		DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
		Connection connection = source.getConnection();

		try {
			return getInstituciones(connection, key);
		} finally {
			connection.close();
		}
	}



	private List<InstitucionDTO> getInstituciones(Connection connection, long key) throws SQLException {
		
		String sql = """
			    SELECT
			        i.id_institucion,
			        i.nombre_institucion,
			        i.direccion
			    FROM institucion i
			    WHERE i.id_institucion = ?
			""";
		
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setLong(1, key);
			try (ResultSet cursor = statement.executeQuery()) {
				List<InstitucionDTO> instituciones = new ArrayList<>();
				while (cursor.next()) {
					
					InstitucionDTO institucion = new InstitucionDTO();
					institucion.setId_institucion(cursor.getString(1));
					institucion.setNombre_institucion(cursor.getString(2));
					institucion.setDireccion(cursor.getString(3));
					
					
					instituciones.add(institucion);
				}
				return instituciones;
			}
		}
	}
	
}
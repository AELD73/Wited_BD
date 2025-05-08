package mx.uam.azc.comunidad00.Wited.controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.InvalidPreferencesFormatException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;


import mx.uam.azc.comunidad00.Wited.data.InstitucionDTO;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

/**
 * Servlet implementation class InstitucionFormServlet
 */
@WebServlet(name = "InstitucionFormXls", urlPatterns = { "/InstitucionFormXls" })
public class InstitucionFormXlsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public InstitucionFormXlsServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		long key = Long.parseLong(request.getParameter("llave"));

		try {
			List<InstitucionDTO> instituciones = getInstitucion(key, response);
			request.setAttribute("institucion", instituciones);

		} catch (Exception e) {
			// TODO: handle exception
			throw new ServletException(e);
		}

	}

	private List<InstitucionDTO> getInstitucion(long key, HttpServletResponse response) throws NamingException, SQLException,
			IOException, ParsePropertyException, InvalidPreferencesFormatException {
		Context context = new InitialContext();
		DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
		Connection connection = source.getConnection();

		try {
			return getInstitucion(connection, key, response);
		} finally {
			connection.close();
		}
	}

	private List<InstitucionDTO> getInstitucion(Connection connection, long key, HttpServletResponse response)
			throws SQLException, IOException {
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
				Map<String, InstitucionDTO> beans = new HashMap<String, InstitucionDTO>();
				while (cursor.next()) {
					InstitucionDTO institucion = new InstitucionDTO();
					
					institucion.setId_institucion(cursor.getString(1));
					institucion.setNombre_institucion(cursor.getString(2));
					institucion.setDireccion(cursor.getString(3));

					instituciones.add(institucion);
					beans.put("institucion", institucion);
				}
				xlsShow(beans, response, key);
				System.out.println("documento creado");
				return instituciones;
			}
		}
	}

	public void xlsShow(Map<String, InstitucionDTO> beans, HttpServletResponse response, long key) throws IOException {
		ServletContext context = getServletContext();
		InputStream istream = context.getResourceAsStream("/WEB-INF/templates/PlantillaInstitucion.xls");
		XLSTransformer transformer = new XLSTransformer();
		HSSFWorkbook workbook = transformer.transformXLS(istream, beans);
		response.setContentType("application/msexcel");
		response.addHeader("Content-Disposition", "attachment; filename=ReporteInstitucion_" + key + ".xls");
		ServletOutputStream os = response.getOutputStream();
		workbook.write(os);
		os.flush();
	}

}

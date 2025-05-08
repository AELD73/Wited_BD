package mx.uam.azc.comunidad00.Wited.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Pipe.SourceChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // ✅ Importación correcta
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List; // ✅ Importación correcta
import java.util.Map;
import java.util.prefs.InvalidPreferencesFormatException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.print.attribute.HashAttributeSet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import mx.uam.azc.comunidad00.Wited.data.AlumnoDTO;
import mx.uam.azc.comunidad00.Wited.data.CursoDTO;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

/**
 * Servlet implementation class UpdateFormServlet
 */
@WebServlet(name = "AlumnoFormXls", urlPatterns = { "/AlumnoFormXls" })
public class AlumnoFormXlsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AlumnoFormXlsServlet() {
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
			List<AlumnoDTO> alumnos = getAlumno(key, response);
			request.setAttribute("alumno", alumnos);

		} catch (Exception e) {
			// TODO: handle exception
			throw new ServletException(e);
		}

	}

	private List<AlumnoDTO> getAlumno(long key, HttpServletResponse response) throws NamingException, SQLException,
			IOException, ParsePropertyException, InvalidPreferencesFormatException {
		Context context = new InitialContext();
		DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
		Connection connection = source.getConnection();

		try {
			return getAlumno(connection, key, response);
		} finally {
			connection.close();
		}
	}

	private List<AlumnoDTO> getAlumno(Connection connection, long key, HttpServletResponse response)
			throws SQLException, IOException {
		String sql = "SELECT a.num_cuenta_alumno, a.nombre_completo, a.grado, i.nombre_institucion FROM alumno a JOIN institucion i ON i.id_institucion = a.id_institucion WHERE a.num_cuenta_alumno = ?;";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setLong(1, key);
			try (ResultSet cursor = statement.executeQuery()) {
				List<AlumnoDTO> alumnos = new ArrayList<>();
				Map<String, AlumnoDTO> beans = new HashMap<String, AlumnoDTO>();
				while (cursor.next()) {
					AlumnoDTO alumno = new AlumnoDTO();
					alumno.setNum_cuenta_alumno(cursor.getString(1));
					alumno.setNombre_completo(cursor.getString(2));
					alumno.setGrado(cursor.getString(3));
					alumno.setId_institucion(cursor.getString(4));

					alumnos.add(alumno);
					beans.put("alumno", alumno);
				}
				xlsShow(beans, response, key);
				System.out.println("doumneto creado");
				return alumnos;
			}
		}
	}

	public void xlsShow(Map<String, AlumnoDTO> beans, HttpServletResponse response, long key) throws IOException {
		ServletContext context = getServletContext();
		InputStream istream = context.getResourceAsStream("/WEB-INF/templates/PlantillaAlumno.xls");
		XLSTransformer transformer = new XLSTransformer();
		HSSFWorkbook workbook = transformer.transformXLS(istream, beans);
		response.setContentType("application/msexcel");
		response.addHeader("Content-Disposition", "attachment; filename=ReporteAlumno_" + key + ".xls");
		ServletOutputStream os = response.getOutputStream();
		workbook.write(os);
		os.flush();
	}

}

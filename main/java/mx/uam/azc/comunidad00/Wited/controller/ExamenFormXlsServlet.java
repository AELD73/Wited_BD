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

import mx.uam.azc.comunidad00.Wited.data.ExamenDTO;
import mx.uam.azc.comunidad00.Wited.data.CursoDTO;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

/**
 * Servlet implementation class UpdateFormServlet
 */
@WebServlet(name = "ExamenFormXls", urlPatterns = { "/ExamenFormXls" })
public class ExamenFormXlsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ExamenFormXlsServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		long key = Long.parseLong(request.getParameter("llave"));

		try {
			List<ExamenDTO> examenes = getExamen(key, response);
			request.setAttribute("examen", examenes);

		} catch (Exception e) {
			// TODO: handle exception
			throw new ServletException(e);
		}

	}

	private List<ExamenDTO> getExamen(long key, HttpServletResponse response) throws NamingException, SQLException,
			IOException, ParsePropertyException, InvalidPreferencesFormatException {
		Context context = new InitialContext();
		DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
		Connection connection = source.getConnection();

		try {
			return getExamen(connection, key, response);
		} finally {
			connection.close();
		}
	}

	private List<ExamenDTO> getExamen(Connection connection, long key, HttpServletResponse response)
			throws SQLException, IOException {
		String sql = "SELECT a.num_cuenta_alumno,a.nombre_completo,i.nombre_institucion,e.nombre_examen, e.calificacion_examen FROM alumno a JOIN institucion i ON a.id_institucion = i.id_institucion JOIN alumno_examen ae ON a.num_cuenta_alumno = ae.num_cuenta_alumno JOIN examen e ON ae.id_examen = e.id_examen WHERE a.num_cuenta_alumno= ?;";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setLong(1, key);
			try (ResultSet cursor = statement.executeQuery()) {
				List<ExamenDTO> examenes = new ArrayList<>();
				Map<String, ExamenDTO> beans = new HashMap<String, ExamenDTO>();
				while (cursor.next()) {
					ExamenDTO examen = new ExamenDTO();
					
					examen.setNum_cuenta_alumno_examen(cursor.getString(1));
					examen.setNombre_alumno(cursor.getString(2));
					examen.setNombre_institucion(cursor.getString(3));
					examen.setNombre_examen(cursor.getString(4));
					examen.setCalificacion_examen(cursor.getBigDecimal(5));
					
					examenes.add(examen);
					beans.put("examen", examen);
				}
				xlsShow(beans, response, key);
				System.out.println("doumneto creado");
				return examenes;
			}
		}
	}

	public void xlsShow(Map<String, ExamenDTO> beans, HttpServletResponse response, long key) throws IOException {
		ServletContext context = getServletContext();
		InputStream istream = context.getResourceAsStream("/WEB-INF/templates/PlantillaCalificaciones.xls");
		XLSTransformer transformer = new XLSTransformer();
		HSSFWorkbook workbook = transformer.transformXLS(istream, beans);
		response.setContentType("application/msexcel");
		response.addHeader("Content-Disposition", "attachment; filename=CalificacionesExamenes_" + key + ".xls");
		ServletOutputStream os = response.getOutputStream();
		workbook.write(os);
		os.flush();
	}

}

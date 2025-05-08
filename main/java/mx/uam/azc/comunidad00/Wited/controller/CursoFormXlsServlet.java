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

import mx.uam.azc.comunidad00.Wited.data.CursoDTO;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

/**
 * Servlet implementation class UpdateFormServlet
 */
@WebServlet(name = "CursoFormXls", urlPatterns = { "/CursoFormXls" })
public class CursoFormXlsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CursoFormXlsServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int key = Integer.parseInt(request.getParameter("llave"));
		RequestDispatcher dispatcher = request.getRequestDispatcher("/curso_search.jsp");
		dispatcher.forward(request, response);

		try {
			List<CursoDTO> cursos = getCurso(key, response);
			request.setAttribute("cursos", cursos);

		} catch (Exception e) {
			// TODO: handle exception
			throw new ServletException(e);
		}
		
	}

	private List<CursoDTO> getCurso(int key, HttpServletResponse response) throws NamingException, SQLException,
			IOException, ParsePropertyException, InvalidPreferencesFormatException {
		Context context = new InitialContext();
		DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
		Connection connection = source.getConnection();

		try {
			return getCurso(connection, key, response);
		} finally {
			connection.close();
		}
	}

	private List<CursoDTO> getCurso(Connection connection, int key, HttpServletResponse response) throws SQLException, IOException {
		String sql = "SELECT id_curso, nombre_curso, grado_curso, descripcion_curso FROM curso WHERE id_curso = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, key);
			try (ResultSet cursor = statement.executeQuery()) {
				List<CursoDTO> cursos = new ArrayList<>();
				Map<String, CursoDTO> beans = new HashMap<String, CursoDTO>();
				while (cursor.next()) {
					CursoDTO curso = new CursoDTO();
					curso.setIdCurso(cursor.getString(1));
					curso.setNombreCurso(cursor.getString(2));
					curso.setGradoCurso(cursor.getString(3));
					curso.setDescripcionCurso(cursor.getString(4));
					cursos.add(curso);
					beans.put("cursos", curso);
				}
				xlsShow(beans, response, key);
				System.out.println("doumneto creado");
				return cursos;
			}
		}
	}

	public void xlsShow( Map<String, CursoDTO> beans, HttpServletResponse response, int key ) throws IOException{
			ServletContext context = getServletContext();
			InputStream istream = context.getResourceAsStream( "/WEB-INF/templates/PlantillaCursos.xls" );
			XLSTransformer transformer = new XLSTransformer ();
			HSSFWorkbook workbook = transformer. transformXLS( istream, beans );
			response. setContentType( "application/msexcel" );
			response. addHeader ( "Content-Disposition",
			"attachment; filename=ReporteCliente" + key + ".xls" );
			ServletOutputStream os = response.getOutputStream();
			workbook.write( os );
			os.flush();
			}
	
	
}

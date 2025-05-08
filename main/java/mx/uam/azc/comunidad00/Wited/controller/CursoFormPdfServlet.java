package mx.uam.azc.comunidad00.Wited.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
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

import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

import mx.uam.azc.comunidad00.Wited.data.CursoDTO;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

/**
 * Servlet implementation class UpdateFormServlet
 */
@WebServlet(name = "CursoFormPdf", urlPatterns = { "/CursoFormPdf" })
public class CursoFormPdfServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CursoFormPdfServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int key = Integer.parseInt(request.getParameter("llave"));
		

		try {
			List<CursoDTO> cursos = getCurso(key, response);
			request.setAttribute("cursos", cursos);

		} catch (Exception e) {
			// TODO: handle exception
			throw new ServletException(e);
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("/curso_search.jsp");
		dispatcher.forward(request, response);

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

	private List<CursoDTO> getCurso(Connection connection, int key, HttpServletResponse response)
			throws SQLException, IOException {
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
				documentShow(cursos, response, key);
				System.out.println("doumneto creado");
				return cursos;
			}
		}
	}
	public void documentShow(List<CursoDTO> cursos, HttpServletResponse response, int key) throws IOException {
		try {
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment;filename=ReporteCliente" + key + ".pdf");

			ServletOutputStream fos = response.getOutputStream();
			Document document = new Document(PageSize.LETTER.rotate());
			PdfWriter.getInstance(document, fos);

			document.addTitle("Detalles del Curso");
			document.addAuthor("Linternas Negras");
			document.addCreationDate();
			document.addSubject("Cursos");
			document.addCreator("iText");
			document.open();

			com.lowagie.text.Font titleFont = FontFactory.getFont(FontFactory.COURIER, 18, Font.BOLD, new Color(0, 0, 18));
			com.lowagie.text.Font headerFont = FontFactory.getFont(FontFactory.COURIER, 8, Font.BOLD, new Color(64, 64, 255));
			com.lowagie.text.Font dataFont = FontFactory.getFont(FontFactory.COURIER, 8, Font.BOLD, new Color(0, 128, 0));

			document.add(new Phrase("Curso\n\n", titleFont));

			Table headerTable = new Table(4);
			headerTable.setBorderWidth(3);
			headerTable.setBorderColor(new Color(0, 0, 255));
			headerTable.setBackgroundColor(new Color(226, 222, 222));
			headerTable.setPadding(5);
			headerTable.setSpacing(5);

			headerTable.addCell(new Phrase("Id Curso", headerFont));
			headerTable.addCell(new Phrase("Nombre del Curso", headerFont));
			headerTable.addCell(new Phrase("Grado de Curso", headerFont));
			headerTable.addCell(new Phrase("Descripción del Curso", headerFont));

			document.add(headerTable);

			Table dataTable = new Table(4);
			dataTable.setPadding(5);

			for (CursoDTO curso : cursos) {
				dataTable.addCell(new Phrase(curso.getIdCurso(), dataFont));
				dataTable.addCell(new Phrase(curso.getNombreCurso(), dataFont));
				dataTable.addCell(new Phrase(curso.getGradoCurso(), dataFont));
				dataTable.addCell(new Phrase(curso.getDescripcionCurso(), dataFont));
			}

			document.add(dataTable);
			fos.flush();
			document.close();

		} catch (IOException | DocumentException e) {
			e.printStackTrace();
		}
	}

	
	
	
}


	

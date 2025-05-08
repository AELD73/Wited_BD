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
import com.lowagie.text.pdf.PRAcroForm;
import com.lowagie.text.pdf.PdfWriter;

import mx.uam.azc.comunidad00.Wited.data.AlumnoDTO;
import mx.uam.azc.comunidad00.Wited.data.CursoDTO;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

/**
 * Servlet implementation class UpdateFormServlet
 */
@WebServlet(name = "AlumnoFormPdf", urlPatterns = { "/AlumnoFormPdf" })
public class AlumnoFormPdfServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AlumnoFormPdfServlet() {
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
		RequestDispatcher dispatcher = request.getRequestDispatcher("/alumno_search.jsp");
		dispatcher.forward(request, response);

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

		String sql = """
					SELECT
				    a.num_cuenta_alumno,
				    a.nombre_completo,
				    a.grado,
				    i.nombre_institucion,
				    i.direccion,
				    c.nombre_curso,
				    c.descripcion_curso,
				    c.grado_curso,
				    e.nombre_examen,
				    e.calificacion_examen
				FROM alumno a
				JOIN institucion i ON a.id_institucion = i.id_institucion
				LEFT JOIN alumno_curso ac ON a.num_cuenta_alumno = ac.num_cuenta_alumno
				LEFT JOIN curso c ON ac.id_curso = c.id_curso
				LEFT JOIN alumno_examen ae ON a.num_cuenta_alumno = ae.num_cuenta_alumno
				LEFT JOIN examen e ON ae.id_examen = e.id_examen
				WHERE a.num_cuenta_alumno = ?;
					    """;

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setLong(1, key);
			try (ResultSet cursor = statement.executeQuery()) {
				List<AlumnoDTO> alumnos = new ArrayList<>();
				Map<String, String> extraData = new HashMap<>();

				if (cursor.next()) {
					AlumnoDTO alumno = new AlumnoDTO();
					alumno.setNum_cuenta_alumno(cursor.getString(1));
					alumno.setNombre_completo(cursor.getString(2));
					alumno.setGrado(cursor.getString(3));
					alumno.setId_institucion(cursor.getString(4)); // solo guardamos el nombre aquí

					extraData.put("direccion", cursor.getString(5));
					extraData.put("curso", cursor.getString(6));
					extraData.put("descripcionCurso", cursor.getString(7));
					extraData.put("gradoCurso", cursor.getString(8));
					
					extraData.put("nombreExamen", cursor.getString(9));
					extraData.put("calificacion", cursor.getString(10));
					
					

					alumnos.add(alumno);
				}

				// Ahora pasa también extraData
				documentShow(alumnos, response, key, extraData);
				return alumnos;
			}
		}
	}

	public void documentShow(List<AlumnoDTO> alumnos, HttpServletResponse response, long key,
			Map<String, String> extraData) throws IOException {
		try {
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment;filename=AvanceAcademico_" + key + ".pdf");

			ServletOutputStream fos = response.getOutputStream();
			Document document = new Document();
			PdfWriter.getInstance(document, fos);

			document.open();

			com.lowagie.text.Font titleFont = FontFactory.getFont(FontFactory.COURIER, 18, Font.BOLD,
					new Color(0, 0, 18));
			com.lowagie.text.Font headerFont = FontFactory.getFont(FontFactory.COURIER, 8, Font.BOLD,
					new Color(64, 64, 255));
			com.lowagie.text.Font dataFont = FontFactory.getFont(FontFactory.COURIER, 8, Font.BOLD,
					new Color(0, 128, 0));

			document.add(new Phrase("Avance Académico\n\n\n", titleFont));
			document.add(new Phrase("Datos Alumno:\n\n", titleFont));

			Table headerTable = new Table(5);
			
			headerTable.setPadding(5);

			headerTable.addCell(new Phrase("Matrícula", headerFont));
			headerTable.addCell(new Phrase("Nombre", headerFont));
			headerTable.addCell(new Phrase("Grado", headerFont));
			headerTable.addCell(new Phrase("Institución", headerFont));
			headerTable.addCell(new Phrase("Dirección", headerFont));

			for (AlumnoDTO alumno : alumnos) {
				headerTable.addCell(new Phrase(alumno.getNum_cuenta_alumno(), dataFont));
				headerTable.addCell(new Phrase(alumno.getNombre_completo(), dataFont));
				headerTable.addCell(new Phrase(alumno.getGrado(), dataFont));
				headerTable.addCell(new Phrase(alumno.getId_institucion(), dataFont)); // contiene el nombre
				headerTable.addCell(new Phrase(extraData.get("direccion"), dataFont));
				document.add(headerTable);
				document.add(new Phrase("Materias Cursadas: \n", titleFont));

				
				// Otra tabla solo para curso
				Table cursoTable = new Table(3);
				cursoTable.setPadding(5);
				cursoTable.addCell(new Phrase("Curso", headerFont));
				cursoTable.addCell(new Phrase("Descripción", headerFont));
				cursoTable.addCell(new Phrase("Grado", headerFont));
				cursoTable.addCell(new Phrase(extraData.get("curso"), dataFont));
				cursoTable.addCell(new Phrase(extraData.get("descripcionCurso"), dataFont));
				cursoTable.addCell(new Phrase(extraData.get("gradoCurso"), dataFont));
				document.add(cursoTable);
				document.add(new Phrase("Calificacion Examen Final: \n", titleFont));
				Table examenTable = new Table(2);
				
				examenTable.setPadding(5);
				examenTable.addCell(new Phrase("Examen", headerFont));
				examenTable.addCell(new Phrase("Calificación", headerFont));
				
				examenTable.addCell(new Phrase(extraData.get("nombreExamen"), dataFont));
				examenTable.addCell(new Phrase(extraData.get("calificacion"), dataFont));
				document.add(examenTable);
			}
			
			document.close();
			fos.flush();

		} catch (IOException | DocumentException e) {
			e.printStackTrace();
		}
	}

}

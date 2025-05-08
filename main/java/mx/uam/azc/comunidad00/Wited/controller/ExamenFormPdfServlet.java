package mx.uam.azc.comunidad00.Wited.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
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
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PRAcroForm;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

import mx.uam.azc.comunidad00.Wited.data.ExamenDTO;
import mx.uam.azc.comunidad00.Wited.data.CursoDTO;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

/**
 * Servlet implementation class UpdateFormServlet
 */
@WebServlet(name = "ExamenFormPdf", urlPatterns = { "/ExamenFormPdf" })
public class ExamenFormPdfServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ExamenFormPdfServlet() {
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
			List<ExamenDTO> examenes = getExamen(key, response);
			request.setAttribute("examen", examenes);

		} catch (Exception e) {
			// TODO: handle exception
			throw new ServletException(e);
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("/examen_search.jsp");
		dispatcher.forward(request, response);

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
				Map<String, String> extraData = new HashMap<>();

				if (cursor.next()) {
					ExamenDTO examen = new ExamenDTO();
					extraData.put("num_cuenta_alumno", cursor.getString(1));
					extraData.put("nombre_alumno", cursor.getString(2));
					extraData.put("nombre_institucion", cursor.getString(3));
					examen.setNombre_examen(cursor.getString(4));
					examen.setCalificacion_examen(cursor.getBigDecimal(5));
					examenes.add(examen);
				}
				// Ahora pasa también extraData
				documentShow(examenes, response, key, extraData);
				return examenes;
			}
		}
	}

	public void documentShow(List<ExamenDTO> examenes, HttpServletResponse response, long key,
			Map<String, String> extraData) throws IOException {
		try {
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment;filename=AvanceAcademico_" + key + ".pdf");

			ServletOutputStream fos = response.getOutputStream();
			Document document = new Document();

			PdfWriter.getInstance(document, fos);

			document.open();

			com.lowagie.text.Font titleFont = FontFactory.getFont(FontFactory.COURIER, 35, Font.BOLD);
			com.lowagie.text.Font headerFont = FontFactory.getFont(FontFactory.COURIER, 13, Font.BOLD);
			com.lowagie.text.Font dataFont = FontFactory.getFont(FontFactory.COURIER, 13, Font.BOLD);

			document.add(new Phrase("Avance Académico\n", titleFont));
			document.add(new Phrase("Matricula: ", headerFont));
			document.add(new Phrase(extraData.get("num_cuenta_alumno") + "\n", dataFont));
			document.add(new Phrase("Nombre Alumno: ", headerFont));
			document.add(new Phrase(extraData.get("nombre_alumno") + "\n", dataFont));
			document.add(new Phrase("Institución: ", headerFont));
			document.add(new Phrase(extraData.get("nombre_institucion") + "\n", dataFont));

			Table calificacionesTable = new Table(2);
			calificacionesTable.setPadding(5);

			calificacionesTable.addCell(new Phrase("Examen", headerFont));
			calificacionesTable.addCell(new Phrase("Calificación", headerFont));
			for (ExamenDTO examen : examenes) {

				calificacionesTable.addCell(new Phrase(examen.getNombre_examen(), dataFont));
				calificacionesTable.addCell(new Phrase(examen.getCalificacion_examen().toString(), dataFont));

			}

			document.add(calificacionesTable);
			document.close();
			fos.flush();

		} catch (IOException | DocumentException e) {
			e.printStackTrace();
		}

	}

}

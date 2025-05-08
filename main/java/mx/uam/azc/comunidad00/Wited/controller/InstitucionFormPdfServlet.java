package mx.uam.azc.comunidad00.Wited.controller;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
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
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;


import mx.uam.azc.comunidad00.Wited.data.InstitucionDTO;
import net.sf.jxls.exception.ParsePropertyException;

/**
 * Servlet implementation class InstitucionFormPdfServlet
 */
@WebServlet(name = "InstitucionFormPdf", urlPatterns = { "/InstitucionFormPdf" })
public class InstitucionFormPdfServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public InstitucionFormPdfServlet() {
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
		RequestDispatcher dispatcher = request.getRequestDispatcher("/institucion_search.jsp");
		dispatcher.forward(request, response);

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
				Map<String, String> extraData = new HashMap<>();

				if (cursor.next()) {
					InstitucionDTO institucion = new InstitucionDTO();
					institucion.setId_institucion(cursor.getString(1));
					institucion.setNombre_institucion(cursor.getString(2));
					institucion.setDireccion(cursor.getString(3));
						
					

					instituciones.add(institucion);
				}

				// Ahora pasa también extraData
				documentShow(instituciones, response, key, extraData);
				return instituciones;
			}
		}
	}

	public void documentShow(List<InstitucionDTO> instituciones, HttpServletResponse response, long key,
			Map<String, String> extraData) throws IOException {
		try {
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment;filename=Instituciones_" + key + ".pdf");

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

			document.add(new Phrase("Catalogo de instituciones\n\n\n", titleFont));
			document.add(new Phrase("Datos Institucion:\n\n", titleFont));

			Table headerTable = new Table(3);
			
			headerTable.setPadding(3);

			headerTable.addCell(new Phrase("ID", headerFont));
			headerTable.addCell(new Phrase("Nombre", headerFont));
			headerTable.addCell(new Phrase("Direccion", headerFont));


			for (InstitucionDTO institucion : instituciones) {
				
				headerTable.addCell(new Phrase(institucion.getId_institucion(), dataFont));
				headerTable.addCell(new Phrase(institucion.getNombre_institucion(), dataFont));
				headerTable.addCell(new Phrase(institucion.getDireccion(), dataFont));
				

				document.add(headerTable);


				
				
			}
			
			document.close();
			fos.flush();

		} catch (IOException | DocumentException e) {
			e.printStackTrace();
		}
	}

}
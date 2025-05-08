<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<html>
<head>
<meta http-equiv="content-type"
	content="text/html;
      charset=windows-1252">
<meta name="GENERATOR"
	content="SeaMonkey/2.40 [en] (Windows; 10;
      Intel(R) Core(TM) i7-4500U CPU @ 1.80GHz 2.40 GHz) [Composer]">
<meta name="Author" content="Equipo Linternas Negras">
<title>Generador de reportes de calificaciones</title>
<link rel="stylesheet"
	href="${ pageContext.request.contextPath }/css/style.css">
</head>
<body>
	<form method="get"
		action="${ pageContext.request.contextPath }/examen_view.jsp">
		<div class="step">Forma de Busqueda para Alumnos</div>
		<div class="instructions">Proporciona la infomración de los
			alumnos en forma de reportes</div>
		<br>
		<c:set var="pattern" value="${ param.pattern }" />
		<c:if test="${ param.pattern == null || pattern == '' }">
			<c:set var="pattern" value="%" />
		</c:if>
		<%--
              String pattern = request.getParameter( "pattern" );
                      if( pattern == null || pattern.equals( "" ) )
                                pattern = "%";
                      pageContext.setAttribute( "pattern", pattern );
        --%>
		<div class="tabla-examen">


			<table class="tabla-examen">
				<tr class="form">
					<td align="right">
						<div class="label">Matricula:</div>
					</td>
					<td><input name="pattern" size="30" value="${ pattern }"></td>
					<td><input type="submit" value="  Buscar  "
						class="boton_tabla"></td>
				</tr>
			</table>


		</div>

		<table border="0" width="100%" class="tabla-examen">
			<tr class="form">
				<td align="center">
					<div class="label">Matricula</div>
				</td>
				<td align="center">
					<div class="label">Nombre</div>
				</td>
				<td align="center">
					<div class="label">Institución</div>
				</td>
				<td align="center">
					<div class="label">Examen</div>
				</td>
				<td align="center">
					<div class="label">Calificación</div>
				</td>
				<td align="center">
					<div class="label">VER</div>
				</td>
				<td align="center">
					<div class="label">PDF</div>
				</td>
				<td align="center">
					<div class="label">XLS</div>
				</td>
				<td align="center">
					<div class="label">HTML</div>
				</td>
			</tr>
			<sql:query var="resultados" dataSource="jdbc/TestDS">
               SELECT a.num_cuenta_alumno,a.nombre_completo,i.nombre_institucion,e.nombre_examen, e.calificacion_examen FROM alumno a JOIN institucion i ON a.id_institucion = i.id_institucion JOIN alumno_examen ae ON a.num_cuenta_alumno = ae.num_cuenta_alumno JOIN examen e ON ae.id_examen = e.id_examen WHERE a.num_cuenta_alumno LIKE ?;
                <sql:param value="${ pattern }" />
			</sql:query>
			<c:forEach var="fila" items="${ resultados.rows }">
				<tr>
					<td align="center">${ fila.num_cuenta_alumno}</td>
					<td align="center">${ fila.nombre_completo }</td>
					<td align="center">${ fila.nombre_institucion}</td>
					<td align="center">${ fila.nombre_examen}</td>
					<td align="center">${ fila.calificacion_examen}</td>

					<td align="center"><input type="button" value="  Ver  "
						onclick="window.location='ExamenForm?llave=${fila.num_cuenta_alumno}'"
						class="boton_tabla"></td>

					<td align="center"><input type="button" value="  PDF  "
						onclick="window.location='ExamenFormPdf?llave=${fila.num_cuenta_alumno}'"
						class="boton_tabla"></td>

					<td align="center"><input type="button" value="   XLS  "
						onclick="window.location='ExamenFormXls?llave=${fila.num_cuenta_alumno}'"
						class="boton_tabla"></td>
					<td align="center"><input type="button" value="  HTML  "
						onclick="window.location='ExamenFormHTML?llave=${fila.num_cuenta_alumno}'"
						class="boton_tabla"></td>
				</tr>
			</c:forEach>
		</table>
		<br> <input type="button" value="  Regresar  "
			onclick="window.location='${ pageContext.request.contextPath }/main.jsp'" class="boton_regresar">
	</form>
	<br>



</body>
</html>
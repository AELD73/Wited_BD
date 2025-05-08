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
<title>Forma de Busqueda de Cursos</title>
<link rel="stylesheet" href="${ pageContext.request.contextPath }/css/style.css">
</head>
<body>
	<form method="get"
		action="${ pageContext.request.contextPath }/curso_view.jsp">
		<div class="step">Forma de Busqueda de Cursos</div>
		<div class="instructions">Proporciona la infomración de los cursos</div>
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
		<table border="1" cellpadding="10">
			<tr>
				<td align="center">
					<table>
						<tr class="form">
							<td align="right">
								<div class="label">Patren:</div>
							</td>
							<td><input name="pattern" size="10" value="${ pattern }"></td>
							<td><input type="submit" value="  Buscar  "></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<br>
		<table border="0" width="100%">
			<tr class="form">
				<td align="center">
					<div class="label">Id Curso</div>
				</td>
				<td align="center">
					<div class="label">Nombre Curso</div>
				</td>
				<td align="center">
					<div class="label">Grado Curso</div>
				</td>
				<td align="center">
					<div class="label">Descripción Curso</div>
				</td>
				<td align="center">
					<div class="label">Detalle</div>
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
                SELECT * FROM curso WHERE nombre_curso LIKE ?;
                <sql:param value="${ pattern }" /></sql:query>
			<c:forEach var="fila" items="${ resultados.rows }">
				<tr>
					<td align="center">${ fila.id_curso}</td>
					<td align="center">${ fila.nombre_curso }</td>
					<td align="center">${ fila.grado_curso}</td>
					<td align="center">${ fila.descripcion_curso}</td>
					
					<td align="center"><input type="button" value="  Ver  "
						onclick="window.location='CursoForm?llave=${fila.id_curso}'"></td>
						
					<td align="center"><input type="button"
						value="  PDF  " onclick="window.location='CursoFormPdf?llave=${fila.id_curso}'"></td>
						
					<td align="center"><input type="button"
						value="   XLS  " onclick="window.location='CursoFormXls?llave=${fila.id_curso}'"></td>
					<td align="center"><input type="button"
						value="  HTML  " onclick="window.location='CursoFormHTML?llave=${fila.id_curso}'"></td>
				</tr>
			</c:forEach>
		</table>
		<br> <input type="button" value="  Regresar  "
			onclick="window.location='${ pageContext.request.contextPath }/main.jsp'">
	</form>
	<br>



</body>
</html>
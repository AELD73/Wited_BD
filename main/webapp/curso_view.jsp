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
		<div class="step">Detalle Curso</div>
		<div class="instructions">Proporciona la infomración de los cursos</div>
		<br>
		
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
			</tr>
			
			<c:forEach var="fila" items="${ cursos }">
				<tr>
					<td align="center">${ fila.idCurso }</td>
					<td align="center">${ fila.nombreCurso }</td>
					<td align="center">${ fila.gradoCurso }</td>
					<td align="center">${ fila.descripcionCurso }</td>
					
					
				</tr>
			</c:forEach>
		</table>
		<br> <input type="button" value="  Regresar  "
			onclick="window.location='${ pageContext.request.contextPath }/curso_search.jsp'">
	</form>
	<br>



</body>
</html>
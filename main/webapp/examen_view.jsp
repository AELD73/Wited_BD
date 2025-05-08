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
<title>Examen</title>
<link rel="stylesheet" href="${ pageContext.request.contextPath }/css/style.css">
</head>
<body>
	<form method="get"
		action="${ pageContext.request.contextPath }/examen_view.jsp">
		<div class="step">Detalle Curso</div>
		<div class="instructions">Generador de reportes sobre listado de alumnos</div>
		<br>
		
		<br>
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
			</tr>
			
			<c:forEach var="fila" items="${ examen }">
				<tr>
					<td align="center">${ fila.num_cuenta_alumno_examen }</td>
					<td align="center">${ fila.nombre_alumno }</td>
					<td align="center">${ fila.nombre_institucion }</td>
					<td align="center">${ fila.nombre_examen }</td>
					<td align="center">${ fila.calificacion_examen }</td>
					
					
				</tr>
			</c:forEach>
		</table>
		<br> <input type="button" value="  Regresar  "
			onclick="window.location='${ pageContext.request.contextPath }/examen_search.jsp'" class="boton_regresar">
	</form>
	<br>



</body>
</html>
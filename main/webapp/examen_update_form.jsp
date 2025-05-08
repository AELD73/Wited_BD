
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
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
<meta name="Author" content="Hugo Pablo Leyva">
<title>Calificaciones</title>
<link rel="stylesheet"
	href="${ pageContext.request.contextPath }/css/style.css">
</head>
<body>
	<div class="step">Examenes</div>
	<div class="instructions">Actualiza los Campos que se Requieran
		Modificar</div>
	<br>
	<sql:query var="resultados" dataSource="jdbc/TestDS"
		sql="SELECT a.num_cuenta_alumno,a.nombre_completo,i.nombre_institucion,e.nombre_examen, e.calificacion_examen FROM alumno a JOIN institucion i ON a.id_institucion = i.id_institucion JOIN alumno_examen ae ON a.num_cuenta_alumno = ae.num_cuenta_alumno JOIN examen e ON ae.id_examen = e.id_examen;" />
	<c:forEach var="fila" items="${ resultados.rows }">
		<form method="post"
			action="${ pageContext.request.contextPath }/UpdateExamen"
			class="form-examen">
			<table class="tabla-examen">
				<tr class="form">
					<td align="center"><div class="label">Matrícula</div></td>
					<td align="center"><div class="label">Nombre Completo</div></td>
					<td align="center"><div class="label">Institución</div></td>
					<td align="center"><div class="label">Examen presentado</div></td>
					<td align="center"><div class="label">Calificación</div></td>
					<td></td>
				</tr>

				<tr>
					<td align="center" data-label="num_cuenta_alumno">${ fila.num_cuenta_alumno }
						<input type="hidden" name="num_cuenta_alumno"
						value="${ fila.num_cuenta_alumno }" />
					</td>

					<td align="center" data-label="Nombre Completo"><label>${ fila.nombre_completo }</label>
					</td>

					<td align="center" data-label="Institución"><label>${ fila.nombre_institucion }</label>
					</td>

					<td align="center" data-label="Examen presentado"><label>${ fila.nombre_examen }</label>
					</td>

					<td align="center" data-label="Calificacion"><input size="20"
						name="calificacion" value="${ fila.calificacion_examen }">
					</td>

					<td valign="bottom">
					<input type="submit" name="accion" value="Modificar" class="boton_tabla"> 
					</td>
					<td valign="bottom">
					<input type="submit" name="accion" value="Borrar" class="boton_tabla">
					</td>
					
				</tr>
			</table>
		</form>

	</c:forEach>
	<form method="post"
		action="${ pageContext.request.contextPath }/UpdateExamen" class="form-examen">
		<table class="tabla-examen">

			<tr class="form">
				<td align="center"><div class="label">Matrícula</div></td>
				<td align="center"><div class="label">Nombre Examen</div></td>
				<td align="center"><div class="label">Calificación</div></td>
			</tr>
			<tr>
				<td align="center"><input size="30" name="num_cuenta_alumno"></td>
				<td align="center"><input size="30" name="nombre_examen"></td>
				<td align="center"><input size="20" name="calificacion_examen"></td>


				<td valign="bottom"><input type="submit" name="accion"
					value="Agregar" class="boton_agregar"></td>
			</tr>
		</table>
	</form>
	<br>
</body>
</html>
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
<title>Alumnos</title>
<link rel="stylesheet"
	href="${ pageContext.request.contextPath }/css/style.css">
</head>
<body>
	<div class="step">Calificaciones de Examenes</div>
	<div class="instructions">Actualiza los Campos que se Requieran
		Modificar</div>
	<br>
	<sql:query var="resultados" dataSource="jdbc/TestDS"
		sql="SELECT a.num_cuenta_alumno, a.nombre_completo, a.grado, i.nombre_institucion FROM alumno a JOIN institucion i ON i.id_institucion = a.id_institucion;" />
	<c:forEach var="fila" items="${ resultados.rows }">
		<form method="post"
			action="${ pageContext.request.contextPath }/UpdateAlumno"
			class="form-examen">
			<table width="150%" class="tabla-examen">
				<tr class="form">
					<td align="center">
						<div class="label">Matricula</div>
					</td>
					<td align="center">
						<div class="label">Nombre Completo</div>
					</td>
					<td align="center">
						<div class="label">Grado</div>
					</td>
					<td align="center">
						<div class="label">Institución</div>
					</td>
					<td align="center">
						<div class="label">Modificar</div>
					</td>
					<td align="center">
						<div class="label">Borrar</div>
					</td>
				</tr>

				<tr>
					<td align="center">${ fila.num_cuenta_alumno }<input
						type="hidden" name="num_cuenta_alumno"
						value="${ fila.num_cuenta_alumno }">
					</td>
					<td align="center">
						<table border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td><input size="20" name="nombre_completo"
									value="${ fila.nombre_completo }"></td>
							</tr>

						</table>
					</td>
					<td>
						<table border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td><input size="20" name="grado" value="${ fila.grado }"></td>
							</tr>

						</table>

					</td>

					<td>

						<table border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td><input size="20" name="nombre_institucion"
									value="${ fila.nombre_institucion }"></td>
							</tr>

						</table>
					</td>


					<td valign="bottom"><input type="submit" name="accion"
						value="Modificar" class="boton_tabla"></td>
					<td valign="bottom"><input type="submit" name="accion"
						value="Borrar" class="boton_tabla"></td>
				</tr>
			</table>
		</form>
	</c:forEach>
	<form method="post"
		action="${ pageContext.request.contextPath }/UpdateAlumno"
		class="form-examen">
		<table width="100%" class="tabla-examen">


			<tr class="form">
				<td align="center"><div class="label">Matricula</div></td>
				<td align="center"><div class="label">Nombre Completo</div></td>
				<td align="center"><div class="label">Grado</div></td>
				<td align="center"><div class="label">Institución</div></td>
				<td align="center"><div class="label">Acción</div></td>
				
			</tr>

			<tr>
				<td align="center"><input size="20" name="num_cuenta_alumno"></td>
				<td align="center"><input size="20" name="nombre_completo"></td>
				<td align="center"><input size="20" name="grado"></td>
				<td align="center"><input size="20" name="nombre_institucion"></td>
				<td valign="bottom"><input type="submit" name="accion"
					value="Agregar" class="boton_agregar"></td>
			</tr>
		</table>
		<input type="button" value="  Regresar  "
			onClick="window.location='${ pageContext.request.contextPath }/alumno_update_form.jsp'">
	</form>
	<br>
</body>
</html>
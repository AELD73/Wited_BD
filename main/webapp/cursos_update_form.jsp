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
<title>Cat logo de Cursos</title>
<link rel="stylesheet"
	href="${ pageContext.request.contextPath }/css/style.css">
</head>
<body>
	<div class="step">Catalogo Cursos</div>
	<div class="instructions">Actualiza los Campos que se Requieran
		Modificar</div>
	<br>
	<sql:query var="resultados" dataSource="jdbc/TestDS"
		sql="SELECT * FROM curso;" />
	<c:forEach var="fila" items="${ resultados.rows }">
		<form method="post"
			action="${ pageContext.request.contextPath }/Update" class="form-examen">
			<table width="150%" class="tabla-examen">
				<tr class="form">
					<td align="center">
						<div class="label">Clave</div>
					</td>
					<td align="center">
						<div class="label">Nombre Curso</div>
					</td>
					<td align="center">
						<div class="label">Grado Curso</div>
					</td>
					<td align="center">
						<div class="label">Descripción del Curso</div>
					</td>
					<td align="center">
						<div class="label">Modificar</div>
					</td>
					<td align="center">
						<div class="label">Borrar</div>
					</td>
				</tr>

				<tr>
					<td align="center">${ fila.id_curso }<input type="hidden"
						name="id_curso" value="${ fila.id_curso }">
					</td>
					<td align="center">
						<table border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td><input size="20" name="nombre"
									value="${ fila.nombre_curso }"></td>
							</tr>

						</table>
					</td>
					<td>
						<table border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td><input size="20" name="grado"
									value="${ fila.grado_curso }"></td>
							</tr>

						</table>

					</td>

					<td>

						<table border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td><input size="20" name="descripcion"
									value="${ fila.descripcion_curso }"></td>
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
		action="${ pageContext.request.contextPath }/Update">
		
		<table width="100%" class="tabla-examen">


			<tr class="form">
				<td align="center"><div class="label">Nombre Curso</div></td>
				<td align="center"><div class="label">Grado Curso</div></td>
				<td align="center"><div class="label">Descripción</div></td>
				<td align="center"><div class="label">Acción</div></td>
				
				
			</tr>


			<tr>
				<td align="center"><input size="20" name="nombre_curso"></td>
				<td align="center"><input size="20" name="grado_curso"></td>
				<td align="center"><input size="20" name="descripcion_curso"></td>
				
				<td valign="bottom"><input type="submit" name="accion"
					value="Agregar" class="boton_agregar"></td>
			</tr>
		</table>
		

		<br> <input type="button" value="  Regresar  "
			onClick="window.location='${ pageContext.request.contextPath }/cursos_update_form.jsp'">

	</form>
	<br>
</body>
</html>
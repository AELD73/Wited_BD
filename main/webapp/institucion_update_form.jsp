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
<title>Instituciones</title>
<link rel="stylesheet"
	href="${ pageContext.request.contextPath }/css/style.css">
</head>
<body>
	<div class="step">Instituciones registradas</div>
	<div class="instructions">Actualiza los Campos que se Requieran
		Modificar</div>
	<br>
	<sql:query var="resultados" dataSource="jdbc/TestDS"
		sql="SELECT i.id_institucion, i.nombre_institucion, i.direccion FROM institucion i;" />
	<c:forEach var="fila" items="${ resultados.rows }">
		<form method="post"
			action="${ pageContext.request.contextPath }/UpdateInstitucion"
			class="form-institucion">
			<table width="150%" class="tabla-examen">
				<tr class="form">
					<td align="center">
						<div class="label">ID</div>
					</td>
					<td align="center">
						<div class="label">Nombre</div>
					</td>
					<td align="center">
						<div class="label">Direccion</div>
					</td>
					<td align="center">
						<div class="label">Modificar</div>
					</td>
					<td align="center">
						<div class="label">Borrar</div>
					</td>
					
				</tr>

				<tr>
					<td align="center">${ fila.id_institucion }<input
						type="hidden" name="id_institucion"
						value="${ fila.id_institucion }">
					</td>
					<td align="center">
						<table border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td><input size="20" name="nombre_institucion"
									value="${ fila.nombre_institucion }"></td>
							</tr>

						</table>
					</td>
					<td align="center">
						<table border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td><input size="20" name="direccion"
									value="${ fila.direccion }"></td>
							</tr>

						</table>
					</td>					
					


					<td valign="bottom"><input type="submit" name="accion"
						value="Modificar" class="boton_tabla"> </td>
					<td valign="bottom"><input
						type="submit" name="accion" value="Borrar" class="boton_tabla"></td>
				</tr>
			</table>
		</form>
	</c:forEach>
		<form method="post"
		      action="${ pageContext.request.contextPath }/UpdateInstitucion" 
		      class="form-institucion">
		  <table width="100%" class="tabla-examen">
		    <thead>
		      <tr>
		        <th colspan="4" style="text-align:center; font-size:1.2em;">Agregar Institucion</th>
		      </tr>
		      <tr>
		        <!-- <th align="center">ID Institución</th> -->
		        <th align="center">Nombre de la Institución</th>
		        <th align="center">Dirección</th>
		        <th></th> <!-- Para el botón -->
		      </tr>
		    </thead>
		    <tbody>
		      <tr>
		        <!--   <td align="center"><input size="50" name="id_institucion"></td> -->
		        <td align="center"><input size="50" name="nombre_institucion"></td>
		        <td align="center"><input size="20" name="direccion"></td>
		        <td valign="bottom">
		          <input type="submit" name="accion" value="Agregar" class="boton_agregar">
		        </td>
		      </tr>
		    </tbody>
		  </table>
		</form>


	<br>
</body>
</html>
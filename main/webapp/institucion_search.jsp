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
<title>Generador de reportes para instituciones</title>
<link rel="stylesheet"
	href="${ pageContext.request.contextPath }/css/style.css">
</head>
<body>
	<form method="get"
		action="${ pageContext.request.contextPath }/institucion_view.jsp" >
		<div class="step">Forma de Busqueda para Instituciones</div>
		<div class="instructions">Proporciona la informacion de las
			instituciones en forma de reportes</div>
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
						<div class="label">Patron:</div>
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
					<div class="label">Id Institucion</div>
				</td>
				<td align="center">
					<div class="label">Nombre Institucion</div>
				</td>
				<td align="center">
					<div class="label">Direccion</div>
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
			    SELECT i.id_institucion, i.nombre_institucion, i.direccion
			    FROM institucion i;
			</sql:query>

			<c:forEach var="fila" items="${resultados.rows}">
				<tr>
					<td align="center">${fila.id_institucion}</td>
					<td align="center">${fila.nombre_institucion}</td>
					<td align="center">${fila.direccion}</td>
			
					<td align="center">
						<input type="button" value="  Ver  "
							onclick="window.location='InstitucionForm?llave=${fila.id_institucion}'"
							class="boton_tabla">
					</td>
			
					<td align="center">
						<input type="button" value="  PDF  "
							onclick="window.location='InstitucionFormPdf?llave=${fila.id_institucion}'"
							class="boton_tabla">
					</td>
			
					<td align="center">
						<input type="button" value="   XLS  "
							onclick="window.location='InstitucionFormXls?llave=${fila.id_institucion}'"
							class="boton_tabla">
					</td>
			
					<td align="center">
						<input type="button" value="  HTML  "
							onclick="window.location='InstitucionFormHTML?llave=${fila.id_institucion}'"
							class="boton_tabla">
					</td>
				</tr>
			</c:forEach>

		</table>
		<br> <input type="button" value="  Regresar  "
			onclick="window.location='${ pageContext.request.contextPath }/main.jsp'">
	</form>
	<br>



</body>
</html>
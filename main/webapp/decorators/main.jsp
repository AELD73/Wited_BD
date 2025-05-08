<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<title><decorator:title default="Título" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
<decorator:head />
</head>

<body class="layout">
	<header class="layout-header">
		<%@include file="header.jspf"%>
	</header>

	<div class="layout-main">
		<aside class="layout-menu">
			<%@include file="menu.jspf"%>
		</aside>

		<main class="layout-content">
			<div class="section">
				<decorator:title />
			</div>
			<hr>
			<decorator:body />
		</main>
	</div>

	<footer class="layout-footer">
		<%@include file="footer.jspf"%>
	</footer>
</body>
</html>

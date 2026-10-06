<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Error</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/comun/menu.jsp"/>

<div class="container">
    <div class="alert alert-danger mt-4">
        <c:choose>
            <c:when test="${pageContext.errorData.statusCode == 403}">Acceso denegado: tu perfil no puede ver esta pagina.</c:when>
            <c:when test="${pageContext.errorData.statusCode == 404}">La pagina no existe.</c:when>
            <c:otherwise>Ocurrio un error inesperado. Intenta de nuevo.</c:otherwise>
        </c:choose>
    </div>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/inicio">Volver al inicio</a>
</div>

<jsp:include page="/WEB-INF/jsp/comun/scripts.jsp"/>
</body>
</html>

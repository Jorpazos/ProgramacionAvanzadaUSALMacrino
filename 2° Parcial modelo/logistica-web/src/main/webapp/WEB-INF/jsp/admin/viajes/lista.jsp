<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="es_AR"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Viajes</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/comun/menu.jsp"/>

<div class="container">
    <h2>Viajes</h2>
    <a class="btn btn-primary mb-3" href="${pageContext.request.contextPath}/admin/viajes?accion=nuevo">Cargar viaje</a>

    <table class="table table-bordered table-striped">
        <thead>
        <tr>
            <th>#</th><th>Chofer</th><th>Camion</th><th>Recorrido</th>
            <th>Km</th><th>Dias</th><th>Tanques</th><th>Estado</th><th>Cargado</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="viaje" items="${viajes}">
            <tr>
                <td>${viaje.id}</td>
                <td><c:out value="${viaje.chofer.nombreCompleto}"/></td>
                <td><c:out value="${viaje.camion.dominio}"/></td>
                <td><c:out value="${viaje.origen.nombre}"/> - <c:out value="${viaje.destino.nombre}"/></td>
                <td>${viaje.estimacion.distanciaKm()}</td>
                <td>${viaje.estimacion.dias()}</td>
                <td>${viaje.estimacion.tanques()}</td>
                <td><c:out value="${viaje.estado.descripcion}"/></td>
                <td><c:out value="${viaje.fechaCargaFormateada}"/></td>
            </tr>
        </c:forEach>
        <c:if test="${empty viajes}">
            <tr><td colspan="9" class="text-center">Todavia no se cargaron viajes</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<jsp:include page="/WEB-INF/jsp/comun/scripts.jsp"/>
</body>
</html>

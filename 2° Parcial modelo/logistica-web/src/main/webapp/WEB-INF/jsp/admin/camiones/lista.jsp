<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="es_AR"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Camiones</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/comun/menu.jsp"/>

<div class="container">
    <h2>Camiones</h2>
    <a class="btn btn-primary mb-3" href="${pageContext.request.contextPath}/admin/camiones?accion=nuevo">Nuevo camion</a>

    <table class="table table-bordered table-striped">
        <thead>
        <tr>
            <th>Marca</th><th>Modelo</th><th>Dominio</th><th>Toneladas</th>
            <th>Tanque (l)</th><th>Consumo (l/km)</th><th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="camion" items="${camiones}">
            <tr>
                <td><c:out value="${camion.marca}"/></td>
                <td><c:out value="${camion.modelo}"/></td>
                <td><c:out value="${camion.dominio}"/></td>
                <td><fmt:formatNumber value="${camion.toneladasMaximas}" maxFractionDigits="2"/></td>
                <td><fmt:formatNumber value="${camion.capacidadTanqueLitros}" maxFractionDigits="2"/></td>
                <td><fmt:formatNumber value="${camion.consumoLitrosPorKm}" maxFractionDigits="3"/></td>
                <td>
                    <a class="btn btn-warning btn-sm" href="${pageContext.request.contextPath}/admin/camiones?accion=editar&id=${camion.id}">Editar</a>
                    <button type="button" class="btn btn-danger btn-sm" onclick="confirmarEliminar(${camion.id})">Eliminar</button>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty camiones}">
            <tr><td colspan="7" class="text-center">No hay camiones cargados</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<form id="formEliminar" method="post" action="${pageContext.request.contextPath}/admin/camiones">
    <input type="hidden" name="accion" value="eliminar">
    <input type="hidden" name="id" id="idEliminar">
</form>
<jsp:include page="/WEB-INF/jsp/comun/scripts.jsp"/>
<script>
    function confirmarEliminar(id) {
        Swal.fire({
            title: 'Eliminar camion',
            text: 'Esta accion no se puede deshacer',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Si, eliminar',
            cancelButtonText: 'Cancelar'
        }).then(function (resultado) {
            if (resultado.isConfirmed) {
                document.getElementById('idEliminar').value = id;
                document.getElementById('formEliminar').submit();
            }
        });
    }
</script>
</body>
</html>

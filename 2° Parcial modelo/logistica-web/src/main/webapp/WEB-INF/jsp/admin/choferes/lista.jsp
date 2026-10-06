<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="es_AR"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Choferes</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/comun/menu.jsp"/>

<div class="container">
    <h2>Choferes</h2>
    <a class="btn btn-primary mb-3" href="${pageContext.request.contextPath}/admin/choferes?accion=nuevo">Nuevo chofer</a>

    <table class="table table-bordered table-striped">
        <thead>
        <tr>
            <th>Apellido y nombre</th><th>DNI</th><th>Nacimiento</th><th>Categoria</th>
            <th>Celular</th><th>Camiones autorizados</th><th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="chofer" items="${choferes}">
            <tr>
                <td><c:out value="${chofer.nombreCompleto}"/></td>
                <td><c:out value="${chofer.dni}"/></td>
                <td><c:out value="${chofer.fechaNacimientoFormateada}"/></td>
                <td><c:out value="${chofer.categoria}"/></td>
                <td><c:out value="${chofer.telefono}"/></td>
                <td>
                    <c:forEach var="camion" items="${chofer.camionesAutorizados}">
                        <div><c:out value="${camion.dominio}"/></div>
                    </c:forEach>
                </td>
                <td>
                    <a class="btn btn-warning btn-sm" href="${pageContext.request.contextPath}/admin/choferes?accion=editar&id=${chofer.id}">Editar</a>
                    <button type="button" class="btn btn-danger btn-sm" onclick="confirmarEliminar(${chofer.id})">Eliminar</button>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty choferes}">
            <tr><td colspan="7" class="text-center">No hay choferes cargados</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<form id="formEliminar" method="post" action="${pageContext.request.contextPath}/admin/choferes">
    <input type="hidden" name="accion" value="eliminar">
    <input type="hidden" name="id" id="idEliminar">
</form>
<jsp:include page="/WEB-INF/jsp/comun/scripts.jsp"/>
<script>
    function confirmarEliminar(id) {
        Swal.fire({
            title: 'Eliminar chofer',
            text: 'Se borra tambien su usuario. Esta accion no se puede deshacer',
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

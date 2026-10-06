<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="es_AR"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Mis viajes</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/comun/menu.jsp"/>

<div class="container">
    <h2>Mis viajes</h2>

    <table class="table table-bordered table-striped">
        <thead>
        <tr>
            <th>Recorrido</th><th>Camion</th><th>Distancia</th><th>Dias</th><th>Tanques</th>
            <th>Estado</th><th>Iniciado</th><th>Finalizado</th><th>Accion</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="viaje" items="${viajes}">
            <tr>
                <td><c:out value="${viaje.origen.nombre}"/> - <c:out value="${viaje.destino.nombre}"/></td>
                <td><c:out value="${viaje.camion.descripcion}"/></td>
                <td>${viaje.estimacion.distanciaKm()} km</td>
                <td>${viaje.estimacion.dias()}</td>
                <td>${viaje.estimacion.tanques()}</td>
                <td><c:out value="${viaje.estado.descripcion}"/></td>
                <td><c:out value="${viaje.fechaInicioFormateada}"/></td>
                <td><c:out value="${viaje.fechaFinFormateada}"/></td>
                <td>
                    <c:if test="${viaje.puedeIniciarse()}">
                        <form method="post" action="${pageContext.request.contextPath}/chofer/viajes" onsubmit="return confirmarEnvio(event, 'Iniciar este viaje ahora?')">
                            <input type="hidden" name="accion" value="iniciar">
                            <input type="hidden" name="viajeId" value="${viaje.id}">
                            <button type="submit" class="btn btn-primary btn-sm">Iniciar viaje</button>
                        </form>
                    </c:if>
                    <c:if test="${viaje.puedeFinalizarse()}">
                        <form method="post" action="${pageContext.request.contextPath}/chofer/viajes" onsubmit="return confirmarEnvio(event, 'Marcar el viaje como realizado?')">
                            <input type="hidden" name="accion" value="finalizar">
                            <input type="hidden" name="viajeId" value="${viaje.id}">
                            <button type="submit" class="btn btn-success btn-sm">Marcar como realizado</button>
                        </form>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty viajes}">
            <tr><td colspan="9" class="text-center">No tenes viajes asignados</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<jsp:include page="/WEB-INF/jsp/comun/scripts.jsp"/>
<script>
    // Pide confirmacion con SweetAlert antes de enviar el formulario
    function confirmarEnvio(evento, mensaje) {
        evento.preventDefault();
        var form = evento.target;
        Swal.fire({
            text: mensaje,
            icon: "question",
            showCancelButton: true,
            confirmButtonText: "Si",
            cancelButtonText: "Cancelar"
        }).then(function (resultado) {
            if (resultado.isConfirmed) {
                form.submit();
            }
        });
        return false;
    }
</script>
</body>
</html>

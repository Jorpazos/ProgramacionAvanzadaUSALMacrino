<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="es_AR"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Cargar viaje</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/comun/menu.jsp"/>

<div class="container">
    <h2>Cargar viaje</h2>

    <%-- PASO 1: buscar el chofer por DNI (consulta: GET) --%>
    <h5 class="mt-4">1. Chofer</h5>
    <form method="get" action="${pageContext.request.contextPath}/admin/viajes" class="row g-2 mb-3">
        <input type="hidden" name="accion" value="nuevo">
        <div class="col-md-4">
            <input type="text" class="form-control" name="dni" required pattern="\d{7,8}"
                   placeholder="DNI del chofer" value="<c:out value='${dni}'/>">
        </div>
        <div class="col-auto"><button type="submit" class="btn btn-primary">Buscar</button></div>
    </form>
    <c:if test="${not empty chofer}">
        <p><b><c:out value="${chofer.nombreCompleto}"/></b> -
            <c:out value="${chofer.categoria.descripcion}"/> - Cel. <c:out value="${chofer.telefono}"/></p>
    </c:if>

    <%-- PASO 2: camion disponible, origen y destino; "Calcular" sigue siendo una consulta --%>
    <c:if test="${not empty chofer and not empty camiones}">
        <h5 class="mt-4">2. Camion, origen y destino</h5>
        <form method="get" action="${pageContext.request.contextPath}/admin/viajes">
            <input type="hidden" name="accion" value="nuevo">
            <input type="hidden" name="dni" value="<c:out value='${chofer.dni}'/>">
            <div class="row">
                <div class="col-md-4 mb-3">
                    <label class="form-label" for="camionId">Camion disponible</label>
                    <select class="form-select" id="camionId" name="camionId" required>
                        <option value="">Seleccione...</option>
                        <c:forEach var="camion" items="${camiones}">
                            <option value="${camion.id}" ${camionElegido eq camion.id ? 'selected' : ''}>
                                <c:out value="${camion.descripcion}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-4 mb-3">
                    <label class="form-label" for="origen">Origen</label>
                    <select class="form-select" id="origen" name="origen" required>
                        <option value="">Seleccione...</option>
                        <c:forEach var="d" items="${destinos}">
                            <option value="${d}" ${origenElegido eq d ? 'selected' : ''}><c:out value="${d.nombre}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-4 mb-3">
                    <label class="form-label" for="destino">Destino</label>
                    <select class="form-select" id="destino" name="destino" required>
                        <option value="">Seleccione...</option>
                        <c:forEach var="d" items="${destinos}">
                            <option value="${d}" ${destinoElegido eq d ? 'selected' : ''}><c:out value="${d.nombre}"/></option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <button type="submit" class="btn btn-primary">Calcular viaje</button>
        </form>
    </c:if>

    <%-- PASO 3: resumen con tiempo y tanques; confirmar es una modificacion: POST --%>
    <c:if test="${not empty viaje}">
        <h5 class="mt-4">3. Resumen del viaje</h5>
        <table class="table table-bordered">
            <tr><th>Recorrido</th><td><c:out value="${viaje.origen.nombre}"/> - <c:out value="${viaje.destino.nombre}"/></td></tr>
            <tr><th>Distancia</th><td>${viaje.estimacion.distanciaKm()} km</td></tr>
            <tr><th>Tiempo de viaje</th><td>${viaje.estimacion.dias()} dia(s) (200 km por dia)</td></tr>
            <tr><th>Combustible</th><td><fmt:formatNumber value="${viaje.estimacion.litrosTotales()}" maxFractionDigits="1"/> litros</td></tr>
            <tr><th>Tanques a llenar</th><td>${viaje.estimacion.tanques()}
                (tanque de <fmt:formatNumber value="${viaje.camion.capacidadTanqueLitros}" maxFractionDigits="0"/> l)</td></tr>
        </table>
        <form method="post" action="${pageContext.request.contextPath}/admin/viajes" onsubmit="return confirmarEnvio(event, 'Confirmar la carga del viaje?')">
            <input type="hidden" name="accion" value="guardar">
            <input type="hidden" name="dni" value="<c:out value='${chofer.dni}'/>">
            <input type="hidden" name="camionId" value="${viaje.camion.id}">
            <input type="hidden" name="origen" value="${viaje.origen}">
            <input type="hidden" name="destino" value="${viaje.destino}">
            <button type="submit" class="btn btn-success">Confirmar y cargar viaje</button>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/viajes">Cancelar</a>
        </form>
    </c:if>

    <%-- Tabla de distancias del enunciado (consultada a la base con DistanciaDAO) --%>
    <h5 class="mt-5">Tabla de distancias (km)</h5>
    <table class="table table-bordered table-sm text-center">
        <thead>
        <tr>
            <th></th>
            <c:forEach var="d" items="${destinos}"><th><c:out value="${d.nombre}"/></th></c:forEach>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="fila" items="${destinos}">
            <tr>
                <th><c:out value="${fila.nombre}"/></th>
                <c:forEach var="col" items="${destinos}">
                    <c:choose>
                        <c:when test="${fila eq col}"><td class="table-secondary"></td></c:when>
                        <c:otherwise><td>${tabla[fila][col]}</td></c:otherwise>
                    </c:choose>
                </c:forEach>
            </tr>
        </c:forEach>
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

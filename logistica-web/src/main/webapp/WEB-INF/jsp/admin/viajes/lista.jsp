<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/comun/taglibs.jspf" %>
<c:set var="tituloPagina" value="Viajes"/>
<%@ include file="/WEB-INF/jsp/comun/encabezado.jspf" %>
<div class="d-flex justify-content-between align-items-center mb-3">
    <h1 class="h3 mb-0">Viajes</h1>
    <a class="btn btn-primary" href="${ctx}/admin/viajes?accion=nuevo">Cargar viaje</a>
</div>
<div class="card shadow-sm">
    <div class="table-responsive">
        <table class="table table-hover align-middle mb-0">
            <thead class="table-light">
            <tr>
                <th>#</th><th>Chofer</th><th>Camión</th><th>Recorrido</th>
                <th class="text-end">Km</th><th class="text-end">Días</th><th class="text-end">Tanques</th>
                <th>Estado</th><th>Cargado</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="viaje" items="${viajes}">
                <tr>
                    <td>${viaje.id}</td>
                    <td><c:out value="${viaje.chofer.nombreCompleto}"/></td>
                    <td><c:out value="${viaje.camion.dominio}"/></td>
                    <td><c:out value="${viaje.origen.nombre}"/> → <c:out value="${viaje.destino.nombre}"/></td>
                    <td class="text-end">${viaje.estimacion.distanciaKm()}</td>
                    <td class="text-end">${viaje.estimacion.dias()}</td>
                    <td class="text-end">${viaje.estimacion.tanques()}</td>
                    <td>
                        <c:choose>
                            <c:when test="${viaje.estado eq 'FINALIZADO'}"><span class="badge text-bg-success"></c:when>
                            <c:when test="${viaje.estado eq 'EN_CURSO'}"><span class="badge text-bg-warning"></c:when>
                            <c:otherwise><span class="badge text-bg-secondary"></c:otherwise>
                        </c:choose>
                        <c:out value="${viaje.estado.descripcion}"/></span>
                    </td>
                    <td><c:out value="${viaje.fechaCargaFormateada}"/></td>
                </tr>
            </c:forEach>
            <c:if test="${empty viajes}">
                <tr><td colspan="9" class="text-center text-muted py-4">Todavía no se cargaron viajes.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>
<%@ include file="/WEB-INF/jsp/comun/pie.jspf" %>

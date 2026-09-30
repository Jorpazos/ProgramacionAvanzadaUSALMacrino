<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/comun/taglibs.jspf" %>
<c:set var="tituloPagina" value="Mis viajes"/>
<%@ include file="/WEB-INF/jsp/comun/encabezado.jspf" %>
<h1 class="h3 mb-3">Mis viajes</h1>
<div class="row g-3">
    <c:forEach var="viaje" items="${viajes}">
        <div class="col-md-6">
            <div class="card shadow-sm h-100">
                <div class="card-body">
                    <div class="d-flex justify-content-between">
                        <h2 class="h5">
                            <c:out value="${viaje.origen.nombre}"/> → <c:out value="${viaje.destino.nombre}"/>
                        </h2>
                        <c:choose>
                            <c:when test="${viaje.estado eq 'FINALIZADO'}"><span class="badge text-bg-success align-self-start"></c:when>
                            <c:when test="${viaje.estado eq 'EN_CURSO'}"><span class="badge text-bg-warning align-self-start"></c:when>
                            <c:otherwise><span class="badge text-bg-secondary align-self-start"></c:otherwise>
                        </c:choose>
                        <c:out value="${viaje.estado.descripcion}"/></span>
                    </div>
                    <ul class="list-unstyled small mb-3">
                        <li>Camión: <c:out value="${viaje.camion.descripcion}"/></li>
                        <li>Distancia: ${viaje.estimacion.distanciaKm()} km · ${viaje.estimacion.dias()} día(s)</li>
                        <li>Tanques a llenar: ${viaje.estimacion.tanques()}</li>
                        <li>Iniciado: <c:out value="${viaje.fechaInicioFormateada}"/> ·
                            Finalizado: <c:out value="${viaje.fechaFinFormateada}"/></li>
                    </ul>
                    <c:if test="${viaje.puedeIniciarse()}">
                        <form action="${ctx}/chofer/viajes" method="post" data-confirmar="¿Iniciar este viaje ahora?">
                            <input type="hidden" name="accion" value="iniciar">
                            <input type="hidden" name="viajeId" value="${viaje.id}">
                            <button type="submit" class="btn btn-primary">Iniciar viaje</button>
                        </form>
                    </c:if>
                    <c:if test="${viaje.puedeFinalizarse()}">
                        <form action="${ctx}/chofer/viajes" method="post" data-confirmar="¿Marcar el viaje como realizado?">
                            <input type="hidden" name="accion" value="finalizar">
                            <input type="hidden" name="viajeId" value="${viaje.id}">
                            <button type="submit" class="btn btn-success">Marcar como realizado</button>
                        </form>
                    </c:if>
                </div>
            </div>
        </div>
    </c:forEach>
    <c:if test="${empty viajes}">
        <div class="col-12"><p class="text-center text-muted py-4">No tenés viajes asignados.</p></div>
    </c:if>
</div>
<%@ include file="/WEB-INF/jsp/comun/pie.jspf" %>

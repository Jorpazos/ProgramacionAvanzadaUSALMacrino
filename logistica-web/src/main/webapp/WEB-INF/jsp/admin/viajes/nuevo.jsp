<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/comun/taglibs.jspf" %>
<c:set var="tituloPagina" value="Cargar viaje"/>
<%@ include file="/WEB-INF/jsp/comun/encabezado.jspf" %>
<h1 class="h3 mb-3">Cargar viaje</h1>

<%-- PASO 1: buscar el chofer por DNI (consulta: GET) --%>
<div class="card shadow-sm mb-3">
    <div class="card-header">1. Chofer</div>
    <div class="card-body">
        <form action="${ctx}/admin/viajes" method="get" class="row g-2">
            <input type="hidden" name="accion" value="nuevo">
            <div class="col-sm-6 col-md-4">
                <input type="text" class="form-control" name="dni" required pattern="\d{7,8}" maxlength="8"
                       inputmode="numeric" placeholder="DNI del chofer" aria-label="DNI del chofer"
                       value="<c:out value='${dni}'/>">
            </div>
            <div class="col-auto"><button type="submit" class="btn btn-primary">Buscar</button></div>
        </form>
        <c:if test="${not empty chofer}">
            <p class="mt-3 mb-0">
                <strong><c:out value="${chofer.nombreCompleto}"/></strong> ·
                <c:out value="${chofer.categoria.descripcion}"/> · Cel. <c:out value="${chofer.telefono}"/>
            </p>
        </c:if>
    </div>
</div>

<c:if test="${not empty chofer and not empty camiones}">
    <%-- PASO 2: camion disponible, origen y destino; "Calcular" sigue siendo una consulta --%>
    <div class="card shadow-sm mb-3">
        <div class="card-header">2. Camión, origen y destino</div>
        <div class="card-body">
            <form action="${ctx}/admin/viajes" method="get" class="row g-3">
                <input type="hidden" name="accion" value="nuevo">
                <input type="hidden" name="dni" value="<c:out value='${chofer.dni}'/>">
                <div class="col-md-4">
                    <label for="camionId" class="form-label">Camión disponible</label>
                    <select class="form-select" id="camionId" name="camionId" required>
                        <option value="">Seleccione...</option>
                        <c:forEach var="camion" items="${camiones}">
                            <option value="${camion.id}" ${camionElegido eq camion.id ? 'selected' : ''}>
                                <c:out value="${camion.descripcion}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-4">
                    <label for="origen" class="form-label">Origen</label>
                    <select class="form-select" id="origen" name="origen" required>
                        <option value="">Seleccione...</option>
                        <c:forEach var="d" items="${destinos}">
                            <option value="${d}" ${origenElegido eq d ? 'selected' : ''}><c:out value="${d.nombre}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-4">
                    <label for="destino" class="form-label">Destino</label>
                    <select class="form-select" id="destino" name="destino" required>
                        <option value="">Seleccione...</option>
                        <c:forEach var="d" items="${destinos}">
                            <option value="${d}" ${destinoElegido eq d ? 'selected' : ''}><c:out value="${d.nombre}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-12"><button type="submit" class="btn btn-primary">Calcular viaje</button></div>
            </form>
        </div>
    </div>
</c:if>

<c:if test="${not empty viaje}">
    <%-- PASO 3: resumen con tiempo y tanques; confirmar es una modificacion: POST --%>
    <div class="card shadow-sm border-success mb-3">
        <div class="card-header text-bg-success">3. Resumen del viaje</div>
        <div class="card-body">
            <dl class="row resumen-viaje mb-3">
                <dt class="col-sm-4">Recorrido</dt>
                <dd class="col-sm-8"><c:out value="${viaje.origen.nombre}"/> → <c:out value="${viaje.destino.nombre}"/></dd>
                <dt class="col-sm-4">Distancia</dt>
                <dd class="col-sm-8">${viaje.estimacion.distanciaKm()} km</dd>
                <dt class="col-sm-4">Tiempo de viaje</dt>
                <dd class="col-sm-8">${viaje.estimacion.dias()} día(s) (200 km por día)</dd>
                <dt class="col-sm-4">Combustible</dt>
                <dd class="col-sm-8"><fmt:formatNumber value="${viaje.estimacion.litrosTotales()}" maxFractionDigits="1"/> litros</dd>
                <dt class="col-sm-4">Tanques a llenar</dt>
                <dd class="col-sm-8">${viaje.estimacion.tanques()}
                    (tanque de <fmt:formatNumber value="${viaje.camion.capacidadTanqueLitros}" maxFractionDigits="0"/> l)</dd>
            </dl>
            <form action="${ctx}/admin/viajes" method="post" class="d-flex gap-2"
                  data-confirmar="¿Confirmar la carga del viaje?">
                <input type="hidden" name="accion" value="guardar">
                <input type="hidden" name="dni" value="<c:out value='${chofer.dni}'/>">
                <input type="hidden" name="camionId" value="${viaje.camion.id}">
                <input type="hidden" name="origen" value="${viaje.origen}">
                <input type="hidden" name="destino" value="${viaje.destino}">
                <button type="submit" class="btn btn-success">Confirmar y cargar viaje</button>
                <a class="btn btn-outline-secondary" href="${ctx}/admin/viajes">Cancelar</a>
            </form>
        </div>
    </div>
</c:if>

<%-- Tabla de distancias del enunciado (consultada a la base con DistanciaDAO) --%>
<div class="card shadow-sm">
    <div class="card-header">Tabla de distancias (km)</div>
    <div class="table-responsive">
        <table class="table table-sm table-bordered tabla-distancias mb-0">
            <thead class="table-light">
            <tr>
                <th></th>
                <c:forEach var="d" items="${destinos}"><th><c:out value="${d.nombre}"/></th></c:forEach>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="fila" items="${destinos}">
                <tr>
                    <th class="table-light"><c:out value="${fila.nombre}"/></th>
                    <c:forEach var="col" items="${destinos}">
                        <c:choose>
                            <c:when test="${fila eq col}"><td class="misma"></td></c:when>
                            <c:otherwise><td>${tabla[fila][col]}</td></c:otherwise>
                        </c:choose>
                    </c:forEach>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<%@ include file="/WEB-INF/jsp/comun/pie.jspf" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/comun/taglibs.jspf" %>
<c:set var="tituloPagina" value="Chofer"/>
<%@ include file="/WEB-INF/jsp/comun/encabezado.jspf" %>
<c:set var="esNuevo" value="${empty valores.id}"/>
<h1 class="h3 mb-3"><c:out value="${esNuevo ? 'Nuevo chofer' : 'Editar chofer'}"/></h1>
<div class="card shadow-sm">
    <div class="card-body">
        <form action="${ctx}/admin/choferes" method="post" class="row g-3">
            <input type="hidden" name="accion" value="guardar">
            <input type="hidden" name="id" value="<c:out value='${valores.id}'/>">
            <div class="col-md-6">
                <label for="nombre" class="form-label">Nombre</label>
                <input type="text" class="form-control" id="nombre" name="nombre" required maxlength="50"
                       value="<c:out value='${valores.nombre}'/>">
            </div>
            <div class="col-md-6">
                <label for="apellido" class="form-label">Apellido</label>
                <input type="text" class="form-control" id="apellido" name="apellido" required maxlength="50"
                       value="<c:out value='${valores.apellido}'/>">
            </div>
            <div class="col-md-4">
                <label for="dni" class="form-label">DNI</label>
                <input type="text" class="form-control" id="dni" name="dni" required pattern="\d{7,8}"
                       maxlength="8" inputmode="numeric" title="7 u 8 dígitos" value="<c:out value='${valores.dni}'/>">
            </div>
            <div class="col-md-4">
                <label for="fechaNacimiento" class="form-label">Fecha de nacimiento</label>
                <input type="date" class="form-control" id="fechaNacimiento" name="fechaNacimiento" required
                       value="<c:out value='${valores.fechaNacimiento}'/>">
            </div>
            <div class="col-md-4">
                <label for="telefono" class="form-label">Celular</label>
                <input type="tel" class="form-control" id="telefono" name="telefono" required pattern="\d{8,15}"
                       maxlength="15" inputmode="numeric" title="Solo números, entre 8 y 15 dígitos"
                       value="<c:out value='${valores.telefono}'/>">
            </div>
            <div class="col-md-6">
                <label for="categoria" class="form-label">Categoría</label>
                <select class="form-select" id="categoria" name="categoria" required>
                    <option value="">Seleccione...</option>
                    <c:forEach var="cat" items="${categorias}">
                        <option value="${cat}" ${valores.categoria eq cat ? 'selected' : ''}>
                            <c:out value="${cat.descripcion}"/>
                        </option>
                    </c:forEach>
                </select>
            </div>
            <c:if test="${esNuevo}">
                <div class="col-md-6">
                    <label for="password" class="form-label">Contraseña inicial (usuario = DNI)</label>
                    <input type="password" class="form-control" id="password" name="password" required minlength="6"
                           autocomplete="new-password">
                </div>
            </c:if>
            <div class="col-12">
                <label class="form-label">Camiones que está autorizado a manejar</label>
                <div class="form-text mb-2">Solo se aceptan camiones cuyas toneladas entren en la categoría elegida.</div>
                <div class="row">
                    <c:forEach var="camion" items="${camiones}">
                        <div class="col-md-6">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" name="camionIds"
                                       id="camion${camion.id}" value="${camion.id}"
                                       ${camionesSeleccionados.contains(camion.id) ? 'checked' : ''}>
                                <label class="form-check-label" for="camion${camion.id}">
                                    <c:out value="${camion.descripcion}"/>
                                </label>
                            </div>
                        </div>
                    </c:forEach>
                    <c:if test="${empty camiones}">
                        <p class="text-muted">No hay camiones cargados todavía.</p>
                    </c:if>
                </div>
            </div>
            <div class="col-12 d-flex gap-2">
                <button type="submit" class="btn btn-primary">Guardar</button>
                <a class="btn btn-outline-secondary" href="${ctx}/admin/choferes">Cancelar</a>
            </div>
        </form>
    </div>
</div>
<%@ include file="/WEB-INF/jsp/comun/pie.jspf" %>

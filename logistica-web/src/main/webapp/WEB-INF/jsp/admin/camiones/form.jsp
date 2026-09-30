<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/comun/taglibs.jspf" %>
<c:set var="tituloPagina" value="Camión"/>
<%@ include file="/WEB-INF/jsp/comun/encabezado.jspf" %>
<h1 class="h3 mb-3"><c:out value="${empty valores.id ? 'Nuevo camión' : 'Editar camión'}"/></h1>
<div class="card shadow-sm">
    <div class="card-body">
        <form action="${ctx}/admin/camiones" method="post" class="row g-3">
            <input type="hidden" name="accion" value="guardar">
            <input type="hidden" name="id" value="<c:out value='${valores.id}'/>">
            <div class="col-md-6">
                <label for="marca" class="form-label">Marca</label>
                <input type="text" class="form-control" id="marca" name="marca" required maxlength="40"
                       value="<c:out value='${valores.marca}'/>">
            </div>
            <div class="col-md-6">
                <label for="modelo" class="form-label">Modelo</label>
                <input type="text" class="form-control" id="modelo" name="modelo" required maxlength="40"
                       value="<c:out value='${valores.modelo}'/>">
            </div>
            <div class="col-md-4">
                <label for="dominio" class="form-label">Dominio (patente)</label>
                <input type="text" class="form-control text-uppercase" id="dominio" name="dominio" required
                       maxlength="10" placeholder="AB123CD" value="<c:out value='${valores.dominio}'/>">
            </div>
            <div class="col-md-4">
                <label for="toneladasMaximas" class="form-label">Toneladas máximas</label>
                <input type="number" step="0.01" min="0.01" max="99" class="form-control" id="toneladasMaximas"
                       name="toneladasMaximas" required value="<c:out value='${valores.toneladasMaximas}'/>">
            </div>
            <div class="col-md-4">
                <label for="capacidadTanqueLitros" class="form-label">Capacidad del tanque (litros)</label>
                <input type="number" step="0.01" min="0.01" class="form-control" id="capacidadTanqueLitros"
                       name="capacidadTanqueLitros" required value="<c:out value='${valores.capacidadTanqueLitros}'/>">
            </div>
            <div class="col-md-4">
                <label for="consumoLitrosPorKm" class="form-label">Consumo (litros por km)</label>
                <input type="number" step="0.001" min="0.001" class="form-control" id="consumoLitrosPorKm"
                       name="consumoLitrosPorKm" required value="<c:out value='${valores.consumoLitrosPorKm}'/>">
            </div>
            <div class="col-12 d-flex gap-2">
                <button type="submit" class="btn btn-primary">Guardar</button>
                <a class="btn btn-outline-secondary" href="${ctx}/admin/camiones">Cancelar</a>
            </div>
        </form>
    </div>
</div>
<%@ include file="/WEB-INF/jsp/comun/pie.jspf" %>

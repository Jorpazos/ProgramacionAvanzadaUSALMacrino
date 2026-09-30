<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/comun/taglibs.jspf" %>
<c:set var="tituloPagina" value="Camiones"/>
<%@ include file="/WEB-INF/jsp/comun/encabezado.jspf" %>
<div class="d-flex justify-content-between align-items-center mb-3">
    <h1 class="h3 mb-0">Camiones</h1>
    <a class="btn btn-primary" href="${ctx}/admin/camiones?accion=nuevo">Nuevo camión</a>
</div>
<div class="card shadow-sm">
    <div class="table-responsive">
        <table class="table table-hover align-middle mb-0">
            <thead class="table-light">
            <tr>
                <th>Marca</th><th>Modelo</th><th>Dominio</th>
                <th class="text-end">Toneladas máx.</th>
                <th class="text-end">Tanque (l)</th>
                <th class="text-end">Consumo (l/km)</th>
                <th class="text-end">Acciones</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="camion" items="${camiones}">
                <tr>
                    <td><c:out value="${camion.marca}"/></td>
                    <td><c:out value="${camion.modelo}"/></td>
                    <td><span class="badge text-bg-secondary"><c:out value="${camion.dominio}"/></span></td>
                    <td class="text-end"><fmt:formatNumber value="${camion.toneladasMaximas}" maxFractionDigits="2"/></td>
                    <td class="text-end"><fmt:formatNumber value="${camion.capacidadTanqueLitros}" maxFractionDigits="2"/></td>
                    <td class="text-end"><fmt:formatNumber value="${camion.consumoLitrosPorKm}" maxFractionDigits="3"/></td>
                    <td class="text-end">
                        <a class="btn btn-sm btn-outline-primary"
                           href="${ctx}/admin/camiones?accion=editar&id=${camion.id}">Editar</a>
                        <form action="${ctx}/admin/camiones" method="post" class="d-inline"
                              data-confirmar="¿Eliminar el camión ${fn:escapeXml(camion.dominio)}?">
                            <input type="hidden" name="accion" value="eliminar">
                            <input type="hidden" name="id" value="${camion.id}">
                            <button type="submit" class="btn btn-sm btn-outline-danger">Eliminar</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty camiones}">
                <tr><td colspan="7" class="text-center text-muted py-4">Todavía no hay camiones cargados.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>
<%@ include file="/WEB-INF/jsp/comun/pie.jspf" %>

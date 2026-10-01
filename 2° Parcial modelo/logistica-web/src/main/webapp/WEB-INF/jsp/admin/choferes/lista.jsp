<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/comun/taglibs.jspf" %>
<c:set var="tituloPagina" value="Choferes"/>
<%@ include file="/WEB-INF/jsp/comun/encabezado.jspf" %>
<div class="d-flex justify-content-between align-items-center mb-3">
    <h1 class="h3 mb-0">Choferes</h1>
    <a class="btn btn-primary" href="${ctx}/admin/choferes?accion=nuevo">Nuevo chofer</a>
</div>
<div class="card shadow-sm">
    <div class="table-responsive">
        <table class="table table-hover align-middle mb-0">
            <thead class="table-light">
            <tr>
                <th>Apellido y nombre</th><th>DNI</th><th>Nacimiento</th><th>Categoría</th>
                <th>Celular</th><th>Camiones autorizados</th><th class="text-end">Acciones</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="chofer" items="${choferes}">
                <tr>
                    <td><c:out value="${chofer.nombreCompleto}"/></td>
                    <td><c:out value="${chofer.dni}"/></td>
                    <td><c:out value="${chofer.fechaNacimientoFormateada}"/></td>
                    <td><span class="badge text-bg-info"><c:out value="${chofer.categoria}"/></span></td>
                    <td><c:out value="${chofer.telefono}"/></td>
                    <td>
                        <c:forEach var="camion" items="${chofer.camionesAutorizados}">
                            <span class="badge text-bg-secondary"><c:out value="${camion.dominio}"/></span>
                        </c:forEach>
                        <c:if test="${empty chofer.camionesAutorizados}"><span class="text-muted">Ninguno</span></c:if>
                    </td>
                    <td class="text-end">
                        <a class="btn btn-sm btn-outline-primary"
                           href="${ctx}/admin/choferes?accion=editar&id=${chofer.id}">Editar</a>
                        <form action="${ctx}/admin/choferes" method="post" class="d-inline"
                              data-confirmar="¿Eliminar al chofer ${fn:escapeXml(chofer.nombreCompleto)}? También se elimina su usuario.">
                            <input type="hidden" name="accion" value="eliminar">
                            <input type="hidden" name="id" value="${chofer.id}">
                            <button type="submit" class="btn btn-sm btn-outline-danger">Eliminar</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty choferes}">
                <tr><td colspan="7" class="text-center text-muted py-4">Todavía no hay choferes cargados.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>
<%@ include file="/WEB-INF/jsp/comun/pie.jspf" %>

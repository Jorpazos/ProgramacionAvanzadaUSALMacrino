<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ include file="/WEB-INF/jsp/comun/taglibs.jspf" %>
<c:set var="tituloPagina" value="Error"/>
<%@ include file="/WEB-INF/jsp/comun/encabezado.jspf" %>
<div class="text-center mt-5">
    <c:choose>
        <c:when test="${pageContext.errorData.statusCode == 403}">
            <h1 class="h3">Acceso denegado</h1>
            <p class="text-muted">Tu perfil no tiene permiso para ver esta página.</p>
        </c:when>
        <c:when test="${pageContext.errorData.statusCode == 404}">
            <h1 class="h3">Página no encontrada</h1>
            <p class="text-muted">La dirección solicitada no existe.</p>
        </c:when>
        <c:otherwise>
            <h1 class="h3">Ocurrió un error inesperado</h1>
            <p class="text-muted">No pudimos completar la operación. Intentá de nuevo en unos minutos.</p>
        </c:otherwise>
    </c:choose>
    <a class="btn btn-primary" href="${ctx}/inicio">Volver al inicio</a>
</div>
<%@ include file="/WEB-INF/jsp/comun/pie.jspf" %>

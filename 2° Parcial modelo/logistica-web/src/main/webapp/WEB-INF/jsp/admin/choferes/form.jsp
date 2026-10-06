<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="es_AR"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Chofer</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/comun/menu.jsp"/>

<div class="container">
    <c:set var="esNuevo" value="${empty valores.id}"/>
    <h2><c:out value="${esNuevo ? 'Nuevo chofer' : 'Modificar chofer'}"/></h2>

    <form method="post" action="${pageContext.request.contextPath}/admin/choferes">
        <input type="hidden" name="accion" value="guardar">
        <input type="hidden" name="id" value="<c:out value='${valores.id}'/>">

        <div class="row">
            <div class="col-md-4 mb-3">
                <label class="form-label" for="nombre">Nombre</label>
                <input type="text" class="form-control" id="nombre" name="nombre" required
                       value="<c:out value='${valores.nombre}'/>">
            </div>
            <div class="col-md-4 mb-3">
                <label class="form-label" for="apellido">Apellido</label>
                <input type="text" class="form-control" id="apellido" name="apellido" required
                       value="<c:out value='${valores.apellido}'/>">
            </div>
            <div class="col-md-4 mb-3">
                <label class="form-label" for="dni">DNI</label>
                <input type="text" class="form-control" id="dni" name="dni" required pattern="\d{7,8}"
                       title="7 u 8 digitos" value="<c:out value='${valores.dni}'/>">
            </div>
        </div>
        <div class="row">
            <div class="col-md-4 mb-3">
                <label class="form-label" for="fechaNacimiento">Fecha de nacimiento</label>
                <input type="date" class="form-control" id="fechaNacimiento" name="fechaNacimiento" required
                       value="<c:out value='${valores.fechaNacimiento}'/>">
            </div>
            <div class="col-md-4 mb-3">
                <label class="form-label" for="telefono">Celular</label>
                <input type="text" class="form-control" id="telefono" name="telefono" required pattern="\d{8,15}"
                       title="Solo numeros, entre 8 y 15 digitos" value="<c:out value='${valores.telefono}'/>">
            </div>
            <div class="col-md-4 mb-3">
                <label class="form-label" for="categoria">Categoria</label>
                <select class="form-select" id="categoria" name="categoria" required>
                    <option value="">Seleccione...</option>
                    <c:forEach var="cat" items="${categorias}">
                        <option value="${cat}" ${valores.categoria eq cat ? 'selected' : ''}>
                            <c:out value="${cat.descripcion}"/>
                        </option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <%-- La clave inicial solo se pide al crear --%>
        <c:if test="${esNuevo}">
            <div class="row">
                <div class="col-md-4 mb-3">
                    <label class="form-label" for="password">Clave inicial</label>
                    <input type="password" class="form-control" id="password" name="password" required minlength="6">
                    <div class="form-text">El usuario para entrar sera su DNI.</div>
                </div>
            </div>
        </c:if>

        <div class="mb-3">
            <label class="form-label">Camiones autorizados</label>
            <c:forEach var="camion" items="${camiones}">
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" name="camionIds"
                           id="camion${camion.id}" value="${camion.id}"
                           ${camionesSeleccionados.contains(camion.id) ? 'checked' : ''}>
                    <label class="form-check-label" for="camion${camion.id}">
                        <c:out value="${camion.descripcion}"/>
                    </label>
                </div>
            </c:forEach>
            <c:if test="${empty camiones}"><p>No hay camiones cargados todavia.</p></c:if>
        </div>

        <button type="submit" class="btn btn-primary">Guardar</button>
        <a href="${pageContext.request.contextPath}/admin/choferes" class="btn btn-secondary">Cancelar</a>
    </form>
</div>

<jsp:include page="/WEB-INF/jsp/comun/scripts.jsp"/>
</body>
</html>

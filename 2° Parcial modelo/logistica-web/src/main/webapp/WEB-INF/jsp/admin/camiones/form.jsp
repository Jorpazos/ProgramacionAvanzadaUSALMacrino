<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="es_AR"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Camion</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/comun/menu.jsp"/>

<div class="container">
    <h2><c:out value="${empty valores.id ? 'Nuevo camion' : 'Modificar camion'}"/></h2>

    <form method="post" action="${pageContext.request.contextPath}/admin/camiones">
        <input type="hidden" name="accion" value="guardar">
        <input type="hidden" name="id" value="<c:out value='${valores.id}'/>">

        <div class="row">
            <div class="col-md-4 mb-3">
                <label class="form-label" for="marca">Marca</label>
                <input type="text" class="form-control" id="marca" name="marca" required
                       value="<c:out value='${valores.marca}'/>">
            </div>
            <div class="col-md-4 mb-3">
                <label class="form-label" for="modelo">Modelo</label>
                <input type="text" class="form-control" id="modelo" name="modelo" required
                       value="<c:out value='${valores.modelo}'/>">
            </div>
            <div class="col-md-4 mb-3">
                <label class="form-label" for="dominio">Dominio (patente)</label>
                <input type="text" class="form-control" id="dominio" name="dominio" required
                       value="<c:out value='${valores.dominio}'/>">
            </div>
        </div>
        <div class="row">
            <div class="col-md-4 mb-3">
                <label class="form-label" for="toneladasMaximas">Toneladas maximas</label>
                <input type="number" step="0.01" class="form-control" id="toneladasMaximas" name="toneladasMaximas" required
                       value="<c:out value='${valores.toneladasMaximas}'/>">
            </div>
            <div class="col-md-4 mb-3">
                <label class="form-label" for="capacidadTanqueLitros">Capacidad del tanque (litros)</label>
                <input type="number" step="0.01" class="form-control" id="capacidadTanqueLitros" name="capacidadTanqueLitros" required
                       value="<c:out value='${valores.capacidadTanqueLitros}'/>">
            </div>
            <div class="col-md-4 mb-3">
                <label class="form-label" for="consumoLitrosPorKm">Consumo (litros por km)</label>
                <input type="number" step="0.001" class="form-control" id="consumoLitrosPorKm" name="consumoLitrosPorKm" required
                       value="<c:out value='${valores.consumoLitrosPorKm}'/>">
            </div>
        </div>

        <button type="submit" class="btn btn-primary">Guardar</button>
        <a href="${pageContext.request.contextPath}/admin/camiones" class="btn btn-secondary">Cancelar</a>
    </form>
</div>

<jsp:include page="/WEB-INF/jsp/comun/scripts.jsp"/>
</body>
</html>

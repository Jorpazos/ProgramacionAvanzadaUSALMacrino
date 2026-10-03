<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Artículos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container py-4">

    <h2 class="mb-4"><c:out value="${appNombre}"/></h2>

    <c:if test="${not empty mensaje}">
        <div class="alert alert-success"><c:out value="${mensaje}"/></div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger"><c:out value="${error}"/></div>
    </c:if>

    <!-- Formulario de alta -->
    <div class="card shadow-sm mb-4">
        <div class="card-header bg-primary text-white">Cargar nuevo artículo</div>
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/articulos" method="post">
                <div class="row g-3">
                    <div class="col-md-3">
                        <label for="codigo" class="form-label">Código</label>
                        <input type="text" id="codigo" name="codigo" class="form-control"
                               maxlength="20" required value="<c:out value='${param.codigo}'/>">
                    </div>
                    <div class="col-md-5">
                        <label for="nombre" class="form-label">Nombre</label>
                        <input type="text" id="nombre" name="nombre" class="form-control"
                               maxlength="100" required value="<c:out value='${param.nombre}'/>">
                    </div>
                    <div class="col-md-2">
                        <label for="precio" class="form-label">Precio</label>
                        <input type="number" id="precio" name="precio" class="form-control"
                               step="0.01" min="0" required value="<c:out value='${param.precio}'/>">
                    </div>
                    <div class="col-md-2">
                        <label for="stock" class="form-label">Stock</label>
                        <input type="number" id="stock" name="stock" class="form-control"
                               min="0" required value="<c:out value='${param.stock}'/>">
                    </div>
                    <div class="col-12">
                        <label for="descripcion" class="form-label">Descripción</label>
                        <input type="text" id="descripcion" name="descripcion" class="form-control"
                               maxlength="255" value="<c:out value='${param.descripcion}'/>">
                    </div>
                </div>
                <button type="submit" class="btn btn-success mt-3">Guardar artículo</button>
            </form>
        </div>
    </div>

    <!-- Resumen (viene del Stored Procedure via CallableStatement) -->
    <c:if test="${not empty resumen}">
        <div class="row g-3 mb-4">
            <div class="col-md-4">
                <div class="card text-center shadow-sm"><div class="card-body">
                    <div class="text-muted">Artículos</div>
                    <div class="fs-3 fw-bold">${resumen.cantidadArticulos}</div>
                </div></div>
            </div>
            <div class="col-md-4">
                <div class="card text-center shadow-sm"><div class="card-body">
                    <div class="text-muted">Stock total</div>
                    <div class="fs-3 fw-bold">${resumen.stockTotal}</div>
                </div></div>
            </div>
            <div class="col-md-4">
                <div class="card text-center shadow-sm"><div class="card-body">
                    <div class="text-muted">Valor del inventario</div>
                    <div class="fs-3 fw-bold">
                        $ <fmt:formatNumber value="${resumen.valorTotal}" minFractionDigits="2" maxFractionDigits="2"/>
                    </div>
                </div></div>
            </div>
        </div>
    </c:if>

    <!-- Listado -->
    <h4 class="mb-3">Artículos cargados</h4>
    <c:choose>
        <c:when test="${empty articulos}">
            <div class="alert alert-info">No hay artículos cargados.</div>
        </c:when>
        <c:otherwise>
            <table class="table table-striped table-hover bg-white shadow-sm border">
                <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Código</th>
                    <th>Nombre</th>
                    <th>Descripción</th>
                    <th class="text-end">Precio</th>
                    <th class="text-end">Stock</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="a" items="${articulos}">
                    <tr>
                        <td>${a.id}</td>
                        <td><c:out value="${a.codigo}"/></td>
                        <td><c:out value="${a.nombre}"/></td>
                        <td><c:out value="${a.descripcion}"/></td>
                        <td class="text-end">
                            $ <fmt:formatNumber value="${a.precio}" minFractionDigits="2" maxFractionDigits="2"/>
                        </td>
                        <td class="text-end">${a.stock}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>

</div>
</body>
</html>

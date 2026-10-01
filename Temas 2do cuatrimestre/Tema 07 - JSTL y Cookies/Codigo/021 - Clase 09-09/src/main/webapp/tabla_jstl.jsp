<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="edu.usal.model.Producto" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><%--
  Created by IntelliJ IDEA.
  User: usal
  Date: 9/9/26
  Time: 8:19 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Tabla con JSTL</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
</head>
<%
    List<Producto> listaProductos = new ArrayList<>();
    listaProductos.add(new Producto("Teclado Mecanico", 45600.50, true));
    listaProductos.add(new Producto("Mouse Inalambrico", 12800.99, false));
    listaProductos.add(new Producto("Monitor 24' Widescreen", 210000.50, true));
    //pageContext.setAttribute("listaProductos", new ArrayList<>());
    pageContext.setAttribute("productos", listaProductos);
%>
<body>
    <div class="container">
        <h3 class="text-danger mb-3">Lista de productos</h3>

        <c:if test="${empty productos}">
            <p class="alert alert-info">No hay elementos en la lista</p>
        </c:if>

        <c:if test="${not empty productos}">
            <table class="table table-border bg-white shadow-sm">
                <thead class="table-dark">
                <tr>
                    <th>Producto</th>
                    <th>Precio</th>
                    <th>Estado</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="producto" items="${productos}">
                    <tr>
                        <td>${producto.nombre}</td>
                        <td>
                            <fmt:formatNumber value="${producto.precio}" type="currency" currencySymbol="$" />
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${producto.enStock}">
                                    <span class="badge bg-success">Disponible</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-danger">No disponible</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>

    </div>

</body>
</html>

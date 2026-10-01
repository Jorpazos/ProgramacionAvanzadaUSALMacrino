<%--
  Created by IntelliJ IDEA.
  User: usal
  Date: 16/9/26
  Time: 7:51 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Lista de productos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
</head>
<body>
    <h1>Listado de productos</h1>

    <c:choose>
        <c:when test="${empty requestScope.listado}">
            <div class="alert alert-info">
                <p>No existe productos cargados en la BD</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="container">
                <table class="table table-striped table-hover bg-white shadow-sm border">
                    <thead class="table-dark">
                    <tr>
                        <td>ID</td>
                        <td>Nombre</td>
                        <td>Precio</td>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="produ" items="${requestScope.listado}">
                        <tr>
                            <td>${produ.id}</td>
                            <td>${produ.nombre}</td>
                            <td>${produ.precio}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>


</body>
</html>

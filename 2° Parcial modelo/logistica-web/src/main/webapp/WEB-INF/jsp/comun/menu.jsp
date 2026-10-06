<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- Barra de navegacion: solo se muestra si hay un usuario logueado --%>
<c:if test="${not empty sessionScope.usuario}">
    <nav class="navbar navbar-expand navbar-dark bg-dark mb-4">
        <div class="container">
            <span class="navbar-brand">Logistica</span>
            <ul class="navbar-nav me-auto">
                <c:if test="${sessionScope.usuario.esAdmin()}">
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/viajes">Viajes</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/choferes">Choferes</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/camiones">Camiones</a></li>
                </c:if>
                <c:if test="${not sessionScope.usuario.esAdmin()}">
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/chofer/viajes">Mis viajes</a></li>
                </c:if>
            </ul>
            <span class="navbar-text me-3"><c:out value="${sessionScope.usuario.username}"/></span>
            <form method="post" action="${pageContext.request.contextPath}/logout">
                <button type="submit" class="btn btn-outline-light btn-sm">Salir</button>
            </form>
        </div>
    </nav>
</c:if>

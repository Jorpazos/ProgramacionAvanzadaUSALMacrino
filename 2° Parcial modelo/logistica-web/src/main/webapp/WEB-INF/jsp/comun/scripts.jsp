<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<%-- El aviso puede venir de la request (forward) o de la Session (redirect) --%>
<c:set var="avisoTipo" value="${not empty alertaTipo ? alertaTipo : sessionScope.flashTipo}"/>
<c:set var="avisoTexto" value="${not empty alertaTexto ? alertaTexto : sessionScope.flashTexto}"/>
<c:remove var="flashTipo" scope="session"/>
<c:remove var="flashTexto" scope="session"/>
<c:if test="${not empty avisoTexto}">
    <script>
        Swal.fire({
            icon: '<c:out value="${avisoTipo}"/>',
            text: '<c:out value="${avisoTexto}"/>'
        });
    </script>
</c:if>

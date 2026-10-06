<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Ingresar</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5 col-md-4">
    <h3 class="text-center mb-4">Logistica</h3>
    <form method="post" action="${pageContext.request.contextPath}/login">
        <div class="mb-3">
            <label class="form-label" for="username">Usuario</label>
            <input class="form-control" id="username" name="username" required autofocus
                   value="<c:out value='${username}'/>">
        </div>
        <div class="mb-3">
            <label class="form-label" for="password">Contrasena</label>
            <input class="form-control" id="password" name="password" type="password" required>
        </div>
        <div class="form-check mb-3">
            <input class="form-check-input" type="checkbox" id="recordar" name="recordar" checked>
            <label class="form-check-label" for="recordar">Recordarme</label>
        </div>
        <button class="btn btn-primary w-100" type="submit">Ingresar</button>
    </form>
</div>

<jsp:include page="/WEB-INF/jsp/comun/scripts.jsp"/>
</body>
</html>

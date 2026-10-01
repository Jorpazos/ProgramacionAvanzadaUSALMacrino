<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/comun/taglibs.jspf" %>
<c:set var="tituloPagina" value="Iniciar sesión"/>
<%@ include file="/WEB-INF/jsp/comun/encabezado.jspf" %>
<div class="card card-login shadow-sm mx-auto mt-5">
    <div class="card-body p-4">
        <h1 class="h4 text-center mb-1">Logística y Distribución</h1>
        <p class="text-center text-muted">Iniciá sesión para continuar</p>
        <form action="${ctx}/login" method="post">
            <div class="mb-3">
                <label for="username" class="form-label">Usuario</label>
                <input type="text" class="form-control" id="username" name="username" required maxlength="30"
                       autofocus autocomplete="username" value="<c:out value='${username}'/>">
            </div>
            <div class="mb-3">
                <label for="password" class="form-label">Contraseña</label>
                <input type="password" class="form-control" id="password" name="password" required
                       autocomplete="current-password">
            </div>
            <div class="form-check mb-3">
                <input class="form-check-input" type="checkbox" id="recordar" name="recordar" checked>
                <label class="form-check-label" for="recordar">Recordarme en este equipo</label>
            </div>
            <button type="submit" class="btn btn-primary w-100">Ingresar</button>
        </form>
    </div>
</div>
<%@ include file="/WEB-INF/jsp/comun/pie.jspf" %>

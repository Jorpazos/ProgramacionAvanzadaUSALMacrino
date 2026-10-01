<%--
  Created by IntelliJ IDEA.
  User: usal
  Date: 9/9/26
  Time: 7:16 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Destinos JSP</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
</head>
<%
    //String ultimoDestino = (String) session.getAttribute("ultimoDestino");
    String ultimoDestino = "";
    Cookie[] cookies = request.getCookies();

    if(cookies != null) {
        for(Cookie cookie : cookies) {
            if("ultimoDestino".equals(cookie.getName())){
                ultimoDestino = cookie.getValue();
                break;
            }
        }
    }

%>
<body>

    <div class="container">

        <div class="card-header">
            <p class="alert alert-info">
                Tu ultima busqueda guardad fue :
                <strong>
                    <%= ultimoDestino %>
                </strong>
            </p>
        </div>

        <div class="card-body">
            <form action="procesamiento.jsp" method="post">
                <div class="form-label">
                    <label for="selectDestino">¿Donde te gustaria ir?</label>
                </div>
                <select id=selectDestino" name="selectDestino" class="form-select">
                    <option value="">Seleccione un destino...</option>
                    <option value="bariloche">Bariloche</option>
                    <option value="madrid">Madrid</option>
                    <option value="cancún">Cancun</option>
                </select>
                <button type="submit" class="mb-2 btn btn-primary">Buscar </button>
            </form>
        </div>

        <div>
            <form method="post" action="eliminar.jsp">
                <button type="submit" class="btn btn-danger">Eliminar destino guardado</button>
            </form>
        </div>
    </div>

</body>
</html>

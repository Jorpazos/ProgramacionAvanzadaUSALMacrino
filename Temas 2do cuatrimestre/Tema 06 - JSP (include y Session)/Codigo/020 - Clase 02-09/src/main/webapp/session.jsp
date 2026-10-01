<%--
  Created by IntelliJ IDEA.
  User: usal
  Date: 2/9/26
  Time: 7:57 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Session en JSP</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
</head>
<body class="bg-light p-5">
    <div class="container">
        <div class="card shadow border-danger">
            <div class="card-header bg-danger text-white">
                <h4 class="mb-0">Ejemplo de contador</h4>
            </div>
        </div>
        <div class="card-body text-center mb-1">
            <%
                int contador = 0;

                Integer contadorSession = (Integer) session.getAttribute("miContador");

                session.setMaxInactiveInterval(5000);

                if(contadorSession != null && contadorSession > 0) {
                    contadorSession = contadorSession + 1;
                    contador = contadorSession;
                    session.setAttribute("miContador", contadorSession);
                } else {

                    contador = contador + 1;
                    session.setAttribute("miContador", contador);
                }

                /*Borro valor del atributo*/
                session.removeAttribute("miContador");
                /*Elimina la sesion completa*/
                session.invalidate();
            %>
            <p class="fs-4">Valor del contador: </p>
            <h4>
                <%= contador %>
            </h4>
            <p>Session ID : <%= session.getId() %></p>
            <a class="btn btn-outline-danger" href="session.jsp">Incrementar +1</a>
        </div>
    </div>

</body>
</html>

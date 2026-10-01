<%--
  Created by IntelliJ IDEA.
  User: usal
  Date: 26/8/26
  Time: 8:31 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Introduccion JSP</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
</head>
<body>
    <h2>Pagina 2 : Procesado de Formulario por JSP</h2>

    <%
        String txtNombre = request.getParameter("txtNombre");
        String txtEdad = request.getParameter("txtEdad");

        int edad = 0;
        if(txtEdad != null || !txtEdad.isEmpty()){
            edad = Integer.parseInt(txtEdad);
        }

    %>

    <h2>¡Hola, <%= txtNombre %> </h2>
    <%--<h4>¡Hola, <%= out.print(txtNombre) %> </h4>--%>

    <%
        if(edad >= 18) {
            out.println("<p> Sos mayor edad </p>");
        } else {
            out.println("<p> Sos menor edad </p>");
        }
    %>

    <a href="formulario.html" class="btn btn-primary">Volver al formulario</a>
</body>
</html>

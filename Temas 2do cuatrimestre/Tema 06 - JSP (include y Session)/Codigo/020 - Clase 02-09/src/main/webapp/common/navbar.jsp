<%--
  Created by IntelliJ IDEA.
  User: usal
  Date: 2/9/26
  Time: 7:20 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
</head>
<%
    String classActive = request.getParameter("classActive");
%>

<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-4">
        <div class="container">
            <a class="navbar-brand" href="inicio.jsp">Portal USAL 2026</a>
            <div class="navbar-nav">
                <a class="nav-link <%= classActive %>" href="inicio.jsp">Inicio</a>
                <%
                    String active = request.getParameter("isContacto");



                    if(active != null && active.equals("true")){
                        out.println("<a class=\"nav-link active\"  href=\"contacto.jsp\">Contacto</a>");
                    } else {
                        out.println("<a class=\"nav-link\"  href=\"contacto.jsp\">Contacto</a>");
                    }
                %>
            </div>
        </div>
    </nav>
</body>
</html>

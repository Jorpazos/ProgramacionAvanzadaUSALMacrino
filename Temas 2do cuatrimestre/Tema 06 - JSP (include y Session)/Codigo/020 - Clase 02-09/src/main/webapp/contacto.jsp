<html>
<head>
    <title>Contacto</title>
</head>
<body class="bg-light">

   <%-- <nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-4">
        <div class="container">
            <a class="navbar-brand" href="inicio.jsp">Portal USAL 2026</a>
            <div class="navbar-nav">
                <a class="nav-link " href="inicio.jsp">Inicio</a>
                <a class="nav-link active" href="contacto.jsp">Contacto</a>
            </div>
        </div>

    </nav>--%>
   <%--Include dinamico--%>
   <jsp:include page="common/navbar.jsp">
        <jsp:param name="isContacto" value="true"/>
   </jsp:include>

    <div class="container">
        <h1>Pagina de contacto</h1>
        <p>Contactese con mail@mail.com</p>
    </div>
</body>
</html>
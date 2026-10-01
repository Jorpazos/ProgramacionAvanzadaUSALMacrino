<html>
<head> 
    <title>Inicio</title>
</head>
<body class="bg-light">

    <%--<nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-4">
        <div class="container">
            <a class="navbar-brand" href="inicio.jsp">Portal USAL 2026</a>
            <div class="navbar-nav">
                <a class="nav-link active" href="inicio.jsp">Inicio</a>
                <a class="nav-link" href="contacto.jsp">Contacto</a>
            </div>
        </div>

    </nav>--%>
    <%--Include estatico--%>
<%--    <%@ include file="common/navbar.jsp"%>--%>
    <jsp:include page="common/navbar.jsp">
        <jsp:param name="classActive" value="active"/>
    </jsp:include>

    <div class="container">
        <h1>Pagino de inicio</h1>
        <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Alias assumenda debitis optio quia quod ratione rem saepe vel vero voluptate! Asperiores deserunt illum laudantium numquam quia, quo repellendus velit voluptates!</p>
    </div>


</body>
</html>
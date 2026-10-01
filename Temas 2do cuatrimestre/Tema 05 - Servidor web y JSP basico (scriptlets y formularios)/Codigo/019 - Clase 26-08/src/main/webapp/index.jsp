<%@ page import="java.time.LocalTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
</head>
<body>

<%
    //Scriptlets
    LocalTime horaActual = LocalTime.now();
    int hora = horaActual.getHour();
    String msjSaludo;
    String classColor;

    if(hora >= 6 && hora < 12){
        msjSaludo = "Buenos dias";
        classColor = "alert-warning";
    } else if(hora >= 12 && hora < 20) {
        msjSaludo = "Buenas tardes";
        classColor = "alert-info";
    } else {
        msjSaludo = "Buenas noches";
        classColor = "alert-dark";
    }
%>

    <div class="container">
        <%-- Comentario JSP --%>
        <!-- Comentario HTML -->
        <h2>JSP Clase 26-08</h2>

        <div class="alert <%= classColor %>">
            <%= msjSaludo %>
        </div>

        <p>
            La hora del servidor es :
            <strong>
                <%= horaActual.format(DateTimeFormatter.ofPattern("HH:mm:ss"))%>
            </strong>
        </p>
    </div>
</body>
</html>

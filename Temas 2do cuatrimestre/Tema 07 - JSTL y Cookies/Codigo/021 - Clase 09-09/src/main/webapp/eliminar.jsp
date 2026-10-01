<%--
  Created by IntelliJ IDEA.
  User: usal
  Date: 9/9/26
  Time: 7:30 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<%
    //session.setAttribute("ultimoDestino", null);
    //session.invalidate();
    //session.removeAttribute("ultimoDestino");

    Cookie cookie = new Cookie("ultimoDestino", "");

    cookie.setMaxAge(0);

    response.addCookie(cookie);

    response.sendRedirect("destinos.jsp");
%>
<body>

</body>
</html>

<%@ page import="java.util.Locale" %>
<%@ page import="java.net.URLEncoder" %><%--
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
    String selectDestino = request.getParameter("selectDestino");

    if(selectDestino != null && !selectDestino.trim().isEmpty()){
        String strSanitizado = URLEncoder.encode(selectDestino, "UTF-8");

        //Crear la cookie
        Cookie cookie = new Cookie("ultimoDestino", strSanitizado);

        cookie.setMaxAge(60 * 60 * 24 * 7);

        //Agregarla al response
        response.addCookie(cookie);
    }

    //session.setAttribute("ultimoDestino", selectDestino);

    response.sendRedirect("destinos.jsp");
%>
<body>

</body>
</html>

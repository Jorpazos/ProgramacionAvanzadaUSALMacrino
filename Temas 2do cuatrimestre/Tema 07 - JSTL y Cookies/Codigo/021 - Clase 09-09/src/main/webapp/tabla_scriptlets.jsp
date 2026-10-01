<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="edu.usal.model.Producto" %><%--
  Created by IntelliJ IDEA.
  User: usal
  Date: 9/9/26
  Time: 8:19 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Tabla con Scriptlets</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
</head>
<%
    List<Producto> productos = new ArrayList<>();
    productos.add(new Producto("Teclado Mecanico", 45600.50, true));
    productos.add(new Producto("Mouse Inalambrico", 12800.99, true));
    productos.add(new Producto("Monitor 24' Widescreen", 210000.50, true));
%>
<body>
    <div class="container">
        <h3 class="text-danger mb-3">Lista de productos</h3>

        <table class="table table-border bg-white shadow-sm">
            <thead class="table-dark">
                <tr>
                    <th>Producto</th>
                    <th>Precio</th>
                    <th>Estado</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for(Producto producto : productos) {
                        out.println("<tr>");
                            out.println("<td>");
                            out.println(producto.getNombre());
                            out.println("</td>");
                            out.println("<td>");
                            out.println(producto.getPrecio());
                            out.println("</td>");
                            out.println("<td>");
                            out.println(producto.isEnStock());
                            out.println("</td>");
                        out.println("</tr>");
                    }
                %>
            </tbody>
        </table>
    </div>

</body>
</html>

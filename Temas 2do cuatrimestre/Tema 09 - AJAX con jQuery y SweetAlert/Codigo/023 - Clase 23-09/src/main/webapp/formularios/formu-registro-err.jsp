<%--
  Created by IntelliJ IDEA.
  User: usal
  Date: 23/9/26
  Time: 1:23 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Clase 23-09 | Demo Ajax</title>
    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- SweetAlert2 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css" rel="stylesheet">
    <!-- Librerías Externas (CDN) AJAX -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body class="bg-light p-5">

<div class="container" style="max-width: 450px;">
    <div class="card shadow">
        <div class="card-header bg-primary text-white">
            <h5 class="mb-0">Registrar Cliente</h5>
        </div>
        <div class="card-body">

            <!-- Formulario -->
            <form id="formCliente">
                <div class="mb-3">
                    <label for="nombre" class="form-label">Nombre Completo:</label>
                    <input type="text" id="nombre" name="nombre" class="form-control" >
                </div>

                <div class="mb-3">
                    <label for="email" class="form-label">Correo Electrónico:</label>
                    <input type="email" id="email" name="email" class="form-control" >
                </div>

                <button type="submit" class="btn btn-success w-100">Guardar Cliente</button>
                <button type="button" class="my-3 btn btn-secondary w-100" onclick="alert('Mensaje sin SweetAlert')">Mensaje Clasico</button>
            </form>

            </div>
        </div>
    </div>

   <script src="../js/cliente-err.js"></script>
</body>
</html>

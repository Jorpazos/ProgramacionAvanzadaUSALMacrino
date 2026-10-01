$(document).ready(function() {

    $('#formCliente').on('submit', function(e) {

        e.preventDefault();


        $.ajax({
            url: contextPath + '/registro',
            type: 'POST',
            data: $(this).serialize(), // Empaqueta 'nombre' y 'email'
            dataType: 'text',          // Esperamos respuesta de tipo TEXTO
            success: function(respuestaTexto) {

                // 3. Evaluamos la respuesta de texto recibida desde el Servlet
                if (respuestaTexto.startsWith("OK:")) {

                    // Extraemos el mensaje removiendo el prefijo "OK:"
                    let mensajeExito = respuestaTexto.replace("OK:", "");

                    // Mostramos el diálogo emergente de SweetAlert2
                    Swal.fire({
                        title: '¡Éxito!',
                        text: mensajeExito,
                        icon: 'success',
                        confirmButtonText: 'Aceptar'
                    }).then((result) => {
                        // Al presionar "Aceptar", limpiamos el formulario
                        if (result.isConfirmed) {
                            $('#formCliente')[0].reset();
                        }
                    });

                } else {

                    // Extraemos el mensaje de error removiendo el prefijo "ERROR:"
                    let mensajeError = respuestaTexto.replace("ERROR:", "");

                    Swal.fire({
                        title: 'Atención',
                        text: mensajeError,
                        icon: 'warning'
                    });

                }
            },
            error: function() {
                Swal.fire({
                    title: 'Error',
                    text: 'No se pudo comunicar con el servidor.',
                    icon: 'error'
                });
            }
        });
    });

});
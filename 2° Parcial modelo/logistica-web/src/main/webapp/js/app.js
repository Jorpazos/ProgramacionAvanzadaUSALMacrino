// Comportamiento comun a todas las paginas (JavaScript + SweetAlert, ambos opcionales).
(function () {
    'use strict';

    var iconos = { success: 'success', error: 'error', warning: 'warning', info: 'info' };

    // Muestra el mensaje que dejo el servidor (div#alerta-servidor) como ventana SweetAlert
    function mostrarAlerta() {
        var alerta = document.getElementById('alerta-servidor');
        if (!alerta || !alerta.dataset.texto) {
            return;
        }
        var tipo = alerta.dataset.tipo || 'info';
        if (window.Swal) {
            Swal.fire({ icon: iconos[tipo] || 'info', text: alerta.dataset.texto, confirmButtonText: 'Aceptar' });
        } else {
            alerta.classList.remove('d-none'); // sin SweetAlert queda visible el aviso de Bootstrap
        }
    }

    // Pide confirmacion antes de enviar los formularios marcados con data-confirmar
    function confirmarFormularios() {
        document.querySelectorAll('form[data-confirmar]').forEach(function (form) {
            form.addEventListener('submit', function (evento) {
                if (form.dataset.confirmado === 'si') {
                    return;
                }
                evento.preventDefault();
                var mensaje = form.dataset.confirmar;
                if (window.Swal) {
                    Swal.fire({
                        icon: 'question', text: mensaje, showCancelButton: true,
                        confirmButtonText: 'Sí, continuar', cancelButtonText: 'Cancelar'
                    }).then(function (resultado) {
                        if (resultado.isConfirmed) {
                            form.dataset.confirmado = 'si';
                            form.submit();
                        }
                    });
                } else if (window.confirm(mensaje)) {
                    form.dataset.confirmado = 'si';
                    form.submit();
                }
            });
        });
    }

    document.addEventListener('DOMContentLoaded', function () {
        mostrarAlerta();
        confirmarFormularios();
    });
})();

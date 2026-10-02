package ar.edu.usal.logistica.web.controlador;

import ar.edu.usal.logistica.modelo.Usuario;
import ar.edu.usal.logistica.web.SesionUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Punto de entrada despues de autenticarse: deriva a cada perfil a su pantalla. */
@WebServlet(name = "InicioServlet", urlPatterns = "/inicio")
public class InicioServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Usuario usuario = SesionUtil.usuarioActual(req);
        redirigir(req, resp, usuario.esAdmin() ? "/admin/viajes" : "/chofer/viajes");
    }
}

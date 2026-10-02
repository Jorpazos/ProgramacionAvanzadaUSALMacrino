package ar.edu.usal.logistica.web.controlador;

import ar.edu.usal.logistica.web.SesionUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Clase base de los controladores (herencia): junta las utilidades que todos
 * necesitan para no repetir codigo. Las vistas JSP viven en WEB-INF, por lo que
 * nadie puede abrirlas directamente: siempre se pasa por un controlador.
 */
public abstract class BaseServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /** Reenvia (forward) a una vista JSP. */
    protected void vista(HttpServletRequest req, HttpServletResponse resp, String jsp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/" + jsp).forward(req, resp);
    }

    /** Redirige dentro de la aplicacion (patron Post/Redirect/Get). */
    protected void redirigir(HttpServletRequest req, HttpServletResponse resp, String ruta) throws IOException {
        resp.sendRedirect(req.getContextPath() + ruta);
    }

    /** Mensaje para la proxima pagina (sobrevive a un redirect porque vive en la Session). */
    protected void mensajeFlash(HttpServletRequest req, String tipo, String texto) {
        req.getSession().setAttribute("flashTipo", tipo);
        req.getSession().setAttribute("flashTexto", texto);
    }

    /** Mensaje para la pagina que se esta por mostrar ahora mismo (forward). */
    protected void mensajeAhora(HttpServletRequest req, String tipo, String texto) {
        req.setAttribute("alertaTipo", tipo);
        req.setAttribute("alertaTexto", texto);
    }

    protected Long parametroLong(HttpServletRequest req, String nombre) {
        String valor = req.getParameter(nombre);
        if (valor == null || valor.isBlank()) {
            return null;
        }
        try {
            return Long.valueOf(valor.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    protected String parametro(HttpServletRequest req, String nombre) {
        String valor = req.getParameter(nombre);
        return valor == null ? "" : valor.trim();
    }

    protected Long choferIdDeSesion(HttpServletRequest req) {
        return SesionUtil.usuarioActual(req).getChoferId();
    }
}

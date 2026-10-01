package ar.edu.usal.logistica.web.controlador;

import ar.edu.usal.logistica.dao.DAOFactory;
import ar.edu.usal.logistica.excepcion.DAOException;
import ar.edu.usal.logistica.web.SesionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Cierre de sesion (declarado en web.xml). Limpia la Session y las cookies,
 * como pide el enunciado. Solo acepta POST porque cambia el estado del sistema.
 */
public class LogoutServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Una URL tipeada no debe cerrar la sesion: se vuelve al inicio
        redirigir(req, resp, "/inicio");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // El token recordado tambien se elimina de la base para que no pueda reutilizarse
            String token = SesionUtil.tokenRecordado(req);
            if (token != null) {
                DAOFactory.getInstance().getUsuarioDAO().eliminarToken(token);
            }
        } catch (DAOException e) {
            throw new ServletException(e);
        } finally {
            HttpSession sesion = req.getSession(false);
            if (sesion != null) {
                sesion.invalidate(); // destruye la Session y todos sus atributos
            }
            SesionUtil.limpiarCookies(req, resp); // borra del navegador la cookie de sesion y la de recordarme
        }
        redirigir(req, resp, "/login");
    }
}

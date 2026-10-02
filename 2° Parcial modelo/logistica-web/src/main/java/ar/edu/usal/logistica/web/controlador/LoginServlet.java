package ar.edu.usal.logistica.web.controlador;

import ar.edu.usal.logistica.dao.DAOFactory;
import ar.edu.usal.logistica.dao.UsuarioDAO;
import ar.edu.usal.logistica.excepcion.DAOException;
import ar.edu.usal.logistica.modelo.Usuario;
import ar.edu.usal.logistica.util.PasswordUtil;
import ar.edu.usal.logistica.web.SesionUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Pantalla principal de inicio de sesion. Esta clase se declara en web.xml
 * (no usa @WebServlet). GET muestra el formulario; POST autentica.
 */
public class LoginServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (SesionUtil.usuarioActual(req) != null) {
            redirigir(req, resp, "/inicio");
            return;
        }
        vista(req, resp, "login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = parametro(req, "username");
        String password = req.getParameter("password");
        boolean recordar = "on".equals(req.getParameter("recordar"));

        try {
            UsuarioDAO usuarioDAO = DAOFactory.getInstance().getUsuarioDAO();
            Usuario usuario = usuarioDAO.buscarPorUsername(username);
            // Mismo mensaje si falla el usuario o la clave: no se revela cual de los dos es incorrecto
            if (usuario == null || !PasswordUtil.verificar(password, usuario.getPasswordHash())) {
                req.setAttribute("username", username);
                mensajeAhora(req, "error", "Usuario o contraseña incorrectos.");
                vista(req, resp, "login.jsp");
                return;
            }

            // Se cambia el id de Session al autenticar para evitar la fijacion de sesion
            HttpSession sesion = req.getSession(false);
            if (sesion != null) {
                req.changeSessionId();
            } else {
                sesion = req.getSession(true);
            }
            sesion.setAttribute(SesionUtil.ATRIBUTO_USUARIO, usuario);

            if (recordar) {
                String token = PasswordUtil.generarToken();
                usuarioDAO.guardarToken(token, usuario.getId(), LocalDateTime.now().plusDays(SesionUtil.DIAS_RECORDAR));
                SesionUtil.guardarCookie(req, resp, token);
            }
            redirigir(req, resp, "/inicio");
        } catch (DAOException e) {
            throw new ServletException(e);
        }
    }
}

package ar.edu.usal.logistica.web.filtro;

import ar.edu.usal.logistica.dao.DAOFactory;
import ar.edu.usal.logistica.excepcion.DAOException;
import ar.edu.usal.logistica.modelo.Usuario;
import ar.edu.usal.logistica.web.SesionUtil;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filtro de seguridad (declarado en web.xml). Se ejecuta antes de cualquier servlet y:
 * 1) deja pasar solo las paginas publicas (login y recursos estaticos);
 * 2) si no hay usuario en Session, intenta recuperarlo con la cookie "recordarme";
 * 3) verifica que el perfil alcance: /admin/* solo ADMIN, /chofer/* solo CHOFER.
 */
public class AutenticacionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String ruta = req.getRequestURI().substring(req.getContextPath().length());

        if (esPublica(ruta)) {
            chain.doFilter(req, resp);
            return;
        }

        Usuario usuario = SesionUtil.usuarioActual(req);
        if (usuario == null) {
            usuario = recuperarDesdeCookie(req);
        }
        if (usuario == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Las paginas privadas no deben quedar en cache: al cerrar sesion, "Atras" no las muestra
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);

        boolean rutaAdmin = ruta.equals("/admin") || ruta.startsWith("/admin/");
        boolean rutaChofer = ruta.equals("/chofer") || ruta.startsWith("/chofer/");
        if ((rutaAdmin && !usuario.esAdmin()) || (rutaChofer && usuario.esAdmin())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        chain.doFilter(req, resp);
    }

    private boolean esPublica(String ruta) {
        return ruta.equals("/login") || ruta.startsWith("/css/") || ruta.startsWith("/js/");
    }

    /** Si la cookie trae un token vigente, se reconstruye la Session sin pedir contrasena. */
    private Usuario recuperarDesdeCookie(HttpServletRequest req) throws ServletException {
        String token = SesionUtil.tokenRecordado(req);
        if (token == null || token.isEmpty()) {
            return null;
        }
        try {
            Usuario usuario = DAOFactory.getInstance().getUsuarioDAO().buscarPorToken(token);
            if (usuario != null) {
                HttpSession sesion = req.getSession(true);
                sesion.setAttribute(SesionUtil.ATRIBUTO_USUARIO, usuario);
            }
            return usuario;
        } catch (DAOException e) {
            throw new ServletException(e);
        }
    }
}

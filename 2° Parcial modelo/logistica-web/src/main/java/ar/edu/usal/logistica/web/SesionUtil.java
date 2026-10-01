package ar.edu.usal.logistica.web;

import ar.edu.usal.logistica.modelo.Usuario;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Centraliza el manejo de la Session y de la cookie "recordarme" para que
 * el filtro, el login y el logout usen exactamente las mismas reglas.
 */
public final class SesionUtil {

    /** Nombre del atributo de Session donde vive el usuario autenticado. */
    public static final String ATRIBUTO_USUARIO = "usuario";
    /** Nombre de la cookie con el token de sesion recordada. */
    public static final String COOKIE_RECORDARME = "RECORDARME";
    /** Dias que se recuerda la sesion. */
    public static final int DIAS_RECORDAR = 30;

    private SesionUtil() {
    }

    public static Usuario usuarioActual(HttpServletRequest req) {
        HttpSession sesion = req.getSession(false);
        return sesion == null ? null : (Usuario) sesion.getAttribute(ATRIBUTO_USUARIO);
    }

    /** Devuelve el valor de la cookie "recordarme" o null si no vino en la request. */
    public static String tokenRecordado(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (COOKIE_RECORDARME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /** Crea la cookie persistente. HttpOnly: JavaScript no puede leerla; SameSite=Lax: no viaja en POST de otros sitios. */
    public static void guardarCookie(HttpServletRequest req, HttpServletResponse resp, String token) {
        resp.addCookie(crearCookie(req, token, DIAS_RECORDAR * 24 * 60 * 60));
    }

    /** Borra del navegador la cookie de recordarme y la de la Session (JSESSIONID). */
    public static void limpiarCookies(HttpServletRequest req, HttpServletResponse resp) {
        resp.addCookie(crearCookie(req, "", 0));
        Cookie jsession = new Cookie("JSESSIONID", "");
        jsession.setPath(rutaCookie(req));
        jsession.setMaxAge(0);
        jsession.setHttpOnly(true);
        resp.addCookie(jsession);
    }

    private static Cookie crearCookie(HttpServletRequest req, String valor, int segundos) {
        Cookie cookie = new Cookie(COOKIE_RECORDARME, valor);
        cookie.setHttpOnly(true);
        cookie.setSecure(req.isSecure());
        cookie.setPath(rutaCookie(req));
        cookie.setMaxAge(segundos);
        cookie.setAttribute("SameSite", "Lax");
        return cookie;
    }

    private static String rutaCookie(HttpServletRequest req) {
        String contexto = req.getContextPath();
        return contexto.isEmpty() ? "/" : contexto;
    }
}

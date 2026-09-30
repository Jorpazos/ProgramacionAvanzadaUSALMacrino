package ar.edu.usal.logistica.web.controlador;

import ar.edu.usal.logistica.dao.DAOFactory;
import ar.edu.usal.logistica.dao.ViajeDAO;
import ar.edu.usal.logistica.excepcion.DAOException;
import ar.edu.usal.logistica.excepcion.ValidacionException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Pantalla del perfil chofer: solo ve SUS viajes y puede iniciarlos y
 * marcarlos como realizados. El id del chofer sale siempre de la Session,
 * nunca de un parametro, para que nadie pueda tocar viajes ajenos.
 */
@WebServlet(name = "MisViajesServlet", urlPatterns = "/chofer/viajes")
public class MisViajesServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;

    private final ViajeDAO viajeDAO = DAOFactory.getInstance().getViajeDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("viajes", viajeDAO.listarPorChofer(choferIdDeSesion(req)));
            vista(req, resp, "chofer/mis-viajes.jsp");
        } catch (DAOException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = parametro(req, "accion");
        Long viajeId = parametroLong(req, "viajeId");
        try {
            if (viajeId == null || !("iniciar".equals(accion) || "finalizar".equals(accion))) {
                throw new ValidacionException("Solicitud inválida.");
            }
            if ("iniciar".equals(accion)) {
                viajeDAO.iniciar(viajeId, choferIdDeSesion(req));
                mensajeFlash(req, "success", "Viaje iniciado. ¡Buen camino!");
            } else {
                viajeDAO.finalizar(viajeId, choferIdDeSesion(req));
                mensajeFlash(req, "success", "Viaje marcado como realizado.");
            }
        } catch (ValidacionException e) {
            mensajeFlash(req, "warning", e.getMessage());
        } catch (DAOException e) {
            throw new ServletException(e);
        }
        redirigir(req, resp, "/chofer/viajes");
    }
}

package ar.edu.usal.logistica.web.controlador;

import ar.edu.usal.logistica.dao.CamionDAO;
import ar.edu.usal.logistica.dao.ChoferDAO;
import ar.edu.usal.logistica.dao.DAOFactory;
import ar.edu.usal.logistica.dao.DistanciaDAO;
import ar.edu.usal.logistica.dao.ViajeDAO;
import ar.edu.usal.logistica.excepcion.ChoferNoEncontradoException;
import ar.edu.usal.logistica.excepcion.DAOException;
import ar.edu.usal.logistica.excepcion.ValidacionException;
import ar.edu.usal.logistica.modelo.Camion;
import ar.edu.usal.logistica.modelo.Chofer;
import ar.edu.usal.logistica.modelo.Destino;
import ar.edu.usal.logistica.modelo.Viaje;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Carga de viajes (solo administrador). El alta se hace en pasos, tal como
 * describe el enunciado, y cada paso es una consulta (GET):
 * 1) buscar el chofer por DNI; 2) elegir camion disponible, origen y destino;
 * 3) ver el calculo de tiempo y tanques y confirmar (POST).
 */
@WebServlet(name = "ViajeServlet", urlPatterns = "/admin/viajes")
public class ViajeServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;

    private final ViajeDAO viajeDAO = DAOFactory.getInstance().getViajeDAO();
    private final ChoferDAO choferDAO = DAOFactory.getInstance().getChoferDAO();
    private final CamionDAO camionDAO = DAOFactory.getInstance().getCamionDAO();
    private final DistanciaDAO distanciaDAO = DAOFactory.getInstance().getDistanciaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if ("nuevo".equals(parametro(req, "accion"))) {
                nuevo(req, resp);
            } else {
                req.setAttribute("viajes", viajeDAO.listarTodos());
                vista(req, resp, "admin/viajes/lista.jsp");
            }
        } catch (DAOException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!"guardar".equals(parametro(req, "accion"))) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción desconocida");
            return;
        }
        String dni = parametro(req, "dni");
        String volver = "/admin/viajes?accion=nuevo&dni=" + URLEncoder.encode(dni, StandardCharsets.UTF_8);
        try {
            // Nada de lo que muestra la pantalla se da por cierto: se vuelve a consultar y a calcular todo
            Chofer chofer = choferDAO.buscarPorDni(dni);
            Viaje viaje = armarViaje(req, chofer);
            viajeDAO.insertar(viaje);
            mensajeFlash(req, "success", "Viaje cargado para " + chofer.getNombreCompleto() + ": "
                    + viaje.getOrigen().getNombre() + " → " + viaje.getDestino().getNombre() + ".");
            redirigir(req, resp, "/admin/viajes");
        } catch (ChoferNoEncontradoException e) {
            mensajeFlash(req, "error", e.getMessage());
            redirigir(req, resp, volver);
        } catch (ValidacionException e) {
            mensajeFlash(req, "warning", e.getMessage());
            redirigir(req, resp, volver);
        } catch (DAOException e) {
            throw new ServletException(e);
        }
    }

    private void nuevo(HttpServletRequest req, HttpServletResponse resp)
            throws DAOException, ServletException, IOException {
        String dni = parametro(req, "dni");
        req.setAttribute("dni", dni);
        req.setAttribute("destinos", Destino.values());
        req.setAttribute("tabla", distanciaDAO.obtenerTabla());
        req.setAttribute("origenElegido", parametro(req, "origen"));
        req.setAttribute("destinoElegido", parametro(req, "destino"));
        req.setAttribute("camionElegido", parametroLong(req, "camionId"));

        if (!dni.isEmpty()) {
            try {
                Chofer chofer = choferDAO.buscarPorDni(dni);
                req.setAttribute("chofer", chofer);
                List<Camion> disponibles = camionDAO.listarDisponiblesParaChofer(chofer.getId());
                req.setAttribute("camiones", disponibles);
                if (disponibles.isEmpty()) {
                    mensajeAhora(req, "warning", "El chofer no tiene camiones disponibles: no está autorizado "
                            + "a ninguno, o todos sus camiones están en viaje.");
                } else if (parametroLong(req, "camionId") != null && !parametro(req, "origen").isEmpty()
                        && !parametro(req, "destino").isEmpty()) {
                    // Ya hay toda la informacion: se calcula y se muestra el resumen para confirmar
                    req.setAttribute("viaje", armarViaje(req, chofer));
                }
            } catch (ChoferNoEncontradoException e) {
                mensajeAhora(req, "error", e.getMessage());
            } catch (ValidacionException e) {
                mensajeAhora(req, "warning", e.getMessage());
            }
        }
        vista(req, resp, "admin/viajes/nuevo.jsp");
    }

    /** Arma el viaje validando que el camion este realmente disponible para ese chofer. */
    private Viaje armarViaje(HttpServletRequest req, Chofer chofer) throws DAOException, ValidacionException {
        Long camionId = parametroLong(req, "camionId");
        Camion camion = null;
        if (camionId != null) {
            for (Camion c : camionDAO.listarDisponiblesParaChofer(chofer.getId())) {
                if (c.getId().equals(camionId)) {
                    camion = c;
                }
            }
        }
        if (camion == null) {
            throw new ValidacionException("El camión elegido no está disponible para este chofer.");
        }
        Destino origen = destino(parametro(req, "origen"));
        Destino destino = destino(parametro(req, "destino"));
        int km = distanciaDAO.obtenerKm(origen, destino);
        return Viaje.nuevo(chofer, camion, origen, destino, km);
    }

    private Destino destino(String codigo) throws ValidacionException {
        try {
            return Destino.valueOf(codigo);
        } catch (IllegalArgumentException e) {
            throw new ValidacionException("Seleccione un origen y un destino válidos.");
        }
    }
}

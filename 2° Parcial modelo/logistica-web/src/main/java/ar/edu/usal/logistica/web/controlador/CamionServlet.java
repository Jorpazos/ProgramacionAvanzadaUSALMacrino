package ar.edu.usal.logistica.web.controlador;

import ar.edu.usal.logistica.dao.CamionDAO;
import ar.edu.usal.logistica.dao.DAOFactory;
import ar.edu.usal.logistica.excepcion.DAOException;
import ar.edu.usal.logistica.excepcion.ValidacionException;
import ar.edu.usal.logistica.modelo.Camion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * ABM de camiones (solo administrador). GET consulta (lista, formulario);
 * POST modifica datos (guardar, eliminar).
 */
@WebServlet(name = "CamionServlet", urlPatterns = "/admin/camiones")
public class CamionServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;

    private final CamionDAO camionDAO = DAOFactory.getInstance().getCamionDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = parametro(req, "accion");
        try {
            if ("nuevo".equals(accion)) {
                req.setAttribute("valores", new HashMap<String, String>());
                vista(req, resp, "admin/camiones/form.jsp");
            } else if ("editar".equals(accion)) {
                Camion camion = buscarCamion(req);
                if (camion == null) {
                    mensajeFlash(req, "error", "El camión solicitado no existe.");
                    redirigir(req, resp, "/admin/camiones");
                    return;
                }
                req.setAttribute("valores", aMapa(camion));
                vista(req, resp, "admin/camiones/form.jsp");
            } else {
                req.setAttribute("camiones", camionDAO.listarTodos());
                vista(req, resp, "admin/camiones/lista.jsp");
            }
        } catch (DAOException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = parametro(req, "accion");
        try {
            if ("guardar".equals(accion)) {
                guardar(req, resp);
            } else if ("eliminar".equals(accion)) {
                eliminar(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción desconocida");
            }
        } catch (DAOException e) {
            throw new ServletException(e);
        }
    }

    private void guardar(HttpServletRequest req, HttpServletResponse resp)
            throws DAOException, ServletException, IOException {
        Long id = parametroLong(req, "id");
        try {
            Camion camion = new Camion(id, parametro(req, "marca"), parametro(req, "modelo"),
                    parametro(req, "dominio"), numero(req, "toneladasMaximas", "las toneladas máximas"),
                    numero(req, "capacidadTanqueLitros", "la capacidad del tanque"),
                    numero(req, "consumoLitrosPorKm", "el consumo por km"));
            if (id == null) {
                camionDAO.insertar(camion);
                mensajeFlash(req, "success", "Camión " + camion.getDominio() + " creado correctamente.");
            } else {
                camionDAO.actualizar(camion);
                mensajeFlash(req, "success", "Camión " + camion.getDominio() + " modificado correctamente.");
            }
            redirigir(req, resp, "/admin/camiones");
        } catch (ValidacionException e) {
            // Se vuelve al formulario conservando lo que el usuario habia escrito
            Map<String, String> valores = new HashMap<>();
            for (String campo : new String[] {"id", "marca", "modelo", "dominio", "toneladasMaximas",
                    "capacidadTanqueLitros", "consumoLitrosPorKm"}) {
                valores.put(campo, parametro(req, campo));
            }
            req.setAttribute("valores", valores);
            mensajeAhora(req, "warning", e.getMessage());
            vista(req, resp, "admin/camiones/form.jsp");
        }
    }

    private void eliminar(HttpServletRequest req, HttpServletResponse resp) throws DAOException, IOException {
        Long id = parametroLong(req, "id");
        try {
            if (id == null) {
                throw new ValidacionException("Falta indicar el camión a eliminar.");
            }
            camionDAO.eliminar(id);
            mensajeFlash(req, "success", "Camión eliminado correctamente.");
        } catch (ValidacionException e) {
            mensajeFlash(req, "warning", e.getMessage());
        }
        redirigir(req, resp, "/admin/camiones");
    }

    private Camion buscarCamion(HttpServletRequest req) throws DAOException {
        Long id = parametroLong(req, "id");
        return id == null ? null : camionDAO.buscarPorId(id);
    }

    private double numero(HttpServletRequest req, String campo, String descripcion) throws ValidacionException {
        try {
            // Se acepta la coma decimal, habitual en Argentina
            return Double.parseDouble(parametro(req, campo).replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new ValidacionException("Ingrese un número válido para " + descripcion + ".");
        }
    }

    private Map<String, String> aMapa(Camion camion) {
        Map<String, String> valores = new HashMap<>();
        valores.put("id", String.valueOf(camion.getId()));
        valores.put("marca", camion.getMarca());
        valores.put("modelo", camion.getModelo());
        valores.put("dominio", camion.getDominio());
        valores.put("toneladasMaximas", String.valueOf(camion.getToneladasMaximas()));
        valores.put("capacidadTanqueLitros", String.valueOf(camion.getCapacidadTanqueLitros()));
        valores.put("consumoLitrosPorKm", String.valueOf(camion.getConsumoLitrosPorKm()));
        return valores;
    }
}

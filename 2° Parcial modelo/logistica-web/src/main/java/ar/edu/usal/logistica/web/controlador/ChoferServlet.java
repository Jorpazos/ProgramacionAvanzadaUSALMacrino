package ar.edu.usal.logistica.web.controlador;

import ar.edu.usal.logistica.dao.CamionDAO;
import ar.edu.usal.logistica.dao.ChoferDAO;
import ar.edu.usal.logistica.dao.DAOFactory;
import ar.edu.usal.logistica.excepcion.DAOException;
import ar.edu.usal.logistica.excepcion.ValidacionException;
import ar.edu.usal.logistica.modelo.Camion;
import ar.edu.usal.logistica.modelo.Categoria;
import ar.edu.usal.logistica.modelo.Chofer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ABM de choferes (solo administrador), incluyendo los camiones que cada uno
 * esta autorizado a manejar. GET consulta; POST guarda o elimina.
 */
@WebServlet(name = "ChoferServlet", urlPatterns = "/admin/choferes")
public class ChoferServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;

    private final ChoferDAO choferDAO = DAOFactory.getInstance().getChoferDAO();
    private final CamionDAO camionDAO = DAOFactory.getInstance().getCamionDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = parametro(req, "accion");
        try {
            if ("nuevo".equals(accion)) {
                prepararFormulario(req, new HashMap<>(), new HashSet<>());
                vista(req, resp, "admin/choferes/form.jsp");
            } else if ("editar".equals(accion)) {
                Long id = parametroLong(req, "id");
                Chofer chofer = id == null ? null : choferDAO.buscarPorId(id);
                if (chofer == null) {
                    mensajeFlash(req, "error", "El chofer solicitado no existe.");
                    redirigir(req, resp, "/admin/choferes");
                    return;
                }
                Set<Long> seleccionados = new HashSet<>();
                chofer.getCamionesAutorizados().forEach(c -> seleccionados.add(c.getId()));
                prepararFormulario(req, aMapa(chofer), seleccionados);
                vista(req, resp, "admin/choferes/form.jsp");
            } else {
                req.setAttribute("choferes", choferDAO.listarTodos());
                vista(req, resp, "admin/choferes/lista.jsp");
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
            Chofer chofer = new Chofer(id, parametro(req, "nombre"), parametro(req, "apellido"),
                    parametro(req, "dni"), fecha(req), categoria(req), parametro(req, "telefono"));
            for (Camion camion : camionesSeleccionados(req)) {
                chofer.autorizar(camion);
            }
            if (id == null) {
                choferDAO.insertar(chofer, req.getParameter("password"));
                mensajeFlash(req, "success", "Chofer " + chofer.getNombreCompleto()
                        + " creado. Usuario de acceso: " + chofer.getDni() + ".");
            } else {
                choferDAO.actualizar(chofer);
                mensajeFlash(req, "success", "Chofer " + chofer.getNombreCompleto() + " modificado correctamente.");
            }
            redirigir(req, resp, "/admin/choferes");
        } catch (ValidacionException e) {
            Map<String, String> valores = new HashMap<>();
            for (String campo : new String[] {"id", "nombre", "apellido", "dni", "fechaNacimiento", "categoria",
                    "telefono"}) {
                valores.put(campo, parametro(req, campo));
            }
            Set<Long> seleccionados = new HashSet<>();
            String[] ids = req.getParameterValues("camionIds");
            if (ids != null) {
                for (String valor : ids) {
                    try {
                        seleccionados.add(Long.valueOf(valor));
                    } catch (NumberFormatException ignorado) {
                        // Un id mal formado simplemente no se marca
                    }
                }
            }
            prepararFormulario(req, valores, seleccionados);
            mensajeAhora(req, "warning", e.getMessage());
            vista(req, resp, "admin/choferes/form.jsp");
        }
    }

    private void eliminar(HttpServletRequest req, HttpServletResponse resp) throws DAOException, IOException {
        Long id = parametroLong(req, "id");
        try {
            if (id == null) {
                throw new ValidacionException("Falta indicar el chofer a eliminar.");
            }
            choferDAO.eliminar(id);
            mensajeFlash(req, "success", "Chofer eliminado correctamente.");
        } catch (ValidacionException e) {
            mensajeFlash(req, "warning", e.getMessage());
        }
        redirigir(req, resp, "/admin/choferes");
    }

    private void prepararFormulario(HttpServletRequest req, Map<String, String> valores, Set<Long> seleccionados)
            throws DAOException {
        req.setAttribute("valores", valores);
        req.setAttribute("camionesSeleccionados", seleccionados);
        req.setAttribute("camiones", camionDAO.listarTodos());
        req.setAttribute("categorias", Categoria.values());
    }

    private LocalDate fecha(HttpServletRequest req) throws ValidacionException {
        try {
            return LocalDate.parse(parametro(req, "fechaNacimiento"));
        } catch (DateTimeParseException e) {
            throw new ValidacionException("La fecha de nacimiento no es válida.");
        }
    }

    private Categoria categoria(HttpServletRequest req) throws ValidacionException {
        try {
            return Categoria.valueOf(parametro(req, "categoria"));
        } catch (IllegalArgumentException e) {
            throw new ValidacionException("Seleccione una categoría válida.");
        }
    }

    private List<Camion> camionesSeleccionados(HttpServletRequest req)
            throws DAOException, ValidacionException {
        List<Camion> camiones = new ArrayList<>();
        String[] ids = req.getParameterValues("camionIds");
        if (ids == null) {
            return camiones;
        }
        for (String valor : ids) {
            try {
                Camion camion = camionDAO.buscarPorId(Long.parseLong(valor));
                if (camion == null) {
                    throw new ValidacionException("Alguno de los camiones seleccionados ya no existe.");
                }
                camiones.add(camion);
            } catch (NumberFormatException e) {
                throw new ValidacionException("Camión seleccionado inválido.");
            }
        }
        return camiones;
    }

    private Map<String, String> aMapa(Chofer chofer) {
        Map<String, String> valores = new HashMap<>();
        valores.put("id", String.valueOf(chofer.getId()));
        valores.put("nombre", chofer.getNombre());
        valores.put("apellido", chofer.getApellido());
        valores.put("dni", chofer.getDni());
        valores.put("fechaNacimiento", chofer.getFechaNacimiento().toString());
        valores.put("categoria", chofer.getCategoria().name());
        valores.put("telefono", chofer.getTelefono());
        return valores;
    }
}

package edu.usal.controller;

import edu.usal.domain.Articulo;
import edu.usal.exception.NegocioException;
import edu.usal.service.ArticuloService;
import edu.usal.util.PropertiesUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/articulos")
public class ArticuloServlet extends HttpServlet {

    private static final String VISTA = "/WEB-INF/articulos/articulos.jsp";

    private ArticuloService service;

    @Override
    public void init() throws ServletException {
        this.service = new ArticuloService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Mensaje "flash" dejado por el POST antes del redirect
        HttpSession session = req.getSession();
        Object mensaje = session.getAttribute("mensaje");
        if (mensaje != null) {
            req.setAttribute("mensaje", mensaje);
            session.removeAttribute("mensaje");
        }
        mostrarListado(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        try {
            Articulo articulo = new Articulo();
            articulo.setCodigo(req.getParameter("codigo"));
            articulo.setNombre(req.getParameter("nombre"));
            articulo.setDescripcion(req.getParameter("descripcion"));
            articulo.setPrecio(parseDouble(req.getParameter("precio")));
            articulo.setStock(parseInt(req.getParameter("stock")));

            service.crear(articulo);

            // Post/Redirect/Get: evita que F5 reenvie el formulario
            req.getSession().setAttribute("mensaje",
                    "Artículo " + articulo.getCodigo() + " cargado correctamente.");
            resp.sendRedirect(req.getContextPath() + "/articulos");
            return;

        } catch (NumberFormatException e) {
            req.setAttribute("error", "El precio y el stock deben ser valores numéricos válidos.");
        } catch (NegocioException e) {
            req.setAttribute("error", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Error al guardar el artículo: " + e.getMessage());
        }

        // Si hubo error: se vuelve a mostrar la pagina (los campos se repueblan con ${param.xxx})
        mostrarListado(req, resp);
    }

    private void mostrarListado(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("appNombre", PropertiesUtil.get("app.nombre", "Artículos"));

        try {
            req.setAttribute("articulos", service.listar());          // Statement
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "No se pudo obtener el listado: " + e.getMessage());
        }

        try {
            req.setAttribute("resumen", service.obtenerResumen());    // CallableStatement
        } catch (Exception e) {
            e.printStackTrace();
            // No pisa un error previo; el listado se muestra igual
            if (req.getAttribute("error") == null) {
                req.setAttribute("error", "No se pudo obtener el resumen (¿creaste el SP?): " + e.getMessage());
            }
        }
        req.getRequestDispatcher(VISTA).forward(req, resp);
    }

    private double parseDouble(String valor) {
        if (valor == null) throw new NumberFormatException("vacío");
        return Double.parseDouble(valor.trim());
    }

    private int parseInt(String valor) {
        if (valor == null) throw new NumberFormatException("vacío");
        return Integer.parseInt(valor.trim());
    }
}

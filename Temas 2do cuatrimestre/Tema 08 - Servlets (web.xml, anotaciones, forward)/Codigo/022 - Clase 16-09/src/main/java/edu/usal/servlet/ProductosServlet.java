package edu.usal.servlet;

import edu.usal.model.Productos;
import edu.usal.model.ProductosDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/productos")
public class ProductosServlet extends HttpServlet {

    private ProductosDAO productosDAO;

    @Override
    public void init() throws ServletException {
        this.productosDAO = new ProductosDAO();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Productos> productos = this.productosDAO.getAll();

        req.setAttribute("listado", productos);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/productos/lista-productos.jsp");

        dispatcher.forward(req, resp);

        //resp.sendRedirect("lista-productos.jsp");
    }
}

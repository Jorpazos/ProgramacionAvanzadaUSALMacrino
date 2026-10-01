package edu.usal.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");

        String nombre = req.getParameter("nombre");
        String email = req.getParameter("email");

        PrintWriter out = resp.getWriter();

        if (nombre != null && !nombre.trim().isEmpty() && email != null && !email.trim().isEmpty()) {
            out.print("OK:El cliente " + nombre + " fue registrado con éxito.");
        } else {
            out.print("ERROR:Todos los campos son obligatorios.");
        }

        out.flush();
    }
}

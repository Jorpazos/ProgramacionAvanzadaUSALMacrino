package edu.usal.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.HttpJspPage;
import java.io.IOException;
import java.io.PrintWriter;

public class PrimerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");

        PrintWriter out = resp.getWriter();

        out.println("<html>");
            out.println("<head> <title>Servlet 16-09</title>");
            out.println("</head>");
                out.println("<body>");
                    out.println("<h1> Hola Mundo desde el Servlet! </h1>");
                out.println("</body>");
        out.println("</html>");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

}

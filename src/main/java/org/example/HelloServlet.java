package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;
    private EntityManagerFactory emf;

    public void init() {
        try {
            emf = Persistence.createEntityManagerFactory("myJPAUnit");
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            em.createNativeQuery("SELECT 1").getSingleResult();
            em.getTransaction().commit();
            em.close();
            message = "Connected to the database successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            message = "Error connecting to the database: " + e.getMessage();
        }
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    public void destroy() {
        if (emf != null) {
            emf.close();
        }
    }
}

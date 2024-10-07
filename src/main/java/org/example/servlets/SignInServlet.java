package org.example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/login")
public class SignInServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Here you would typically check the email and password against your database
        // For demonstration, let's assume a simple check
        if ("user@example.com".equals(email) && "password123".equals(password)) {
            response.sendRedirect("welcome.jsp"); // Redirect to a welcome page
        } else {
            // If login fails, you might want to show an error message
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body><h3>Invalid email or password. Please try again.</h3>");
            out.println("<a href='signin'>Back to Sign In</a></body></html>");
        }
    }
}

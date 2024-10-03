package org.example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.entities.User;
import org.example.services.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@WebServlet("/users")
public class UserServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> users = userService.findAll();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");

        String isManagerInput = request.getParameter("isManager");
        boolean isManager = Objects.equals(isManagerInput, "true");

        User user = new User(username, password, firstName, lastName, email, isManager);
        userService.create(user);
        response.sendRedirect("users");
    }
}

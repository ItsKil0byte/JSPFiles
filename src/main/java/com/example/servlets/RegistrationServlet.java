package com.example.servlets;

import com.example.database.UserService;
import com.example.models.UserProfile;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("uid") == null) {
            req.getRequestDispatcher("registration.jsp").forward(req, resp);
            return;
        }

        resp.sendRedirect("files");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        if (login.isEmpty() || password.isEmpty()) {
            req.setAttribute("error", "Login and password cannot be empty.");
        }

        if (req.getAttribute("error") != null) {
            req.getRequestDispatcher("registration.jsp").forward(req, resp);
            return;
        }

        UserService service = (UserService) getServletContext().getAttribute("service");
        UserProfile user = service.getUser(login);

        if (user != null) {
            req.setAttribute("error", "This login is already used.");
            req.getRequestDispatcher("registration.jsp").forward(req, resp);
            return;
        }

        Long uid = service.addNewUser(login, password);
        req.getSession().setAttribute("uid", uid);

        Files.createDirectories(Paths.get("D:/tmp/filemanager/" + login));

        resp.sendRedirect("login");
    }
}

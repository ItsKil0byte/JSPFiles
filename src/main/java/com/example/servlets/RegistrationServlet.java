package com.example.servlets;

import com.example.accounts.AccountService;
import com.example.accounts.UserProfile;
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
        if (req.getSession().getAttribute("user") == null) {
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

        AccountService accountService = (AccountService) getServletContext().getAttribute("accountService");
        UserProfile userProfile = accountService.getUserByLogin(login);

        if (userProfile != null) {
            req.setAttribute("error", "This login is already used.");
            req.getRequestDispatcher("registration.jsp").forward(req, resp);
            return;
        }

        UserProfile newUser = new UserProfile(login, password);
        req.getSession().setAttribute("user", newUser);
        accountService.addNewUser(newUser);

        Files.createDirectories(Paths.get("D:/tmp/filemanager/" + login));

        resp.sendRedirect("login");
    }
}

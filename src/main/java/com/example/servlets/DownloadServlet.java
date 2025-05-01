package com.example.servlets;

import com.example.database.UserService;
import com.example.models.UserProfile;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet("/download")
public class DownloadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long uid = (Long) req.getSession().getAttribute("uid");

        if (uid == null) {
            resp.sendRedirect("login");
            return;
        }

        UserService service = (UserService) getServletContext().getAttribute("service");
        UserProfile user = service.getUser(uid);

        Path path;
        String login = user.getLogin();
        String userPath = req.getParameter("path");

        if (userPath == null || !userPath.startsWith("D:/tmp/filemanager/" + login)) {
            resp.sendRedirect("files");
            return;
        } else {
            path = Paths.get(userPath);
        }

        String mimeType = Files.probeContentType(path);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        String name = path.getFileName().toString();
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8).replaceAll("\\+", " ");

        resp.setContentType(mimeType);
        resp.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedName);

        try (OutputStream outputStream = resp.getOutputStream()) {
            Files.copy(path, outputStream);
        }
    }
}

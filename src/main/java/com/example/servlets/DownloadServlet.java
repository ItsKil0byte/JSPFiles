package com.example.servlets;

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
        Path path = Paths.get(req.getParameter("path"));

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

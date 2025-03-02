package com.example.servlets;

import com.example.models.FileInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/files")
public class FilesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        req.setAttribute("time", currentTime);

        Path path = Paths.get(req.getParameter("path"));
        List<FileInfo> files = parseFiles(path);
        req.setAttribute("path", path);
        req.setAttribute("parent", path.getParent());
        req.setAttribute("files", files);

        req.getRequestDispatcher("files.jsp").forward(req, resp);
    }

    private List<FileInfo> parseFiles(Path path) throws IOException {
        List<FileInfo> filesInfo = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path _path : stream) {
                BasicFileAttributes attributes = Files.readAttributes(_path, BasicFileAttributes.class);

                filesInfo.add(new FileInfo(
                        _path.getFileName().toString(),
                        attributes.size(),
                        attributes.creationTime().toString(),
                        attributes.isDirectory()
                ));
            }
        }

        return filesInfo;
    }
}

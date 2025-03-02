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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/files")
public class FilesServlet extends HttpServlet {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String currentTime = LocalDateTime.now().format(formatter);
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

                if (Files.isHidden(_path)) continue;

                BasicFileAttributes attributes = Files.readAttributes(_path, BasicFileAttributes.class);

                LocalDateTime fileTime = LocalDateTime.ofInstant(
                        attributes.creationTime().toInstant(),
                        ZoneId.systemDefault()
                );

                filesInfo.add(new FileInfo(
                        _path.getFileName().toString(),
                        attributes.size(),
                        fileTime.format(formatter),
                        attributes.isDirectory()
                ));
            }
        }

        return filesInfo;
    }
}

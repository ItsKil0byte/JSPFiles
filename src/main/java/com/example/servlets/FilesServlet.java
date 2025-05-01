package com.example.servlets;

import com.example.database.UserService;
import com.example.models.UserProfile;
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
        Long uid = (Long) req.getSession().getAttribute("uid");

        if (uid == null) {
            resp.sendRedirect("login");
            return;
        }

        UserService service = (UserService) getServletContext().getAttribute("service");
        UserProfile user = service.getUser(uid);

        String login = user.getLogin();
        String userPath = req.getParameter("path");

        if (userPath == null || !userPath.startsWith("D:/tmp/filemanager/" + login) || userPath.contains("..")) {
            resp.sendRedirect("files?path=D:/tmp/filemanager/" + login);
            return;
        }

        Path path = Paths.get(userPath);
        String parent = path.getParent().toString().replace("\\", "/");

        if (parent.equals("D:/tmp/filemanager")) {
            parent = null;
        }

        String currentTime = LocalDateTime.now().format(formatter);
        req.setAttribute("time", currentTime);

        List<FileInfo> files = parseFiles(path);
        req.setAttribute("path", path.toString().replace("\\", "/"));
        req.setAttribute("parent", parent);
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

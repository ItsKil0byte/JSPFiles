<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>File list</title>
</head>
<body>
    ${time}
    <h1>${path}</h1>
    <hr>
    <a href="files?path=${parent}">⬅️ Back</a>
    <table>
        <thead>
            <tr>
                <th></th> <th>File</th> <th>Size</th> <th>Creation date</th>
            </th>
        </thead>
        <tbody>
            <c:forEach var="file" items="${files}">
                <tr>
                    <td>${file.directory ? "📁" : "📄"}</td>
                    <td>${file.name}</td>
                    <td>${file.directory ? "" : file.size}</td>
                    <td>${file.creationDate}</td>
                </tr>
            </c:forEach>
        <tbody>
    </table>
</body>
</html>
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
    <c:if test="${parent != null}">
        <a href="files?path=${parent}">⬅️ Back</a>
    </c:if>
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
                    <td>
                        <c:choose>
                            <c:when test="${file.directory && parent == null}">
                                <a href="files?path=${path}${file.name}">
                                    ${file.name}
                                </a>
                            </c:when>
                            <c:when test="${file.directory && parent != null}">
                                <a href="files?path=${path}/${file.name}">
                                    ${file.name}
                                </a>
                            </c:when>
                            <c:when test="${!file.directory && parent == null}">
                                <a href="download?path=${path}${file.name}">
                                    ${file.name}
                                </a>
                            </c:when>
                            <c:otherwise>
                                <a href="download?path=${path}/${file.name}">
                                    ${file.name}
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:if test="${!file.directory}">
                            ${file.size} B
                        </c:if>
                    </td>
                    <td>${file.creationDate}</td>
                </tr>
            </c:forEach>
        <tbody>
    </table>
</body>
</html>
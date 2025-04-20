<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
<h1>Registration</h1>
    <c:if test="${error != null}">
        <b>ERROR: ${error}</b>
    </c:if>
    <form action="registration" method="POST">
        <p><input type="text" name="login" placeholder="Enter login"/></p>
        <p><input type="password" name="password" placeholder="Enter password"/></p>
        <p>
            <button type="submit">Sign Up</button>
        </p>
    </form>
    <li><a href="login">Login</a></li>
</body>
</html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<title>News List</title>
<body>
<h1>News List</h1>
    <c:if test="${not empty news}">
        <c:forEach var="news" items="${news}">
            <p>${news.headline}</p>
            <p>${news.content}</p>
            <p>${news.date}</p>
        </c:forEach>
    </c:if>
</body>
</html>
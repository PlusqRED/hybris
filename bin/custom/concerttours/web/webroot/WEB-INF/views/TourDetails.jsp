<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!doctype html>
<html>
<title>Tour Details</title>
<body>
<h1>Tour Details</h1>
Tour Details for ${tour.tourName}
<p>${tour.description}</p>
<p>Schedule:</p>
<table>
    <tr>
        <th>Type</th>
        <th>Date</th>
        <th>Days Until</th>
        <th>Tickets</th>
    </tr>
    <c:forEach var="concert" items="${tour.concerts}">
        <tr>
            <td>${concert.type}</td>
            <td>${concert.date}</td>
            <td>${concert.countDown}</td>

            <c:if test="${not empty concert.tickets}">
                <td>
                    <table>
                        <tr>
                            <th>Price</th>
                            <th>Amount</th>
                        </tr>

                        <c:forEach var="ticket" items="${concert.tickets}">
                            <tr>
                                <td>${ticket.price}</td>
                                <td>${ticket.amount}</td>
                            </tr>
                        </c:forEach>
                    </table>
                </td>
            </c:if>
        </tr>
    </c:forEach>
</table>
<p>
    <a href="../bands">Back to Band List</a>
</p>
</body>
</html>
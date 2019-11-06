<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Summary Of Spam-o-Tron 3000&trade;</title>
        <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css"/>
    </head>
    <body>
        <h1>Alphabetical List of Suckers!</h1>
        <table class="w3-table-all w3-hoverable">
            <tr>
                <th>Email Address</th>
                <th>Date Added</th>
                <th>Subscribed</th>
            </tr>
            <c:forEach var="email" items="${emails}">
                <tr>
                    <td>${email.address}</td>
                    <td>${email.added}</td>
                    <td>
                    <c:choose>
                    <c:when test="${email.subscribed}">
                        <input type="checkbox" checked/>
                    </c:when>
                    <c:otherwise>
                        <input type="checkbox"/>
                    </c:otherwise>
                    </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>
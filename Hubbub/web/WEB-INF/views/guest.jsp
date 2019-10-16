<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Hubbub&trade; &raquo; Guest Timeline</title>
        <%@include file="/WEB-INF/jspf/w3csshead.jspf"%>
    </head>
    <body>
        <div id="navbar" class="w3-bar" style="background-color:#92C4BE">
            <a class="w3-bar-item w3-button" href="main?action=login">I'm a Bub</a>
            <a class="w3-bar-item w3-button" href="main?action=join">Sign me up!</a>
        </div>
        <div class="w3-container">
            <%@include file="/WEB-INF/jspf/masthead.jspf"%>
            <h1>Welcome to Hubbub&trade;</h1>
            <div class="w3-container w3-margin">
                <p>Here's our Bub&trade;s' Blurb&trade;s.</p>
                <c:forEach var="post" items="${posts}">
                    <%@include file="/WEB-INF/jspf/guestpost.jspf"%>
                </c:forEach>
            </div>
        </div>
    </body>
</html>

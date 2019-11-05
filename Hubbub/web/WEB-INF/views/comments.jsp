<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Hubbub&trade; &raquo; Blurb&trade; &raquo; Comment</title>
        <%@include file="/WEB-INF/jspf/w3csshead.jspf"%>
    </head>
    <body>
        <div class="w3-bar hubbubblue">
            <a class="w3-bar-item w3-button" href="main?action=timeline">Home</a>
            <a class="w3-bar-item w3-button" href="main?action=profile&for=${user}">My Deets&trade;</a>
            <a class="w3-bar-item w3-button" href="main?action=logout">Log Me Out</a>
        </div>
        <div class="w3-container w3-margin-top">
        <h1>The Blurb&trade; Sayeth:</h1>
        <%@include file="/WEB-INF/jspf/post.jspf"%>
        </div>
        <div class="w3-card-4">        
        <%@include file="/WEB-INF/jspf/commentbox.jspf"%>
        <c:forEach var="comment" items="${post.comments}">
        <%@include file="/WEB-INF/jspf/comment.jspf"%>
        </c:forEach>
        </div>
    </body>
</html>
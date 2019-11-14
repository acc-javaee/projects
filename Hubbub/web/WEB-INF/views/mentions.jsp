<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Hubbub&trade; &raquo; @Bub&trade; Mentions</title>
        <%@include file="/WEB-INF/jspf/w3csshead.jspf"%>
    </head>
    <body>
        <div id="navbar" class="w3-bar" style="background-color:#92C4BE">
            <a class="w3-bar-item w3-button" href="main?action=timeline">Home</a>
            <a class="w3-bar-item w3-button" href="main?action=post">Add a Blurb&trade;</a>
            <a class="w3-bar-item w3-button" href="main?action=profile&for=${param.subject}">
                ${param.subject}'s Deets&trade;
            </a>
            <a class="w3-bar-item w3-button" href="main?action=wall&for=${param.subject}">
                ${param.subject}'s Wall
            </a>
            <a class="w3-bar-item w3-button" href="main?action=logout">Log Me Out</a>
        </div>
        <div class="w3-container">
            <%@include file="/WEB-INF/jspf/masthead.jspf"%>
            <h1>
                @Mention Wall for
                <a href="main?action=profile&for=${param.subject}">${param.subject}</a>
            </h1>
            <div class="w3-container w3-margin">
                <%@include file="/WEB-INF/jspf/postbox.jspf"%>
                <h2>Here are the Blurb&trade;s Mentioning ${param.subject}</h2>
                <c:forEach var="post" items="${posts}">
                    <%@include file="/WEB-INF/jspf/post.jspf"%>
                </c:forEach>
            </div>
        </div>
    </body>
</html>

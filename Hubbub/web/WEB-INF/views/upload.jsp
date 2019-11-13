<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%-- @elvariable id="user" type="edu.acc.jee.hubbub.domain.User" --%>
<%-- @elvariable id="flash" type="java.lang.String" --%>
<!DOCTYPE html>
<html>
    <head>
        <title>Hubbub&trade; &raquo; Avatar Upload</title>
        <%@include file="/WEB-INF/jspf/w3csshead.jspf"%>
    </head>
    <body>
        <div class="w3-bar" style="background-color:#92C4BE">
            <a class="w3-bar-item w3-button" href="main?action=timeline">Home</a>
            <a class="w3-bar-item w3-button" href="main?action=profile&for=${user.username}">My Deetz&trade;</a>
            <a class="w3-bar-item w3-button" href="main?action=wall&for=${user.username}">My Wall</a>
            <a class="w3-bar-item w3-button" href="main?action=post">Post Something</a>
            <a class="w3-bar-item w3-button" href="main?action=logout">Log me out</a>            
        </div>
        <div class="w3-container">
            <%@include file="/WEB-INF/jspf/masthead.jspf"%>
            <h1>Avatar of <a href="main?action=wall&for=${user}"><span style="color:#8F3049">${user}<span></a>
            <c:if test="${not empty flash}">
            <span class="flash">${flash}</span>
            </c:if>
            </h1>
        </div>
        <div class="w3-container w3-half w3-margin-top">               
            <form class="w3-container w3-card-4" method="POST" action="main" enctype="multipart/form-data">
                <input type="hidden" name="action" value="avatar"/>
                <div class="w3-container w3-center">
                    <h3>Your Current Avatar</h3>
                    <img src="avatar?for=${user}" alt="Avatar" style="width:80%"/>
                    <div class="w3-container w3-center w3-margin-top">
                        <input type="file" name="avatar" id="avatar">
                    </div>
                    <p>
                        <button class="w3-button w3-section w3-ripple" style="background-color:#92C4BE">
                            Upload New Avatar
                        </button>
                    </p>
                </div>
            </form>
        </div>                   
    </body>
</html>
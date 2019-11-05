<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Hubbub&trade; &raquo; Deets&trade; &raquo; ${target.username}</title>
        <%@include file="/WEB-INF/jspf/w3csshead.jspf"%>
        <style type="text/css">.success {color:#92C4BE;}</style>
    </head>
    <body>
        <div class="w3-bar hubbubblue">
            <a class="w3-bar-item w3-button" href="main?action=timeline">Home</a>
            <a class="w3-bar-item w3-button" href="main?action=post">I'm Gonna Blurb&trade;!</a>
            <c:if test="${target ne user}">
                <a class="w3-bar-item w3-button" href="main?action=profile&for=${user}">My Deets&trade;</a>
            </c:if>
            <a class="w3-bar-item w3-button" href="main?action=logout">Log Out</a>
        </div>
        <div class="w3-container">
            <h1>
                Hubbub&trade; Deets&trade; for <span class="hubbubred">${target}</span>
                (Bub&trade; since <f:formatDate type="date" value="${target.joined}"/>)
            </h1>
        </div>
        <div class="w3-container"><!-- area for success/flash message -->
            <c:choose>
                <c:when test="${not empty flash}">
                    <h2 class="flash">${flash}</h2>
                </c:when>
                <c:when test="${not empty success}">
                    <h2 style="color:blue;">${success}</h2>
                </c:when>
            </c:choose>
        </div>
        <div class="w3-container w3-card-4 w3-margin"><!-- avatar -->
            <img src="avatar?for=${target}"/>
            <c:if test="${target eq user}">
                <button class="w3-button w3-margin-right hubbubblue">
                    <a href="main?action=avatar">Upload a new Avatar</a>
                </button>
                <c:if test="${user.profile.avatar ne null}">
                    <button class="w3-button w3-margin-left hubbublue">
                        <a href="main?action=revert">Revert to Default Avatar</a>
                    </button>
                </c:if>
            </c:if>
        </div>
            <form class="w3-container w3-card-4 w3-margin" method="POST" action="main">
                <input type="hidden" name="action" value="profile"/>
                <input type="hidden" name="for" value="${target}"/>
                <c:set var="disabled" value="${target eq user ? '' : 'disabled'}"/>
                <c:if test="${empty disabled}">
                    <p>The following fields are all optional and may be updated piecemeal.</p>
                </c:if>
                <p>
                    <input class="w3-input w3-light-gray" type="text" ${disabled}
                       name="firstName" value="${target.profile.firstName}"/>
                    <label>First Name</label>
                </p>
                <hr/>
                <p>
                    <input class="w3-input w3-light-gray" type="text" ${disabled}
                       name="lastName" value="${target.profile.lastName}"/>
                    <label>Last Name</label>
                </p>
                <hr/>
                <p>
                    <input class="w3-input w3-light-gray" type="text" ${disabled}
                       name="email" value="${target.profile.email}"/>
                    <label>Email</label>
                </p>
                <hr/>
                <p>
                    <select class="w3-select" ${disabled} name="timeZone">
                        <option value=""></option>
                        <c:forEach var="tz" items="${timeZones}">
                        <c:set var="selected" value="${tz eq target.profile.timeZone ? 'selected' : ''}"/>
                        <option value="${tz}" ${selected}>${tz}</option>
                        </c:forEach>
                    </select>                    
                    <label>Time Zone</label>
                </p>
                <hr/>
                <p>
                    <textarea rows="10" cols="50" name="biography" ${disabled} spellcheck="true"
                              class="w3-input w3-light-gray">${target.profile.biography}</textarea>
                    <label>Biography (512 characters max)</label>
                </p>
                <c:if test="${empty disabled}">
                    <hr/>
                    <p>
                        <button class="w3-button w3-section w3-ripple hubbubblue">
                            Save Deets&trade;
                        </button>
                    </p>
                </c:if>
            </form>
    </body>
</html>


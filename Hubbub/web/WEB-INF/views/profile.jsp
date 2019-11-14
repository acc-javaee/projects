<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Hubbub&trade; &raquo; Deets&trade; &raquo; ${target}</title>
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
            <c:choose>
                <c:when test="${user eq target}"></c:when>
                <c:when test="${followers.contains(user)}">
                    <a class="w3-bar-item w3-button" href="main?action=unfollow&target=${target}">
                        Unfollow ${target}
                    </a>
                </c:when>
                <c:otherwise>
                    <a class="w3-bar-item w3-button" href="main?action=follow&target=${target}">
                        Follow ${target}
                    </a>
                </c:otherwise>
            </c:choose>
            <a class="w3-bar-item w3-button" href="main?action=logout">Log Out</a>
        </div>
        <div class="w3-container">
            <h1>
                <c:set var="owner" value="${target eq user ? 'Me' : target}"/>
                Hubbub&trade; Deets&trade; for <a href="main?action=wall&for=${target}" class="hubbubred">${owner}</a>
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
                <c:if test="${profile.avatar ne null}">
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
                   name="firstName" value="${profile.firstName}"/>
                <label>First Name</label>
            </p>
            <hr/>
            <p>
                <input class="w3-input w3-light-gray" type="text" ${disabled}
                   name="lastName" value="${profile.lastName}"/>
                <label>Last Name</label>
            </p>
            <hr/>
            <p>
                <input class="w3-input w3-light-gray" type="text" ${disabled}
                   name="email" value="${profile.email}"/>
                <label>Email</label>
            </p>
            <hr/>
            <p>
                <select class="w3-select" ${disabled} name="timeZone">
                    <option value=""></option>
                    <c:forEach var="tz" items="${timeZones}">
                    <c:set var="selected" value="${tz eq profile.timeZone ? 'selected' : ''}"/>
                    <option value="${tz}" ${selected}>${tz}</option>
                    </c:forEach>
                </select>                    
                <label>Time Zone</label>
            </p>
            <hr/>
            <p>
                <textarea rows="10" cols="50" name="biography" ${disabled} spellcheck="true"
                    class="w3-input w3-light-gray"
                    onkeyup="charcountupdate(this.value)">${profile.biography}</textarea>
                <label>Biography (<span id="charcount"></span> left)</label>
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
        <div class="w3-container w3-card-4 w3-margin">
            <h3>Followees of ${target}:</h3>
            <ul class="w3-ul">
            <c:forEach var="followee" items="${followees}">
                <li class="w3-bar">
                    <img src="avatar?for=${followee}" class="w3-bar-item w3-circle" style="width:80px">
                    <div class="w3-bar-item">
                        <span class="w3-large"><a href="main?action=wall&for=${followee}">${followee}</a></span><br>
                    </div>
                </li>
            </c:forEach>
            </ul>
        </div>
        <div class="w3-container w3-card-4 w3-margin">     
            <h3>Followers of ${target}:</h3>
            <ul class="w3-ul">
            <c:forEach var="follower" items="${followers}">
                <li class="w3-bar">
                    <img src="avatar?for=${follower}" class="w3-bar-item w3-circle" style="width:80px">
                    <div class="w3-bar-item">
                        <span class="w3-large"><a href="main?action=wall&for=${follower}">${follower}</a></span><br>
                    </div>
                </li>
            </c:forEach>
            </ul>  
        </div>
        <div class="w3-container w3-card-4 w3-margin">
            <h3>Tags Created by ${target}</h3>
            <ul class="w3-ul">
            <c:forEach var="tag" items="${tags}">
                <li>
                    <img src="images/hashtag.png" width="40"/>
                    <a href="main?action=tags&tagName=${tag.tagName}">${tag.tagName}</a>
                </li>
            </c:forEach>
            </ul>
        </div>
    </body>
    <script type="text/javascript">
        function charcountupdate(str) {
            var lng = str.length;
            document.getElementById("charcount").innerHTML = 512 - lng;
        }
        charcountupdate("");
    </script>    
</html>


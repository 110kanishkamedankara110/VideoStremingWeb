<%
    HttpServletRequest req = request;
    HttpServletResponse resp = response;

    User u = (User) req.getSession().getAttribute("user");

    if (u == null) {
        resp.sendRedirect("Login.jsp");
    } else {
        pageContext.setAttribute("userId", u.getId());
        HttpSession ses = session;
        String userName = "";
        String content = "";
        if (u != null) {
            userName = u.getFirstName() + " " + u.getLastName();
            content = "<h3>" + userName + "</h3>\n" +
                    "    <button class=\"opt\" onclick=\"window.location='index.jsp' \" style=\"border: none;width: 100%;box-sizing: border-box;background-color: transparent\"><h5 style=\"text-align: center\">Home</h5></button>\n" +
                    "    <button class=\"opt\" onclick=\"window.location='product.jsp' \" style=\"border: none;width: 100%;box-sizing: border-box;background-color: transparent\"><h5 style=\"text-align: center\">Upload a Video</h5></button>\n" +
                    "    <button class=\"opt\" onclick=\"window.location='History.jsp' \" style=\"border: none;width: 100%;box-sizing: border-box;background-color: transparent\"><h5 style=\"text-align: center\">History</h5></button>\n" +
//                    "    <button class=\"opt\" style=\"border: none;width: 100%;box-sizing: border-box;background-color: transparent\"><h5 style=\"text-align: center\">My Account</h5></button>\n" +
                    "    <div style=\"width: 100%;display: flex;align-items: flex-end;justify-content: flex-end;box-sizing: border-box\">\n" +
                    "        <button class=\"bu\" onclick=\"window.location='Logout' \"   `>Log out</button>\n" +
                    "    </div>";
        } else {
            content = "<h3>Menu</h3>\n" +
                    "    <div style=\"width: 100%;display: flex;align-items: flex-end;justify-content: flex-end;box-sizing: border-box\">\n" +
                    "        <button class=\"bu\" onclick=\"window.location='Login.jsp' \">Log In</button>\n" +
                    "    </div>";
        }
        pageContext.setAttribute("content", content);
    }
%>

<%@ page import="com.phoenix.videoStream.entities.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="styleSheets/style.css"/>
    <title>History</title>
</head>
<body onload="loadHistory()">
<div style="visibility: hidden;position: absolute" class="fd" id="fakeDiv"></div>
<div>
    <h1 style="text-align: center">History</h1>
<div class="main-div" id="main-div">
</div>
</div>
<div class="moreBut" onclick="showMenu()">
    <b><p>More</p></b>
</div>
<div style="display: none" id="moreMenu" class="moreMenu">
    ${content}
</div>
<div class="player-div" id="player">

    <div class="player" id="pl">
        <div style="display: flex;width: 100%;justify-content: end;align-items: center;box-sizing: border-box;height: 5%">
            <button class="cb" id="closePlayer">
                X
            </button>
        </div>
        <div class="vid-mor">
            <div class="vp">
                <video id="play_vid" class="vidPlay"></video>
                <h3 style="padding: 10px;box-sizing: border-box" id="title1"></h3>
                <p style="box-sizing: border-box" id="user"></p>
                <div class="desc" id="description">

                </div>
            </div>
            <div class="more">
                <div id="cur" class="current">
                    <div class="img" id="vidimg" ></div>
                    <span  style="padding: 10px;box-sizing: border-box;width: 70%;overflow-y: auto;font-size: smaller;color: white;display:none"
                           id="title2"></span>
                </div>

                <div id="mv" class="dvdv" >

                </div>
            </div>
        </div>
    </div>
</div>

<script src="js/script.js">

</script>
<script>
    function showMenu() {
        let menu = document.getElementById('moreMenu');
        menu.style.display == 'none' ? menu.style.display = 'block' : menu.style.display = 'none';
    }
</script>
</body>
</html>


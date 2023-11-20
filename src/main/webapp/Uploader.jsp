<%@ page import="com.phoenix.videoStream.annotation.Auth" %>
<%@ page import="com.phoenix.videoStream.entities.User" %><%--
  Created by IntelliJ IDEA.
  User: Kanishka
  Date: 7/25/2023
  Time: 8:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%
    HttpServletRequest req=request;
    HttpServletResponse resp=response;

    User u = (User) req.getSession().getAttribute("user");
    if(u==null){
        resp.sendRedirect("Login.jsp");
    }
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>uploader</title>
</head>
<body id="bod">
<input type="file" id="video" accept="video/*"/>
<input type="file" id="image" accept="image/*"/>
<input type="text" id="title" placeholder="Title"/>

<textarea id="desc"></textarea>
<button onclick="upload()">Submit</button>
<button onclick="load()">check</button>

<script src="js/script.js">

</script>
</body>

</html>

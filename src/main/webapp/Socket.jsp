<%--
  Created by IntelliJ IDEA.
  User: BLACKBOX
  Date: 2/5/2024
  Time: 1:33 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <div style="width: 300px;
    height: 200px;
    background-color: black;
    background-position: center
;background-repeat: no-repeat;background-size: contain;transition: 2s"  id="te"></div>
    <script>
        let socket = new WebSocket("ws://localhost:8080/VideoStreamingWeb/vidStr");

        // Connection opened
        socket.addEventListener('open', (event) => {

        });

        // Connection closed
        socket.addEventListener('close', (event) => {
            // setTimeout(()=>{
            //     console.log("connecting............")
            //      socket = new WebSocket("ws://localhost:8080/VideoStreamingWeb/mes");
            // }, 1000);
        });

        // Error occurred
        socket.addEventListener('error', (event) => {
        });

        // Message received
        socket.addEventListener('message', (event) => {
            const url = URL.createObjectURL(event.data);
            console.log(url)
            document.getElementById("te").style.backgroundImage="url("+url+")";

        });

        // Example: Sending a binary message
        // const sendData = new Uint8Array([1, 2, 3, 4, 5]);
        // socket.send(sendData.buffer);
    </script>

</body>
</html>

<%@ page import="com.phoenix.videoStream.entities.User" %>

<%
    HttpServletRequest req=request;
    HttpServletResponse resp=response;

    User u = (User) req.getSession().getAttribute("user");
    if(u==null){
        resp.sendRedirect("Login.jsp");
    }else{
        pageContext.setAttribute("userId", u.getId());
    }
%>

<!DOCTYPE html>
<html>

<head>
    <title>NeW Teck | List Product</title>
    <link rel="stylesheet" href="styleSheets/style_Pages.css"/>
    <link rel="icon" href="images/logo.svg"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

</head>

<body onload="loadMyVideos(${userId})" id="bo" style="background-color:rgb(18, 18, 18);">



<div class="carosel" style="display:grid" id="car">

    <div id="imm">

        <div class="carosel" style="display:grid">
            <input onchange="loadVid()" style="visibility: hidden" type="file" id="video" accept="video/*"/>
            <input onchange="loadImg()" style="visibility: hidden" type="file" id="image" accept="image/*"/>
            <label for="video" class="iii"
                   style="background-size: cover;background-position: center;background-repeat:no-repeat ; border: solid rgb(106 117 117) 2px;box-sizing:border-box ;border-radius: 20px;background-image: url('images/plus gray.svg');cursor: pointer">
                <span style="color: rgba(209, 202, 202, 0.761);margin: 20px">Select a Video</span>
                <video  id="vid" class="iii"  controls muted
                       style="background-size: cover;background-position: center;background-repeat:no-repeat ; border: solid rgb(106 117 117) 2px;box-sizing:border-box ;border-radius: 20px;width: 400px;height: 200px;display:none"/>
            </label>


            <div class="car-div2">

                <label id="thu" for="image" class="new-itm iii"
                       style="background-size: cover; background-position: center;background-repeat:no-repeat ;border: solid rgb(106 117 117) 2px;box-sizing:border-box ;width: 50%;background-image: url('images/plus gray.svg');">
                    <span style="color: rgba(209, 202, 202, 0.761);margin: 20px">Select a Thumbnail</span>

                </label>





            </div>
        </div>
    </div>

    <div class="car-div2">
        <h1 style="color:white">Upload a Video</h1>
        <hr/>
        <p style="text-align:center;color:white;">Video</p>
        <hr/>
        <br/>
        <input id="title" type="text"
               style="padding:5px;border: none;border-radius: 5px;background-color:  rgb(48, 44, 44);width: 80%;margin-bottom: 10px;color:white"
               placeholder="Title"/> <span style="color:white"> : Title </span><br/>
        <hr/>
        <p style="text-align:center;color:white;">Description</p>
        <hr/>
        <br/>
        <div style="box-sizing: border-box ;border:solid 1px rgb(106 117 117);border-radius: 5px;background-color:  rgb(48, 44, 44);width:100%;height: 200px;">
            <textarea id="desc"
                    style="color:white;border: none;border-radius: 5px;width: 100%;height:100%;padding: 10px;background-color:  rgb(48, 44, 44);box-sizing: border-box;"></textarea>
        </div>
        <hr/>

        <button class="s" style="border-radius: 5px;" id="addimg" onclick="upload()">Upload</button>

    </div>


</div>
<div style="margin-top: 60px"></div>

<div class="main-div" id="main" style="margin-top: 20px;">
    <iframe style="width: 100%;height: 50vh"
            src="MyVideos.jsp"
            name="targetframe"

    >
    </iframe>



</div>


<script src="js/script2.js"></script>
<script src="js/script.js"></script>
<script>
    function loadImg() {
        let img = document.getElementById("image").files[0];
        let url = URL.createObjectURL(img);
        document.getElementById("thu").style.backgroundImage = "url(" + url + ")";
    }

    function loadVid() {


        var vplayer = document.getElementById('vid');
        var vfile = document.getElementById('video');
        vid.style.display="flex";
        var source = document.createElement('source')
        source.src = URL.createObjectURL(vfile.files[0]);
        vplayer.replaceChildren(source);


    }
</script>

</body>

</html>
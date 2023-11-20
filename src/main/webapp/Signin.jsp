<!DOCTYPE html>
<html>

<head>
    <title> | SignIn</title>
    <link rel="stylesheet" href="styleSheets/style_Pages.css"/>
    <link rel="icon" href="images/logo.svg"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>

<body>

<div class="bg-div" style="background-image: url('images/background.png');">

</div>
<div class="contain-div">
    <div class="img-div" style="background-image: url('images/background.png');">

    </div>
    <div class="m-div">
        <div class="des-div" style="display: flex;align-items:center;justify-content:center">
            <div style="text-align: center;width: 100%;">
                <h1>User SignIn</h1>
                <input style="width: 40%;border-radius: 10px;border:none;text-align: center;height: 30px;" type="text"
                       placeholder="First Name" id="firstName"/>
                <input style="width: 40%;border-radius: 10px;border:none;text-align: center;height: 30px;" type="text"
                       placeholder="Last Name" id="lastName"/><br/><br/>
                <input style="width: 80%;border-radius: 10px;border:none;text-align: center;height: 30px;" type="text"
                       placeholder="Email" id="email"/><br/><br/>
                <input style="width: 80%;border-radius: 10px;border:none;text-align: center;height: 30px;"
                       type="password" placeholder="Password" id="password1"/><br/><br/>

                <p id="err" style="background-color: tomato;color:white"></p>
                <button class="bu" onclick="signin()">SignIn</button>
                <button class="bu" onclick="window.location='Login.jsp'">LogIn</button>

                <br/>
            </div>


        </div>
    </div>
</div>


<div class="back" id="back" onclick="history.back()">

</div>

<script src="js/script2.js"></script>
<script src="js/script.js"></script>


</body>

</html>
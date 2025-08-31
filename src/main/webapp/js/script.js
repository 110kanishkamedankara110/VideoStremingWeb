var text;

function load() {

    var r = new XMLHttpRequest();
    r.onreadystatechange = function () {
        if (r.readyState == 4) {
            console.log(r.responseText);
            text = JSON.parse(r.responseText);
            loadComponents();

        }
    };
    r.open("get", "playlist", true);
    r.send();
}

function loadMyVideos(id) {

    var r = new XMLHttpRequest();
    r.onreadystatechange = function () {
        if (r.readyState == 4) {
            console.log(r.responseText);
            text = JSON.parse(r.responseText);
            loadComponents();
        }
    };
    r.open("get", "playlist?u=" + id, true);
    r.send();
}

function loadHistory(){
    var r = new XMLHttpRequest();
    r.onreadystatechange = function () {
        if (r.readyState == 4) {
            console.log(r.responseText);
            text = JSON.parse(r.responseText);
            loadComponents();
        }
    };
    r.open("get", "history", true);
    r.send();
}

function loadComponents() {
    var consize = document.getElementById("fakeDiv").offsetWidth;
    var main = document.getElementById("main-div");
    for (let obj of text) {
        var div1 = document.createElement("div");
        var div2 = document.createElement("div");
        var div3 = document.createElement("div");

        var width = obj.width;
        var whr = consize / width;

        var height = obj.height * whr;
        var end = height / 10;
        end = end.toString()
        end = end.split(".")[0];
        height = end * 10;

        div1.className = "card";
        div1.id = "vid" + obj.id;
        div1.style.height = height + "px";
        div1.style.gridRowEnd = "span " + end;
        div1.style.backgroundImage = "url('" + obj.thumbnail + "')";
        div1.style.backgroundColor = obj.color;
        div1.setAttribute("onmouseenter", "mouseOver('" + obj.id + "')");
        div1.setAttribute("onmouseleave", "mouseOut('" + obj.id + "')");
        div1.setAttribute('onClick', onclick = "playvid('" + obj.id + "')");


        div2.className = "card-ti";
        div2.id = "ti" + obj.id;


        div3.className = "tit";
        div3.innerHTML =obj.title.replaceAll('_'," ");
        div3.style.backgroundColor = "black";
        div3.style.color = "white";

        main.appendChild(div1);
        div1.appendChild(div2);
        div2.appendChild(div3);

    }

}

function load2(x) {


    var main = document.getElementById("mv");
    var coun = 0;
    var start = false;
    for (let obj of text) {
        if (coun >= 10) {
            break;
        }

        if (start) {


            var div1 = document.createElement("img");

            div1.className = "morevid";
            div1.src = obj.thumbnail;


            div1.setAttribute('onClick', onclick = "playvid('" + obj.id + "')");


            main.appendChild(div1);


            coun++;
        }
        if (obj.id == x) {
            start = true;
        }

    }

}


function upload() {

    let desc = document.getElementById("desc").value;
    let image = document.getElementById("image").files[0];
    let video = document.getElementById("video").files[0];
    let title = document.getElementById("title").value;

    let videodet = {};
    videodet.description = desc;
    videodet.title = title;
    let f = new FormData();
    f.append("details", JSON.stringify(videodet));
    f.append("video", video);
    f.append("image", image);

    var r = new XMLHttpRequest();
    r.onreadystatechange = function () {

        r.upload.addEventListener('progress', (e) => {
            console.clear();
            console.log("finished : " + e.loaded + " Of : " + e.total)
        });


        if (r.readyState == 4 && r.status == 200) {
            let text = r.responseText;
            alert(text);
            if (text == "Sucess") {
                window.location.reload();
            }

        }
    };
    r.open("POST", "videoPlay", true);
    r.send(f);

}

var stat = true;
var int;
var t = 0;
var vp;
var playtimes = {};
var play = false;
var vol = 1;
var intv;
var click = false
var prev = "";


function loadFromLocalStorage() {
    const data = localStorage.getItem("appState");
    if (data) {
        const savedState = JSON.parse(data);
        stat = savedState.stat;
        int = savedState.int;
        t = savedState.t;
        vp = savedState.vp;
        playtimes = savedState.playtimes;
        play = savedState.play;
        vol = savedState.vol;
        intv = savedState.intv;
        click = savedState.click;
        prev = savedState.prev;
    }
}

window.addEventListener("load", loadFromLocalStorage);

function saveToLocalStorage() {
    const data = {
        stat,
        int,
        t,
        vp,
        playtimes,
        play,
        vol,
        intv,
        click,
        prev
    };

    localStorage.setItem("appState", JSON.stringify(data));
    console.log("State saved to localStorage.");
}

window.addEventListener("beforeunload", saveToLocalStorage);
window.addEventListener('', saveToLocalStorage);



function mouseOver(id) {
    var vid = searchObject(id).video;
    var ele = document.getElementById("vid" + id);
    ele.style.transform = "scale(1.2)";
    document.getElementById("ti" + id).style = "display:flex";
    if (t == 0) {
        int = setInterval(() => {
            if (!click) {
                document.getElementById("ti" + id).style = "display:none";
                ele.style.transform = "scale(1.5)";
                intv = setInterval(() => {
                    if (!vp.muted) {
                        ele.style.transform = "scale(3)";
                        clearInterval(intv);
                    }
                }, 15000);
                vp = document.createElement("video");
                vp.addEventListener('volumechange', (e) => {
                    stat = vp.muted;
                });
                play = true;
                if (id in playtimes) {
                    vp.currentTime = playtimes[id];
                }
                vp.volume = vol;
                vp.className = "vid";
                vp.id = "vp" + id;
                vp.controls = true;
                vp.autoplay = true;
                vp.disablePictureInPicture = "false";
                vp.muted = stat;
                vp.src = "videoPlay?video=" + vid;
                ele.appendChild(vp);
                t++;
                clearInterval(int);
            }
        }, 3000);


    }

}

function mouseOut(id) {
    let vid = searchObject(id).video;
    let ele = document.getElementById("vid" + id)
    ele.style.transform = "none";
    document.getElementById("ti" + id).style = "display:none";
    if (play) {
        clearInterval(intv);
        playtimes[id] = vp.currentTime;
        vol = vp.volume;
        ele.removeChild(vp);
        vp.src = "";
    }
    play = false;
    clearInterval(int);
    t = 0;
    saveToLocalStorage();
}

function setHistory(x) {

            fetch("history", {
                method: "POST",
                headers: {
                    'content-Type': 'application/x-www-form-urlencoded',
                },
                body: "video=" + x
            }).then(
                (resp) => {
                    return resp.json();
                }
            ).then(
                (json) => {
                    // (json.title == "Sucess") ? window.location = "MyVideos.jsp" : alert(json.title);
                }
            )



}

function playvid(x) {
    saveToLocalStorage();
    if (prev != "") {
        playtimes[prev] = vp.currentTime;
    }
    click = true;
    var object = searchObject(x);
    setHistory(x);
    prev = object.id;

    document.getElementById("mv").innerHTML = "";
    load2(object.id);
    if (play) {
        playtimes[object.id] = vp.currentTime;
        vp.src = "";
        var ele = document.getElementById("vid" + object.id)
        ele.style.transform = "none";
        document.getElementById("ti" + object.id).style = "display:none";
        clearInterval(intv);
        vol = vp.volume;
        ele.removeChild(vp);
        play = false;
        clearInterval(int);
        t = 0;
    }


    var img = new Image();
    img.src = object.thumbnail;
    var imgheight;
    img.onload = function () {
        var width = this.width;
        var whr = 60 / width;
        imgheight = this.height * whr;

    };


    document.getElementById("vidimg").style.height = imgheight + "%";
    document.getElementById("cur").style.height = imgheight + "%";

    if (document.getElementById("del") != null) {
        document.getElementById("del").setAttribute("onClick", "delet('" + object.id + "')");
    }
    if (document.getElementById("upd") != null) {
        document.getElementById("upd").setAttribute("onClick", "update('" + object.id + "')");
    }
    if (document.getElementById("updIcon") != null) {
        document.getElementById("updIcon").onchange = () => {
            let img = document.getElementById("updIcon").files[0];
            let url = URL.createObjectURL(img);
            document.getElementById("vidimg").src = url;
        };
    }

    document.getElementById("vidimg").src = object.thumbnail;
    document.getElementById("title1").innerHTML = object.title.replaceAll('_',' ');
    document.getElementById("user").innerHTML = object.userEmail;
    document.getElementById("title2").innerHTML = "<b>Playing</b>";
    document.getElementById("description").innerHTML = object.description;

    document.getElementById("player").style.display = "flex";

    document.getElementById("pl").style.backgroundColor = object.color;
    document.getElementById("pl").style.color = object.minColor;

    document.getElementById("closePlayer").style.color = object.minColor;
    document.getElementById("title2").style.color = object.minColor;


    vp = document.getElementById("play_vid");
    vp.addEventListener('volumechange', (e) => {
        stat = vp.muted;
    });
    document.getElementById("closePlayer").setAttribute("onClick", "closePlayer('" + object.id + "')");

    vp.volume = vol;
    vp.controls = true;
    vp.autoplay = true;
    vp.disablePictureInPicture = "false";
    vp.muted = false;
    vp.src = "videoPlay?video=" + object.video;
    if (object.id in playtimes) {
        vp.currentTime = playtimes[object.id];
    }
}


function delet(id) {
    window.location = "Delete?v=" + id;
}

function update(id) {
    let title = document.getElementById("title1").value;
    let description = document.getElementById("description").value;
    let image = document.getElementById("updIcon").files[0];

    if(!title) { alert("Title Required"); return; }
    if(!description) { alert("Description Required"); return; }

    let formData = new FormData();
    formData.append("data", JSON.stringify({ title, description, id }));

    if(image) {
        formData.append("image", image);
    }

    fetch("Update", {
        method: "POST",
        body: formData // ✅ do not set Content-Type manually
    })
        .then(resp => resp.json())
        .then(json => {
            if(json.status === "success") {
                window.location = "MyVideos.jsp";
            } else {
                alert(json.message);
            }
        })
        .catch(err => {
            console.error(err);
            alert("Something went wrong.");
        });
}


function closePlayer(x) {
    saveToLocalStorage();
    prev = "";
    var object = searchObject(x);

    playtimes[object.id] = vp.currentTime;
    vp.src = "";
    vol = vp.volume;
    document.getElementById("vidimg").src = "";
    document.getElementById("title1").innerHTML = "";
    document.getElementById("title2").innerHTML = "";
    document.getElementById("description").innerHTML = "";
    document.getElementById("mv").innerHTML = "";
    click = false;
    play = false;
    t = 0;
    document.getElementById("player").style.display = "none";
}

function searchObject(id) {
    var object;

    if (text != null) {

        for (let obj of text) {
            if (obj.id == id) {
                object = obj;

                break;
            }
        }

    }
    return object
}

function login() {
    let email = document.getElementById("email");
    let password = document.getElementById("password1");

    fetch("Login",
        {
            method: 'POST',
            headers: {
                'content-Type': 'application/x-www-form-urlencoded',
            },
            body: "data=" + JSON.stringify({
                email: email.value,
                password: password.value,
            }),
        }
    )
        .then(
            (resp) => {
                return resp.json();
            }
        ).then(
        (json) => {
            (json.title == "Sucess") ? window.location = "index.jsp" : alert(json.title);
        }
    )

        .catch(

        )
}

function signin() {
    let firstName = document.getElementById("firstName");
    let lastName = document.getElementById("lastName");
    let email = document.getElementById("email");
    let password = document.getElementById("password1");
    fetch(
        "Signin",
        {
            method: 'POST',
            headers: {
                'content-Type': 'application/x-www-form-urlencoded',
            },
            body: "data=" + JSON.stringify({
                email: email.value,
                password: password.value,
                firstName: firstName.value,
                lastName: lastName.value,
            }),
        }
    )
        .then(
            (resp) => {
                return resp.json();
            }
        ).then(
        (json) => {
            (json.title == "Sucess") ? window.location = "Login.jsp" : alert(json.title);
        }
    )

        .catch(

        )
}
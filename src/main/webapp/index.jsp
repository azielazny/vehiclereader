<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalDateTime" %>
<!DOCTYPE html>
<html>
<head>
    <title>Rejestracja pojazdu</title>
    <style>
        #canvasID {
            border: 1px solid black;
            width: 500px;
            height: 500px
        }

        #regbase tr td {
            border: 1px solid #333;
            padding: 5px;
            background: #fefefe;
        }

        body {
            margin: 0px;
            overflow: hidden;
        }

        .column {
            overflow: auto;
        }

        #fromFile, #base64, #camera {
            border: 1px solid #ddd;
            padding: 5px;
        }

        textarea {
            width: 80%;
            margin: 0 auto;
        }

    </style>
</head>
<body>

<div class="column" style="width: 50%; float:left; display: inline-block">
    <h2>Aktualny czas to <%= LocalDateTime.now() %>
    </h2>
    <div id="fromFile">
        <p>Jeśli masz zdjęcie to dodaj je poniżej</p>
        <form enctype="multipart/form-data" method="post" action="/imagesender" name="fileinfo">
            <input type="file" name="fileFromDisc" value=""/>
            <input type="submit" value="załaduj" name="fromFile" style="width: 200px; height: 30px;"/>
        </form>
        Wybrałeś plik: ${uploadedFile}
    </div>
    <div id="base64">
        <p>Jeśli masz zeskanowany kod to wklej go poniżej</p>
        <form method="post" action="/textsender" name="textinfo">
            <textarea cols="40" rows="10" name="base64ToDecode" id="uploadedText">${uploadedText}</textarea>
            <input type="button" value="dekoduj" name="fromTextarea" style="width: 200px; height: 30px;"
                   onclick="textSender()"/>
        </form>
    </div>
    <div id="camera">
        <p>Poniżej możesz zeskanować kod z dowodu:</p>
        <div width="333px" height="333px">
            <video id="videoID" autoplay style="border: 1px solid black;"></video>
        </div>
        <div>
            <canvas id="canvasID" style="display: none"></canvas>
        </div>
        <div>
            <input type="button" value="Take photo" onclick="captureTimer()"
                   style="width: 200px; height: 30px;"/>
            <input type="button" value="Send" onclick="stopCaptureTimer()" style="width: 200px; height: 30px;"/>
        </div>
    </div>

</div>
<div class="column" style="width: 50%; float:right; height:100vh; background: #ddd;display: inline-block">
    WYNIK:
    <div id="wynik">
        <table id="regbase" style="margin: 0 auto">
        </table>
    </div>
</div>


<script type="text/javascript">
    var registrationInfo = ${(empty result)? '{}' : result}, key, base;

    var video = document.getElementById('videoID');
    var canvas = document.getElementById('canvasID');
    var context = canvas.getContext('2d');
    canvas.width = 640;
    canvas.height = 480;
    window.URL = window.URL || window.webkitURL;
    navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia ||
        navigator.mozGetUserMedia || navigator.msGetUserMedia;
    navigator.getUserMedia({
        video: true
    }, function (stream) {
        video.src = window.URL.createObjectURL(stream);
    }, function (e) {
        console.log('An error happened:', e);
    });
    var t;

    function captureTimer() {
        context.drawImage(video, 0, 0, canvas.width, canvas.height);
        var imageData = canvas.toDataURL();
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.open("POST", "/webcamera", true);
        xmlhttp.send(imageData);
        t = setTimeout("captureTimer()", 2000);
    }

    function stopCaptureTimer() {
        clearTimeout(t);
    }

    //upload text
    function textSender() {
        var oTextarea = document.getElementById("uploadedText").value;
        var oReq = new XMLHttpRequest();
        oReq.open("POST", "/textsender", true);
        oReq.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        oReq.send("base64ToDecode=" + encodeURIComponent(oTextarea));
        oReq.addEventListener('load', function () {
            if (this.status === 200) {
                console.log(this.responseText);
                registrationInfo = JSON.parse(oReq.responseText);
                updateResult();
            }
        });
    }

    //upload image
    var fileform = document.forms.namedItem("fileinfo");
    fileform.addEventListener('submit', function (ev) {
        var oData = new FormData(fileform);
        var oReq = new XMLHttpRequest();
        oReq.open("POST", "/imagesender", true);
        oReq.onload = function (oEvent) {
            if (oReq.status == 200) {
                console.log(oReq.responseText);
                registrationInfo = JSON.parse(oReq.responseText);
                updateResult();
            }
        };
        oReq.send(oData);
        ev.preventDefault();
    }, false);

    function updateResult() {
        for (key in registrationInfo) {
            if (registrationInfo.hasOwnProperty(key)) {
                base += "<tr><td>" + key + "</td><td>" + registrationInfo[key] + "</td></tr>";
            }
        }
        document.getElementById("regbase").innerHTML = base;
        base = "";
    }


</script>
</body>
</html>

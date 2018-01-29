<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalDateTime" %>
<!DOCTYPE html>
<html>
<head>
    <title>Rejestracja pojazdu</title>
<style>
    #canvasID {
        border: 1px solid black;width:500px; height: 500px
    }
</style>
</head>
<body >

<h2>Aktualny czas to <%= LocalDateTime.now() %>
</h2>
<div style="width: 50%; float:left; display: inline-block">
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

    <p>Jeśli masz zdjęcie to dodaj je poniżej</p>
    <form action="/webinterface" method="post" enctype="multipart/form-data" mediaType="multipart/form-data">
        <input type="file" name="fileFromDisc" value=""/>
        <input type="submit" value="załaduj" name="fromFile" style="width: 200px; height: 30px;"/>
    </form>
    ${zaladowanyplik2}

    <p>Jeśli masz zeskanowany kod to wklej go poniżej</p>
    <textarea cols="40" rows="10"></textarea>
    <button type="button" title="dekoduj" style="width: 200px; height: 30px;"></button>
</div>
<div style="width: 50%; float:right; height:100vh; background: #ddd;display: inline-block">
    WYNIK:
    <div id="wynik">${wynik}</div>
</div>


<script type="text/javascript">

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
        t=setTimeout("captureTimer()",2000);
    }

    function capture() {
        context.drawImage(video, 0, 0, canvas.width, canvas.height);
    };

    function send() {
        var imageData = canvas.toDataURL();
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.open("POST", "/webcamera", true);
        xmlhttp.send(imageData);
    };


    function stopCaptureTimer() {
        clearTimeout(t);
    }
</script>
</body>
</html>

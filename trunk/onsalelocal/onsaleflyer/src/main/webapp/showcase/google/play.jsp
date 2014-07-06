<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o"%>

 <script type="text/javascript">
     window.requestAnimFrame = (function(callback){
         return window.requestAnimationFrame ||
         window.webkitRequestAnimationFrame ||
         window.mozRequestAnimationFrame ||
         window.oRequestAnimationFrame ||
         window.msRequestAnimationFrame ||
         function(callback){
             window.setTimeout(callback, 1000 / 60);
         };
     })();
     
     function initBalls(){
         balls = [];
         
         var blue = "#3A5BCD";
         var red = "#EF2B36";
         var yellow = "#FFC636";
         var green = "#02A817";
         
         // G
         balls.push(new Ball(173, 63, 0, 0, blue));
         balls.push(new Ball(158, 53, 0, 0, blue));
         balls.push(new Ball(143, 52, 0, 0, blue));
         balls.push(new Ball(130, 53, 0, 0, blue));
         balls.push(new Ball(117, 58, 0, 0, blue));
         balls.push(new Ball(110, 70, 0, 0, blue));
         balls.push(new Ball(102, 82, 0, 0, blue));
         balls.push(new Ball(104, 96, 0, 0, blue));
         balls.push(new Ball(105, 107, 0, 0, blue));
         balls.push(new Ball(110, 120, 0, 0, blue));
         balls.push(new Ball(124, 130, 0, 0, blue));
         balls.push(new Ball(139, 136, 0, 0, blue));
         balls.push(new Ball(152, 136, 0, 0, blue));
         balls.push(new Ball(166, 136, 0, 0, blue));
         balls.push(new Ball(174, 127, 0, 0, blue));
         balls.push(new Ball(179, 110, 0, 0, blue));
         balls.push(new Ball(166, 109, 0, 0, blue));
         balls.push(new Ball(156, 110, 0, 0, blue));
         
         // O
         balls.push(new Ball(210, 81, 0, 0, red));
         balls.push(new Ball(197, 91, 0, 0, red));
         balls.push(new Ball(196, 103, 0, 0, red));
         balls.push(new Ball(200, 116, 0, 0, red));
         balls.push(new Ball(209, 127, 0, 0, red));
         balls.push(new Ball(223, 130, 0, 0, red));
         balls.push(new Ball(237, 127, 0, 0, red));
         balls.push(new Ball(244, 114, 0, 0, red));
         balls.push(new Ball(242, 98, 0, 0, red));
         balls.push(new Ball(237, 86, 0, 0, red));
         balls.push(new Ball(225, 81, 0, 0, red));
         
         // O
         var oOffset = 67;
         balls.push(new Ball(oOffset + 210, 81, 0, 0, yellow));
         balls.push(new Ball(oOffset + 197, 91, 0, 0, yellow));
         balls.push(new Ball(oOffset + 196, 103, 0, 0, yellow));
         balls.push(new Ball(oOffset + 200, 116, 0, 0, yellow));
         balls.push(new Ball(oOffset + 209, 127, 0, 0, yellow));
         balls.push(new Ball(oOffset + 223, 130, 0, 0, yellow));
         balls.push(new Ball(oOffset + 237, 127, 0, 0, yellow));
         balls.push(new Ball(oOffset + 244, 114, 0, 0, yellow));
         balls.push(new Ball(oOffset + 242, 98, 0, 0, yellow));
         balls.push(new Ball(oOffset + 237, 86, 0, 0, yellow));
         balls.push(new Ball(oOffset + 225, 81, 0, 0, yellow));
         
         // G
         balls.push(new Ball(370, 80, 0, 0, blue));
         balls.push(new Ball(358, 79, 0, 0, blue));
         balls.push(new Ball(346, 79, 0, 0, blue));
         balls.push(new Ball(335, 84, 0, 0, blue));
         balls.push(new Ball(330, 98, 0, 0, blue));
         balls.push(new Ball(334, 111, 0, 0, blue));
         balls.push(new Ball(348, 116, 0, 0, blue));
         balls.push(new Ball(362, 109, 0, 0, blue));
         balls.push(new Ball(362, 94, 0, 0, blue));
         balls.push(new Ball(355, 128, 0, 0, blue));
         balls.push(new Ball(340, 135, 0, 0, blue));
         balls.push(new Ball(327, 142, 0, 0, blue));
         balls.push(new Ball(325, 155, 0, 0, blue));
         balls.push(new Ball(339, 165, 0, 0, blue));
         balls.push(new Ball(352, 166, 0, 0, blue));
         balls.push(new Ball(367, 161, 0, 0, blue));
         balls.push(new Ball(371, 149, 0, 0, blue));
         balls.push(new Ball(366, 137, 0, 0, blue));
         
         // L
         balls.push(new Ball(394, 49, 0, 0, green));
         balls.push(new Ball(381, 50, 0, 0, green));
         balls.push(new Ball(391, 61, 0, 0, green));
         balls.push(new Ball(390, 73, 0, 0, green));
         balls.push(new Ball(392, 89, 0, 0, green));
         balls.push(new Ball(390, 105, 0, 0, green));
         balls.push(new Ball(390, 118, 0, 0, green));
         balls.push(new Ball(388, 128, 0, 0, green));
         balls.push(new Ball(400, 128, 0, 0, green));
         
         // E
         balls.push(new Ball(426, 101, 0, 0, red));
         balls.push(new Ball(436, 98, 0, 0, red));
         balls.push(new Ball(451, 95, 0, 0, red));
         balls.push(new Ball(449, 83, 0, 0, red));
         balls.push(new Ball(443, 78, 0, 0, red));
         balls.push(new Ball(430, 77, 0, 0, red));
         balls.push(new Ball(418, 82, 0, 0, red));
         balls.push(new Ball(414, 93, 0, 0, red));
         balls.push(new Ball(412, 108, 0, 0, red));
         balls.push(new Ball(420, 120, 0, 0, red));
         balls.push(new Ball(430, 127, 0, 0, red));
         balls.push(new Ball(442, 130, 0, 0, red));
         balls.push(new Ball(450, 125, 0, 0, red));
         
         return balls;
     }
     
     function getMousePos(canvas, evt){
         // get canvas position
         var obj = canvas;
         var top = 0;
         var left = 0;
         while (obj.tagName != 'BODY') {
             top += obj.offsetTop;
             left += obj.offsetLeft;
             obj = obj.offsetParent;
         }
         
         // return relative mouse position
         var mouseX = evt.clientX - left + window.pageXOffset;
         var mouseY = evt.clientY - top + window.pageYOffset;
         return {
             x: mouseX,
             y: mouseY
         };
     }
     
     function updateBalls(canvas, balls, timeDiff, mousePos){
         var context = canvas.getContext("2d");
         var collisionDamper = 0.3;
         var floorFriction = 0.0005 * timeDiff;
         var mouseForceMultiplier = 1 * timeDiff;
         var restoreForce = 0.002 * timeDiff;
         
         for (var n = 0; n < balls.length; n++) {
             var ball = balls[n];
             // set ball position based on velocity
             ball.y += ball.vy;
             ball.x += ball.vx;
             
             // restore forces
             if (ball.x > ball.origX) {
                 ball.vx -= restoreForce;
             }
             else {
                 ball.vx += restoreForce;
             }
             if (ball.y > ball.origY) {
                 ball.vy -= restoreForce;
             }
             else {
                 ball.vy += restoreForce;
             }
             
             // mouse forces
             var mouseX = mousePos.x;
             var mouseY = mousePos.y;
             
             var distX = ball.x - mouseX;
             var distY = ball.y - mouseY;
             
             var radius = Math.sqrt(Math.pow(distX, 2) +
             Math.pow(distY, 2));
             
             var totalDist = Math.abs(distX) + Math.abs(distY);
             
             var forceX = (Math.abs(distX) / totalDist) *
             (1 / radius) *
             mouseForceMultiplier;
             var forceY = (Math.abs(distY) / totalDist) *
             (1 / radius) *
             mouseForceMultiplier;
             
             if (distX > 0) { // mouse is left of ball
                 ball.vx += forceX;
             }
             else {
                 ball.vx -= forceX;
             }
             if (distY > 0) { // mouse is on top of ball
                 ball.vy += forceY;
             }
             else {
                 ball.vy -= forceY;
             }
             
             // floor friction
             if (ball.vx > 0) {
                 ball.vx -= floorFriction;
             }
             else if (ball.vx < 0) {
                 ball.vx += floorFriction;
             }
             if (ball.vy > 0) {
                 ball.vy -= floorFriction;
             }
             else if (ball.vy < 0) {
                 ball.vy += floorFriction;
             }
             
             // floor condition
             if (ball.y > (canvas.height - ball.radius)) {
                 ball.y = canvas.height - ball.radius - 2;
                 ball.vy *= -1;
                 ball.vy *= (1 - collisionDamper);
             }
             
             // ceiling condition
             if (ball.y < (ball.radius)) {
                 ball.y = ball.radius + 2;
                 ball.vy *= -1;
                 ball.vy *= (1 - collisionDamper);
             }
             
             // right wall condition
             if (ball.x > (canvas.width - ball.radius)) {
                 ball.x = canvas.width - ball.radius - 2;
                 ball.vx *= -1;
                 ball.vx *= (1 - collisionDamper);
             }
             
             // left wall condition
             if (ball.x < (ball.radius)) {
                 ball.x = ball.radius + 2;
                 ball.vx *= -1;
                 ball.vx *= (1 - collisionDamper);
             }
         }
     }
     
     function Ball(x, y, vx, vy, color){
         this.x = x;
         this.y = y;
         this.vx = vx;
         this.vy = vy;
         this.color = color;
         this.origX = x;
         this.origY = y;
         this.radius = 10;
     }
     
     function animate(canvas, balls, lastTime, mousePos){
         var context = canvas.getContext("2d");
         
         // update
         var date = new Date();
         var time = date.getTime();
         var timeDiff = time - lastTime;
         updateBalls(canvas, balls, timeDiff, mousePos);
         lastTime = time;
         
         // clear
         context.clearRect(0, 0, canvas.width, canvas.height);
         
         // render
         for (var n = 0; n < balls.length; n++) {
             var ball = balls[n];
             context.beginPath();
             context.arc(ball.x, ball.y, ball.radius, 0, 2 * Math.PI, false);
             context.fillStyle = ball.color;
             context.fill();
         }
         
         // request new frame
         requestAnimFrame(function(){
             animate(canvas, balls, lastTime, mousePos);
         });
     }
     
     window.onload = function(){
         var canvas = document.getElementById("myCanvas");
         var balls = initBalls();
         var date = new Date();
         var time = date.getTime();
         /*
          * set mouse position really far away
          * so the mouse forces are nearly obsolete
          */
         var mousePos = {
             x: 9999,
             y: 9999
         };
         
         canvas.addEventListener("mousemove", function(evt){
             var pos = getMousePos(canvas, evt);
             mousePos.x = pos.x;
             mousePos.y = pos.y;
         });
         
         canvas.addEventListener("mouseout", function(evt){
             mousePos.x = 9999;
             mousePos.y = 9999;
         });
         
         animate(canvas, balls, time, mousePos);
     };

     $(document).ready(function(){
         $('#myCanvas').center();
     });
    
 </script>
        
<canvas id="myCanvas" width="578" height="200">
</canvas>

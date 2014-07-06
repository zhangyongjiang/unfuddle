<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o"%>

    <style>
      canvas {
        border: 1px solid #9C9898;
      }
    </style>
    <script>
      function loadImages(sources, callback) {
        var assetDir = "http://www.html5canvastutorials.com/demos/assets/";
        var images = {};
        var loadedImages = 0;
        var numImages = 0;
        for(var src in sources) {
          numImages++;
        }
        for(var src in sources) {
          images[src] = new Image();
          images[src].onload = function() {
            if(++loadedImages >= numImages) {
              callback(images);
            }
          };
          images[src].src = assetDir + sources[src];
        }
      }
      function isNearOutline(animal, outline) {
        var a = animal;
        var o = outline;
        if(a.attrs.x > o.x - 20 && a.attrs.x < o.x + 20 && a.attrs.y > o.y - 20 && a.attrs.y < o.y + 20) {
          return true;
        }
        else {
          return false;
        }
      }
      function drawBackground(background, beachImg, text) {
        var canvas = background.getCanvas();
        var context = background.getContext();

        context.drawImage(beachImg, 0, 0);
        context.font = "20pt Calibri";
        context.textAlign = "center";
        context.fillStyle = "white";
        context.fillText(text, background.getStage().getWidth() / 2, 40);
      }
      function initStage(images) {
        var stage = new Kinetic.Stage({
          container: "container",
          width: 578,
          height: 530
        });
        var background = new Kinetic.Layer();
        var animalLayer = new Kinetic.Layer();
        var animalShapes = [];
        var score = 0;

        // image positions
        var animals = {
          snake: {
            x: 10,
            y: 70
          },
          giraffe: {
            x: 90,
            y: 70
          },
          monkey: {
            x: 275,
            y: 70
          },
          lion: {
            x: 400,
            y: 70
          },
        };

        var outlines = {
          snake_black: {
            x: 275,
            y: 350
          },
          giraffe_black: {
            x: 390,
            y: 250
          },
          monkey_black: {
            x: 300,
            y: 420
          },
          lion_black: {
            x: 100,
            y: 390
          },
        };

        // create draggable animals
        for(var key in animals) {
          // anonymous function to induce scope
          (function() {
            var privKey = key;
            var anim = animals[key];

            var animal = new Kinetic.Image({
              image: images[key],
              x: anim.x,
              y: anim.y,
              draggable: true
            });
            
            animal.createImageBuffer();

            animal.on("dragstart", function() {
              animal.moveToTop();
              animalLayer.draw();
            });
            /*
             * check if animal is in the right spot and
             * snap into place if it is
             */
            animal.on("dragend", function() {
              var outline = outlines[privKey + "_black"];
              if(!animal.inRightPlace && isNearOutline(animal, outline)) {
                animal.attrs.x = outline.x;
                animal.attrs.y = outline.y;
                animalLayer.draw();
                // disable drag and drop
                animal.setDraggable(false);
                animal.inRightPlace = true;

                if(++score >= 4) {
                  var text = "You win! Enjoy your booty!"
                  drawBackground(background, images.beach, text);
                }
              }
            });
            // make animal glow on mouseover
            animal.on("mouseover", function() {
              animal.setImage(images[privKey + "_glow"]);
              animalLayer.draw();
              document.body.style.cursor = "pointer";
            });
            // return animal on mouseout
            animal.on("mouseout", function() {
              animal.setImage(images[privKey]);
              animalLayer.draw();
              document.body.style.cursor = "default";
            });

            animal.on("dragmove", function() {
              document.body.style.cursor = "pointer";
            });

            animalLayer.add(animal);
            animalShapes.push(animal);
          })();
        }

        // create animal outlines
        for(var key in outlines) {
          // anonymous function to induce scope
          (function() {
            var imageObj = images[key];
            var out = outlines[key];

            var outline = new Kinetic.Image({
              image: imageObj,
              x: out.x,
              y: out.y
            });

            animalLayer.add(outline);
          })();
        }

        stage.add(background);
        stage.add(animalLayer);

        drawBackground(background, images.beach, "Ahoy! Put the animals on the beach!");
      }

      window.onload = function() {
        var sources = {
          beach: "beach.png",
          snake: "snake.png",
          snake_glow: "snake-glow.png",
          snake_black: "snake-black.png",
          lion: "lion.png",
          lion_glow: "lion-glow.png",
          lion_black: "lion-black.png",
          monkey: "monkey.png",
          monkey_glow: "monkey-glow.png",
          monkey_black: "monkey-black.png",
          giraffe: "giraffe.png",
          giraffe_glow: "giraffe-glow.png",
          giraffe_black: "giraffe-black.png",
        };
        loadImages(sources, initStage);
      };

      $(document).ready(function(){
          $('#container').center();
      });

    </script>
    
  <div id="container" style='width:578px;height:530px;'></div>

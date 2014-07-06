    <style>
      canvas {
        border: 1px solid #9C9898;
      }
      #container {
        background-image: url("http://www.html5canvastutorials.com/demos/assets/blue-background.jpg");
        display: inline-block;
        overflow: hidden;
        height: 365px;
        width: 580px;
      }
    </style>
    
        <div id="container" style='width:580px;height:365px;'></div>
    <script>
      function tango(layer) {
        for(var n = 0; n < layer.getChildren().length; n++) {
          var shape = layer.getChildren()[n];
          var stage = shape.getStage();
          shape.transitionTo({
            rotation: Math.random() * Math.PI * 2,
            radius: Math.random() * 100 + 20,
            x: Math.random() * stage.getWidth(),
            y: Math.random() * stage.getHeight(),
            opacity: Math.random(),
            duration: 1,
            easing: 'ease-in-out'
          });
        }
      }
      var stage = new Kinetic.Stage({
        container: 'container',
        width: 578,
        height: 363
      });

      var layer = new Kinetic.Layer();

      var colors = ['red', 'orange', 'yellow', 'green', 'blue', 'purple'];
      for(var n = 0; n < 10; n++) {
        var shape = new Kinetic.RegularPolygon({
          x: Math.random() * stage.getWidth(),
          y: Math.random() * stage.getHeight(),
          sides: Math.ceil(Math.random() * 5 + 3),
          radius: Math.random() * 100 + 20,
          fill: colors[Math.round(Math.random() * 5)],
          stroke: 'black',
          opacity: Math.random(),
          strokeWidth: 4,
          draggable: true
        });

        layer.add(shape);
      }

      stage.add(layer);

      setTimeout("play()", 2000);

      function play() {
          tango(layer);
          setTimeout("play()", 5000);
      }
      
      $(document).ready(function(){
          $('#container').center();
      });
      
    </script>
<script>
      function update(group, activeAnchor) {
        var topLeft = group.get(".topLeft")[0];
        var topRight = group.get(".topRight")[0];
        var bottomRight = group.get(".bottomRight")[0];
        var bottomLeft = group.get(".bottomLeft")[0];
        var image = group.get(".image")[0];

        // update anchor positions
        switch (activeAnchor.getName()) {
          case "topLeft":
            topRight.attrs.y = activeAnchor.attrs.y;
            bottomLeft.attrs.x = activeAnchor.attrs.x;
            break;
          case "topRight":
            topLeft.attrs.y = activeAnchor.attrs.y;
            bottomRight.attrs.x = activeAnchor.attrs.x;
            break;
          case "bottomRight":
            bottomLeft.attrs.y = activeAnchor.attrs.y;
            topRight.attrs.x = activeAnchor.attrs.x;
            break;
          case "bottomLeft":
            bottomRight.attrs.y = activeAnchor.attrs.y;
            topLeft.attrs.x = activeAnchor.attrs.x;
            break;
        }

        image.setPosition(topLeft.attrs.x, topLeft.attrs.y);

        var width = topRight.attrs.x - topLeft.attrs.x;
        var height = bottomLeft.attrs.y - topLeft.attrs.y;
        if(width && height) {
          image.setSize(width, height);
        }
      }
      function addAnchor(group, x, y, name) {
        var stage = group.getStage();
        var layer = group.getLayer();

        var anchor = new Kinetic.Circle({
          x: x,
          y: y,
          stroke: "#666",
          fill: "#ddd",
          strokeWidth: 2,
          radius: 8,
          name: name,
          draggable: true
        });

        anchor.on("dragmove", function() {
          update(group, this);
          layer.draw();
        });
        anchor.on("mousedown touchstart", function() {
          group.setDraggable(false);
          this.moveToTop();
        });
        anchor.on("dragend", function() {
          group.setDraggable(true);
          layer.draw();
        });
        // add hover styling
        anchor.on("mouseover", function() {
          var layer = this.getLayer();
          document.body.style.cursor = "pointer";
          this.setStrokeWidth(4);
          layer.draw();
        });
        anchor.on("mouseout", function() {
          var layer = this.getLayer();
          document.body.style.cursor = "default";
          this.setStrokeWidth(2);
          layer.draw();
        });

        group.add(anchor);
      }
      function loadImages(sources, callback) {
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
          images[src].src = sources[src];
        }
      }
      function initStage(images) {
        var stage = new Kinetic.Stage({
          container: "container",
          width: 578,
          height: 400
        });
        var darthVaderGroup = new Kinetic.Group({
          x: 270,
          y: 100,
          draggable: true
        });
        var yodaGroup = new Kinetic.Group({
          x: 100,
          y: 110,
          draggable: true
        });
        var layer = new Kinetic.Layer();

        /*
         * go ahead and add the groups
         * to the layer and the layer to the
         * stage so that the groups have knowledge
         * of its layer and stage
         */
        layer.add(darthVaderGroup);
        layer.add(yodaGroup);
        stage.add(layer);

        // darth vader
        var darthVaderImg = new Kinetic.Image({
          x: 0,
          y: 0,
          image: images.darthVader,
          width: 200,
          height: 138,
          name: "image"
        });

        darthVaderGroup.add(darthVaderImg);
        addAnchor(darthVaderGroup, 0, 0, "topLeft");
        addAnchor(darthVaderGroup, 200, 0, "topRight");
        addAnchor(darthVaderGroup, 200, 138, "bottomRight");
        addAnchor(darthVaderGroup, 0, 138, "bottomLeft");

        darthVaderGroup.on("dragstart", function() {
          this.moveToTop();
        });
        // yoda
        var yodaImg = new Kinetic.Image({
          x: 0,
          y: 0,
          image: images.yoda,
          width: 93,
          height: 104,
          name: "image"
        });

        yodaGroup.add(yodaImg);
        addAnchor(yodaGroup, 0, 0, "topLeft");
        addAnchor(yodaGroup, 93, 0, "topRight");
        addAnchor(yodaGroup, 93, 104, "bottomRight");
        addAnchor(yodaGroup, 0, 104, "bottomLeft");

        yodaGroup.on("dragstart", function() {
          this.moveToTop();
        });

        stage.draw();
      }

      window.onload = function() {
        var sources = {
          darthVader: "http://www.html5canvastutorials.com/demos/assets/darth-vader.jpg",
          yoda: "http://www.html5canvastutorials.com/demos/assets/yoda.jpg"
        };
        loadImages(sources, initStage);
      };

      $(document).ready(function(){
          $('#container').center();
      });
</script>

<div id="container" style='width:578px;height:400px;'></div>

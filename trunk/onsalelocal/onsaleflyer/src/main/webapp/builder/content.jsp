<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<style>
	.tool-btn {
		width:100px; height:32px;
		margin-left:4px;
	}
</style>

<script type="text/javascript" src='<c:url value="/script/kinetic-v4.0.5.js"/>'></script>
<script type="text/javascript">
	$(document).ready(function(){
//		$('#container').center();
	});

	window.onload = function() {
		initStage();
	}

    function initStage() {
        stage = new Kinetic.Stage({
          container: "container",
          width: 800,
          height: 600
        });
        stage.draw();
    }

    function addText() {
        var title = "2/$7 or $3.99 ea.";
        title = prompt("Please input text", title);
        if(title == null || title.length == 0)
            return;
    	var text = new Kinetic.Text({
    	    x: 20,
    	    y: 20,
    	    text: title,
    	    textFill: 'black',
    	    detectionType: 'path',
    	    draggable: true,
    	    fontSize: 48
    	});
    	text.on('dbclick', function() {
             alert(0);
        });
        var layer = new Kinetic.Layer();
        layer.add(text);
        stage.add(layer);
        stage.draw();
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
    
    function addImage() {
        var url = '<c:url value="/builder/images/on_sale_256x256.png"/>';
        url = prompt('Please input image url', url);
        if(url == null || url.length == 0)
            return;
        var imageObj = new Image();
        imageObj.onload = function() {
            var imgGroup = new Kinetic.Group({
                x: 256,
                y: 256,
                draggable: true
            });
            var layer = new Kinetic.Layer();
            stage.add(layer);
    		layer.add(imgGroup);        
            addAnchor(imgGroup, 0, 0, "topLeft");
            addAnchor(imgGroup, 255, 0, "topRight");
            addAnchor(imgGroup, 255, 255, "bottomRight");
            addAnchor(imgGroup, 0, 255, "bottomLeft");
            imgGroup.on("dragstart", function() {
                this.moveToTop();
              });            
            var addedImg = new Kinetic.Image({
                x: 0,
                y: 0,
                image: imageObj,
                width: 255,
                height: 255,
                name: "image"
              });

            imgGroup.add(addedImg);
            stage.draw();
        }
        imageObj.src = url;
    }
</script>

<div style="margin-bottom:8px;margin-top:8px;margin-left:-4px;">
	<div style="float:left;"><button class="tool-btn" onclick="addImage()">Image</button></div>
	<div style="float:left;"><button class="tool-btn" onclick="addText()">Text</button></div>
	<div style="float:left;"><button class="tool-btn">Upload</button></div>
	<div style="float:left;"><button class="tool-btn">Preview</button></div>
	<div style="float:left;"><button class="tool-btn">Save</button></div>
	<div style="float:left;"><button class="tool-btn">Delete</button></div>
	<div style="float:left;"><button class="tool-btn">Reset</button></div>
</div>
<div style="clear:both;height:1px;"></div>

<div id="container" style="float:left;width:800px;height:600px;border:solid 1px #ccc;"></div>


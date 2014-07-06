function Text(startPoint, msg, color) {
	var thisObj = this;
	
	startPoint.setParent(thisObj);
	
	thisObj.startPoint = startPoint;
	thisObj.msg = msg;
	thisObj.selected = false;
	thisObj.color = color;
	
	this.save = function(array) {
		array.push({
			x:this.startPoint.x,
			y:this.startPoint.y,
			msg:this.msg,
			color:this.color,
			type:'Text'
		});
	}
	
	this.shape = new Kinetic.Shape(function() {
           var context = this.getContext();
           context.beginPath();
           context.lineWidth = 2;
           
           var height = 15;
           
           context.font = "12pt Calibri";
           context.fillStyle = thisObj.getStrokeStyle();
           context.fillText(thisObj.msg, 
        		   thisObj.startPoint.x, thisObj.startPoint.y + height);
           
	   		var textMetrics = context.measureText(thisObj.msg);
			thisObj.width = textMetrics.width;
			thisObj.height = height;
		
           context.closePath();
       });
	
	this.getShape = function() {
		return this.shape;
	}
	
	this.setStrokeStyle = function(style) {
		this.strokeStyle = style;
	}
	
	this.setSelected = function(selected) {
		this.selected = selected;
	}
	
	this.getStrokeStyle = function() {
		return this.color;
	}
	
	this.same = function(another) {
		return this.startPoint.x == another.startPoint.x 
			&& this.startPoint.y == another.startPoint.y ;
	}
	
	this.inside = function(x, y) {
		return x > this.startPoint.x && x < (this.startPoint.x + this.width) && y > this.startPoint.y && y < (this.startPoint.y + this.height);
	}
	
    this.distanceFrom = function(x, y) {
    	if(this.inside(x, y)) {
    		return 0;
    	}
        var dis0 = distance({x:x, y:y}, {x:this.startPoint.x, y:this.startPoint.y});
        var dis1 = distance({x:x, y:y}, {x:this.startPoint.x + this.width, y:this.startPoint.y});
        var dis2 = distance({x:x, y:y}, {x:this.startPoint.x, y:this.startPoint.y + this.height});
        var dis3 = distance({x:x, y:y}, {x:this.startPoint.x + this.width, y:this.startPoint.y + this.height});
        
    	var dis = Math.min(dis0, dis1, dis2, dis3);
    	android.log("from this " + this.startPoint.x 
    			+ "," + this.startPoint.y 
    			+ " to " + x + ", " + y 
    			+ " distance is " + dis + "." 
    			+ " text width: " + this.width
    			+ ", height:" + this.height);
    	return dis;
    };
    
    this.change = function() {
		var msg = prompt("Please input new text", this.msg);
		if(!msg) {
			return;
		}
		this.msg = msg;
    }
}

function newText() {
	var msg = prompt("Please input text");
	if(!msg) {
		return;
	}

	if (gaoshin.currentPoint != null) {
		gaoshin.currentPoint.parent.setSelected(false);
		gaoshin.currentPoint = null;
	}
	
	var top = Math.floor(android.getOffsetTop() / android.getScale());
	var left = Math.floor(android.getOffsetLeft() / android.getScale());
	var cornor = Math.floor(120/android.getScale());
	
	var x = left + cornor;
	var y = top + cornor;

	createText({x:x, y:y, msg:msg, color:currentColor});
}

function createText(param) {
	var x = param.x;
	var y = param.y;
	var msg = param.msg;
	var point0 = new Point();
	point0.setPosition(x, y);
	gaoshin.stage.add(point0.getShape());
	
	var line = new Text(point0, msg, param.color);
	line.setSelected(true);
	gaoshin.stage.add(line.getShape());
	
	gaoshin.objects.push(line);
	gaoshin.currentPoint = point0;
}
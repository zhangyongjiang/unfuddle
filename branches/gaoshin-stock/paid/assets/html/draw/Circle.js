function Circle(startPoint, endPoint, color) {
	var thisObj = this;
	
	startPoint.setParent(thisObj);
	endPoint.setParent(thisObj);
	
	thisObj.startPoint = startPoint;
	thisObj.endPoint = endPoint;
	thisObj.selected = false;
	thisObj.color = color;
	
	this.save = function(array) {
		array.push({
			x:this.startPoint.x, 
			y:this.startPoint.y, 
			radius:this.getRadius(),
			color:this.color,
			type: 'Circle'
		});
	}
	
	this.shape = new Kinetic.Shape(function() {
           var context = this.getContext();
           context.beginPath();
           context.lineWidth = 2;
           context.strokeStyle = thisObj.color;
           context.arc(thisObj.startPoint.x, thisObj.startPoint.y, 
        		   thisObj.getRadius(), 0, 2*Math.PI, false);

           context.stroke();
           context.closePath();
           
           if(thisObj.selected) {
               context.beginPath();
               context.lineWidth = 2;
               context.strokeStyle = gaoshin.colorUnselected;
	           context.moveTo(thisObj.startPoint.x, thisObj.startPoint.y);
	           context.lineTo(thisObj.endPoint.x, thisObj.endPoint.y);
               context.stroke();
               context.closePath();
           }
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
	
	this.same = function(another) {
		return this.startPoint.x == another.startPoint.x 
			&& this.startPoint.y == another.startPoint.y 
			&& this.endPoint.x == another.endPoint.x 
			&& this.endPoint.y == another.endPoint.y; 
	}
	
	this.getRadius = function() {
		return distance(this.startPoint, this.endPoint);
	}
	
	this.inside = function(x, y) {
		return distance(this.startPoint, {x:x, y:y}) < this.getRadius();
	}
	
    this.distanceFrom = function(x, y) {
        return distance(this.startPoint, {x:x, y:y}) - this.getRadius();
    };
}

function newCircle() {
	if (gaoshin.currentPoint != null) {
		gaoshin.currentPoint.parent.setSelected(false);
		gaoshin.currentPoint = null;
	}
	
	var top = Math.floor(android.getOffsetTop() / android.getScale());
	var left = Math.floor(android.getOffsetLeft() / android.getScale());
	var cornor = Math.floor(90/android.getScale());
	
	var x = left + cornor;
	var y = top + cornor;
	createCircle({x:x, y:y, radius:cornor, color:currentColor});
}

function createCircle(param) {
	var x = param.x;
	var y = param.y;
	var radius = param.radius;
	var point0 = new Point();
	point0.setPosition(x, y);
	gaoshin.stage.add(point0.getShape());

	var point1 = new Point();
	point1.setPosition(x+radius, y);
	gaoshin.stage.add(point1.getShape());

	var line = new Circle(point0, point1, param.color);
	line.setSelected(true);
	gaoshin.stage.add(line.getShape());
	
	gaoshin.objects.push(line);
	gaoshin.currentPoint = point1;
}
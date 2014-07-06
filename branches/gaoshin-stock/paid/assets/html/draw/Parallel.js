function Parallel(startPoint, endPoint, startPoint1, color) {
	var line = this;
	
	startPoint.setParent(line);
	endPoint.setParent(line);
	startPoint1.setParent(line);
	
	line.startPoint = startPoint;
	line.endPoint = endPoint;
	line.startPoint1 = startPoint1;
	line.selected = false;
	line.main = true;
	line.color = color;
	
	this.save = function(array) {
		array.push({
			x0:this.startPoint.x,
			y0:this.startPoint.y,
			x1:this.endPoint.x,
			y1:this.endPoint.y,
			x2:this.startPoint1.x,
			y2:this.startPoint1.y,
			color:this.color,
			type: 'Parallel',
		});
	}
	
	this.shape = new Kinetic.Shape(function() {
           var context = this.getContext();
           context.beginPath();
           context.lineWidth = 2;
           context.strokeStyle = line.getStrokeStyle();
           context.moveTo(line.startPoint.x, line.startPoint.y);
           context.lineTo(line.endPoint.x, line.endPoint.y);
           
           context.moveTo(line.startPoint1.x, line.startPoint1.y);
           context.lineTo(line.startPoint1.x + line.diffX(), line.startPoint1.y+line.diffY());
           
           context.stroke();
           context.closePath();
       });
	
	this.diffX = function() {
		return this.endPoint.x - this.startPoint.x;
	}
	
	this.diffY = function() {
		return this.endPoint.y - this.startPoint.y;
	}
	
	this.endPoint1 = function() {
		return {x: this.startPoint1.x + this.diffX(), y: this.startPoint1.y + this.diffY()};
	}
	
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
		return this.main ? this.color : gaoshin.colorUnselected;
	}
	
	this.same = function(another) {
		return this.startPoint.x == another.startPoint.x 
			&& this.startPoint.y == another.startPoint.y 
			&& this.endPoint.x == another.endPoint.x 
			&& this.endPoint.y == another.endPoint.y 
			&& this.startPoint1.x == another.startPoint1.x 
			&& this.startPoint1.y == another.startPoint1.y; 
	}
	
    this.distanceFrom = function(x, y) {
    	var c = {x: x, y: y};
        return Math.min(
        		new Segment(this.startPoint, this.endPoint).distanceFrom(c),
        		new Segment(this.startPoint1, this.endPoint1()).distanceFrom(c)
        		);
    };
}

function newParallel() {
	if (gaoshin.currentPoint != null) {
		gaoshin.currentPoint.parent.setSelected(false);
		gaoshin.currentPoint = null;
	}
	
	var top = Math.floor(android.getOffsetTop() / android.getScale());
	var left = Math.floor(android.getOffsetLeft() / android.getScale());
	var cornor = Math.floor(90/android.getScale());
	
	var x = left + cornor;
	var y = top + cornor;
	var x1 = x+cornor*2;
	var y1 = y+cornor;
	var x2 = x;
	var y2 = y + cornor;

	createParallel({x0:x, y0:y, x1:x1, y1:y1, x2:x2, y2:y2, color:currentColor});
}

function createParallel(param) {
	var x = param.x0;
	var y = param.y0;
	var x1 = param.x1;
	var y1 = param.y1;
	
	var point0 = new Point();
	point0.setPosition(x, y);
	gaoshin.stage.add(point0.getShape());

	var point1 = new Point();
	point1.setPosition(x1, y1);
	gaoshin.stage.add(point1.getShape());

	var point2 = new Point();
	point2.setPosition(param.x2, param.y2);
	gaoshin.stage.add(point2.getShape());

	var line = new Parallel(point0, point1, point2, param.color);
	line.setSelected(true);
	gaoshin.stage.add(line.getShape());
	
	gaoshin.objects.push(line);
	gaoshin.currentPoint = point1;
}

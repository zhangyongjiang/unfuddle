function Point() {
	var point = this;
	point.parent = null;
	point.showing = false;
	
	this.shape = new Kinetic.Shape(function() {
		if(point.parent == null || !point.parent.selected) {
			point.showing = false;
			return;
		}
		point.showing = true;
		
        var context = this.getContext();
        context.beginPath();
        context.lineWidth = 2;
        context.strokeStyle = point.getStrokeStyle();
        context.moveTo(point.x, point.y);
        context.lineTo(point.x - point.radius(), point.y - point.radius());
        context.arc(point.x, point.y - point.radius(), point.radius(), Math.PI, 2*Math.PI, false);
        context.lineTo(point.x, point.y);
        context.stroke();
        context.closePath();
    });
	
	this.getStrokeStyle = function() {
		if(this.parent == null) return gaoshin.colorSelected;
		if(this.parent.selected) {
			return this.same(gaoshin.currentPoint) ? gaoshin.colorSelected : gaoshin.colorUnselected;
		}
		return gaoshin.colorSelected;
	}
	
	this.getShape = function() {
		return this.shape;
	}
	
	this.setPosition = function(x, y) {
		var newx = x;
		var newy = y;
		var diff = {x:newx-this.x, y:newy-this.y};
		this.x = newx;
		this.y = newy;
		return diff;
	}
	
	this.move = function(diff) {
		this.x += diff.x;
		this.y += diff.y;
	}
	
	this.setParent = function(line) {
		this.parent = line;
	}
	
	this.inside = function(x, y) {
		if(!this.showing) {
			return false;
		}
		
		var dx = x - this.x;
		var dy = y - (this.y - this.radius());
		var dis = Math.sqrt ( dx * dx + dy * dy );
		return dis < this.radius();
	}
	
	this.same = function(another) {
		if(another == null) return false;
		return this.x == another.x && this.y == another.y;
	}
	
	this.radius = function() {
		return pointRadius();
	}
}

function pointRadius() {
	return 50 / android.getScale();
}
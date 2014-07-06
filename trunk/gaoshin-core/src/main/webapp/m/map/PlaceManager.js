oneMileLat = 0.0145;
oneMileLngTable = [
        0.0144, 0.0144, 0.0144, 0.0144, 0.0145, 0.0145, 0.0145, 0.0145,
        0.0146, 0.0146, 0.0146, 0.0147, 0.0147, 0.0148, 0.0149, 0.0149,
        0.0150, 0.0151, 0.0152, 0.0153, 0.0154, 0.0155, 0.0156, 0.0157,
        0.0158, 0.0159, 0.0161, 0.0162, 0.0163, 0.0165, 0.0167, 0.0168,
        0.0170, 0.0172, 0.0174, 0.0176, 0.0178, 0.0181, 0.0183, 0.0186,
        0.0188, 0.0191, 0.0194, 0.0197, 0.0201, 0.0204, 0.0208, 0.0212,
        0.0216, 0.0220, 0.0225, 0.0229, 0.0235, 0.0240, 0.0246, 0.0252,
        0.0258, 0.0265, 0.0273, 0.0281, 0.0289, 0.0298, 0.0308, 0.0318,
        0.0330, 0.0342, 0.0355, 0.0370, 0.0386, 0.0403, 0.0423, 0.0444,
        0.0468, 0.0495, 0.0525, 0.0559, 0.0598, 0.0643, 0.0696, 0.0758,
        0.0833, 0.0925, 0.1040, 0.1187, 0.1384, 0.1660, 0.2074, 0.2765,
        0.4147, 0.8293
];

function Cell(lat, lng) {
	this.lat = lat;
	this.lng = lng;

	// public 
	this.equals = function(cell) {
		return this.lat == cell.lat && this.lng == cell.lng;
	}
	
	this.distanceFrom = function(cell) {
		var geo0 = new GLatLng(this.lat/1000000.0, this.lng/1000000.0);
		var geo1 = new GLatLng(cell.lat/1000000.0, cell.lng/1000000.0);
		var dis = geo0.distanceFrom(geo1) / 1600;
		return dis;
	}
}

function CellLayerManager(latStep, lngStep) {
	this.latStep = parseInt(latStep * 1000000);
	this.lngStep = parseInt(lngStep * 1000000);
	this.cells = new Array();
	
	// public
	this.addNewPlaces = function(latLngBounds, zoom) {
		var southWest = latLngBounds.getSouthWest();
		var northEast = latLngBounds.getNorthEast();
		var lat0 = parseInt(southWest.lat() * 1000000);
		var lng0 = parseInt(southWest.lng() * 1000000);
		var lat1 = parseInt(northEast.lat() * 1000000);
		var lng1 = parseInt(northEast.lng() * 1000000);

		var cells = this.getCells(lat0, lng0, lat1, lng1);
		for(var i=0; i<cells.length; i++) {
			if (this.cellExists(cells[i])) continue;
			this.addCell(cells[i], zoom);
		}
	}
	
	this.cellExists = function(cell) {
		for (var i=0; i<this.cells.length; i++) {
			if(this.cells[i].equals(cell))
				return true;
		}
		return false;
	}
	
	this.drawRect = function(lat0, lng0, lat1, lng1) {
		var points = new Array(new GLatLng(lat0, lng0), new GLatLng(lat1, lng0), new GLatLng(lat1, lng1), new GLatLng(lat0, lng1));
		var polygon = new GPolygon(points, '#FF0000', 2, 0.7);
		gView.map.addOverlay(polygon);
		return polygon;
	}
	
	/* return places retrieved from server */
	this.addCell = function(cell, zoom) {
		this.cells.push(cell);
		var r = (new Cell(0,0)).distanceFrom(new Cell(this.latStep, this.lngStep));

		// call server to get new places
		var lat0 = cell.lat / 1000000;
		var lng0 = cell.lng / 1000000;
		var lat1 = (cell.lat + this.latStep) / 1000000;
		var lng1 = (cell.lng + this.lngStep) / 1000000 ;
		var query = "lat=" + lat0 + "&lng=" + lng0 + "&lat1=" + lat1 + "&lng1=" + lng1;
		var url = serverBase + "/user/search?format=json&nome=false&" + query;

		var objHTTP = GXmlHttp.create();
		objHTTP.open("GET", url, true);
		objHTTP.setRequestHeader("Accept", "application/json");
		objHTTP.onreadystatechange = function() {
		  if (objHTTP.readyState == 4) {
		      var json = objHTTP.responseText;
	            var users = JSON.parse(json);
	            if(users.list) {
    	            for (var i=0; i<users.list.length; i++) {
    	                var user = users.list[i];
                        var uid = user.id;
                        var gender = user.gender;
                        var age = new Date().getFullYear() - user.birthYear;
                        var name = user.name + "&lt;br/&gt;" + age;
                        var lat = Number(user.locationDistance.latitude);
                        lat = lat * (1 + (Math.random()-0.5)/4000);
                        var lng = Number(user.locationDistance.longitude);
                        lng = lng * (1 + (Math.random()-0.5)/4000);
    	                var id = user.id;
                        var description = "";
                        if(user.interests)
                            description = user.interests + "&lt;br/&gt;";
    	                if(user.icon) {
    	                    description += "&lt;img border=&apos;0&apos; width=80 src=&apos;" + serverBase + "/user/icon/" + id + "&apos;&amp;gt;";
    	                } else {
    	                    description += "&lt;img border=&apos;0&apos; width=80 src=&apos;" + serverBase + "/m/images/icon_person_48x48.png" + "&apos;&amp;gt;";
    	                }
    	                var xml = '<?xml version="1.0"?><place id="' + id + '"><routes><route loop="false"><point><geo><lat>' + lat + '</lat><lng>' + lng + '</lng></geo><localized_info><info la="en"><caption>' + name + '</caption><description>' + description + '</description><uid>' + uid + '</uid><gender>' + gender + '</gender><age>' + age+ '</age></info></localized_info></point></route></routes></place>';
    	                gView.addPlaceFromXml(xml);
    	            }
	            }
		  }
		  else {
		  }
		}
		objHTTP.send(null);
	}
	
	this.roundLat = function(lat) {
		if (lat % this.latStep == 0)
			return lat;
		if (lat >= 0)
			return parseInt(lat / this.latStep) * this.latStep;
		else
			return parseInt(lat / this.latStep - 1) * this.latStep
	}
	
	this.roundLng = function(lng) {
		if ((lng % this.lngStep) == 0)
			return lng;
		if (lng >= 0)
			return parseInt(lng / this.lngStep) * this.lngStep;
		else 
			return parseInt(lng / this.lngStep - 1) * this.lngStep
	}
	
	this.getCells = function(lat0, lng0, lat1, lng1) {
		lat0 = this.roundLat(lat0);
		lng0 = this.roundLng(lng0);
		lat1 = this.roundLat(lat1);
		lng1 = this.roundLng(lng1);
		if (lat0 == lat1)
			lat1 = lat0 + this.latStep;
		if (lng0 == lng1)
			lng1 = lng0 + this.lngStep;
		
		var latStep = this.latStep * (lat0 > lat1 ? -1 : 1);
		var lngStep = this.lngStep * (lng0 > lng1 ? -1 : 1);
		var latHowmany = (lat1 - lat0) / latStep;
		var lngHowmany = (lng1 - lng0) / lngStep;
		var cells = new Array();
		for (var i=0; i<=latHowmany; i++) {
			for(var j=0; j<=lngHowmany; j++) {
				cells.push (new Cell(lat0 + i * latStep, lng0 + j * lngStep));
			}
		}
		return cells;
	}
}

function SchoolPlaceManager(){
	var layer14 = new CellLayerManager(0.080000, 0.080000); // zoom = 13
	var layer13 = new CellLayerManager(0.160000, 0.160000); // zoom = 13
	var layer12 = new CellLayerManager(0.320000, 0.320000); // zoom = 12
    var layer11 = new CellLayerManager(0.640000, 0.640000); // zoom = 11
    var layer10 = new CellLayerManager(1.28,  1.28); // zoom = 10
    var layer9 = new CellLayerManager(2.56,  2.56); // zoom = 10
    var layer8 = new CellLayerManager(5.12,  5.12); // zoom = 10
/*
	var layer7  = new CellLayerManager(x2560000, 2560000); // zoom = 7
	var layer6  = new CellLayerManager(x5120000, 5120000); // zoom = 6
	var layer5  = new CellLayerManager(x8000000, 8000000); // zoom = 5
*/
	this.layers = new Array(
			null,	// 1
			null,	// 2
			null,	// 3
			null,	// 4
			null,	// 5
			null,	// 6
			null,	// 7
			null,	// 8
			null,	// 9
			layer10,	// 10
            layer11,    // 11
            layer12,    // 12
			layer13,	// 13
			layer14,	// 14
			layer14,	// 15
			layer14,	// 16
            layer14, // 17
            layer14, // 18
            layer14, // 19
            layer14, // 20
            layer14, // 21
            layer14, // 22
            layer14 // 23
	);
	
	this.onMapChanged = function() {
		var zoom = gView.map.getZoom();
		var bounds = gView.map.getBounds();
		var layerManager = this.layers[zoom - 1];
		if (layerManager != null) {
			layerManager.addNewPlaces(bounds, zoom);
		}
		else {
			
		}
	}
}

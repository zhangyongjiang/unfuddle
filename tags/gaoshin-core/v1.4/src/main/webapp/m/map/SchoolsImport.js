function getSchoolList() {
	var countrySchools = new Array();
	var count = 0;
	
	var stateLinks = null;
	var stateFileName = "c:\\mydata\\www\\schools\\states.txt";
	if (fso.fileExists(stateFileName)) {
		var jsonTxt = ReadFromFile(fso, stateFileName);
		stateLinks = JSON.parse(jsonTxt);
	} 
	else {
		var stateListHtml = getHttpData("GET", "http://www.city-data.com/", "");
		stateLinks = search(stateListHtml, '<a HREF="/city/', '">');
		WriteToFile(fso, stateFileName, JSON.stringify(stateLinks));
	}

	for (var i=0; i<stateLinks.length; i++) {
		try {
			var cities = null;
			var stateName = stateLinks[i].substring(0, stateLinks[i].indexOf(".html")).replace('-', ' ');
			var dirState = "c:\\mydata\\www\\schools\\" + stateName;
			var fileName = dirState + "\\cities.txt";

			if (!fso.folderExists(dirState)) {
				fso.createFolder(dirState);
			}
			
			if (fso.fileExists(fileName)) {
				var jsonTxt = ReadFromFile(fso, fileName);
				cities = JSON.parse(jsonTxt);
			} 
			else {
				var stateHtml = getHttpData("GET", "http://www.city-data.com/city/" + stateLinks[i], "");
				cities = searchLine(stateHtml, stateLinks[i], '<A HREF="', '"');
				WriteToFile(fso, fileName, JSON.stringify(cities));
			}

			var stateSchools = new Array();
			for (var j=0; j<cities.length; j++) {
				try {
					var cityName = cities[j].substring(0, cities[j].indexOf("-"+stateLinks[i])).replace('-', ' ');
					var dirCity = dirState + "\\" + cityName;
					var fileName = dirCity + "\\schools.txt";
					var schools = null;
					
					if (!fso.folderExists(dirCity)) {
						fso.createFolder(dirCity);
					}
					
					if (fso.fileExists(fileName)) {
						var jsonTxt = ReadFromFile(fso, fileName);
						schools = JSON.parse(jsonTxt);
					}
					else {
						//if (confirm("quit ("+stateName + "-" + cityName+ ")?")) return;
						var cityLink = "http://www.city-data.com/city/" + cities[j];
						var cityHtml = getHttpData("GET", cityLink, "");
						schools = searchLine(cityHtml, "Location:");
						WriteToFile(fso, fileName, JSON.stringify(schools));
					}
					
					var citySchools = new Array();
					if (schools.length>0) {
						for(var k=0; k<schools.length; k++) {
							try {
								var name = search(schools[k], "<b>", "</b>")[0];
								var location = search(schools[k], "Location: ", ';')[0];
								citySchools[k] = {name:name, location:location};
								window.status = stateName + ", " + cityName + ", " + location;
							} catch (eschool) {
								AppendToFile(fso, "c:\\mydata\\www\\schools\\error.txt", "exception school: " + cities[j] + "^" + schools[k] + "\n");
							}
						}
					}
					stateSchools[j] = {name:cityName, schools:citySchools};
				} catch (se) {
					AppendToFile(fso, "c:\\mydata\\www\\schools\\error.txt", "exception city: " + cities[j] + "\n");
				}
			}
			countrySchools[i] = {name:stateName, cities:stateSchools};
		} catch (ce) {
			AppendToFile(fso, "c:\\mydata\\www\\schools\\error.txt", "exception state: " + stateLinks[i] + "\n");
		}
	}
	alert(0);
	return countrySchools;
}

var fso = null;
var gGeocoder = null;
var geoCounter = 0;
var eCounter = 0;
function getLatLng(iState, iCity, iSchool) {
	var states = JSON.parse(ReadFromFile(fso, "c:\\mydata\\www\\schools\\states.txt"));
	var stateName = states[iState].substring(0, states[iState].indexOf(".html")).replace('-', ' ');
	var dirState = "c:\\mydata\\www\\schools\\" + stateName;
	var citiesFileName = dirState + "\\cities.txt";
	var cities = null;
	try {
		cities = JSON.parse(ReadFromFile(fso, citiesFileName));
	} catch (e) {
	}
	
	if (cities==null || cities.length==0) {
		iSchool = 0;
		iCity = 0;
		iState++;
		if(iState >= states.length) {
			return;
		}
		createCookie("state", iState, 365);
		createCookie("city", iCity, 365);
		createCookie("school", iSchool, 365);
		var f = "getLatLng(" + iState +", " + iCity + ", " + iSchool +")";
		setTimeout(f, 5);
		return;
	}

	var cityName = cities[iCity].substring(0, cities[iCity].indexOf("-"+states[iState])).replace('-', ' ');
	var dirCity = dirState + "\\" + cityName;
	var schoolsFileName = dirCity + "\\schools.txt";
	var schools = null;
	try { 
		schools = JSON.parse(ReadFromFile(fso, schoolsFileName));
	} catch (e) {
	}

	try {
//			window.status = "states: " + states.length + ", iState:" + iState + ", cities:" + cities.length + ", iCity:" + iCity + ", schools:" + schools.length + ", iSchool:" + iSchool;
	} catch (e) {
	}

	if (schools==null || schools.length==0) {
		iSchool = 0;
		iCity ++;

		if (iCity>=cities.length) {
			iCity = 0;
			iState++;
			if(iState >= states.length) {
				return;
			}
		}
		createCookie("state", iState, 365);
		createCookie("city", iCity, 365);
		createCookie("school", iSchool, 365);
		var f = "getLatLng(" + iState +", " + iCity + ", " + iSchool +")";
		setTimeout(f, 5);
		return;
	}
	
	var school = schools[iSchool];
	var schoolName = search(school, "<b>", "</b>");
	if (schoolName==null || schoolName.length==0) {
		iSchool ++;
		if (iSchool>=schools.length) {
			iSchool = 0;
			iCity ++;
			if (iCity>=cities.length) {
				iCity = 0;
				iState++;
				if(iState >= states.length) {
					return;
				}
			}
		}
		createCookie("state", iState, 365);
		createCookie("city", iCity, 365);
		createCookie("school", iSchool, 365);
		setTimeout("getLatLng(" + iState +", " + iCity + ", " + iSchool +")", 5);
		return;
	}
	schoolName = schoolName[0];

	var schoolLocation = search(school, "Location: ", ';');
	if (schoolLocation==null || schoolLocation.length==0) {
		iSchool ++;
		if (iSchool>=schools.length) {
			iSchool = 0;
			iCity ++;
			if (iCity>=cities.length) {
				iCity = 0;
				iState++;
				if(iState >= states.length) {
					return;
				}
			}
		}
		createCookie("state", iState, 365);
		createCookie("city", iCity, 365);
		createCookie("school", iSchool, 365);
		setTimeout("getLatLng(" + iState +", " + iCity + ", " + iSchool +")", 5);
		return;
	}
	schoolLocation = schoolLocation[0];

	var fileSchool = "c:\\mydata\\www\\schools\\" + stateName + "\\" + cityName + "\\geo_" + schoolName.replace("/", "_") + ".txt";
	if (fso.fileExists(fileSchool)) 
	{
		window.status = iState + "-" + iCity + "-" + iSchool;
		try {
			var geo = ReadFromFile(fso, fileSchool).split("\n")[1];
			var lat = geo.split(",")[0].replace("\r", "").replace("\n", "");
			var lng = geo.split(",")[1].replace("\r", "").replace("\n", "");
			//AppendToFile(fso, "c:\\mydata\\www\\schools\\schools.xml", "<school><state>" + stateName + "</state><city>" + cityName + "</city><name>" + schoolName + "</name><address>" + schoolLocation + "</address><lat>" + lat + "</lat><lng>" + lng + "</lng></school>\r\n");
			var sql = "insert into `schools` ( `name`, city, state, address, lat, lng) values ('"+schoolName.replace("'", "")+"', '"+cityName+"', '"+stateName+"', '" + schoolLocation.replace("'", "") + "', "+lat+", "+lng+")";
			var url = "cmd=directsql&dbhost=localhost&dbuser=root&dbpwd=wordpass&dbname=us_schools&dbsql=" + sql;
			window.status = (getHttpData("GET", "http://localhost/php/db.php", url));
		} catch (e) {
			eCounter ++;
			window.status = "error "+ eCounter;
		}
	}
	{
		iSchool ++;
		if (iSchool>=schools.length) {
			iSchool = 0;
			iCity ++;
			if (iCity>=cities.length) {
				iCity = 0;
				iState++;
				if(iState >= states.length) {
					return;
				}
			}
		}
		createCookie("state", iState, 365);
		createCookie("city", iCity, 365);
		createCookie("school", iSchool, 365);
		setTimeout("getLatLng(" + iState +", " + iCity + ", " + iSchool +")", 5);
		return;
	}

	var address = schoolLocation + "," + cityName + "," + stateName;
	gGeocoder.getLatLng(address,
		function (point) {
			var waitTime = 5;
			try {
				if (!point) {
//						WriteToFile(fso, fileSchool, address);
					window.status = iState + "-" + iCity + "-" + iSchool + " addr:" + address + ", geo: XXXXXX";
					geoCounter++;
//						if (geoCounter > 10) {
//							waitTime = 3600000;
//							geoCounter = 0;
//						}
				}
				else {
					geoCounter = 0;
					WriteToFile(fso, fileSchool, address + "\n\r" + point.toUrlValue());
					window.status = iState + "-" + iCity + "-" + iSchool + " addr:" + address + ", geo:" + point.toUrlValue();
				}
			} catch (e) {
				window.status = iState + "-" + iCity + "-" + iSchool + " addr:" + address + ", exception:" + fileSchool;
				alert(schoolName);
				return;
			}

			iSchool ++;
			if (iSchool>=schools.length) {
				iSchool = 0;
				iCity ++;
				if (iCity>=cities.length) {
					iCity = 0;
					iState++;
					if(iState >= states.length) {
						return;
					}
				}
			}
			createCookie("state", iState, 365);
			createCookie("city", iCity, 365);
			createCookie("school", iSchool, 365);
			setTimeout("getLatLng(" + iState +", " + iCity + ", " + iSchool +")", waitTime);
		}
	);
}

function importSchools() {
	fso = new ActiveXObject("Scripting.FileSystemObject");

	//gStates = getSchoolList();

	gGeocoder = new GClientGeocoder();
	
	var iState = readCookie("state");
	var iCity = readCookie("city");
	var iSchool = readCookie("school");
//		if (iState !=null && iCity != null && iSchool != null) 
//			getLatLng(iState, iCity, iSchool);
//		else
		getLatLng(0, 0, 0);
}

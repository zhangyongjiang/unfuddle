var seqId = 0;

function getNextSeqId() {
    return seqId++;
}
Number.prototype.toRad = function () {
    return this * Math.PI / 180;
}
Number.prototype.toDeg = function () {
    return this * 180 / Math.PI;
}
Number.prototype.toBrng = function () {
    return (this.toDeg() + 360) % 360;
}

function geoMeterDistance(geo1, geo2) {
    return meterDistance(geo1.lat(), geo1.lng(), geo2.lat(), geo2.lng());
}

function geoMileDistance(geo1, geo2) {
    return meterDistance(geo1.lat(), geo1.lng(), geo2.lat(), geo2.lng()) / 1600;
}

function meterDistance(lat1, lon1, lat2, lon2) {
    var R = 6371000;
    var dLat = (lat2 - lat1).toRad();
    var dLon = (lon2 - lon1).toRad();
    var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1.toRad()) * Math.cos(lat2.toRad()) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    var d = R * c;
    return d;
}

function findValues(data, startTag, endTag) {
    var items = data.split(startTag);
    var values = new Array();
    for (var i = 0; i < items.length; i++) {
        var pos = items[i].indexOf(endTag);
        if (pos == -1) continue;
        values.push(items[i].substring(0, pos));
    }
    return values;
}

function search(str, prefix, postfix) {
    var pos = 0;
    var ret = new Array();
    while (true) {
        pos = str.indexOf(prefix, pos);
        if (pos == -1) break;
        var endPos = str.indexOf(postfix, pos + prefix.length);
        var s = str.substring(pos + prefix.length, endPos);
        ret.push(s);
        pos = endPos + postfix.length;
    }
    return ret;
}

function searchLine(str, pattern, prefix, postfix) {
    var lines = str.split("\n");
    var ret = new Array();
    for (var i = 0; i < lines.length; i++) {
        if (lines[i].indexOf(pattern) == -1) continue;
        if (prefix == null) {
            ret.push(lines[i]);
            continue;
        }
        var pos = lines[i].indexOf(prefix);
        var endPos = lines[i].indexOf(postfix, pos + prefix.length);
        var item = lines[i].substring(pos + prefix.length, endPos);
        ret.push(item);
    }
    return ret;
}

function getXmlString(node) {
    var s = "";
    if (node.nodeType == 1) {
        s += "<" + node.nodeName;
        for (var i = 0; i < node.attributes.length; i++)
        s += " " + node.attributes[i].nodeName + "=\"" + xmlEncode((node.attributes[i].nodeValue || "").toString()) + "\"";
        if (node.firstChild) {
            s += ">";
            for (var c = node.firstChild; c; c = c.nextSibling)
            s += getXmlString(c);
            s += "</" + node.nodeName + ">";
        } else
        s += "/>";
    } else if (node.nodeType == 3) s += xmlEncode(node.nodeValue);
    else if (node.nodeType == 4) s += "<![CDATA[" + node.nodeValue + "]]>";
    return s;
}

function getDocument(xml) {
    return GXml.parse(xml);
}

function xmlEncode(txt) {
    txt = txt.replace(/[&]/g, "&amp;");
    txt = txt.replace(/[\"]/g, "&quot;");
    txt = txt.replace(/[\']/g, "&apos;");
    txt = txt.replace(/[>]/g, "&gt;");
    txt = txt.replace(/[<]/g, "&lt;");
    return txt;
}

function getChild(node, childName) {
    for (var child = node.firstChild; child != null; child = child.nextSibling) {
        if (child.nodeName == childName) {
            return child;
        }
    }
}

function getChildElement(node) {
    for (var child = node.firstChild; child != null; child = child.nextSibling) {
        if (child.nodeType == 1) return child;
    }
}

function nextSibling(node) {
    var nodeName = node.nodeName;
    for (var s = node.nextSibling; s != null; s = s.nextSibling) {
        if (s.nodeName == nodeName) return s;
    }
}

function getHttpData(method, page, data) {
    var objHTTP = GXmlHttp.create();
    if (method == "POST") {
        objHTTP.open(method, page, false);
        objHTTP.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        objHTTP.send(data);
    } else {
        if (data != null) objHTTP.open(method, page + "?" + data, false);
        else
        objHTTP.open(method, page, false);
        objHTTP.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        objHTTP.send();
    }
    return objHTTP.responseText;
}

function createCookie(name, value, days) {
    if (days) {
        var date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        var expires = "; expires=" + date.toGMTString();
    } else
    var expires = "";
    document.cookie = name + "=" + value + expires + "; path=/";
}

function readCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
    }
    return null;
}

function eraseCookie(name) {
    createCookie(name, "", -1);
}

function getUTCTime() {
    var d = new Date();
    var offset = d.getTimezoneOffset();
    return d.getTime() + offset * 60 * 1000 - 37 * 365 * 24 * 3600 * 1000;
}

function getId() {
    return getUTCTime();
}

function getMapTypeFromChar(c) {
    if (c == 'h') return G_HYBRID_MAP;
    else if (c == 'm') return G_NORMAL_MAP;
    else if (c == 'k') return G_SATELLITE_MAP;
}

function Property() {
    this.keys = new Array();
}
Property.prototype.set = function (key, value) {
    this.keys[key] = value;
}
Property.prototype.get = function (key, def) {
    var value = this.keys[key];
    if (value == null) value = def;
    return value;
}
Property.prototype.toString = function () {
    var str = "";
    for (var key in this.keys) {
        if (key != "" && key != "remove" && key != "indexOf" && this.keys[key] != null) str += key + "=" + this.keys[key] + "^";
    }
    return str;
}
Property.prototype.fromString = function (str) {
    var keyValues = str.split("^");
    for (var i = 0; i < keyValues.length; i++) {
        var key = keyValues[i].split("=")[0];
        var value = keyValues[i].split("=")[1];
        this.set(key, value);
    }
}

function I18NDesc() {
    this.descs = new Array();
}
I18NDesc.prototype.put = function (lang, desc) {
    this.descs[lang] = desc;
}
I18NDesc.prototype.get = function (lang) {
    return this.descs[lang];
}
I18NDesc.prototype.getAll = function () {
    return this.descs;
}

function Photo() {
    this.id = null;
    this.creator = null;
    this.time = null;
}
var baseIcons = new Array();

function getSmallIcon(focused) {
    var color = "red";
    if (baseIcons[color] != null) return new GIcon(baseIcons[color]);
    var geourlIcon = new GIcon();
    //geourlIcon.image = "img/mm_20_" + color + ".png";
    geourlIcon.image = "img/trans_48x48.png";
    //geourlIcon.shadow = "img/mm_20_shadow.png";
    geourlIcon.iconSize = new GSize(50, 43);
    //geourlIcon.shadowSize = new GSize(22, 20);
    geourlIcon.iconAnchor = new GPoint(25, 21);
    geourlIcon.infoWindowAnchor = new GPoint(0, 18);
    baseIcon = geourlIcon;
    baseIcons[color] = baseIcon;
    return baseIcon;
}
var redStarIcon = null;

function getRedStarIcon() {
    if (redStarIcon != null) return new GIcon(redStarIcon);
    var icon = new GIcon();
    icon.image = "img/red_star.png";
    icon.iconSize = new GSize(13, 13);
    icon.iconAnchor = new GPoint(6, 6);
    icon.infoWindowAnchor = new GPoint(5, 1);
    redStarIcon = icon;
    return redStarIcon;
}

function getMap() {
    return gView.map;
}

function MapObject(parentPlace) {
    this.parentPlace = parentPlace;
    this.map = parentPlace.map;
    this.loop = false;
    this.title = new I18NDesc();
    this.descs = new Array();
    this.photos = new Array();
    this.infoMarkers = new Array();
    this.centerMarkers = new Array();
    this.polygone = null;
    this.iCurrMkr = -1;
    this.focused = true;
    this.seqId = getNextSeqId();
}
MapObject.prototype.getSeqId = function () {
    return this.seqId;
}
MapObject.prototype.setIsFocused = function (focused) {
    if (this.focused == focused) return;
    this.focused = focused;
    for (var i = 0; i < this.infoMarkers.length; i++) {
        this.infoMarkers[i].setIsFocused(focused);
    }
    if (this.polygon != null) {
        this.map.removeOverlay(this.polygon);
        this.polygon = null;
    }
    this.drawPolygon();
}
MapObject.prototype.highlight = function (fill) {
    this.drawPolygon(fill);
    if (false && !fill) {
        if (!this.focused && this.mode == "view") {
            this.map.removeOverlay(this.polygon);
            this.polygon = null;
        }
    }
}
MapObject.prototype.getInfoMarkers = function () {
    return this.infoMarkers;
}
MapObject.prototype.setLoop = function (loop) {
    this.loop = loop;
}
MapObject.prototype.getPoints = function () {
    var points = new Array();
    for (var i = 0; i < this.infoMarkers.length; i++)
    points.push(this.infoMarkers[i].getPoint());
    if (this.loop && this.infoMarkers.length > 2) points.push(this.infoMarkers[0].getPoint());
    return points;
}
MapObject.prototype.getCurrentInfoMarker = function () {
    return this.infoMarkers[this.iCurrMkr];
}
MapObject.prototype.toXml = function () {
    var xml = "<route loop=\"" + this.loop + "\">\n";
    for (var i = 0; i < this.infoMarkers.length; i++) {
        xml += this.infoMarkers[i].toXml();
    }
    xml += "</route>\n";
    return String(xml);
}
MapObject.prototype.fromNode = function (node) {
    this.loop = node.getAttribute("loop");
    if (this.loop == "false") this.loop = false;
    if (this.loop == "true") this.loop = true;
    for (var infoMarker = getChildElement(node); infoMarker != null; infoMarker = nextSibling(infoMarker)) {
        var im = InfoMarkerFromNode(this, infoMarker, {
            icon: getSmallIcon(this.focused)
        }, this.infoMarkers.length);
        im.draw();
        this.infoMarkers.push(im);
    }
    if (this.infoMarkers.length > 0 && this.iCurrMkr == -1) this.iCurrMkr = 0;
    this.drawPolygon();
}
MapObject.prototype.toUrlValue = function () {
    var url = "[" + (this.loop ? 1 : 0) + "]";
    for (var i = 0; i < this.infoMarkers.length; i++) {
        url += "[" + this.infoMarkers[i].toUrlValue() + "]";
    }
    return String(url);
}
MapObject.prototype.fromUrlValue = function (urlValue) {
    var infoMarkers = findValues(urlValue, "[", "]");
    this.loop = (infoMarkers[0] == "1");
    var thisObj = this;
    for (var i = 1; i < infoMarkers.length; i++) {
        var im = InfoMarkerFromUrlValue(thisObj, infoMarkers[i], {
            icon: getSmallIcon(this.focused)
        }, i);
        this.infoMarkers.push(im);
        im.draw();
        this.addMarkerListener(im);
    }
    if (this.infoMarkers.length > 0) this.iCurrMkr = 0;
}
MapObject.prototype.setMode = function (mode) {
    if (this.mode != mode) {
        this.mode = mode;
        for (var i = 0; i < this.infoMarkers.length; i++) {
            this.infoMarkers[i].setMode(mode);
        }
        if (mode == "view") {
            for (var i = 0; i < this.centerMarkers.length; i++) {
                this.map.removeOverlay(this.centerMarkers[i]);
            }
            this.centerMarkers = new Array();
        } else {
            for (var i = 1; i < this.infoMarkers.length; i++) {
                this.addCenterPointMarker(i - 1, i);
            }
        }
    }
}
MapObject.prototype.deleteCurrentPoint = function () {
    if (this.iCurrMkr != -1) {
        this.infoMarkers[this.iCurrMkr].removeOverlay();
        if (this.iCurrMkr < this.infoMarkers.length - 1) {
            if (this.centerMarkers[this.iCurrMkr] != null) this.map.removeOverlay(this.centerMarkers[this.iCurrMkr]);
            this.centerMarkers.splice(this.iCurrMkr, 1);
        }
        if (this.iCurrMkr != 0) {
            if (this.centerMarkers[this.iCurrMkr - 1] != null) this.map.removeOverlay(this.centerMarkers[this.iCurrMkr - 1]);
            this.centerMarkers.splice(this.iCurrMkr - 1, 1);
        }
        this.infoMarkers.splice(this.iCurrMkr, 1);
        this.drawPolygon();
        if (this.iCurrMkr >= this.infoMarkers.length) {
            this.iCurrMkr = this.infoMarkers.length - 1;
        }
        if (this.infoMarkers.length - this.centerMarkers.length > 1) {
            this.addCenterPointMarker(this.iCurrMkr - 1, this.iCurrMkr);
        }
        if (this.iCurrMkr == 0) {
            var latLng = this.infoMarkers[0].getPoint();
            this.infoMarkers[0].removeOverlay();
            this.infoMarkers.splice(0, 1);
            this.addMarker(latLng, 0);
        }
        this.onChange();
    }
}
MapObject.prototype.contains = function (latLng) {
    for (var i = 0; i < this.infoMarkers.length; i++) {
        if (this.infoMarkers[i].getPoint().equals(latLng)) {
            return true;
        }
    }
    for (var i = 0; i < this.centerMarkers.length; i++) {
        if (this.centerMarkers[i].getPoint().equals(latLng)) {
            return true;
        }
    }
    return false;
}
MapObject.prototype.select = function (latLng) {
    for (var i = 0; i < this.infoMarkers.length; i++) {
        if (this.infoMarkers[i].getPoint().equals(latLng)) {
            this.iCurrMkr = i;
            this.infoMarkers[this.iCurrMkr].infoOverlay.div_.style.display = 'block';
            return true;
        }
    }
    return false;
}
MapObject.prototype.unselect = function () {
    if(this.iCurrMkr!=-1) {
        this.infoMarkers[this.iCurrMkr].infoOverlay.div_.style.display = 'none';
    }
}
MapObject.prototype.repositionCenterMarker = function (pos) {
    if (pos != 0) {
        var indexa = pos - 1;
        var indexb = pos;
        var centerPoint = new GLatLng((this.infoMarkers[indexa].getPoint().lat() + this.infoMarkers[indexb].getPoint().lat()) / 2, (this.infoMarkers[indexa].getPoint().lng() + this.infoMarkers[indexb].getPoint().lng()) / 2);
        this.centerMarkers[pos - 1].setPoint(centerPoint);
    }
    if (pos != this.infoMarkers.length - 1) {
        var indexa = pos;
        var indexb = pos + 1;
        var centerPoint = new GLatLng((this.infoMarkers[indexa].getPoint().lat() + this.infoMarkers[indexb].getPoint().lat()) / 2, (this.infoMarkers[indexa].getPoint().lng() + this.infoMarkers[indexb].getPoint().lng()) / 2);
        this.centerMarkers[pos].setPoint(centerPoint);
    }
}
MapObject.prototype.addBreakMakerListener = function (centerPointMkr) {
    var thisMObj = this;
    GEvent.addListener(centerPointMkr, "click", function () {
        thisMObj.parentPlace.select(centerPointMkr.getPoint());
    });
    GEvent.addListener(centerPointMkr, "dragstart", function () {
        thisMObj.parentPlace.select(centerPointMkr.getPoint());
        for (var j = 0; j < thisMObj.centerMarkers.length; j++) {
            if (thisMObj.centerMarkers[j].getPoint().equals(centerPointMkr.getPoint())) {
                thisMObj.centerMarkers.splice(j, 1);
                thisMObj.dragMarkerIndex = j + 1;
                thisMObj.addPoint(centerPointMkr.getPoint(), thisMObj.dragMarkerIndex);
                thisMObj.onChange();
                break;
            }
        }
    });
    GEvent.addListener(centerPointMkr, "drag", function () {
        thisMObj.parentPlace.onChange();
        thisMObj.infoMarkers[thisMObj.dragMarkerIndex].setPoint(centerPointMkr.getPoint());
        thisMObj.repositionCenterMarker(thisMObj.dragMarkerIndex);
        thisMObj.drawPolygon();
    });
    GEvent.addListener(centerPointMkr, "dragend", function () {
        thisMObj.map.removeOverlay(centerPointMkr);
        thisMObj.onChange();
        thisMObj.parentPlace.onChange();
    });
}
MapObject.prototype.removeCenterMarkers = function () {
    for (var i = 0; i < this.centerMarkers.length; i++) {
        this.map.removeOverlay(this.centerMarkers[i]);
    }
    this.centerMarkers = new Array();
}
MapObject.prototype.onChange = function () {
    for (var i = 0; i < this.infoMarkers.length; i++) {
        this.infoMarkers[i].setIndex(i);
    }
}

function getStringLenInBytes(str) {
    var size = 0;
    for (var i = 0; str != null && i < str.length; i++) {
        var c = str.charAt(i);
        if (c > 0x80) size += 2;
        else
        size++;
    }
    return size;
}
MapObject.prototype.addMarkerListener = function (infoMarker) {
    var mkr = infoMarker.marker;
    var thisMObj = this;
    GEvent.addListener(mkr, "mouseover", function () {
        //thisMObj.highlight(true);
        //infoMarker.infoOverlay.div_.style.display = 'block';
    });
    GEvent.addListener(mkr, "mouseout", function () {
        //thisMObj.highlight(false);
        //infoMarker.infoOverlay.div_.style.display = 'none';
    });
    GEvent.addListener(mkr, "click", function () {
        var point = mkr.getPoint();
        var lat = point.lat();
        var lng = point.lng();
        var info = "<div style='float:right;margin-top:-12px;margin-right:-10px;zindex:999;'>" 
                    + lat + "," + lng 
                    + "<input type='button' value='close' onclick='gView.map.closeInfoWindow()'/></div>";
        //gView.map.openInfoWindowHtml(mkr.getPoint(), info);

        if(infoMarker.infoOverlay.div_.style.display == 'block') {
            infoMarker.infoOverlay.div_.style.display = 'none';
        }
        else {
            infoMarker.infoOverlay.div_.style.display = 'block';
            gView.unselect();
            gView.select(point);
        }
        return;
    });
    GEvent.addListener(mkr, "dblclick", function () {
        //gView.map.openInfoWindow(mkr.getPoint(), document.createTextNode("Hello, world"));
        gView.select(mkr.getPoint());
        return;

        if (gView.mode == 'edit') {
            gView.setMode('view');
        } else {
            gView.select(mkr.getPoint());
            gView.setMode("view");
            gView.setMode("edit");
            gView.getCurrentPlace().onChange();
            gView.onChange();
        }
    });
    if (this.mode == "view") return;
    GEvent.addListener(mkr, "dragstart", function () {
        thisMObj.parentPlace.select(mkr.getPoint());
        for (var j = 0; j < thisMObj.infoMarkers.length; j++) {
            if (thisMObj.infoMarkers[j].getPoint().equals(mkr.getPoint())) {
                thisMObj.dragMarkerIndex = j;
                break;
            }
        }
    });
    GEvent.addListener(mkr, "drag", function () {
        thisMObj.onChange();
        thisMObj.parentPlace.onChange();
        gView.onChange();
        thisMObj.infoMarkers[thisMObj.dragMarkerIndex].setPoint(mkr.getPoint());
        thisMObj.drawPolygon();
        if (thisMObj.dragMarkerIndex != 0) {
            var prevPoint = thisMObj.infoMarkers[thisMObj.dragMarkerIndex - 1].getPoint();
            var centerPoint = new GLatLng((mkr.getPoint().lat() + prevPoint.lat()) / 2, (mkr.getPoint().lng() + prevPoint.lng()) / 2);
            thisMObj.centerMarkers[thisMObj.dragMarkerIndex - 1].setPoint(centerPoint);
        }
        if (thisMObj.dragMarkerIndex != (thisMObj.infoMarkers.length - 1)) {
            var nextPoint = thisMObj.infoMarkers[thisMObj.dragMarkerIndex + 1].getPoint();
            var centerPoint = new GLatLng((mkr.getPoint().lat() + nextPoint.lat()) / 2, (mkr.getPoint().lng() + nextPoint.lng()) / 2);
            thisMObj.centerMarkers[thisMObj.dragMarkerIndex].setPoint(centerPoint);
        }
    });
    GEvent.addListener(mkr, "dragend", function () {
        thisMObj.parentPlace.modified = true;
        thisMObj.onChange();
        thisMObj.parentPlace.onChange();
    });
}
MapObject.prototype.draw = function () {
    for (var i = 0; i < this.infoMarkers.length; i++) {
        this.infoMarkers[i].removeOverlay();
    }
    for (var i = 0; i < this.centerMarkers.length; i++) {
        this.map.removeOverlay(this.centerMarkers[i]);
    }
    this.centerMarkers = new Array();
    for (var i = 0; i < this.infoMarkers.length; i++) {
        var latLng = this.infoMarkers[i].getPoint();
        if (i > 0 && this.mode == 'edit') {
            this.addCenterPointMarker(i - 1, i);
        }
        var infoMkr = this.addMarker(latLng, i);
        if (infoMkr != null) this.infoMarkers.splice(i, 1);
    }
    this.drawPolygon();
}
MapObject.prototype.addMarker = function (latLng, pos) {
    var infoMkr = null;
    var thisObj = this;
    if (pos == 0) {
        if (this.mode == "edit") {
            infoMkr = new InfoMarker(thisObj, latLng, {
                draggable: (this.mode != 'view')
            }, pos);
            infoMkr.setMode("edit");
        } else if (this.focused) {
            infoMkr = new InfoMarker(thisObj, latLng, {
                icon: getSmallIcon(this.focused),
                draggable: (this.mode != 'view')
            }, pos);
            infoMkr.draw();
        }
    } else {
        if (this.mode != 'view') {
            infoMkr = new InfoMarker(thisObj, latLng, {
                icon: getSmallIcon(this.focused),
                draggable: (this.mode != 'view')
            }, pos);
            infoMkr.setMode("edit");
        }
    }
    if (infoMkr != null) {
        this.addMarkerListener(infoMkr);
        this.infoMarkers.splice(pos, 0, infoMkr);
    }
    return infoMkr;
}
MapObject.prototype.isCurrentMObj = function () {
    var curr = this.parentPlace.getCurrentMObj();
    if (curr == null) return false;
    return this.seqId == curr.getSeqId();
}
MapObject.prototype.isEmpty = function () {
    return this.infoMarkers.length == 0;
}
MapObject.prototype.drawPolygon = function (fill) {
    if (this.polygon != null) {
        this.map.removeOverlay(this.polygon);
        this.polygon = null;
    }
    var color = this.focused ? (this.isCurrentMObj() ? '#FF0000' : '#FF00FF') : '#ff0000';
    var width = this.focused ? 4 : 2;
    var points = this.getPoints();
    if (points.length < 2) return;
    if (fill != null && fill) {
        this.polygon = new GPolygon(points, color, width, 0.7, '#0000FF', 0.1);
    } else {
        this.polygon = new GPolyline(points, color, width, 0.7);
    }
    this.map.addOverlay(this.polygon);
}
MapObject.prototype.addPoint = function (latLng, pos, title) {
    if (pos == null) {
        pos = this.infoMarkers.length;
    }
    this.addMarker(latLng, pos);
    this.iCurrMkr = pos;
    if (pos != 0) {
        this.addCenterPointMarker(pos - 1, pos);
    }
    if (pos != this.infoMarkers.length - 1) {
        this.addCenterPointMarker(pos, pos + 1);
    }
    if (title != null) this.infoMarkers[this.iCurrMkr].setTitle(title);
    this.drawPolygon();
    this.parentPlace.modified = true;
}
MapObject.prototype.addCenterPointMarker = function (indexa, indexb) {
    if (indexa < 0 || indexb < 0 || this.mode != 'edit') return;
    var centerPoint = new GLatLng((this.infoMarkers[indexa].getPoint().lat() + this.infoMarkers[indexb].getPoint().lat()) / 2, (this.infoMarkers[indexa].getPoint().lng() + this.infoMarkers[indexb].getPoint().lng()) / 2);
    var centerPointMkr = new GMarker(centerPoint, {
        icon: getRedStarIcon(),
        draggable: true
    });
    this.centerMarkers.splice(indexa, 0, centerPointMkr);
    this.map.addOverlay(centerPointMkr);
    this.addBreakMakerListener(centerPointMkr);
}

function InfoOverlay(latLng, innerHTML) {
    this.latLng_ = latLng;
    this.innerHTML_ = innerHTML;
    this.offset = new GSize(-80, 20);
}
InfoOverlay.prototype = new GOverlay();
InfoOverlay.prototype.getPoint = function () {
    return this.latLng_;
}
InfoOverlay.prototype.setPoint = function (latLng) {
    this.latLng_ = latLng;
    this.redraw(true);
}
InfoOverlay.prototype.initialize = function (map) {
    var div = document.createElement("span");
    div.innerHTML = this.innerHTML_;
    div.style.position = "absolute";
    div.style.textAlign = "center";
    div.style.overflow = "hidden";
    //div.style.opacity = "0.8";
    //div.style.filter = "alpha(opacity=80)"
    div.style.backgroundColor = "#ffccff";
    div.style.width = "160px";
    div.style.display = "none";
    div.style.padding = "4px";
    div.style.fontFamily = "Courier";
    div.style.fontSize = "12px";
    //var pan = map.getPane(G_MAP_FLOAT_SHADOW_PANE);
    var pan = map.getPane(G_MAP_OVERLAY_LAYER_PANE);
    pan.style.border = "0";
    pan.appendChild(div);
    this.map_ = map;
    this.div_ = div;
}
InfoOverlay.prototype.remove = function () {
    this.div_.parentNode.removeChild(this.div_);
}
InfoOverlay.prototype.copy = function () {
    return new InfoOverlay(this.latLng_);
}
InfoOverlay.prototype.getDiv = function () {
    return this.div_;
}
InfoOverlay.prototype.setOffset = function (offset) {
    this.offset = offset;
    this.redraw(force);
}
InfoOverlay.prototype.redraw = function (force) {
    var p = this.map_.fromLatLngToDivPixel(this.latLng_);
    this.div_.style.left = (p.x + this.offset.width) + "px";
    this.div_.style.top = (p.y + this.offset.height) + "px";
}
InfoOverlay.prototype.setInnerHTML = function (innerHTML) {
    this.innerHTML_ = innerHTML;
    if (innerHTML == null) innerHTML = "";
    var title = innerHTML;
    if (innerHTML.length == 0) {
        this.div_.style.border = "0";
    } else {
        this.div_.style.border = "1px solid black";
        //this.div_.style.height = "20px";
    }
    //var width = 80;
    //if (innerHTML.length < 8) width = innerHTML.length * 8 + 4;
    //this.div_.style.width = width + "px";
    this.div_.innerHTML = "<span title='" + title + "'>" + innerHTML + "</span>";
}

function CenterCross() {
    this.offset = new GSize(-11, -23);
}
CenterCross.prototype = new InfoOverlay();
CenterCross.prototype.initialize = function (map) {
    this.latLng_ = map.getCenter();
    this.innerHTML_ = "+";
    var div = document.createElement("span");
    div.innerHTML = this.innerHTML_;
    div.style.position = "absolute";
    div.style.textAlign = "left";
    div.style.fontSize = "36px";
    div.style.color = "red";
    div.style.margin = "0";
    div.style.padding = "0";
    map.getPane(G_MAP_MAP_PANE).appendChild(div);
    this.map_ = map;
    this.div_ = div;
}

function InfoMarker(container, latLng, mkrOpts, index, title, description, uid, gender) {
    this.uid = uid;
    this.container = container;
    this.map = container.map;
    this.latLng = latLng;
    this.mkrOpts = mkrOpts;
    this.index = index;
    this.title = title;
    this.description = description;
    this.focused = true;
    this.mode = "view";
    this.firstOfPlace = false;
    this.gender = gender;
    if(gender == 'Man') {
        this.backgroundImage = "url('img/heart_blue_50_43.png')";
    }
    else {
        this.backgroundImage = "url('img/heart_pink_50_43.png')";
    }
}

function InfoMarkerFromJson(container, jobj, mkrOpts, index) {
    var lat = jobj.point.geo.lat;
    var lng = jobj.point.geo.lng;
    var infos = jobj.localized_info;
    if (typeof infos.info == "array") var title = infos.info[0].caption;
    else
    var title = infos.info.caption;
    var description = infos.info.description;
    var latLng = new GLatLng(lat, lng);
    return new InfoMarker(container, latLng, mkrOpts, index, title, description);
}

function InfoMarkerFromNode(container, node, mkrOpts, index) {
    var geo = getChild(node, "geo");
    var lat = getChild(geo, "lat").firstChild.nodeValue;
    var lng = getChild(geo, "lng").firstChild.nodeValue;
    var latLng = new GLatLng(Number(lat), Number(lng));
    var localizedInfo = getChild(node, "localized_info");
    var info = getChild(localizedInfo, "info");
    var caption = getChild(info, "caption");
    var gender = getChild(info, "gender");
    var description = getChild(info, "description");
    var uid = getChild(info, "uid");
    caption = caption.firstChild == null ? "" : caption.firstChild.nodeValue;
    description = description.firstChild == null ? "" : description.firstChild.nodeValue;
    gender = gender.firstChild == null ? "" : gender.firstChild.nodeValue;
    uid = uid.firstChild == null ? "" : uid.firstChild.nodeValue;
    return new InfoMarker(container, latLng, mkrOpts, index, caption, description, uid, gender);
}

function InfoMarkerFromUrlValue(container, urlValue, mkrOpts, index) {
    var data = findValues(urlValue, "(", ")");
    var title = data[1];
    var latLng = data[0];
    var lat = latLng.split(",")[0];
    var lng = latLng.split(",")[1];
    var latLng = new GLatLng(lat, lng);
    var description = "";
    return new InfoMarker(container, latLng, mkrOpts, index, title, description);
}
InfoMarker.prototype.removeOverlay = function () {
    if (this.marker != null) {
        this.map.removeOverlay(this.marker);
        this.marker = null;
    }
    if (this.infoOverlay != null) {
        this.map.removeOverlay(this.infoOverlay);
        this.infoOverlay = null;
    }
}
InfoMarker.prototype.setPoint = function (latLng) {
    this.latLng = latLng;
    if (!latLng.equals(this.marker.getPoint())) {
        this.marker.setPoint(latLng);
    }
    if (!latLng.equals(this.infoOverlay.getPoint())) this.infoOverlay.setPoint(latLng);
}
InfoMarker.prototype.getPoint = function () {
    return this.latLng;
}
InfoMarker.prototype.toXml = function () {
    var xml = "<point>";
    xml += "<geo><lat>" + this.latLng.lat() + "</lat><lng>" + this.latLng.lng() + "</lng></geo>\n";
    xml += "<localized_info>\n";
    xml += "<info la=\"en\">\n";
    xml += "<caption>" + xmlEncode(this.title == null ? "" : this.title) + "</caption>\n"
    xml += "<description></description>\n";
    xml += "</info>\n";
    xml += "</localized_info>\n";
    xml += "</point>\n";
    return String(xml);
}
InfoMarker.prototype.toUrlValue = function () {
    return String("(" + this.latLng.toUrlValue() + ")(" + (this.title == null ? '' : this.title) + ")");
}
InfoMarker.prototype.fromUrlValue = function (urlValue) {
    var data = findValues(urlValue, "(", ")");
    this.title = data[1];
    var latLng = data[0];
    var lat = Number(latLng.split(",")[0]);
    var lng = Number(latLng.split(",")[1]);
    this.latLng = new GLatLng(lat, lng);
}
InfoMarker.prototype.draw = function () {
    if (this.mode == "view") {
        if (this.index == 0) {
            if (this.marker) this.map.removeOverlay(this.marker);
            this.marker = new LabeledMarker(this.latLng, {
                icon: getSmallIcon(this.focused),
                draggable: false,
                clickable: true,
                labelText: this.title,
                labelClass: 'labeledMarker',
                backgroundImage: this.backgroundImage
            });
            this.map.addOverlay(this.marker);
            if (this.infoOverlay) this.map.removeOverlay(this.infoOverlay);
            this.infoOverlay = new InfoOverlay(this.latLng, this.getInnerHTML());
            this.map.addOverlay(this.infoOverlay);
            this.infoOverlay.setInnerHTML(this.getInnerHTML());
        } else {
            if (this.marker) {
                this.map.removeOverlay(this.marker);
                this.marker = null;
            }
            if (this.title != null && this.title.length > 0) {
                this.marker = new GMarker(this.latLng, {
                    icon: getSmallIcon(this.focused),
                    draggable: false
                });
                this.map.addOverlay(this.marker);
            }
            if (this.infoOverlay) {
                this.map.removeOverlay(this.infoOverlay);
                this.infoOverlay = null;
            }
            if (this.infoOverlay == null) {
                this.infoOverlay = new InfoOverlay(this.latLng, this.getInnerHTML());
                this.map.addOverlay(this.infoOverlay);
            }
            this.infoOverlay.setInnerHTML(this.getInnerHTML());
        }
    } else {
        if (this.index == 0) {
            if (this.marker) {
                this.map.removeOverlay(this.marker);
            }
            this.marker = new GMarker(this.latLng, {
                draggable: true
            });
            this.map.addOverlay(this.marker);
            if (this.infoOverlay != null) this.map.removeOverlay(this.infoOverlay);
            this.infoOverlay = new InfoOverlay(this.latLng, this.getInnerHTML());
            this.map.addOverlay(this.infoOverlay);
            this.infoOverlay.setInnerHTML(this.getInnerHTML());
        } else {
            if (this.marker) {
                this.map.removeOverlay(this.marker);
            }
            this.marker = new GMarker(this.latLng, {
                icon: getSmallIcon(this.focused),
                draggable: true
            });
            this.map.addOverlay(this.marker);
            if (this.infoOverlay != null) this.map.removeOverlay(this.infoOverlay);
            this.infoOverlay = new InfoOverlay(this.latLng, this.getInnerHTML());
            this.map.addOverlay(this.infoOverlay);
            this.infoOverlay.setInnerHTML(this.getInnerHTML());
        }
    }
    if (this.marker != null) this.container.addMarkerListener(this);
}
InfoMarker.prototype.setMode = function (mode) {
    if (this.mode == null || this.mode != mode) {
        this.mode = mode;
        this.draw();
    }
}
InfoMarker.prototype.setIsFocused = function (focused) {
    if (this.focused == focused) return;
    this.focused = focused;
    if (this.mode == "view") {
        if ((this.index == 0 || (this.focused || this.firstOfPlace)) || (this.title != null && this.title.length > 0)) {
            if (this.marker) {}
        } else {
            if (this.marker) {
                this.map.removeOverlay(this.marker);
            }
        }
    } else {}
}
InfoMarker.prototype.setIndex = function (index) {
    this.index = index;
    this.setInnerHTML(this.getInnerHTML());
}
InfoMarker.prototype.setTitle = function (title) {
    this.title = title;
    this.setInnerHTML(this.getInnerHTML());
}
InfoMarker.prototype.getInnerHTML = function () {
    if (this.mode == "view") return this.description == null ? "" : (this.description);
    var html = "";
    if (this.index != null) {
        html += this.index;
    }
    if (this.index != null && this.title != null && this.title != "") {
        html += ": ";
    }
    if (this.title != null) {
        html += this.title;
    }
    return html;
}
InfoMarker.prototype.setInnerHTML = function (innerHTML) {
    if (innerHTML == null) innerHTML = this.getInnerHTML();
    return this.infoOverlay.setInnerHTML(innerHTML);
}

function Place(map) {
    this.id = null;
    this.mobjs = new Array();
    this.iCurrMObj = -1;
    this.map = map;
    this.focused = true;
    this.seqId = getNextSeqId();
    this.modified = false;
}
Place.prototype.setId = function (id) {
    this.id = id;
}
Place.prototype.getId = function () {
    return this.id;
}
Place.prototype.getBounds = function () {
    var points = new Array();
    for (var i = 0; i < this.mobjs.length; i++) {
        points.concat(this.mobjs[i].getPoints());
    }
    return new GPolygon(points).getBounds();
}
Place.prototype.getFirstPointsOfMObjs = function () {
    var points = new Array();
    for (var i = 0; i < this.mobjs.length; i++) {
        var mobjPoints = this.mobjs[i].getPoints();
        if (mobjPoints.length > 0) points.push(mobjPoints[0]);
    }
    return points;
}
Place.prototype.setIsFocused = function (focused) {
    if (this.focused == focused) return;
    for (var i = 0; i < this.mobjs.length; i++) {
        this.mobjs[i].setIsFocused(focused);
    }
    this.focused = focused;
}
Place.prototype.highlight = function (fill) {
    for (var i = 0; i < this.mobjs.length; i++) {
        this.mobjs[i].highlight(fill);
    }
}
Place.prototype.toXml = function () {
    var xml = "<place id=\"" + (this.id == null ? "" : this.id) + "\">\n";
    xml += "<routes>\n";
    for (var i = 0; i < this.mobjs.length; i++) {
        xml += this.mobjs[i].toXml();
    }
    xml += "</routes>\n";
    xml += "</place>";
    return String(xml);
}
Place.prototype.fromNode = function (node) {
    this.id = node.getAttribute("id");
    var routes = getChild(node, "routes");
    for (var child = getChildElement(routes); child != null; child = nextSibling(child)) {
        var mapObject = new MapObject(this);
        mapObject.fromNode(child);
        this.mobjs.push(mapObject);
    }
    if (this.mobjs.length > 0 && this.iCurrMObj == -1) this.iCurrMObj = 0;
    this.setMode("view");
    this.setIsFocused(false);
}
Place.prototype.toUrlValue = function () {
    if (this.id) return this.id;
    var url = "";
    for (var i = 0; i < this.mobjs.length; i++) {
        url += "{" + this.mobjs[i].toUrlValue() + "}";
    }
    return String(url);
}
Place.prototype.getMObjIndexBySeqId = function (seqId) {
    for (var i = 0; i < this.mobjs.length; i++) {
        if (this.mobjs[i].getSeqId() == seqId) return i;
    }
    return -1;
}
Place.prototype.getMObjSize = function () {
    return this.mobjs.length;;
}
Place.prototype.fromId = function (id) {
    var xml = getHttpData("GET", "/php/Gogomapper.php5", "cmd=get&objtype=place&id=" + id);
    try {
        var d = getDocument(xml);
        var placeNode = d.documentElement;
        this.fromNode(placeNode);
    } catch (e) {}
}
Place.prototype.fromUrlValue = function (urlValue) {
    if (urlValue.charAt(0) != '{') {
        this.fromId(urlValue);
        return;
    }
    var mobjs = findValues(urlValue, "{", "}");
    var thisObj = this;
    for (var i = 0; i < mobjs.length; i++) {
        this.addMObj();
        var infoMarkers = findValues(mobjs[i], "[", "]");
        for (var j = 1; j < infoMarkers.length; j++) {
            var data = findValues(infoMarkers[j], "(", ")");
            var lat = Number(data[0].split(",")[0]);
            var lng = Number(data[0].split(",")[1]);
            var title = data[1];
            this.addPoint(new GLatLng(lat, lng), null, title);
        }
        var loop = infoMarkers[0] == '1';
        this.getCurrentMObj().setLoop(loop);
    }
    if (this.mobjs.length > 0) this.iCurrMObj = 0;
}
Place.prototype.getMObjs = function () {
    return this.mobjs;
}
Place.prototype.setMode = function (mode) {
    if (this.mode != mode) {
        this.mode = mode;
        for (var i = 0; i < this.mobjs.length; i++) {
            this.mobjs[i].setMode("view");
        }
        if (this.mode != "view") {
            if (this.iCurrMObj != -1) {
                this.mobjs[this.iCurrMObj].setMode(this.mode);
            }
        }
    }
}
Place.prototype.select = function (latLng) {
    for (var i = 0; i < this.mobjs.length; i++) {
        if (this.mobjs[i].select(latLng)) {
            if (this.iCurrMObj != i) {
                this.onChange();
                this.iCurrMObj = i;
            }
            return true;
        }
    }
    return false;
}
Place.prototype.unselect = function () {
    if(this.iCurrMObj != -1) {
        this.mobjs[this.iCurrMObj].unselect();
    }
}
Place.prototype.draw = function () {
    for (var i = 0; i < this.mobjs.length; i++) {
        this.mobjs[i].draw();
    }
}
Place.prototype.contains = function (latLng) {
    for (var i = 0; i < this.mobjs.length; i++) {
        if (this.mobjs[i].contains(latLng)) {
            return true;
        }
    }
    return false;
}
Place.prototype.getCurrentMObj = function () {
    return this.mobjs[this.iCurrMObj];
}
Place.prototype.getCurrentMObjIndex = function () {
    return this.iCurrMObj;
}
Place.prototype.getPrevMObj = function () {
    return this.mobjs[this.iCurrMObj - 1];
}
Place.prototype.getNextMObj = function () {
    return this.mobjs[this.iCurrMObj + 1];
}
Place.prototype.deleteCurrentMObj = function () {
    while (this.mobjs[this.iCurrMObj].getPoints().length > 0)
    this.mobjs[this.iCurrMObj].deleteCurrentPoint();
    this.mobjs.splice(this.iCurrMObj, 1);
    if (this.iCurrMObj >= this.mobjs.length) this.iCurrMObj = this.mobjs.length - 1;
    if (this.iCurrMObj == 0) {
        if (this.mobjs[this.iCurrMObj].getInfoMarkers().length > 0) {
            this.mobjs[this.iCurrMObj].getInfoMarkers()[0].firstOfPlace = true;
        }
    }
    this.onChange();
    this.modified = true;
}
Place.prototype.deleteCurrentPoint = function () {
    this.mobjs[this.iCurrMObj].deleteCurrentPoint();
    if (this.iCurrMObj == 0) {
        if (this.mobjs[this.iCurrMObj].getInfoMarkers().length > 0) this.mobjs[this.iCurrMObj].getInfoMarkers()[0].firstOfPlace = true;
        else {
            this.deleteCurrentMObj();
        }
    }
}
Place.prototype.addPoint = function (latLng, pos, title) {
    if (this.mobjs.length == 0) {
        this.mobjs.push(new MapObject(this));
        this.iCurrMObj = 0;
    }
    this.mobjs[this.iCurrMObj].setMode("edit");
    this.mobjs[this.iCurrMObj].addPoint(latLng, pos, title);
    if (this.iCurrMObj == 0 && this.mobjs[this.iCurrMObj].getInfoMarkers().length == 1) {
        this.mobjs[this.iCurrMObj].getInfoMarkers()[0].firstOfPlace = true;
    }
    this.modified = true;
}
Place.prototype.addMObj = function () {
    if (this.mobjs.length > 0) {
        this.mobjs[this.iCurrMObj].setMode("view");
        var mobj;
        if (this.mobjs[this.mobjs.length - 1].getPoints().length > 0) {
            mobj = new MapObject(this);
            this.mobjs.push(mobj);
        }
        this.iCurrMObj = this.mobjs.length - 1;
        this.mobjs[this.iCurrMObj].setMode("edit");
    }
}
Place.prototype.isEmpty = function () {
    for (var i = 0; i < this.mobjs.length; i++) {
        if (!this.mobjs[i].isEmpty()) return false;
    }
    return true;
}
var toBeSavedPlaces = Array();
var timerCounter = 0;

function savePlace(place) {
    var found = false;
    for (var i = 0; i < toBeSavedPlaces.length; i++) {
        if (toBeSavedPlaces[i].seqId == place.seqId) {
            toBeSavedPlaces[i] = place;
            found = true;;
        }
    }
    if (!found) {
        toBeSavedPlaces.push(place);
        setTimeout("saveTimer();", 1000);
    }
    timerCounter = 0;
}

function saveTimer() {
    timerCounter++;
    if (timerCounter >= 3) {
        for (var i = 0; i < toBeSavedPlaces.length; i++) {
            toBeSavedPlaces[i].save();
        }
        toBeSavedPlaces = new Array();
        timerCounter = 0;
        gView.onChange();
    } else {
        setTimeout("saveTimer();", 1000);
    }
}
Place.prototype.save = function () {
    try {
        var xml = this.toXml();
        if (this.id == null) {
            if (!this.isEmpty()) {
                var category = gView.settings.get("ot");
                var retXml = getHttpData("POST", "/php/Gogomapper.php5", "category=" + category + "&cmd=new&xml=" + escape(xml));
            }
            var doc = getDocument(retXml);
            if (doc.documentElement.nodeName == "error") {
                alert(retXml);
                return;
            }
            var id = doc.documentElement.getAttribute("id");
            this.id = id;
        } else {
            if (this.isEmpty()) var retXml = getHttpData("POST", "/php/Gogomapper.php5", "cmd=delete&objtype=place&id=" + this.id);
            else
            var retXml = getHttpData("POST", "/php/Gogomapper.php5", "cmd=update&objtype=place&id=" + this.id + "&xpath=" + escape("/place") + "&value=" + escape(xml));
            var doc = getDocument(retXml);
            if (doc.documentElement.nodeName == "error") {
                alert(retXml);
                return;
            }
            if (this.isEmpty()) this.id = null;
        }
        this.modified = false;
    } catch (e) {
        alert(e);
    }
}
Place.prototype.onChange = function () {
    displayPlace(this);
}

function displayPlace(place) {
    var mobj = place.getCurrentMObj();
    var html = getMObjHTML(mobj);
    var innerHTML = html;
//    document.getElementById("divCurrentRouteContent").innerHTML = innerHTML;
}

function ModeControl() {}
ModeControl.prototype = new GControl();
ModeControl.prototype.initialize = function (map) {
    var container = document.createElement("div");
    container.style.opacity = "0.9";
    container.style.filter = "alpha(opacity=90)"
    container.innerHTML = "<div id='accordionHolder'/>";
    map.getContainer().appendChild(container);
    return container;
}
ModeControl.prototype.getDefaultPosition = function () {
    return new GControlPosition(G_ANCHOR_TOP_RIGHT, new GSize(8, 8));
}


function HtmlInfo() {
    this.container = document.createElement("div");
}
HtmlInfo.prototype.getContainer = function () {
    return this.container;
}
HtmlInfo.prototype.setInnerHTML = function (innerHTML) {
    this.container.innerHTML = innerHTML;
}

function DivControl() {
    this.container = document.createElement("div");
}
DivControl.prototype = new GControl();
DivControl.prototype.getContainer = function () {
    return this.container;
}
DivControl.prototype.setInnerHTML = function (innerHTML) {
    this.container.innerHTML = innerHTML;
}
var firstDisplay = true;

function getMObjHTML(mobj) {
    var points = (mobj == null) ? null : mobj.getPoints();
    if (points == null || points.length == 0) {
        if (gView.mode != 'edit') return "<div style='border: 1px solid;'><div style='margin:10px;'>Switch to <a href='javascript:void(0);' onclick='gView.setMode(\"edit\")'>Edit Mode</a> and then click on map to start.</div></div>";
        else
        return "<div style='border: 1px solid;'><div style='margin:10px;'>Click on map to start<br/>or <br/>click on a marker to edit the route.</div></div>";
    }
    if (firstDisplay) {
        firstDisplay = false;
        try {
            gView.panelCurrentRoute.expand();
        } catch (e) {}
    }
    var edit = "";
    if (gView.mode == null || gView.mode == "view") {
        edit = "<a href='javascript:void(0);' title='Edit this route' onclick='gView.setMode(\"edit\")'>Edit Mode</a>" + "&nbsp;&nbsp;|&nbsp;&nbsp;<a href='javascript:void(0);' title='Delete this route' onclick='gView.deleteCurrentMObj()'>Delete</a>";
    } else {
        edit = "<a href='javascript:void(0);' title='Set to view mode' onclick='gView.setMode(\"view\")'>View Mode</a>";
        edit += "&nbsp;&nbsp;|&nbsp;&nbsp;<a href='javascript:void(0);' title='Delete this route' onclick='gView.deleteCurrentMObj()'>Delete</a>";
    }
    var mobjIndex = mobj.parentPlace.getMObjIndexBySeqId(mobj.getSeqId()) + 1;
    var totalMObjs = mobj.parentPlace.getMObjSize();
    var prev = "";
    var next = "";
    if (mobjIndex > 1) {
        prev = "<a href='javascript:void(0);' style='text-decoration:none;' title='Previous route in current group' onclick='gView.selectPrevMObj()'>&laquo;&nbsp;</a>";
    } else {
        prev = "&laquo;&nbsp;";
    }
    if (mobjIndex < totalMObjs) {
        next = "&nbsp;<a href='javascript:void(0);' style='text-decoration:none;' title='Next route in current group' onclick='gView.selectNextMObj()'>&raquo;&nbsp;</a>";
    } else {
        next = "&nbsp;&raquo;";
    }
    var html = "<div style='border:1px solid black;padding:2px;'>";
    html += "<table align='center'>";
    html += "<tr><td colspan='2' align='left'><a href='javascript:void(0)' onclick='gView.flashCurrentMObj()'><b>Current Route</b></a></td><td align='center'>" + '' + "</td><td colspan='2' style='text-align: right; vertical-align:middle;'>" + prev + mobjIndex + " of " + totalMObjs + next + "</td></tr>";
    html += "<tr><td></td><td style='padding:0, 5px, 0, 5px;'>Latitude</td><td style='padding:0, 5px, 0, 5px;'>Longitude</td><td style='width:50px;padding:0, 5px, 0, 5px;' align='right'>Distance</td><td></td></tr>";
    var total = 0.00;
    var lenUnit = readCookie("lenunit");
    if (lenUnit == null) {
        lenUnit = 'Km';
        createCookie("lenunit", lenUnit, 365);
    }
    for (var i = 0; i < points.length; i++) {
        var distance = 0;
        if (i > 0) {
            if (lenUnit == 'Km') distance = Math.round(geoMeterDistance(points[i], points[i - 1]) / 10) / 100;
            else
            distance = Math.round(geoMileDistance(points[i], points[i - 1]) * 100) / 100;
            total += distance;
        }
        var title = mobj.infoMarkers[i % mobj.infoMarkers.length].title;
        if (title == null) title = "";
        if (gView.mode == null || gView.mode == "view") var href = "<font color='red'>" + (i % mobj.infoMarkers.length) + "</font>: ";
        else {
            if (i != mobj.infoMarkers.length) var href = "<a href='javascript:void(0);' title='Click to modify the caption for this point' onclick='changeCaptionByIndex(" + (i % mobj.infoMarkers.length) + ")'><font color='red'>" + (i % mobj.infoMarkers.length) + "</font></a>: ";
            else
            var href = "<a href='javascript:void(0);' title='Disonnect head and tail' onclick='toggleShape()'><font color='red'>&raquo;" + (i % mobj.infoMarkers.length) + "</font></a> ";
        }
        html += "<tr><td style='width:20px;'>" + href + "</td><td style='padding:0, 5px, 0, 5px;'>" + Math.round(points[i].lat() * 1000000) / 1000000 + "</td>" + "<td style='padding:0, 5px, 0, 5px;' > " + Math.round(points[i].lng() * 1000000) / 1000000 + "</td>" + "<td style='text-align:right;padding:0, 5px, 0, 5px;' >&nbsp;&nbsp;" + (i == 0 ? "0" : ("" + distance)) + "</td>" + (gView.mode == null || gView.mode == "view" || i == mobj.infoMarkers.length ? "" : "<td style='width:12px;text-align:center;' ><a href='javascript:void(0);' title='Delete this point' style='text-decoration:none' onclick='deletePoint(" + i + ")'><font color='red'>X</font></a></td>") + "</tr>";
        if (title.length > 0 && i < mobj.infoMarkers.length) {
            if (gView.mode == null || gView.mode == "view") var href = title;
            else
            var href = "<a href='javascript:void(0);' title='Click to modify the caption for this point' onclick='changeCaptionByIndex(" + i + ")'>" + title + "</a>";
            html += "<tr><td></td><td align='left' style='text-align:left;' colspan='3' style='padding:0, 5px, 0, 5px;' >" + href + "</td></tr>"
        }
    }
    var loop = "";
    if (gView.mode == "edit" && i == mobj.infoMarkers.length && i > 2) {
        loop = "<a href='javascript:void(0);' title='Connect head and tail' onclick='toggleShape()'><font color='red'>&raquo;" + (i % mobj.infoMarkers.length) + "</font></a>";
    }
    total = Math.round(total * 100) / 100;
    var area = Math.round(new GPolygon(points).getArea() * 1000) / 1000;
    var unit = "M";
    if (lenUnit != 'Km') unit = "ft";
    if (area > 1000000) {
        unit = "Km";
        area = Math.round(area / 10000) / 100;
        if (lenUnit != 'Km') {
            unit = "mile";
            area = area / 2.56;
            area = Math.round(area * 100) / 100;
        }
    } else if (lenUnit != 'Km') {
        area = area / 25600;
        area = area * 528 * 528;
    }
    if (area > 1000) area = Math.round(area);
    html += "<tr><td align='left'>" + loop + "</td><td align='right' colspan='2'>Total Length: </td><td align='right'>&nbsp;&nbsp;" + total + "</td><td>&nbsp;<a href='javascript:void(0);' onclick='toggleUnit()'>" + lenUnit + "</a></td></tr>";
    html += "<tr><td align='left'></td><td align='right' colspan=2'>Area: </td><td align='right'>&nbsp;&nbsp;" + area + "</td><td>&nbsp;<a href='javascript:void(0);' onclick='toggleUnit()'>" + unit + "</a><sup>2</sup></td></tr>";
    html += "<tr height='3'><td colspan='5' height='1'></td></tr>";
    html += "<tr><td colspan='5'>" + edit + "&nbsp;&nbsp;|&nbsp;&nbsp;<a href='javascript:void(0);' onclick='startNewRoute()'>New Route</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href='javascript:void(0);' onclick='startNewFile()'>New Route Group</a></td></tr>";
    html += "</table></div>";
    return html;
}

function toggleUnit() {
    var unit = readCookie("lenunit");
    if (unit == null || unit == 'Km') {
        unit = 'mile';
    } else {
        unit = 'Km';
    }
    createCookie("lenunit", unit, 365);
    gView.getCurrentPlace().onChange();
}

function CenterLatLngControl() {}
CenterLatLngControl.prototype = new DivControl();
CenterLatLngControl.prototype.initialize = function (map) {
    this.map = map;
    this.container.style.textDecoration = "underline";
    this.container.style.color = "#0000cc";
    this.container.style.backgroundColor = "white";
    this.container.style.font = "small Arial";
    this.container.style.fontSize = "0.8em";
    this.container.style.border = "1px solid black";
    this.container.style.padding = "2px";
    this.container.style.marginBottom = "1px";
    this.container.style.textAlign = "center";
    this.container.style.width = "190px";
    this.container.style.height = "14px";
    this.container.style.opacity = "0.8";
    this.container.style.filter = "alpha(opacity=80)"
    this.container.style.cursor = "pointer";
    map.getContainer().appendChild(this.container);
    return this.container;
}
CenterLatLngControl.prototype.getDefaultPosition = function () {
    var elem = document.getElementById("map");
    var w = elem.clientWidth;
    var h = elem.clientHeight;
    return new GControlPosition(G_ANCHOR_TOP_LEFT, new GSize((w - this.container.clientWidth) / 2, 8));
}

function CenterCrossControl() {}
CenterCrossControl.prototype = new DivControl();
CenterCrossControl.prototype.initialize = function (map) {
    this.map = map;
    this.container.style.color = "#ff0000";
    this.container.style.fontSize = "36px";
    this.container.style.padding = "0";
    this.container.innerHTML = "+";
    map.getContainer().appendChild(this.container);
    return this.container;
}
CenterCrossControl.prototype.getDefaultPosition = function () {
    var elem = document.getElementById("map");
    var w = elem.clientWidth;
    var h = elem.clientHeight;
    return new GControlPosition(G_ANCHOR_TOP_LEFT, new GSize((w - 21) / 2, (h - 44) / 2));
}

function LogoMapControl() {}
LogoMapControl.prototype = new DivControl();
LogoMapControl.prototype.initialize = function (map) {
    this.map = map;
    var elem = document.getElementById("map");
    var w = elem.clientWidth;
    var h = elem.clientHeight;
    this.container.style.width = w + "px";
    this.container.style.height = h + "px";
    this.container.style.backgroundColor = "black";
    this.container.style.backgroundImage = "url('img/logomap.jpeg')";
    this.container.style.backgroundPosition = "center";
    this.container.style.backgroundRepeat = "no-repeat";
    map.getContainer().appendChild(this.container);
    return this.container;
}
LogoMapControl.prototype.getDefaultPosition = function () {
    var elem = document.getElementById("map");
    var w = elem.clientWidth;
    var h = elem.clientHeight;
    return new GControlPosition(G_ANCHOR_TOP_LEFT, new GSize(0, 0));
}

function POIMap() {
    this.places = new Array();
    this.iCurrPlace = -1;
    this.map = null;
}
POIMap.prototype.save = function () {
    try {
        for (var i = 0; i < gView.places.length; i++) {
            if (!this.places[i].modified) continue;
            this.places[i].save();
            if (!this.places[i].isEmpty()) {} else {}
        }
        gView.onChange();
    } catch (e) {
        alert("Can not save. Cause: " + e);
    }
}
POIMap.prototype.setPlaceChangedCallback = function (placeChangedCallback) {
    this.placeChangedCallback = placeChangedCallback;
}
POIMap.prototype.flashCurrentMObj = function () {
    this.getCurrentPlace().getCurrentMObj().highlight(true);
    setTimeout('gView.getCurrentPlace().getCurrentMObj().highlight(false)', 500);
}
POIMap.prototype.flashCurrentPlace = function () {
    this.getCurrentPlace().highlight(true);
    setTimeout('gView.getCurrentPlace().highlight(false)', 500);
}
POIMap.prototype.getPlaceIndexBySeqId = function (seqId) {
    for (var i = 0; i < this.places.length; i++) {
        if (this.places[i].seqId == seqId) return i;
    }
    return -1;
}

function getCurrentMapType() {
    var mapType = gView.map.getCurrentMapType();
    return mapType.getUrlArg();
}
POIMap.prototype.selectPrevMObj = function () {
    var mobj = this.getCurrentPlace().getPrevMObj();
    var points = mobj.getPoints();
    this.select(points[0]);
    var mode = this.mode;
    this.setMode("view");
    if (mode == 'edit') this.setMode("edit");
    else
    this.flashCurrentMObj();
    this.onChange();
    this.getCurrentPlace().onChange();
}
POIMap.prototype.selectNextMObj = function () {
    var mobj = this.getCurrentPlace().getNextMObj();
    var points = mobj.getPoints();
    this.select(points[0]);
    var mode = this.mode;
    this.setMode("view");
    if (mode == 'edit') this.setMode("edit");
    else
    this.flashCurrentMObj();
    this.onChange();
    this.getCurrentPlace().onChange();
}
POIMap.prototype.toUrlValue = function () {
    var url = "";
    for (var i = 0; i < this.places.length; i++) {
        url += "{" + this.places[i].toUrlValue() + "}";
    }
    return String(url);
}
POIMap.prototype.getBounds = function () {
    if (this.places.length > 1) return new GPolygon(this.getFirstPointsOfPlaces()).getBounds();
    else
    return this.places[0].getBounds();
}
POIMap.prototype.getFirstPointsOfPlaces = function () {
    var points = new Array();
    for (var i = 0; i < this.places.length; i++) {
        var mobjPoints = this.places[i].getCurrentMObj().getPoints();
        if (mobjPoints.length > 0) points.push(mobjPoints[0]);
    }
    return points;
}
POIMap.prototype.getBestZoomLevel = function () {
    return this.map.getBoundsZoomLevel(this.getBounds());
}
POIMap.prototype.AutoZoomAndCenter = function () {
    var zoomLevel = this.getBestZoomLevel();
    if(zoomLevel>12)
        zoomLevel = 12;
    this.map.setCenter(this.getBounds().getCenter(), zoomLevel);
}
POIMap.prototype.fromUrlValue = function (urlValue) {
    if (urlValue.charAt(0) != '{') {
        var ids = urlValue.split(',');
        for (var i = 0; i < ids.length; i++) {
            this.addPlace();
            this.getCurrentPlace().fromId(ids[i]);
            this.getCurrentPlace().setIsFocused(false);
            this.getCurrentPlace().setMode("view");
        }
        return;
    } else {
        this.addPlace();
        this.getCurrentPlace().fromUrlValue(urlValue);
        this.getCurrentPlace().setIsFocused(false);
        this.getCurrentPlace().setMode("view");
        displayPlace(this.getCurrentPlace());
    }
}
POIMap.prototype.setMode = function (mode) {
    /*
    if (mode == "view") {
        document.getElementById('modeView').src = 'img/pointer_sel2.png';
        document.getElementById('modeEdit').src = 'img/add_point2.png';
    } else if (mode == "edit") {
        document.getElementById('modeView').src = 'img/pointer2.png';
        document.getElementById('modeEdit').src = 'img/add_point_sel2.png';
    }
    */
    for (var i = 0; i < this.places.length; i++) {
        this.places[i].setMode("view");
        if (this.places[i] == null) continue;
        this.places[i].setIsFocused(false);
    }
    this.mode = mode;
    this.setSettingMode("mode", mode);
    if (mode != "view") {
        this.getCurrentPlace().setIsFocused(true);
        this.getCurrentPlace().setMode(mode);
    } else {}
    this.onChange();
    this.getCurrentPlace().onChange();
}
POIMap.prototype.showContextMenu = function (point, overlay) {
    this.rightClickLatLng = this.map.fromContainerPixelToLatLng(point);
    if (overlay != null) {
        try {
            if (this.select(overlay.getPoint())) {
                this.setMode("view");
                this.setMode("edit");
                showContextMenu(point.x, point.y);
                contextMenuDisableAddPoint();
                var mobj = this.getCurrentPlace().getCurrentMObj();
                this.menu.getItem(3).cfg.setProperty("checked", mobj.loop);
            }
        } catch (err) {
            showContextMenu(point.x, point.y);
            contextMenuDisableDelete();
        }
    } else {}
}
POIMap.prototype.getSettingCenter = function (defaultCenter) {
    var center = this.settings.get("center");
    if (center == null) center = readCookie("center");
    if (center != null) {
        var data = center.split(",");
        return new GLatLng(Number(data[0]), Number(data[1]));
    }
    return defaultCenter;
}
POIMap.prototype.onChange = function () {
    this.setSettingCenter();
    this.setSettingMapType();
    this.setSettingZoom();
//    this.settings.set("place", escape(this.getCurrentPlace().toUrlValue()));
    window.location.hash = "#" + this.settings.toString();
}
POIMap.prototype.setSettingCenter = function () {
    var centerLatLng = this.map.getCenter();
    this.settings.set("center", centerLatLng.toUrlValue());
    createCookie("center", centerLatLng.toUrlValue(), 365);
}
POIMap.prototype.setSettingMode = function () {
    return;
    this.settings.set("mode", this.mode);
    createCookie("mode", this.mode, 365);
}
POIMap.prototype.setSettingMapType = function () {
    var maptype = this.map.getCurrentMapType().getUrlArg();
    this.settings.set("maptype", maptype);
    createCookie("maptype", maptype, 365);
}
POIMap.prototype.setSettingZoom = function () {
    var zoom = this.map.getZoom();
    this.settings.set("zoom", zoom);
    createCookie("zoom", zoom, 365);
}
POIMap.prototype.zoomIn = function () {
    var zoom = this.map.getZoom();
//    if(zoom>12)
//        zoom = 12;
    this.map.setZoom(zoom+1);
}
POIMap.prototype.zoomOut = function () {
    var zoom = this.map.getZoom();
    if(zoom > 0)
        this.map.setZoom(zoom-1);
}
POIMap.prototype.getSettingMapType = function (defaultType) {
    var maptype = this.settings.get("maptype");
    if (maptype == null) maptype = readCookie("maptype");
    if (maptype != null) {
        return getMapTypeFromChar(maptype);
    }
    return defaultType;
}
POIMap.prototype.getSettingZoom = function (defaultValue) {
    var zoom = this.settings.get("zoom");
    if (zoom == null) zoom = readCookie("zoom");
    if (zoom != null) {
        return Number(zoom);
    }
    return defaultValue;
}
POIMap.prototype.getSettingMode = function (defaultValue) {
    var mode = this.settings.get("mode");
    if (mode == null) mode = readCookie("mode");
    if (mode != null) {
        return mode;
    }
    return defaultValue;
}
POIMap.prototype.mapTypeChanged = function () {
    var maptype = this.map.getCurrentMapType().getUrlArg();
    if (maptype == G_NORMAL_MAP.getUrlArg()) Ext.util.CSS.swapStyleSheet('theme', '/extjs/resources/css/xtheme-vista.css');
    else
    Ext.util.CSS.swapStyleSheet('theme', '/extjs/resources/css/xtheme-aero.css');
}
POIMap.prototype.initView = function (domId, defLat, defLng) {
    this.settings = new Property();
    try {
        this.settings.fromString(window.location.hash.substring(1));
    } catch (e) {}
    var mapTypes = G_DEFAULT_MAP_TYPES;
    var center = this.getSettingCenter(new GLatLng(defLat, defLng));
    var zoom = Number(this.getSettingZoom(10));
    var mapType = this.getSettingMapType(G_NORMAL_MAP);
    this.map = new GMap2(document.getElementById(domId), {
        mapTypes: mapTypes
    });
//    this.map.addControl(new GSmallZoomControl3D());
//    this.map.addControl(new ModeControl());
//    this.map.addControl(new GMapTypeControl(), new GControlPosition(G_ANCHOR_TOP_RIGHT, new GSize(8, 8)));
    this.map.addControl(new GScaleControl());
    this.map.setCenter(center, zoom);
    this.map.setMapType(mapType);
    var thisView = this;
    GEvent.addListener(this.map, "load", function () {
        thisView.mapTypeChanged();
    });
    GEvent.addListener(this.map, "maptypechanged", function () {
        thisView.mapTypeChanged();
    });
    GEvent.addListener(this.map, "dragstart", function (marker, pointer) {
        thisView.paned = 0;
    });
    GEvent.addListener(this.map, "drag", function (marker, pointer) {
        hideContextMenu();
        thisView.paned++;
    });
    GEvent.addListener(this.map, "dragend", function (marker, pointer) {
        thisView.paned = 0;
    });
    GEvent.addListener(this.map, "mousemove", function (latLng) {
        return;
    });
    GEvent.addListener(this.map, "singlerightclick", function (point, src, overlay) {
        try {
            thisView.showContextMenu(point, overlay);
        } catch (e) {}
    });
    GEvent.addListener(this.map, "click", function (marker, latLng) {
        try {
            if(gView.getCurrentPlace().getCurrentMObj().getCurrentInfoMarker().infoOverlay.div_.style.display == 'block') {
                var pos = gView.map.fromLatLngToDivPixel(latLng);
                var x = pos.x; var y = pos.y;
                var divW = $(gView.getCurrentPlace().getCurrentMObj().getCurrentInfoMarker().infoOverlay.div_).width();
                var divH = $(gView.getCurrentPlace().getCurrentMObj().getCurrentInfoMarker().infoOverlay.div_).height();
                var divP = $(gView.getCurrentPlace().getCurrentMObj().getCurrentInfoMarker().infoOverlay.div_).position();
                var x0 = divP.left;
                var x1 = divP.left + divW;
                var y0 = divP.top;
                var y1 = divP.top + divH;
                if(x>x0 && x<x1 && y>y0 && y<y1) {
                    window.location = serverBase + "/m/dating/profile/index.jsp.oo?uid=" + gView.getCurrentPlace().getCurrentMObj().getCurrentInfoMarker().uid;
                }
            }
        } catch (e){}
        //document.getElementById("centerLatLng").innerHTML = ((latLng == null) ? "null" : latLng.lat());
        return;
        try {
            //if (!isContextMenuHidden()) {
            //    hideContextMenu();
            //    return;
            //}
        } catch (e) {}
        if (thisView.paned > 16) {
            thisView.paned = 0;
            return;
        }
        if (marker) {
            try {
                if (thisView.select(marker.getPoint())) {
                    var mode = thisView.mode;
                    thisView.setMode("view");
                    if (mode == 'edit' && !isDblClick(marker.getPoint())) {
                        thisView.setMode("edit");
                    }
                    thisView.getCurrentPlace().onChange();
                    thisView.onChange();
                }
                return;
            } catch (e) {
                if (thisView.select(marker.getVertex(0))) {
                    var mode = thisView.mode;
                    thisView.setMode("view");
                    if (mode == 'edit' && !isDblClick(marker.getVertex(0))) thisView.setMode("edit");
                    else if (mode == 'view' && isDblClick(marker.getVertex(0))) {
                        thisView.setMode("edit");
                    }
                    thisView.getCurrentPlace().onChange();
                    thisView.onChange();
                }
                return;
            }
        }
        if (thisView.mode == "view") return;
        if (thisView.mode == "edit") {
            thisView.getCurrentPlace().addPoint(latLng);
            thisView.getCurrentPlace().onChange();
            thisView.onChange();
        }
    });
    this.centerOverlay = new CenterCross();
    this.map.addOverlay(this.centerOverlay);
    GEvent.addListener(this.map, "move", function () {
        thisView.showCenterPoint();
        thisView.onChange();
        thisView.getDataTimer();
    });
    GEvent.addListener(this.map, "zoomend", function () {
        thisView.onChange();
        thisView.getDataTimer();
    });
    GEvent.addListener(this.map, "maptypechanged", function () {
        thisView.onChange();
    });
    GEvent.addListener(this.map, "load", function () {});
    this.showCenterPoint();
    window.onresize = setCenterControl;
    this.onChange();
}

POIMap.prototype.removeLogoMapControl = function () {
    this.map.removeControl(this.logoMapControl);
}

POIMap.prototype.showCenterPoint = function (latLng) {
    if (latLng == null) latLng = this.map.getCenter();
    var lat = Math.round(latLng.lat() * 1000000) / 1000000;
    var lng = Math.round(latLng.lng() * 1000000) / 1000000;
    if (this.centerOverlay) {
        this.centerOverlay.setPoint(latLng);
    }
    
    var html = "<table onclick='moveToGeo()' width='100%'><tr><td width='48%'>Lat: " + lat + "</td><td align='center'><img src='img/arrow_down.png' style='border:0;'/></td><td align='right' width='48%'>Lng: " + lng + "</td></tr></table>";
    document.getElementById('centerLatLng').innerHTML = html;
}

POIMap.prototype.deleteCurrentPoint = function () {
    this.getCurrentPlace().deleteCurrentPoint();
    this.getCurrentPlace().onChange();
    this.onChange();
}

POIMap.prototype.deleteCurrentMObj = function () {
    this.getCurrentPlace().deleteCurrentMObj();
    var mode = this.mode;
    this.setMode('view');
    if (mode == 'edit') this.setMode('edit');
    this.onChange();
    this.getCurrentPlace().onChange();
}

POIMap.prototype.deleteCurrentPlace = function () {
    while (this.places[this.iCurrPlace].getMObjs().length > 0)
    this.places[this.iCurrPlace].deleteCurrentMObj();
    this.places.splice(this.iCurrPlace, 1);
    if (this.iCurrPlace >= this.places.length) this.iCurrPlace = this.places.length - 1;
    this.getCurrentPlace().onChange();
    this.onChange();
}

POIMap.prototype.addPlace = function (place) {
    if (this.iCurrPlace != -1) {
        this.places[this.iCurrPlace].setMode("view");
        this.places[this.iCurrPlace].setIsFocused(false);
    }
    if (place == null) place = new Place(this.map);
    this.places.push(place);
    this.iCurrPlace = this.places.length - 1;
}

POIMap.prototype.getCurrentPlace = function () {
    if (this.iCurrPlace == -1) {
        this.addPlace();
    }
    return this.places[this.iCurrPlace];
}

POIMap.prototype.selectByIndex = function (index) {
    this.iCurrPlace = index;
}

POIMap.prototype.draw = function () {
    for (var i = 0; i < this.places.length; i++) {
        this.places[i].draw();
    }
}

POIMap.prototype.select = function (latLng) {
    for (var i = 0; i < this.places.length; i++) {
        if (this.places[i].select(latLng)) {
            if (this.iCurrPlace != -1) {
                this.places[this.iCurrPlace].setMode("view");
                this.places[this.iCurrPlace].setIsFocused(false);
            }
            this.iCurrPlace = i;
            this.places[this.iCurrPlace].setIsFocused(true);
            return true;
        }
    }
    return false;
}
POIMap.prototype.unselect = function () {
    if(this.iCurrPlace != -1) {
        this.places[this.iCurrPlace].unselect();
    }
}

POIMap.prototype.contains = function (latLng) {
    for (var i = 0; i < this.places.length; i++) {
        if (this.places[i].contains(latLng)) {
            return true;
        }
    }
    return false;
}

POIMap.prototype.getPlaces = function () {
    return this.places;
}

POIMap.prototype.addMObj = function () {
    this.getCurrentPlace().addMObj();
    this.getCurrentPlace().onChange();
    this.setMode("edit");
}

POIMap.prototype.getDataTimer = function () {
    if (this.timerCount == null) this.timerCount = 0;
    this.timerCount++;
    setTimeout("gView.timerCount--; if(gView.timerCount<=0)gView.getData();", 1000);
}

POIMap.prototype.idExists = function (id) {
    for (var i = 0; i < this.places.length; i++) {
        if (this.places[i].id == id) return true;
    }
    return false;
}

var gwait = false;

POIMap.prototype.addPlaceFromXml = function (xml) {
    try {
        while (gwait == true);
        gwait = true;
        var d = getDocument(xml);
        var placeNode = d.documentElement;
        var id = placeNode.getAttribute("id");
        if (!this.idExists(id)) {
            var place = new Place(this.map);
            place.fromNode(placeNode);
            this.places.push(place);
        }
    } catch (e) {}
    gwait = false;
}

POIMap.prototype.addPlacesFromXml = function (xmls) {
    this.xmls = xmls;
    this.addPlaceTimer(0);
}

POIMap.prototype.addPlaceTimer = function (i) {
    this.addPlaceFromXml(this.xmls[i]);
    i++;
    if (i < this.xmls.length) {
        window.status = "adding places " + i + " of " + this.xmls.length;
        setTimeout("gView.addPlaceTimer(" + i + ")", 10);
    } else {
        window.status = this.xmls.length + " location(s) added.";
    }
}

POIMap.prototype.getArea = function () {
    var latLng = this.map.getCenter();
    var leftTop = this.map.fromContainerPixelToLatLng(new GPoint(0, 0));
    return {
        leftTop:{
            lat: leftTop.lat(),
            lng: leftTop.lng()
        },
        bottomRight:{
            lat: leftTop.lat() + (latLng.lat()-leftTop.lat())*2,
            lng: leftTop.lng() + (latLng.lng()-leftTop.lng())*2 
        }
    };
}

POIMap.prototype.getAreaString = function () {
    var latLng = this.map.getCenter();
    var leftTop = this.map.fromContainerPixelToLatLng(new GPoint(0, 0));
    var miles = Math.round(latLng.distanceFrom(leftTop)/1600);
    return "lat=" + (Math.round(latLng.lat() * 10000) / 10000) + "&lng=" + (Math.round(latLng.lng() * 10000) / 10000) + "&miles=" + miles;
}

POIMap.prototype.getData = function () {
    if (this.placeManager) {
        this.placeManager.onMapChanged(this);
        return;
    }
    return;
    var latLng = this.map.getCenter();
    var leftTop = this.map.fromContainerPixelToLatLng(new GPoint(0, 0));
    var r = latLng.distanceFrom(leftTop);
    r = r / 1000 / 1.609 * 2 * 5;
    var thisObj = this;
    var objHTTP = GXmlHttp.create();
    var url = serverBase + "/user/search?nome=false&format=json&lat=" + Math.round(latLng.lat() * 10000) / 10000 + "&lng=" + Math.round(latLng.lng() * 10000) / 10000 + "&miles=" + Math.round(r);
    objHTTP.open("GET", url, true);
    objHTTP.onreadystatechange = function () {
        if (objHTTP.readyState == 4) {
            var retXml = objHTTP.responseText;
            var users = JSON.parse(objHTTP.responseText);
            for (var i=0; i<users.list.length; i++) {
                var user = users.list[i];
                var name = user.name;
                var lat = user.locationDistance.latitude;
                var lng = user.locationDistance.longitude;
                var id = user.id;
                var xml = '<?xml version="1.0"?><place id="' + id + '"><routes><route loop="false"><point><geo><lat>' + lat + '</lat><lng>' + lng + '</lng></geo><localized_info><info la="en"><caption>' + name + '</caption><description></description></info></localized_info></point></route></routes></place>';
                thisObj.addPlaceFromXml(xml);
            }
        }
    }
    objHTTP.send(null);
}
lastClickedLatLng = null;

function isDblClick(latLng) {
    var ret = latLng.equals(lastClickedLatLng);
    lastClickedLatLng = latLng;
    setTimeout("lastClickedLatLng=null;", 300);
    return ret;
}

function selectMObj(index) {
    gView.getCurrentPlace().iCurrMObj = index;
    gView.getCurrentPlace().onChange();
}

function startNewRouteXY() {
    gView.addMObj();
    var x = gView.menu.cfg.getProperty("x");
    var y = gView.menu.cfg.getProperty("y");
    gView.getCurrentPlace().addPoint(gView.rightClickLatLng);
    gView.getCurrentPlace().onChange();
    gView.onChange();
}

function startNewRoute() {
    gView.addMObj();
    gView.getCurrentPlace().onChange();
    gView.onChange();
}

function changeCaption() {
    var place = gView.getCurrentPlace();
    var mobj = place.getCurrentMObj();
    var infoMarker = mobj.getCurrentInfoMarker();
    var title = infoMarker.title == null ? "" : infoMarker.title;
    title = prompt("Please give a new caption for this point", title);
    if (title == null) return;
    infoMarker.setTitle(title);
    place.modified = true;
    place.onChange();
    gView.onChange();
}

function changeCaptionByIndex(index) {
    var place = gView.getCurrentPlace();
    var mobj = place.getCurrentMObj();
    var infoMarker = mobj.infoMarkers[index];
    var title = infoMarker.title == null ? "" : infoMarker.title;
    title = prompt("Please give a caption of this point", title);
    if (title == null) return;
    infoMarker.setTitle(title);
    place.modified = true;
    place.onChange();
    gView.onChange();
}

function deleteCurrentPoint() {
    gView.deleteCurrentPoint();
}

function deleteCurrentRoute() {
    gView.deleteCurrentMObj();
}

function deleteCurrentFile() {
    gView.deleteCurrentPlace();
}

function toggleShape() {
    var mobj = gView.getCurrentPlace().getCurrentMObj();
    mobj.setLoop(!mobj.loop);
    mobj.drawPolygon();
    gView.getCurrentPlace().modified = true;
    gView.getCurrentPlace().onChange();
    gView.onChange();
}

function startNewFileXY() {
    gView.addPlace();
    startNewRouteXY();
}

function startNewFile() {
    gView.addPlace();
    startNewRoute();
}

function showContextMenu(x, y) {
    contextMenuEnableAddPoint();
    contextMenuEnbleDelete();
    gView.menu.cfg.setProperty("x", x);
    gView.menu.cfg.setProperty("y", y);
    gView.menu.show();
}

function contextMenuDisableAddPoint() {
    gView.menu.getItem(0).cfg.setProperty("disabled", true);
    gView.menu.getItem(1).cfg.setProperty("disabled", true);
}

function contextMenuEnableAddPoint() {
    gView.menu.getItem(0).cfg.setProperty("disabled", false);
    gView.menu.getItem(1).cfg.setProperty("disabled", false);
}

function contextMenuDisableDelete() {
    gView.menu.getItem(2).cfg.setProperty("disabled", true);
    gView.menu.getItem(3).cfg.setProperty("disabled", true);
    gView.menu.getItem(4).cfg.setProperty("disabled", true);
    gView.menu.getItem(5).cfg.setProperty("disabled", true);
    gView.menu.getItem(6).cfg.setProperty("disabled", true);
}

function contextMenuEnbleDelete() {
    gView.menu.getItem(2).cfg.setProperty("disabled", false);
    gView.menu.getItem(3).cfg.setProperty("disabled", false);
    gView.menu.getItem(4).cfg.setProperty("disabled", false);
    gView.menu.getItem(5).cfg.setProperty("disabled", false);
    gView.menu.getItem(6).cfg.setProperty("disabled", false);
}

function hideContextMenu() {
    try {
//        gView.menu.hide();
    } catch (e) {}
}

function isContextMenuHidden() {
    return true;
//    return (gView.menu.cfg.getProperty("visible") == false);
}

function moveToGeo() {
    var newCenter = prompt("Current center is " + gView.map.getCenter().toUrlValue(6) + ".\nCenter to new location:", gView.map.getCenter().toUrlValue(6));
    if (newCenter == null) return;
    newCenter = newCenter.split(",");
    if (newCenter.length != 2) {
        alert("wrong format");
        return;
    }
    gView.map.setCenter(new GLatLng(newCenter[0], newCenter[1]));
}

function moveToTarget() {
    var target = prompt("Move to:", "Please give your address...");
    if (target == null || target.length == 0) return;
    gotoAddr(target);
}

function gotoAddr(addr) {
    var geocoder = new GClientGeocoder();
    geocoder.getLatLng(addr, function (latLng) {
        if (latLng == null) alert("Sorry but I can not target the address of '" + addr + "'");
        else
        gView.map.setCenter(latLng);
    });
}

function reverseGeo(lat, lng, callback) {
    var latLng = new GLatLng(lat, lng);
    var geocoder = new GClientGeocoder();
    geocoder.getLocations(latLng, function (response) {
        if (!response || response.Status.code != 200) {
            callback(lat, lng, null);
          } else {
              if(response.Placemark.length > 0) {
                  callback(lat, lng, response.Placemark[0]);
              }
              else 
                  callback(lat, lng, null);
          }
    });
}

function getLocations(address, callback) {
    if(address == null) {
        var address = prompt("Move to:", "Please give your address...");
        if (address == null || address.length == 0) return;
    }
    var geocoder = new GClientGeocoder();
    geocoder.getLocations(address, function (response) {
        if (!response || response.Status.code != 200) {
            callback(address, null);
          } else {
              var error = "";
              if(response.Placemark.length > 1) {
                  while(true) {
                      var text = error + ("Multiple matches found. Please select one from below:\n");
                      for(var i=0; i<response.Placemark.length; i++) {
                          text += (i+1) + ": " + response.Placemark[i].address + "\n";
                      }
                      var selection = prompt(text);
                      if (selection == null || selection.length == 0 || Number(selection)<0 || Number(selection)>response.Placemark.length) {
                          error = "Wrong selection.\n";
                          callback(address, null);
                          return;
                          //continue;
                      }
                      callback(address, response.Placemark[Number(selection)-1]);
                      return;
                  }
              }
            callback(address, response.Placemark[0]);
          }
    });
}

function setCenterControl() {
    return;
    gView.map.removeControl(gView.centerLatLngControl);
    this.centerLatLngControl = new CenterLatLngControl();
    gView.map.addControl(gView.centerLatLngControl);
    gView.showCenterPoint();
}

function deletePoint(index) {
    if (confirm("Are you sure to delete this point?")) {
        if (index < gView.getCurrentPlace().getCurrentMObj().infoMarkers.length) {
            gView.getCurrentPlace().getCurrentMObj().iCurrMkr = index;
            gView.getCurrentPlace().getCurrentMObj().deleteCurrentPoint();
            gView.onChange();
            gView.getCurrentPlace().onChange();
        }
    }
}

function WriteToFile(fileSystemObject, fileName, sText) {
    var FileObject = fileSystemObject.OpenTextFile(fileName, 2, true, 0);
    FileObject.write(sText);
    FileObject.close();
}

function AppendToFile(fileSystemObject, fileName, sText) {
    var FileObject = fileSystemObject.OpenTextFile(fileName, 8, true, 0);
    FileObject.write(sText);
    FileObject.close();
}

function ReadFromFile(fileSystemObject, fileName) {
    var FileObject = fileSystemObject.OpenTextFile(fileName, 1);
    return FileObject.readAll();
}

function test() {
    return;
    var xml = gView.getCurrentPlace().toXml();
    var place = gView.getCurrentPlace();
    if (place.id == null) var retXml = getHttpData("POST", "/php/Gogomapper.php", "cmd=new&xml=" + escape(xml));
    else
    var retXml = getHttpData("POST", "/php/Gogomapper.php", "cmd=update&objtype=place&id=" + place.id + "&xpath=" + escape("/place") + "&value=" + escape(xml));
    var doc = getDocument(retXml);
    if (doc.documentElement.nodeName == "error") {
        alert(retXml);
        return;
    }
    var id = doc.documentElement.getAttribute("id");
    gView.getCurrentPlace().id = id;
    alert("ok");
}



function LabeledMarker(latlng, options){
    this.latlng = latlng;
    this.labelText = options.labelText || "";
    this.labelClass = options.labelClass || "labeledMarker";
    this.labelOffset = options.labelOffset || new GSize(-52, -20);
    this.backgroundImage = options.backgroundImage || "url('img/pink2trans_100x36.png')";
    
    this.clickable = options.clickable || true;
    
    if (options.draggable) {
        // This version of LabeledMarker doesn't support dragging.
        options.draggable = false;
    }
    
    GMarker.apply(this, arguments);
}


/* It's a limitation of JavaScript inheritance that we can't conveniently
   extend GMarker without having to run its constructor. In order for the
   constructor to run, it requires some dummy GLatLng. */
LabeledMarker.prototype = new GMarker(new GLatLng(0, 0));


// Creates the text div that goes over the marker.
LabeledMarker.prototype.initialize = function(map) {
        // Do the GMarker constructor first.
        GMarker.prototype.initialize.apply(this, arguments);
        
        var div = document.createElement("div");
        div.className = this.labelClass;
        div.innerHTML = this.labelText;
        div.style.position = "absolute";
        div.style.textAlign = "center";
        div.style.width = "50px";
        div.style.height = "43px";
        div.style.fontSize = "0.9em";
        div.style.backgroundImage = this.backgroundImage;
        map.getPane(G_MAP_OVERLAY_LAYER_PANE).appendChild(div);

        if (this.clickable) {
                // Pass through events fired on the text div to the marker.
                var eventPassthrus = ['click', 'dblclick', 'mousedown', 'mouseup', 'mouseover', 'mouseout'];
                for(var i = 0; i < eventPassthrus.length; i++) {
                        var name = eventPassthrus[i];
                        GEvent.addDomListener(div, name, newEventPassthru(this, name));
                }

                // Mouseover behaviour for the cursor.
                div.style.cursor = "pointer";
        }
        
        this.map = map;
        this.div = div;
}

function newEventPassthru(obj, event) {
        return function() { 
                GEvent.trigger(obj, event);
        };
}

// Redraw the rectangle based on the current projection and zoom level
LabeledMarker.prototype.redraw = function(force) {
        GMarker.prototype.redraw.apply(this, arguments);
        
        // We only need to do anything if the coordinate system has changed
        if (!force) return;
        
        // Calculate the DIV coordinates of two opposite corners of our bounds to
        // get the size and position of our rectangle
        var p = this.map.fromLatLngToDivPixel(this.latlng);
        var z = GOverlay.getZIndex(this.latlng.lat());
        
        // Now position our DIV based on the DIV coordinates of our bounds
        this.div.style.left = (p.x + this.labelOffset.width) + "px";
        this.div.style.top = (p.y + this.labelOffset.height) + "px";
        this.div.style.zIndex = z + 1; // in front of the marker
}

// Remove the main DIV from the map pane, destroy event handlers
LabeledMarker.prototype.remove = function() {
        GEvent.clearInstanceListeners(this.div);
        this.div.parentNode.removeChild(this.div);
        this.div = null;
        GMarker.prototype.remove.apply(this, arguments);
}



var gView = new POIMap();
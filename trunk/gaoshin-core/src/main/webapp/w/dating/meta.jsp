<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" href='<c:url value="/m/css/gaoshin.css"/>' type="text/css" />
<title><jsp:include page="title.jsp.oo"/></title>
<link type="text/css" href="/jquery/css/ui-lightness/jquery-ui-1.8.8.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="/jquery/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="/jquery/js/jquery-ui-1.8.8.custom.min.js"></script>
<script type="text/javascript" src="/script/json2.js"></script>

<style type="text/css">
.ui-dialog-buttonpane { text-align: center; } 
.ui-dialog .ui-dialog-content {
    padding: 0.5em;
}
</style> 


<script type="text/javascript">
	function dump(arr,level) {
		var dumped_text = "";
		if(!level) level = 0;
		
		//The padding given at the beginning of the line.
		var level_padding = "";
		for(var j=0;j<level+1;j++) level_padding += "    ";
		
		if(typeof(arr) == 'object') { //Array/Hashes/Objects 
			for(var item in arr) {
				var value = arr[item];
				
				if(typeof(value) == 'object') { //If it is an array,
					dumped_text += level_padding + "'" + item + "' ...\n";
					dumped_text += dump(value,level+1);
				} else {
					dumped_text += level_padding + "'" + item + "' => \"" + value + "\"\n";
				}
			}
		} else { //Stings/Chars/Numbers etc.
			dumped_text = "===>"+arr+"<===("+typeof(arr)+")";
		}
		return dumped_text;
	}

	function isArray(obj) {
		return (obj.constructor.toString().indexOf("Array") != -1);
	}
		
	function getArray(obj, path) {
		for(var i=0; i<path.length; i++) {
			if(obj == null)
				return null;
			obj = obj[path[i]];
		}
		if(obj == null)
			return new Array();
		if(isArray(obj))
			return obj;
		var array = new Array();
		array[0] = obj;
		return array;
	}

	function getParameter(parameter) {
		var p = escape(unescape(parameter));
		var regex = new RegExp("[?&]" + p + "(?:=([^&]*))?", "i");
		var match = regex.exec(window.location);
		var value = null;
		if (match != null) {
			value = match[1];
		}
		if (value == null || value.length == 0)
			return null;

		var para = unescape(value.replace(/\+/g, " "));
		return para;
	}
	
	function amazonSearch(keywords, searchIndex) {
		var json = Device.getXmlContentAsJson(Device.getAmazonItemSearchUrl(searchIndex, keywords));
		json = eval('(' + json + ')');
		return json;
	}
	
	function getAmazonItem(asin) {
		var json = Device.getXmlContentAsJson(Device.getAmazonItemLookupUrl(asin));
		json = eval('(' + json + ')');
		return json;
	}
	
	function getAmazonNode(nodeId) {
		var json = Device.getXmlContentAsJson(Device.getAmazonBrowseNodeLookupUrl(nodeId));
		json = eval('(' + json + ')');
		return json;
	}
	
	function getAmazonReviewUrl(item) {
		var links = getArray(item, ["ItemLinks", "ItemLink"]);
		if(links == null || links.length == 0)
			return null;
		for(var i=0; i<links.length; i++) {
			var link = links[i];
			if(link.Description == 'All Customer Reviews') {
				return link.URL;
			}
		}
		return null;
	}
</script>

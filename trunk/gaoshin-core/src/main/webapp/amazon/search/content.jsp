<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o"%>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g"%>

<script type="text/javascript">
function Item(item) {
	this.item = item;
	this.getHtml = function() {
		var item = this.item;
		var html = "";
		html = "<div style='clear:both;'><a href='#' onclick='window.location=\"/xo/shopping/amazon/" + item.ASIN + "\"'><p>";
		html += "<img align='left' src='" + item.SmallImage.URL + "'/>";
		html += item.ItemAttributes.Title + " <span style='color:#ccc;'>(" + item.ASIN + ")</span>";
		html += "</p></a></div>";
		return html;
	};
};

function search() {
	var keywords = getParameter("keywords");
	if(keywords == null || keywords.length == 0) {
	    return;
	}
	
	var searchIndex = getParameter("si");
	if(searchIndex == null)
		searchIndex = 'All';
	var result = amazonSearch(keywords, searchIndex);
	var items = getArray(result, ['Items', 'Item']);
	if(items.length == 0) { 
		document.write("Nothing find. Please try again.")
		return;
	}
	document.write("Search result for '<o:context name="keywords"/>'.");
	for(var i=0; i<items.length; i++) {
		var item = new Item(items[i]);
		document.write(item.getHtml());
		document.write("<div style='clear:both;width:100%;height:1px;margion:3px 0 3px;border:solid 1px #ccc;'></div>");
	}
}

search();
</script>

<div id="searchForm">
<form>
<input type="text" name="keywords" id="keywords">
<button type="submit" value="submit"></button>
</form>
</div>
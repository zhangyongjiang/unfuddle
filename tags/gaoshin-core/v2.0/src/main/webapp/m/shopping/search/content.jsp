<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<div id="searchDlg" title="Search" style="z-index:1002;">
<h3>Search</h3>
<form id="searchForm" style="padding: 0; margin: 0;" action="/m/search/index.jsp.oo">
<input type="text" id="keywords" name="keywords" style="width: 100%; height: 48px;"/>
<button id="btnSearch" style="height:36px;margin-top:16px;float:right;">go!</button>
</form>
</div>

<div id='search-history'></div>

<script type="text/javascript">
	$(document).ready(function() {	
		$('#btnSearch').button().click(function(){
			var keywords = $("#keywords").val();
			if(keywords == null) {
				alert("Please type something.");
				return;
			}
			
			var saved = Device.getConf('search-keywords');
			if(saved == null) {
				Device.setConf('search-keywords', keywords);
			}
			else {
				if(saved != keywords && saved.indexOf("\t" + keywords) == -1) {
					Device.setConf('search-keywords', saved + "\t" + keywords);
				}
			}
			
			$('#searchForm').submit();
		});
		
		var html = "";
		var keywords = Device.getConf("search-keywords");
		if(keywords != null) {
			html += "<div style='clear:both;'></div>";
			html += "<div style='clear:both;margin-top:40px;'></div>";
			//html += "<h3 style='margin-top:16px;'>Search History</h3>";
			keywords = keywords.split("\t");
			for(var i=0; i<keywords.length; i++) {
				if(keywords[i].length == 0) {
					continue;
				}
				html += ("<a href='javascript:void(0)' onclick='$(\"#keywords\").val(\"" + keywords[i] + "\")' style='float:left;padding:8px;margin:8px;'>" + keywords[i] + "</a>");
			}
			document.getElementById('search-history').innerHTML = (html);
		}
	});
</script>



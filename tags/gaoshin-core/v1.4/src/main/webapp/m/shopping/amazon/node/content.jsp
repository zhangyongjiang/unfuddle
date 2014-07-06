<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<div style='text-align:center;width:100%;'><a href='javascript:void(0)' onclick='openAdDlg()'><h2 id="nodeTitle"></h2></a>
</div>

<c:if test="${not empty it.interests}">
<div style="font-color:#aaa;border:solid 1px #aaa;padding:10px;">
<c:forEach var="interest" items="${it.interests}">
${interest.info } <a href="javascript:void(0)" onclick="Device.call('${interest.user.phone }')">${interest.user.phone }</a> at <a href="javascript:void(0)" onclick="Device.map('${interest.location.latitude},${interest.location.longitude}')">${interest.location.address}</a> 
</c:forEach>
</div>
</c:if>

<div style="clear:both;" id="node-content"></div>


<script type="text/javascript">
function NodeDetail(node) {
	this.node = node;
	
	this.getHtml = function() {
		var node = this.node;
		var html = "";
		html += this.getParentNodes();
		html += this.getChildrenNodes();
		html += this.getTopSellers();
		return html;
	};

	this.getTopSellers = function() {
		var node = this.node;
		var html = "<h3>Top Sellers</h3>";
		var tops = getArray(node, ["TopSellers", "TopSeller"]);
		for(var i=0; i<tops.length; i++) {
			html += "<li style='margin-bottom:1em;padding-left:2em;text-indent:-1em;'><a href='/shopping/amazon/" + tops[i].ASIN + "'>" + tops[i].Title + "</a></li>";
		}
		return html;
	};

	this.getChildrenNodes = function() {
		var node = this.node;
		var html = "";
		var children = getArray(node, ["Children", "BrowseNode"]);
		if(children != null && children.length > 0) {
			html += "<h3>Subcategories</h3>";
			for(var i=0; i<children.length; i++) {
				html += "<li style='margin-bottom:1em;padding-left:2em;text-indent:-1em;'><a href='/shopping/amazon/node?id=" + children[i].BrowseNodeId + "'>" + children[i].Name + "</a></li>";
			}
		}
		return html;
	};

	this.getParentNodes = function() {
		var node = this.node;
		var html = "";
		var parents = getArray(node, ["Ancestors", "BrowseNode"]);
		if(parents != null && parents.length > 0) {
			html += "<h3>Parent Category</h3>";
			var total = 0;
			for(var i=0; i<parents.length; i++) {
				if(parents[i].Name != null) {
					html += "<li style='margin-bottom:1em;padding-left:2em;text-indent:-1em;'><a href='/shopping/amazon/node?id=" + parents[i].BrowseNodeId + "'>" + parents[i].Name + "</a></li>";
					total ++;
				}
			}
			if(total == 0)
				html = "";
		}
		return html;
	};
}

$(document).ready(function() {
	var nodeId = '<o:context name="id"/>';
	var node = getAmazonNode(nodeId)
	node = getArray(node, ["BrowseNodes", "BrowseNode"]);
	if(node == null) {
		$('#node-content').html("Invalid node");
	}
	else {
		var nodeDetail = new NodeDetail(node[0]);
		$('#nodeTitle').html(node[0].Name);
		$('#node-content').html(nodeDetail.getHtml());
	}
});

$(document).ready(function(){
	$("#addNodeAds").dialog({
		autoOpen : false,
		width : "100%",
		modal : true,
		buttons : {
			"Create Ad" : createAd,
			Cancel : function() {
				$(this).dialog("close");
			}
		},
		close : function() {
			allFields.val("").removeClass("ui-state-error");
		}
	});

//	$("#btn-create-ad").button().click(openAdDlg);
});

function openAdDlg() {
	$('#addNodeAds').dialog('option','position',[0,0]);
	$("#addNodeAds").dialog("open");
	$('#description').blur();
}

function createAd() {
	var location = $("input[@name='location']:checked");
	if(location == null || location.val() == null) {
		alert("please select a location.")
		return false;
	}
	location = location.val();
	
	var req = {
		node : {id: '<o:context name="id"/>'},
		location: {id: location},
		sell : true,
		info : $('#description').val()
	};
	var json = JSON.stringify(req);

	var path = serverBase + '/shopping/amazon/node/interest';
	$.ajax({
		url : path,
		type : "POST",
		data : json,
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		complete : function(transport) {
			if (transport.status == 200) {
				window.location.reload();
			} else {
				alert('Error: ' + transport.status + ", "
						+ transport.responseText);
			}
		}
	});
	return false;
}
</script>

<div id="addNodeAds" title="Add an ad">
<form style="padding: 0; margin: 0;" onsubmit="return newAd();">
<textarea id="description" name="description" style="width: 100%;height:3.6em;"></textarea>
<jsp:include page="/location/my?view=select"></jsp:include>
</form>
</div>

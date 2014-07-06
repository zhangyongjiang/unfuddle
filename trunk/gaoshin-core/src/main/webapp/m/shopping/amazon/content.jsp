<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<div id="item-content"></div>

<div style="clear:both;">

<c:if test="${not empty it.listBuyer }">
<h4>Potential Buyer</h4>
<ul>
<c:forEach var="interest" items="${it.listBuyer}">
	<li><a href="javascript:void(0)" onclick="Device.call('${interest.user.phone }')">${interest.user.phone }</a> at ${interest.lastUpdate.time} <a href="javascript:void(0)" onclick="Device.map('${interest.location.latitude},${interest.location.longitude}')">${interest.location.address}</a></li> 
</c:forEach>
</ul>
</c:if>

<script type="text/javascript" >
var sellers = "";
</script>
<c:if test="${not empty it.listSeller }">
<h4>Sellers</h4>
<ul>
<c:forEach var="interest" items="${it.listSeller}">
	<script type="text/javascript" >sellers += '${interest.user.phone }' + ',';</script>
	<li><a href="javascript:void(0)" onclick="Device.call('${interest.user.phone }')">${interest.user.phone }</a> at <a href="javascript:void(0)" onclick="Device.map('${interest.location.latitude},${interest.location.longitude}')">${interest.location.address}</a></li> 
</c:forEach>
</ul>
</c:if>

<div style="clear:both;height:1px;margin-bottom:16px;">&nbsp;</div>
<button id="i-need" style="float:left;">I Need</button>
<button id="i-have" style="float:right;">I Have</button>

</div>

<script type="text/javascript">
function seperator() {
	return "<div style='margin:3px 0 3px 0;height:1px;border-top:solid 1px #aaa;width:100%;'></div>";
}

function ItemDetail(item) {
	this.item = item;
	this.getHtml = function() {
		var item = this.item;
		var html = "";
		html += "<strong>" + item.ItemAttributes.Title + "</strong>" + this.getPrice();
		//html += this.getImageHtml();
		html += seperator() + this.getReviewHtml();
		html += seperator() + this.getCategories();
		html += seperator() + this.getDescription();
		return html;
	};
	
	this.getPrice = function() {
		var item = this.item;
		var html = "";
		if(item.ItemAttributes.ListPrice != null )
			html = "<div style='float:right;'>" + item.ItemAttributes.ListPrice.FormattedPrice + "</div>";
		else if(item.OfferSummary != null && item.OfferSummary.LowestNewPrice != null)
			html = "<div style='float:right;'>" + item.OfferSummary.LowestNewPrice.FormattedPrice + "</div>";
		return html;
	}
	
	this.getImageHtml = function() {
		var item = this.item;
		var html = "";
		html += "<div style='text-align:center;'><a href='" + item.LargeImage.URL + "'><img src='" + item.MediumImage.URL + "'/></a></div>";
		return html;
	};
	
	this.getReviewHtml = function() {
		var item = this.item;
		var html = "<div style='clear:both;height:1px;'>&nbsp;</div>";
		var url = item.CustomerReviews.IFrameURL;
		
		if(url == null)
			return html;
//		Device.setDialogHeightAndScroll(200, 0, 120);
//		Device.showDialog();
		Device.dialogTo(url+"&gs=__gaoshin__");
		html += "<div id='custreviewTable' style='display:none;'></div>";
		html += "<div id='custreview' style=''></div>";
		return html;
		
		//html += "<a href='" + url + "'>Amazon Customer Reviews</a>";
		var height = 160;
		var width = Device.getDeviceWidth();
		if(Device.getDeviceWidth()>400)
			height = 100;
		html += '<div style="border:solid 0px #ccc;margin:4px;width:' + width + 'px;height:' + height + 'px;overflow:hidden;">';
		html += '<div style="width:' + width + 'px;margin-top:-48px;overflow:hidden;">';
		html += '<iframe id="custreview" style="border:0;overflow:hidden;width:' + (Device.getDeviceWidth()-16) + 'px;overflow-x:hidden;overflow-y:hidden;" scrolling="no" name="CustomeReview" src ="' + url + '"></iframe>';
		html += '</div>';
		html += '</div>';
		return html;
	};
	
	this.getFeatures = function() {
		var item = this.item;
		var array = getArray(item, ["ItemAttributes", "Feature"]);
		var html = "";
		if(array == null || array.length == 0)
			return html;
		
		html += "<h4>Features:</h4>";
		html += "<ul>";
		for(var i=0; i<array.length; i++) {
			html += "<li>" + array[i] + "</li>";
		}
		html += "</ul>";
		return html;
	};
	
	this.getDescription = function() {
		var item = this.item;
		var array = getArray(item, ["EditorialReviews", "EditorialReview"]);
		var html = "";
		if(array == null || array.length == 0)
			return html;
		
		//html += "<h4>Editorial Review:</h4>";
		html += "<a href='" + item.LargeImage.URL + "'><img align='right' src='" + item.MediumImage.URL + "'/></a>";
		for(var i=0; i<array.length; i++) {
			html += array[i].Content;
		}
		return html;
	};
	
	this.getCategories = function() {
		var item = this.item;
		var categories = getArray(item, ["BrowseNodes", "BrowseNode"]);
		var html = "";
		html += "<div style='clear:both;'><b>In categories</b></div>";
		for(var i=0; i<categories.length; i++) {
			html += "<div style='float:left;padding:4px;margin:0 10px 0 0;'><a href='/shopping/amazon/node?id=" + categories[i].BrowseNodeId + "'>" + categories[i].Name + "</a></div>";
		}
		html += "<div style='clear:both;height:1px;'>&nbsp;</div>";
		return html;
	}
}

$(document).ready(function() {
	var asin = '${it.asin}';
	var item = getAmazonItem(asin)
	item = getArray(item, ["Items", "Item"]);
	if(item == null) {
		$('#item-content').html("Invalid item");
	}
	else {
		var itemDetail = new ItemDetail(item[0]);
		$('#item-content').html(itemDetail.getHtml());
	}
	
	$("#i-need").button().click(function() {
		window.location = '/m/shopping/amazon/need/select-location/index.jsp.oo?asin=${it.asin}&sellers=' + sellers;
	});
	
	$("#i-have").button().click(function() {
		window.location = '/m/shopping/amazon/have/select-location/index.jsp.oo?asin=${it.asin}';
	});
});

function Star() {
    this.ranks = new Array();
}

Star.prototype.add = function(rank, num) {
    this.ranks[rank] = Number(num);
}

Star.prototype.toString = function() {
    return this.totalReviews() + ":" + this.ranks[0] + "," + this.ranks[1] + "," + this.ranks[2] + "," + this.ranks[3] + "," + this.ranks[4];
}

Star.prototype.totalReviews = function() {
    return this.ranks[0] + this.ranks[1] + this.ranks[2] + this.ranks[3] + this.ranks[4];
}

Star.prototype.toHtml = function() {
    var html = "<table style='width:100%;border:solid 1px;' onclick='Device.showDialog()'>";
    html += "<tr><td align='center'>Star</td><td align='center'>5</td><td align='center'>4</td><td align='center'>3</td><td align='center'>2</td><td align='center'>1</td><td align='center'>Total</td></tr>";
    html += "<tr><td align='center'>Reviews #</td><td align='center'>" + this.ranks[4] + "</td><td align='center'>" + this.ranks[3] + "</td><td align='center'>" + this.ranks[2] + "</td><td align='center'>" + this.ranks[1] + "</td><td align='center'>" + this.ranks[0] + "</td><td align='center'>" + this.totalReviews() + "</td></tr>";
    html += "</table>";
    return html;
}

function dialogPageLoaded() {
    var html = Device.getDialogHtml();
    var pos = html.indexOf('<div class="crIFrameHeaderHistogram">');
    if(pos == -1) 
        return;
    var pos2 = html.indexOf('<div class="crIframeReviewList">');
    if(pos2 == -1)
        return;
    $('#custreviewTable').html(html.substring(pos, pos2));
    var star = new Star();
    var index = 4;
    $('td[class=tiny][align=right]').each(function(){
        var text = (this.innerHTML);
        text = text.replace("&nbsp;", "");
        text = text.replace("(", "");
        text = text.replace(")", "");
        star.add(index, text);
        index--;
    });
    $('#custreview').html(star.toHtml());
}

function onDialogPageStart(url) {
    Device.hideDialog();
}
</script>

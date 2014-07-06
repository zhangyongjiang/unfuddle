<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl" %>

<%
    String param = request.getQueryString();
	String url = "/ws/v2/store/details?format=object&" + param;
	request.getRequestDispatcher(url).include(request, response);
    
    String url2 = "/ws/v2/store/followers?var=users&format=object&" + param;
    request.getRequestDispatcher(url2).include(request, response);
    
    String url3 = "/ws/v2/store/comments?var=comments&format=object&" + param;
    request.getRequestDispatcher(url3).include(request, response);
%>

<osl:ws path="<%=url + " " + url2 + " " + url3 %>"/>
<h3>Store: ${it.storeDetails.name }</h3>

<div>
    <div style="float:left;margin:8px;">
        <o:map w="300" lng="${it.storeDetails.longitude }" h="300" lat="${it.storeDetails.latitude }"></o:map>
    </div>
    <div style="float:left;margin:8px;color:#888;">
        <div>${it.storeDetails.address }</div>
        <div>${it.storeDetails.city }, ${it.storeDetails.state}</div>
        <div>${it.storeDetails.phone}</div>
        <div> Am I Following This Store? 
		    <c:if test="${it.storeDetails.following}">
		        <span style='margin-right:16px;'><input type="radio" name="following" checked value="True">True</span>
		        <span style='margin-right:16px;'><input type="radio" name="following" value="False">False</span>
		    </c:if>
		    <c:if test="${not it.storeDetails.following}">
		        <span style='margin-right:16px;'><input type="radio" name="following" value="True">True</span>
		        <span style='margin-right:16px;'><input type="radio" name="following" checked value="False">False</span>
		    </c:if>
        </div>
        <div>
            <div style="float:left;margin:4px;padding:4px;border:solid 1px #bbb;"><a href="#offers">${fn:length(it.items)} offer(s).</a></div>
            <div style="float:left;margin:4px;padding:4px;border:solid 1px #bbb;"><a href="#followers">${fn:length(users.items)} following(s).</a></div>
            <div style="float:left;margin:4px;padding:4px;border:solid 1px #bbb;"><a href="#comments">${fn:length(comments.items)} comment(s).</a></div>
        </div>
    </div>
</div>

<o:d/>

<a name="offers"></a>
<h2>Offers</h2>
This store has ${fn:length(it.items)} offer(s).

<div id="list">
<c:forEach var="offer" items="${it.items }">
    <osl:OfferDetails offer="${offer }"></osl:OfferDetails>
</c:forEach>
</div>

<o:d/>

<a name="followers"></a>
<h2>Followers</h2>
This store has ${fn:length(users.items)} following(s).
<div id="list">
<c:forEach var="user" items="${users.items }">
    <osl:User user="${user}"/>
</c:forEach>
</div>

<o:d/>

<a name="comments"></a>
<h2>Comments</h2>
This store has ${fn:length(comments.items)} comment(s).
<a href='javascript:void(0);' onclick="comment()">comment</a>
<div id="list">
<c:forEach var="comment" items="${comments.items }">
    <div style="margin:10px;">
    ${comment.content }
    <div style="color:#bbb;font-size:0.8em;">${comment.user.firstName } ${comment.user.lastName } <o:millisecond-to-date time="${comment.updated }"></o:millisecond-to-date></div>
    </div>
</c:forEach>
</div>

<script type="text/javascript">
$(document).ready(function(){
    $('input:radio[name=following]').change(function() {
          var val = $('input:radio[name=following]:checked').val();
          var url = serverBase + "/ws/v2/store/unfollow/${it.storeDetails.id}";
          if(val == 'True') {
              url = serverBase + "/ws/v2/store/follow/${it.storeDetails.id}";
          }
            $.ajax({
                  url: url,
                  type:"POST",
                  contentType:"application/json; charset=utf-8",
                  dataType:"json",
                  complete: function(transport) {
                      }
                });
        });
});

function comment() {
    var comment = prompt("Please give your comment");
    if(!comment)
        return;
    var url = serverBase + "/ws/v2/store/comment";
    var req = {
        "storeId": '${it.storeDetails.id}',
        "content": comment
    };
    $.ajax({
          url: url,
          type:"POST",
          contentType:"application/json; charset=utf-8",
          dataType:"json",
          data: JSON.stringify(req),
          complete: function(transport) {
              if(transport.status == 200) {
                  window.location.reload();
              } else {
                  alert('Error: ' + transport.responseText);
              }
           }
        });
}
</script>

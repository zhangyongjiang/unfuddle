<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl" %>

<%
    String param = request.getQueryString();
    String url = "/ws/user/details?format=object&" + param;
    request.getRequestDispatcher(url).include(request, response);
%>

<osl:ws path="<%=url %>"/>
<h3>${it.firstName } ${it.lastName }</h3>

<c:if test="${not empty it.img }">
    <div><img style="width: 200px;" src='${it.img }'/></div>
</c:if>
    
<table>
<o:tr-label-value label="First Name" value="${it.firstName }"/>
<o:tr-label-value label="Last Name" value="${it.lastName }"/>
<o:tr-label-value label="Email" value="${it.login }"/>
<o:tr-label-value label="Zipcode" id="zipcode" value="${it.zipcode}"/>
<o:tr-label-value label="Gender" id="gender" value="${it.gender}"/>

<c:if test="${not empty me.id && me.id != it.id }">
<o:tr-label-value label="Am I Following ${it.firstName }?" >
    <c:if test="${it.myFollowing}">
        <span style='margin-right:16px;'><input type="radio" name="following" checked value="True">True</span>
        <span style='margin-right:16px;'><input type="radio" name="following" value="False">False</span>
    </c:if>
    <c:if test="${not it.myFollowing }">
        <span style='margin-right:16px;'><input type="radio" name="following" value="True">True</span>
        <span style='margin-right:16px;'><input type="radio" name="following" checked value="False">False</span>
    </c:if>
</o:tr-label-value>
</c:if>
<o:tr-label-value label="Is ${it.firstName } following me?" value="${it.myFollower }">
</o:tr-label-value>

</table>

<div style="border:solid 1px #ccc;margin:6px;padding:4px;float:left;">
    <a href='<c:url value="/user/followers/index.jsp.oo?userId="/>${it.id}'>Followers</a>
</div>
<div style="border:solid 1px #ccc;margin:6px;padding:4px;float:left;">
    <a href='<c:url value="/user/followings/index.jsp.oo?userId="/>${it.id}'>Followings</a>
</div>



<script type="text/javascript">
$(document).ready(function(){
    $('input:radio[name=following]').change(function() {
          var val = $('input:radio[name=following]:checked').val();
          var url = serverBase + "/ws/v2/user/unfollow/${it.id}";
          if(val == 'True') {
              url = serverBase + "/ws/v2/user/follow/${it.id}";
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
</script>

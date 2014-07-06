<%@page import="java.net.URLEncoder"%>
<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<h3>Change Avatar</h3>

<c:if test="${not empty me.img }">
    <img style="width:120px;" src='${me.img }'/>
</c:if>

<form id="avatarForm" onsubmit="return updateAvatar();" method="post" enctype="multipart/form-data" action='<c:url value="/ws/user/avatar"/>'>
    <div style="margin-top:16px;margin-bottom:16px;"><input type="file" id="image" name="image"/></div>
    <div><input type="submit" /></div>
</form>

<script type="text/javascript">
var processing = false;
function updateAvatar() {
    if(processing) return false;
    processing = true;
    $("#avatarForm").ajaxForm({
        success:function(data) { 
            var url = serverBase + "/user/me/index.jsp.oo";
            window.location = url;
         },
         dataType:"json"
       }).submit();    
    processing = false;
    
    return false;
}
</script>

<%@page import="com.gaoshin.beans.User"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<a href='#' onclick='Device.deleteSms()' data-role='button'>Delete</a>
<div id="messageBox">
</div>

<script type="text/javascript" >
function handleMessage() {
    while(Device.hasMessage()) {
        var content = Device.getMessage();
	    $('#messageBox').html($('#messageBox').html() + "<br/>" + content);
    }
}
</script>
<%@page import="com.gaoshin.beans.User"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

TO:
<input type="text" name="to" id="to" value="4089925287"/>
<br/>

Type:
<input type="text" name="type" id="type" value="chat"/>
<br/>

Title:
<input type="text" name="title" id="title" value="You've got msg"/>
<br/>

url:
<input type="text" name="url" id="url" value='<o:url value="/m/dating/profile/index.jsp.oo"/>'/>
<br/>

<textarea style="width:100%;height:60px;padding:16px;" name='msg' id='msg'><%= System.currentTimeMillis() %></textarea>
<a href='#' onclick='sendSms()' data-role="button">SEND SMS</a>

<script type="text/javascript" >
function sendSms() {
    Device.sendSms(
            $('#to').val(), 
            $('#type').val(), 
            $('#title').val(), 
            $('#msg').val(), 
            $('#url').val() 
    );
}
</script>
<%@page import="com.gaoshin.beans.User"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

KEY:
<input type="text" name="key" id="key" value='SERVER_BASE_URL'/>

<textarea style="width:100%;height:400px;padding:16px;scroll-y:auto;" name='conf' id='conf'>http://108.65.77.85:8080/xo</textarea>
<a href='#' onclick='setConf()' data-role="button">SET</a>
<a href='#' onclick='getConf()' data-role="button">GET</a>

<script type="text/javascript" >
function getConf() {
    alert(Device.getConf(null));
}
function setConf() {
    Device.setConf($('#key').val(), $('#conf').val());
    alert(Device.getConf($('#key').val()));
}
</script>
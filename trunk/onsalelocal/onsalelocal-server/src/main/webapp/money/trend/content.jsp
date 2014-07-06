<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl" %>

<%
    String param = request.getQueryString();
    String url = "/ws/v2/trend?format=object&" + param;
    request.getRequestDispatcher(url).include(request, response);
%>

<div class="container-fluid  wrapper"> 
 <div style="display: none;" id="ajax-loader-masonry" class="ajax-loader"></div>  
<h2 class="text-align-center">Trend</h2>
  <div class="masonry" id="masonry">

    <c:forEach var="offer" items="${it.items }">
        <osl:deal offer="${offer }"/>
    </c:forEach>

 
    <div style="visibility: hidden; height: 1px;" id="navigation">
      <ul class="pager">
        <li id="navigation-next"><a href="http://ipinpro.ericulous.com/user/genkiadmin/page/2/?view=pins">Next »</a></li>
      </ul>
    </div>
  </div>
</div>
<div id="scrolltotop"><a href="#"><i class="icon-chevron-up"></i><br />
  Top</a></div>
<div class="modal hide" id="post-lightbox" tabindex="-1" role="dialog" aria-hidden="true"></div>

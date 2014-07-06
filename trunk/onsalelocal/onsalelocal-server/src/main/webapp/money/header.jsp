<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl" %>

<noscript>
<div class="alert alert-error text-align-center">
  <h3>You need to enable Javascript.</h3>
</div>
<style>
    #masonry {
    visibility: visible !important; 
    }
    </style>
</noscript>
<div id="topmenu" class="navbar navbar-fixed-top">
  <div class="navbar-inner">
    <div class="container"> <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse"> <i class="icon-bar"></i> <i class="icon-bar"></i> <i class="icon-bar"></i> </a> 
    <a href="http://www.onsalelocal.com"><img src='<c:url value="/money/img/logo.png"/>'  class="logo"></a>
      <nav id="nav-main" class="nav-collapse" role="navigation">
        <ul id="menu-top-right" class="nav pull-right">
                  <li><a href="#"><i class="icon-camera"></i> Share</a></li>
          <li><a href="about.html"><i class="icon-info-sign"></i> About</a></li>
          <li class="hidden-desktop"><a href="http://www.ipin.gaoshin.com/wordpress/register/"><i class="icon-user"></i> Sign up</a></li>
          <li class="hidden-desktop"><a href="http://www.ipin.gaoshin.com/wordpress/login/?redirect_to=%2Fwordpress%2Flogin%2F%3Faction%3Dloggedout"><i class="icon-signin"></i> Login</a></li>
          <li class="visible-desktop" id="loginbox-wrapper"><a title="" data-original-title="" id="loginbox" data-content='
<p class="social-login-connect-with">Connect with:</p>
    <p><a rel="nofollow" href="#" ><img alt="Facebook" title="Facebook" src="img/assets/fb_login.jpg" />
                </a></p> <hr />'  aria-hidden="true"><i class="icon-signin"></i> Sign up / Login</a> </li>
        </ul>
        <ul id="menu-top-menu" class="nav">
          <li class="dropdown menu-categories"><a class="dropdown-toggle" data-toggle="dropdown" data-target="#" href="#"><i class="icon-th-large"></i> Categories <b class="caret"></b></a>
            <ul class="dropdown-menu">
            <% String caturl = "/ws/category/top-categories?format=object&var=cats";
               request.getRequestDispatcher(caturl).include(request, response);
            %>
            <c:forEach var="cat" items="${cats.items }">
              <li><a href='<c:url value="/money/search/index.jsp.oo?keywords="/>${cat.name}'>${cat.name }</a></li>
            </c:forEach>
            </ul>
          </li>
          <li>        <form class="navbar-search" method="get" id="searchform" action='<c:url value="/money/search/index.jsp.oo"/>'>
          <input class="search-query" placeholder="Search" name="keywords" id="keywords" type="text">
        </form></li>
        </ul>
        <a href="#" title="Find us on iTune store" class="topmenu-social"><i class=" icon-2x icon-apple"></i></a> <a href="#" title="Find us on Google Play" class="topmenu-social"><i class=" icon-2x  icon-android"></i></a>
      </nav>
    </div>
  </div>
</div>

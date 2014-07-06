<%@page import="java.util.Properties"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="en-US" prefix="og: http://ogp.me/ns#">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>iPin Pro | Wordpress Pinterest Clone</title>
<link rel="profile" href="http://gmpg.org/xfn/11" />
<link rel="shortcut icon" href="/favicon.ico">
<link href="<c:url value="/nd/"/>js/bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="<c:url value="/nd/"/>js/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
<link href="<c:url value="/nd/"/>js/font-awesome/css/font-awesome.css" rel="stylesheet">
<link href="<c:url value="/nd/"/>css/style.css" rel="stylesheet">

<!--[if lt IE 9]>
        <script src="http://css3-mediaqueries-js.googlecode.com/svn/trunk/css3-mediaqueries.js"></script>
        <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

<!--[if IE 7]>
      <link href="<c:url value="/nd/"/>js/font-awesome/css/font-awesome-ie7.css" rel="stylesheet">
    <![endif]-->  
</head>

<body>
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
    <div class="container"> <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse"> <i class="icon-bar"></i> <i class="icon-bar"></i> <i class="icon-bar"></i> </a> <a href="http://www.onsalelocal.com"><img src="img/logo.png" class="logo"></a>
      <nav id="nav-main" class="nav-collapse" role="navigation">
        <ul id="menu-top-right" class="nav pull-right">
        <a href="#" title="Find us on iTune store" class="topmenu-social"> <i class=" icon-2x icon-apple"></i></a><a href="#" title="Find us on Google Play" class="topmenu-social"> <i class="icon-2x icon-android"></i></a>
          <li class="visible-desktop"><a href="about.html">About</a></li>
          <li class="hidden-desktop"><a href="register.html">Register</a></li>
          <li class="hidden-desktop"><a href="#">Login</a></li>
          <li class="visible-desktop" id="loginbox-wrapper"><a id="loginbox" data-content='
<p class="social-login-connect-with">Connect with:</p>
    <p><a rel="nofollow" href="#" ><img alt="Facebook" title="Facebook" src="img/assets/fb_login.jpg" />
                </a>
                                
                    </p> <hr />' 
                             aria-hidden="true"><i class="icon-signin"></i> Register / Login</a> </li>
        </ul>
        <ul id="menu-top-menu" class="nav">
          <li class="dropdown menu-categories"><a class="dropdown-toggle" data-toggle="dropdown" data-target="#" href="#">Explore <b class="caret"></b></a>
            <ul class="dropdown-menu">
              <li><a href="#">Popular</a></li>
              <li><a href="#">Everything</a></li>
              <li><a href="#">Local services</a></li>
              <li><a href="#">Department store</a></li>
              <li><a href="#">Cloth</a></li>
              <li><a href="#">shoes</a></li>
              <li><a href="#">kids</a></li>
              <li><a href="#">Pets</a></li>
              <li><a href="#">Electronics</a></li>
              <li><a href="#">computer</a></li>
              <li><a href="#">Tablets</a></li>
            </ul>
          </li>
          <li><a href="#"><i class="icon-camera"></i> Share</a></li>
        </ul>
        <form class="navbar-search" method="get" id="searchform" action="#">
          <input type="text" class="search-query" placeholder="Search" name="s" id="s" value="">
        </form>
      </nav>
    </div>
  </div>
</div>

<jsp:include page="content.jsp.oo"></jsp:include>

<div id="scrolltotop"><a href="#"><i class="icon-chevron-up"></i><br />
    Top</a></div>
<div class="modal hide" id="post-lightbox" tabindex="-1" role="dialog" aria-hidden="true"></div>
<div class="clearfix"></div>
<script type='text/javascript' src="<c:url value="/nd/"/>js/jquery1.10.2.js"></script> 
<script type='text/javascript' src='<c:url value="/nd/"/>js/jquery-migrate.min.1.2.1.js'></script> 
<script type='text/javascript' src='<c:url value="/nd/"/>js/jquery.masonry.min.js'></script> 
<script type='text/javascript' src='<c:url value="/nd/"/>js/jquery.infinitescroll.min.js'></script> 
<script type='text/javascript' src='<c:url value="/nd/"/>js/bootstrap/js/bootstrap.min.js'></script> 
<script type='text/javascript' src='<c:url value="/nd/"/>js/bootstrap-lightbox.min.js'></script> 
<script type='text/javascript'>
/* <![CDATA[ */
var obj_ipin = {"__allitemsloaded":"All items loaded","__addanotherpin":"Add Another Pin","__addnewboard":"Add new board...","__boardalreadyexists":"Board already exists. Please try another title.","__errorpleasetryagain":"Error. Please try again.","__cancel":"Cancel","__close":"Close","__comment":"comment","__comments":"comments","__enternewboardtitle":"Enter new board title","__Forgot":"Forgot?","__incorrectusernamepassword":"Incorrect Username or Password","__invalidimagefile":"Invalid image file. Please choose a JPG\/GIF\/PNG file.","__like":"like","__likes":"likes","__Likes":"Likes","__loading":"Loading...","__Login":"Login","__onto":"onto","__or":"or","__Password":"Password","__pinit":"Pin It","__pinnedto":"Pinned to","__pleaseenteratitle":"Please enter a title","__pleaseenterbothusernameandpassword":"Please enter both username and password.","__pleaseenterurl":"Please enter url","__RegisterAccount":"Register Account","__repin":"repin","__repins":"repins","__Repins":"Repins","__repinnedto":"Repinned to","__seethispin":"See This Pin","__sorryunbaletofindanypinnableitems":"Sorry, unable to find any pinnable items.","__Username":"Username","__Video":"Video","__Welcome":"Welcome","__yourpinispendingreview":"Your pin is pending review","ajaxurl":"http:\/\/ipinpro.ericulous.com\/wp-admin\/admin-ajax.php","avatar30":"<img alt='' src='http:\/\/0.gravatar.com\/avatar\/ad516503a11cd5ca435acc9bb6523536?s=30' class='avatar avatar-30 photo avatar-default' height='30' width='30' \/>","avatar48":"<img alt='' src='http:\/\/0.gravatar.com\/avatar\/ad516503a11cd5ca435acc9bb6523536?s=48' class='avatar avatar-48 photo avatar-default' height='48' width='48' \/>","blogname":"iPin Pro","categories":"<select name='board-add-new-category' id='board-add-new-category' class='postform' >\n\t<option value='-1'>Category for New Board<\/option>\n\t<option class=\"level-0\" value=\"6\">Architecture<\/option>\n\t<option class=\"level-0\" value=\"8\">Cars & Motorcycles<\/option>\n\t<option class=\"level-0\" value=\"9\">Celebrities<\/option>\n\t<option class=\"level-0\" value=\"10\">Design<\/option>\n\t<option class=\"level-0\" value=\"13\">Film, Music & Books<\/option>\n\t<option class=\"level-0\" value=\"18\">Health & Fitness<\/option>\n\t<option class=\"level-0\" value=\"22\">Humour<\/option>\n\t<option class=\"level-0\" value=\"25\">Men\u2019s Fashion<\/option>\n\t<option class=\"level-0\" value=\"31\">Sports<\/option>\n\t<option class=\"level-0\" value=\"33\">Technology<\/option>\n\t<option class=\"level-0\" value=\"99\">Videos<\/option>\n\t<option class=\"level-0\" value=\"36\">Women\u2019s Fashion<\/option>\n<\/select>\n","current_date":"12 Aug 2013 7:18am","description_instructions":"<div class=\"description_instructions\">Allowed HTML tags: <span>&lt;strong> &lt;em> &lt;a> &lt;blockquote><\/span>. To add price tag, use <span>$<\/span> symbol e.g $23 or $23.45. To add tags, input <span>tags[<em>text1, text2<\/em>]<\/span><\/div>","home_url":"http:\/\/ipinpro.ericulous.com","infinitescroll":"enable","lightbox":"enable","login_url":"http:\/\/ipinpro.ericulous.com\/login\/?redirect_to=%2F","nextselector":"#navigation #navigation-next a","nonce":"0b4d362a84","site_url":"http:\/\/ipinpro.ericulous.com","stylesheet_directory_uri":"http:\/\/ipinpro.ericulous.com\/wp-content\/themes\/ipinpro","u":"0","ui":"","ul":"","user_rewrite":"user"};
/* ]]> */
</script> 
<script type='text/javascript' src='<c:url value="/nd/"/>js/ipin.custom.js'></script> 
<script type='text/javascript' src='<c:url value="/nd/"/>http://platform.twitter.com/widgets.js'></script> 
<script type='text/javascript' src='<c:url value="/nd/"/>http://assets.pinterest.com/js/pinit.js'></script> 
<script type="text/javascript">
st_go({blog:'46345223',v:'ext',post:'0'});
var load_cmc = function(){linktracker_init(46345223,0,2);};
if ( typeof addLoadEvent != 'undefined' ) addLoadEvent(load_cmc);
else load_cmc();
</script>
</body>
</html>

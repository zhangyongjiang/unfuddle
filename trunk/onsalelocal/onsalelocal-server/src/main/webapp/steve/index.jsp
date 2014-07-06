<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o"%>
<%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl"%>
<%
    request.getRequestDispatcher("/ws/user/me?format=object&var=me").include(request, response);
    String url = "/ws/v2/trend?var=trend&format=object&";
    request.getRequestDispatcher(url).include(request, response);
%>

<!DOCTYPE html>
<html prefix="og: http://ogp.me/ns#" lang="en-US">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Onsalelocal</title>
<link rel="profile" href="http://gmpg.org/xfn/11">
<link rel="shortcut icon" href="http://www.ipin.gaoshin.com/wordpress/wp-content/themes/ipinpro/favicon.ico">
<link href="js/bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="js/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
<link href="js/font-awesome/css/font-awesome.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet">

<!--[if lt IE 9]>
		<script src="http://css3-mediaqueries-js.googlecode.com/svn/trunk/css3-mediaqueries.js"></script>
		<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->

<!--[if IE 7]>
	  <link href="http://www.ipin.gaoshin.com/wordpress/wp-content/themes/ipinpro/css/font-awesome-ie7.css" rel="stylesheet">
	<![endif]-->

<script type="text/javascript">
<%String contextPath = request.getContextPath();
if(contextPath.equals("/"))
    contextPath = "";
%>
serverBase = '<%=contextPath %>';
</script>

</head>

<body data-twttr-rendered="true">
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
			<div class="container">
				<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
					<i class="icon-bar"></i>
					<i class="icon-bar"></i>
					<i class="icon-bar"></i>
				</a>

<a href="http://www.onsalelocal.com"><img src="img/logo.png"  class="logo"></a>

				<nav id="nav-main" class="nav-collapse" role="navigation">
					<ul id="menu-top-right" class="nav pull-right">
                              <li class="hidden-desktop"><a href="about.html"><i class="icon-info-sign"></i> About</a></li>
											<li class="hidden-desktop"><a href="http://www.ipin.gaoshin.com/wordpress/register/"><i class="icon-user"></i> Register</a></li>
						<li class="hidden-desktop"><a href="http://www.ipin.gaoshin.com/wordpress/login/?redirect_to=%2Fwordpress%2Flogin%2F%3Faction%3Dloggedout"><i class="icon-signin"></i> Login</a></li>
                                  <li class="visible-desktop"><a href="about.html"><i class="icon-info-sign"></i> About</a></li>
						<li class="visible-desktop" id="loginbox-wrapper"><a title="" data-original-title="" id="loginbox" data-content='
<p class="social-login-connect-with">Connect with:</p>
	<p><a rel="nofollow" href="#" ><img alt="Facebook" title="Facebook" src="img/assets/fb_login.jpg" />
				</a></p> <hr />'  aria-hidden="true"><i class="icon-signin"></i> Register / Login</a>
						</li>
										</ul>
					
					<ul id="menu-top-menu" class="nav">
                    <li class="dropdown menu-categories"><a class="dropdown-toggle" data-toggle="dropdown" data-target="#" href="#"><i class="icon-th-large"></i> Categories <b class="caret"></b></a>
<ul class="dropdown-menu">
	<li class="menu-popular"><a href="http://www.ipin.gaoshin.com/wordpress/popular/">Popular</a></li>
	<li class="menu-everything"><a href="http://www.ipin.gaoshin.com/wordpress/everything/">Everything</a></li>
	<li class="menu-cloth"><a href="http://www.ipin.gaoshin.com/wordpress/pin/category/cloth/">Cloth</a></li>
	<li class="menu-electronics"><a href="http://www.ipin.gaoshin.com/wordpress/pin/category/electronics/">Electronics</a></li>
	<li class="menu-kids"><a href="http://www.ipin.gaoshin.com/wordpress/pin/category/kids/">Kids</a></li>
	<li class="menu-local-services"><a href="http://www.ipin.gaoshin.com/wordpress/pin/category/local-services/">Local Services</a></li>
	<li class="menu-man"><a href="http://www.ipin.gaoshin.com/wordpress/pin/category/man/">Man</a></li>
	<li class="menu-shoes"><a href="http://www.ipin.gaoshin.com/wordpress/pin/category/shoes/">Shoes</a></li>
	<li class="menu-woman"><a href="http://www.ipin.gaoshin.com/wordpress/pin/category/woman/">Woman</a></li>
</ul>
</li>
<li><a href="#"><i class="icon-camera"></i> Share</a></li>
</ul>	
									
<a href="#" title="Find us on iTune store" class="topmenu-social"><i class=" icon-2x icon-apple"></i></a> 

<a href="#" title="Find us on Google Play" class="topmenu-social"><i class=" icon-2x  icon-android"></i></a>
					
					<form class="navbar-search" method="get" id="searchform" action="http://www.ipin.gaoshin.com/wordpress/">
						<input class="search-query" placeholder="Search" name="s" id="s" type="text">
					</form>
				</nav>
			</div>
		</div>
	</div>

<div class="container-fluid  wrapper">
  <div style="display: none;" id="ajax-loader-masonry" class="ajax-loader"></div>
  <div class="masonry" id="masonry">
  
<c:forEach var="offer" items="${trend.items }">
    <osl:deal offer="${offer }"></osl:deal>
</c:forEach>  
  
    <div style="visibility: hidden; height: 1px;" id="navigation">
          <ul class="pager">
<li id="navigation-next"><a href="http://ipinpro.ericulous.com/user/genkiadmin/page/2/?view=pins">Next Â»</a></li>
          </ul>
        </div>
  </div>

</div>
<div id="scrolltotop"><a href="#"><i class="icon-chevron-up"></i><br />
  Top</a></div>
<div class="modal hide" id="post-lightbox" tabindex="-1" role="dialog" aria-hidden="true"></div>
<div class="clearfix"></div>
<script src="http://thecodeplayer.com/uploads/js/jquery-1.9.1.min.js" type="text/javascript"></script>
<script type='text/javascript' src='js/jquery-migrate.min.1.2.1.js'></script> 
<script type='text/javascript' src='js/jquery.masonry.min.js'></script> 
<script type='text/javascript' src='js/jquery.infinitescroll.min.js'></script> 
<script type='text/javascript' src='js/bootstrap/js/bootstrap.min.js'></script> 
<script type='text/javascript' src='js/bootstrap-lightbox.min.js'></script> 
<script type="text/javascript">
/* <![CDATA[ */
var obj_ipin = {"__allitemsloaded":"All items loaded","__addanotherpin":"Add Another Pin","__addnewboard":"Add new board...","__boardalreadyexists":"Board already exists. Please try another title.","__errorpleasetryagain":"Error. Please try again.","__cancel":"Cancel","__close":"Close","__comment":"comment","__comments":"comments","__enternewboardtitle":"Enter new board title","__Forgot":"Forgot?","__incorrectusernamepassword":"Incorrect Username or Password","__invalidimagefile":"Invalid image file. Please choose a JPG\/GIF\/PNG file.","__like":"like","__likes":"likes","__Likes":"Likes","__loading":"Loading...","__Login":"Login","__onto":"onto","__or":"or","__Password":"Password","__pinit":"Pin It","__pinnedto":"Pinned to","__pleaseenteratitle":"Please enter a title","__pleaseenterbothusernameandpassword":"Please enter both username and password.","__pleaseenterurl":"Please enter url","__RegisterAccount":"Register Account","__repin":"repin","__repins":"repins","__Repins":"Repins","__repinnedto":"Repinned to","__seethispin":"See This Pin","__sorryunbaletofindanypinnableitems":"Sorry, unable to find any pinnable items.","__Username":"Username","__Video":"Video","__Welcome":"Welcome","__yourpinispendingreview":"Your pin is pending review","ajaxurl":"http:\/\/www.ipin.gaoshin.com\/wordpress\/wp-admin\/admin-ajax.php","avatar30":"<img alt='' src='http:\/\/0.gravatar.com\/avatar\/ad516503a11cd5ca435acc9bb6523536?s=30' class='avatar avatar-30 photo avatar-default' height='30' width='30' \/>","avatar48":"<img alt='' src='http:\/\/0.gravatar.com\/avatar\/ad516503a11cd5ca435acc9bb6523536?s=48' class='avatar avatar-48 photo avatar-default' height='48' width='48' \/>","blogname":"OSL","categories":"<select name='board-add-new-category' id='board-add-new-category' class='postform' >\n\t<option value='-1'>Category for New Board<\/option>\n\t<option class=\"level-0\" value=\"7\">Cloth<\/option>\n\t<option class=\"level-0\" value=\"9\">Electronics<\/option>\n\t<option class=\"level-0\" value=\"6\">Kids<\/option>\n\t<option class=\"level-0\" value=\"10\">Local Services<\/option>\n\t<option class=\"level-0\" value=\"4\">Man<\/option>\n\t<option class=\"level-0\" value=\"8\">Shoes<\/option>\n\t<option class=\"level-0\" value=\"5\">Woman<\/option>\n<\/select>\n","current_date":"23 Sep 2013 3:36am","description_instructions":"<div class=\"description_instructions\"> To add price tag, use <span>$<\/span> symbol e.g $23 or $23.45.<\/div>","home_url":"http:\/\/www.ipin.gaoshin.com\/wordpress","infinitescroll":"enable","lightbox":"enable","login_url":"http:\/\/www.ipin.gaoshin.com\/wordpress\/login\/?redirect_to=%2Fwordpress%2F","nextselector":"#navigation #navigation-next a","nonce":"79ee2f372b","site_url":"http:\/\/www.ipin.gaoshin.com\/wordpress","stylesheet_directory_uri":"http:\/\/www.ipin.gaoshin.com\/wordpress\/wp-content\/themes\/ipinpro","u":"1","ui":"admin","ul":"admin","user_rewrite":"user"};
/* ]]> */
</script> 
<script type="text/javascript" src="js/ipin.custom.js"></script>
</body>
</html>
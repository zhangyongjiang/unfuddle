<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl" %>

<script type='text/javascript' src='<c:url value="/money/js/jquery-1.9.1.min.js"/>'></script> 
<script type='text/javascript' src='<c:url value="/money/js/jquery-migrate.min.1.2.1.js"/>'></script> 
<script type='text/javascript' src='<c:url value="/money/js/jquery.masonry.min.js"/>'></script> 
<script type='text/javascript' src='<c:url value="/money/js/jquery.infinitescroll.min.js"/>'></script> 
<script type='text/javascript' src='<c:url value="/money/js/bootstrap/js/bootstrap.min.js"/>'></script> 
<script type='text/javascript' src='<c:url value="/money/js/bootstrap-lightbox.min.js"/>'></script> 
<script type="text/javascript">
/* <![CDATA[ */
var obj_ipin = {
		   "__allitemsloaded":"All items loaded",
		   "__addanotherpin":"Add Another Pin",
		   "__addnewboard":"Add new board...",
		   "__boardalreadyexists":"Board already exists. Please try another title.",
		   "__errorpleasetryagain":"Error. Please try again.",
		   "__cancel":"Cancel",
		   "__close":"Close",
		   "__comment":"comment",
		   "__comments":"comments",
		   "__enternewboardtitle":"Enter new board title",
		   "__Forgot":"Forgot?",
		   "__incorrectusernamepassword":"Incorrect Username or Password",
		   "__invalidimagefile":"Invalid image file. Please choose a JPG\/GIF\/PNG file.",
		   "__like":"like",
		   "__likes":"likes",
		   "__Likes":"Likes",
		   "__loading":"Loading...",
		   "__Login":"Login",
		   "__onto":"onto",
		   "__or":"or",
		   "__Password":"Password",
		   "__pinit":"Pin It",
		   "__pinnedto":"Pinned to",
		   "__pleaseenteratitle":"Please enter a title",
		   "__pleaseenterbothusernameandpassword":"Please enter both username and password.",
		   "__pleaseenterurl":"Please enter url",
		   "__RegisterAccount":"Register Account",
		   "__repin":"repin",
		   "__repins":"repins",
		   "__Repins":"Repins",
		   "__repinnedto":"Repinned to",
		   "__seethispin":"See This Pin",
		   "__sorryunbaletofindanypinnableitems":"Sorry, unable to find any pinnable items.",
		   "__Username":"Username",
		   "__Video":"Video",
		   "__Welcome":"Welcome",
		   "__yourpinispendingreview":"Your pin is pending review",
		   "ajaxurl":"http:\/\/www.ipin.gaoshin.com\/wordpress\/wp-admin\/admin-ajax.php",
		   "avatar30":"<img alt='' src='http:\/\/0.gravatar.com\/avatar\/ad516503a11cd5ca435acc9bb6523536?s=30' class='avatar avatar-30 photo avatar-default' height='30' width='30' \/>",
		   "avatar48":"<img alt='' src='http:\/\/0.gravatar.com\/avatar\/ad516503a11cd5ca435acc9bb6523536?s=48' class='avatar avatar-48 photo avatar-default' height='48' width='48' \/>",
		   "blogname":"OSL",
		   "categories":"<select name='board-add-new-category' id='board-add-new-category' class='postform' >\n\t<option value='-1'>Category for New Board<\/option>\n\t<option class=\"level-0\" value=\"7\">Cloth<\/option>\n\t<option class=\"level-0\" value=\"9\">Electronics<\/option>\n\t<option class=\"level-0\" value=\"6\">Kids<\/option>\n\t<option class=\"level-0\" value=\"10\">Local Services<\/option>\n\t<option class=\"level-0\" value=\"4\">Man<\/option>\n\t<option class=\"level-0\" value=\"8\">Shoes<\/option>\n\t<option class=\"level-0\" value=\"5\">Woman<\/option>\n<\/select>\n",
		   "current_date":"23 Sep 2013 3:36am",
		   "description_instructions":"<div class=\"description_instructions\"> To add price tag, use <span>$<\/span> symbol e.g $23 or $23.45.<\/div>",
		   "home_url":"http:\/\/www.ipin.gaoshin.com\/wordpress",
		   "infinitescroll":"enable",
		   "lightbox":"enable",
		   "login_url":"http:\/\/www.ipin.gaoshin.com\/wordpress\/login\/?redirect_to=%2Fwordpress%2F",
		   "nextselector":"#navigation #navigation-next a",
		   "nonce":"79ee2f372b",
		   "site_url":"http:\/\/www.ipin.gaoshin.com\/wordpress",
		   "stylesheet_directory_uri":"http:\/\/www.ipin.gaoshin.com\/wordpress\/wp-content\/themes\/ipinpro",
		   "u":"1",
		   "ui":"admin",
		   "ul":"admin",
		   "user_rewrite":"user"
		};
/* ]]> */
</script> 
<script type="text/javascript" src='<c:url value="/money/js/ipin.custom.js"/>'></script>

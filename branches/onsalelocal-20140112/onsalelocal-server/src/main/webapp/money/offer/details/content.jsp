<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/osl" prefix="osl" %>

<%
    String param = request.getQueryString();
    String url = "/ws/v2/offer/details?format=object&" + (param == null ? "" : param);;
    request.getRequestDispatcher(url).include(request, response);
    
    String url2 = "/ws/v2/offer/comments?var=comments&format=object&" + (param == null ? "" : param);;
    request.getRequestDispatcher(url2).include(request, response);
%>

<div class="container">
  <div class="row post">
        <div class="span7 offset1 box-wrapper">
        <c:if test="${not empty it.submitter.id }">
        <div class="author">
              <div class="pull-left"> <a href="#"> <img alt="" src="${it.submitter.img }" class="avatar"> </a> </div>
              <div class="post-author-content">
                <button class="btn follow pull-right" type="button"><i class="icon-plus"></i> Follow</button>
                <a href="http://ipinpro.ericulous.com/user/demo/">
                <div class="post-author">${it.submitter.firstName } ${it.submitter.lastName }</div>
                </a> Shared <o:millisecond-to-date time="${offer.created }"></o:millisecond-to-date> </div>
          </div>
          </c:if>
            
          <div class="post-title">${it.title }</div>  
  
            
            
             <div class="store-logo text-align-center"><a href="#"> ${it.merchant } </a></div>
  
            <div class="post-featured-photo">
                            <div class="post-actionbar"><a class="btn btn-large" data-post_id="1" href="#"><i class="icon-thumbs-up"></i></a></div>
            
            <img class="featured-thumb" alt="PSY – GANGNAM STYLE (강남스타일) M/V " src="${it.largeImg }">
      
            </div>
                <div class="expire expire-soon text-align-center">Expire: <o:millisecond-to-date time="${it.end }"></o:millisecond-to-date></div>
                                <div class="meta text-align-center"> <span><i class="icon-thumbs-up"></i> ${it.likes } Likes</span> <span><i class="icon-comment"></i> ${it.comments } Comments</span></div> 
                
                
          <div class="post-share-horizontal text-align-center">
               <iframe src="img/like_002.htm" style="border:none; overflow:hidden; width:75px; height:21px;" allowtransparency="true" frameborder="0" scrolling="no"></iframe>
               
            <script>(function() {var po=document.createElement('script');po.type='text/javascript';po.async=true;po.src='https://apis.google.com/js/plusone.js';var s=document.getElementsByTagName('script')[0];s.parentNode.insertBefore(po,s);})();</script> 
             <a data-pin-config="beside" data-pin-log="button_pinit" target="_blank" class="PIN_1376281359689_pin_it_button PIN_1376281359689_pin_it_button_inline PIN_1376281359689_pin_it_beside" href="http://pinterest.com/pin/create/button/?url=http%3A%2F%2Fipinpro.ericulous.com%2Fpin%2F771%2F&amp;media=http%3A%2F%2Fipinpro.ericulous.com%2Fwp-content%2Fuploads%2F2012%2F11%2F1358026730ad23b.jpg&amp;guid=satZTWbAsS6v&amp;description=Social%20Media%20Revolution%202010%20%E2%80%93%20YouTube"><span id="PIN_1376281359689_pin_count_1" class="PIN_1376281359689_hidden"></span></a> <a id="post-embed" class="btn btn-mini"><strong>&lt;&gt; Embed</strong></a> <a id="post-email" class="btn btn-mini"><strong>@&nbsp; Email</strong></a> 
                
             <a id="post-report" class="btn btn-mini"><strong><i class="icon-flag"></i> Report</strong></a>
</div>                               

     <div class="shop-now hidden-desktop">
      <div class="post-overview">
<h4>Overview</h4>
<p class="post-discription">
${it.description }
</p> 
</div>
       <div class="post-shopnow">
           <div class="post-price">
<h1><span class="currency">${it.price }</span></h1>
           <div class="discount">${it.discount }</div>
           </div>
     <button class="btn btn-danger btn-large pull-right" style="margin-top:20px;"><i class="icon-2x icon-shopping-cart"></i> Shop Now</button>
               <div class="clearfix"></div> 
     </div>

     </div> 

            <div class="post-comments">

              <div class="post-comments-wrapper">            <h4>Comments</h4>
                <c:if test="${empty comments.items }">No comment yet.</c:if>
                <div id="comments">
                  <ol class="commentlist">
                    <c:forEach var="comment" items="${comments.items }">
                    <li class="comment" id="comment-${comment.id }">
                      <div class="comment-avatar"> 
                        <a href='<c:url value="/money/user/details/index.jsp.oo?userId="/>${comment.user.id}'> 
                            <img alt="" src="${comment.user.img }" class="avatar"> </a> 
                      </div>
                     
                      <div class="comment-content"> <strong><span class="comment"> 
                        <a class="url" href='<c:url value="/money/user/details/index.jsp.oo?userId="/>${comment.user.id}'>
                            ${comment.user.firstName} ${comment.user.lastName}</a> </span></strong> / <o:millisecond-to-date time="${comment.created }"></o:millisecond-to-date>
                        <p>${comment.content }</p>
                      </div>
                    </li>
                    </c:forEach>
                    
                  </ol>
                  <form action="" method="post" id="commentform">
                        <div class="pull-left"><img alt="avatar" src='<c:url value="/money/img/a.jpg"/>' height="48" width="48"></div>
                    <c:if test="${empty me.id }">
                        <div class="textarea-wrapper">
                            <textarea disabled="disabled" placeholder="Login to comment..."></textarea>
                            <a class="btn" href="http://ipinpro.ericulous.com/login/?redirect_to=http%3A%2F%2Fipinpro.ericulous.com%2Fpin%2F771%2F"><strong>Login</strong></a>
                        </div>
                    </c:if>
                    <c:if test="${not empty me.id }">
                        <div class="textarea-wrapper">
                            <textarea disabled="disabled" placeholder="type comment here..."></textarea>
                            <a class="btn" href="http://ipinpro.ericulous.com/login/?redirect_to=http%3A%2F%2Fipinpro.ericulous.com%2Fpin%2F771%2F"><strong>Submit</strong></a>
                        </div>
                    </c:if>
                  </form>
                </div>
              </div>
            </div>

 
          <div class="post-likes">
              <div class="post-likes-wrapper">
                <h4>Likes</h4>
                <div class="post-likes-avatar"> <a id="likes-99" href="http://ipinpro.ericulous.com/user/morgane_yatta/" rel="tooltip" title="Morgane Yatta"> <img alt="" src="img/ad516503a11cd5ca435acc9bb6523536.png" class="avatar"> </a> <a id="likes-133" href="http://ipinpro.ericulous.com/user/do_u_mazu/" rel="tooltip" title="Do U Mazu"> <img alt="avatar" src="img/women-48x48.jpg" class="avatar" height="48" width="48"> </a> <a id="likes-131" href="http://ipinpro.ericulous.com/user/sofiane_kamelanc/" rel="tooltip" title="Sofiane Kamelanc"> <img alt="" src="img/ad516503a11cd5ca435acc9bb6523536.png" class="avatar"> </a> <a id="likes-148" href="http://ipinpro.ericulous.com/user/techs_latest/" rel="tooltip" title="Techs Latest"> <img alt="" src="img/ad516503a11cd5ca435acc9bb6523536.png" class="avatar"> </a> <a id="likes-171" href="http://ipinpro.ericulous.com/user/sdm000/" rel="tooltip" title="sdm000"> <img alt="avatar" src="img/00000-48x48.gif" class="avatar" height="48" width="48"> </a> <a id="likes-265" href="http://ipinpro.ericulous.com/user/jcho91/" rel="tooltip" title="jcho91"> <img alt="" src="img/ad516503a11cd5ca435acc9bb6523536.png" class="avatar"> </a> <a id="likes-283" href="http://ipinpro.ericulous.com/user/geo_lee/" rel="tooltip" title="Geo Lee"> <img alt="" src="img/ad516503a11cd5ca435acc9bb6523536_002.png" class="avatar"> </a> <a id="likes-317" href="http://ipinpro.ericulous.com/user/josh_elledge/" rel="tooltip" title="ggggg"> <img alt="" src="img/ad516503a11cd5ca435acc9bb6523536.png" class="avatar"> </a> <a id="likes-481" href="http://ipinpro.ericulous.com/user/kevinprecy/" rel="tooltip" title="KevinPrecy"> <img alt="" src="img/ad516503a11cd5ca435acc9bb6523536_002.png" class="avatar"> </a> <a id="likes-543" href="http://ipinpro.ericulous.com/user/biswajit_das/" rel="tooltip" title="Biswajit Das"> <img alt="" src="img/ad516503a11cd5ca435acc9bb6523536_002.png" class="avatar"> </a> <a id="likes-664" href="http://ipinpro.ericulous.com/user/manoj_aher/" rel="tooltip" title="Manoj Aher"> <img alt="" src="img/ad516503a11cd5ca435acc9bb6523536_002.png" class="avatar"> </a> <a id="likes-719" href="http://ipinpro.ericulous.com/user/karunabeckmann/" rel="tooltip" title="karunabeckmann"> <img alt="" src="img/ad516503a11cd5ca435acc9bb6523536_002.png" class="avatar"> </a> </div>
                <p><a href="#">more likes...</a></p>
            </div><div class="clearfix"></div>
            </div>
<div id="post-fullsize" class="lightbox hide" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="lightbox-header">
                <button type="button" class="close" id="post-fullsize-close" aria-hidden="true">×</button>
        </div>
              <div class="lightbox-content"> <img src="img/1358026730ad23b.jpg"> </div>
          </div>
            <div class="modal hide" id="post-embed-box" tabindex="-1" role="dialog" aria-hidden="true">
              <div class="modal-header">
                <button id="post-embed-close" type="button" class="close" aria-hidden="true">x</button>
                <h3>Embed Pin on Your Blog</h3>
              </div>
              <div class="modal-footer">
                <input id="embed-width" value="480" type="text">
                <span class="help-inline"> px -Image Width</span>
                <input id="embed-height" value="360" type="text">
                <span class="help-inline"> px -Image Height</span>
                <textarea>&lt;div style='padding-bottom: 
2px;line-height:0px;'&gt;&lt;a 
href='http://ipinpro.ericulous.com/pin/771/' target='_blank'&gt;&lt;img 
src='http://ipinpro.ericulous.com/wp-content/uploads/2012/11/1358026730ad23b.jpg'
 border='0' width='480' height='360' /&gt;&lt;/a&gt;&lt;/div&gt;&lt;div 
style='float:left;padding-top:0px;padding-bottom:0px;'&gt;&lt;p 
style='font-size:10px;color:#76838b;'&gt;Source: &lt;a 
style='text-decoration:underline;font-size:10px;color:#76838b;' 
href='http://www.youtube.com/watch?v=NB_P-_NUdLw'&gt;www.youtube.com&lt;/a&gt;
 via &lt;a 
style='text-decoration:underline;font-size:10px;color:#76838b;' 
href='http://ipinpro.ericulous.com/user/demo' 
target='_blank'&gt;demo&lt;/a&gt; on &lt;a 
style='text-decoration:underline;color:#76838b;' 
href='http://ipinpro.ericulous.com/' target='_blank'&gt;iPin 
Pro&lt;/a&gt;&lt;/p&gt;&lt;/div&gt;</textarea>
              </div>
            </div>
            <div class="modal hide" id="post-email-box" tabindex="-1" role="dialog" aria-hidden="true">
              <div class="modal-header">
                <button id="post-email-close" type="button" class="close" aria-hidden="true">x</button>
                <h3>Email This Pin</h3>
              </div>
              <div class="modal-footer">
                <input id="recipient-name" type="text">
                <span class="help-inline"> Recipient Name</span>
                <input id="recipient-email" type="email">
                <span class="help-inline"> Recipient Email</span>
                <input id="email-post-id" value="771" type="hidden">
                <textarea placeholder="Message (optional)"></textarea>
                <input class="btn btn-primary" disabled="disabled" value="Send Email" id="post-email-submit" name="post-email-submit" type="submit">
                <div class="ajax-loader-email-pin ajax-loader hide"></div>
              </div>
            </div>
            <div class="modal hide" id="post-report-box" tabindex="-1" role="dialog" aria-hidden="true">
              <div class="modal-header">
                <button id="post-report-close" type="button" class="close" aria-hidden="true">x</button>
                <h3>Report This Pin</h3>
              </div>
              <div class="modal-footer">
                <input id="report-post-id" value="771" type="hidden">
                <textarea placeholder="Please write a little about why you want to report this pin."></textarea>
                <input class="btn btn-primary" disabled="disabled" value="Report Pin" id="post-report-submit" name="post-report-submit" type="submit">
                <div class="ajax-loader-report-pin ajax-loader hide"></div>
              </div>
            </div>
    </div>

    <div class="span3 visible-desktop box-wrapper">    
 <div class="post-overview">
<h4>Overview</h4>
<p class="post-discription">Features: High quality sticker, solid and shinning surface Vinyl Skin Material Suitable for: Xbox 360 Fat Package content: 1 Set</p> 
</div>
<div class="post-offer">
<h4>Your Offer</h4>
<h1><span class="currency">$</span>299<span class="currency">99</span></h1>
<div class="discount">50% off</div>
<br>
<button class="btn btn-danger btn-large"><i class="icon-2x icon-shopping-cart"></i> Shop Now</button>
</div>
 
    </div>
    
    
    
    </div>  </div>

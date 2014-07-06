<%@ page contentType="text/html; charset=utf-8" %>
<%@ page isErrorPage="true" import="java.io.*" %>
<%@page import="java.util.Enumeration"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>
<%@page import="common.web.UserAgentTools"%>

<html>
<head>
	<script type="text/javascript">
	
	  var _gaq = _gaq || [];
	  _gaq.push(['_setAccount', 'UA-21767018-1']);
	  _gaq.push(['_trackPageview']);
	
	  (function() {
	    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
	    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
	    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
	  })();
	
	</script>
</head>
<body>

<h3>Oops</h3>

Something must be wrong. Please report the error. Sorry for inconvenience caused.

<pre>
<%
if(exception != null) {
	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);
	exception.printStackTrace(pw);
	out.print(sw);
	sw.close();
	pw.close();
}
%>
</pre>

<%
Enumeration hnames = request.getHeaderNames();
String userAgent = null;
while(hnames.hasMoreElements()) {
    String name = (String)hnames.nextElement();
    String value = request.getHeader(name);
    out.write(name + ": " + value + "<br/>\n");
    if(name.equals("User-Agent"))
        userAgent = value;
}
if(userAgent!=null) {
    String[] os = UserAgentTools.getOS(userAgent);
    for(String s : os) {
    	out.write(s + ", ");
    }
    out.write("<br/>\n");
    
    String[] browser = UserAgentTools.getBrowser(userAgent);
    for(String s : browser) {
    	out.write(s + ", ");
    }
    out.write("<br/>\n");
}
%>

</body>
</html>



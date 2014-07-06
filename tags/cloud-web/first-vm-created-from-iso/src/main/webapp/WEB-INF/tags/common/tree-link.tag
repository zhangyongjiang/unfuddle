<%@tag import="java.util.ArrayList"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" 
%><%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" 
%><%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" 
%><%@ attribute name="path" required="true"
%><%@ attribute name="link" required="true"
%><%
String ctxPath = application.getContextPath();
String icon = null;
if(ctxPath.equals("/")) {
	icon = "/images/arrow_right_20.gif";
}
else {
	icon = ctxPath + "/images/right_arrow_16.gif";
}
String imgTag = "<img style='margin:3px 6px 3px 6px;' src='" + icon + "'/>";
if(link.startsWith("//")) {
    link = link.substring(1);
}
else if (link.startsWith("http")) {
}
else if (link.startsWith("/")) {
    link = ctxPath + link;
}
String[] items = path.split("/");
out.write("<div>");
for(int i=0; i<items.length; i++) { 
        StringBuilder sb = new StringBuilder();
        for(int j=0; j<i; j++) {
                sb.append(items[j]).append("/");
        }
        sb.append(items[i]);
        String subpath = sb.toString();
        if(subpath.length()==0) {
            subpath = "/";
        }
        String href = link.replaceAll("__PATH__", subpath);
        String caption = items[i];
        if(caption.length() == 0)
                caption = "ROOT";
        if(i!=(items.length-1)) 
                out.write("<a style='text-decoration:none;' href='" + href + "'>" + caption + "</a>" + imgTag);
        else
                out.write(caption);
}
out.write("</div>");
%>
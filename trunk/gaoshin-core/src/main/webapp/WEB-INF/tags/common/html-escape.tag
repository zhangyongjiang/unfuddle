<%@tag import="java.text.SimpleDateFormat"
%><%@tag import="java.util.Date"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="value" required="true" 
%><%@ attribute name="paragraph" required="false" type="java.lang.Boolean" 
%><%
if(value != null) {
    value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    if(java.lang.Boolean.TRUE.equals(paragraph)) {
        String[] paras = value.split("[\n\r]+");
        for(String s : paras) {
            out.write("<p>");
            out.write(s);
            out.write("</p>");
        }
    } else {
        out.write(value);
    }
}
%>
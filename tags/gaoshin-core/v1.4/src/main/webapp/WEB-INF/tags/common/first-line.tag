<%@tag import="java.text.SimpleDateFormat"%>
<%@tag import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="str" required="true" 
%><%
if(str != null) {
    int pos = str.indexOf('\n');
    if(pos == -1)
        out.write(str);
    else
        out.write(str.substring(0, pos));
}
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="str" required="false" 
%><%
    if(str.startsWith("http")) {
        out.write("<a href='" + str + "'>" + str + "</a>");
    }
    else if(str.endsWith(".com") || str.endsWith(".net") || str.endsWith(".org")) {
        out.write("<a href='http://" + str + "'>" + str + "</a>");
    }
    else {
        out.write(str);
    }
%>
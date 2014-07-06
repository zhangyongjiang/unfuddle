<%@tag import="java.text.SimpleDateFormat"%><%@tag
	import="java.util.Date"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@ attribute
	name="time" required="true" type="java.lang.Long"%><%@ attribute
	name="format" required="false"%><%
	Date date = new Date(time);
	if (format == null) {
//        format = "yyyy/MM/dd HH:mm:ss";
        format = "yyyy/MM/dd";
	}
	SimpleDateFormat sdf = new SimpleDateFormat(format);
	String str = sdf.format(date);
	out.write(str);
%>
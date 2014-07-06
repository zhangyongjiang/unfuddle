<%@tag import="java.util.HashMap"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="name" required="true" 
%><%
Cookie[] cookies = request.getCookies();
HashMap<String, String> cookieMap = new HashMap<String, String>();
if(cookies!=null) {
	for(Cookie c : cookies) {
		String key = c.getName();
		String value = c.getValue();
		cookieMap.put(key, value);
	}
}

Object value = request.getParameter(name);
if(value != null) {
	out.write(value.toString());
	return;
}

value = request.getAttribute(name);
if(value != null) {
	out.write(value.toString());
	return;
}

value = session.getAttribute(name);
if(value != null) {
	out.write(value.toString());
	return;
}

if(cookieMap.containsKey(name)) {
	value = cookieMap.get(name);
	out.write(value.toString());
	return;
}

value = application.getAttribute(name);
if(value != null) {
	out.write(value.toString());
	return;
}
%>
<%@ attribute name="value" required="true" 
%><% 
String requestURL = request.getRequestURL().toString();
String requestURI = request.getRequestURI();
String baseURL = requestURL.substring(0, requestURL.indexOf(requestURI));
String contextPath = request.getContextPath();
out.write(baseURL + contextPath + value);
%>
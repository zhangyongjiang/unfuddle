<%@tag import="common.geo.Geocode"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="lat0" required="true" 
%><%@ attribute name="lng0" required="true"  
%><%@ attribute name="lat1" required="true"  
%><%@ attribute name="lng1" required="true"  
%><%
try {
	float dis = Geocode.distance(Float.parseFloat(lat1), Float.parseFloat(lng1), Float.parseFloat(lat0), Float.parseFloat(lng0));
	String miles = String.format("%.2f", dis);
	out.write(miles);
	out.write(" miles");
} catch (Exception e){}
%>
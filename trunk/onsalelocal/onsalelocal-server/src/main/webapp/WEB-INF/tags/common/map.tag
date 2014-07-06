<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="lat" required="true" 
%><%@ attribute name="lng" required="true" 
%><%@ attribute name="w" required="true" 
%><%@ attribute name="h" required="true" 
%><img src="http://maps.googleapis.com/maps/api/staticmap?center=${lat },${lng }&zoom=15&size=${w }x${h }&maptype=roadmap&markers=color:red%7Ccolor:red%7Clabel:o%7C${lat },${lng }&sensor=false"/>
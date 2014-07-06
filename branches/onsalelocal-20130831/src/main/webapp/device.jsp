<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>


<%String contextPath = request.getContextPath();
if(contextPath.equals("/"))
    contextPath = "";
%>
serverBase = '<%=contextPath %>';

if (typeof Device === 'undefined') {
	Device = {
		getClientType: function() {
			return "browser";
		},
		getDeviceWidth: function() {
			return 0;
		},
		getDeviceHeight: function() {
			return 0;
		},
		getSavedLatitude: function() {
			return 0;
		},
		getSavedLongitude: function() {
			return 0;
		},
		getLatitude: function() {
			return 0;
		},
		getLongitude: function() {
			return 0;
		},
	};
}


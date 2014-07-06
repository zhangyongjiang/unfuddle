<%@tag import="java.text.SimpleDateFormat"%>
<%@tag import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="time" required="true" type="java.lang.Long" 
%><%
	if(time > 0) {
	    long day = time / (24 * 3600000);
	    long remain = time % (24 * 3600000);
	    if(day == 1) {
	        out.write("1 day ");
	    }
	    if(day > 1) {
	        out.write(day + " days ");
	    }
	    
	    long hour = remain /  3600000;
	    remain = remain % 3600000;
	    if(hour == 1) {
	        out.write("1 hour ");
	    }
	    if(hour > 1) {
	        out.write(hour + " hours ");
	    }
	    
	    long minute = remain /  60000;
	    remain = remain % 60000;
	    if(minute == 1) {
	        out.write("1 minute ");
	    }
	    if(minute > 1) {
	        out.write(minute + " minutes ");
	    }
	    
	    long second = remain /  1000;
	    if(second == 1) {
	        out.write("1 second ");
	    }
	    if(second > 1) {
	        out.write(second + " seconds ");
	    }
	}
%>
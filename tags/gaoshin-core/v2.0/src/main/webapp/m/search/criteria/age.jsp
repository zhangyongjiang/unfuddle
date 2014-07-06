<%@page import="java.util.Calendar"%>
<%@page import="com.gaoshin.dating.SearchCriteria"%>
<%@page import="com.gaoshin.dating.DimensionRace"%>
<%@page import="com.gaoshin.beans.User"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<% SearchCriteria criteria = (SearchCriteria)request.getAttribute("criteria"); %>
<h4>Age</h4>
<% 
	Calendar cal = Calendar.getInstance();
	int thisYear = cal.get(Calendar.YEAR) - 18;
	for(int i=0; i<60; i++) {
	    thisYear --;
	    String color = "";
	    if(criteria.getMinBirthYear() != null && criteria.getMaxBirthYear()!=null && criteria.getMinBirthYear() <= thisYear && thisYear <= criteria.getMaxBirthYear()) {
	        color = "background-color:#afa;";
	    }
%>
<div onclick='change(<%=thisYear %>)' style="<%=color%>float:left;width:48px;padding:5px;margin:1px;border:solid 1px #ccc;text-align:center;"><%= thisYear %></div>
<%	    
	}
%>

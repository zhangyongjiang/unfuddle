<%@page import="com.gaoshin.beans.User"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<div  style="clear:both;">
<ul data-role="listview">
<li>
	<div style='float:left;font-size:0.9em;color:#666;width:118px;'>Gender</div>${profile.gender}
</li>
<li>
	<div style='float:left;font-size:0.9em;color:#666;width:118px;'>Age</div>${profile.age} years old
</li>
<li>
	<div style='float:left;font-size:0.9em;color:#666;width:118px;'>Education</div><c:if test="${not empty profile.education}">${profile.education.label}</c:if><c:if test="${empty profile.education}">-</c:if>
</li>
<li>
	<div style='float:left;font-size:0.9em;color:#666;width:118px;'>Height</div><c:if test="${not empty profile.height && profile.height != 0}">${profile.height} cm</c:if><c:if test="${empty profile.height || profile.height == 0}">-</c:if>
</li>
<li>
	<div style='float:left;font-size:0.9em;color:#666;width:118px;'>Weight</div><c:if test="${not empty profile.weight && profile.weight!=0}">${profile.weight} kg</c:if><c:if test="${empty profile.weight || profile.weight == 0}">-</c:if>
</li>
<li>
	<div style='float:left;font-size:0.9em;color:#666;width:118px;'>Looking for</div><c:if test="${not empty profile.lookingFor }">${profile.lookingFor.label}</c:if><c:if test="${empty profile.lookingFor }">-</c:if>
</li>
<li>
	<div style='float:left;font-size:0.9em;color:#666;width:118px;'>Interest-1</div><c:if test="${not empty profile.interest0}">${profile.interest0}</c:if><c:if test="${empty profile.interest0}">-</c:if>
</li>
<li>
	<div style='float:left;font-size:0.9em;color:#666;width:118px;'>Interest-2</div><c:if test="${not empty profile.interest1}">${profile.interest1}</c:if><c:if test="${empty profile.interest1}">-</c:if>
</li>
<li>
	<div style='float:left;font-size:0.9em;color:#666;width:118px;'>Interest-3</div><c:if test="${not empty profile.interest2}">${profile.interest2}</c:if><c:if test="${empty profile.interest2}">-</c:if>
</li>
<li>
	<div style='float:left;font-size:0.9em;color:#666;width:118px;'>Ethnicity</div><c:if test="${not empty profile.race}">${profile.race.label}</c:if><c:if test="${empty profile.race}">-</c:if>
</li>
<li>
	<div style='float:left;font-size:0.9em;color:#666;width:118px;'>Occupation</div><c:if test="${not empty profile.job}">${profile.job.label}</c:if><c:if test="${empty profile.job}">-</c:if>
</li>
<li>
	<div style='float:left;font-size:0.9em;color:#666;width:118px;'>Income</div><c:if test="${not empty profile.income}">${profile.income.label}</c:if><c:if test="${empty profile.income}">-</c:if>
</li>
<li>
	<div style='float:left;font-size:0.9em;color:#666;width:118px;'>Smoking?</div><c:if test="${not empty profile.smoking}">${profile.smoking.label}</c:if><c:if test="${empty profile.smoking}">-</c:if>
</li>
<li>
	<div style='float:left;font-size:0.9em;color:#666;width:118px;'>Drinking?</div><c:if test="${not empty profile.drinking}">${profile.drinking.label}</c:if><c:if test="${empty profile.drinking}">-</c:if>
</li>
<li>
	<div style='float:left;font-size:0.9em;color:#666;width:118px;'>Language-1</div><c:if test="${not empty profile.language0}">${profile.language0.label}</c:if><c:if test="${empty profile.language0}">-</c:if>
</li>
<li>
	<div style='float:left;font-size:0.9em;color:#666;width:118px;'>Language-2</div><c:if test="${not empty profile.language1}">${profile.language1.label}</c:if><c:if test="${empty profile.language1}">-</c:if>
</li>
<li>
	<div style='float:left;font-size:0.9em;color:#666;width:118px;'>Like Kids?</div><c:if test="${not empty profile.likekids}">${profile.likekids.label}</c:if><c:if test="${empty profile.likekids}">-</c:if>
</li>
<li>
	<div style='float:left;font-size:0.9em;color:#666;width:118px;'>Kids I Have</div><c:if test="${not empty profile.kids}">${profile.kids}</c:if><c:if test="${empty profile.kids}">-</c:if>
</li>
<li>
	<div style='float:left;font-size:0.9em;color:#666;width:118px;'>Like Pets?</div><c:if test="${not empty profile.pets}">${profile.pets.label}</c:if><c:if test="${empty profile.pets}">-</c:if>
</li>
<li>
	<div style='float:left;font-size:0.9em;color:#666;width:118px;'>Married Before?</div><c:if test="${not empty profile.marriage}">${profile.marriage.label}</c:if><c:if test="${empty profile.marriage}">-</c:if>
</li>
</ul>
</div>


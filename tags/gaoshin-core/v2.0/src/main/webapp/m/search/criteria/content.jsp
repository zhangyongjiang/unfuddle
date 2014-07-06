<%@page import="com.gaoshin.beans.Gender"%>
<%@page import="com.gaoshin.dating.DimensionIncome"%>
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

<h3>Your Search Filters</h3>
<% 	
	User me = (User) request.getAttribute("me");
	String searchCriteriaUrl = "/dating/search/criteria/list?format=object&var=criterias";
	int thisYear = 0;
	long criteriaId = 0;
%>
<jsp:include page="<%=searchCriteriaUrl%>"></jsp:include>

<c:forEach var="criteria" items="${criterias.list}">
	<% SearchCriteria criteria = (SearchCriteria)pageContext.getAttribute("criteria"); 
	criteriaId = criteria.getId();
	%>

<div style="clear:both;width:100%;height:1px;">&nbsp;</div>
	<h4>Gender</h4>
	<div style="margin-left:32px;">
		<c:if test="${empty criteria.gender}">
			No gender filter defined currently.
		</c:if>
	
		<div style="width:88%;">
		<g:checkbox-group0 select="<%=criteria.getGender() %>" enumClass="<%=Gender.class %>" checkBoxName="genderCheckBox"></g:checkbox-group0>
		</div>
	</div>

<div style="clear:both;width:100%;height:1px;">&nbsp;</div>
	<h4>Ethnicity</h4>
	<div style="margin-left:32px;">
		<c:if test="${empty criteria.race}">
			No ethnicity filter defined currently.
		</c:if>
	
		<div style="width:88%;">
		<g:checkbox-group select="<%=criteria.getRace() %>" enumClass="<%=DimensionRace.class %>" checkBoxName="raceCheckBox"></g:checkbox-group>
		</div>
	</div>

<% 
	Calendar cal = Calendar.getInstance();
	thisYear = cal.get(Calendar.YEAR);
	int maxAge = 100;
	int minAge = 18;
	if(criteria.getMinBirthYear() != null && criteria.getMinBirthYear() > 0) {
	    maxAge = thisYear - criteria.getMinBirthYear();
	}
	if(criteria.getMaxBirthYear() != null && criteria.getMaxBirthYear() > 0) {
	    minAge = thisYear - criteria.getMaxBirthYear();
	}
%>
<div style="clear:both;width:100%;height:1px;">&nbsp;</div>
	<h4>Min Age</h4>
	<div style="margin-left:32px;">
	 	<input data-theme='d' type="range" name="minAge" id="minAge" value="<%=minAge%>" min="18" max="100"  />
	</div>
	
<div style="clear:both;width:100%;height:1px;">&nbsp;</div>
	<h4>Max Age</h4>
	<div style="margin-left:32px;">
	 	<input data-theme='d' type="range" name="maxAge" id="maxAge" value="<%=maxAge%>" min="18" max="100"  />
	</div>
	
<div style="clear:both;width:100%;height:1px;">&nbsp;</div>
	<h4>Income</h4>
	<div style="margin-left:32px;">
		<c:if test="${empty criteria.income}">
			No income filter defined currently.
		</c:if>
	
		<div style="width:88%;">
		<g:checkbox-group select="<%=criteria.getIncome() %>" enumClass="<%=com.gaoshin.dating.DimensionIncome.class %>" checkBoxName="incomeCheckBox"></g:checkbox-group>
		</div>
	</div>

<div style="clear:both;width:100%;height:1px;">&nbsp;</div>
	<h4>Smoking</h4>
	<div style="margin-left:32px;">
		<c:if test="${empty criteria.smoking}">
			No smoking filter defined currently.
		</c:if>
	
		<div style="width:88%;">
		<g:checkbox-group select="<%=criteria.getSmoking() %>" enumClass="<%=com.gaoshin.dating.DimensionSmoking.class %>" checkBoxName="smokingCheckBox"></g:checkbox-group>
		</div>
	</div>

<div style="clear:both;width:100%;height:1px;">&nbsp;</div>
	<h4>Drinking</h4>
	<div style="margin-left:32px;">
		<c:if test="${empty criteria.drinking}">
			No drinking filter defined currently.
		</c:if>
	
		<div style="width:88%;">
		<g:checkbox-group select="<%=criteria.getDrinking() %>" enumClass="<%=com.gaoshin.dating.DimensionDrinking.class %>" checkBoxName="drinkingCheckBox"></g:checkbox-group>
		</div>
	</div>

<div style="clear:both;width:100%;height:1px;">&nbsp;</div>
	<h4>Marriage Status</h4>
	<div style="margin-left:32px;">
		<c:if test="${empty criteria.marriage}">
			No marriage filter defined currently.
		</c:if>
	
		<div style="width:88%;">
		<g:checkbox-group select="<%=criteria.getMarriage() %>" enumClass="<%=com.gaoshin.dating.DimensionMarriageStatus.class %>" checkBoxName="marriageCheckBox"></g:checkbox-group>
		</div>
	</div>

<div style="clear:both;width:100%;height:1px;">&nbsp;</div>
	<h4>Education Level</h4>
	<div style="margin-left:32px;">
		<c:if test="${empty criteria.education}">
			No education filter defined currently.
		</c:if>
	
		<div style="width:88%;">
		<g:checkbox-group select="<%=criteria.getEducation() %>" enumClass="<%=com.gaoshin.dating.DimensionEducation.class %>" checkBoxName="educationCheckBox"></g:checkbox-group>
		</div>
	</div>

<div style="clear:both;width:100%;height:1px;">&nbsp;</div>
	<h4>Children</h4>
	<div style="margin-left:32px;">
		<c:if test="${empty criteria.likekids}">
			No children filter defined currently.
		</c:if>
	
		<div style="width:88%;">
		<g:checkbox-group select="<%=criteria.getLikekids() %>" enumClass="<%=com.gaoshin.dating.DimensionLikeChildren.class %>" checkBoxName="likekidsCheckBox"></g:checkbox-group>
		</div>
	</div>

<div style="clear:both;width:100%;height:1px;">&nbsp;</div>
	<h4>Pets</h4>
	<div style="margin-left:32px;">
		<c:if test="${empty criteria.pets}">
			No pets filter defined currently.
		</c:if>
	
		<div style="width:88%;">
		<g:checkbox-group select="<%=criteria.getPets() %>" enumClass="<%=com.gaoshin.dating.DimensionPets.class %>" checkBoxName="petsCheckBox"></g:checkbox-group>
		</div>
	</div>

<div style="clear:both;width:100%;height:1px;">&nbsp;</div>
	<h4>Minimal Height</h4>
	<div style="margin-left:32px;">
		<c:if test="${empty criteria.minHeight}">
			No min-height filter defined currently.
		</c:if>
	
		<% 
			int centimeter = criteria.getMinHeight() == null ? 0 : criteria.getMinHeight();
			int inches = (int)((float)centimeter / 2.54);
		%>
		
		<div data-role="fieldcontain">
			<div style="width:100%;text-align:center;margin-bottom:8px;">Centimeters</div>
		 	<input data-theme='d' type="range" name="cm" id="cmslider" value="<%=centimeter %>" min="0" max="220"  />
		</div>
		
		<div data-role="fieldcontain">
			<div style="width:100%;text-align:center;margin-bottom:8px;">Inches</div>
		 	<input data-theme='d' type="range" name="inch" id="inchslider" value="<%=inches%>" min="0" max="88"  />
		</div>
		
		<div data-role="fieldcontain">
			<div id="feet" style="width:100%;text-align:center;margin-bottom:8px;">0 Feet and 0 Inches</div>
		</div>
	</div>

<div style="clear:both;width:100%;height:1px;">&nbsp;</div>
	<h4>Max Number of Children</h4>
	<div style="margin-left:32px;">
		<div data-role="fieldcontain">
		 	<input data-theme='d' type="text" name="maxKids" id="maxKids" value="${criteria.maxKids}" />
		</div>
	</div>

</c:forEach>

<div style="clear:both;width:100%;height:1px;">&nbsp;</div>
<div class="ui-body ui-body-b" style='margin-top:12px;'>
	<fieldset class="ui-grid-a">
			<div class="ui-block-a"><a href='#' data-role='button' onclick='history.back()' id="cancel" data-theme="a">Cancel</a></div>
			<div class="ui-block-b"><button onclick='save()' data-theme="b">Save</button></div>
	</fieldset>
</div>
<br/><br/>
<!--
    private DimensionLanguage[] language;
    private String country;
    private Integer maxHeight;
    private Integer maxKids;
    private Integer minWeight;
    private Integer maxWeight;
-->

<script type="text/javascript">
counter = 0;
function refreshSlider(inc) {
    counter += inc;
    if(counter<=0) {
        $('#cmslider').slider('refresh');
        return;
    }
    setTimeout("refreshSlider(-1)", 200);
}

counter1 = 0;
function refreshSlider1(inc) {
    counter1 += inc;
    if(counter1<=0) {
        $('#cmslider1').slider('refresh');
        return;
    }
    setTimeout("refreshSlider1(-1)", 200);
}

function cmchanged(inchid, cmid, feetid) {
    var oldValue = $(inchid).val() + "";
    var newValue = Math.round($(cmid).val() / 2.54) + "";
    if(oldValue == newValue)
        return;
    $(inchid).val(Number(newValue));
    $(inchid).slider('refresh');
	setFeet(inchid, feetid);        
}

function inchChanged(inchid, cmid, feetid, refreshFun) {
    var oldValue = $(cmid).val() + "";
    var newValue = Math.round($(inchid).val() * 2.54) + "";
    if(oldValue == newValue)
        return;
    $(cmid).val(Number(newValue));
    refreshFun(1);
	setFeet(inchid, feetid);        
}

$(document).ready(function(){
	setFeet('#inchslider', 'feet');        
    $('#cmslider').change(function(){cmchanged('#inchslider', '#cmslider', '#feet');});
    $('#inchslider').change(function(){inchChanged('#inchslider', '#cmslider', '#feet', refreshSlider);});
});

function setFeet(inchid, feetid) {
    var feet = Math.round($(inchid).val() / 12);
    var frac = $(inchid).val() - feet * 12;
    if(frac < 0) {
        feet --;
        frac += 12;
    }
    $(feetid).html(feet + " Feet and " + frac + " Inches");
}

function changeAge(year) {
    var currentColor = $('#year'+year).css('background-color');
    if((currentColor+'') == 'transparent') {
        $('#year'+year).css('background-color', '#afa');
    } else {
        $('#year'+year).css('background-color', 'transparent');
    }
}

function save() {
    var req = {};
    
    var checkedGender = new Array();
    $('input[name=genderCheckBox]').each(function(){
        if(this.checked) {
            checkedGender.push(this.value);
        }
    });
    if(checkedGender.length>0)
	    req.gender = checkedGender;
    
    var checkedRace = new Array();
    $('input[name=raceCheckBox]').each(function(){
        if(this.checked) {
            checkedRace.push(this.value);
        }
    });
    if(checkedRace.length>0)
	    req.race = checkedRace;

    minAge = $('#minAge').val();
    maxAge = $('#maxAge').val();
    req.minBirthYear = <%=thisYear%> - maxAge;
    req.maxBirthYear = <%=thisYear%> - minAge;
    
    var checked = new Array();
    $('input[name=incomeCheckBox]').each(function(){
        if(this.checked) {
            checked.push(this.value);
        }
    });
    if(checked.length>0)
	    req.income = checked;

    var checked = new Array();
    $('input[name=smokingCheckBox]').each(function(){
        if(this.checked) {
            checked.push(this.value);
        }
    });
    if(checked.length>0)
	    req.smoking = checked;

    var checked = new Array();
    $('input[name=drinkingCheckBox]').each(function(){
        if(this.checked) {
            checked.push(this.value);
        }
    });
    if(checked.length>0)
	    req.drinking = checked;

    var checked = new Array();
    $('input[name=marriageCheckBox]').each(function(){
        if(this.checked) {
            checked.push(this.value);
        }
    });
    if(checked.length>0)
	    req.marriage = checked;

    var checked = new Array();
    $('input[name=educationCheckBox]').each(function(){
        if(this.checked) {
            checked.push(this.value);
        }
    });
    if(checked.length>0)
	    req.education = checked;

    var checked = new Array();
    $('input[name=likekidsCheckBox]').each(function(){
        if(this.checked) {
            checked.push(this.value);
        }
    });
    if(checked.length>0)
        req.likekids = checked;

    var checked = new Array();
    $('input[name=petsCheckBox]').each(function(){
        if(this.checked) {
            checked.push(this.value);
        }
    });
    if(checked.length>0)
    req.pets = checked;
    
    req.minHeight = $('#cmslider').val();
    req.maxKids = $('#maxKids').val();
    req.id = <%=criteriaId%>;
    
    var json = JSON.stringify(req);
	$.ajax({
		url : serverBase + "/dating/search/criteria/save",
		type : "POST",
		data : json,
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		complete : function(transport) {
			if (transport.status == 200) {
				window.location = '<c:url value="/m/search/index.jsp.oo"/>';
			} else {
				alert('Error: ' + transport.status + ", "
						+ transport.responseText);
			}
		}
	});
}

</script>

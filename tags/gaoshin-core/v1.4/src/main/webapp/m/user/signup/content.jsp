<%@page import="common.util.JacksonUtil"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<style>
.ui-field-contain {padding: 0.5em 0;}
.ui-br {border-bottom: 0px;}
</style>

<div style="float:right;clear:both;margin-top:0px;"><span style="font-size:0.8em;">Already got account?</span> <a href="#" onclick='window.location="<c:url value="/m/user/login/index.jsp.oo"/>"'>Sign In</a></div>
<div style="width:100%;">
<h3>Sign Up</h3>
</div>

<form onsubmit="return signup();">
<input type="hidden" name="city"/>
<input type="hidden" name="state"/>
<input type="hidden" name="country"/>

<div data-role="fieldcontain" id="phoneContainer">
    <label for="phone">Cell Phone:</label>
    <input type="text" name="phone" id="phone" value="" data-theme="d" />
</div>

<div data-role="fieldcontain">
    <label for="name">Display Name:</label>
    <input type="text" name="name" id="name" value="" data-theme="d" />
</div>

<!--div data-role="fieldcontain">
    <label for="password">Password:</label>
    <input type="text" name="password" id="password" value="" data-theme="d" />
</div-->	

<!--div data-role="fieldcontain">
    <label for="password2">Password Again:</label>
    <input type="password" name="password2" id="password2" value="" data-theme="d" />
</div-->	

<div data-role="fieldcontain">
	<label for="gender" class="select">Gender:</label>
	<select name="gender" id="gender" data-theme="d">
		<option selected value="Man">Man</option>
		<option value="Woman">Woman</option>
	</select>
</div>

<div data-role="fieldcontain">
	<label for="birthyear" class="select">Birth Year:</label>
	<select name="birthyear" id="birthyear" data-theme="d">
	<% for (int i=1930; i<1993; i++) {%>
		<option <%= (i==1985 ? "selected" : "") %> value="<%=i%>"><%=i%></option>
	<% } %>
	</select>
</div>

<div data-role="fieldcontain">
	<label for="lookingfor" class="select">Looking For:</label>
	<select name="lookingfor" id="lookingfor" data-theme="d">
		<option selected value="Woman">Woman</option>
		<option value="Man">Man</option>
		<option value="Friends">Friends</option>
	</select>
</div>

<button type="submit" data-theme="e" >Sign Up</button>
</form>

<script type="text/javascript">
$(document).ready(function(){
    var phone = Device.getMyPhoneNumber();
    if(phone != null) {
        $('#phoneContainer').hide();
        $('#phone').val(phone);
        if(phone == '14085987655') {
            $('#interests').val("Ski\nGolfing\nCooking");
        }
        if(phone == '4089925287') {
            $('#interests').val("Hiking\nReading\nCoding\nSki");
        }
    }

    var lat = Device.getSavedLatitude();
    var lng = Device.getSavedLongitude();
    if(lat >0.0001 || lat <-0.0001) {
	    reverseGeo(lat, lng, function(lat, lng, place){
	        if(place != null) {
	            try {
	            	var country = place.AddressDetails.Country.CountryNameCode;
	            	$('#country').val(country);
	            } catch (e){};
	            try {
	            	var state = place.AddressDetails.Country.AdministrativeArea.AdministrativeAreaName;
	            	$('#state').val(state);
	            } catch (e){};
	            try {
   		            var city = place.AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.Locality.LocalityName;
   	            	$('#city').val(city);
	            } catch (e){};
	        }
	    });
    }
});

function signup() {
	var name = $("#name").val();
	if(name == null || name.trim().length == 0) {
		alert("All filelds are required. Please check name field.");
		return false;
	}
	
	var phone = $("#phone").val();
	if(phone == null || phone.trim().length == 0) {
		alert("All filelds are required. Please check phone field.");
		return false;
	}
	
	var gender = $("#gender").val();
	if(gender == null || gender.trim().length == 0) {
		alert("All filelds are required. Please check gender field.");
		return false;
	}
	
	var birthyear = $("#birthyear").val();
	if(birthyear == null || birthyear.trim().length == 0) {
		alert("All filelds are required. Please check birth year field.");
		return false;
	}
	
	var lookingfor = $("#lookingfor").val();
	
	Device.showLoadingDialog();
	
		var req = {
			name: name,
			phone: phone,
			gender: gender,
			birthyear: birthyear,
			lookingfor: lookingfor,
			clientType: Device.getClientType(),
			deviceInfo: Device.selfencrypt(Device.getPhoneInfo(), new Date().getTime())
		};
		var json = JSON.stringify(req);
		
		$.ajax({
			  url:serverBase + "/dating/signup",
			  type:"POST",
			  data:json,
			  contentType:"application/json; charset=utf-8",
			  dataType:"json",
			  complete: function(transport) {
					Device.hideLoadingDialog();
				     if(transport.status == 200) {
				         window.location = '<c:url value="/m/user/login/index.jsp.oo"/>';
				     } else if (transport.status == 498) {
				         alert('Error: user name is taken. Please try another one');
				     } else if (transport.status == 495){
				         alert('Invalid input error: ' + transport.responseText);
				     } else {
				         alert('Error: ' + transport.responseText);
				     }
				  }
			});

	return false;
}
</script>
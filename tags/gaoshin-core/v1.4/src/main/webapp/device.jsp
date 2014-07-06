<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

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
		getMyPhoneNumber: function() {
			return null;
		},
		sendSms: function() {
		},
		log: function() {
		},
		startSimpleActivity: function(view, url) {
			if(view == 'android.intent.action.VIEW') {
				window.location = url;
			}
		},
		registerKeyHandler: function(code, handler) {
		},
		getPhoneInfo: function() {
			return null;
		},
		hideLoadingDialog: function() {
		},
		showLoadingDialog: function() {
		},
		selfencrypt: function (a ,b) {
			return null;
		},
		setDeviceLog: function() {
		},
		resetLastAccessTime: function(){
		},
		toast: function() {
		},
		exit: function() {
			history.back();
		},
		startActivity: function() {
		},
		setConf: function() {
		},
		back: function() {
		},
		addWidgetMessage: function() {
		},
		removeSessionCookies: function() {
		}
	};
}

<jsp:include page="intent.jsp"></jsp:include>

gMsgHandler = {
    "Location": function() {
    }
};
function handleMessage() {
    while(Device.hasMessage()) {
        var msg = Device.getMessage();
        /*
        var msgObj = JSON.parse(msg);
        var msgType = msgObj.type;
        if(gMsgHandler[msgType]) {
            try {
            	gMsgHandler[msgType](msgObj);
            } catch (e) {
                Device.toast((msgObj.title == null ? "" : msgObj.title) + "\n" + (msgObj.msg == null ? "" : msgObj.msg));
            }
        }
        else {
               Device.toast((msgObj.title == null ? "" : ("\n" + msgObj.title)) + "\n" + (msgObj.msg == null ? "" : msgObj.msg));
        }
        */
    }
}

function mvsearch() {
}
function test() {
	window.location = '<c:url value="/m/test/index.jsp.oo"/>';
}
function wvmenu() {
    if( $('#footer').is(':visible') ) {
    	hideMenu();
    }
    else {
    	showMenu();
    }
}

function hideMenu() {
    $('#footer').hide();
    Device.setMenuVisible(false);
  	createCookie("showMenu", "false", 365);
}

function showMenu() {
    $('#footer').show();
    var width = $('#viewarea').width();
    var height = $('#viewarea').height();
    var divWidth = $('#footer').width();
    var divHeight = $('#footer').height();
    $('#footer').css({top:height-divHeight});
    Device.setMenuVisible(true);
	createCookie("showMenu", "true", 365);
}

function wvback() {
    if( $('#footer').is(':visible') ) {
        $('#footer').hide();
    }
    else {
        Device.back();
    }
}

Device.registerKeyHandler(82, "wvmenu");
Device.registerKeyHandler(4, "wvback");
//Device.registerKeyHandler(84, "wvsearch");
//Device.registerKeyHandler(24, "keyup");
//Device.registerKeyHandler(25, "keydown");

/*
$(document).ready(function(){
    var showMenu = readCookie("showMenu");
    if(showMenu==null || showMenu == "true") {
    	showMenu();
    } else {
    	hideMenu();
    }
});
*/
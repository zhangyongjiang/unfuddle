<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
	String hostId = request.getParameter("hostId");
	String vmId = request.getParameter("vmId");
	String url = "/xen/host/vm/details?hostId=" + hostId + "&vmId=" + vmId + "&format=object&var=it";
	out.write("<!-- " + url + "-->");
%>
<jsp:include page="<%=url %>"></jsp:include>

<div class="page-header">VM Details</div>

<div id="tabs" style="min-height: 480px;">
	<ul>
		<li><a href="#summary">${it.nameLabel }</a></li>
		<li><a href="#vbds">Virtual Block Devices</a></li>
		<li><a href="#vifs">Virtual Network Interfaces</a></li>
	</ul>
	
	<div id="summary">
		<div>
			<c:if test="${!it.template}">
				<div style="float:right;"><img src='<c:url value="/images/vm_status_"/>${it.powerState}_32.png'/></div>
			</c:if>
			<div style="clear:left;height:1px;margin-top:12px;"></div>
			<c:if test="${it.powerState == 'HALTED'}">
				<button id="btnDestroy" onClick='destroyVm("${it.hostId}", "${it.uuid}")'>Destroy</button>
			</c:if>
			<c:if test="${!it.template}">
				<div style="float:left;">
				<c:if test="${it.powerState == 'RUNNING'}">
					<button id="btnSuspend" onClick='suspendVm("${it.hostId}", "${it.uuid}")'>Suspend</button>
					<button id="btnShutdown" onClick='shutdownVm("${it.hostId}", "${it.uuid}")'>Shutdown</button>
					<button id="btnSnapshot" onClick='snapshotVm("${it.hostId}", "${it.uuid}")'>Snapshot</button>
				</c:if>
				<c:if test="${it.powerState == 'HALTED'}">
					<button id="btnStart" onClick='startVm("${it.hostId}", "${it.uuid}")'>Start</button>
					<button id="btnClone" onClick='cloneVm("${it.hostId}", "${it.uuid}")'>Clone</button>
				</c:if>
				<c:if test="${it.powerState == 'SUSPENDED'}">
					<button id="btnResume" onClick='resumeVm("${it.hostId}", "${it.uuid}")'>Resume</button>
					<button id="btnClone" onClick='cloneVm("${it.hostId}", "${it.uuid}")'>Clone</button>
				</c:if>
				</div>
				<div style="float:left;display:none;margin-top:11px;margin-left:3px;" id="loading"><img src='<c:url value="/images/loading_20.gif"/>'/></div>
			</c:if>
			<c:if test="${it.template}">
				<button id="btnCreateVm" onClick='createVm("${it.hostId}", "${it.uuid}")'>Create VM</button>
			</c:if>
		</div>
	
		<table style="clear:both;border: solid 0px;">
		<tr><td>Host</td>
		<td><a href='<c:url value="/xen/host/details/index.jsp.oo?hostId="/>${it.hostId}'>${it.host.name}</a></td></tr>
		<tr><td>VM Name Label</td>
		<td>${it.nameLabel}</td></tr>
	
		<tr><td>IP Address</td>
		<td>${it.ipAddress}</td></tr>
	
		<tr><td>Max Dynamic Memory</td>
		<td>${it.memoryDynamicMax}</td></tr>
	
		<tr><td>Min Dynamic Memory</td>
		<td>${it.memoryDynamicMin}</td></tr>
	
		<tr><td>Max Static Memory</td>
		<td>${it.memoryStaticMax}</td></tr>
	
		<tr><td>Min Static Memory</td>
		<td>${it.memoryStaticMin}</td></tr>
	
		<tr><td>Target Memory</td>
		<td>${it.memoryTarget}</td></tr>
	
		<tr><td>Power State</td>
		<td>
		${it.powerState}
		</td></tr>
	
		<!-- tr><td>Snapshot Time</td>
		<td><o:millisecond-to-date time="${it.snapshotTime}"></o:millisecond-to-date></td></tr-->
	
		<tr><td>Is a Template?</td>
		<td>${it.template}
		</td></tr>
	
		<tr><td>User Version</td>
		<td>${it.userVersion}</td></tr>
	
		<tr><td>Is a Snapshot?</td>
		<td>${it.isASnapshot}</td></tr>
	
		<tr><td>UUID</td>
		<td>${it.uuid}</td></tr>
		
		<tr><td>Virtual CPUs At Startup</td>
		<td>${it.VCPUsAtStartup}</td></tr>
	
		<tr><td>Virtual CPUs Max</td>
		<td>${it.VCPUsMax}</td></tr>
		
		<c:if test="${it.powerState == 'RUNNING' && it.domid > 0 && (not empty it.consoleList)}">
		<tr><td>Console</td>
		<td>
			<c:forEach var="console" items="${it.consoleList }">
			Protocol: ${console.protocol }
				<div style="float:right;"><BUTTON onClick='openConsole("${console.hostId}", "${console.vmId}", "${console.uuid}")'>Open</BUTTON></div>
			</c:forEach>
		</td></tr>
		</c:if>
	</table>
</div>

	<div id="vbds">
	<c:if test="${empty it.vbdList }">
	No virtual block device found.
	</c:if>
	<c:if test="${not empty it.vbdList }">
		<ul>
		<c:forEach var="vbd" items="${it.vbdList }">
			<li>${vbd.uuid } ${vbd.type } ${vbd.device } ${vbd.statusDetail }</li>
		</c:forEach>
		</ul>
	</c:if>
	</div>

	<div id="vifs">
	<c:if test="${empty it.vifList }">
	No virtual network interface found.
	</c:if>
	<c:if test="${not empty it.vifList }">
		<ul>
		<c:forEach var="vif" items="${it.vifList }">
			<li>${vif.uuid } ${vif.device } ${vif.statusDetail } ${vif.mac}  ${vif.currentlyAttached} ${vif.bridge }</li>
		</c:forEach>
		</ul>
	</c:if>
	<button onclick='reconfigNetwork("${it.hostId}", "${it.uuid}")'>Reconfig</button>
	</div>

</div>

<script type="text/javascript">
$(document).ready(function(){
    $.address.change(function(event){
        $("#tabs").tabs( "select" , window.location.hash )
    });

    $("#tabs").bind("tabsselect", function(event, ui) { 
        window.location.hash = ui.tab.hash;
    });
  	
	$( "#tabs" ).tabs();
});

function createVm(hostId, templateId) {
	window.location = base + "/xen/host/vm/create/index.jsp.oo?hostId=" + hostId + "&templateId=" + templateId;
}

function reconfigNetwork(hostId, vmId) {
    $.ajax({
        url : base + "/xen/host/vm/change-network/" + hostId + "/" + vmId,
        type : "POST",
        contentType : "application/json; charset=utf-8",
        dataType : "json",
        complete : function(transport) {
            if (transport.status == 200) {
                window.location.reload();
            } else {
                alert('Error: ' + transport.status + ", "
                                + transport.responseText);
            }
        }
	});
}

function startVm(hostId, vmId) {
	$("#btnStart").attr("disabled", "disabled");
    $.ajax({
        url : base + "/xen/host/vm/start/" + hostId + "/" + vmId,
        type : "POST",
        contentType : "application/json; charset=utf-8",
        dataType : "json",
        complete : function(transport) {
        	$("#btnStart").removeAttr("disabled");
            if (transport.status == 200) {
                window.location.reload();
            } else {
                alert('Error: ' + transport.status + ", "
                                + transport.responseText);
            }
        }
	});
}

function resumeVm(hostId, vmId) {
	$("#btnResume").attr("disabled", "disabled");
    $.ajax({
        url : base + "/xen/host/vm/resume/" + hostId + "/" + vmId,
        type : "POST",
        contentType : "application/json; charset=utf-8",
        dataType : "json",
        complete : function(transport) {
        	$("#btnResume").removeAttr("disabled");
                if (transport.status == 200) {
                    window.location.reload();
                } else {
                    alert('Error: ' + transport.status + ", "
                                    + transport.responseText);
                }
        }
	});
}

function suspendVm(hostId, vmId) {
	$("#btnSuspend").attr("disabled", "disabled");
	$("#btnShutdown").attr("disabled", "disabled");
    $.ajax({
        url : base + "/xen/host/vm/suspend/" + hostId + "/" + vmId,
        type : "POST",
        contentType : "application/json; charset=utf-8",
        dataType : "json",
        complete : function(transport) {
        	$("#btnSuspend").removeAttr("disabled");
            if (transport.status == 200) {
                window.location.reload();
            } else {
                alert('Error: ' + transport.status + ", "
                                + transport.responseText);
            }
        }
	});
}

function shutdownVm(hostId, vmId) {
	$("#btnSuspend").attr("disabled", "disabled");
	$("#btnShutdown").attr("disabled", "disabled");
    $.ajax({
        url : base + "/xen/host/vm/shutdown/" + hostId + "/" + vmId,
        type : "POST",
        contentType : "application/json; charset=utf-8",
        dataType : "json",
        complete : function(transport) {
        	$("#btnSuspend").removeAttr("disabled");
        	$("#btnShutdown").removeAttr("disabled");
            if (transport.status == 200) {
                window.location.reload();
            } else {
                alert('Error: ' + transport.status + ", "
                                + transport.responseText);
            }
        }
	});
}

function destroyVm(hostId, vmId) {
	if(!confirm("Are you sure?")) {
		return;
	}
	$("#btnDestroy").attr("disabled", "disabled");
    $.ajax({
        url : base + "/xen/host/vm/destroy/" + hostId + "/" + vmId,
        type : "POST",
        contentType : "application/json; charset=utf-8",
        dataType : "json",
        complete : function(transport) {
        	$("#btnDestroy").removeAttr("disabled");
            if (transport.status == 200) {
                window.location = base + "/xen/host/details/index.jsp.oo?hostId=" + hostId;
            } else {
                alert('Error: ' + transport.status + ", "
                                + transport.responseText);
            }
        }
	});
}

function cloneVm(hostId, vmId) {
	var name = prompt("Please input the new VM name");
	if(name == null) {
		return;
	}
	$("#btnClone").attr("disabled", "disabled");
	var req = {
	    hostId: hostId,
	    templateId: vmId,
	    vmName: name
	}
	var json = JSON.stringify(req);
    $.ajax({
        url : base + "/xen/host/vm/clone",
        type : "POST",
        contentType : "application/json; charset=utf-8",
        dataType : "json",
        data: json,
        complete : function(transport) {
        	$("#btnClone").removeAttr("disabled");
            if (transport.status == 200) {
                var newVm = JSON.parse(transport.responseText);
                window.location = base + "/xen/host/vm/details/index.jsp.oo?hostId=" + hostId + "&vmId=" + newVm.data;
            } else {
                alert('Error: ' + transport.status + ", "
                                + transport.responseText);
            }
        }
	});
}

function snapshotVm(hostId, vmId) {
	var name = prompt("Please input the snapshot name");
	if(name == null) {
		return;
	}
	$("#btnSnapshot").attr("disabled", "disabled");
	var req = {
	    hostId: hostId,
	    vmId: vmId,
	    snapshotName: name
	}
	var json = JSON.stringify(req);
    $.ajax({
        url : base + "/xen/host/vm/snapshot",
        type : "POST",
        contentType : "application/json; charset=utf-8",
        dataType : "json",
        data: json,
        complete : function(transport) {
        	$("#btnSnapshot").removeAttr("disabled");
            if (transport.status == 200) {
                var newVm = JSON.parse(transport.responseText);
                window.location = base + "/xen/host/vm/details/index.jsp.oo?hostId=" + hostId + "&vmId=" + newVm.data;
            } else {
                alert('Error: ' + transport.status + ", "
                                + transport.responseText);
            }
        }
	});
}

function openConsole(hostId, vmId, consoleId) {
	var url = '<c:url value="/xen/host/vm/console.jsp?hostId="/>' + hostId + '&vmId=' + vmId + '&consoleId=' + consoleId;
	window.open(url,'name_'+new Date().getTime(),'height=520,width=800, toolbar=no,directories=no,status=no,menubar=no, scrollbars=no,resizable=no'); 
}
</script>
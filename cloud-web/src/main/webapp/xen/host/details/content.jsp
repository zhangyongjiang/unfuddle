<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
	String hostId = request.getParameter("hostId");
	String url = "/xen/host/details/" + hostId + "?format=object&var=it";
%>
<jsp:include page="<%=url %>"></jsp:include>

<div class="page-header">Host Details</div>

<div id="tabs" style="min-height: 480px;">
	<ul>
		<li><a href="#summary">${it.name }</a></li>
		<li><a href="#sr">Storage Repositories</a></li>
		<li><a href="#vm">Virtual Machines</a></li>
		<li><a href="#vdi">Virtual Device Images</a></li>
	</ul>
	
	<div id="summary">
		<table style="border: solid 0px;">
			<o:tr-label-value id="name" label="Name" value="${it.name }"></o:tr-label-value>
			<o:tr-label-value id="status" label="Status" value="${it.status }"></o:tr-label-value>
			<o:tr-label-value id="url" label="URL" value="${it.url }"></o:tr-label-value>
			<o:tr-label-value id="user" label="User" value="${it.user }"></o:tr-label-value>
			<o:tr-label-value id="password" label="Password" value="......"></o:tr-label-value>
			<o:tr-label-value id="cpus" label="CPU#" value="${it.cpus }"></o:tr-label-value>
			<o:tr-label-value id="memoryTotal" label="Memory Total" ><fmt:formatNumber pattern="#,###" value="${it.memoryTotal}" /></o:tr-label-value>
			<o:tr-label-value id="memoryFree" label="Memory Free" ><fmt:formatNumber pattern="#,###" value="${it.memoryFree}" /></o:tr-label-value>
		</table>
		<div style="clear:both; margin-top:20px;" >
		<button style="clear:both; margin-top:20px;" id="btnRemove" onClick='removeHost("${it.id}")'>Remove From Pool</button>
		</div>
	</div>

	<div id="sr">
		<c:if test="${empty it.storageRepoList.items }">
		<div style="margin: 20px;">No storage repository found on this host</div>
		</c:if>
		
		<c:if test="${not empty it.storageRepoList.items }">
		<table style="border: solid 0px;">
		<tr>
		    <!-- th>UUID</th -->
		    <th>Name</th>
		    <th>Type</th>
		    <th>Physical Size</th>
		    <th>Physical Utilisation</th>
		    <th>Shared</th>
		    <th>Description</th>
		</tr>
		<c:forEach var="sr" items="${it.storageRepoList.items }">
		<tr>
		    <!-- td>${sr.uuid}</td-->
		    <td><a href='<c:url value="/xen/host/sr/details/index.jsp.oo?hostId="/>${it.id}&srUuid=${sr.uuid}'>${sr.name}</a></td>
		    <td>${sr.type}</td>
		    <td>${sr.physicalSize}</td>
		    <td>${sr.physicalUtilisation}</td>
		    <td>${sr.shared}&nbsp;</td>
		    <td>${sr.description}&nbsp;</td>
		</tr>
		</c:forEach>
		</table>
		</c:if>
	</div>

	<div id="vm">
		<c:if test="${empty it.vmList.items }">
		<div style="margin: 20px;">No VM found on this host</div>
		</c:if>
		
		<c:if test="${not empty it.vmList.items }">
		<table style="border: solid 0px;">
		<tr>
			<th>Name Label</th>
			<!-- th>memoryDynamicMax</th>
			<th>memoryDynamicMin</th>
			<th>memoryStaticMax</th>
			<th>memoryStaticMin</th>
			<th>memoryTarget</th-->
			<th>State</th>
			<th>Template?</th>
			<th>User Version</th>
			<th>Snapshot?</th>
			<th>VCPUsAtStartup</th>
			<th>VCPUsMax</th>
			<th>&nbsp;</th>
		</tr>
		<c:forEach var="vm" items="${it.vmList.items}">
		<tr>
			<td><a href='<c:url value="/xen/host/vm/details/index.jsp.oo?"/>hostId=${it.id}&vmId=${vm.uuid}'>${vm.nameLabel}</a></td>
			<!-- td>${vm.memoryDynamicMax}</td>
			<td>${vm.memoryDynamicMin}</td>
			<td>${vm.memoryStaticMax}</td>
			<td>${vm.memoryStaticMin}</td>
			<td>${vm.memoryTarget}</td-->
			
			<td>
				${vm.powerState}
			</td>
			
			<td>${vm.template}</td>
			<td>${vm.userVersion}</td>
			<td>${vm.isASnapshot}</td>
			<td>${vm.VCPUsAtStartup}</td>
			<td>${vm.VCPUsMax}</td>
			<td>
				<c:if test="${!vm.template && vm.powerState == 'RUNNING' || vm.powerState == 'SUSPENDED'}">
					<img style="width:16px;" src='<c:url value="/images/vm_status_"/>${vm.powerState}_32.png'/>
				</c:if>
				&nbsp;
			</td>
		</tr>
		</c:forEach>
		</table>
		</c:if>
	</div>

	<div id="vdi">
		<c:if test="${empty it.vdiList.items }">
		<div style="margin: 20px;">No Virtual Device Image found on this host</div>
		</c:if>
		
		<c:if test="${not empty it.vdiList.items }">
		<table style="border: solid 0px;">
		<tr>
			<th>Name Label</th>
			<th>Description</th>
			<th>Location</th>
			<th>Type</th>
		</tr>
		<c:forEach var="vdi" items="${it.vdiList.items}">
		<tr>
			<td>${vdi.nameLabel}</td>
			<td>${vdi.nameDescription}&nbsp;</td>
			<td>${vdi.location}</td>
			<td>${vdi.type}</td>
		</tr>
		</c:forEach>
		</table>
		</c:if>
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

function removeHost(hostId) {
	if(!confirm("Are you sure?")) {
		return;
	}
    $.ajax({
        url : base + "/xen/host/remove/" + hostId,
        type : "POST",
        contentType : "application/json; charset=utf-8",
        dataType : "json",
        complete : function(transport) {
            if (transport.status == 200) {
                window.location = base + "/xen/host/list/index.jsp.oo";
            } else {
                alert('Error: ' + transport.status + ", "
                                + transport.responseText);
            }
        }
	});
}
</script>
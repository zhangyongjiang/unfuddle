<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>

<div>
<c:if test="${empty it.children }">
	EMPTY FOLDER
</c:if>

<c:if test="${not empty it.children }">
<table style="border:solid 1px;margin-top:10px;">
<tr><th>Name</th><th>Type</th><th>Size</th><th>Replication</th><th>Block Size</th><th>Modification Time</th><th>Permission</th><th>Owner</th><th>Group</th><th>&nbsp;</th></tr>
<c:forEach var="file" items="${it.children }">
	<tr>
	
	<td><a href='<c:url value="/hadoop/hdfs/info/index.jsp.oo?path="/>${file.path }'>${file.name }</a></td>
	<c:if test="${file.dir }">
		<td>dir</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</c:if>
	<c:if test="${not file.dir }">
		<td>file</td>
		<td>${file.size }</td>
		<td>${file.replication }</td>
		<td>${file.blockSize }</td>
	</c:if>

	<td><o:millisecond-to-date time="${file.modificationTime }"></o:millisecond-to-date></td>
	<td>${file.permission }</td>
	<td>${file.owner }</td>
	<td>${file.group }</td>
	
	<c:if test="${file.dir }">
		<td>&nbsp;</td>
	</c:if>
	<c:if test="${not file.dir }">
		<td><img onClick="rm('${file.name}')" src='<c:url value="/images/delete_icon.gif"/>'/></td>
	</c:if>
	
	</tr>	
</c:forEach>
</table>
</c:if>

</div>

<div style="margin-top:16px;">
		<div style="padding:8px;float:left;"><button onClick="mkdir()">mkdir</button></div>
		<div style="padding:8px;float:left;"><button onClick="upload()">upload</button></div>
</div>

<script type="text/javascript">
function mkdir() {
	var name = prompt("Please give dir name");
	if(name == null || name.length == 0) {
		return;
	}

	var path = '${it.path}/' + name;
	var url = base + '/hadoop/hdfs/mkdir' + path;
    $.ajax({
        url : url,
        type : "POST",
        contentType : "application/json; charset=utf-8",
        dataType : "json",
        complete : function(transport) {
                if (transport.status == 200) {
                    alert("succeed!")
                	location.reload();
                } else {
                        alert('Error ' + url + ": " + transport.status + ", "
                                        + transport.responseText);
                }
        }
	});
}

function rm(name) {
	var path = '${it.path}/' + name;
	var url = base + '/hadoop/hdfs/rm?path=' + path;
    $.ajax({
        url : url,
        type : "GET",
        contentType : "application/json; charset=utf-8",
        accepts: "application/json; charset=utf-8",
        dataType : "json",
        complete : function(transport) {
                if (transport.status == 200) {
                    alert("succeed!")
                	location.reload();
                } else {
                        alert('Error ' + url + ": " + transport.status + ", "
                                        + transport.responseText);
                }
        }
	});
}

function upload() {
	window.location = base + '/hadoop/hdfs/upload${it.path}/index.jsp.oo';	
}
</script>
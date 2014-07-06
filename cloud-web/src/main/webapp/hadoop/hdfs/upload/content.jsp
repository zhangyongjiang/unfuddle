<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o"%>

<%
    String uri = request.getRequestURI();
	int pos0 = uri.indexOf("/upload");
	int pos1 = uri.indexOf("/index.");
	String path = uri.substring(pos0 + 7, pos1);
%>

<script type="text/javascript"
	src='<c:url value="/scripts/ajaxfileupload.js"/>'></script>

<div class="page-header" style="margin-bottom: 32px;">Upload File</div>

<form enctype="multipart/form-data" method="post"
	onsubmit="return uploadFile();">
	<div style="margin-bottom: 12px;">
		TO:
		<%=path%></div>
	<div style="margin-bottom: 12px;">
		<input size="48" type="file" name="file" id="file" />
	</div>
	<div id="btnUpload" style="margin-bottom: 12px;">
		<input type="submit" value="Upload">
	</div>
	<div id="uploading" style="margin-bottom: 12px;display:none;">
		<img src='<c:url value="/images/uploading.gif"/>'/>
	</div>
	<div id="done" style="margin-bottom: 12px;display:none;">
		done!
	</div>
</form>

<script type="text/javascript">
	function uploadFile() {
		$("#btnUpload").hide();
		$("#uploading").show();

		$.ajaxFileUpload({
			url : base + '/hadoop/hdfs/upload<%=path%>',
			secureuri : false,
			fileElementId : 'file',
//			dataType : 'json',
//	        accepts: "application/json",
			success : function(data, status) {
				$("#uploading").hide();
				$("#done").show();
				alert("Succeed!");
				window.location = '<c:url value="/hadoop/hdfs/info/index.jsp.oo?path="/><%=path%>';
			},
			error : function(data, status, e) {
				alert(e);
			}
		})

		return false;
	}
</script>
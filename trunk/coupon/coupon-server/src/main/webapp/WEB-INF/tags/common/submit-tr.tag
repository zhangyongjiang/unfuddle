<%@ attribute name="value" required="false" %>
<%@ attribute name="cancel" required="false" %>
<tr><td align="right"></td><td>
<input <%if(value!=null) out.write("value='" + value + "'"); %>type="submit" />
<% if(cancel != null) {
%>
<input type="button" value="Cancel" onclick="${cancel}"/>
<%
} %>
<jsp:doBody/>
</td></tr>
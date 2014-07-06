<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="select" required="true" type="java.lang.Enum[]"
%><%@ attribute name="enumClass" required="true" type="java.lang.Class"
%><%@ attribute name="checkBoxName" required="true" 
%>
<fieldset data-role="controlgroup" >
	<% 	for (Object obj : enumClass.getEnumConstants()) {
	    request.setAttribute("temp", obj);
	    Enum e = (Enum)obj;
	    boolean selected = false;
	    if(select != null) {
		    for(Enum sel : select) {
		        if(sel.equals(e)) {
		            selected = true;
		            break;
		        }
		    }
	    }
	%>
     	<input type="checkbox" data-theme='c' name="<%=checkBoxName %>" id="<%=e.name() %>" value="<%=e.name() %>" <% if(selected){ %>checked="checked"<% } %> />
     	<label for="<%=e.name() %>"><%=e.name() %></label>
	<% } %>
</fieldset>


<%@tag import="org.json.JSONObject"
%><%@tag import="common.util.reflection.ReflectionUtil"
%><%@tag import="common.util.JsonUtil"
%><%@tag import="java.text.SimpleDateFormat"
%><%@tag import="java.util.Date"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ attribute name="obj" required="true" type="java.lang.Object"
%><%
if(obj != null) {
        if(ReflectionUtil.isPrimeType(obj)){
            JSONObject jobj = new JSONObject();
            jobj.put(obj.getClass().getSimpleName(), ReflectionUtil.primeString(obj));
            out.write(jobj.toString());
        } else {
                String json = JsonUtil.toJsonString(obj);
                out.write(json);
        }
}
%>
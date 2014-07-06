<% 
String path = request.getRequestURI();
path = path.replaceAll("action", "resources");
if(path.endsWith(".jsp.oo")) {
    int pos = path.lastIndexOf('/');
    path = path.substring(0, pos);
}
out.write(path);
%>
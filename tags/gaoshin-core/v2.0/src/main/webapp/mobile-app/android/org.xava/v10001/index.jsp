<%@page import="java.util.Random"%>
<html>
<head>
<script type="text/javascript">
function createCookie(name, value, days) {
    if (days) {
        var date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        var expires = "; expires=" + date.toGMTString();
    } else
    var expires = "";
    document.cookie = name + "=" + value + expires + "; path=/";
}
createCookie("p", Device.getPhoneCookieValue(), 365);

try {
//    Device.removeSessionCookies();
} catch (e) {}
</script>
<% Random random = new Random(System.currentTimeMillis());
int pick = random.nextInt(3);
String url = "/xo/m/map/index.jsp.oo";
if(pick == 1) {
    url = "/xo/m/poem/list/index.jsp.oo";
}
if(pick == 2) {
    url = "/xo/m/user/most-recent/index.jsp.oo";
}
%>
<meta http-equiv="refresh" content="0;url=<%=url %>" />
</head>
<body>
Sincere Mobile Dating
</body>
</html>

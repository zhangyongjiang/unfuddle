<%@ page contentType="text/html; charset=utf-8" %>
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

var center = Device.getLatitude() + "," + Device.getLongitude();
createCookie("center", center, 365);
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv='refresh' content='0;url=/xo/m/search/index.jsp.oo' />
</head>
<body>
<jsp:include page="/m/poem/random/content-only.jsp.oo"></jsp:include>
</body>
</html>

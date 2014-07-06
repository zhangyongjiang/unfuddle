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
    Device.removeSessionCookies();
} catch (e) {}
</script>
<meta http-equiv="refresh" content="0;url=/onemile/m/index.jsp.oo" />
</head>
<body>
One Mile Friends
</body>
</html>

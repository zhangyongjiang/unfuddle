<%@ page contentType="text/html; charset=utf-8" 
%><%request.getRequestDispatcher("login-check.jsp.oo").include(request, response);
request.getRequestDispatcher("/ws/user/me?format=object&var=me").include(request, response);
boolean loginRequired = (Boolean) request.getAttribute("loginRequired");
com.gaoshin.applotto.entity.User me = (com.gaoshin.applotto.entity.User)request.getAttribute("me");
boolean gotoLoginPage = loginRequired && (me.getId() == null);
if(gotoLoginPage) {
    request.getRequestDispatcher("/pub/login/main.jsp.oo").include(request, response);
}
else {
    request.getRequestDispatcher("main.jsp.oo").include(request, response);
}%>
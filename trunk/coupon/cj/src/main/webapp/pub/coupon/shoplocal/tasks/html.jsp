<%
    String param = request.getQueryString();
    String url = "/ws/coupon/shoplocal-result?format=object&" + (param == null ? "" : param);
    request.getRequestDispatcher(url).include(request, response);
%>${it.html }

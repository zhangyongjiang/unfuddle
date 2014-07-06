<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.gaoshin.amazon.AwsBrowseNode"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<%! 
public List<List<AwsBrowseNode>> flat(AwsBrowseNode node) {
    List<List<AwsBrowseNode>> all = new ArrayList<List<AwsBrowseNode>>();
    if(node.getParents().size() == 0) {
        List<AwsBrowseNode> self = new ArrayList<AwsBrowseNode>();
        self.add(node);
        all.add(self);
    }
    else {
        for(AwsBrowseNode parent : node.getParents()) {
            List<List<AwsBrowseNode>> pall = flat(parent);
            for(List<AwsBrowseNode> p : pall) {
                p.add(node);
            }
            all.addAll(pall);
        }
    }
    return all;
}


public void printNode(AwsBrowseNode node, JspWriter out) throws Exception {
    List<List<AwsBrowseNode>> all = flat(node);
    out.write("<table cellpadding='8' border='1'>");
    out.write("<tr valign='bottom'>");
    for(List<AwsBrowseNode> path : all) {
        out.write("<td>");
        for(AwsBrowseNode n : path) {
    	    out.write("<a href='/aws/node/" + n.getId() + "'>" + n.getName() + "</a> ");
    	    out.write("<a href='javascript:void(0)' onclick=\"parent.gaoshin.addChild('" + n.getName() + "}')\"><img src='<c:url value="/m/images/right.png"/>'/></a><br/><br/>");
        }
        out.write("</td>");
    }
    out.write("</tr>");
    out.write("</table>");
}
%>

<a href='<c:url value="/aws/tops"/>'>Tops</a>

<c:forEach var="node" items="${it.list}">
<% printNode((AwsBrowseNode)pageContext.getAttribute("node"), out); %>
<br/><br/>
</c:forEach>

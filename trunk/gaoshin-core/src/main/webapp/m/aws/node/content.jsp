<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.gaoshin.amazon.AwsBrowseNode"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<a href='<c:url value="/aws/tops"/>'>Tops</a>

<div style="clear:both;height:1px;">&nbsp;</div>

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
    	    out.write("<a href='javascript:void(0)' onclick=\"parent.gaoshin.addChild('" + n.getName() + "}')\"><img src='/xo/m/images/right.png'/></a><br/><br/>");
        }
        out.write("</td>");
    }
    out.write("</tr>");
    out.write("</table>");
}
%>
<%
	AwsBrowseNode node = (AwsBrowseNode) request.getAttribute("it");
	printNode(node, out);
%>

<div style='clear:both;height:1px;'>&nbsp;</div>

<blockquote>
<c:forEach var="node" items="${it.children}">
<li><a href="/aws/node/${node.id }">${node.name}</a>
	<a href="javascript:void(0)" onclick="parent.gaoshin.addChild('${node.name}')"><img src='<c:url value="/m/images/right.png"/>'/></a>
<br/></li>
</c:forEach>
<blockquote>
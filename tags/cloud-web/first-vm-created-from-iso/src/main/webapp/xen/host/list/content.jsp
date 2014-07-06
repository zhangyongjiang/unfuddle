<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/xen/host/list?format=object&var=hosts"></jsp:include>

<div class="page-header">Host List</div>

<c:if test="${empty hosts.items }">
        No host found.
</c:if>

<c:if test="${not empty hosts.items }">
        <div><table style="border:solid 1px;"><tbody>
                        <tr>
                                <th>Name</th>
                                <th>URL</th>
                                <th>User</th>
                                <th>CPUs</th>
                                <th>Memory Total</th>
                                <th>Memory Free</th>
                                <th>Status</th>
                        </tr>

                        <c:forEach var="host" items="${hosts.items}">
                        <tr>
                                <td style="text-align:left;border:solid 1px #ddd;padding:6px;"><a style="text-decoration:none;" href="<c:url value="/xen/host/details/index.jsp.oo?hostId=${host.id}"/>">${host.name}</a></td>
                                <td style="text-align:left;border:solid 1px #ddd;padding:6px;">${host.url}&nbsp;</td>
                                <td style="text-align:left;border:solid 1px #ddd;padding:6px;">${host.user}</td>
                                <td style="text-align:left;border:solid 1px #ddd;padding:6px;">${host.cpus}</td>
                                <td style="text-align:left;border:solid 1px #ddd;padding:6px;"><fmt:formatNumber pattern="#,###" value="${host.memoryTotal}" /></td>
                                <td style="text-align:left;border:solid 1px #ddd;padding:6px;"><fmt:formatNumber pattern="#,###" value="${host.memoryFree}" /></td>
                                <td style="text-align:left;border:solid 1px #ddd;padding:6px;"><img border="0" src="<c:url value="/images/status-${host.status}.gif"/>"/> ${host.status}&nbsp;</td>
                        </tr>
                </c:forEach>

                <tr>
                        <td style="text-align:left;border:solid 1px #ddd;padding:6px;"><strong>TOTAL</strong></td>
                        <td style="text-align:left;border:solid 1px #ddd;padding:6px;"></td>
                        <td style="text-align:left;border:solid 1px #ddd;padding:6px;"></td>
                        <td style="text-align:left;border:solid 1px #ddd;padding:6px;">${hosts.cpus}</td>
                        <td style="text-align:left;border:solid 1px #ddd;padding:6px;"><fmt:formatNumber pattern="#,###" value="${hosts.memoryTotal}" /></td>
                        <td style="text-align:left;border:solid 1px #ddd;padding:6px;"><fmt:formatNumber pattern="#,###" value="${hosts.memoryFree}" /></td>
                        <td style="text-align:left;border:solid 1px #ddd;padding:6px;"></td>
                </tr>
        </tbody></table></div>
</c:if>

<button style="clear:left; margin-top:20px;" onClick='window.location="<c:url value="/xen/host/add/index.jsp.oo"/>"'>Add Host</button>

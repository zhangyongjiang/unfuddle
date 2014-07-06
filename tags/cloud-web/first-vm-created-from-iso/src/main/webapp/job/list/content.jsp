<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/common" prefix="o" %>
<%@ taglib tagdir="/WEB-INF/tags/gaoshin" prefix="g" %>

<jsp:include page="/job/list?format=object&var=jobs"></jsp:include>

<div class="page-header">Work Flow List</div>

<c:if test="${empty jobs.list }">
        No job found.
</c:if>

<c:if test="${not empty jobs.list }">
        <div><table style="border:solid 1px;"><tbody>
                        <tr>
                                <th>Name</th>
                                <th>Frequency</th>
                                <th>Job Executions</th>
                                <th>Expected Duration</th>
                                <th>Notification Email</th>
                                <th>Timer</th>
                                <th>export</th>
                                <th>Run Once Now</th>
                        </tr>

                        <c:forEach var="cron" items="${jobs.list }">
                        <tr>
                                <td style="text-align:left;border:solid 1px #ddd;padding:6px;"><a style="text-decoration:none;" href="<c:url value="/job/details/index.jsp.oo?id=${cron.id}"/>">${cron.name}</a></td>
                                <td style="text-align:left;border:solid 1px #ddd;padding:6px;">${cron.cronExpression}&nbsp;</td>
                                <td style="text-align:left;border:solid 1px #ddd;padding:6px;">
                                        <a href="<c:url value="/job/job-execution/list/index.jsp.oo?jobId=${cron.id}"/>">executions</a>
                                </td>
                                <td style="text-align:left;border:solid 1px #ddd;padding:6px;">${cron.expectedDuration}</td>
                                <td style="text-align:left;border:solid 1px #ddd;padding:6px;">${cron.notificationEmail}&nbsp;</td>
                                <td style="text-align:left;border:solid 1px #ddd;padding:6px;">
                                        <c:if test="${empty cron.cronExpression}">Freq=?</c:if>
                                        <c:if test="${not empty cron.cronExpression}">
                                        <c:set var="cmd"><c:if test="${cron.enabled}">disable</c:if><c:if test="${cron.enabled == false}">enable</c:if></c:set>
                                        <c:set var="label"><c:if test="${cron.enabled}">Stop</c:if><c:if test="${cron.enabled == false}">Start</c:if></c:set>
                                        <input type="button" onClick="${label}('${cron.id}')" value="${label}" />
                                        </c:if>
                                </td>
                                <td style="text-align:left;border:solid 1px #ddd;padding:6px;"><a href="<c:url value="/job/cron/${cron.id}?format=xml"/>">XML</a></td>
                                <td style="text-align:left;border:solid 1px #ddd;padding:6px;">
                                        <input type="button" onClick="runOnceNow('${cron.id}')" value="Run Once Now" />
                                </td>
                        </tr>
                </c:forEach>
        </tbody></table></div>
</c:if>


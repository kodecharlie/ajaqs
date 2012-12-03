<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ajq" uri="/WEB-INF/ajaqs.tld" %>

<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="com.beardediris.ajaqs.i18n.ManageUsers"/>
<ajq:setUserList logon="${pageContext.request.remoteUser}"
  var="userList" scope="page"/>

<c:set var="pageSize" scope="request">
  <ajq:getConfig param="com.beardediris.ajaqs.session.pageSize"/>
</c:set>
<c:choose>
  <c:when test="${empty param.page}">
    <c:set var="page" value="0" scope="request"/>
  </c:when>
  <c:otherwise>
    <c:set var="page" value="${param.page}" scope="request"/>
  </c:otherwise>
</c:choose>
<c:set var="begin" value="${page * pageSize}" scope="request"/>
<c:set var="end" value="${begin + pageSize - 1}" scope="request"/>

  <p class="banner">
    <fmt:message key="banner.title"/>
  </p>
  <p class="links">
    <a href="/ajaqs/jsp/admin/manageprojects.jsp">
      <fmt:message key="link.projects"/>
    </a> |
    <a href="/ajaqs/jsp/admin/manageusers.jsp">
      <fmt:message key="link.users"/>
    </a> |
    <a href="/ajaqs/servlets/security/logout">
      <fmt:message key="link.logout"/>
    </a>
  </p>
<c:forEach var="u" items="${userList}" begin="${begin}" end="${end}">
  <p class="user">
    <a href="<c:out value="/ajaqs/jsp/user/user.jsp?logon=${u.logon}"/>"><c:out value="${u.logon}"/></a>
  </p>
  <p class="infoedit">
    <c:set var="creationDate">
      <fmt:formatDate value="${u.creationDate}" type="both"
        dateStyle="medium" timeStyle="medium"/>
    </c:set>
    <fmt:message key="user.creation">
      <fmt:param value="${creationDate}"/>
    </fmt:message> - <a href="<c:out value="/ajaqs/jsp/admin/delete.jsp?type=User&logon=${u.logon}"/>">[<fmt:message key="link.delete"/>]</a> - <a href="<c:out value="/ajaqs/jsp/admin/edituser.jsp?logon=${u.logon}"/>">[<fmt:message key="link.edit"/>]</a>
  </p>
</c:forEach>
  <c:set var="numScrollable" scope="request">
    <c:out value="${fn:length(userList)}"/>
  </c:set>
  <c:set var="prevPage" scope="request">
    <c:out value="/ajaqs/jsp/admin/manageusers.jsp?page=${param.page - 1}"/>
  </c:set>
  <c:set var="nextPage" scope="request">
    <c:out value="/ajaqs/jsp/admin/manageusers.jsp?page=${param.page + 1}"/>
  </c:set>
  <jsp:include page="/jsp/site/traversal.jsp"/>

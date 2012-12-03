<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="com.beardediris.ajaqs.i18n.Traversal"/>

<c:if test="${numScrollable > 0}">
  <hr />
</c:if>
<table class="traversal" cellspacing="0" cellpadding="0">
  <tr>
    <c:set var="prevLink">
      <a href="<c:out value="${prevPage}" escapeXml="false"/>"><<</a>&nbsp;<fmt:message key="previous"/>
    </c:set>
    <c:set var="nextLink">
      <fmt:message key="next"/>&nbsp;<a href="<c:out value="${nextPage}" escapeXml="false"/>">>></a>
    </c:set>
    <c:choose>
      <c:when test="${numScrollable <= (pageSize + 0)}">
        <td colspan="3" align="center">&nbsp;</td>
      </c:when>
      <c:when test="${((begin + pageSize) >= numScrollable) && (begin > 0)}">
        <td colspan="3" align="left">
          <c:out value="${prevLink}" escapeXml="false"/></td>
      </c:when>
      <c:when test="${(page == 0) && (numScrollable > pageSize)}">
        <td colspan="3" align="right">
          <c:out value="${nextLink}" escapeXml="false"/></td>
      </c:when>
      <c:otherwise>
        <td align="left">
          <c:out value="${prevLink}" escapeXml="false"/></td>
        <td>&nbsp;</td>
        <td align="right">
          <c:out value="${nextLink}" escapeXml="false"/></td>
      </c:otherwise>
    </c:choose>
  </tr>
</table>

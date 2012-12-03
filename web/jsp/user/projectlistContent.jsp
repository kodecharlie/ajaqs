<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ajq" uri="/WEB-INF/ajaqs.tld" %>

<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="com.beardediris.ajaqs.i18n.ProjectList"/>
<ajq:setUser logon="${pageContext.request.remoteUser}"
  scope="page" var="user"/>

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
    <fmt:message key="banner.title">
      <fmt:param value="${pageContext.request.remoteUser}"/>
    </fmt:message><br />
    <span class="subtitle">
      <c:set var="lastLoginDate">
        <fmt:formatDate value="${user.lastLoginDate}" type="both"
          dateStyle="medium" timeStyle="medium"/>
      </c:set>
      <fmt:message key="banner.subtitle">
        <fmt:param value="${lastLoginDate}"/>
      </fmt:message>
    </span>
  </p>
  <p class="links">
    <a href="/ajaqs/servlets/security/logout">
      <fmt:message key="link.logout"/>
    </a>
  </p>
  <form method="POST" action="/ajaqs/jsp/user/search.jsp" class="search"
    name="searchForm">
    <input type="hidden" name="project" value="*" />
    <input type="hidden" name="faq" value="*" />
    <label for="kw">
      <fmt:message key="search.label"/>
    </label>
    <input type="text" name="keywords" id="kw" />
    <input type="submit" value="<fmt:message key="search.submit"/>" />
  </form>
  <script language="javascript">
  <!--
    document.searchForm.keywords.focus()
    // -->
  </script>
  <hr />
<c:forEach var="project" items="${user.projects}"
  begin="${begin}" end="${end}">
  <p class="project">
    <a href="<c:out value="/ajaqs/jsp/user/projectpage.jsp?project=${project.id}"/>">
      <c:out value="${project.name}"/></a> -
    <a href="<c:out value="/ajaqs/jsp/user/browse.jsp?project=${project.id}&faq=*"/>" class="projlink">
      [<fmt:message key="link.browse"/>]</a> -
    <a href="<c:out value="/ajaqs/jsp/user/search.jsp?project=${project.id}&faq=*"/>" class="projlink">
      [<fmt:message key="link.search"/>]</a> -
    <a href="<c:out value="/ajaqs/jsp/feeds/rssproject.jsp?logon=${user.logon}&project=${project.id}"/>" class="rsslink">
      [RSS 2.0]
    </a> -
    <a href="<c:out value="/ajaqs/servlets/user/faq.pdf?logon=${user.logon}&project=${project.id}"/>" class="pdflink">
      [PDF]
    </a>
  </p>
  <c:forEach var="faq" items="${project.faqs}"
    begin="0" end="3" varStatus="status">
    <c:choose>
      <c:when test="${status.index == 3}">
        <p class="faq">...</p>
      </c:when>
      <c:otherwise>
        <p class="faq">
          <a href="<c:out value="/ajaqs/jsp/user/browse.jsp?project=${project.id}&faq=${faq.id}"/>">
            <c:out value="${faq.name}"/>
          </a>
        </p>
      </c:otherwise>
    </c:choose>
  </c:forEach>
</c:forEach>
  <c:set var="numScrollable" scope="request">
    <c:out value="${fn:length(user.projects)}"/>
  </c:set>
  <c:set var="prevPage" scope="request">
    <c:out value="/ajaqs/jsp/user/projectlist.jsp?page=${param.page - 1}"/>
  </c:set>
  <c:set var="nextPage" scope="request">
    <c:out value="/ajaqs/jsp/user/projectlist.jsp?page=${param.page + 1}"/>
  </c:set>
  <jsp:include page="/jsp/site/traversal.jsp"/>

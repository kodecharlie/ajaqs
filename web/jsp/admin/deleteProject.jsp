<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tmplt" uri="/WEB-INF/templates.tld" %>
<%@ taglib prefix="ajq" uri="/WEB-INF/ajaqs.tld" %>

<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="com.beardediris.ajaqs.i18n.Delete"/>
<ajq:setUser logon="${pageContext.request.remoteUser}"
  scope="page" var="user"/>
<ajq:setProject project="${param.project}" userid="user"
  scope="page" var="project"/>

<c:set var="action">
  <c:out value="/ajaqs/servlets/admin/deleteproject?project=${project.id}"/>
</c:set>

<c:set var="banner">
  <fmt:message key="banner.title">
    <fmt:param value="${project.name}"/>
  </fmt:message>
</c:set>

<c:set var="links">
  <a href="/ajaqs/jsp/admin/manageprojects.jsp">
    <fmt:message key="link.projects"/>
  </a> |
  <a href="/ajaqs/jsp/admin/manageusers.jsp">
    <fmt:message key="link.users"/>
  </a> |
  <a href="/ajaqs/servlets/security/logout">
    <fmt:message key="link.logout"/>
  </a>
</c:set>

<c:set var="request">
  <fmt:message key="delete.request">
    <fmt:param value="${param.type}"/>
  </fmt:message>
</c:set>

<c:set var="confirm">
  <fmt:message key="delete.confirm">
    <fmt:param value="${param.type}"/>
  </fmt:message>
</c:set>

<c:set var="error">
  <fmt:message key="error.msg">
    <fmt:param value="${param.type}"/>
  </fmt:message>
</c:set>

<c:set var="submit">
  <fmt:message key="form.submit"/>
</c:set>

<tmplt:insert template="/jsp/admin/deleteTemplate.jsp">
  <tmplt:put name="action" content="${action}" direct="true" />
  <tmplt:put name="banner" content="${banner}" direct="true" />
  <tmplt:put name="links" content="${links}" direct="true" />
  <tmplt:put name="request" content="${request}" direct="true" />
  <tmplt:put name="synopsis" content="${project.name}" direct="true" />
  <tmplt:put name="confirm" content="${confirm}" direct="true" />
  <tmplt:put name="error" content="${error}" direct="true" />
  <tmplt:put name="submit" content="${submit}" direct="true" />
</tmplt:insert>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ajq" uri="/WEB-INF/ajaqs.tld" %>
<%@ taglib prefix="tmplt" uri="/WEB-INF/templates.tld" %>

<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="com.beardediris.ajaqs.i18n.Browse"/>
<ajq:setUser logon="${pageContext.request.remoteUser}"
  scope="request" var="user"/>
<ajq:setProject project="${param.project}" userid="user"
  scope="request" var="project"/>

<c:set var="title">
  <fmt:message key="head.title">
    <fmt:param value="${project.name}"/>
  </fmt:message>
</c:set>

<tmplt:insert template="/jsp/site/pageTemplate.jsp">
  <tmplt:put name="styles"
    content="/ajaqs/css/user/browse.css" direct="true" />
  <tmplt:put name="title" content="${title}" direct="true" />
  <tmplt:put name="header" content="/jsp/site/header.jsp" />
  <tmplt:put name="sidebar" content="/jsp/site/sidebar.jsp" />
  <tmplt:put name="content" content="/jsp/user/browseContent.jsp"/>
  <tmplt:put name="footer" content="/jsp/site/footer.jsp"/>
</tmplt:insert>

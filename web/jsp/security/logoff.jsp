<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tmplt" uri="/WEB-INF/templates.tld" %>
<%@ page session="false" %>

<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="com.beardediris.ajaqs.i18n.Logoff"/>

<c:set var="title">
  <fmt:message key="head.title"/>
</c:set>

<tmplt:insert template="/jsp/site/pageTemplate.jsp">
  <tmplt:put name="styles"
    content="/ajaqs/css/security/logoff.css" direct="true" />
  <tmplt:put name="title" content="${title}" direct="true" />
  <tmplt:put name="header" content="/jsp/site/header.jsp" />
  <tmplt:put name="sidebar" content="/jsp/site/sidebar.jsp" />
  <tmplt:put name="content" content="/jsp/security/logoffContent.jsp"/>
  <tmplt:put name="footer" content="/jsp/site/footer.jsp"/>
</tmplt:insert>

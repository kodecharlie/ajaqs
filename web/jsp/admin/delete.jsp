<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tmplt" uri="/WEB-INF/templates.tld" %>

<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="com.beardediris.ajaqs.i18n.Delete"/>

<c:set var="title">
  <fmt:message key="head.title">
    <fmt:param value="${param.type}"/>
  </fmt:message>
</c:set>

<c:set var="delpage" value="/jsp/admin/delete${param.type}.jsp"/>

<tmplt:insert template="/jsp/site/pageTemplate.jsp">
  <tmplt:put name="styles"
    content="/ajaqs/css/admin/delete.css" direct="true" />
  <tmplt:put name="title" content="${title}" direct="true" />
  <tmplt:put name="header" content="/jsp/site/header.jsp" />
  <tmplt:put name="sidebar" content="/jsp/site/sidebar.jsp" />
  <tmplt:put name="content" content="${delpage}"/>
  <tmplt:put name="footer" content="/jsp/site/footer.jsp"/>
</tmplt:insert>

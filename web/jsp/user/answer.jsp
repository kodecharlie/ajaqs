<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ajq" uri="/WEB-INF/ajaqs.tld" %>
<%@ taglib prefix="tmplt" uri="/WEB-INF/templates.tld" %>

<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="com.beardediris.ajaqs.i18n.Answer"/>
<ajq:setUser logon="${pageContext.request.remoteUser}"
  scope="request" var="user"/>
<ajq:setProject project="${param.project}" userid="user"
  scope="request" var="project"/>
<ajq:setFaq faq="${param.faq}" projectid="project"
  scope="request" var="faq"/>
<ajq:setQuestion question="${param.question}" faqid="faq"
  scope="request" var="question"/>

<c:set var="title">
  <c:out value="${question.question}"/>
</c:set>

<tmplt:insert template="/jsp/site/pageTemplate.jsp">
  <tmplt:put name="styles"
    content="/ajaqs/css/user/answer.css" direct="true" />
  <tmplt:put name="title" content="${title}" direct="true" />
  <tmplt:put name="header" content="/jsp/site/header.jsp" />
  <tmplt:put name="sidebar" content="/jsp/site/sidebar.jsp" />
  <tmplt:put name="content" content="/jsp/user/answerContent.jsp"/>
  <tmplt:put name="footer" content="/jsp/site/footer.jsp"/>
</tmplt:insert>

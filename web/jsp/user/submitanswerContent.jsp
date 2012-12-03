<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ajq" uri="/WEB-INF/ajaqs.tld" %>

<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="com.beardediris.ajaqs.i18n.SubmitAnswer"/>
<ajq:setUser logon="${pageContext.request.remoteUser}"
  scope="page" var="user"/>
<ajq:setProject project="${param.project}" userid="user"
  scope="page" var="project"/>
<ajq:setFaq faq="${param.faq}" projectid="project"
  scope="page" var="faq"/>
<ajq:setQuestion question="${param.question}" faqid="faq"
  scope="page" var="question"/>

<form method="POST" enctype="multipart/form-data"
  action="/ajaqs/servlets/user/newanswer" name="ansForm">
  <input type="hidden" name="project"
    value="<c:out value="${project.id}"/>" />
  <input type="hidden" name="faq"
    value="<c:out value="${faq.id}"/>" />
  <input type="hidden" name="question"
    value="<c:out value="${question.id}"/>" />
  <p class="banner">
    <c:out value="${question.question}"/>
  </p>
  <p class="links">
    <a href="/ajaqs/jsp/user/projectlist.jsp">
      <fmt:message key="link.projects"/>
    </a> |
    <a href="<c:out value="/ajaqs/jsp/user/browse.jsp?project=${project.id}&faq=${faq.id}"/>">
      <fmt:message key="link.browse"/>
    </a> |
    <a href="/ajaqs/servlets/security/logout">
      <fmt:message key="link.logout"/>
    </a>
  </p>
<c:if test="${!empty param.error}">
  <p class="error">
    <fmt:message key="error.emptyanswer"/>
  </p>
</c:if>
  <table summary="<fmt:message key="inputs.summary"/>"
    border="0" cellpadding="3" cellspacing="15">
    <tr>
      <th colspan="3" align="left">
        <label for="ans">
          <fmt:message key="inputs.textarea"/>:
        </label><br />
        <textarea name="answer" cols="80" rows="8" id="ans"></textarea>
      </th>
    </tr>
  </table>
  <input type="submit" value="<fmt:message key="inputs.submit"/>" />
</form>
<script language="javascript">
<!--
  document.ansForm.answer.focus()
  // -->
</script>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ajq" uri="/WEB-INF/ajaqs.tld" %>

<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="com.beardediris.ajaqs.i18n.SubmitQuestion"/>
<ajq:setUser logon="${pageContext.request.remoteUser}"
  scope="page" var="user"/>
<ajq:setProject project="${param.project}" userid="user"
  scope="page" var="project"/>

  <p class="banner">
    <fmt:message key="banner.title">
      <fmt:param value="${project.name}"/>
    </fmt:message>
  </p>
  <p class="links">
    <a href="/ajaqs/jsp/user/projectlist.jsp">
      <fmt:message key="link.projects"/>
    </a> |
    <a href="<c:out value="/ajaqs/jsp/user/browse.jsp?project=${project.id}&faq=*"/>">
      <fmt:message key="link.browse"/>
    </a> |
    <a href="/ajaqs/servlets/security/logout">
      <fmt:message key="link.logout"/>
    </a>
  </p>
<c:if test="${!empty param.error}">
  <p class="error">
    <fmt:message key="error.emptyquestion"/>
  </p>
</c:if>
<form method="POST" enctype="multipart/form-data"
  action="/ajaqs/servlets/user/newquestion" name="questForm">
  <input type="hidden" name="logon"
    value="<c:out value="${user.logon}"/>" />
  <input type="hidden" name="project"
    value="<c:out value="${project.id}"/>" />
  <table summary="<fmt:message key="inputs.summary"/>"
    border="0" cellpadding="3" cellspacing="15">
    <tr>
      <th colspan="2" align="left">
        <label for="selfaq">
          <fmt:message key="selfaq.label"/>:
        </label>
        <select name="faq" id="selfaq">
          <c:forEach var="faq" items="${project.faqs}" varStatus="status">
            <option value="<c:out value="${faq.id}"/>"
            <c:if test="${(!empty param.faq && param.faq == faq.id)
                          || status.first}">
              selected="selected"
            </c:if>
            ><c:out value="${faq.name}"/></option>
          </c:forEach>
        </select>
      </th>
    </tr>
    <tr>
      <th colspan="2" align="left">
        <label for="ask">
          <fmt:message key="question.ask"/>:
        </label><br />
        <textarea name="question" cols="80" rows="3" id="ask"></textarea>
      </th>
    </tr>
    <tr>
      <th colspan="2" align="left">
        <label for="ans">
          <fmt:message key="question.answer"/>:
        </label><br />
        <textarea name="answer" cols="80" rows="8" id="ans"></textarea>
      </th>
    </tr>
  </table>
  <input type="submit" value="<fmt:message key="question.submit"/>" />
</form>
<script language="javascript">
<!--
  document.questForm.faq.focus()
  // -->
</script>

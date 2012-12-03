<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ajq" uri="/WEB-INF/ajaqs.tld" %>

<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="com.beardediris.ajaqs.i18n.EditAnswer"/>

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
<c:if test="${!empty param.error && param.error == 'emptyanswer'}">
  <p class="error">
    <fmt:message key="error.emptyanswer"/>
  </p>
</c:if>
<form method="POST" enctype="multipart/form-data"
  action="/ajaqs/servlets/admin/editanswer" name="editanswerForm">
  <input type="hidden" name="project"
    value="<c:out value="${project.id}"/>" />
  <input type="hidden" name="faq"
    value="<c:out value="${faq.id}"/>" />
  <input type="hidden" name="question"
    value="<c:out value="${question.id}"/>" />
  <input type="hidden" name="answer"
    value="<c:out value="${answer.id}"/>" />
  <table summary="<fmt:message key="form.summary"/>"
    border="0" cellpadding="3" cellspacing="3" align="center">
    <tr>
      <th align="left">
        <label for="ask">
          <fmt:message key="form.answer"/>
        </label><br />
        <textarea name="astring" cols="80" rows="3" id="ask"><c:out value="${answer.answer}"/></textarea>
      </th>
    </tr>
    <tr>
      <th align="left">
        <input type="submit" value="<fmt:message key="form.submit"/>" />
      </th>
    </tr>
  </table>
</form>
<script language="javascript">
<!--
  document.editanswerForm.astring.focus()
  // -->
</script>
  <hr />
<c:forEach var="attachment" items="${answer.attachments}">
  <p class="attachment">
    <a href="<c:out value="/ajaqs/servlets/user/download?project=${project.id}&faq=${faq.id}&question=${question.id}&answer=${answer.id}&attachment=${attachment.id}"/>"><c:out value="${attachment.fileName}"/></a>
  </p>
  <p class="descr">
    <c:out value="${attachment.descr}"/>
  </p>
  <p class="date">
    <c:set var="creationDate">
      <fmt:formatDate value="${attachment.creationDate}" type="both"
        dateStyle="medium" timeStyle="medium"/>
    </c:set>
    <fmt:message key="attachment.creation">
      <fmt:param value="${creationDate}"/>
    </fmt:message> - <a href="<c:out value="/ajaqs/jsp/admin/delete.jsp?type=Attachment&project=${project.id}&faq=${faq.id}&question=${question.id}&answer=${answer.id}&attachment=${attachment.id}"/>">[<fmt:message key="link.delete"/>]</a>
  </p>
</c:forEach>
<c:if test="${fn:length(answer.attachments) > 0}">
  <hr />
</c:if>

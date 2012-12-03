<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="com.beardediris.ajaqs.i18n.NewFaq"/>

  <p class="banner">
    <fmt:message key="banner.title">
      <fmt:param value="${project.name}"/>
    </fmt:message>
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
<c:if test="${!empty param.error}">
  <p class="error">
  <c:choose>
    <c:when test="${param.error == 'noname'}">
      <fmt:message key="error.noname"/>
    </c:when>
    <c:otherwise>
      <fmt:message key="error.duplicate">
        <fmt:param value="${project.name}"/>
      </fmt:message>
    </c:otherwise>
  </c:choose>
  </p>
</c:if>
<form method="POST" action="/ajaqs/servlets/admin/newfaq"
  class="userinputs" name="newfaqForm">
  <input type="hidden" name="project"
    value="<c:out value="${project.id}"/>" />
  <table summary="<fmt:message key="form.summary"/>"
    border="0" cellpadding="3" cellspacing="3" align="center">
    <tr>
      <th align="right"><label for="name">
        <fmt:message key="form.name"/></label></th>
      <td>
        <input type="text" name="name" size="32" id="name" />
      </td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <th align="left">
        <input type="submit" value="<fmt:message key="form.submit"/>" />
      </th>
    </tr>
  </table>
</form>
<script language="javascript">
<!--
  document.newfaqForm.name.focus()
  // -->
</script>
<hr />

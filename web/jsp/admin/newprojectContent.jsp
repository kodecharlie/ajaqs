<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ajq" uri="/WEB-INF/ajaqs.tld" %>

<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="com.beardediris.ajaqs.i18n.NewProject"/>
<ajq:setUserList logon="${pageContext.request.remoteUser}"
  var="userList" scope="page"/>

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
<c:if test="${!empty param.error}">
  <p class="error">
  <c:choose>
    <c:when test="${param.error == 'noname'}">
      <fmt:message key="error.noname"/>
    </c:when>
    <c:when test="${param.error == 'nousers'}">
      <fmt:message key="error.nousers"/>
    </c:when>
    <c:otherwise>
      <fmt:message key="error.duplicate"/>
    </c:otherwise>
  </c:choose>
  </p>
</c:if>
<form method="POST" action="/ajaqs/servlets/admin/newproject"
  class="userinputs" name="newprojectForm">
  <table summary="<fmt:message key="form.summary"/>"
    border="0" cellpadding="3" cellspacing="3" align="center">
    <tr>
      <th align="right"><label for="name">
        <fmt:message key="form.name"/></label></th>
      <td>
        <input type="text" name="name" size="16" id="name"
          <c:if test="${!empty param.projname}">
            value="<c:out value="${param.projname}"/>"
          </c:if> />
      </td>
    </tr>
    <tr>
      <th align="right"><label for="selusrs">
          <fmt:message key="form.users"/></label></th>
      <td>
        <select name="users" multiple="multiple" size="10"
          id="selusrs">
          <option value="all" selected="selected">
            <fmt:message key="selusrs.all"/>
          </option>
        <c:forEach var="u" items="${userList}">
          <option value="<c:out value="${u.logon}"/>">
            <c:out value="${u.logon}"/>
          </option>
        </c:forEach>
        </select>
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
  document.newprojectForm.name.focus()
  // -->
</script>
<hr />

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ajq" uri="/WEB-INF/ajaqs.tld" %>

<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="com.beardediris.ajaqs.i18n.Register"/>
<ajq:setProjList var="projList" scope="page"/>

  <p class="banner">
    <fmt:message key="banner.title"/>
  </p>
<c:if test="${!empty param.error}">
  <p class="error">
  <c:choose>
    <c:when test="${param.error == 'emptylogon'}">
      <fmt:message key="error.emptylogon"/>
    </c:when>
    <c:when test="${param.error == 'passwords'}">
      <fmt:message key="error.passwords"/>
    </c:when>
    <c:otherwise>
      <fmt:message key="error.duplicate"/>
    </c:otherwise>
  </c:choose>
  </p>
</c:if>
<form method="POST" action="/ajaqs/servlets/security/register"
  class="register" name="regForm">
  <table align="center"
    summary="<fmt:message key="form.summary"/>"
    border="0" cellpadding="3" cellspacing="3">
    <tr>
      <th align="right" width="40%"><label for="logon">
        <fmt:message key="form.logon"/></label></th>
      <td>
        <input type="text" name="logon" size="16" id="logon" />
      </td>
    </tr>
    <tr>
      <th align="right"><label for="pw">
        <fmt:message key="form.password"/></label></th>
      <td>
        <input type="password" name="password" size="16" id="pw" />
      </td>
    </tr>
    <tr>
      <th align="right"><label for="rtpw">
        <fmt:message key="form.retype"/></label></th>
      <td>
        <input type="password" name="retype" size="16" id="rtpw" />
      </td>
    </tr>
    <tr>
      <th align="right"><label for="em">
        <fmt:message key="form.email"/></label></th>
      <td>
        <input type="text" name="email" size="16" id="em" />
      </td>
    </tr>
    <tr>
      <th align="right"><label for="selpjs">
        <fmt:message key="form.projects"/></label></th>
      <td>
        <select name="projects" multiple="multiple" size="10"
          id="selpjs">
          <option value="all" selected="selected">
            <fmt:message key="selpjs.all"/>
          </option>
        <c:forEach var="p" items="${projList}">
          <option value="<c:out value="${p.id}"/>">
            <c:out value="${p.name}"/>
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
  document.regForm.logon.focus()
  // -->
</script>

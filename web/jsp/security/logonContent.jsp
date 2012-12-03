<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="com.beardediris.ajaqs.i18n.Logon"/>

  <p class="banner">
    <fmt:message key="banner.title"/>
  </p>
<c:if test="${!empty param.error}">
  <p class="error">
    <fmt:message key="error.msg"/>
  </p>
</c:if>
<form method="POST" action="j_security_check" class="validation"
  name="loginForm">
  <table align="center"
    summary="<fmt:message key="form.summary"/>"
    border="0" cellpadding="3" cellspacing="3">
    <tr>
      <th align="right"><label for="user">
        <fmt:message key="form.user"/></label></th>
      <td>
        <input type="text" name="j_username" size="16" id="user"
          tabindex="1" />
      </td>
      <td>
        <a href="/ajaqs/jsp/security/register.jsp">
          <fmt:message key="link.newuser"/></a>
      </td>
    </tr>
    <tr>
      <th align="right">
        <label for="pw">
          <fmt:message key="form.password"/>
        </label>
      </th>
      <td colspan="2">
        <input type="password" name="j_password" size="16" id="pw"
          tabindex="2" />
      </td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <th align="left" colspan="2">
        <input type="submit" value="<fmt:message key="form.submit"/>"
          tabindex="3" />
      </th>
    </tr>
  </table>
</form>

<script language="javascript">
<!--
  document.loginForm.j_username.focus()
  // -->
</script>

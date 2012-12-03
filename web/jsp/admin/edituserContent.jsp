<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ajq" uri="/WEB-INF/ajaqs.tld" %>

<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="com.beardediris.ajaqs.i18n.EditUser"/>
<ajq:setUser logon="${param.logon}" scope="page" var="user"/>
<ajq:setProjList logon="${pageContext.request.remoteUser}"
  var="projList" scope="page" all="true"/>
<ajq:setRoleList logon="${pageContext.request.remoteUser}"
  var="roleList" scope="page"/>

  <p class="banner">
    <fmt:message key="banner.title">
      <fmt:param value="${user.logon}"/>
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
<form method="POST" action="/ajaqs/servlets/admin/edituser"
  class="userinputs" name="edituserForm">
  <input type="hidden" name="logon"
    value="<c:out value="${param.logon}"/>" />
  <table summary="<fmt:message key="form.summary"/>"
    border="0" cellpadding="3" cellspacing="3" align="center">
    <tr>
      <th align="right"><label for="email">
        <fmt:message key="form.email"/></label></th>
      <td>
        <input type="text" name="email" size="16" id="email"
          value="<c:out value="${user.email}"/>" />
      </td>
    </tr>
    <tr>
      <th align="right"><label for="selst">
          <fmt:message key="form.state"/></label></th>
      <td>
        <select name="state" id="selst">
          <option value="1"
            <c:if test="${user.state == 1}">
              selected="selected"
            </c:if>
            ><fmt:message key="selst.active"/>
          </option>
          <option value="0"
            <c:if test="${user.state == 0}">
              selected="selected"
            </c:if>
            ><fmt:message key="selst.inactive"/>
          </option>
        </select>
      </td>
    </tr>
    <tr>
      <th align="right"><label for="selroles">
        <fmt:message key="form.roles"/></label></th>
      <td>
        <select name="roles" multiple="multiple" size="5"
          id="selroles">
          <option value="all">
            <fmt:message key="selroles.all"/>
          </option>
        <c:forEach var="r" items="${roleList}">
          <option value="<c:out value="${r.roleName}"/>"
            <ajq:ifContains collection="${user.roles}" item="${r}">
              selected="selected"
            </ajq:ifContains>
            ><c:out value="${r.roleName}"/>
          </option>
        </c:forEach>
        </select>
      </td>
    </tr>
    <tr>
      <th align="right"><label for="selpjs">
          <fmt:message key="form.projects"/></label></th>
      <td>
        <select name="projects" multiple="multiple" size="10"
          id="selpjs">
          <option value="all">
            <fmt:message key="selpjs.all"/>
          </option>
        <c:forEach var="p" items="${projList}">
          <option value="<c:out value="${p.id}"/>"
            <ajq:ifContains collection="${user.projects}" item="${p}">
              selected="selected"
            </ajq:ifContains>
            ><c:out value="${p.name}"/>
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
  document.edituserForm.email.focus()
  // -->
</script>
<hr />

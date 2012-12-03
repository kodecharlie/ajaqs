<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ajq" uri="/WEB-INF/ajaqs.tld" %>

<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="com.beardediris.ajaqs.i18n.User"/>
<ajq:setUser logon="${pageContext.request.remoteUser}"
  scope="page" var="curUser"/>

  <p class="banner">
    <fmt:message key="head.title">
      <fmt:param value="${otherUser.logon}"/>
    </fmt:message>
  </p>
  <p class="links">
    <a href="/ajaqs/jsp/user/projectlist.jsp">
      <fmt:message key="link.projects"/>
    </a> |
    <a href="/ajaqs/servlets/security/logout">
      <fmt:message key="link.logout"/>
    </a>
  </p>
  <table summary="<fmt:message key="info.summary"/>"
    border="0" cellpadding="3" cellspacing="3" align="center">
    <tr>
      <th align="right"><fmt:message key="info.logon"/>:</th>
      <td>
        <c:out value="${otherUser.logon}"/>
      </td>
    </tr>
    <tr>
      <th align="right"><fmt:message key="info.email"/>:</th>
      <td>
        <c:out value="${otherUser.email}"/>
      </td>
    </tr>
    <tr>
      <th align="right"><fmt:message key="info.lastLogin"/>:</th>
      <td>
        <fmt:formatDate value="${otherUser.lastLoginDate}" type="both"
          dateStyle="medium" timeStyle="medium"/>
      </td>
    </tr>
    <tr>
      <th align="right"><fmt:message key="info.state"/>:</th>
      <td>
        <c:choose>
          <c:when test="${otherUser.state == 1}">
            <fmt:message key="info.state.active"/>
          </c:when>
          <c:otherwise>
            <fmt:message key="info.state.inactive"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr valign="top">
      <th align="right"><fmt:message key="info.projects"/>:</th>
      <td>
        <select name="projects" multiple="multiple" size="10">
          <c:forEach var="project" items="${otherUser.projects}"
            varStatus="status">
            <ajq:setProject project="${project.id}" userid="curUser"
              scope="page" var="curProj" emptyOk="true"/>
            <c:if test="${!empty curProj}">
              <option><c:out value="${curProj.name}"/></option>
            </c:if>
          </c:forEach>
        </select>
      </td>
    </tr>
  </table>

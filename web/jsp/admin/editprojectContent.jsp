<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ajq" uri="/WEB-INF/ajaqs.tld" %>

<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="com.beardediris.ajaqs.i18n.EditProject"/>
<ajq:setUserList logon="${pageContext.request.remoteUser}"
  var="userList" scope="page"/>

<c:set var="pageSize" scope="request">
  <ajq:getConfig param="com.beardediris.ajaqs.session.pageSize"/>
</c:set>
<c:choose>
  <c:when test="${empty param.page}">
    <c:set var="page" value="0" scope="request"/>
  </c:when>
  <c:otherwise>
    <c:set var="page" value="${param.page}" scope="request"/>
  </c:otherwise>
</c:choose>
<c:set var="begin" value="${page * pageSize}" scope="request"/>
<c:set var="end" value="${begin + pageSize - 1}" scope="request"/>

  <p class="banner">
    <fmt:message key="banner.title">
      <fmt:param value="${project.name}"/>
    </fmt:message>
  </p>
  <p class="links">
    <a href="<c:out value="/ajaqs/jsp/admin/newfaq.jsp?project=${project.id}"/>">
      <fmt:message key="link.addfaq"/>
    </a> |
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
<c:if test="${(empty param.page) || (param.page == 0)}">
  <form method="POST" action="/ajaqs/servlets/admin/editproject"
    name="editprojectForm">
    <input type="hidden" name="project"
      value="<c:out value="${project.id}"/>" />
    <table summary="<fmt:message key="form.summary"/>"
      border="0" cellpadding="3" cellspacing="3" align="center">
      <tr>
        <th align="right"><label for="name">
          <fmt:message key="form.name"/></label></th>
        <td>
          <input type="text" name="name" size="16" id="name"
            value="<c:out value="${project.name}"/>" />
        </td>
      </tr>
      <tr>
        <th align="right"><label for="selst">
            <fmt:message key="form.state"/></label></th>
        <td>
          <select name="state" id="selst">
            <option value="1"
              <c:if test="${project.state == 1}">
                selected="selected"
              </c:if>
              ><fmt:message key="selst.active"/>
            </option>
            <option value="0"
              <c:if test="${project.state == 0}">
                selected="selected"
              </c:if>
              ><fmt:message key="selst.inactive"/>
            </option>
          </select>
        </td>
      </tr>
      <tr>
        <th align="right"><label for="selusers">
            <fmt:message key="form.users"/></label></th>
        <td>
          <select name="users" multiple="multiple" size="5"
            id="selusers">
            <option value="all">
              <fmt:message key="selusers.all"/>
            </option>
          <c:forEach var="u" items="${userList}">
            <option value="<c:out value="${u.logon}"/>"
              <ajq:ifContains collection="${u.projects}" item="${project}">
                selected="selected"
              </ajq:ifContains>
              ><c:out value="${u.logon}"/>
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
    document.editprojectForm.name.focus()
    // -->
  </script>
  <hr />
</c:if>
<c:forEach var="f" items="${project.faqs}" begin="${begin}" end="${end}">
  <p class="faq">
    <a href="<c:out value="/ajaqs/jsp/user/browse.jsp?project=${project.id}&faq=${f.id}"/>"><c:out value="${f.name}"/></a>
  </p>
  <p class="infoedit">
    <c:set var="creationDate">
      <fmt:formatDate value="${f.creationDate}" type="both"
        dateStyle="medium" timeStyle="medium"/>
    </c:set>
    <fmt:message key="faq.creation">
      <fmt:param value="${creationDate}"/>
    </fmt:message> - <a href="<c:out value="/ajaqs/jsp/admin/delete.jsp?type=FAQ&project=${project.id}&faq=${f.id}"/>">[<fmt:message key="link.delete"/>]</a> - <a href="<c:out value="/ajaqs/jsp/admin/editfaq.jsp?project=${project.id}&faq=${f.id}"/>">[<fmt:message key="link.edit"/>]</a>
  </p>
</c:forEach>
  <c:set var="numScrollable" scope="request">
    <c:out value="${fn:length(project.faqs)}"/>
  </c:set>
  <c:set var="prevPage" scope="request">
    <c:out value="/ajaqs/jsp/admin/editproject.jsp?project=${project.id}&page=${param.page - 1}"/>
  </c:set>
  <c:set var="nextPage" scope="request">
    <c:out value="/ajaqs/jsp/admin/editproject.jsp?project=${project.id}&page=${param.page + 1}"/>
  </c:set>
  <jsp:include page="/jsp/site/traversal.jsp"/>

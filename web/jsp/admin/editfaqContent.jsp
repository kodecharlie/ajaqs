<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ajq" uri="/WEB-INF/ajaqs.tld" %>

<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="com.beardediris.ajaqs.i18n.EditFaq"/>

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
      <fmt:param value="${faq.name}"/>
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
      <fmt:message key="error.duplicate"/>
    </c:otherwise>
  </c:choose>
  </p>
</c:if>
<c:if test="${(empty param.page) || (param.page == 0)}">
  <form method="POST" action="/ajaqs/servlets/admin/editfaq"
    name="editfaqForm">
    <input type="hidden" name="project"
      value="<c:out value="${project.id}"/>" />
    <input type="hidden" name="faq"
      value="<c:out value="${faq.id}"/>" />
    <table summary="<fmt:message key="form.summary"/>"
      border="0" cellpadding="3" cellspacing="3" align="center">
      <tr>
        <th align="right"><label for="name">
          <fmt:message key="form.name"/></label></th>
        <td>
          <input type="text" name="name" size="16" id="name"
            value="<c:out value="${faq.name}"/>" />
        </td>
      </tr>
      <tr>
        <th align="right"><label for="selst">
            <fmt:message key="form.state"/></label></th>
        <td>
          <select name="state" id="selst">
            <option value="1"
              <c:if test="${faq.state == 1}">
                selected="selected"
              </c:if>
              ><fmt:message key="selst.active"/>
            </option>
            <option value="0"
              <c:if test="${faq.state == 0}">
                selected="selected"
              </c:if>
              ><fmt:message key="selst.inactive"/>
            </option>
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
    document.editfaqForm.name.focus()
    // -->
  </script>
  <hr />
</c:if>
<c:forEach var="q" items="${faq.questions}" begin="${begin}" end="${end}">
  <p class="question">
    <a href="<c:out value="/ajaqs/jsp/user/answer.jsp?project=${project.id}&faq=${faq.id}&question=${q.id}"/>"><c:out value="${q.question}"/></a>
  </p>
  <p class="infoedit">
    <c:set var="creationDate">
      <fmt:formatDate value="${q.creationDate}" type="both"
        dateStyle="medium" timeStyle="medium"/>
    </c:set>
    <fmt:message key="question.creation">
      <fmt:param value="${creationDate}"/>
    </fmt:message> - <a href="<c:out value="/ajaqs/jsp/admin/delete.jsp?type=Question&project=${project.id}&faq=${faq.id}&question=${q.id}"/>">[<fmt:message key="link.delete"/>]</a> - <a href="<c:out value="/ajaqs/jsp/admin/editquestion.jsp?project=${project.id}&faq=${faq.id}&question=${q.id}"/>">[<fmt:message key="link.edit"/>]</a>
  </p>
</c:forEach>
  <c:set var="numScrollable" scope="request">
    <c:out value="${fn:length(faq.questions)}"/>
  </c:set>
  <c:set var="prevPage" scope="request">
    <c:out value="/ajaqs/jsp/admin/editfaq.jsp?project=${project.id}&faq=${faq.id}&page=${param.page - 1}"/>
  </c:set>
  <c:set var="nextPage" scope="request">
    <c:out value="/ajaqs/jsp/admin/editfaq.jsp?project=${project.id}&faq=${faq.id}&page=${param.page + 1}"/>
  </c:set>
  <jsp:include page="/jsp/site/traversal.jsp"/>

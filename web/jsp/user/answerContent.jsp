<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ajq" uri="/WEB-INF/ajaqs.tld" %>

<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="com.beardediris.ajaqs.i18n.Answer"/>

<c:set var="numAnswers">
  <c:out value="${fn:length(question.answers)}"/>
</c:set>

  <p class="banner">
    <c:choose>
      <c:when test="${!empty param.keywords}">
        <ajq:emphasize cssClass="searchword"
          keywords="${param.keywords}" text="${question.question}"/>
      </c:when>
      <c:otherwise>
        <c:out value="${question.question}"/>
      </c:otherwise>
    </c:choose>
    <c:if test="${numAnswers > 0}">
      <br />
      <span class="subtitle">
        <c:set var="lastUpdate">
          <fmt:formatDate value="${question.lastAnswer.creationDate}"
            type="both" dateStyle="medium" timeStyle="medium"/>
        </c:set>
        <fmt:message key="banner.subtitle">
          <fmt:param value="${lastUpdate}"/>
        </fmt:message>
      </span>
    </c:if>
  </p>
  <p class="links">
    <a href="/ajaqs/jsp/user/projectlist.jsp">
      <fmt:message key="link.projects"/>
    </a> |
    <a href="<c:out value="/ajaqs/jsp/user/browse.jsp?project=${project.id}&faq=${faq.id}"/>">
      <fmt:message key="link.browse"/>
    </a> |
    <a href="/ajaqs/servlets/security/logout">
      <fmt:message key="link.logout"/>
    </a>
  </p>
  <ol>
  <c:forEach var="a" items="${question.answers}">
    <li>
      <p class="answer">
        <c:choose>
          <c:when test="${!empty param.keywords}">
            <ajq:emphasize cssClass="searchword"
              keywords="${param.keywords}" text="${a.answer}"/>
          </c:when>
          <c:otherwise>
            <c:out value="${a.answer}" escapeXml="false"/>
          </c:otherwise>
        </c:choose>
      </p>
      <table border="0" cellspacing="0" cellpadding="2"
        summary="<fmt:message key="answer.summary"/>">
        <tr>
          <td colspan="5">
            <c:set var="anchor">
              <a href="/ajaqs/jsp/user/user.jsp?logon=<c:out value="${a.user.logon}"/>"><c:out value="${a.user.logon}"/></a>
            </c:set>
            <c:set var="answerDate">
              <fmt:formatDate value="${a.creationDate}"
                type="both" dateStyle="medium" timeStyle="medium"/>
            </c:set>
            <p class="userinfo">
              <fmt:message key="answer.submitter">
                <fmt:param value="${anchor}"/>
                <fmt:param value="${answerDate}"/>
              </fmt:message>
            </p>
          </td>
        </tr>
    <c:forEach var="b" items="${a.attachments}" varStatus="status">
        <tr>
      <c:choose>
        <c:when test="${status.first}">
          <td valign="top">
            <p class="attachment">
              <fmt:message key="answer.attachments"/>
            </p>
          </td>
        </c:when>
        <c:otherwise>
          <td>&nbsp;</td>
        </c:otherwise>
      </c:choose>
          <td class="attachment">
            <a href="<c:out value="/ajaqs/servlets/user/download?project=${project.id}&faq=${faq.id}&question=${question.id}&answer=${a.id}&attachment=${b.id}"/>"><c:out value="${b.fileName}"/></a>
          </td>
          <td class="attachment"> - <c:out value="${b.descr}"/></td>
          <td class="attachment"> - <a href="/ajaqs/jsp/user/user.jsp?logon=<c:out value="${b.user.logon}"/>"><c:out value="${b.user.logon}"/></a>
          </td>
          <td class="attachment"> - <fmt:formatDate value="${b.creationDate}" type="both" dateStyle="medium" timeStyle="medium"/></td>
        </tr>
    </c:forEach>
    <c:set var="count">
      <c:out value="${fn:length(a.attachments)}"/>
    </c:set>
    <c:choose>
      <c:when test="${count > 0}">
        <tr class="attachment">
          <td>&nbsp;</td>
          <td colspan="4">
            <a href="<c:out value="/ajaqs/jsp/user/attach.jsp?project=${project.id}&faq=${faq.id}&question=${question.id}&answer=${a.id}"/>">
              [<fmt:message key="attachment.add"/>]
            </a>
          </td>
        </tr>
      </c:when>
      <c:otherwise>
        <tr>
          <td valign="top">
            <p class="attachment">
              <fmt:message key="answer.attachments"/>
              <a href="<c:out value="/ajaqs/jsp/user/attach.jsp?project=${project.id}&faq=${faq.id}&question=${question.id}&answer=${a.id}"/>">
                [<fmt:message key="attachment.add"/>]
              </a>
            </p>
          </td>
        </tr>
      </c:otherwise>
    </c:choose>
      </table></p>
    </li>
  </c:forEach>
  </ol>
  <form method="POST" action="/ajaqs/jsp/user/submitanswer.jsp">
    <input type="hidden" name="project"
      value="<c:out value="${project.id}"/>" />
    <input type="hidden" name="faq"
      value="<c:out value="${faq.id}"/>" />
    <input type="hidden" name="question"
      value="<c:out value="${question.id}"/>" />
    <input type="submit"
      value="<fmt:message key="answer.new"/>" />
  </form>

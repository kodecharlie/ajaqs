<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ajq" uri="/WEB-INF/ajaqs.tld" %>

<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="com.beardediris.ajaqs.i18n.Browse"/>

<c:choose>
  <c:when test="${param.faq == '*'}">
    <c:set var="submitquestionURL">
      <c:out value="/ajaqs/jsp/user/submitquestion.jsp?project=${project.id}"/>
    </c:set>
    <c:set var="faqList" value=""/>
    <c:set var="subjSearched" value="${project.name}"/>
    <c:forEach var="faq" items="${project.faqs}" varStatus="status">
      <c:choose>
        <c:when test="${status.first}">
          <c:set var="faqList" value="${faq.id}"/>
        </c:when>
        <c:otherwise>
          <c:set var="faqList" value="${tmpvar},${faq.id}"/>
        </c:otherwise>
      </c:choose>
      <c:set var="tmpvar" value="${faqList}"/>
    </c:forEach>
  </c:when>
  <c:otherwise>
    <c:set var="submitquestionURL">
      <c:out value="/ajaqs/jsp/user/submitquestion.jsp?project=${project.id}&faq=${param.faq}"/>
    </c:set>
    <c:set var="faqList" value="${param.faq}"/>
    <ajq:setFaq faq="${param.faq}" projectid="project"
      scope="request" var="faq"/>
    <c:set var="subjSearched" value="${project.name} -&gt; ${faq.name}"/>
  </c:otherwise>
</c:choose>

  <p class="banner">
    <fmt:message key="banner.title">
      <fmt:param value="${project.name}"/>
    </fmt:message>
  </p>
  <p class="links">
    <a href="/ajaqs/jsp/user/projectlist.jsp">
      <fmt:message key="link.projects"/>
    </a> |
    <a href="<c:out value="/ajaqs/jsp/user/browse.jsp?project=${project.id}&faq=*"/>">
      <fmt:message key="link.browse"/>
    </a> |
    <a href="<c:out value="${submitquestionURL}"/>">
      <fmt:message key="link.question"/>
    </a> |
    <a href="/ajaqs/servlets/security/logout">
      <fmt:message key="link.logout"/>
    </a>
  </p>
  <form method="POST" action="/ajaqs/jsp/user/search.jsp" class="search"
    name="searchForm">
    <input type="hidden" name="project"
      value="<c:out value="${project.id}"/>" />
    <input type="hidden" name="faq"
      value="<c:out value="${param.faq}"/>" />
    <label for="kw">
      <fmt:message key="search.label"/>
    </label>
    <input type="text" name="keywords" id="kw" />
    <input type="submit"
      value="<fmt:message key="search.submit">
              <fmt:param value="${subjSearched}"/>
            </fmt:message>" />
  </form>
  <script language="javascript">
  <!--
    document.searchForm.keywords.focus()
    // -->
  </script>
  <hr/>
<c:forTokens var="faqId" items="${faqList}" delims=",">
  <ajq:setFaq faq="${faqId}" projectid="project"
    scope="request" var="faq"/>
  <p class="faq">
    <c:out value="${faq.name}"/>
  </p>
  <c:forEach var="q" items="${faq.questions}">
    <p class="question">
      <c:out value="${q.question}"/>
    </p>
    <ol>
    <c:forEach var="a" items="${q.answers}">
      <li>
        <p class="answer">
          <c:out value="${a.answer}"/>
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
              <a href="<c:out value="/ajaqs/servlets/user/download?project=${project.id}&faq=${faq.id}&question=${q.id}&answer=${a.id}&attachment=${b.id}"/>"><c:out value="${b.fileName}"/></a>
            </td>
            <td class="attachment"> - <c:out value="${b.descr}"/></td>
            <td class="attachment"> - <a href="<c:out value="/ajaqs/jsp/user/user.jsp?logon=${b.user.logon}"/>"><c:out value="${b.user.logon}"/></a>
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
              <a href="<c:out value="/ajaqs/jsp/user/attach.jsp?project=${project.id}&faq=${faq.id}&question=${q.id}&answer=${a.id}"/>">
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
                <a href="<c:out value="/ajaqs/jsp/user/attach.jsp?project=${project.id}&faq=${faq.id}&question=${q.id}&answer=${a.id}"/>">
                  [<fmt:message key="attachment.add"/>]
                </a>
              </p>
            </td>
          </tr>
        </c:otherwise>
      </c:choose>
        </table>
      </li>
      </c:forEach>
    </ol>
    <form method="POST" action="/ajaqs/jsp/user/submitanswer.jsp"
      class="newanswer">
      <input type="hidden" name="project"
        value="<c:out value="${project.id}"/>" />
      <input type="hidden" name="faq"
        value="<c:out value="${faq.id}"/>" />
      <input type="hidden" name="question"
        value="<c:out value="${q.id}"/>" />
      <input type="submit"
        value="<fmt:message key="answer.new"/>" />
    </form>
    <hr class="thinhr"/>
  </c:forEach>
</c:forTokens>

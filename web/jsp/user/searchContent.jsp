<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ajq" uri="/WEB-INF/ajaqs.tld" %>

<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="com.beardediris.ajaqs.i18n.Search"/>
<ajq:setUser logon="${pageContext.request.remoteUser}"
  scope="page" var="user"/>

<c:choose>
  <c:when test="${param.project == '*'}">
    <c:set var="projSearched">
      <fmt:message key="all.faqs"/>
    </c:set>
  </c:when>
  <c:otherwise>
    <ajq:setProject project="${param.project}" userid="user"
      scope="page" var="project"/>
    <c:choose>
      <c:when test="${param.faq == '*'}">
        <c:set var="projSearched" value="${project.name}"/>
      </c:when>
      <c:otherwise>
        <ajq:setFaq faq="${param.faq}" projectid="project"
          scope="page" var="faq"/>
        <c:set var="projSearched" value="${project.name} -&gt; ${faq.name}"/>
      </c:otherwise>
    </c:choose>
  </c:otherwise>
</c:choose>

<c:set var="keywords" value="${fn:trim(param.keywords)}" scope="page"/>
<c:choose>
  <c:when test="${empty keywords}">
    <c:set var="numResults" value="0"/>
    <c:set var="results" value=""/>
  </c:when>
  <c:otherwise>
    <ajq:searchProject var="results" keywords="${keywords}"
      userid="user" project="${param.project}" faq="${param.faq}"/>
    <c:set var="numResults">
      <c:out value="${fn:length(results)}"/>
    </c:set>
  </c:otherwise>
</c:choose>

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

  <div class="searchbanner">
    <p class="searchtitle">
      <fmt:message key="banner.title"/>
    </p>
  <c:if test="${!empty keywords}">
    <table class="searchsubtitle">
      <tr>
        <td class="lefttitle">
          &nbsp;
          <c:set var="emphasized">
            <ajq:emphasize cssClass="searchword"
              keywords="${keywords}" text="${keywords}"/>
          </c:set>
          <fmt:message key="banner.subtitle">
            <fmt:param value="${projSearched}"/>
            <fmt:param value="${emphasized}"/>
          </fmt:message>
        </td>
        <td class="righttitle">
          <c:choose>
            <c:when test="${numResults >= 1}">
              <c:set var="upThrough" value="${end + 1}"/>
              <c:if test="${upThrough > numResults}">
                <c:set var="upThrough" value="${numResults}"/>
              </c:if>
              <fmt:message key="result.summary">
                <fmt:param value="<b>${begin + 1}</b>"/>
                <fmt:param value="<b>${upThrough}</b>"/>
                <fmt:param value="<b>${numResults}</b>"/>
              </fmt:message>
            </c:when>
            <c:otherwise>
              <fmt:message key="result.none"/>
            </c:otherwise>
          </c:choose>
          &nbsp;
        </td>
      </tr>
    </table>
  </c:if>
  </div>
  <p class="links">
    <a href="/ajaqs/jsp/user/projectlist.jsp">
      <fmt:message key="link.projects"/>
    </a> |
  <c:if test="${param.project != '*'}">
    <a href="<c:out value="/ajaqs/jsp/user/browse.jsp?project=${param.project}&faq=${param.faq}"/>">
      <fmt:message key="link.browse"/>
    </a> |
  </c:if>
    <a href="/ajaqs/servlets/security/logout">
      <fmt:message key="link.logout"/>
    </a>
  </p>
  <form method="POST" action="/ajaqs/jsp/user/search.jsp" class="search"
    name="searchForm">
    <input type="hidden" name="project"
      value="<c:out value="${param.project}"/>" />
    <input type="hidden" name="faq"
      value="<c:out value="${param.faq}"/>" />
    <label for="kw">
      <fmt:message key="search.label"/>
    </label>
    <input type="text" name="keywords" id="kw" 
      value="<c:out value="${keywords}"/>" />
    <input type="submit" value="<fmt:message key="search.submit">
                                  <fmt:param value="${projSearched}"/>
                                </fmt:message>" />
  </form>
  <script language="javascript">
  <!--
    document.searchForm.keywords.focus()
    // -->
  </script>
  <hr />
<c:forEach var="r" items="${results}" begin="${begin}" end="${end}">
  <p class="searchresult">
    <a class="question" href="<c:out value="/ajaqs/jsp/user/answer.jsp?project=${r.question.faq.project.id}&faq=${r.question.faq.id}&question=${r.question.id}&keywords=${keywords}"/>">
      <ajq:emphasize cssClass="searchword"
        keywords="${keywords}" text="${r.question.question}"/>
    </a><br />
    <c:if test="${!empty r.match}">
      <span class="searchmatch">
        <ajq:emphasize cssClass="searchword"
          keywords="${keywords}" text="${r.match}"/>
      </span>
    </c:if>
  </p>
  <p class="infoblock">
    <span class="searchpath">
      <c:out value="${r.question.faq.project.name}"/> -&gt; <c:out value="${r.question.faq.name}"/>
    </span>
    <c:set var="numAnswers">
      <c:out value="${fn:length(r.question.answers)}"/>
    </c:set>
    <c:if test="${numAnswers > 0}">
      -
      <span class="searchinfo">
        <c:set var="anchor">
          <a href="/ajaqs/jsp/user/user.jsp?logon=<c:out value="${r.question.lastAnswer.user.logon}"/>"><c:out value="${r.question.lastAnswer.user.logon}"/></a>
        </c:set>
        <c:set var="lastUpdate">
          <fmt:formatDate value="${r.question.lastAnswer.creationDate}"
            type="both" dateStyle="medium" timeStyle="medium"/>
        </c:set>
        <fmt:message key="result.lastUpdate">
          <fmt:param value="${lastUpdate}"/>
          <fmt:param value="${anchor}"/>
        </fmt:message>
      </span>
    </p>
  </c:if>
</c:forEach>
  <c:set var="numScrollable" value="${numResults}" scope="request"/>
  <c:set var="prevPage" scope="request">
    <c:out value="/ajaqs/jsp/user/search.jsp?project=${param.project}&faq=${param.faq}&keywords=${keywords}&page=${param.page - 1}"/>
  </c:set>
  <c:set var="nextPage" scope="request">
    <c:out value="/ajaqs/jsp/user/search.jsp?project=${param.project}&faq=${param.faq}&keywords=${keywords}&page=${param.page + 1}"/>
  </c:set>
  <jsp:include page="/jsp/site/traversal.jsp"/>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ajq" uri="/WEB-INF/ajaqs.tld" %>

<ajq:setUser logon="${param.logon}"
  scope="request" var="user"/>
<ajq:setProject project="${param.project}" userid="user"
  scope="request" var="project"/>
<ajq:setFaq faq="${param.faq}" projectid="project"
  scope="request" var="faq"/>

<jsp:directive.page contentType="text/xml"/>
<?xml version="1.0" encoding="UTF-8"?>
<rss version="2.0">
  <channel>
    <title>
      <c:out value="${project.name}"/>-&gt;<c:out value="${faq.name}"/>
    </title>
    <link>http://<ajq:getConfig param="com.beardediris.ajaqs.session.serverName"/><c:out value="/ajaqs/jsp/user/browse.jsp?project=${project.id}&faq=${faq.id}"/></link>
    <description>
      <c:out value="${project.name}"/>-&gt;<c:out value="${faq.name}"/>
    </description>
    <language>en</language>
  <c:forEach var="q" items="${faq.questions}">
    <item>
      <title>
        <c:out value="${q.question}"/>
      </title>
      <link>http://<ajq:getConfig param="com.beardediris.ajaqs.session.serverName"/><c:out value="/ajaqs/jsp/user/answer.jsp?project=${project.id}&faq=${faq.id}&question=${q.id}"/></link>
      <guid>http://<ajq:getConfig param="com.beardediris.ajaqs.session.serverName"/><c:out value="/ajaqs/jsp/user/answer.jsp?project=${project.id}&faq=${faq.id}&question=${q.id}"/></guid>
    </item>
  </c:forEach>
  </channel>
</rss>

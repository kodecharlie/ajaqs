<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ajq" uri="/WEB-INF/ajaqs.tld" %>

<ajq:setUser logon="${param.logon}"
  scope="request" var="user"/>
<ajq:setProject project="${param.project}" userid="user"
  scope="request" var="project"/>

<jsp:directive.page contentType="text/xml"/>
<?xml version="1.0" encoding="UTF-8"?>
<rss version="2.0">
  <channel>
    <title>
      <c:out value="${project.name}"/>
    </title>
    <link>http://<ajq:getConfig param="com.beardediris.ajaqs.session.serverName"/><c:out value="/ajaqs/jsp/user/projectpage.jsp?project=${project.id}"/></link>
    <description>
      <c:out value="${project.name}"/>
    </description>
    <language>en</language>
  <c:forEach var="faq" items="${project.faqs}">
    <item>
      <title>
        <c:out value="${faq.name}"/>
      </title>
      <link>http://<ajq:getConfig param="com.beardediris.ajaqs.session.serverName"/><c:out value="/ajaqs/jsp/user/browse.jsp?project=${project.id}&faq=${faq.id}"/></link>
      <guid>http://<ajq:getConfig param="com.beardediris.ajaqs.session.serverName"/><c:out value="/ajaqs/jsp/user/browse.jsp?project=${project.id}&faq=${faq.id}"/></guid>
    </item>
  </c:forEach>
  </channel>
</rss>

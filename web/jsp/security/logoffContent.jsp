<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>

<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="com.beardediris.ajaqs.i18n.Logoff"/>

  <p class="banner">
    <fmt:message key="banner.title"/>
  </p>
  <p class="relogin">
    <a href="/ajaqs/jsp/user/projectlist.jsp">
      <fmt:message key="login.msg"/>
    </a>
  </p>

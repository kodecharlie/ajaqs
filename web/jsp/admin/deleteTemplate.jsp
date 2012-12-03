<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tmplt" uri="/WEB-INF/templates.tld" %>

  <p class="banner">
    <tmplt:get name="banner"/>
  </p>
  <p class="links">
    <tmplt:get name="links"/>
  </p>
<c:if test="${!empty param.error}">
  <p class="error">
    <tmplt:get name="error"/>
  </p>
</c:if>
  <p class="deletenote">
    <tmplt:get name="request"/>
  </p>
  <p class="deletable">
    <tmplt:get name="synopsis"/>
  </p>
  <p class="deletenote">
    <tmplt:get name="confirm"/>
  </p>
  <form method="POST" action="<tmplt:get name="action"/>">
    <input type="submit" value="<tmplt:get name="submit"/>" />
  </form>
  <hr />

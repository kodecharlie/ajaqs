<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ajq" uri="/WEB-INF/ajaqs.tld" %>
<%@ taglib prefix="tmplt" uri="/WEB-INF/templates.tld" %>

<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="com.beardediris.ajaqs.i18n.PageTemplate"/>

<html xmlns="http://www.w3.org/1999/xhtml"
  xml:lang="<ajq:getConfig param="locale"/>"
  lang="<ajq:getConfig param="locale"/>">
<head>
<!-- <fmt:message key="mimetype.comment"/> -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>
  <tmplt:get name="title"/>
</title>
<link type="text/css" rel="stylesheet" href="/ajaqs/css/site/common.css" />
<link type="text/css" rel="stylesheet" href="<tmplt:get name="styles"/>" />
</head>

<body>

<table width="100%" cellspacing="0" cellpadding="0">
  <tr valign="top">
    <td>
      <tmplt:get name="sidebar"/>
    </td>
    <td>
      <table width="100%">
        <tr><td><tmplt:get name="header"/></td></tr>
        <tr><td><tmplt:get name="content"/></td></tr>
        <tr><td><tmplt:get name="footer"/></td></tr>
      </table>
    </td>
  </tr>
</table>

</body>
</html>

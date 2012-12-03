<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="com.beardediris.ajaqs.i18n.Attach"/>

  <p class="banner">
    <fmt:message key="banner.title"/>
  </p>
  <p class="links">
    <a href="/ajaqs/jsp/user/projectlist.jsp">
      <fmt:message key="link.projects"/>
    </a> |
    <a href="<c:out value="/ajaqs/jsp/user/browse.jsp?project=${param.project}&faq=${param.faq}"/>">
      <fmt:message key="link.browse"/>
    </a> |
    <a href="/ajaqs/servlets/security/logout">
      <fmt:message key="link.logout"/>
    </a>
  </p>
<c:if test="${!empty param.error}">
  <p class="error">
    <fmt:message key="error.emptyfilename"/>
  </p>
</c:if>
<center>
<form method="POST" enctype="multipart/form-data"
  action="/ajaqs/servlets/user/upload" class="uploadinfo"
  name="attachForm">
  <input type="hidden" name="project"
    value="<c:out value="${param.project}"/>" />
  <input type="hidden" name="faq"
    value="<c:out value="${param.faq}"/>" />
  <input type="hidden" name="question"
    value="<c:out value="${param.question}"/>" />
  <input type="hidden" name="answer"
    value="<c:out value="${param.answer}"/>" />
  <table summary="<fmt:message key="form.summary"/>"
    border="0" cellpadding="6" cellspacing="3" align="center">
    <tr>
      <th align="right"><label for="fn"><fmt:message key="form.filename"/>:</label></th>
      <td>
        <input type="file" name="filename" id="fn" />
      </td>
    </tr>
    <tr>
      <th align="right"><label for="ft"><fmt:message key="form.filetype"/>:</label></th>
      <td>
        <select name="filetype" id="ft">
          <option value="application/octet-stream">
            <fmt:message key="filetype.default"/></option>
          <option value="text/css">
            Cascading Style Sheet (*.css)</option>
          <option value="text/html">
            HTML (*.html)</option>
          <option value="text/sgml">
            SGML (*.sgml)</option>
          <option value="text/plain">
            Plain Text (*.txt)</option>
          <option value="text/rtf">
            Rich Text Format (*.rtf)</option>
          <option value="text/xml">
            XML (*.xml)</option>
          <option value="application/applefile">
            MacIntosh File</option>
          <option value="application/mac-binhex40">
            BinHex4.0 (*.hqx)</option>
          <option value="application/macwriteii">
            MacWrite II</option>
          <option value="application/mathematica">
            Mathematica Notebook (*.nb)</option>
          <option value="application/msword">
            Microsoft Word (*.doc)</option>
          <option value="application/pdf">
            Portable Document Format (*.pdf)</option>
          <option value="application/postscript">
            PostScript (*.ps)</option>
          <option value="application/zip">
            Zip (*.zip)</option>
          <option value="image/cgm">
            Computer Graphics Metafile (*.cgm)</option>
          <option value="image/gif">
            GIF (*.gif)</option>
          <option value="image/ief">
            Image Exchange Format (*.ief)</option>
          <option value="image/jpeg">
            JPEG (*.jpg)</option>
          <option value="image/png">
            Portable Network Graphics (*.png)</option>
          <option value="audio/basic">
            Basic Audio</option>
          <option value="audio/mpeg">
            MP3 Audio</option>
          <option value="video/mpeg">
            MPEG Video</option>
          <option value="video/quicktime">
            QuickTime Video</option>
        </select>
      </td>
    </tr>
    <tr>
      <th align="right"><label for="dsc"><fmt:message key="form.descr"/>:</label></th>
      <td align="left">
        <input type="text" name="descr" id="dsc" maxlength="128" />
      </td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <th align="left">
        <input type="submit" value=<fmt:message key="form.submit"/> />
      </th>
    </tr>
  </table>
</form>
<script language="javascript">
<!--
  document.attachForm.filename.focus()
  // -->
</script>
</center>

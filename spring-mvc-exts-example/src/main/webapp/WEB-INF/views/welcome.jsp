<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
	<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
	<title>Spring jXLS examples</title>
	<link rel="stylesheet" href="<c:url value="/resources/blueprint/screen.css" />" type="text/css" media="screen, projection">
	<link rel="stylesheet" href="<c:url value="/resources/blueprint/print.css" />" type="text/css" media="print">
	<!--[if lt IE 8]>
		<link rel="stylesheet" href="<c:url value="/resources/blueprint/ie.css" />" type="text/css" media="screen, projection">
	<![endif]-->
</head>
<body>
<div class="container">  
	<h1>Spring jXSL sample</h1>
	<a href="jxls/department" >Download department.xsl</a><br/>
	<a href="jxls/department_filename_in_model" >Download department.xsl(Filename is specified as model key.)</a>

	<%-- File upload test --%>
	<p>File upload test.</p>
	<form method="post" action="jxls/upload" enctype="multipart/form-data">
     	<input type="file" name="file"/>
        <input type="submit"/>
    </form>
</div>
</body>
</html>
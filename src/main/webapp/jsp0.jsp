<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.*, java.text.*" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Phone book</title>
</head>
<body>
	Hello, it is <%=getFormattedDate()%>
	<form action="${pageContext.request.contextPath}/MainServlet" method="POST">
	    Part of name: <input type="text" name="name_part" />
	    <br/>
	    Part of phone: <input type="text" name="phone_part" />
	    <br/>
	    <input type="submit" value="Submit" />
    </form>
</body>
</html>

<%!
	String getFormattedDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
		return sdf.format(new Date());
	}
%>
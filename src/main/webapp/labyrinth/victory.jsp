<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import = "java.io.*,java.util.*,java.sql.*"%>
<%@ page import = "javax.servlet.http.*,javax.servlet.*" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/sql" prefix = "sql"%>
<jsp:include page="../include.jsp"/>


<!DOCTYPE html>
<html>
<head>
    <title>Labyrinth: victory</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Add some nice styling and functionality by using Twitter Bootstrap -->
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap-theme.min.css">
    <style>
        body{padding:0 20px;}
    </style>
</head>

<body>
    <%
        request.setAttribute("username", org.apache.shiro.SecurityUtils.getSubject().getPrincipal());
    %>
    
    <sql:setDataSource var = "snapshot" driver = "com.mysql.jdbc.Driver"
       url = "jdbc:mysql://radagast.asuscomm.com:3306/web_game"
       user = "web_game"  password = "webgamepassword"/>
    <sql:query dataSource = "${snapshot}" var = "result">
       SELECT * from Players where username = "${username}";
    </sql:query>
	<h1>Congratulations!</h1>
    <h2>So, ${username}, you are out of Labyrinth.</h2>
	<c:forEach var = "row" items = "${result.rows}">	
	<p>Now you have ${row.victories} victories and ${row.looses} looses.</p>
	<p>You were ${row.slayed_by_minotaurs} times slayed by minotaurs
		and ${row.slayed_by_players} times by other players,<br>
		and you've killed them
		${row.killed_minotaurs} and ${row.killed_players} times, respectively.<br>
		You've ${row.suicides} times committed suicide.</p>
	</c:forEach>
	
    <p>What are you going to do now?</p>

    <shiro:authenticated><p>You can visit your <a href="<c:url value="/account"/>">account page</a>.</p>
	<p>Or, if you dare, you can <a href=/Lobby>walk into another Labyrinth.</a></p></shiro:authenticated>
	 <shiro:notAuthenticated><p>If you want to access the authenticated-only
	 <a href="<c:url value="/account"/>">account page</a>,
        you will need to log-in first.</p>
    </shiro:notAuthenticated>

    <p>Anyway, you are free to go <a href=/>home</a>.</p>

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://code.jquery.com/jquery.js"></script>
    <script src="//netdna.bootstrapcdn.com/bootstrap/3.0.2/js/bootstrap.min.js"></script>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
</body>
</html>

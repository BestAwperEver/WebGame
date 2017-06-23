<jsp:include page="../include.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/sql" prefix = "sql"%>
<!DOCTYPE html>
<html>
<head>
    <title>Labyrinth : account</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Add some nice styling and functionality.  We'll just use Twitter Bootstrap -->
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap-theme.min.css">
    <style>
        body{padding: 0 20px;}
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
	
    <h2>Personal info</h2>

    <p>Hello there. It's your personal page. Here you can watch your current status:</p>
    
    <c:forEach var = "row" items = "${result.rows}">	
		<p>	
			You have ${row.victories} victories and ${row.looses} looses.<br>
			You were ${row.slayed_by_minotaurs} times slayed by minotaurs
			and ${row.slayed_by_players} times by other players,<br>
			and you've killed them
			${row.killed_minotaurs} and ${row.killed_players} times, respectively.<br>
			You've ${row.suicides} times committed suicide.<br>
		</p>
	</c:forEach>
    
    <p>Visit <a href="<c:url value="/top"/>">Top-10 page.</a></p>
    
    <p>Return to the <a href="<c:url value="/home.jsp"/>">home page.</a></p>

    <p><a href="<c:url value="/logout"/>">Log out.</a></p>

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
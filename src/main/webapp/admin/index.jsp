<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/sql" prefix = "sql"%>
<!DOCTYPE html>
<html>
<head>
    <title>Labyrinth : administrator page</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap-theme.min.css">
<!--     <link rel="stylesheet" href="https://silviomoreto.github.io/bootstrap-select/dist/css/bootstrap-select.min.css"> -->
    <style>
        body{padding: 0 20px;}
    </style>
</head>
<body>

	<sql:setDataSource var = "snapshot" driver = "com.mysql.jdbc.Driver"
       url = "jdbc:mysql://radagast.asuscomm.com:3306/web_game"
       user = "web_game"  password = "webgamepassword"/>
    <sql:query dataSource = "${snapshot}" var = "result">
    	SELECT username from Players;
	</sql:query>
	
    <sql:query dataSource = "${snapshot}" var = "result2">
    	select map_name from Maps;
	</sql:query>

    <h2>For administrators only!</h2>

    <p>This page simulates a restricted part of a web application intended for administrators only.</p>
    
	<p>You can visit <a href="<c:url value="/search/jsp0.jsp"/>">search page</a>.</p>
	
	<p>You can add a specific amount (from 1 to 20) of bullets to any player.</p>
	
	<form action="/admin/AdminFeatures" method="POST">
		<p>Send to
		<select class="selectpicker" name="player">
			<c:forEach var = "row" items = "${result.rows}">
				<option value = "${row.username}">${row.username}</option>
			</c:forEach>
		</select>
	    <input type="text" name="bullets" style="height: 20px; width: 20px;"/>
	    bullets: 
	    <input type="submit" value="Send"/>
	    </p>
    </form>
    
    <p>You can resurrect minotaurs on any map.</p>
    
    	<form action="/admin/AdminFeatures" method="POST">
		<p>Resurrect minotaurs on
		<select class="selectpicker" name="map_name">
			<option value = "all_maps">all maps</option>
			<c:forEach var = "row2" items = "${result2.rows}">
				<option value = "${row2.map_name}">${row2.map_name}</option>
			</c:forEach>
		</select>
	    <input type="submit" value="Resurrect"/>
	    </p>
    </form>
    
    <c:if test="${error != null}">
    	<p>You have an error: ${error}</p>
    </c:if>
    <c:if test="${success != null}">
    	<p>${success}</p>
    </c:if>
	
	<br>
    <p>You are currently logged in.</p>

    <p><a href="<c:url value="/home.jsp"/>">Return to the home page.</a></p>

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
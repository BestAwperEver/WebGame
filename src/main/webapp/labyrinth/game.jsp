<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import = "java.io.*,java.util.*,java.sql.*"%>
<%@ page import = "javax.servlet.http.*,javax.servlet.*" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/sql" prefix = "sql"%>
<jsp:include page="../include.jsp"/>


<!DOCTYPE html>
<html>
<head>
    <title>Labyrinth : game</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Add some nice styling and functionality by using Twitter Bootstrap -->
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="/w3css/4/w3.css">
    <style>
        body{padding:0 20px;}
		.button {
			padding: 0px 0px;
			width: 100px;
			height: 50px;
		    background-color: #4CAF50;
		    color: white;
			border: none;
		    border-radius: 12px;
		    text-align: center;
		    text-decoration: none;
		    display: inline-block;
		    font-size: 14px;
		    margin: 4px 2px;
		    cursor: pointer;
		}
		.button1 {
			border: 2px solid #f44336;
			color: #f44336;
			background-color: black;
		}
	</style>
</head>

<body>
<!-- 	<script src="http://code.jquery.com/jquery-latest.js"></script> -->
<!-- 	<button onclick="jQuery('#aaa').load(' #aaa');">Reload</button> -->
<%-- 	<div id="aaa"><%=new java.util.Date().toString()%></div> --%>
	<c:if test="${init != true}">
		<c:redirect url = "/Lobby"/>
	</c:if>	
    <%
        request.setAttribute("username", org.apache.shiro.SecurityUtils.getSubject().getPrincipal());
    %>
    <h2>You're in the Labyrinth</h2>
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.0/jquery.min.js"></script>
    <script>
	    function loadlink(){
	        $('#load_me').load(' #load_me',function () {
	             $(this).unwrap();
	        });
	        $('#datetime').load(' #datetime',function () {
	             $(this).unwrap();
	        });
	    }
	
	    loadlink(); // This will run on page load
	    setInterval(function(){
	        loadlink() // this will run after every 5 seconds
	    }, 5000);
    </script>
	<sql:setDataSource var = "snapshot" driver = "com.mysql.jdbc.Driver"
       url = "jdbc:mysql://radagast.asuscomm.com:3306/web_game"
       user = "web_game"  password = "webgamepassword"/>
	
	<div id="load_me">
    <sql:query dataSource = "${snapshot}" var = "result">
       SELECT <shiro:hasRole name="admin">map_name, coords, </shiro:hasRole>bullets, info from Players where username = "${username}";
    </sql:query>
	<c:forEach var = "row" items = "${result.rows}">
		<p><c:out value = "${row.info}"/></p>
<%-- 	</c:forEach> --%>
<%-- 	<c:forEach var = "row" items = "${result.rows}"> --%>
		<p>You have ${row.bullets} bullets<shiro:hasRole name="admin"> on map "${row.map_name}"</shiro:hasRole>.</p>
		<shiro:hasRole name="admin"><p>You are on ${row.coords} cell.</p></shiro:hasRole>
	</c:forEach>
	</div>
	
<%-- 	<c:if test="${in_hospital == true}"> --%>
<!-- 	<p>You are in hospital.</p>	 -->
<%-- 	</c:if>	 --%>
<%-- 	<p>You have ${number_of_bullets} bullets.</p> --%>

	<form action="${pageContext.request.contextPath}/Game" method="post">
	    <button class="button" type="submit" name="action" value="turn_right">Step Right</button>
	    <button class="button" type="submit" name="action" value="turn_up">Step Up</button>
	    <br>
	    <button class="button" type="submit" name="action" value="turn_left">Step Left</button>
	    <button class="button" type="submit" name="action" value="turn_down">Step Down</button>
	    <br><br>
	    <c:forEach var = "row" items = "${result.rows}">
		    <c:if test="${row.bullets > 0}">
		    <button class="button" type="submit" name="action" value="turn_right">Shoot Right</button>
		    <button class="button" type="submit" name="action" value="turn_up">Shoot Up</button><br>
		    <button class="button" type="submit" name="action" value="turn_left">Shoot Left</button>
		    <button class="button" type="submit" name="action" value="turn_down">Shoot Down</button>
		    </c:if>
	    </c:forEach>
	</form>
	<br>
	<form action="${pageContext.request.contextPath}/Game" method="post">
	    <button class="button button1" type="submit" name="action" value="giveup">Give up</button>
	</form>
	<br>
	<div id="datetime">
		<p>It's <%=new java.util.Date().toString()%></p>
	</div>
<%--     <p>Hi, <shiro:guest>Guest</shiro:guest><shiro:user> --%>
<%--         <c:out value="${username}"/></shiro:user>! --%>
<%--         ( <shiro:user><a href="<c:url value="/logout"/>">Log out</a></shiro:user> --%>
<%--         <shiro:guest><a href="<c:url value="/login.jsp"/>">Log in</a></shiro:guest> ) --%>
<!--     </p> -->

<!--     <p>Welcome to Labyrinth.</p> -->
<%-- 	<p>Visit <a href="<c:url value="/jsp0.jsp"/>">search page</a>.</p> --%>
<%--     <shiro:authenticated><p>Visit your <a href="<c:url value="/account"/>">account page</a>.</p></shiro:authenticated> --%>
<%--     <shiro:notAuthenticated><p>If you want to access the authenticated-only <a href="<c:url value="/account"/>">account page</a>, --%>
<%--         you will need to log-in first.</p></shiro:notAuthenticated> --%>

<!--     <h2>Roles</h2> -->

<!--     <p>Here are the roles you have and don't have. Log out and log back in under different user -->
<!--         accounts to see different roles.</p> -->

<!--     <h3>Roles you have:</h3> -->

<!--     <p> -->
<%--         <shiro:hasRole name="Captains">Captain<br/></shiro:hasRole> --%>
<%--         <shiro:hasRole name="admin">Admin<br/></shiro:hasRole> --%>
<%--         <shiro:hasRole name="Enlisted">Enlisted<br/></shiro:hasRole> --%>
<!--     </p> -->

<!--     <h3>Roles you DON'T have:</h3> -->

<!--     <p> -->
<%--         <shiro:lacksRole name="Captains">Captain<br/></shiro:lacksRole> --%>
<%--         <shiro:lacksRole name="admin">Admin<br/></shiro:lacksRole> --%>
<%--         <shiro:lacksRole name="Enlisted">Enlisted<br/></shiro:lacksRole> --%>
<!--     </p> -->

<!--     <h2>Permissions</h2> -->

<!--     <ul> -->
<%--         <li>You may <shiro:lacksPermission name="ship:command:NCC-1701-D"><b>NOT</b> </shiro:lacksPermission> command the <code>NCC-1701-D</code> Starship!</li> --%>
<%--         <li>You may <shiro:lacksPermission name="user:edit:${username}"><b>NOT</b> </shiro:lacksPermission> edit the ${username} user!</li> --%>
<!--     </ul> -->

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
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="include.jsp"/>
<!DOCTYPE html>
<html>
<head>
    <title>Labyrinth</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Add some nice styling and functionality by using Twitter Bootstrap -->
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap-theme.min.css">
    <style>
        body{padding:0 20px;}
    </style>
</head>
<body>

    <h1>Labyrinth</h1>

    <p>Hi, <shiro:guest>Guest</shiro:guest><shiro:user>
        <%
            request.setAttribute("username", org.apache.shiro.SecurityUtils.getSubject().getPrincipal()); // .oneByType(java.util.Map.class));
        %>
        <c:out value="${username}"/></shiro:user>!
        ( <shiro:user><a href="<c:url value="/logout"/>">Log out</a></shiro:user>
        <shiro:guest><a href="<c:url value="/login.jsp"/>">Log in</a></shiro:guest> )
    </p>

    <p>Welcome to Labyrinth.</p>
	<p>Visit <a href="<c:url value="/jsp0.jsp"/>">search page</a>.</p>
    <shiro:authenticated><p>Visit your <a href="<c:url value="/account"/>">account page</a>.</p></shiro:authenticated>
    <shiro:notAuthenticated><p>If you want to access the authenticated-only <a href="<c:url value="/account"/>">account page</a>,
        you will need to log-in first.</p></shiro:notAuthenticated>

    <h2>Roles</h2>

    <p>Here are the roles you have and don't have. Log out and log back in under different user
        accounts to see different roles.</p>

	<p>
		<shiro:hasRole name="player"><a href=/Game>Let's rock!</a></shiro:hasRole>
	</p>

    <h3>Roles you have:</h3>

    <p>
        <shiro:hasRole name="Captains">Captain<br/></shiro:hasRole>
        <shiro:hasRole name="admin">Admin<br/></shiro:hasRole>
        <shiro:hasRole name="Enlisted">Enlisted<br/></shiro:hasRole>
    </p>

    <h3>Roles you DON'T have:</h3>

    <p>
        <shiro:lacksRole name="Captains">Captain<br/></shiro:lacksRole>
        <shiro:lacksRole name="admin">Admin<br/></shiro:lacksRole>
        <shiro:lacksRole name="Enlisted">Enlisted<br/></shiro:lacksRole>
    </p>

    <h2>Permissions</h2>

    <ul>
        <li>You may <shiro:lacksPermission name="ship:command:NCC-1701-D"><b>NOT</b> </shiro:lacksPermission> command the <code>NCC-1701-D</code> Starship!</li>
        <li>You may <shiro:lacksPermission name="user:edit:${username}"><b>NOT</b> </shiro:lacksPermission> edit the ${username} user!</li>
    </ul>

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

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

	<p>Say, you are in the Labyrinth. What do you do?</p>
	<p>That's pretty simple.
	You can walk, shoot from your revolver and use your knife for slashing enemies down.</p>
	<p>Every Labyrinth has an exit. May be several ones. When you reach any of it &mdash;
	congrats, you are the winner.</p>
	<p>But you are not alone in the dark. There are minotaurs, which are waiting for you in the corners
	(and may be in the middle... who knows) and, more importantly, other players.</p>
	<p>They can stab you with a knife, or even shot from their precious revolvers.
	Just like you can.</p>
	<br>
	<p>So, <shiro:guest>Guest</shiro:guest><shiro:user>
        <%
            request.setAttribute("username", org.apache.shiro.SecurityUtils.getSubject().getPrincipal()); // .oneByType(java.util.Map.class));
        %>
        <c:out value="${username}"/></shiro:user>, what are you going to do now?
    </p>
	<br>
	<p>Return to the <a href="<c:url value="/home.jsp"/>">home page.</a></p>
	<shiro:user><p><a href="<c:url value="/logout"/>">Log out.</a></p></shiro:user>
	<shiro:guest><a href="<c:url value="/testlogin.jsp"/>">Log in.</a></shiro:guest>
	<p><shiro:user><a href=/Lobby>Enter the Labyrinth</a>, if you dare.</shiro:user></p>

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

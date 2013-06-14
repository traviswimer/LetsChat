<!DOCTYPE html>
	<head>
		<title>Sign-up to Chat</title>
		<link rel="stylesheet" type="text/css" href="style.css">
	</head>
	<body>
		<form id="login-form" method="post" action="register.jsp">
			<h1>Sign-up to Chat!</h1>
			<%
				String error = request.getParameter("error");
				if( error != null ){
			%>
					<h3>Username already taken</h3>
			<%
				}
			%>
			<div>
				<input type="text" placeholder="Username" name="username" class="text-field input-top">
			</div>
			<div>
				<input type="password" placeholder="Password" name="password" class="text-field input-bottom">
			</div>
			<div>
				<input type="submit" value="Register" class="login-btn">
			</div>
		</form>
	</body>
</html>

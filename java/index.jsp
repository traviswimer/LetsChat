<!DOCTYPE html>
	<head>
		<title>Let's Chat</title>
		<link rel="stylesheet" type="text/css" href="style.css">
	</head>
	<body>
		<div>
			<%
				//session.putValue("userid", "asdf");

				// Open the chat page if logged in, else go to login page
				if( session.getValue("userid") != null ){
					%>
						<jsp:include page='<%= "chatroom.jsp" %>' />
					<%
				}else{
					%>
						<jsp:include page='<%= "loginform.jsp" %>' />
					<%
				}
			%>
		</div>
	</body>
</html>
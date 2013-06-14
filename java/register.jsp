<%@ page import="java.sql.*" %>
<%@ page import="chat.DB" %>

<%
	String username = request.getParameter("username");
	String password = request.getParameter("password");
	
	try{
		DB database = new DB();
		if(database.createUser(username, password)){
			// Go to chatroom if user created successfully
			response.sendRedirect("chatroom.jsp");
		}else{
			// Return to form is name is already taken
			response.sendRedirect("registerform.jsp?error");
		}
	}catch(Exception e){
		// Return to form if error inserting name in database
		response.sendRedirect("registerform.jsp");
	}
%>
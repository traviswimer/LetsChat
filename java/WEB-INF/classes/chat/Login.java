package chat;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import chat.DB;
import org.json.simple.*;
import java.sql.*;
 
public class Login extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

		// Opens the current users session
		HttpSession session = request.getSession(true);

		DB database = null;

		try{
			String username = request.getParameter("username");
			String password = request.getParameter("password");

			database = new DB();
			int result = database.authenticateUser(username, password);

			if(result != -1){
				session.putValue("userid", result);
				response.sendRedirect("chatroom.jsp");
			}else{
				response.sendRedirect("index.jsp");
			}

		}catch(Exception e){
			PrintWriter output = response.getWriter();
			output.println("ERROR: " + e);
			//response.sendRedirect("index.jsp");
		}finally{
			try{
				database.close();
			}catch(Exception e){}
		}
	}
}
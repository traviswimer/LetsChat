package chat;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import chat.DB;
import org.json.simple.*;
import java.sql.*;
 
public class Logout extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		HttpSession session = request.getSession(true);
		session.invalidate();
		response.sendRedirect("index.jsp");
	}
}
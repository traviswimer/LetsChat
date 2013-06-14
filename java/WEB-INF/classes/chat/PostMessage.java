package chat;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import chat.DB;
import org.json.simple.*;
import java.sql.*;
 
public class PostMessage extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		

		// Opens the current users session
		HttpSession session = request.getSession(true);

		DB database = null;
		PrintWriter output = null;

		try{
			String newMessage = request.getParameter("msg");
			Integer userid = (Integer)session.getValue("userid");

			if(userid != null){
				database = new DB();

				Boolean result = database.postMessage(userid, newMessage);

				JSONObject obj = new JSONObject();
				obj.put("success", result);

				StringWriter out = new StringWriter();
				JSONValue.writeJSONString(obj, out);
				String jsonText = out.toString();

				response.setContentType("application/json;charset=UTF-8");
				output = response.getWriter();
				output.println(jsonText);
			}

		}catch(Exception e){
			output = response.getWriter();
			output.println("ERROR: " + e);
		}finally{
			try{
				database.close();
				output.close();
			}catch(Exception e){}
		}
	}
}
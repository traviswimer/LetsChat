package chat;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import chat.DB;
import org.json.simple.*;
import java.sql.*;
 
public class GetMessages extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		

			

		// Opens the current users session
		//HttpSession session = request.getSession(true);


		DB database = null;
		PrintWriter output = null;


		try{
			int lastMsgId = Integer.parseInt( request.getParameter("lastMsgId") );

			database = new DB();

			ResultSet results = database.getMessages(lastMsgId);

			// array to hold all messages
			JSONArray messageArray = new JSONArray();

			while(results.next()){
				JSONObject msgObj = new JSONObject();
				msgObj.put("msgId", results.getInt("messageid"));
				msgObj.put("userid", results.getInt("userid"));
				msgObj.put("user", results.getString("username"));
				msgObj.put("text", results.getString("message"));
				messageArray.add(msgObj);
			}
			JSONObject obj = new JSONObject();
			obj.put("messages", messageArray);

			StringWriter out = new StringWriter();
			JSONValue.writeJSONString(obj, out);
			String jsonText = out.toString();


			response.setContentType("application/json;charset=UTF-8");
			output = response.getWriter();
			output.println(jsonText);

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
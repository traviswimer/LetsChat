package chat;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import chat.DB;
import org.json.simple.*;
import java.sql.*;
 
public class GetUsers extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

		DB database = null;
		PrintWriter output = null;

		try{
			database = new DB();

			ResultSet results = database.getUsers();

			// array to hold all users
			JSONArray userArray = new JSONArray();

			while(results.next()){
				JSONObject msgObj = new JSONObject();
				msgObj.put("id", results.getInt("userid"));
				userArray.add(msgObj);
			}
			JSONObject obj = new JSONObject();
			obj.put("users", userArray);

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
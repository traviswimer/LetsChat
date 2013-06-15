package chat;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
//import chat.DB;
import org.json.simple.*;
import java.sql.*;
import java.io.File;
import java.nio.file.*;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.output.DeferredFileOutputStream;
 
public class Register extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

		// Opens the current users session
		HttpSession session = request.getSession(true);

		chat.DB database = null;

		try{
			String username = null;
			String password = null;

			FileItem imagePart = null;
			InputStream imageInputStream = null;
			PrintWriter output = response.getWriter();

			List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
			for(FileItem item : items){
				if (item.isFormField()) {
					String fieldname = item.getFieldName();
					String fieldvalue = item.getString();

					if(fieldname.equals("username")){
						username = fieldvalue;
					}
					if(fieldname.equals("password")){
						password = fieldvalue;
					}
				} else {
					String fieldname = item.getFieldName();
					if(fieldname.equals("image")){
						//String filename = FilenameUtils.getName(item.getName());
						imagePart = item;
						imageInputStream = item.getInputStream();
					}
				}
			}


			/////////////////////////////////////////////
			if(username == null){
				output.println("user null");
			}
			if(password == null){
				output.println("pass null");
			}
			/////////////////////////////////////////////




			database = new chat.DB();

			long longResult = database.createUser(username, password);
			int userid = (int)Math.min(longResult, Integer.MAX_VALUE);

			if(userid != -1){

				// Save image file if provided
				//Part imagePart = request.getPart("image");
				//InputStream imageInputStream = imagePart.getInputStream();
				
				//String fileType = Files.probeContentType(imagePart.getFile().getAbsolutePath());

				String fileType = imagePart.getContentType();

				if(fileType.equals("image/jpeg")){
					String[] splitType = fileType.split("/");
					String extension = "";
					if(splitType[1].equals("jpeg")){
						extension = "jpg";
					}else{
						extension = splitType[1];
					}
					File newFile = new File(System.getProperty("user.dir")+"\\webapps\\chatroom\\user_images\\"+userid+"."+extension);
					newFile.createNewFile();
					imagePart.write(newFile);
				}
				

				session.setAttribute("userid", userid);
				session.setAttribute("username", username);

				// Go to chatroom if user created successfully
				response.sendRedirect("chatroom.jsp");
			}else{
				// Return to form is name is already taken
				response.sendRedirect("registerform.jsp?error");
			}

		}catch(Exception e){
			e.printStackTrace();
			//response.sendRedirect("registerform.jsp?"+e);
		}finally{
			try{
				database.close();
			}catch(Exception e){}
		}
	}


	public static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
}
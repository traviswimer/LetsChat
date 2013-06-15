package chat;

import java.sql.*;
import java.util.Date;
import java.security.*;

public class DB{

	// Database login information
	static String connectionURL = "jdbc:mysql://localhost:3306/chatroom";
	static String conncetionUsername = "root";
	static String connectionPassword = "";

	// Connection variables
	private java.sql.Connection con = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet results = null;

	// Constructor
	public DB() throws Exception{
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(connectionURL, conncetionUsername, connectionPassword);
			statement = con.createStatement();
		}catch(Exception e){
			throw e;
		}
	}

	// creates a new user
	public long createUser(String username, String password) throws Exception{
		try{
			// Check if username already taken
			if( !userExists(username) ){
				long userId = insertUser(username, password);
				return userId;
			}
			return -1;

		}catch(Exception e){
			throw e;
		}
	}

	// Determines if a username/password combination is correct
	public int authenticateUser(String username, String password) throws Exception{
		try{
			String hashedPass = sha512ify(password);

			preparedStatement = con.prepareStatement("SELECT COUNT(*) AS `row_count`, `userid` FROM `users` WHERE `username` = ? AND `password` = ? LIMIT 1");
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, hashedPass);
			results = preparedStatement.executeQuery();

			results.first();

			if( results.getInt("row_count") > 0 ){
				return results.getInt("userid");
			}else{
				return -1;
			}
		}catch(NoSuchAlgorithmException e){
			return -1;
		}
	}

	// creates a new chat messages
	public boolean postMessage(String userId, String message) throws Exception{
		preparedStatement = con.prepareStatement("INSERT INTO `messages` (`userid`, `message`) VALUES (?, ?)");
		preparedStatement.setString(1, userId);
		preparedStatement.setString(2, message);
		int success = preparedStatement.executeUpdate();
		
		if(success > 0){
			return true;
		}else{
			return false;
		}
	}

	// retrieves an array of chat messages posted since the last message received
	public ResultSet getMessages(int lastMsgId) throws SQLException{
		preparedStatement = con.prepareStatement("SELECT * FROM `messages` LEFT JOIN `users` ON users.userid=messages.userid WHERE `messageid`>? ORDER BY `messageid` DESC LIMIT 25");
		preparedStatement.setInt(1, lastMsgId);
		results = preparedStatement.executeQuery();

		return results;

	}


	// Adds a chat message to the database
	public boolean postMessage(int userid, String message) throws SQLException{
		preparedStatement = con.prepareStatement("INSERT INTO `messages` (`userid`, `message`) VALUES (?, ?)");
		preparedStatement.setInt(1, userid);
		preparedStatement.setString(2, message);
		int success = preparedStatement.executeUpdate();
		
		if(success > 0){
			return true;
		}else{
			return false;
		}
	}

	// retrieves an array users who posted in the past 10 minutes
	public ResultSet getUsers() throws SQLException{
		java.util.Date date = new java.util.Date();
	 	java.sql.Timestamp anHourAgo = new java.sql.Timestamp( System.currentTimeMillis() - (10 * 60 * 1000) );

		preparedStatement = con.prepareStatement("SELECT * FROM `messages` LEFT JOIN `users` ON users.userid=messages.userid WHERE `time` > ? GROUP BY messages.userid");
		preparedStatement.setTimestamp(1, anHourAgo);
		results = preparedStatement.executeQuery();

		return results;
	}


	// Closes the database connection
	public void close() throws Exception{
		try{
			con.close();
		}catch(Exception e){
			throw e;
		}
	}


	// Check if username already taken
	private boolean userExists(String username) throws SQLException{
		preparedStatement = con.prepareStatement("SELECT COUNT(*) AS `row_count` FROM `users` WHERE `username` = ?");
		preparedStatement.setString(1, username);
		results = preparedStatement.executeQuery();

		results.first();

		if( results.getInt("row_count") > 0 ){
			return true;
		}else{
			return false;
		}
	}


	// Add user to database
	private long insertUser(String username, String password) throws SQLException{
		try{
			String hashedPass = sha512ify(password);

			preparedStatement = con.prepareStatement("INSERT INTO `users` (`username`, `password`) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, hashedPass);
			int success = preparedStatement.executeUpdate();
			
			if(success > 0){
				ResultSet keys = preparedStatement.getGeneratedKeys();
				if (keys != null && keys.first()) {
	    			long key = keys.getLong(1);
					return key;
				}else{
					return -1;
				}
			}else{
				return -1;
			}
		}catch(NoSuchAlgorithmException e){
			return -1;
		}
	}


	// Creates a password hash
	private String sha512ify(String password) throws NoSuchAlgorithmException{
		MessageDigest digester;
		try{
			digester = MessageDigest.getInstance("SHA-512");

			digester.update(password.getBytes());
			byte[] byteArray = digester.digest();
			String hashedPassword = "";

			for( int i = 0; i < byteArray.length; i++ ){
				String hexString = Integer.toHexString( new Byte(byteArray[i]) );
				while (hexString.length() < 2) {
					hexString = "0" + hexString;
				}
				hexString = hexString.substring(hexString.length() - 2);
				hashedPassword += hexString;
			}
			return hashedPassword;

		}catch(NoSuchAlgorithmException e) {
			throw e;
		}
	}



}

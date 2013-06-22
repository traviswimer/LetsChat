<?php
class Chat{
	private $userid;
	private $conn;

	function __construct(DB $the_connection = null){
		$this->conn = $the_connection ? $the_connection : null;
		if( isset($_SESSION['userid']) ){
			$this->userid = $_SESSION['userid'];
		}
	}

	// retrieves a list of most recent chat messages
	public function getMessages($last_msg_id){

		$last_msg_id = intval($last_msg_id);

		try{
			// fetch messages from database
			$statement = $this->conn->prepare(
				"SELECT 
					* 
				FROM 
					`messages` 
				LEFT JOIN 
					`users` 
				ON 
					users.userid=messages.userid 
				WHERE 
					`messageid`>:lastid 
				ORDER BY 
					`messageid` 
				DESC LIMIT 25
				");
			$statement->bindParam(':lastid', $last_msg_id, PDO::PARAM_INT, 11);
			$statement->execute();

			$message_data = $statement->fetchAll(PDO::FETCH_ASSOC);

			$return_array = array();
			foreach($message_data as $row){
				$new_row_array = array();
				$new_row_array['msgId'] = $row['messageid'];
				$new_row_array['userid'] = $row['userid'];
				$new_row_array['user'] = $row['username'];
				$new_row_array['text'] = $row['message'];
				array_push($return_array, $new_row_array);
			}
			
			return $return_array;

		}catch(Exception $e){
			return array();
		}

	}

	public function __destruct(){
		$this->connection = null;
	}
}
?>
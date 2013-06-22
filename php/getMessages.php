<?php
	session_start();
	if( !isset($_SESSION['userid']) ){
		echo json_encode( array("messages" => array()) );
	}

	include('DB.php');
	include('Chat.php');
	$chat = new Chat(new DB());

	$last_msg_id = isset($_GET['lastMsgId']) ? $_GET['lastMsgId'] : 0;

	$message_array = $chat->getMessages($last_msg_id);

	echo json_encode( array("messages" => $message_array) );

?>
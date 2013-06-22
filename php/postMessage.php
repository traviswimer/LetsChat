<?php
	session_start();
	if( !isset($_SESSION['userid']) ){
		echo json_encode( array("success" => false) );
	}

	include('DB.php');
	include('Chat.php');
	$chat = new Chat(new DB());

	$message = isset($_POST['msg']) ? $_POST['msg'] : false;

	if($message){
		$message_success = $chat->postMessage($message);

		echo json_encode( array("success" => $message_success) );
	}else{
		echo json_encode( array("success" => false) );
	}

?>
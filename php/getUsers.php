<?php
	session_start();
	if( !isset($_SESSION['userid']) ){
		echo json_encode( array("users" => array()) );
	}

	include('DB.php');
	include('Chat.php');
	$chat = new Chat(new DB());

	$users_array = $chat->getUsers();

	echo json_encode( array("users" => $users_array) );

?>
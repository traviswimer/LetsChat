<?php
	session_start();
	if( !isset($_SESSION['userid']) ){
		header('Location: chatroom.php');
	}

	include('DB.php');
	include('User.php');
	$user = new User();
	$user->logout();

	header('Location: index.php');
?>
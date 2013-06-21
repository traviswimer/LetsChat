<?php
class DB extends PDO{
	public function __construct(){
		$dbAndHost = "mysql:dbname=chatroom;host=localhost";
		$username = "root";
		$password = "";
		$driver = array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION);
		parent::__construct($dbAndHost, $username, $password, $driver);
	}
}
?>
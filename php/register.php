<?php
	session_start();
	if( isset($_SESSION['userid']) ){
		header('Location: chatroom.php');
	}

	include('DB.php');
	include('User.php');

	if( isset($_POST['username'], $_POST['password'], $_FILES['image'])){
		$username = $_POST['username'];
		$password = $_POST['password'];
		$image = $_FILES['image'];
		
		try{
			$user = new User( new DB() );
			$user->register($username, $password, $image);
			header('Location: chatroom.php');
		}catch(Exception $e){
			$error_msg = $e->getMessage();
		}
	}

?><!DOCTYPE html>
	<head>
		<title>Login to Chat</title>
		<link rel="stylesheet" type="text/css" href="style.css">
	</head>
	<body>
		<form id="login-form" method="post" action="?" enctype="multipart/form-data">
			<h1>Sign-up to Chat!</h1>
			<?php
			if( isset($error_msg) ){
				echo "<h3>".$error_msg."</h3>";
			}
			?>
			<div>
				<input type="text" placeholder="Username" name="username" class="text-field input-top">
			</div>
			<div>
				<input type="password" placeholder="Password" name="password" class="text-field input-bottom">
			</div>
			<div>
				<label>Upload an image</label><input type="file" name="image">
			</div>
			<div>
				<input type="submit" value="Register" class="login-btn">
			</div>
		</form>

	</body>
</html>
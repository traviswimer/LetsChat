<?php
	session_start();

	if( isset($_POST['username'], $_POST['password'])){
		$username = $_POST['username'];
		$password = $_POST['password'];
	}

?><!DOCTYPE html>
	<head>
		<title>Login to Chat</title>
		<link rel="stylesheet" type="text/css" href="style.css">
	</head>
	<body>
		<form id="login-form" method="post" action="?">
			<h1>Let's Chat!</h1>
			<?php
			if( isset($_GET['error_msg']) && !empty($_GET['error_msg']) ){
				echo "<h3>".$_GET['error_msg']."</h3>";
			}
			?>
			<div>
				<input type="text" placeholder="Username" name="username" class="text-field input-top">
			</div>
			<div>
				<input type="password" placeholder="Password" name="password" class="text-field input-bottom">
			</div>
			<div>
				<input type="submit" value="Login" class="login-btn">
			</div>
			<div class="register-link">
				<a href="registerform.php">Create New Account</a>
			</div>
		</form>

	</body>
</html>
<?php
class User{
	private $userid;
	private $conn;

	function __construct(DB $the_connection){
		$this->conn = $the_connection ? $the_connection : null;
	}

	// Creates a new user in the database
	public function register($username, $password, $image){

		// Make sure username/password are valid
		if( empty($username) || empty($password) ){
			throw new Exception("Invalid Username or Password");
		}

		// Add user to database
		$statement = $this->conn->prepare(
			'INSERT INTO 
				`users` 
				(
					`username`, 
					`password`
				) 
			VALUES 
				(
					:username, 
					:password
				)
		');
		$statement->bindParam(':username', $username, PDO::PARAM_STR, 20);
		$statement->bindParam(':password', hash('sha512', $password), PDO::PARAM_STR, 128);
		$statement->execute();

		$new_users_id = $this->conn->lastInsertId();

		// Sets the user as logged in
		$_SESSION['userid'] = $new_users_id;

		// Upload image if included
		if( isset($image) ){
 			$the_file = $image['tmp_name'];
			$image_data = @getimagesize($the_file);

			if( $image_data[2] ){
				// create and image object based on file type
				switch( $image_data[2] ){
					case IMAGETYPE_GIF:
						$new_img_holder = imagecreatefromgif($the_file);
						break;
					case IMAGETYPE_JPEG:
						$new_img_holder = imagecreatefromjpeg($the_file);
						break;
					case IMAGETYPE_PNG:
						$new_img_holder = imagecreatefrompng($the_file);
						break;
					default:
						$new_img_holder = null;
				}

				if( !empty($new_img_holder) ){
					$img_size = 60;
					$orig_img_width = $image_data[0];
					$orig_img_height = $image_data[1];


					$temp_img = imagecreatetruecolor($img_size, $img_size);

					imagecopyresampled(
						$temp_img,
						$new_img_holder,
						0,0,0,0,
						$img_size,
						$img_size,
						$orig_img_width,
						$orig_img_height
					);

					imagejpeg($temp_img, "user_images/".$new_users_id.".jpg", 100);
					imagedestroy($new_img_holder);
					imagedestroy($temp_img);
				}

			}
		}

	}

	// logs in a user based on their username/password
	public function login($username, $password){

	}

	// clears the users session
	public function logout(){

	}

	public function __destruct(){
		$this->connection = null;
	}
}
?>
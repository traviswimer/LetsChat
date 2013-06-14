(function(){

// Check for document finished loading
$(function($){
	chat.init();
});


var chat = (function(){

	// URLs for sending ajax requests
	var messageURL = "GetMessages";
	var postMessageURL = "PostMessage";
	var usersURL = "GetUsers";

	// Array of users already in the list
	var currentUsers = [];

	// ID of last message recieved
	var lastMsgId = 0;

	// Adds a chat message to the chat feed
	function addChatMessage(username, message){
		$('#chat-feed').append("<div>"+username+" - "+message+"</div>");
	}

	// adds a user to the user list
	function addUser(userInfo){
		$('#chatters-list').append('<img id="user-pic-'+userInfo.id+'" class="chatter" src="user_images/1.jpg">');
	}

	// adds a user to the user list
	function removeUser(userId){
		var userPic = document.getElementById('user-pic-'+userId);
		if(userPic){
			userPic.parentNode.removeChild(userPic);
		}
	}

	// Retrieves new chat messages from the server
	function updateChatFeed(){
		$.get(messageURL, {lastMsgId: lastMsgId}, function(data){
			if(data && data.messages){

				// Remove loading message if response
				if(lastMsgId === 0){
					$('#chat-feed').html('');
				}

				var messages = data.messages;
				if(messages.length>0){

					//update last message received
					lastMsgId = messages[0].msgId;

					// Loop through all new messages
					for(var i=0; i<messages.length; i++){
						var theMessage = messages[i];

						// Add user to list if not already in list
						if( $.inArray(theMessage.name, currentUsers) === -1 ){
							/*addUser({
								id: theMessage.userid,
								name: theMessage.user
							});
							currentUsers.push(theMessage.userid);*/
						}

						// Add message to the feed
						addChatMessage(theMessage.user, theMessage.text);
					}
				}

			}
		}, "json");
	}

	// Retrieves current chat users
	function updateUsers(){
		$.get(usersURL, function(data){
			if(data && data.users){
				var users = data.users;
				var i;

				for(i=0; i<users.length; i++){
					var theUser = users[i];

					// Add user to list if not already in list
					if( $.inArray(theUser.id, currentUsers) === -1 ){
						addUser(theUser);
						currentUsers.push(theUser.id);
					}
				}

				// remove users no longer active
				var usersToRemove = arrayDifference(currentUsers, users);
				for(i=0; i<usersToRemove.length; i++){
					removeUser(usersToRemove[i]);
				}
				currentUsers = users;
			}
		}, "json");
	}

	// sends a chat message to the server
	function postMessage(message, attempts){
		if(typeof attempts == "undefined"){
			attempts = 0;
		}
		if(attempts<5){
			$.post(postMessageURL, {msg: message}, function(data){
				if(!data || data.success !== true){
					setTimeout(postMessage, 1000, message, attempts+1);
				}
			}, 'json');
		}
	}

	// Initialize chat functionality
	function init(){
		updateChatFeed();
		updateUsers();

		setInterval(updateChatFeed, 2000);
		setInterval(updateUsers, 30000);


		$('#chat-post-form').submit(function(e){
			var newMessage = $('#chat-msg-input').val();
			$('#chat-msg-input').val('');
			postMessage(newMessage);
			return false;
		});
	}

	// Return public methods
	return {
		init: init
	};



	function arrayDifference(array1, array2){
		return $.grep(array1, function(x){
			return $.inArray(x, array2) < 0;
		});
	}
})();

})();
(function(){

// Check for document finished loading
$(function(){
	$.ajaxSetup({
		beforeSend: function(xhr, settings) {
			function getCookie(name) {
				var cookieValue = null;
				if (document.cookie && document.cookie != '') {
					var cookies = document.cookie.split(';');
					for (var i = 0; i < cookies.length; i++) {
						var cookie = jQuery.trim(cookies[i]);
						// Does this cookie string begin with the name we want?
					if (cookie.substring(0, name.length + 1) == (name + '=')) {
						cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
						break;
					}
				}
			}
			return cookieValue;
			}
			if (!(/^http:.*/.test(settings.url) || /^https:.*/.test(settings.url))) {
				// Only send the token to relative URLs i.e. locally.
				xhr.setRequestHeader("X-CSRFToken", getCookie('csrftoken'));
			}
		}
	});

	chat.init();
});


var chat = (function(){

	// URLs for sending ajax requests
	var messageURL = "GetMessages";
	var postMessageURL = "PostMessage";
	var usersURL = "GetUsers";

	// ID of last message recieved
	var lastMsgId = 0;

	// Adds a chat message to the chat feed
	function addChatMessage(userid, username, message){
		// Create contents of message
		var userMsgName = document.createElement('div');
		userMsgName.className = "msg-user";
		userMsgName.innerHTML = username;
		var userImg = document.createElement('img');
		userImg.className = "msg-img";
		userImg.src = "/static/user_images/user.png";
		var userMsg = document.createElement('div');
		userMsg.className = "msg-text";
		userMsg.innerHTML = message;

		var msgDiv = document.createElement('div');
		msgDiv.className = "msg-box";
		msgDiv.appendChild(userMsgName);
		msgDiv.appendChild(userImg);
		msgDiv.appendChild(userMsg);

		document.getElementById('chat-feed').appendChild(msgDiv);

		// scroll to bottom
		var feed = document.getElementById("chat-feed");
		feed.scrollTop = feed.scrollHeight;
	}

	// adds a user to the user list
	function addUser(userInfo){
		$('#chatters-list').append('<img id="user-pic-'+userInfo.id+'" class="chatter" src="/static/user_images/user.png">');
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
					for(var i=messages.length-1; i>=0; i--){
						var theMessage = messages[i];

						// Add message to the feed
						addChatMessage(theMessage.userid, theMessage.user, theMessage.text);
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
				$('#chatters-list').html("");
				for(i=0; i<users.length; i++){
					var theUser = users[i];

					// Add user to list if not already in list
					addUser(theUser);
				}

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

		setInterval(updateChatFeed, 1000);
		setInterval(updateUsers, 5000);


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
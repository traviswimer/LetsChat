<!DOCTYPE html>
	<head>
		<title>Let's Chat</title>
		<link rel="stylesheet" type="text/css" href="style.css">
	</head>
	<body>
		<div id="chat-container">
			<div class="bg-box"></div>
			<div class="left-col">
				<div id="chat-feed">
					Loading...
				</div>
				<div id="chat-input-container">
					<form id="chat-post-form">
						<input id="chat-msg-input" type="text" placeholder="Enter chat message...">
					</form>
				</div>
			</div>
			<div class="right-col">
				<div id="chat-title">Let's Chat!</div>
				<div id="chatters-list">
				</div>
			</div>
		</div>
		<script type="text/javascript" src="scripts/zepto.min.js"></script>
		<script type="text/javascript" src="scripts/chat.js"></script>
	</body>
</html>

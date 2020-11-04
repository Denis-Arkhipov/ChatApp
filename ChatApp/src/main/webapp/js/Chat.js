/**
 * Implements the chat page methods.
 * 
 * @author Arkhipov Denis
 * @version 1.0 2020
 */

init();

function init() {
	let sendButton = document.querySelector("[name='buttonSendMessage']");
	let back = document.querySelector("#back");
	let userInfoBlock = document.querySelector("#userInfo");
	
	sendButton.addEventListener("click", sendUserMessage);
	back.addEventListener("click", logout);
	
	userInfoBlock.addEventListener("mouseover", function() {
		userInfoBlock.classList.add("showBlock");
	});
	
	userInfoBlock.addEventListener("mouseout", function() {
		userInfoBlock.classList.remove("showBlock");
	});
}


function openChatWindow(user) {
	let windowLogin = document.querySelector("#window_login");
	let windowChat = document.querySelector("#chat");
	let main = document.querySelector("main");
	let back = document.querySelector("#back");	
	let userInfoBlock = document.querySelector("#userInfo");
	let username = document.querySelector("#username");
	let email = document.querySelector("#email > span");
	let phone = document.querySelector("#phone > span");
	
	main.classList.add("chatWindow");
	userInfoBlock.classList.remove("hideBlock");
	back.classList.remove("hideBlock");
	
	username.textContent = user.username;
	email.textContent = user.email;
	phone.textContent = user.phone;

	getMessageHistory().then(listMessages => {				
		appendBlockMessage(listMessages);
		
		getUsersOnline().then(listUsers => {
			downloadPhoto(user.photoName);
			appendBlockUsers(listUsers);						
		});
	});

	windowLogin.hidden = true;
	windowChat.hidden = false;
}


function downloadPhoto(photoName) {
	let url = new URL("Controller/image", window.location.href);
	let img = document.querySelector("img");
	let photoNameJson = JSON.stringify(photoName);
	let csrfToken = getToken();	
	
	fetch(url, {
		method: "POST",
		headers: {
			'Content-Type': 'application/json;charset=utf-8',
			'CSRF-TOKEN' : csrfToken
		},
		body: photoNameJson
	})
		.then(response => {
			if (response.ok) {
				response.blob().then(blob => {
					addToken(response);
					img.src = URL.createObjectURL(blob);					
				})
			} else {
				showErrorMessage("Failed to load image!");
			}
		});
}

function appendBlockMessage(listMessages) {		
	let messages = document.querySelector(".messages");
	
	for(let i = 0; i < listMessages.length; i++) {
		let divContentMessage = document.createElement("div");
		let spanDate = document.createElement("span");
		let spanUsername = document.createElement("span");
		let spanTextMessage = document.createElement("span");
	
		divContentMessage.className = "contentMessage";
		spanDate.className = "dateMessage";
		spanUsername.className = "userNameMessage";
		spanTextMessage.className = "textMessage";
	
		spanDate.textContent = formatDate(listMessages[i].date) + " ";
		spanUsername.textContent = listMessages[i].username + ": ";
		spanTextMessage.textContent = listMessages[i].textMessage;
	
		divContentMessage.append(spanDate);
		divContentMessage.append(spanUsername);
		divContentMessage.append(spanTextMessage);
	
		messages.append(divContentMessage);	
	}
	
	scrollDown();	
}

function scrollDown() {
	let messagesElement = document.querySelector(".messages");
	messagesElement.scrollTop = messagesElement.scrollHeight;
}

function formatDate(milliseconds) {
	let date = new Date(milliseconds);	
	let day = date.getDate();
	let month = date.getMonth();
	let year = date.getFullYear();
	let hour = date.getHours();
	let min = date.getMinutes();
	let sec = date.getSeconds();	
	let dateAsString = day + "." + month + "." + year + " " + 
						hour + ":" + min + ":" + sec;
	
	return dateAsString;
}


function getUsersOnline() {
	let url = new URL("Controller/users/online", window.location.href);
	let csrfToken = getToken();
	
	return fetch(url, {
		method: "POST",
		headers: {
			'Content-Type': 'application/json;charset=utf-8',
			'CSRF-TOKEN' : csrfToken
		},
	}).then(response => {
		addToken(response);
		return response.json();
	});
}

function appendBlockUsers(listUsers) {
	let ulPerson = document.querySelector(".person");
	let username = document.querySelector("#username").textContent;
	
	for(let i = 0; i < listUsers.length; i++) {
		if (username !== listUsers[i].username) {
			let liPerson = document.createElement("li");	
			liPerson.textContent = listUsers[i].username;	
			ulPerson.append(liPerson);
		}
	}
}


function sendMessage(message) {
	let url = new URL("Controller/message", window.location.href);
	let messageJson = JSON.stringify(message);
	let csrfToken = getToken();
		
	fetch(url, {
		method: "POST",
		headers: {
			'Content-Type': 'application/json;charset=utf-8',
			'CSRF-TOKEN' : csrfToken
		},
		body: messageJson
	})
		.then(response => {
			if (response.ok) {
				response.json().then(result => {				
					addToken(response);					
					appendBlockMessage(result);					
				})
			} else {
				showErrorMessage("Message not send!");
			}
		});		
}

function sendUserMessage() {
	let fieldMessage = document.forms.formChat.fieldTextMessage;
	
	if (fieldMessage.value !== "" && fieldMessage.value !== " ") {
		let username = document.querySelector("#username").textContent;	 
		let messageOfUser = new Message(Date.now(), username, 
				fieldMessage.value, "MESSAGE");

		clearField("fieldTextMessage");
		sendMessage(messageOfUser);
	}		
}


function getMessageHistory() {	
	let url = new URL("Controller/message/history", window.location.href);
	let username = document.querySelector("#username").textContent;	
	let userJson = JSON.stringify(username);
	let csrfToken = getToken();
	
	return fetch(url, {
		method: "POST",
		headers: {
			'Content-Type': 'application/json;charset=utf-8',
			'CSRF-TOKEN' : csrfToken
		},
		body: userJson
	}).then(response => {
		addToken(response);
		return response.json();
	});
}


function logout() {
	let url = new URL("Controller/user/logout", window.location.href);
	let username = document.querySelector("#username").textContent;	
	let img = document.querySelector("img");
	let csrfToken = getToken();	
	let logoutJson = JSON.stringify(username);
		
	fetch(url, {
		method: "POST",
		headers: {
			'Content-Type': 'application/json;charset=utf-8',
			'CSRF-TOKEN' : csrfToken,
			'DateLogout' : Date.now()
		},
		body: logoutJson
	}).then(() => {		
		URL.revokeObjectURL(img.src);
		openLoginWindow();		
	});
}




/**
 * Starting point of the application. 
 * Sends the username and password to the server and processes the result.
 * 
 * @author Arkhipov Denis
 * @version 1.0 2020
 */

init();

function init() {	
	let buttonLogin = document.querySelector("[name='buttonLogin']");
	let buttonReg = document.querySelector("[name='buttonRegistration']");
		
	buttonLogin.addEventListener("click", login);	
	buttonReg.addEventListener("click", openRegWindow);
}


function login() {
	let url = new URL("Controller/user/login", window.location.href);
	let loginField = document.forms.formLogin.login;
	let passwordField = document.forms.formLogin.password;	

	if(loginField.value && passwordField.value) {
		let user = new User(loginField.value, passwordField.value);		
		let userJson = JSON.stringify(user);
		
		fetch(url, {
			method: "POST",
			headers: {
				'Content-Type': 'application/json;charset=utf-8',
				'LoginDate' : Date.now()
			},
			body: userJson
		}).then(response => {
			if (response.ok) {				
				response.json().then(result => {						
					addToken(response);
					showError("");
					openChatWindow(result);
				})
			} else{
				showError("User not registered");
			}
		});		
	}  else {
		showError("Enter username and password.");
	}

}

function openLoginWindow() {
	let windowLogin = document.querySelector("#window_login");
	let windowReg = document.querySelector("#window_reg");
	let windowChat = document.querySelector("#chat");
	let main = document.querySelector("main");
	let back = document.querySelector("#back");
	let user = document.querySelector("#username");

	windowChat.hidden = true;
	windowReg.hidden = true;
	windowLogin.hidden = false;

	back.textContent = "";
	user.textContent = "";	
	main.classList.remove("chatWindow");
	
	window.location.reload();
}




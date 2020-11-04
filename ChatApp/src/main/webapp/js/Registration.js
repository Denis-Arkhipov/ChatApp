/**
 * Performs user registration.
 * 
 * @author Arkhipov Denis
 * @version 1.0 2020
 */

init();

function init() {
	let buttonSendReg = document.querySelector("[name='sendRegistration']");
	let buttonRegLogin = document.querySelector("[name='buttonRegLogin']");
	
	buttonSendReg.addEventListener("click", registration);
	buttonRegLogin.addEventListener("click", () => window.location.reload());
}


function registration() {
	let formReg = document.forms.formReg;
	let formData = new FormData(formReg);
	let url = new URL("Controller/user/registration", window.location.href);
	
	if(formReg.loginReg.value && formReg.Email.value && 
			formReg.passwordReg.value) {		
		fetch(url, {
			method: "POST",
			body: formData
		}).then(response => {
			if (response.ok) {				
				openLoginWindow();				
			} else {
				showError("User not registered! Probably Username is taken.");
			}
		});		
	}  else {
		showError("Fill in all the fields.");
	}
}

function openRegWindow() {
	let windowLogin = document.querySelector("#window_login");
	let windowReg = document.querySelector("#window_reg");
	
	windowLogin.hidden = true;
	windowReg.hidden = false;
}



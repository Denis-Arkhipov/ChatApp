/**
 * Additional methods for running the application.
 * 
 * @author Arkhipov Denis
 * @version 1.0 2020
 */
function addToken(response) {
	let csrf = document.querySelector("[name='csrf']");
	csrf.value = response.headers.get("CSRF-TOKEN");
}

function getToken() {
	let csrfElem = document.querySelector("[name='csrf']");
	let csrfToken = csrfElem.value;
	
	return csrfToken;
}

function showError(textError) {
	let divError = document.querySelector(".error");
	
	divError.textContent = textError;
}

function showErrorMessage(textError) {
	let messages = document.querySelector(".messages");
	let divContentMessage = document.createElement("div");
	
	divContentMessage.className = "contentMessage";
	divContentMessage.textContent = textError;
	
	messages.append(divContentMessage);
}

function clearField(nameField) {
	let selector = "[name='" + nameField + "']";
	let fieldName = document.querySelector(selector);
	let divError = document.querySelector(".error");
	
	fieldName.value = "";
	divError.textContent = "";
}
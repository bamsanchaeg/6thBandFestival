// 로그인..
function loginService() {
    const inputEmail = document.getElementById("inputEmail");
    email = inputEmail.value;
    console.log("email : " + email);

    const inputPassword = document.getElementById("inputPassword");
    password = inputPassword.value;
    console.log("password : " + password);

    const url = `/api/service/getUserInfo?email=${email}&password=${password}`
    fetch(url)
        .then(response => response.json())
        .then((response) => {
            console.log(response.data.searchUserInfo);
            if (response.data.searchUserInfo == null) {
                inputEmail.value = '';
                inputPassword.value = '';
                const loginCheck = document.getElementById("loginCheck");
                loginCheck.classList.toggle('hidden-with-space');
            } else {
                const serviceTeamLoginForm = document.getElementById('serviceTeamLoginForm');
                serviceTeamLoginForm.submit();
            }
        });
}

window.addEventListener("DOMContentLoaded", () => {
    document.getElementById('inputEmail').value = 'eunjin@service.com';
    document.getElementById('inputPassword').value = 'eunjin1234';
});
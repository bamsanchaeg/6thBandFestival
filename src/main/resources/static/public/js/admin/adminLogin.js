// 로그인..
function loginAdmin() {
    const inputAccountName = document.getElementById("inputAccountName");
    accountName = inputAccountName.value;
    console.log("accountName : " + accountName);

    const inputPassword = document.getElementById("inputPassword");
    password = inputPassword.value;
    console.log("password : " + password);

    const url = `/six/user/getAdminInfo?account_name=${accountName}&password=${password}`
    fetch(url)
        .then(response => response.json())
        .then((response) => {
            console.log(response.data.searchAdminInfo);
            if (response.data.searchAdminInfo == null) {
                inputAccountName.value = '';
                inputPassword.value = '';
                const loginCheck = document.getElementById("loginCheck");
                loginCheck.classList.toggle('hidden-with-space');
            } else {
                const adminLoginForm = document.getElementById('adminLoginForm');
                adminLoginForm.submit();
            }
        });
}

window.addEventListener("DOMContentLoaded", () => {
    document.getElementById('inputAccountName').value = 'officialband';
    document.getElementById('inputPassword').value = 'officialband1234';
});
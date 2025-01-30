const url = "/api/bubble/getArtistApplyList"

function getApplicationList() {
    fetch(url)
        .then(response => response.json())
        .then(response => {

            const applications = response.data.applications;

            //메세지 반복문 출력
            const applicationListBox = document.getElementById("applicationListBox");
            applicationListBox.innerHTML = "";

            const applicationWrapperTemplete = document.querySelector("#templete .applicationListWrapper");

            for (let e of applications) {

                const newApplicationWrapper = applicationWrapperTemplete.cloneNode(true);
                newApplicationWrapper.classList.remove('d-none');

                const applicationId = newApplicationWrapper.querySelector(".applicationId");
                applicationId.innerText = e.id;

                const userId = newApplicationWrapper.querySelector(".userId");
                userId.innerText = e.user_id;

                const userName = newApplicationWrapper.querySelector(".userName");
                userName.innerText = e.user_name;

                const emailAddress = newApplicationWrapper.querySelector(".emailAddress");
                emailAddress.innerText = e.email_address;

                const accessStatement = newApplicationWrapper.querySelector(".accessStatement");
                accessStatement.innerText = e.access_statement;

                const getApplicationId = newApplicationWrapper.querySelector(".getApplicationId");
                getApplicationId.href = '/admin/bubble/applicationDetail?id=' + e.id;

                applicationListBox.appendChild(newApplicationWrapper);

            }

        })
}

window.addEventListener("DOMContentLoaded", () => {
    getApplicationList();
})
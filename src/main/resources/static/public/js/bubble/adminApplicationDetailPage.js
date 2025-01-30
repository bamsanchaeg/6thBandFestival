const urlParams = new URL(window.location.href).searchParams;
const applicationId = urlParams.get("id");
const applicationUrl = "/api/bubble/getApplicationDetail?id=" + applicationId;

function getApplicationDetail() {
    fetch(applicationUrl)
        .then(response => response.json())
        .then(response => {

            const e = response.data.applicationDetail;

            const applicationId = document.getElementById("applicationId");
            applicationId.innerText = e.id;

            const applicationUserId = document.getElementById("applicationUserId");
            applicationUserId.innerText = e.user_id;

            const applicationUserName = document.getElementById("applicationUserName");
            applicationUserName.innerText = e.user_name;

            const applicationEmailAddress = document.getElementById("applicationEmailAddress");
            applicationEmailAddress.innerText = e.email_address;

            const certificationImage = document.getElementById("certificationImage");
            certificationImage.src = '/images/' + e.identification_image;

            const createdDate = document.getElementById("createdDate");
            createdDate.innerText = addNineHours(e.created_at).toISOString().split('T')[0];


            //눌러서 승인버튼 update 쿼리 작동하게 하기
            // 승인 버튼에 update 쿼리 작동하게 하기
            const accessStatement = document.getElementById("accessStatement");
            accessStatement.innerText = e.access_statement;
            // 이미 승인된 상태인지 확인
            if (e.access_statement === 'Y') { // 'Y'가 승인된 상태를 나타낸다고 가정
                accessStatement.innerText = "승인완료된 사항입니다.";
                accessStatement.disabled = true; // 버튼 비활성화
                accessStatement.style.cursor = "not-allowed"; // 커서를 비활성화 모양으로 변경
                accessStatement.style.backgroundColor = "#d3d3d3"; // 버튼 색상 변경 (선택 사항)
            } else {
                accessStatement.innerText = "승인대기중";
                accessStatement.onclick = function () {
                    acceptRegister(e.id); // 클릭 시 acceptRegister 함수 호출, id 전달
                };
            }


        })
}

function acceptRegister(applicationId) {
    const acceptUrl = "/api/bubble/updateStatement";
    fetch(acceptUrl, {
        method: "post",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: `id=${applicationId}`
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            alert("처리가 완료되었습니다.")
            window.location.reload();
            return response.text(); // 응답을 텍스트로 받아서 확인
        })
}

// 타임스탬프에 9시간을 더하는 함수
function addNineHours(dateString) {
    const newDate = new Date(dateString);
    newDate.setHours(newDate.getHours() + 9);
    return newDate;
}

window.addEventListener("DOMContentLoaded", () => {
    getApplicationDetail();
})
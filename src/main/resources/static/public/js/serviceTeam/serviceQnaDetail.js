const urlParams = new URL(window.location.href).searchParams;
const qnaId = urlParams.get("id");
let qnaMyId = null;

// 아이디 세팅
function setSessionId() {
    const url = '/api/service/getSessionServiceId';

    fetch(url)
        .then(response => response.json())
        .then((response) => {
            noticeMyId = response.data.id;
            reloadServiceTeamQnA();
            reloadServiceTeamQnAAnswer();
        });
}

// qna 상세.. 개행용
function reloadServiceTeamQnA() {
    const url = `/api/service/getServiceQnADetail?id=${qnaId}`
    fetch(url)
        .then(response => response.json())
        .then((response) => {
            const e = response.data.serviceQnaDetail

            const qnaStatusSpan = document.getElementById('qnaStatusSpan');
            qnaStatusSpan.innerText = e.status;

            const qnaContentDiv = document.getElementById('qnaContentDiv');
            qnaContentDiv.innerText = e.content;
        });
}

// 날짜포맷
function dateFormat(formatDay) {
    const myDate = new Date(formatDay);

    let day = myDate.getDate();
    let month = myDate.getMonth() + 1;
    // .toString().slice(-2) 연도의 마지막 2자리뽑기
    let year = myDate.getFullYear().toString()

    // 두 자릿수로 맞추기 위해 day와 month가 10보다 작을 경우 앞에 0을 추가
    day = day < 10 ? '0' + day : day;
    month = month < 10 ? '0' + month : month;

    return `${year}.${month}.${day}`;
}

// 답변 입력 모달 열기
function answerModalOpen() {
    const serviceTeamQnAAnswerInsertModal = bootstrap.Modal.getOrCreateInstance("#serviceTeamQnAAnswerInsertModal");
    serviceTeamQnAAnswerInsertModal.show();
}

// 답변 입력 모달 닫기
function answerModalClose() {
    const serviceTeamQnAAnswerInsertModal = bootstrap.Modal.getOrCreateInstance("#serviceTeamQnAAnswerInsertModal");
    serviceTeamQnAAnswerInsertModal.hide();
}

// 답변 등록
function answerSubmit() {
    const qnaAnswerRegisterForm = document.getElementById("qnaAnswerRegisterForm");

    const inputBoardId = document.getElementById('inputBoardId');
    inputBoardId.value = qnaId;

    const formData = new FormData(qnaAnswerRegisterForm);

    if(!formData.get("inputImages") || formData.get("inputImages").name === "") {
        formData.set("inputImages", null);
    }

    const url = `/api/service/createQnaAnswer`
    fetch(url, {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then((response) => {
            answerModalClose();
            showMessage("답변 등록이 완료되었습니다.");
            changeStatus(qnaId);
            reloadServiceTeamQnAAnswer();
        });
}

// 상태 변경
function changeStatus(id) {
    const url = `/api/service/updateQnAStatusDone?id=${id}`;
    fetch(url)
        .then(response => response.json())
        .then(response => {
            reloadServiceTeamQnA();
        });
}

// qna 답변
function reloadServiceTeamQnAAnswer() {
    const url = `/api/service/getServiceQnAAnswer?id=${qnaId}`
    fetch(url)
        .then(response => response.json())
        .then((response) => {
            // const e = response.data.answer;

            const answerDiv = document.getElementById('answerDiv');
            answerDiv.innerText = response.data.answer.content;
        });
}

// 알림창
function showMessage(text) {
    const reviewUpdateDoneModal = bootstrap.Modal.getOrCreateInstance("#reviewUpdateDoneModal");
    reviewUpdateDoneModal.show();

    const testAlert = document.getElementById("testAlert")
    testAlert.innerText = text;

    setTimeout(() => {
        reviewUpdateDoneModal.hide();
    }, 1000);

}

window.addEventListener("DOMContentLoaded", () => {
    setSessionId();
});

const urlParams = new URL(window.location.href).searchParams;
const noticeId = urlParams.get("id");
let noticeMyId = null;

// 아이디 세팅
function setSessionId() {
    const url = '/api/service/getSessionServiceId';

    fetch(url)
        .then(response => response.json())
        .then((response) => {
            noticeMyId = response.data.id;
            reloadServiceTeamNoticeList();
        });
}

// 공지사항 하나
function reloadServiceTeamNoticeList() {
    const url = `/api/service/getServiceTeamNoticeById?id=${noticeId}`
    fetch(url)
        .then(response => response.json())
        .then((response) => {
            const e = response.data.serviceTeamNotice

            const noticeTitleSpan = document.getElementById('noticeTitleSpan');
            noticeTitleSpan.innerText = e.title;

            if (e.is_important === 'Y') {
                noticeTitleSpan.innerText ='[중요] ' +  e.title;
                noticeTitleSpan.classList.add('fw-bold');
                noticeTitleSpan.classList.add('new-text-red');
            }

            const noticeReadCountSpan = document.getElementById('noticeReadCountSpan');
            noticeReadCountSpan.innerText = e.read_count;

            const noticeContent = document.getElementById('noticeContent');
            noticeContent.innerText = e.content;

            getPreviousNotice(e.previous_id);
            getNextNotice(e.next_id);
        });
}

// 이전글
function getPreviousNotice(id) {
    const url = `/api/service/getServiceTeamNoticeById?id=${id}`
    fetch(url)
        .then(response => response.json())
        .then((response) => {
            const e = response.data.serviceTeamNotice

            const previousNoticeA = document.getElementById('previousNoticeA');
            previousNoticeA.href = `/serviceTeam/noticeDetailPage?id=${e.id}`;

            const previousNoticeTitle = document.getElementById('previousNoticeTitle');
            previousNoticeTitle.innerText = e.title;

            if (e.is_important === 'Y') {
                previousNoticeTitle.innerText ='[중요] ' +  e.title;
                previousNoticeTitle.classList.add('fw-bold');
                previousNoticeTitle.classList.add('new-text-red');
            }

            const previousNoticeIsImageSpan = document.getElementById('previousNoticeIsImageSpan');
            if (e.url !== '0') {
                previousNoticeIsImageSpan.classList.remove('d-none');
            }

            const previousNoticeName = document.getElementById('previousNoticeName');
            previousNoticeName.innerText = e.name;

            const previousNoticeCreatedAt = document.getElementById('previousNoticeCreatedAt');
            previousNoticeCreatedAt.innerText = dateFormat(e.created_at);
        });
}

// 다음글
function getNextNotice(id) {
    const url = `/api/service/getServiceTeamNoticeById?id=${id}`
    fetch(url)
        .then(response => response.json())
        .then((response) => {
            const e = response.data.serviceTeamNotice

            const nextNoticeA = document.getElementById('nextNoticeA');
            nextNoticeA.href = `/serviceTeam/noticeDetailPage?id=${e.id}`;

            const nextNoticeTitle = document.getElementById('nextNoticeTitle');
            nextNoticeTitle.innerText = e.title;

            if (e.is_important === 'Y') {
                nextNoticeTitle.innerText ='[중요] ' +  e.title;
                nextNoticeTitle.classList.add('fw-bold');
                nextNoticeTitle.classList.add('new-text-red');
            }

            const nextNoticeIsImageSpan = document.getElementById('nextNoticeIsImageSpan');
            if (e.url !== '0') {
                nextNoticeIsImageSpan.classList.remove('d-none');
            }

            const nextNoticeName = document.getElementById('nextNoticeName');
            nextNoticeName.innerText = e.name;

            const nextNoticeCreatedAt = document.getElementById('nextNoticeCreatedAt');
            nextNoticeCreatedAt.innerText = dateFormat(e.created_at);
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

// 글 삭제
function noticeDelete() {
    if(!confirm("해당 공지를 삭제하시겠습니까?")) {
        return;
    }

    const url = `/api/service/deleteNotice?id=${noticeId}`;
    fetch(url)
        .then(response => response.json())
        .then(response => {
            showMessage("삭제가 완료되었습니다.");
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
        window.location = '/serviceTeam/noticePage';
    }, 1000);

}

// 수정 모달 열기
function noticeModalOpen() {
    const serviceTeamNoticeInsertModal = bootstrap.Modal.getOrCreateInstance("#serviceTeamNoticeInsertModal");
    serviceTeamNoticeInsertModal.show();
}

// 수정 모달 닫기
function noticeModalClose() {
    const serviceTeamNoticeInsertModal = bootstrap.Modal.getOrCreateInstance("#serviceTeamNoticeInsertModal");
    serviceTeamNoticeInsertModal.hide();
}

// 수정
function noticeUpdateSubmit() {
    const noticeRegisterForm = document.getElementById("noticeRegisterForm");

    const formData = new FormData(noticeRegisterForm);

    const url = `/api/service/updateNotice?id=${noticeId}`
    fetch(url, {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then((response) => {
            noticeModalClose();
            showMessageUpdate("공지사항 수정이 완료되었습니다.");
            reloadServiceTeamNoticeList();
        });
}

// 알림창2
function showMessageUpdate(text) {
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

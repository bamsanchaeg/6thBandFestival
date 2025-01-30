let serviceTeamMemberId = null;

// 아이디 세팅
function setSessionMemberId() {
    const url = '/api/service/getSessionServiceId';

    fetch(url)
        .then(response => response.json())
        .then((response) => {
            serviceTeamMemberId = response.data.id;
            reloadMyInfo();
        });
}

let e;

// 개인정보
function reloadMyInfo() {
    const url = `/api/service/getMyInformation?id=${serviceTeamMemberId}`;

    fetch(url)
        .then(response => response.json())
        .then((response) => {
            e = response.data.MyAttendance;
            const infoProfileImg = document.getElementById('infoProfileImg');
            infoProfileImg.src = `/images/${e.profile}`

            const infoNameSpan = document.getElementById('infoNameSpan');
            infoNameSpan.innerText = e.name;

            const infoEmailSpan = document.getElementById('infoEmailSpan');
            infoEmailSpan.innerText = e.email;

            const infoBirthSpan = document.getElementById('infoBirthSpan');
            infoBirthSpan.innerText = e.birth.substring(0, 10);

            const infoPhoneSpan = document.getElementById('infoPhoneSpan');
            infoPhoneSpan.innerText = e.phone.substring(0, 3) + '-' + e.phone.substring(3, 7) + '-' + e.phone.substring(7);
        });
}

// 프로필 수정 모달창 열기
function imageChangeModalOpen() {
    const imageChangeModal = bootstrap.Modal.getOrCreateInstance("#imageChangeModal");
    imageChangeModal.show();
}

// 프로필 수정 모달창 닫기
function imageChangeModalClose() {
    const imageChangeModal = bootstrap.Modal.getOrCreateInstance("#imageChangeModal");
    imageChangeModal.hide();
}

// 프로필 수정
function changeImage() {
    const changeImgForm = document.getElementById('changeImgForm');

    const hiddenId = document.getElementById('hiddenId');
    hiddenId.value = e.id;

    const formData = new FormData(changeImgForm);

    const url = '/api/service/updateMyProfile'
    fetch(url, {
        method: 'POST',
        cache: 'no-cache',
        body: formData
    })
        .then(response => response.json())
        .then((response) => {
            imageChangeModalClose();
            showMessage("수정이 완료되었습니다.");
            reloadMyInfo();
        });
}

// 비밀번호 수정 모달창 열기
function passwordChangeModelOpen() {
    const passwordChangeModal = bootstrap.Modal.getOrCreateInstance("#passwordChangeModal");
    passwordChangeModal.show();
}

// 비밀번호 수정 모달창 닫기
function passwordChangeModelClose() {
    const passwordChangeModal = bootstrap.Modal.getOrCreateInstance("#passwordChangeModal");
    passwordChangeModal.hide();
}

// 비밀번호 수정
function changePassword() {
    const changePasswordForm = document.getElementById('changePasswordForm');

    const hiddenPasswordId = document.getElementById('hiddenPasswordId');
    hiddenPasswordId.value = e.id;

    const formData = new FormData(changePasswordForm);

    const url = '/api/service/updateMyPassword'
    fetch(url, {
        method: 'POST',
        cache: 'no-cache',
        body: formData
    })
        .then(response => response.json())
        .then((response) => {
            passwordChangeModelClose();
            showMessage("수정이 완료되었습니다.");
            reloadMyInfo();
        });
}

// 번호 수정 모달창 열기
function phoneChangeModalOpen() {
    const phoneChangeModal = bootstrap.Modal.getOrCreateInstance("#phoneChangeModal");
    phoneChangeModal.show();
}

// 번호 수정 모달창 닫기
function phoneChangeModalClose() {
    const phoneChangeModal = bootstrap.Modal.getOrCreateInstance("#phoneChangeModal");
    phoneChangeModal.hide();
}

// 번호 수정
function changePhone() {
    const changePhoneForm = document.getElementById('changePhoneForm');

    const hiddenPhoneId = document.getElementById('hiddenPhoneId');
    hiddenPhoneId.value = e.id;

    const formData = new FormData(changePhoneForm);

    const url = '/api/service/updateMyPhone'
    fetch(url, {
        method: 'POST',
        cache: 'no-cache',
        body: formData
    })
        .then(response => response.json())
        .then((response) => {
            phoneChangeModalClose();
            showMessage("수정이 완료되었습니다.");
            reloadMyInfo();
        });
}

// 수정완료창
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
    setSessionMemberId();
});
const urlParams = new URL(window.location.href).searchParams;
const roomId = urlParams.get("id");

let chatMyId = null;
let chatMyEmail;

// 아이디 세팅
function setSessionId() {
    const url = "/api/ticket/getSessionUserId";

    fetch(url)
        .then(response => response.json())
        .then((response) => {
            chatMyId = response.data.id;
            chatMyEmail = response.data.email;
            reloadChatMessageUser();
            // 풀링..
            setInterval(reloadChatMessageUser, 5000);
        });
}

// 채팅 메세지 생성
function createLiveChatMessage() {
    const chatMessageForm = document.getElementById('chatMessageForm');
    const formData = new FormData(chatMessageForm);

    const chatMessageInput = document.getElementById('chatMessageInput');
    if (chatMessageInput.value === '') {
        alert('메세지를 입력해주세요.');
        chatMessageInput.focus();
        return;
    }

    const url = '/api/service/createChatMessage'
    fetch(url, {
        method: 'POST',
        body: formData
    })
        .then((response) => response.json())
        .then((data) => {
            console.log("메세지 입력 완료");
            chatMessageInput.value = '';
            reloadChatMessageUser();
        });
}

// 채팅 출력
function reloadChatMessageUser() {
    const url = `/api/service/getChatRoomMessage?id=${roomId}`
    fetch(url)
        .then(response => response.json())
        .then(response => {
            const chatMessageListBox = document.getElementById('chatMessageListBox');
            chatMessageListBox.innerHTML = "";

            const serviceChatMessageListWrapperTemplate = document.querySelector("#serviceChatMessageListTemplate .serviceChatMessageListWrapper");

            for (const e of response.data.message) {
                const serviceChatMessageListWrapper = serviceChatMessageListWrapperTemplate.cloneNode(true);

                const chatMessageSpan = serviceChatMessageListWrapper.querySelector('.chatMessageSpan');
                chatMessageSpan.innerText = e.message;

                const chatSendTimeSpan = serviceChatMessageListWrapper.querySelector('.chatSendTimeSpan');
                chatSendTimeSpan.innerText = chatDateFormat(e.created_at);

                if(e.from_email === chatMyEmail) {
                    chatMessageSpan.classList.add('send-message');
                    chatMessageSpan.classList.add('ms-auto');
                    chatSendTimeSpan.classList.add('ms-auto');
                } else {
                    chatMessageSpan.classList.add('receive-message');
                    chatMessageSpan.classList.add('me-auto');
                    chatSendTimeSpan.classList.add('me-auto');
                }

                chatMessageListBox.appendChild(serviceChatMessageListWrapper);
            }
        });
}

// 메세지 작성 시간
function chatDateFormat(createdAt) {
    const today = new Date();
    const chatDate = new Date(createdAt);

    let day = chatDate.getDate();
    let month = chatDate.getMonth() + 1;
    let year = chatDate.getFullYear().toString().slice(-2);
    let hours = chatDate.getHours();
    let minutes = chatDate.getMinutes();

    const isToday = chatDate.toDateString() == today.toDateString();

    if (isToday) {
        hours = hours < 10 ? '0' + hours : hours;
        minutes = minutes < 10 ? '0' + minutes : minutes;
        return `${hours}:${minutes}`;
    } else {
        day = day < 10 ? '0' + day : day;
        month = month < 10 ? '0' + month : month;
        return `${year}.${month}.${day}  ${hours}:${minutes}`;
    }
}

// 알림창
function showMessage(text) {
    const reviewUpdateDoneModal = bootstrap.Modal.getOrCreateInstance("#reviewUpdateDoneModal");
    reviewUpdateDoneModal.show();

    const testAlert = document.getElementById("testAlert")
    testAlert.innerHTML = text;

    setTimeout(() => {
        reviewUpdateDoneModal.hide();
        // 내 상담내역 페이지로..
        window.location = '';
    }, 1500);

}

// 채팅방 나가기 모달 열기
function chatRoomExitModalOpen() {
    const chatRoomExitModal = bootstrap.Modal.getOrCreateInstance("#chatRoomExitModal");
    chatRoomExitModal.show();
}

// 채팅방 나가기 모달 닫기
function chatRoomExitModalClose() {
    const chatRoomExitModal = bootstrap.Modal.getOrCreateInstance("#chatRoomExitModal");
    chatRoomExitModal.hide();
}

function changeChatRoomActive() {
    const roomIdHidden = document.getElementById('roomIdHidden');
    const roomId = roomIdHidden.value;

    const url = `/api/service/updateRoomIsActive?id=${roomId}`;

    fetch(url)
        .then(response => response.json())
        .then((response) => {
            showMessage('실시간 상담을 종료합니다. <br>이용해주셔서 감사합니다.');
        });
}

window.addEventListener("DOMContentLoaded", () => {
    setSessionId();
});
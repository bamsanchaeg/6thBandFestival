let today;
let serviceId;

let chatMyId = null;
// 아이디 세팅
function setSessionId() {
    const url = "/api/ticket/getSessionUserId";

    fetch(url)
        .then(response => response.json())
        .then((response) => {
            chatMyId = response.data.id;
        });
}

// 오늘 날짜
function getTodayDate() {
    const now = new Date();
    const year = now.getFullYear();
    const month = (now.getMonth() + 1).toString().padStart(2, '0'); // 월은 0부터 시작하므로 +1 필요
    const date = now.getDate().toString().padStart(2, '0');

    today = `${year}-${month}-${date}`;
    getServiceId();
}

// 상담 시작 버튼 보이게
function checkInfo() {
    const chatButton = document.getElementById('chatButton');
    chatButton.classList.toggle('d-none')
}

// 알림
function infoModal() {
    createLiveChatRoom();

    // showMessage('실시간 상담을 시작합니다.');
}

// 알림창
function showMessage(text) {
    const reviewUpdateDoneModal = bootstrap.Modal.getOrCreateInstance("#reviewUpdateDoneModal");
    reviewUpdateDoneModal.show();

    const testAlert = document.getElementById("testAlert")
    testAlert.innerText = text;

    console.log("알림창 띄울때 유저 아이디 : " + chatMyId);
    console.log("알림창 띄울때 서비스 아이디 : " + serviceId);

    // const roomId = await getLiveChatRoomId(chatMyId, serviceId);
    // console.log("알림창 띄울때 채팅창 아이디 : " + roomId);

    // 콜백...콜백어케하는거임~
    getLiveChatRoomId(chatMyId, serviceId, (roomId) => {
        console.log("알림창 띄울때 채팅창 아이디 : " + roomId);
        setTimeout(() => {
            reviewUpdateDoneModal.hide();
            window.location = `/user/chatStartPage?id=${roomId}`;
        }, 1500);
    });
    //
    // setTimeout(() => {
    //     reviewUpdateDoneModal.hide();
    //     window.location = `/user/chatStartPage?id=${roomId}`
    // }, 1500);

}

// 서비스팀 아이디 가져오기 (제일 일 없는 한사람)
function getServiceId() {
    const url = `/api/service/getChatServiceId?today=${today}`;

    fetch(url)
        .then(response => response.json())
        .then((response) => {
            serviceId = response.data.serviceId;
            // console.log("서비스팀 아이디 : " + serviceId);
        });
}

// 채팅방 생성
function createLiveChatRoom() {
    const formData = new FormData();

    formData.append('service_id', serviceId);
    // formData.append('user_id', chatMyId);

    const url = '/api/service/createChatRoom'
    fetch(url, {
        method: 'POST',
        body: formData
    })
        .then((response) => response.json())
        .then((data) => {
            showMessage('실시간 상담을 시작합니다.');
            // console.log("채팅방을 만들었을때 서비스 직원 아이디 : " + serviceId);
        });
}

// 채팅방 아이디 알아내기
function getLiveChatRoomId(uId, sId, callback) {
    const url = `/api/service/getChatRoomId?user_id=${uId}&service_id=${sId}`
    fetch(url)
        .then(response => response.json())
        // .then(response => {
        //     console.log("aaa : " + response);
        // })
        .then((response) => {
            const roomId = response.data.roomId;
            console.log("roomID 찾기 : " + roomId);
            callback(roomId);  // 콜백 함수 호출, roomId를 전달
        });
}

// 채팅방 아이디.. async/await를 사용하여 비동기 처리
// async function getLiveChatRoomId(uId, sId) {
//     const url = `/api/service/getChatRoomId?user_id=${uId}&service_id=${sId}`;
//     const response = await fetch(url);
//     const data = await response.json();
//     const roomId = data.data.roomId;
//     console.log('roomid : ' + roomId);
//     return roomId;
// }

window.addEventListener("DOMContentLoaded", () => {
    setSessionId();
    getTodayDate();
});
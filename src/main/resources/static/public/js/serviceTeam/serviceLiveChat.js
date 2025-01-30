let serviceTeamMemberId = null;
let serviceTeamMemberEmail;

// 아이디 세팅
function setSessionMemberId() {
    const url = '/api/service/getSessionServiceId';

    fetch(url)
        .then(response => response.json())
        .then((response) => {
            serviceTeamMemberId = response.data.id;
            serviceTeamMemberEmail = response.data.email;
            reloadChatRoomList();
        });
}

// 채팅방 목록
function reloadChatRoomList() {
    const url = `/api/service/getChatRoomList?id=${serviceTeamMemberId}`
    fetch(url)
        .then(response => response.json())
        .then(response => {
            const chatRoomListBox = document.getElementById('chatRoomListBox');
            chatRoomListBox.innerHTML = "";

            const serviceChatRoomListWrapperTemplate = document.querySelector("#serviceChatRoomListTemplate .serviceChatRoomListWrapper");

            for (const e of response.data.chatList) {
                const serviceChatRoomListWrapper = serviceChatRoomListWrapperTemplate.cloneNode(true);

                const userProfileImg = serviceChatRoomListWrapper.querySelector('.userProfileImg');
                userProfileImg.src = `/images/${e.user_profile}`;

                const userNameSpan = serviceChatRoomListWrapper.querySelector('.userNameSpan');
                userNameSpan.innerText = e.user_nickname;

                const userNoReadChatCountSpan = serviceChatRoomListWrapper.querySelector('.userNoReadChatCountSpan');
                if (e.chat_count === 0) {
                    userNoReadChatCountSpan.classList.add('d-none');
                } else {
                    userNoReadChatCountSpan.innerText = e.chat_count;
                }

                serviceChatRoomListWrapper.onclick = () => { selectChatRoom(e.id) }
                chatRoomListBox.appendChild(serviceChatRoomListWrapper);
            }
        });
}

let roomId;
// 채팅방 선택 -> 해당 채팅방 정보 출력
function selectChatRoom(id) {
    // console.log(id);

    // 채팅 읽음처리
    updateForIsReading(id);

    const url = `/api/service/getChatRoomById?id=${id}`
    fetch(url)
        .then(response => response.json())
        .then(response => {
            const userProfileImage = document.getElementById('userProfileImage');
            userProfileImage.src = `/images/${response.data.chat.user_profile}`;

            const userName = document.getElementById('userName');
            userName.innerText = response.data.chat.user_nickname;

            const inputMessageDNone = document.getElementById('inputMessageDNone');
            inputMessageDNone.classList.remove('d-none');

            const roomIdSetting = document.getElementById('roomIdSetting');
            roomIdSetting.value = id;

            // 상단 정보 뽑듯이 해야함!! 수정할것
            const chatMessageServiceInput = document.getElementById('chatMessageServiceInput');
            const chatInputButton = document.getElementById('chatInputButton');

            if (response.data.chat.is_active === 'N') {
                chatMessageServiceInput.disabled = true;
                chatMessageServiceInput.placeholder = '종료된 상담입니다.';
                chatInputButton.classList.add('d-none');
            } else {
                chatMessageServiceInput.disabled = false;
                chatMessageServiceInput.placeholder = '메세지를 입력하세요.';
                chatInputButton.classList.remove('d-none');
            }
            roomId = response.data.chat.id;
            reloadChatMessage(roomId);
            // 풀링...
            // reloadChatMessage(roomId);
            console.log('채팅방 아이디1 : ' + roomId);
            setInterval(() => reloadChatMessage(roomId), 5000);
        });
}

// 채팅방 선택 -> 채팅 내역 보이기
function reloadChatMessage(id) {
    // console.log("이메일 : " + serviceTeamMemberEmail);
    const url = `/api/service/getChatRoomMessage?id=${roomId}`
    fetch(url)
        .then(response => response.json())
        .then(response => {
            console.log('아이디 : ' + id);
            console.log('채팅방 아이디2 : ' + roomId);
            const chatMessageListBox = document.getElementById('chatMessageListBox');
            chatMessageListBox.innerHTML = "";

            const serviceChatMessageListWrapperTemplate = document.querySelector("#serviceChatMessageListTemplate .serviceChatMessageListWrapper");

            for (const e of response.data.message) {
                const serviceChatMessageListWrapper = serviceChatMessageListWrapperTemplate.cloneNode(true);

                const chatMessageSpan = serviceChatMessageListWrapper.querySelector('.chatMessageSpan');
                chatMessageSpan.innerText = e.message;

                const chatSendTimeSpan = serviceChatMessageListWrapper.querySelector('.chatSendTimeSpan');
                chatSendTimeSpan.innerText = chatDateFormat(e.created_at);

                if(e.from_email === serviceTeamMemberEmail) {
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

// 채팅 메세지 생성
function createLiveChatMessageService() {
    const chatMessageServiceForm = document.getElementById('chatMessageServiceForm');
    const formData = new FormData(chatMessageServiceForm);

    const chatMessageServiceInput = document.getElementById('chatMessageServiceInput');
    if (chatMessageServiceInput.value === '') {
        alert('메세지를 입력해주세요.');
        chatMessageServiceInput.focus();
        return;
    }

    const roomIdSetting = document.getElementById('roomIdSetting');

    const url = '/api/service/createChatMessageService'
    fetch(url, {
        method: 'POST',
        body: formData
    })
        .then((response) => response.json())
        .then((data) => {
            chatMessageServiceInput.value = '';
            reloadChatMessage(roomIdSetting.value);
            updateForIsReading(roomIdSetting.value);
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
        hours = hours < 10 ? '0' + hours : hours;
        minutes = minutes < 10 ? '0' + minutes : minutes;
        return `${year}.${month}.${day}  ${hours}:${minutes}`;
    }
}

// 메세지 읽음처리
function updateForIsReading(roomId) {
    const url = `/api/service/updateMessageIsReading?id=${roomId}`
    fetch(url)
        .then(response => response.json())
        .then(response => {
            reloadChatRoomList();
        });
}

window.addEventListener("DOMContentLoaded", () => {
    setSessionMemberId();
});
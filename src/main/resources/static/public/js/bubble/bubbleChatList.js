//세션 로그인으로 내가 진행했던 채팅 리스트 가져옴, 여기서 클릭하면 other_user_id를 receiver 정보로 맞춰서 채팅 내역 가져오는걸로 해야할 것 같음
const chatUrl = "/api/bubble/getBubbleUserChatRoomList"

function getChatListBySession(){
    fetch(chatUrl)
        .then(response => response.json())
        .then(response =>{


            const chatListData = response.data.chatRoomList;



            console.log("데이터리스트",chatListData);

            //메세지 반복문 출력
            const messageListBox = document.getElementById("messageListBox") ;
            messageListBox.innerHTML = "";

            const messageWrapperTemplete = document.querySelector("#templete .messageListWrapper");

            // 데이터가 없는 경우
            if (!chatListData || chatListData.length === 0) {
                const noChatMessage = document.createElement('div');
                noChatMessage.innerText = "채팅 내역이 없습니다. 다른 유저와 대화를 시작해보세요.";
                noChatMessage.className = "no-chat-message"; // 스타일을 적용할 수 있도록 클래스 추가
                messageListBox.appendChild(noChatMessage);
                return;
            }


            for(let e of chatListData){

                const newMessageWrapper = messageWrapperTemplete.cloneNode(true);

                // 복제한 요소의 d-none 클래스 제거
                newMessageWrapper.classList.remove('d-none');

                const lastMessage = newMessageWrapper.querySelector(".lastMessage");
                lastMessage.innerText = e.chatRoom.last_message_content;

                const profileImg = newMessageWrapper.querySelector(".profileImg");
                profileImg.src = '/images/' + e.chatRoom.profile_img;


                const userAccountName = newMessageWrapper.querySelector(".userAccountName");
                userAccountName.innerText = e.chatRoom.other_username;

                const userId = newMessageWrapper.querySelector(".userId");
                userId.href = "/bubble/bubbleChatPage?id=" + e.chatRoom.other_user_id;

                // 서버로부터 받은 시간 문자열
                const lastMessageTimeString = e.chatRoom.last_message_time;

                // 문자열을 Date 객체로 변환
                const lastMessageTime = new Date(lastMessageTimeString);
                // 현재 시간
                const now = new Date();
                // 시간 차이 계산(밀리초 단위)
                const diffInMilliseconds = now - lastMessageTime;

                // 밀리초를 시간으로 변환
                const diffInHours = diffInMilliseconds / (1000 * 60 * 60);

                // 소수점 이하를 버리기 위해 Math.floor 사용
                const hoursAgo = Math.floor(diffInHours);

                // 경과 시간 표시 설정
                let timeDisplay;
                if(hoursAgo<24){
                    if (hoursAgo === 0) {
                        timeDisplay = "sent now"; // 0시간 전인 경우
                    }else{
                        timeDisplay = `${hoursAgo}시간 전`;

                    }
                }else{
                    // 24시간 이상인 경우 날짜로 표시
                    const year = lastMessageTime.getFullYear();
                    const month = ('0' + (lastMessageTime.getMonth() + 1)).slice(-2); // 월은 0부터 시작하므로 1을 더해줌
                    const day = ('0' + lastMessageTime.getDate()).slice(-2);
                    timeDisplay = `${year}-${month}-${day}`; // YYYY-MM-DD 형식으로 표시
                }


                console.log(`마지막 메시지는 ${hoursAgo}시간 전에 보냈습니다.`);


                // 서버로부터 받은 시간 문자열 적용
                const lastMessageTimeElement = newMessageWrapper.querySelector(".lastMessageTimeString");
                lastMessageTimeElement.innerText = timeDisplay;

                // 새 메시지 요소 추가
                messageListBox.appendChild(newMessageWrapper);

            }
        })
}





window.addEventListener("DOMContentLoaded",() =>{
    getChatListBySession();
})
const urlParams = new URL(window.location.href).searchParams;
const receiverId = urlParams.get("id");
const getSessionUrl = "/api/bubble/getSessionUserId"
const sentMessageListUrl = "/api/bubble/sentChatListUserToUser?receiver=" + receiverId;

//전역변수로 유저정보 선언
let sessionUser = null;

function getMessageList(){

    fetch(sentMessageListUrl)
        .then(response => response.json())
        .then(response =>{

            const messageList = response.data.messageList
            if(messageList.length === 0){
                console.log("대화 내역이 없습니다.")
                const messageContainer = document.getElementById("messageContainer");
                messageContainer.innerHTML = "<div class='row'><div class='col'>대화를 시작해보세요</div></div>";
            }else{
                console.log("대화내역을 출력합니다.", messageList);
                const e = response.data.messageList

                //메세지 반복문 출력
                const messageListBox = document.getElementById("messageListBox") ;
                messageListBox.innerHTML = "";




                const messageWrapperTemplete = document.querySelector("#templete .messageListWrapper");

                const receiverName = document.getElementById("receiverName");
                receiverName.innerText = response.data.receiverInfo.account_name;

                console.log("받는사람 아이디", receiverName);


                let lastSender = null;
                let lastSenderMessageElement = null;

                // 메시지 최대 20개까지만 유지
                //20개 메세지만 부분출력하려고 만들어둔건데 위에 스크롤 하는것도 같이 해줘야할듯
                // const displayedMessages = e.slice(-20).reverse();

                for(let message of e){

                    // 메시지 래퍼 생성
                    const newMessageWrapper = messageWrapperTemplete.cloneNode(true);

                    const messageContent = newMessageWrapper.querySelector(".messageContent");
                    messageContent.innerText = message.Message.content;
                    console.log("메세지 : ",message.Message.content);

                    // const messageTime = newMessageWrapper.querySelector(".messageTime");
                    // messageTime.innerText = message.Message.created_at;


                    //receiverId가 문자열인 경우, message.Message.receiver는 숫자일 가능성이 커서 늘 false가 되기 때문에 String으로 변환해서 비교해야함...
                    if (String(message.Message.receiver) !== receiverId) {
                        newMessageWrapper.classList.add("align-left");
                        messageContent.classList.add("receiverMessage");
                    }else{
                        newMessageWrapper.classList.add("align-right");
                        messageContent.classList.add("myMessage");
                    }

                    // 보낸 사람과 받은 사람에 따라 위치 및 스타일 조정
                    messageListBox.appendChild(newMessageWrapper);

                }
            }

        })
}

//세션 유저 정보 받아오기
function getSessionInfo(){
    fetch(getSessionUrl)
        .then(response => response.json())
        .then(response =>{

            const e= response.data
            const userInfo = e[0];
            console.log("유저 인포", userInfo);
            if(userInfo === null){
                alert("로그인 후 이용가능합니다.");
                history.back();
            }else{
                // 권한이 있으면 콘텐츠 표시

            }

        })
}

function sendMessage(){
    const sendMessageUrl = "/api/bubble/sendMessage";
    // 사용자가 입력한 메시지 내용을 가져옵니다.
    const messageContent = document.getElementById("messageInput").value;
    // 메시지 내용이 비어있는지 확인합니다.
    if (!messageContent) {
        alert("메시지를 입력해주세요.");
        return;
    }

    // URL 인코딩 형식으로 데이터를 준비합니다.
    const bodyData = `receiver=${encodeURIComponent(receiverId)}&content=${encodeURIComponent(messageContent)}`;


    fetch(sendMessageUrl,{
        method: "post",
        headers:{
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: bodyData
    })
        .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.text();
    })
        .then(data => {
            console.log("Message sent successfully:", data);
            // 메시지 전송 성공 후 입력 필드 비우기
            document.getElementById("messageInput").value = "";
            getMessageList();
        })
        .catch(error => {
            console.error("Error sending message:", error);
        });

}


//5초마다 풀링해주는 메소드
setInterval(getMessageList,5000);//5000밀리초 = 5초



//function 밖에 있어야함.
window.addEventListener("DOMContentLoaded",()=>{
    getMessageList();
    // getSessionInfo(); //이건 나중에

})
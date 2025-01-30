function chairmanMember(){
    const url = "/api/club/memberAndResidentExist?id="+clubId+"&user_id="+myId;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        if(!response.data.residentInfo.residentExist && !response.data.residentInfo.memberExist){
            const scheduleRegistButton = document.getElementById("scheduleRegistButton");
            if (scheduleRegistButton) {
                scheduleRegistButton.addEventListener("click",function(event) {
                    // event.preventDefault();
                    alert("모임 가입하여야 이용 가능합니다. 모임가입을 해주세요.")//ㄹ확인버튼 클릭하면 넘어감~~~~~
                        location.href="/club/detail/home?id="+clubId;
                })
                return;
            }
        }
    })
}

const url='/api/bubble/getMyAlarmsBySessionId';
fetch(url)
.then(response => response.json())
.then(response =>{
    const alarmListBox = document.getElementById("alarmListBox");
    alarmListBox.innerHTML = "";

    const alarmWrapperTemplete = document.querySelector("#templeteAlarm .postListWrapper");
    if (!alarmWrapperTemplete) {
        console.error("Template element not found!");
    }
    console.log("데이터 출력", response.data.alarmList)
    const alarms = response.data.alarmList;

    console.log("알람", alarms);


    // 데이터가 없는 경우
    if (!alarms || alarms.length === 0) {
        const noAlarmMessage = document.createElement('div');
        noAlarmMessage.innerText = "최신 알람 내역이 없습니다.";
        noAlarmMessage.className = "no-alarm-message"; // 스타일을 적용할 수 있도록 클래스 추가
        alarmListBox.appendChild(noAlarmMessage);
        return;
    }

    for(let e of alarms){

        const newAlarmWrapper = alarmWrapperTemplete.cloneNode(true);

        const userId = newAlarmWrapper.querySelector(".userId");
        userId.href = '/bubble/bubbleUserPrivatePage?id=' + e.alarmDto.user_id;

        const userName = newAlarmWrapper.querySelector(".userName");
        userName.innerText = e.alarmDto.username;

        const messageElement = newAlarmWrapper.querySelector(".postContent");
        let message = '';

        if(e.alarmDto.activity_type === "like"){
            // 좋아요에 대한 처리
            message = `${e.alarmDto.username} 가 게시물을 좋아합니다.`;

            // 포스트 링크 설정
            const postLink = newAlarmWrapper.querySelector(".postDetail");
            postLink.href = '/bubble/bubbleDetailPage?id=' + e.alarmDto.post_id;


        }else if(e.alarmDto.activity_type === "follow"){

            // 팔로우에 대한 처리
            message = `${e.alarmDto.username} 당신을 팔로우하기 시작합니다.`;
            // 팔로우는 포스트 링크가 필요 없으므로 설정하지 않음
            newAlarmWrapper.querySelector(".postDetail").remove(); // 불필요한 링크 제거


        }else if(e.alarmDto.activity_type === "comment"){

            message = `${e.alarmDto.username} 가 댓글을 달았습니다.: "${e.alarmDto.content}"`;

            // 포스트 링크 설정
            const postLink = newAlarmWrapper.querySelector(".postDetail");
            postLink.href = '/bubble/bubbleDetailPage?id=' + e.alarmDto.post_id;

        }

        const profile = newAlarmWrapper.querySelector(".profile");
        if (e.alarmDto.profile_img) {
            profile.src = '/images/' + e.alarmDto.profile_img;  // 프로필 이미지로 설정
        } else {
            profile.src = "/public/img/user/user_icon.png";  // 기본 이미지로 설정
        }


        messageElement.innerText = message;
        alarmListBox.appendChild(newAlarmWrapper);


    }
})
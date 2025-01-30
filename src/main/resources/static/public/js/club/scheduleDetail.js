const urlParams = new URL(window.location.href).searchParams;
        const clubId = urlParams.get("club_id");
        const scheduleId = urlParams.get("event_id");

        function setSessionId(){
            const url = "/six/user/getSessionUserId";
            fetch(url)
            .then(response => response.json())
            .then((response)=>{
                myId = response.data.id;
                scheduleMemberExist();
            });
        }

        function clubScheduleDetail(){
            const url = "/api/club/clubSchedulDetail?id="+scheduleId;
            fetch(url)
            .then(response => response.json())
            .then((response)=>{
                const nickname = document.getElementById("nickname");
                nickname.innerText = response.data.clubSchedulDetail.userDto.nickname;

                const imageContainer = document.getElementById("imageContainer");
                const profileImg = document.createElement('img');
                profileImg.src = `/images/${response.data.clubSchedulDetail.userDto.profile_img}`;
                imageContainer.appendChild(profileImg);

                profileImg.style.width="3.5em";
                profileImg.style.height="3.5em";
                profileImg.style.borderRadius="50%";

                const registDate= document.getElementById("registDate");

                const dateee = new Date(response.data.clubSchedulDetail.clubPlanDto.created_at);

                function formatTwo(num) {
                    return num < 10 ? '0' + num : num.toString();
                }
                registDate.innerText= `${dateee.getFullYear().toString()}.${formatTwo(dateee.getMonth()+1)}.${formatTwo(dateee.getDate())}`;

                const clubScheduleTitle = document.getElementById("clubScheduleTitle");
                clubScheduleTitle.innerText=response.data.clubSchedulDetail.clubPlanDto.schedule_title;

                const clubScheduleExplanation = document.getElementById("clubScheduleExplanation");
                clubScheduleExplanation.innerText=response.data.clubSchedulDetail.clubPlanDto.schedule_explanation;

                let eventStart = new Date(response.data.clubSchedulDetail.clubPlanDto.start_date);
                let eventEnd = new Date(response.data.clubSchedulDetail.clubPlanDto.end_date);
                let formattedStart = `${eventStart.getMonth()+1}월 ${eventStart.getDate()}일 ${formatTime(eventStart)}`;

                let formattedEnd = `${eventEnd.getMonth()+1}월 ${eventEnd.getDate()}일 ${formatTime(eventEnd)}`;
                const scheduleDate = document.getElementById("scheduleDate");

                scheduleDate.innerText = `${formattedStart} ~ ${formattedEnd}`;

                const schedulePlace = document.getElementById("schedulePlace");
                schedulePlace.innerText=response.data.clubSchedulDetail.clubPlanDto.schedule_location;

                let today = new Date();
                let year = today.getFullYear();
                let month = ('0'+(today.getMonth()+1)).slice(-2);
                let day = ('0'+(today.getDate())).slice(-2);
                let todayDate = new Date(`${year}-${month}-${day}`);

                const timeRest = eventStart- todayDate;//UTC 시간임.
                const dateRest = Math.ceil(timeRest/(1000 * 3600 * 24));
                
                const scheduleState = document.getElementById("scheduleState");
                const attendSchedule = document.getElementById("attendSchedule");

                if(dateRest >0 ){
                    scheduleState.innerText="모집중";
                    scheduleState.classList.add("text-primary");
                    scheduleState.classList.add("fw-bold");

                }
                else{
                    scheduleState.innerText="종료";
                    scheduleState.classList.add("new-club-text-grey");
                    scheduleState.classList.add("fw-bold");
                    attendSchedule.classList.add("d-none");
                }

                if(response.data.clubSchedulDetail.clubPlanDto.schedule_image != null){
                    const imgContainer = document.getElementById("imgContainer");
                    const img = document.createElement('img');
                    img.src=`/images/${response.data.clubSchedulDetail.clubPlanDto.schedule_image}`;
                    imgContainer.appendChild(img);
                    img.style.height='100%';
                    img.style.width='100%';
                }

                const nowMember = document.getElementById("nowMember");
                nowMember.innerText=response.data.clubSchedulDetail.nowScheduleMember;
                nowMember.classList.add("text-primary");

                const possibleRegistMember = document.getElementById("possibleRegistMember");
                possibleRegistMember.innerText=response.data.clubSchedulDetail.clubPlanDto.max_members;

            })
        }

        function nowMember(){
            const url = "/api/club/clubSchedulDetail?id="+scheduleId;
            fetch(url)
            .then(response => response.json())
            .then((response)=>{
            const nowMember = document.getElementById("nowMember");
            nowMember.innerText=response.data.clubSchedulDetail.nowScheduleMember;
            nowMember.classList.add("text-primary");
            })
        }

        function goToSchedule(){
            window.location.href = "/club/detail/schedule?id="+clubId;

        }

        function clubScheduleRegist(){
            window.location.href = "/club/schedule/chat?club_id="+clubId+"&schedule_id="+scheduleId;
            const myUrl = "/api/club/scheduleMemberExist?schedule_id="+scheduleId+"&club_member_id="+myId;
             fetch(myUrl)
            .then(response => response.json())
            .then((response)=>{
                
                if(!response.data.scheduleMemberExist){
                    const url = "/api/club/scheduleAttend";
                    fetch(url,{
                        method:"post",
                        headers:{
                            'Content-Type': 'application/x-www-form-urlencoded' // URL 인코딩된 데이터를 보냄을 명시
                        },
                        body: `club_id=${clubId}&club_member_id=${myId}&schedule_id=${scheduleId}`
                    })
                    .then(response => response.json())
                    .then((response)=>{
                        const attendSchedule = document.getElementById("attendSchedule")
                        attendSchedule.textContent = "대화중인 채팅방";
                        nowMember();
                        return;
                    })
                }
            })            
            
        }

        function scheduleMemberExist(){
            const url = "/api/club/scheduleMemberExist?schedule_id="+scheduleId+"&club_member_id="+myId;
             fetch(url)
            .then(response => response.json())
            .then((response)=>{

                if(response.data.scheduleMemberExist){
                    const attendSchedule = document.getElementById("attendSchedule")
                    attendSchedule.textContent = "대화중인 채팅방";
                }
            })
        }

        function formatTime(date){
            let hours = date.getHours();
            let minutes = date.getMinutes().toString().padStart(2,'0');
            let period = hours >= 12 ? '오후' : '오전';
            hours = hours % 12 || 12;

            return `${period} ${hours}시 ${minutes}분`;

        }

        window.addEventListener("DOMContentLoaded", () => {
            setSessionId();
            clubScheduleDetail();
            weedoGyungdo();
        })
let serviceTeamMemberId = null;

// 아이디 세팅
function setSessionMemberId() {
    const url = '/api/service/getSessionServiceId';

    fetch(url)
        .then(response => response.json())
        .then((response) => {
            serviceTeamMemberId = response.data.id;
            buttonCheckAndChange();
            answerStatus();
            answerStatusCanvas();
            liveChatStatus();
            liveChatStatusCanvas();
        });
}

// 로그인 체크
function memberLoginCheck () {
    if (serviceTeamMemberId == null) {
        if (confirm('로그인이 필요한 서비스입니다. 로그인 페이지로 이동하시겠습니까?')) {
            location.href = '/serviceTeam';
        }
        return false;
    }
    return true;
}

// 현재 날짜와 시간.. ㅁ월 ㅁ일 00시 00분
function getCurrentDateTime() {
    const now = new Date();
    const year = now.getFullYear();
    const month = now.getMonth() + 1; // 월은 0부터 시작하므로 +1 필요
    const date = now.getDate();
    const hours = now.getHours();
    const minutes = now.getMinutes();

    // 두 자리 수로 포맷
    const formattedMonth = month.toString().padStart(2, '0');
    const formattedDate = date.toString().padStart(2, '0');
    const formattedHours = hours.toString().padStart(2, '0');
    const formattedMinutes = minutes.toString().padStart(2, '0');

    return `${formattedMonth}월 ${formattedDate}일 ${formattedHours}시 ${formattedMinutes}분`;
}

let today;
// 오늘 날짜
function getTodayDate() {
    const now = new Date();
    const year = now.getFullYear();
    const month = (now.getMonth() + 1).toString().padStart(2, '0'); // 월은 0부터 시작하므로 +1 필요
    const date = now.getDate().toString().padStart(2, '0');

    today = `${year}-${month}-${date}`;

    reloadScheduleMember();
}

let end_time;
// 오늘 기록이 있는지? 버튼 분기
function buttonCheckAndChange() {
    const url = `/api/service/getTodayAttendance?service_id=${serviceTeamMemberId}&today=${today}`
    fetch(url)
        .then(response => response.json())
        .then(response => {
            if (response.data.todayAttendance === true) {
                document.getElementById('attendanceButton').classList.remove('d-none');
            } else {
                if (response.data.todayBreak === true) {
                    document.getElementById('workingButton').classList.remove('d-none');
                } else {
                    if (response.data.breakInfo.is_done === 'N') {
                        document.getElementById('breakTimeButton').classList.remove('d-none');
                        end_time = response.data.breakInfo.end_time;
                    } else {
                        if (response.data.todayGoHome === false){
                            document.getElementById('finishButton').classList.remove('d-none');
                        } else {
                            document.getElementById('workingNoBreakTimeButton').classList.remove('d-none');
                        }

                    }
                }
            }
        });
}

// 출근
function memberAttendance() {
    if (!memberLoginCheck()) {
        // 로그인 체크가 실패하면 출근 체크를 하지 않음
        return;
    }

    const currentDateTime = getCurrentDateTime();

    if(!confirm(`${currentDateTime}. 출근을 완료하시겠습니까?`)) {
        return;
    }

    const url = '/api/service/createServiceTeamAttendance';
    const attendance = '출근';
    fetch(url, {
        method: "post",
        headers: {
            "Content-Type" : "application/x-www-form-urlencoded"
        },
        body: `service_id=${serviceTeamMemberId}&status=${attendance}`
    })
        .then(response => response.json())
        .then(response => {
            alert('출근이 완료되었습니다. 오늘도 좋은 하루 되세요!');
            document.getElementById('attendanceButton').classList.add('d-none');
            buttonCheckAndChange();
            reloadScheduleMember();
        });
}

// 휴게시간
function memberBreakTime() {
    if (!memberLoginCheck()) {
        // 로그인 체크가 실패하면 출근 체크를 하지 않음
        return;
    }

    if(!confirm(`휴게시간을 시작하시겠습니까?`)) {
        return;
    }

    const endTime = new Date(Date.now() + 60 * 60 * 1000).toISOString(); // 1시간 후

    console.log(endTime);
    const url = '/api/service/createServiceTeamBreakTime';
    fetch(url, {
        method: "post",
        headers: {
            "Content-Type" : "application/x-www-form-urlencoded"
        },
        body: `service_id=${serviceTeamMemberId}`
    })
        .then(response => response.json())
        .then(response => {
            alert('휴게시간이 시작되었습니다. 편안한 휴게시간 보내세요.');
            document.getElementById('workingButton').classList.add('d-none');
            buttonCheckAndChange();
            reloadScheduleMember();
        });
}

// 남은 시간 확인
function memberBreakTimeCheck() {
    const endTimeDate = new Date(end_time);
    const now = new Date();
    const remainingTime = endTimeDate - now;

    // 이 조건이면 내가 계속 눌러서 확인해야함.. 시간을 계산해서 알아서 띄우는 방법이 있는가..
    if (remainingTime <= 0) {
        memberBreakTimeOut();
        return;
    }

    const minutes = Math.floor(remainingTime / 60000);
    const seconds = Math.floor((remainingTime % 60000) / 1000);

    alert(`남은 휴게시간 : ${minutes}분 ${seconds}초`);
}

// 휴게 종료
function memberBreakTimeOut() {
    const url = '/api/service/updateBreakTimeDone';
    fetch(url, {
        method: "post",
        headers: {
            "Content-Type" : "application/x-www-form-urlencoded"
        },
        body: `service_id=${serviceTeamMemberId}&today=${today}`
    })
        .then(response => response.json())
        .then(response => {
            alert('휴게시간이 종료되었습니다. 업무에 복귀해 주세요.');
            document.getElementById('breakTimeButton').classList.add('d-none');
            buttonCheckAndChange();
            reloadScheduleMember();
        });
}

// 퇴근
function memberGoHome() {
    if (!memberLoginCheck()) {
        // 로그인 체크가 실패하면 출근 체크를 하지 않음
        return;
    }

    const currentDateTime = getCurrentDateTime();

    if(!confirm(`${currentDateTime}. 퇴근 하시겠습니까?`)) {
        return;
    }

    const url = '/api/service/createServiceTeamAttendance';
    const attendance = '퇴근';
    fetch(url, {
        method: "post",
        headers: {
            "Content-Type" : "application/x-www-form-urlencoded"
        },
        body: `service_id=${serviceTeamMemberId}&status=${attendance}`
    })
        .then(response => response.json())
        .then(response => {
            alert('퇴근이 완료되었습니다. 오늘도 수고하셨습니다.');
            document.getElementById('workingNoBreakTimeButton').classList.add('d-none');
            buttonCheckAndChange();
            reloadScheduleMember();
        });
}

// 출근하는 멤버 리스트
function reloadScheduleMember() {
    const url = `/api/service/getScheduleMemberList?today=${today}`;

    fetch(url)
        .then(response => response.json())
        .then((response) => {
            const serviceTeamListBox = document.getElementById("serviceTeamListBox");
            serviceTeamListBox.innerHTML = "";

            const serviceTeamListWrapperTemplate = document.querySelector("#serviceTeamListTemplate .serviceTeamListWrapper");

            for (const e of response.data.todayScheduleMember) {
                const serviceTeamListWrapper = serviceTeamListWrapperTemplate.cloneNode(true);

                const stProfileImg = serviceTeamListWrapper.querySelector(".stProfileImg");
                stProfileImg.src = `/images/${e.profile}`;

                const stNameSpan = serviceTeamListWrapper.querySelector(".stNameSpan");
                stNameSpan.innerText = e.name;

                const stEmailSpan = serviceTeamListWrapper.querySelector(".stEmailSpan");
                stEmailSpan.innerText = e.email;

                const stPositionSpan = serviceTeamListWrapper.querySelector(".stPositionSpan");
                if (e.position === 'Leader') {
                    stPositionSpan.innerText = '팀장';
                } else {
                    stPositionSpan.innerText = '팀원';
                }

                const stWorkSpan = serviceTeamListWrapper.querySelector(".stWorkSpan");
                const stGoHomeSpan = serviceTeamListWrapper.querySelector(".stGoHomeSpan");
                const stBreakTimeSpan = serviceTeamListWrapper.querySelector(".stBreakTimeSpan");
                const stNotInSpan = serviceTeamListWrapper.querySelector(".stNotInSpan");
                const stClosedSpan = serviceTeamListWrapper.querySelector(".stClosedSpan");
                if (String(e.status) !== '0') {
                    if (e.status === '출근') {
                        if (e.is_done === 'N') {
                            stBreakTimeSpan.classList.remove('d-none')
                        } else {
                            stWorkSpan.classList.remove('d-none');
                        }
                    } else if (e.status === '퇴근') {
                        stGoHomeSpan.classList.remove('d-none');
                    }
                } else if (String(e.status) === '0') {
                    // 이거 왜 안되는지... e.status 가 문자열이 아니므로 무조건 false
                    stNotInSpan.classList.remove('d-none');
                }

                serviceTeamListBox.appendChild(serviceTeamListWrapper);
            }
        });
}

// 공지사항 리스트
function reloadServiceNotice() {
    const url = `/api/service/getFiveServiceTeamNotice`;

    fetch(url)
        .then(response => response.json())
        .then((response) => {
            const serviceTeamNoticeListBox = document.getElementById("serviceTeamNoticeListBox");
            serviceTeamNoticeListBox.innerHTML = "";

            const serviceTeamNoticeListWrapperTemplate = document.querySelector("#serviceTeamNoticeListTemplate .serviceTeamNoticeListWrapper");

            for (const e of response.data.serviceTeamNotice) {
                const serviceTeamNoticeListWrapper = serviceTeamNoticeListWrapperTemplate.cloneNode(true);

                const stIdSpan = serviceTeamNoticeListWrapper.querySelector(".stIdSpan");
                stIdSpan.innerText = e.id;

                const stDetailPageA = serviceTeamNoticeListWrapper.querySelector(".stDetailPageA");
                stDetailPageA.href = `/serviceTeam/noticeDetailPage?id=${e.id}`;

                const stTitleSpan = serviceTeamNoticeListWrapper.querySelector(".stTitleSpan");
                stTitleSpan.innerText = e.title;

                if (e.is_important === 'Y') {
                    stTitleSpan.innerText ='[중요] ' +  e.title;
                    stTitleSpan.classList.add('fw-bold');
                    stTitleSpan.classList.add('new-text-red');
                }

                const stIsImageSpan = serviceTeamNoticeListWrapper.querySelector(".stIsImageSpan");
                if (e.url !== '0') {
                    stIsImageSpan.classList.remove('d-none');
                }

                const stNameSpan = serviceTeamNoticeListWrapper.querySelector(".stNameSpan");
                stNameSpan.innerText = e.name;

                const stCreatedAtSpan = serviceTeamNoticeListWrapper.querySelector(".stCreatedAtSpan");
                stCreatedAtSpan.innerText = stDateFormat(e.created_at);

                // const stPositionSpan = serviceTeamNoticeListWrapper.querySelector(".stPositionSpan");
                // if (e.position === 'Leader') {
                //     stPositionSpan.innerText = '팀장';
                // } else {
                //     stPositionSpan.innerText = '팀원';
                // }

                // const stMemberInfoA = serviceTeamNoticeListWrapper.querySelector(".stMemberInfoA");
                // stMemberInfoA.onclick = () => {memberOffcanvas(e.id)}


                serviceTeamNoticeListBox.appendChild(serviceTeamNoticeListWrapper);
            }
        });
}

// 포맷
function stDateFormat(createdAt) {
    const stDate = new Date(createdAt);

    let day = stDate.getDate();
    let month = stDate.getMonth() + 1;
    let year = stDate.getFullYear().toString().slice(-2);

    day = day < 10 ? '0' + day : day;
    month = month < 10 ? '0' + month : month;
    return `${year}.${month}.${day}`;
}

// qna 상황
function answerStatus() {
    const url = `/api/service/getServiceQnAStatus?id=${serviceTeamMemberId}`;
    fetch(url)
        .then(response => response.json())
        .then(response => {
            const qnaAcceptingSpan = document.getElementById('qnaAcceptingSpan');
            qnaAcceptingSpan.innerText = response.data.qnaAccepting;

            const qnaApplicationCompletedSpan = document.getElementById('qnaApplicationCompletedSpan');
            qnaApplicationCompletedSpan.innerText = response.data.qnaApplicationCompleted;

            const qnaAnswerCompletedSpan = document.getElementById('qnaAnswerCompletedSpan');
            qnaAnswerCompletedSpan.innerText = response.data.qnaAnswerCompleted;
        })
}

// qna 그래프
function answerStatusCanvas() {
    const url = `/api/service/getServiceQnACanvas?id=${serviceTeamMemberId}`;
    fetch(url)
        .then(response => response.json())
        .then(response => {

            const labels = response.data.qnaCanvas.map(item => item.created_at);
            const totalSales = response.data.qnaCanvas.map(item => item.answer_count);

            const ctx = document.getElementById('dailyAnswerCompletedCanvas').getContext('2d');
            // ctx.width = ctx.clientWidth;
            // ctx.height = ctx.clientHeight;
            const dailyAnswerCompletedCanvas = new Chart(ctx, {
                type: 'line', // 차트 종류
                data: {
                    labels: labels,
                    datasets: [{
                        label: '답변 완료',
                        data: totalSales,
                        fill: false,
                        borderColor: 'rgb(0,0,0)',
                        tension: 0.1
                    }]
                },
                options: {
                    scales: {
                        x:  {
                            type: 'category',
                            title: {
                                display: true,
                                text: 'Day'
                            }
                        },
                    }
                }
            });
        })
}

// 실시간 상담 상황
function liveChatStatus() {
    const url = `/api/service/getServiceLiveChatStatus?id=${serviceTeamMemberId}`;
    fetch(url)
        .then(response => response.json())
        .then(response => {
            const liveChatIngSpan = document.getElementById('liveChatIngSpan');
            liveChatIngSpan.innerText = response.data.active;

            const liveChatDoneSpan = document.getElementById('liveChatDoneSpan');
            liveChatDoneSpan.innerText = response.data.notActive;

        })
}

// 실시간 상담 그래프
function liveChatStatusCanvas() {
    const url = `/api/service/getServiceLiveChatCanvas?id=${serviceTeamMemberId}`;
    fetch(url)
        .then(response => response.json())
        .then(response => {

            const labels = response.data.liveChat.map(item => item.created_at);
            const totalChat = response.data.liveChat.map(item => item.chat_count);

            const ctx = document.getElementById('dailyChatCompletedCanvas').getContext('2d');
            // ctx.width = ctx.clientWidth;
            // ctx.height = ctx.clientHeight;
            const dailyChatCompletedCanvas = new Chart(ctx, {
                type: 'line', // 차트 종류
                data: {
                    labels: labels,
                    datasets: [{
                        label: '채팅 완료',
                        data: totalChat,
                        fill: false,
                        borderColor: 'rgb(0,0,0)',
                        tension: 0.1
                    }]
                },
                options: {
                    scales: {
                        x:  {
                            type: 'category',
                            title: {
                                display: true,
                                text: 'Day'
                            }
                        },
                    }
                }
            });
        })
}

window.addEventListener("DOMContentLoaded", () => {
    setSessionMemberId();
    getTodayDate();
    reloadServiceNotice();
});
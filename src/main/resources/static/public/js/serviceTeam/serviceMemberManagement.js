// 계정 등록 모달
function accountModalOpen() {
    const serviceTeamMemberInsertModal = bootstrap.Modal.getOrCreateInstance("#serviceTeamMemberInsertModal");
    serviceTeamMemberInsertModal.show();
}

function accountModalClose() {
    const serviceTeamMemberInsertModal = bootstrap.Modal.getOrCreateInstance("#serviceTeamMemberInsertModal");
    serviceTeamMemberInsertModal.hide();
}


let today;
// 오늘 날짜
function getTodayDate() {
    const now = new Date();
    const year = now.getFullYear();
    const month = (now.getMonth() + 1).toString().padStart(2, '0'); // 월은 0부터 시작하므로 +1 필요
    const date = now.getDate().toString().padStart(2, '0');

    today = `${year}-${month}-${date}`;
}
// 서비스팀 목록
function reloadServiceTeamList() {
    const url = `/api/service/getServiceTeamList?today=${today}`
    fetch(url)
        .then(response => response.json())
        .then((response) => {
            const serviceTeamListBox = document.getElementById("serviceTeamListBox");
            serviceTeamListBox.innerHTML = "";

            const serviceTeamListWrapperTemplate = document.querySelector("#serviceTeamListTemplate .serviceTeamListWrapper");

            for (const e of response.data.serviceTeamList) {
                const serviceTeamListWrapper = serviceTeamListWrapperTemplate.cloneNode(true);

                const stIdSpan = serviceTeamListWrapper.querySelector(".stIdSpan");
                stIdSpan.innerText = e.id;

                const stNameSpan = serviceTeamListWrapper.querySelector(".stNameSpan");
                stNameSpan.innerText = e.name;

                const stEmailSpan = serviceTeamListWrapper.querySelector(".stEmailSpan");
                stEmailSpan.innerText = e.email;

                const stPhoneNumberSpan = serviceTeamListWrapper.querySelector(".stPhoneNumberSpan");
                stPhoneNumberSpan.innerText = e.phone.substring(0, 3) + '-' + e.phone.substring(3, 7) + '-' + e.phone.substring(7);

                const stBirthSpan = serviceTeamListWrapper.querySelector(".stBirthSpan");
                stBirthSpan.innerText = e.birth;

                const stCreatedAtSpan = serviceTeamListWrapper.querySelector(".stCreatedAtSpan");
                stCreatedAtSpan.innerText = stDateFormat(e.created_at);

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
                // const stTardySpan = serviceTeamListWrapper.querySelector(".stTardySpan");
                // const stNightShiftSpan = serviceTeamListWrapper.querySelector(".stNightShiftSpan");
                if(e.is_working == '근무') {
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
                } else if(e.is_working == '휴무') {
                    stClosedSpan.classList.remove('d-none');
                }


                const stMemberInfoA = serviceTeamListWrapper.querySelector(".stMemberInfoA");
                stMemberInfoA.onclick = () => {memberOffcanvas(e.id)}

                const stRegisterWorkA = serviceTeamListWrapper.querySelector(".stRegisterWorkA");
                stRegisterWorkA.onclick = () => {workModalOpen(e.id, e.name)}


                serviceTeamListBox.appendChild(serviceTeamListWrapper);
            }
        });
}

// 멤버 등록 + 유효성
function memberSubmit() {
    const memberRegisterForm = document.getElementById("memberRegisterForm");

    const inputEmail = document.getElementById("inputEmail");
    if (inputEmail.value === '') {
        alert("이메일을 입력해 주세요.");
        inputEmail.focus();
        return;
    }

    const inputPassword = document.getElementById("inputPassword");
    if (inputPassword.value === '') {
        alert("비밀번호를 입력해 주세요.");
        inputPassword.focus();
        return;
    }

    const inputName = document.getElementById("inputName");
    if (inputName.value === '') {
        alert("이름을 입력해 주세요.");
        inputName.focus();
        return;
    }

    const inputBirth = document.getElementById("inputBirth");
    if (inputBirth.value === '') {
        alert("생년월일을 입력해 주세요.");
        inputBirth.focus();
        return;
    }

    const inputPhone = document.getElementById("inputPhone");
    if (inputPhone.value === '') {
        alert("연락처를 입력해 주세요.");
        inputPhone.focus();
        return;
    }

    if (inputPhone.value.length !== 11) {
        alert("연락처는 010부터 입력해 주세요.");
        inputPhone.focus();
        return;
    }

    const formData = new FormData(memberRegisterForm);

    const url = '/api/service/createServiceTeamMember'
    fetch(url, {
        method: 'POST',
        // cache: 'no-cache',
        body: formData
    })
        .then(response => response.json())
        .then((response) => {
            accountModalClose();
            showMessage("계정 생성이 완료되었습니다.");
            reloadServiceTeamList();
        });

}

// 멤버 상세 오프캔버스
function memberOffcanvas(memberId) {
    const url = `/api/service/getServiceTeamById?service_id=${memberId}&today=${today}`

    fetch(url)
        .then(response => response.json())
        .then(response => {
            const memberProfile = document.getElementById('memberProfile');
            memberProfile.src = `/images/${response.data.memberInfo.profile}`

            const memberNameSpan = document.getElementById('memberNameSpan');
            memberNameSpan.innerText = response.data.memberInfo.name;

            const memberPositionSpan = document.getElementById('memberPositionSpan');
            memberPositionSpan.innerText = response.data.memberInfo.position;

            const memberPhoneSpan = document.getElementById('memberPhoneSpan');
            memberPhoneSpan.innerText = response.data.memberInfo.phone.replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3');

            const memberEmailSpan = document.getElementById('memberEmailSpan');
            memberEmailSpan.innerText = response.data.memberInfo.email;

            const memberScheduleSpan = document.getElementById('memberScheduleSpan');
            if (response.data.memberInfo.select_date == '0') {
                memberScheduleSpan.innerText = '주간 스케줄이 없습니다.';
            } else {
                memberScheduleSpan.innerText = response.data.memberInfo.select_date + ' ~ ' + response.data.memberInfo.end_date;
            }


            const memberWorkingTimeSpan = document.getElementById('memberWorkingTimeSpan');
            if (response.data.memberInfo.working_time == '0') {
                memberWorkingTimeSpan.innerText = '주간 스케줄을 등록해주세요.';
            } else {
                memberWorkingTimeSpan.innerText = response.data.memberInfo.working_time.substring(0, 5);
            }

            const memberQuittingTimeSpan = document.getElementById('memberQuittingTimeSpan');
            if (response.data.memberInfo.quitting_time == '0') {
                memberQuittingTimeSpan.innerText = '주간 스케줄을 등록해주세요.';
            } else {
                memberQuittingTimeSpan.innerText = response.data.memberInfo.quitting_time.substring(0, 5);
            }


        })
    const memberInfoOffcanvas = bootstrap.Offcanvas.getOrCreateInstance("#memberInfoOffcanvas");
    memberInfoOffcanvas.show();
}

// 날짜 포맷
function stDateFormat(createdAt) {
    const stDate = new Date(createdAt);

    let day = stDate.getDate();
    let month = stDate.getMonth() + 1;
    let year = stDate.getFullYear().toString().slice(-2);

    day = day < 10 ? '0' + day : day;
    month = month < 10 ? '0' + month : month;
    return `${year}.${month}.${day}`;
}

// 근무 등록 modal 오픈
function workModalOpen(memberId, name) {
    const memberWorkNameSpan = document.getElementById('memberWorkNameSpan');
    memberWorkNameSpan.innerText = name;

    const memberWorkIdSpan = document.getElementById('memberWorkIdSpan');
    memberWorkIdSpan.value = memberId;

    const inputStartDate = document.getElementById('inputStartDate');
    inputStartDate.value = '';

    const inputEndDate = document.getElementById('inputEndDate');
    inputEndDate.value = '';

    const inputWorkingTime = document.getElementById('inputWorkingTime');
    inputWorkingTime.value = '';

    const inputQuittingTime = document.getElementById('inputQuittingTime');
    inputQuittingTime.value = '';

    const workInsertModal = bootstrap.Modal.getOrCreateInstance("#workInsertModal");
    workInsertModal.show();
}

// 근무 등록 modal 닫기
function workModalClose() {
    const workInsertModal = bootstrap.Modal.getOrCreateInstance("#workInsertModal");
    workInsertModal.hide();
}

// 근무 등록
function memberWorkSubmit() {
    const memberWorkRegisterForm = document.getElementById("memberWorkRegisterForm");

    const inputStartDate = document.getElementById("inputStartDate");
    if (inputStartDate.value === '') {
        alert("지정일을 입력해 주세요.");
        inputStartDate.focus();
        return;
    }

    const inputWorkingTime = document.getElementById("inputWorkingTime");
    if (inputWorkingTime.value === '') {
        alert("출근시간을 입력해 주세요.");
        inputWorkingTime.focus();
        return;
    }

    const inputQuittingTime = document.getElementById("inputQuittingTime");
    if (inputQuittingTime.value === '') {
        alert("퇴근시간을 입력해 주세요.");
        inputQuittingTime.focus();
        return;
    }

    const inputEndDate = document.getElementById('inputEndDate');

    // console.log("aaa : " + inputEndDate.value);
    const formData = new FormData(memberWorkRegisterForm);

    // 그냥 formData에 안들어가서 강제로 넣어야함..왜지
    formData.append('end_date', inputEndDate.value);

    const url = '/api/service/createMemberWork'
    fetch(url, {
        method: 'POST',
        cache: 'no-cache',
        body: formData
    })
        .then(response => response.json())
        .then((response) => {
            workModalClose();
            showMessage("일정 등록이 완료되었습니다.");
            reloadServiceTeamList();
        });
}

// 지정일부터 +5일..
document.getElementById('inputStartDate').addEventListener('change', function() {
    const startDate = new Date(this.value);
    const endDate = new Date(startDate);
    endDate.setDate(startDate.getDate() + 4); // 5일 더하기

    const endDateString = formatDate(endDate);
    document.getElementById('inputEndDate').value = endDateString;
});

function formatDate(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 월을 2자리로
    const day = String(date.getDate()).padStart(2, '0'); // 일을 2자리로
    return `${year}-${month}-${day}`;
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
    getTodayDate();
    reloadServiceTeamList();
});

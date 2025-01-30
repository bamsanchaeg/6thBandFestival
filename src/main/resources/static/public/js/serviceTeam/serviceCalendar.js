let serviceTeamMemberId = null;

// 아이디 세팅
function setSessionMemberId() {
    const url = '/api/service/getSessionServiceId';

    fetch(url)
        .then(response => response.json())
        .then((response) => {
            serviceTeamMemberId = response.data.id;
            calendar();
        });
}

// 풀캘린더 api
function calendar() {
    const calendarEl = document.getElementById('calendar');

    const url = `/api/service/getMyAttendance?service_id=${serviceTeamMemberId}`
    fetch(url)
        .then(response => response.json())
        .then(response => {
            // 데이터를 담을 빈 배열 생성
            const events = [];

            for (const e of response.data.MyAttendance) {
                events.push({
                    // 캘린더 형식에 맞게 넣어야함!! (title, start, end..)
                    title: formatDateTime(e.created_at) + '  ' + e.status,
                    start: e.created_at
                });
            }

            for (const e of response.data.MySchedule) {
                events.push({
                    title: '주간 근무 스케줄',
                    start: e.select_date,
                    end: endDatePlus(e.end_date)
                });
            }

            // 만들어둔 events 배열로 캘린더 생성
            var calendar = new FullCalendar.Calendar(calendarEl, {
                initialView: 'dayGridMonth',  // 달력을 월 단위로 표시
                height: '83vh', // 캘린더가 부모 요소의 높이에 맞도록 설정
                contentHeight: '100vh', // 콘텐츠 높이 자동 조정
                expandRows: true, // 모든 행의 높이를 동일하게 맞춤
                events: events,
                displayEventTime: false,  // 시간 표시
                locale: "ko" // 달력 한글로
            });

            calendar.render(); // 달력 렌더링
        });
}

function formatDateTime(dateTimeStr) {
    const date = new Date(dateTimeStr);

    let hours = date.getHours();
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const period = hours >= 12 ? '오후' : '오전';

    // 12시간제로 변환
    hours = hours % 12;
    hours = hours ? hours : 12; // 0시를 12시로 변환
    const formattedTime = `${period} ${hours}시 ${minutes}분`;

    return formattedTime;
}

function endDatePlus(formatDay) {
    const myDate = new Date(formatDay);

    let day = myDate.getDate();
    let month = myDate.getMonth() + 1;
    let year = myDate.getFullYear().toString();

    // 두 자릿수로 맞추기 위해 day와 month가 10보다 작을 경우 앞에 0을 추가
    day = day < 10 ? '0' + day : day;
    month = month < 10 ? '0' + month : month;

    const hours = '23';
    const minutes = '59';
    const seconds = '59';

    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

window.addEventListener("DOMContentLoaded", () => {
    setSessionMemberId();
});
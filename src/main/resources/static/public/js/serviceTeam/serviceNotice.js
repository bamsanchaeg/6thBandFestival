let currentPage = 1; // 현재 페이지로 사용 할 변수
const itemsPerPage = 15; // 한 화면에 뽑고 싶은 리스트 수
const maxPageButtons = 5; // 한 세트에 출력할 최대 페이지 수

// 공지사항 등록 모달 열기
function noticeModalOpen() {
    const inputTitle = document.getElementById("inputTitle");
    inputTitle.value = '';

    const inputContent = document.getElementById("inputContent");
    inputContent.value = '';

    const serviceTeamNoticeInsertModal = bootstrap.Modal.getOrCreateInstance("#serviceTeamNoticeInsertModal");
    serviceTeamNoticeInsertModal.show();
}

// 공지사항 등록 모달 닫기
function noticeModalClose() {
    const serviceTeamNoticeInsertModal = bootstrap.Modal.getOrCreateInstance("#serviceTeamNoticeInsertModal");
    serviceTeamNoticeInsertModal.hide();
}

// 서비스팀 공지 목록
function reloadServiceTeamNoticeList(page = 1) {
    currentPage = page;
    const url = `/api/service/getAllServiceTeamNotice?page=${page}`
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

                const stReadCountSpan = serviceTeamNoticeListWrapper.querySelector(".stReadCountSpan");
                stReadCountSpan.innerText = e.read_count;

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
            setupPagination(response.data.noticeCount, itemsPerPage, page);
        });
}

// 공지 등록 + 유효성
function noticeSubmit() {
    const noticeRegisterForm = document.getElementById("noticeRegisterForm");

    const inputTitle = document.getElementById("inputTitle");
    if (inputTitle.value === '') {
        alert("제목을 입력해 주세요.");
        inputTitle.focus();
        return;
    }

    const inputContent = document.getElementById("inputContent");
    if (inputContent.value === '') {
        alert("내용을 입력해 주세요.");
        inputContent.focus();
        return;
    }

    const formData = new FormData(noticeRegisterForm);

    // 파일 첨부 확인 : 첨부하지 않은 경우 null 처리
    if(!formData.get("inputImages") || formData.get("inputImages").name === "") {
        formData.set("inputImages", null);
    }

    const url = '/api/service/createServiceNotice'
    fetch(url, {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then((response) => {
            noticeModalClose();
            showMessage("공지사항 등록이 완료되었습니다.");
            reloadServiceTeamNoticeList();
        });

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

// 페이지네이션
function setupPagination(totalItems, itemsPerPage, currentPage) {
    const pageCount = Math.ceil(totalItems / itemsPerPage);
    const backPageButton = document.getElementById("backPageButton");
    const nextPageButton = document.getElementById("nextPageButton");

    document.querySelectorAll('.pageLi').forEach(li => li.remove());

    backPageButton.classList.toggle('disabled', currentPage === 1);
    backPageButton.onclick = (e) => {
        //  페이지가 맨 위로 스크롤되는 현상을 방지
        // e.preventDefault();
        if (currentPage > 1) {
            // Math.max 주어진 인자 중 가장 큰 값을 반환
            reloadServiceTeamNoticeList(Math.max(1, currentPage - maxPageButtons));
        }
    };

    nextPageButton.classList.toggle('disabled', currentPage === pageCount);
    nextPageButton.onclick = (e) => {
        // e.preventDefault();
        if (currentPage < pageCount) {
            reloadServiceTeamNoticeList(Math.min(pageCount, currentPage + maxPageButtons));
        }
    };


    for (let i = 1; i <= pageCount; i++) {
        const li = document.createElement('li');
        li.classList.add('page-item', 'pageLi');
        if (i === currentPage) {
            li.classList.add('active');
        }
        const a = document.createElement('a');
        a.classList.add('page-link');
        a.classList.add('new-text-black');
        a.innerText = i;
        a.onclick = (e) => {
            // e.preventDefault();
            reloadServiceTeamNoticeList(i);
        };
        li.appendChild(a);
        nextPageButton.parentElement.before(li);
    }
}

window.addEventListener("DOMContentLoaded", () => {
    reloadServiceTeamNoticeList(currentPage);
});

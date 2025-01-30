let serviceTeamMemberId = null;

let currentPage = 1; // 현재 페이지로 사용 할 변수
const itemsPerPage = 15; // 한 화면에 뽑고 싶은 리스트 수
const maxPageButtons = 5; // 한 세트에 출력할 최대 페이지 수

// 아이디 세팅
function setSessionMemberId() {
    const url = '/api/service/getSessionServiceId';

    fetch(url)
        .then(response => response.json())
        .then((response) => {
            serviceTeamMemberId = response.data.id;
            reloadQnAList(currentPage);
        });
}

// 목록
function reloadQnAList(page = 1) {
    currentPage = page;
    const url = `/api/service/getQnAServiceId?service_id=${serviceTeamMemberId}&page=${page}`
    fetch(url)
        .then(response => response.json())
        .then((response) => {
            const serviceTeamQnAListBox = document.getElementById("serviceTeamQnAListBox");
            serviceTeamQnAListBox.innerHTML = "";

            const serviceTeamQnAListWrapperTemplate = document.querySelector("#serviceTeamQnAListTemplate .serviceTeamQnAListWrapper");

            for (const e of response.data.qna) {
                const serviceTeamQnAListWrapper = serviceTeamQnAListWrapperTemplate.cloneNode(true);

                const qnaIdSpan = serviceTeamQnAListWrapper.querySelector(".qnaIdSpan");
                qnaIdSpan.innerText = e.id;

                const qnaCategoryTitleSpan = serviceTeamQnAListWrapper.querySelector(".qnaCategoryTitleSpan");
                qnaCategoryTitleSpan.innerText = `[${e.category_title}]`;

                const qnaDetailPageA = serviceTeamQnAListWrapper.querySelector(".qnaDetailPageA");
                qnaDetailPageA.href = `/serviceTeam/mineQnaDetailPage?id=${e.id}`;

                if (e.status === '접수 중') {
                    const qnaStatusChangeA = serviceTeamQnAListWrapper.querySelector(".qnaStatusChangeA");
                    qnaStatusChangeA.onclick = () => { changeStatus(e.id) }
                } else {
                    const qnaStatusChangeA = serviceTeamQnAListWrapper.querySelector(".qnaStatusChangeA");
                    qnaStatusChangeA.innerText = '완료';
                }

                const qnaTitleSpan = serviceTeamQnAListWrapper.querySelector(".qnaTitleSpan");
                qnaTitleSpan.innerText = e.title;

                const qnaIsImageSpan = serviceTeamQnAListWrapper.querySelector(".qnaIsImageSpan");
                if (e.url !== '0') {
                    qnaIsImageSpan.classList.remove('d-none');
                }

                const qnaNameSpan = serviceTeamQnAListWrapper.querySelector(".qnaNameSpan");
                qnaNameSpan.innerText = e.nickname;

                const qnaCreatedAtSpan = serviceTeamQnAListWrapper.querySelector(".qnaCreatedAtSpan");
                qnaCreatedAtSpan.innerText = stDateFormat(e.created_at);

                const qnaStatusSpan = serviceTeamQnAListWrapper.querySelector(".qnaStatusSpan");
                qnaStatusSpan.innerText = e.status;

                if (e.status === '답변 완료') {
                    qnaStatusSpan.classList.add('text-success');
                }
                serviceTeamQnAListBox.appendChild(serviceTeamQnAListWrapper);
            }
            setupPagination(response.data.qnaCount, itemsPerPage, page);
        });
}

function changeStatus(id) {
    if(!confirm("해당 문의사항을 접수하시겠습니까?")) {
        return;
    }

    const url = `/api/service/updateQnaStatusComplete?id=${id}`;
    fetch(url)
        .then(response => response.json())
        .then(response => {
            showMessage("접수가 완료되었습니다. <br>답변을 등록해주세요.");
        });
}

// 날짜 포맷
function stDateFormat(createdAt) {
    const stDate = new Date(createdAt);

    let day = stDate.getDate();
    let month = stDate.getMonth() + 1;
    let year = stDate.getFullYear().toString();

    day = day < 10 ? '0' + day : day;
    month = month < 10 ? '0' + month : month;
    return `${year}-${month}-${day}`;
}

// 완료창
function showMessage(text) {
    const reviewUpdateDoneModal = bootstrap.Modal.getOrCreateInstance("#reviewUpdateDoneModal");
    reviewUpdateDoneModal.show();

    const testAlert = document.getElementById("testAlert")
    testAlert.innerHTML = text;

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
            reloadQnAList(Math.max(1, currentPage - maxPageButtons));
        }
    };

    nextPageButton.classList.toggle('disabled', currentPage === pageCount);
    nextPageButton.onclick = (e) => {
        // e.preventDefault();
        if (currentPage < pageCount) {
            reloadQnAList(Math.min(pageCount, currentPage + maxPageButtons));
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
            reloadQnAList(i);
        };
        li.appendChild(a);
        nextPageButton.parentElement.before(li);
    }
}
window.addEventListener("DOMContentLoaded", () => {
    setSessionMemberId();
});
// 이미지 미리보기
document.getElementById('inputQnaImages').addEventListener('change', function(event) {
    const previewBox = document.getElementById('previewBox');
    previewBox.innerHTML = ''; // 기존의 미리보기 이미지 제거

    const files = event.target.files;
    // 파일업로드를 하면 업로드한 파일을 배열로 받아서 forEach문 실행함
    if (files) {
        Array.from(files).forEach(file => {
            // FileReader : 파일을 읽고 그 내용을 브라우저에서 사용할 수 있도록 하는 api!
            const reader = new FileReader();

            // reader.onload : 파일 읽기가 완료되면 실행할 함수 지정 (파일 읽기 완료하면 템플릿 붙여넣음)
            reader.onload = function(e) {
                const previewTemplate = document.querySelector("#previewTemplate .previewWrapper");
                const previewWrapper = previewTemplate.cloneNode(true);

                const previewImg = previewWrapper.querySelector(".previewImg");
                previewImg.src = e.target.result;

                previewBox.appendChild(previewWrapper);
            };
            reader.readAsDataURL(file);
        });
    }
});

let today;
let serviceId;
// 오늘 날짜
function getTodayDate() {
    const now = new Date();
    const year = now.getFullYear();
    const month = (now.getMonth() + 1).toString().padStart(2, '0'); // 월은 0부터 시작하므로 +1 필요
    const date = now.getDate().toString().padStart(2, '0');

    today = `${year}-${month}-${date}`;
    getServiceId();
}

let qnaMyId = null;
function setSessionId() {
    const url = "/api/ticket/getSessionUserId";

    fetch(url)
        .then(response => response.json())
        .then((response) => {
            qnaMyId = response.data.id;
            reloadServiceQnAList();
        });
}

// 로그인 체크
function loginCheck () {
    if(qnaMyId == null) {
        if(confirm("로그인 후 이용 가능합니다. 로그인 페이지로 이동하시겠습니까?")) {
            location.href="/user/loginPage";
        }
        return;
    }
}

// qna 작성 모달 열기
function writeQnAModalOpen() {
    const qnaModal = bootstrap.Modal.getOrCreateInstance("#qnaModal");
    qnaModal.show();
}

// qna 작성 모달 닫기
function writeQnAModalClose() {
    const qnaModal = bootstrap.Modal.getOrCreateInstance("#qnaModal");
    qnaModal.hide();
    getServiceId();
}

// qna 작성
function submitQnaWrite() {
    const writeQnaForm = document.getElementById("writeQnaForm");
    const formData = new FormData(writeQnaForm);

    const selectCategory = document.getElementById("selectCategory");
    if (selectCategory.value === '0') {
        alert("문의하실 사항의 카테고리를 선택해 주세요.")
        selectCategory.focus();
        return;
    }

    const inputTitle = document.getElementById("inputTitle");
    if (inputTitle.value == '') {
        alert("제목을 입력해 주세요.")
        inputTitle.focus();
        return;
    }

    const textareaContent = document.getElementById("textareaContent");
    if (textareaContent.value == '') {
        alert("내용을 입력해 주세요.")
        textareaContent.focus();
        return;
    }

    formData.append('service_id', serviceId);
    // console.log(serviceId);

    // 파일 첨부 확인 : 첨부하지 않은 경우 null 처리
    if(!formData.get("inputImages") || formData.get("inputImages").name === "") {
        formData.set("inputImages", null);
    }

    const url = '/api/service/createQna'
    fetch(url, {
        method: 'POST',
        body: formData
    })
        .then((response) => response.json())
        .then((data) => {
            writeQnAModalClose();
            showMessage("문의사항 등록이 완료되었습니다. <br>현재 접수 중이며, 곧 처리될 예정입니다.");
            reloadServiceQnAList();
        });
}

// 알림창
function showMessage(text) {
    const reviewUpdateDoneModal = bootstrap.Modal.getOrCreateInstance("#reviewUpdateDoneModal");
    reviewUpdateDoneModal.show();

    const testAlert = document.getElementById("testAlert")
    testAlert.innerHTML = text;

    setTimeout(() => {
        reviewUpdateDoneModal.hide();
    }, 1000);
}

// 서비스팀 아이디 가져오기 (제일 일 없는 한사람)
function getServiceId() {
    const url = `/api/service/getServiceId?today=${today}`;

    fetch(url)
        .then(response => response.json())
        .then((response) => {
            serviceId = response.data.serviceId;
            // console.log(serviceId)
        });
}

// qna 리스트 목록
function reloadServiceQnAList() {
    const url = `/api/service/getAllQnAList?id=${qnaMyId}`;

    fetch(url)
        .then(response => response.json())
        .then((response) => {
            const QnAListBox = document.getElementById("QnAListBox");
            QnAListBox.innerHTML = "";

            const qnaListWrapperTemplate = document.querySelector("#QnAListTemplate .QnAListWrapper");

            for (const e of response.data.qnaList) {
                const qnaTeamListWrapper = qnaListWrapperTemplate.cloneNode(true);

                const qnaDetailLinkA = qnaTeamListWrapper.querySelector(".qnaDetailLinkA");
                qnaDetailLinkA.href = `/user/qnaDetailPage?id=${e.id}`;

                const qnaCategorySpan = qnaTeamListWrapper.querySelector(".qnaCategorySpan");
                qnaCategorySpan.innerText = `[${e.category_title}]`;

                const qnaTitleSpan = qnaTeamListWrapper.querySelector(".qnaTitleSpan");
                qnaTitleSpan.innerText = e.title;

                const qnaStatusSpan = qnaTeamListWrapper.querySelector(".qnaStatusSpan");
                qnaStatusSpan.innerText = e.status;

                if (e.status === '답변 완료') {
                    qnaStatusSpan.classList.add('text-success');
                }

                QnAListBox.appendChild(qnaTeamListWrapper);
            }
        });
}

// 체크
function checkQnAInfo() {
    const qnaCheckInput = document.getElementById('qnaCheckInput');
    const qnaInsertButton = document.getElementById('qnaInsertButton');

    if (qnaCheckInput.checked) {
        qnaInsertButton.disabled = false;
    } else {
        qnaInsertButton.disabled = true;
    }
}

window.addEventListener("DOMContentLoaded", () => {
    getTodayDate();
    setSessionId();
});
// 이미지 미리보기
document.getElementById('inputReviewImages').addEventListener('change', function(event) {
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

// 글쓰기 모달 열기
function writeModalOpen() {
    const reviewModal = bootstrap.Modal.getOrCreateInstance("#reviewModal");
    reviewModal.show();
}

// 글쓰기 모달 닫기
function writeModalClose() {
    const reviewModal = bootstrap.Modal.getOrCreateInstance("#reviewModal");
    reviewModal.hide();
}

// 글쓰기 insert + 유효성 검사 해야함
function submitWrite() {
    const writeReviewForm = document.getElementById("writeReviewForm");
    const formData = new FormData(writeReviewForm);

    const selectTicket = document.getElementById("selectTicket");
    if (selectTicket.value == 0) {
        alert("후기를 작성할 페스티벌을 선택해 주세요.")
        selectTicket.focus();
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

// 이미지 넘기는게 없을때 자바스크립트 처리 이거 안하면 fetch가 안됨. 그리고 controller에 value랑 required 입력...
//     const uploadFiles = document.getElementById("inputReviewImages").files;
//     console.log("파일길이 : " + uploadFiles.length);
//     if(uploadFiles.length === 0){
//         formData.delete("inputImages");
//         console.log("이미지 폼데이터 삭제");
//     }

    // 파일 첨부 확인 : 첨부하지 않은 경우 null 처리
    if(!formData.get("inputImages") || formData.get("inputImages").name === "") {
        formData.set("inputImages", null);
    }

    const url = '/api/ticket/createFestivalReview'
    fetch(url, {
        method: 'POST',
        body: formData
    })
        .then((response) => response.json())
        .then((data) => {
            writeModalClose();
            showMessage("리뷰 등록이 완료되었습니다.");
            reloadReviewList();
        });
}

let pageNumber = 1;
let loading = false;
// 더 가져올 데이터가 있는지 여부를 나타내는 변수
let hasMore = true;
let sort = 1;
let searchWord = '';

// 검색?
document.getElementById('searchInput').addEventListener('input', function() {
    searchWord = this.value;
    pageNumber = 1;  // 검색어 입력 시 페이지 번호 초기화
    hasMore = true;
    document.getElementById('reviewListBox').innerHTML = '';  // 기존 리스트 초기화
    reloadReviewList();  // 새로운 검색어에 따라 리스트 재로드
});

function reloadReviewList() {
    loading = true;

    const url = `/api/ticket/getReviewList?page=${pageNumber}&sort=${sort}&searchWord=${encodeURIComponent(searchWord)}`;
    fetch(url)
        .then(response => response.json())
        .then(response => {
            const reviewCount = document.getElementById('reviewCount');
            reviewCount.innerText = response.data.searchCount;

            const searchNoResult = document.getElementById('searchNoResult');
            // 이거 없으면 페이지 무한으로 ++..
            if (response.data.reviewList.length == 0) {
                hasMore = false;
                loading = false;
                searchNoResult.classList.remove('d-none');
                return;
            }
            searchNoResult.classList.add('d-none');

            const reviewListBox = document.getElementById("reviewListBox");
            // reviewListBox.innerHTML = "";

            const reviewListWrapperTemplate = document.querySelector("#reviewListTemplate .reviewListWrapper");

            for (const e of response.data.reviewList) {
                const reviewListWrapper = reviewListWrapperTemplate.cloneNode(true);

                const reviewColSize = reviewListWrapper.querySelector(".reviewColSize");
                if(e.review_id != 0) {
                    reviewColSize.classList.add("col-9");
                } else {
                    reviewColSize.classList.add("col");
                }

                const reviewHref = reviewListWrapper.querySelector(".reviewHref");
                reviewHref.href = `/ticket/reviewDetailPage?id=${e.id}`;

                const reviewTitleSpan = reviewListWrapper.querySelector(".reviewTitleSpan");
                reviewTitleSpan.innerText = e.title;

                // const reviewContentSpan = reviewListWrapper.querySelector(".reviewContentSpan");
                // reviewContentSpan.innerText = e.content;

                const reviewNicknameSpan = reviewListWrapper.querySelector(".reviewNicknameSpan");
                reviewNicknameSpan.innerText = e.nickname;

                const reviewCreatedAtSpan = reviewListWrapper.querySelector(".reviewCreatedAtSpan");
                reviewCreatedAtSpan.innerText = reviewDateFormat(e.created_at);

                // const reviewLikeCountSpan = reviewListWrapper.querySelector(".reviewLikeCountSpan");
                // reviewLikeCountSpan.innerText = e.like_count;

                const reviewReadCountSpan = reviewListWrapper.querySelector(".reviewReadCountSpan");
                reviewReadCountSpan.innerText = e.read_count;

                const reviewImageSpan = reviewListWrapper.querySelector(".reviewImageSpan");
                const reviewColImageSize = reviewListWrapper.querySelector(".reviewColImageSize")
                if(e.review_id != 0) {
                    reviewImageSpan.src = `/images/${e.url}`;
                } else {
                    reviewColImageSize.classList.add("d-none")
                }

                reviewListBox.appendChild(reviewListWrapper);

            }
            pageNumber ++;
            loading = false;
        });
}

document.addEventListener('DOMContentLoaded', () => {
    // 스크롤
    window.addEventListener('scroll', () => {
        if (loading || !hasMore) {
            return;
        }
        // document.body.offsetHeight : <body> 요소의 높이를 포함하여 요소의 보더(border)를 포함한 높이, height: 100%가 적용된 경우, 화면의 높이로 제한
        // document.documentElement.scrollHeight : 문서의 전체 높이. 스크롤 가능한 전체 높이, height: 100% 스타일이 적용된 경우에도 문서의 전체 높이를 정확하게 반영
        if ((window.innerHeight + window.scrollY) >= (document.documentElement.scrollHeight - 100)) {
            reloadReviewList();
        }
    })
    reloadReviewList();
});

function reviewDateFormat(createdAt) {
    const today = new Date();
    const reviewDate = new Date(createdAt);

    let day = reviewDate.getDate();
    let month = reviewDate.getMonth() + 1;
    let year = reviewDate.getFullYear().toString().slice(-2);
    let hours = reviewDate.getHours();
    let minutes = reviewDate.getMinutes();

    const isToday = reviewDate.toDateString() == today.toDateString();

    if (isToday) {
        hours = hours < 10 ? '0' + hours : hours;
        minutes = minutes < 10 ? '0' + minutes : minutes;
        return `${hours}:${minutes}`;
    } else {
        day = day < 10 ? '0' + day : day;
        month = month < 10 ? '0' + month : month;
        return `${year}.${month}.${day}`;
    }
}

// 수정완료창
function showMessage(text) {
    const reviewUpdateDoneModal = bootstrap.Modal.getOrCreateInstance("#reviewUpdateDoneModal");
    reviewUpdateDoneModal.show();

    const testAlert = document.getElementById("testAlert")
    testAlert.innerText = text;

    setTimeout(() => {
        reviewUpdateDoneModal.hide();
        location.reload();
    }, 1000);
}

// 목록 : 정렬 기능
function sortList(order) {
    // order : 1 최신 / 2 조회 / 3 좋아요

    pageNumber = 1;
    sort = order;

    const reviewListBox = document.getElementById("reviewListBox");
    reviewListBox.innerHTML = "";
    reloadReviewList();

    // 정렬명 변경
    const sortName = document.getElementById("sortName");
    const sortText = event.target.innerText;
    sortName.innerText = sortText;
}
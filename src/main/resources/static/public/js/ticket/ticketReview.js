let reviewMyId = null;

const urlParams = new URL(window.location.href).searchParams;
const reviewId = urlParams.get("id");

let reviewLikeCount = 0;

// 아이디 세팅
function setSessionId() {
    const url = "/api/ticket/getSessionUserId";

    fetch(url)
        .then(response => response.json())
        .then((response) => {
            reviewMyId = response.data.id;
            isReviewLike();
        });
}

// 로그인 체크
function loginCheck () {
    if(reviewMyId == null) {
        if(confirm("로그인 후 이용 가능합니다. 로그인 페이지로 이동하시겠습니까?")) {
            // console.log("aa?" + Utils.FESTIVAL_URL)
            location.href='/user/loginPage';
        }
        return;
    }
}

// 좋아요 확인
function isReviewLike() {
    const url = `/api/ticket/isReviewLike?user_id=${reviewMyId}&review_id=${reviewId}`;

    fetch(url)
        .then(response => response.json())
        .then((response) => {
            const likeIcon = document.getElementById("likeIcon");
            const likeButton = document.getElementById("likeButton");
            const likeCount = document.getElementById("likeCount");
            likeCount.innerText = response.data.likeCount;
            reviewLikeCount = response.data.likeCount;

            // likeButton.removeEventListener('click', like);
            // likeButton.removeEventListener('click', unLike);

            // 좋아요를 눌렀을 때
            if(response.data.isLike == true) {
                likeIcon.classList.add("fill-1");
                // setAttribute 하면 오류남!!
                likeButton.onclick = ()=> {unLike()};
                // = ()=>... 이전에 설정된 이벤트 핸들러는 제거되고 새로운 이벤트 핸들러만 설정

                // addEventListener로 하면 위에서 강제로 지워야함!!!!
                // likeButton.addEventListener('click', unLike);
            } else {
                likeIcon.classList.remove("fill-1");
                likeButton.onclick = ()=> {like()};
                // likeButton.addEventListener('click', like);
            }
        });
}

// 좋아요
function like() {
    const url = `/api/ticket/createReviewLike?user_id=${reviewMyId}&review_id=${reviewId}`;

    fetch(url)
        .then(response => response.json())
        .then((response) => {
            isReviewLike();
            alert('좋아요! 함께 즐겨주셔서 감사합니다.');
            location.reload();
        });
}

// 좋아요 취소
function unLike() {
    const url = `/api/ticket/deleteReviewLike?user_id=${reviewMyId}&review_id=${reviewId}`;

    fetch(url)
        .then(response => response.json())
        .then((response) => {
            isReviewLike();
            alert('좋아요가 취소되었습니다. 언제든 다시 즐겨주세요!');
            location.reload();
        });
}

// 리뷰 개행.....
function reloadReview() {
    const url = `/api/ticket/getReviewById?id=${reviewId}`;
    fetch(url)
        .then(response => response.json())
        .then((response) => {
            const reviewTitleSpan = document.getElementById("reviewTitleSpan");
            reviewTitleSpan.innerText = response.data.reviewInfo.title;

            const reviewContentSpan = document.getElementById("reviewContentSpan");
            reviewContentSpan.innerText = response.data.reviewInfo.content;
        });
}

// 리뷰 삭제
function reviewDelete() {
    const isReviewDelete = confirm("리뷰를 삭제하시겠습니까?");

    // 확인을 클릭하지 않은 경우
    if (!isReviewDelete) {
        return;
    }

    const url = `/api/ticket/deleteReview?id=${reviewId}`;

    fetch(url)
        .then(response => response.json())
        .then((response) => {
            location.href="/ticket/reviewPage";
        });
}

// 리뷰 수정 모달 열기
function updateModalOpen() {
    // const optionModal = bootstrap.Modal.getOrCreateInstance("#optionModal");
    // optionModal.hide();

    const optionBox = document.getElementById("optionBox");
    optionBox.classList.add('hide');
    optionBox.classList.remove('show');
    setTimeout(function () {
        optionBox.classList.add('d-none');
    }, 200); // 애니메이션 지속 시간과 일치


    const reviewUpdateModal = bootstrap.Modal.getOrCreateInstance("#reviewUpdateModal");
    reviewUpdateModal.show();
}

// 리뷰 수정 모달 닫기
function updateModalClose() {
    const reviewUpdateModal = bootstrap.Modal.getOrCreateInstance("#reviewUpdateModal");
    reviewUpdateModal.hide();
}

// 리뷰 수정하기
function submitUpdate() {
    const updateReviewForm = document.getElementById("updateReviewForm");
    const formData = new FormData(updateReviewForm);

    const inputUpdateTitle = document.getElementById("inputUpdateTitle");
    if (inputUpdateTitle.value == '') {
        alert("제목을 입력해 주세요.")
        inputUpdateTitle.focus();
        return;
    }

    const textareaUpdateContent = document.getElementById("textareaUpdateContent");
    if (textareaUpdateContent.value == '') {
        alert("내용을 입력해 주세요.")
        textareaUpdateContent.focus();
        return;
    }

    const url = '/api/ticket/updateReview'
    fetch(url, {
        method: 'POST',
        body: formData
    })
        .then((response) => response.json())
        .then((data) => {
            updateModalClose();
            // location.reload();
            showMessage("수정이 완료되었습니다.");
            reloadReview();
        });
}

// 확인창
function showMessage(text) {
    const reviewUpdateDoneModal = bootstrap.Modal.getOrCreateInstance("#reviewUpdateDoneModal");
    reviewUpdateDoneModal.show();

    const testAlert = document.getElementById("testAlert")
    testAlert.innerText = text;

    setTimeout(() => {
        reviewUpdateDoneModal.hide();
    }, 1000);
}

// 공유 모달
// function shareModalOpen(id) {
//     const shareUrl = document.getElementById("shareUrl");
//     shareUrl.innerText = `https://festival.null-pointer-exception.com/ticket/reviewDetailPage?id=${id}`;
//
//     const twitterShareIcon = document.getElementById('twitterShareIcon');
//     twitterShareIcon.onclick = () => { shareTwitter(`${id}`) };
//
//     const facebookShareIcon = document.getElementById('facebookShareIcon');
//     facebookShareIcon.onclick = () => { shareFacebook(`${id}`) };
//
//     const kakaoShareIcon = document.getElementById('kakaoShareIcon');
//     kakaoShareIcon.onclick = () => { shareKakao(`${id}`) };
//
//     const shareModal = bootstrap.Modal.getOrCreateInstance("#shareModal");
//     shareModal.show();
// }


// 옵션 모달
function optionModalOpen() {
    const optionBox = document.getElementById("optionBox");

    if (optionBox.classList.contains('d-none')) {
        optionBox.classList.remove('d-none');
        setTimeout(function() {
            optionBox.classList.add('show');
            optionBox.classList.remove('hide');
        }, 10); // 브라우저 렌더링을 위한 약간의 지연 시간
    } else {
        optionBox.classList.add('hide');
        optionBox.classList.remove('show');
        setTimeout(function () {
            optionBox.classList.add('d-none');
        }, 200); // 애니메이션 지속 시간과 일치
    }

    // const optionModal = bootstrap.Modal.getOrCreateInstance("#optionModal");
    // optionModal.show();

    // 모달 밖을 클릭하면 닫히게
    // const optionModalElement = document.getElementById('optionModal');
    // optionModalElement.addEventListener('click', function (event) {
    //     if (event.target === optionModalElement) {
    //         optionModal.hide();
    //     }
    // });
}

window.addEventListener("DOMContentLoaded", () => {
    setSessionId();
    reloadReview();
});
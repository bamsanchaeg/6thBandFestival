// 날짜 포맷
function dateFormat(formatDay) {
    const myDate = new Date(formatDay);

    let day = myDate.getDate();
    let month = myDate.getMonth() + 1;
    // 연도의 마지막 2자리
    let year = myDate.getFullYear().toString().slice(-2);

    // 두 자릿수로 맞추기 위해 day와 month가 10보다 작을 경우 앞에 0을 추가
    day = day < 10 ? '0' + day : day;
    month = month < 10 ? '0' + month : month;

    return `${year}.${month}.${day}`;
}

// 리뷰 삭제
function deleteReview(id) {
    if(!confirm("해당 리뷰를 삭제하시겠습니까?")) {
        return;
    }

    const url = `/api/admin/ticket/deleteReview?id=${id}`;
    fetch(url)
        .then(response => response.json())
        .then(response => {
            alert("삭제가 완료되었습니다.");
            reloadReviewList();
        });
}

let currentPage = 1;
const itemsPerPage = 15;
const maxPageButtons = 5;

// 리뷰 리스트
function reloadReviewList(page = 1) {
    currentPage = page;
    const url = `/api/admin/ticket/getReviewList?page=${page}`
    fetch(url)
        .then(response => response.json())
        .then(response => {
            const reviewListBox = document.getElementById("reviewListBox");
            reviewListBox.innerHTML = "";

            const reviewListWrapperTemplate = document.querySelector("#reviewListTemplate .reviewListWrapper");

            for (const e of response.data.reviewList) {
                const reviewListWrapper = reviewListWrapperTemplate.cloneNode(true);

                const reviewIdSpan = reviewListWrapper.querySelector(".reviewIdSpan");
                reviewIdSpan.innerText = e.id;

                const reviewTitleSpan = reviewListWrapper.querySelector(".reviewTitleSpan");
                reviewTitleSpan.innerText = e.title;

                const reviewContentSpan = reviewListWrapper.querySelector(".reviewContentSpan");
                reviewContentSpan.innerText = e.content;

                const isReviewImagesSpan = reviewListWrapper.querySelector(".isReviewImagesSpan");
                if (e.image_count > 0) {
                    isReviewImagesSpan.classList.remove("d-none");
                }

                const reviewInfoSpan = reviewListWrapper.querySelector(".reviewInfoSpan");
                reviewInfoSpan.href = `/admin/ticket/reviewAdminInfoPage?id=${e.id}`

                const reviewWriterSpan = reviewListWrapper.querySelector(".reviewWriterSpan");
                reviewWriterSpan.innerText = e.nickname;

                const reviewCreatedAtSpan = reviewListWrapper.querySelector(".reviewCreatedAtSpan");
                reviewCreatedAtSpan.innerText = dateFormat(e.created_at);

                const reviewDeleteSpan = reviewListWrapper.querySelector(".reviewDeleteSpan");
                reviewDeleteSpan.onclick = () => {deleteReview(e.id)};

                reviewListBox.appendChild(reviewListWrapper);
            }
            setupPagination(response.data.reviewCount, itemsPerPage, page);
        });
}

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
            reloadReviewList(Math.max(1, currentPage - maxPageButtons));
        }
    };

    nextPageButton.classList.toggle('disabled', currentPage === pageCount);
    nextPageButton.onclick = (e) => {
        // e.preventDefault();
        if (currentPage < pageCount) {
            reloadReviewList(Math.min(pageCount, currentPage + maxPageButtons));
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
            reloadReviewList(i);
        };
        li.appendChild(a);
        nextPageButton.parentElement.before(li);
    }
}

window.addEventListener("DOMContentLoaded", () => {
    reloadReviewList(currentPage);
});
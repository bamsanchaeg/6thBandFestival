const urlParams = new URL(window.location.href).searchParams;
const reviewId = urlParams.get("id");

// 리뷰
function reloadReview() {
    const url = `/api/admin/ticket/getReviewListById?id=${reviewId}`
    fetch(url)
        .then(response => response.json())
        .then(response => {
            const reviewContentSpan = document.getElementById('reviewContentSpan');
            reviewContentSpan.innerText = response.data.reviewInfo.content;
        });
}

function deleteReview() {
    if(!confirm("해당 리뷰를 삭제하시겠습니까?")) {
        return;
    }

    const url = `/api/admin/ticket/deleteReview?id=${reviewId}`;
    fetch(url)
        .then(response => response.json())
        .then(response => {
            alert("삭제가 완료되었습니다.");
            window.location = "/admin/ticket/reviewAdminPage";
        });
}


// 날짜 포맷
function dateFormat(formatDay) {
    const myDate = new Date(formatDay);

    let day = myDate.getDate();
    let month = myDate.getMonth() + 1;
    // 연도의 마지막 2자리
    let year = myDate.getFullYear().toString().slice(-2);
    let hours = myDate.getHours();
    let minutes = myDate.getMinutes();

    // 두 자릿수로 맞추기 위해 day와 month가 10보다 작을 경우 앞에 0을 추가
    day = day < 10 ? '0' + day : day;
    month = month < 10 ? '0' + month : month;

    return `${year}.${month}.${day} ${hours}:${minutes}`;
}

window.addEventListener("DOMContentLoaded", () => {
    reloadReview();
});
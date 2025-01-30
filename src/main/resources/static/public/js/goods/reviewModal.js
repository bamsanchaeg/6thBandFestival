
// 모달삽입
function insertmodal(){
    const userReviewModal = bootstrap.Modal.getOrCreateInstance("#userReviewModal");
    userReviewModal.show();
}

//내용
const urlParams = new URL(window.location).searchParams;
const orderId = urlParams.get("id");

let myId = null;

function setSessionId(){
        const url = "/api/userGoods/getSessionUserId";
        fetch(url)
        .then(response => response.json())
        .then((response) => {
            myId = response.data.id;
        });
    }

function registerReview(){
    if(myId == null){
        if(confirm("로그인 하셔야 이용 가능합니다. \n로그인 페이지로 이동하시겠습니까?")){
            location.href="/user/loginPage";
        }else{
            return;
        }
    }

    const reviewForm = document.getElementById("frmReviewRegistrationForm");

    // 별점 유효성 검사
    const ratingInputs = document.getElementsByName("rating");
    let selectedRating = null;
    for (let i = 0; i < ratingInputs.length; i++) {
        if (ratingInputs[i].checked) {
            selectedRating = ratingInputs[i].value;
            break;
        }
    }

    if (!selectedRating) {
        alert("별점을 선택해주세요.");
        return;
    }

    //내용 유효성 검사
    const content = document.getElementById("content");
    if(content.value == ''){
        alert("내용을 입력해주세요.");
        return;
    }

    //상세이미지 유효성 검사
    const multiple_image = document.getElementById("multiple_image");
    if(multiple_image.value == ''){
        if(confirm("사진을 등록하지 않으셨습니다. \n리뷰등록을 완료하시겠습니까?")){
            alert("리뷰작성이 완료되었습니다.");
            window.location = "/goods/mainPage";
        }else{

        }
    }

    const formData = new FormData(reviewForm);
    formData.append("order_id", orderId);

    // FormData 내용 출력
    for (let [key, value] of formData.entries()) {
        console.log(key, value);
    }
    
    const url = `/api/userGoods/registerReview?id=${orderId}`;
    fetch(url, {
        method: "post",
        body: formData
    })
    .then(response => response.json())
    .then(response => {
        console.log(response);
        
        if(confirm("리뷰작성이 완료되었습니다. \n메인화면으로 가시겠습니까?")){
            alert("리뷰작성이 완료되었습니다.");
            window.location = "/goods/goodsList";
        }else{
            alert("리뷰작성에 실패하였습니다. \n다시 작성해 주세요.")
        }
    });
}

//영수증 모달
document.getElementById('receiptModal').addEventListener('show.bs.modal', function (event) {
    const orderCreatedAtValue = document.querySelector('#order-data #orderCreatedAt').textContent.trim();
    const orderCreatedAtElement = document.getElementById('orderCreatedAt');
    orderCreatedAtElement.textContent = orderCreatedAtValue;

    const orderGoodsTitleValue = document.querySelector('#order-data #orderGoodsTitle').textContent.trim();
    const orderGoodsTitleElement = document.getElementById('orderGoodsTitle');
    orderGoodsTitleElement.textContent = orderGoodsTitleValue;

    const orderPricetValue = document.querySelector('#order-data #orderPrice').textContent.trim();
    const orderPrice = parseFloat(orderPricetValue.replace(/[^0-9.]/g, ''));
    const orderPriceFormatted = orderPrice.toLocaleString('en-US', { minimumFractionDigits: 0, maximumFractionDigits: 0 });
    const orderPriceElement = document.getElementById('orderPrice');
    orderPriceElement.textContent = `${orderPriceFormatted} 원`;

    // 부가세 제외 결제금액을 계산
    const orderPrice2Element = document.getElementById('orderPrice2');
    const orderPrice2 = parseFloat(orderPrice2Element.textContent.trim().replace(/[^0-9.]/g, ''));

    const orderPrice90 = Math.round(orderPrice2 * 0.9);
    const orderPrice90Formatted = orderPrice90.toLocaleString('en-US');

    const orderPrice90Element = document.getElementById('orderPrice90');
    orderPrice90Element.textContent = `${orderPrice90Formatted} 원`;

    const orderPrice210Element = document.getElementById('orderPrice210');
    const orderPrice210 = parseFloat(orderPrice210Element.textContent.trim().replace(/[^0-9.]/g, ''));

    const orderPrice10 = Math.round(orderPrice210 * 0.1);
    const orderPrice10Formatted = orderPrice10.toLocaleString('en-US');

    // 계산된 값 설정
    const orderPrice10Element = document.getElementById('orderPrice10');
    orderPrice10Element.textContent = `${orderPrice10Formatted} 원`;

});


window.addEventListener("DOMContentLoaded", () => {
    setSessionId();
});

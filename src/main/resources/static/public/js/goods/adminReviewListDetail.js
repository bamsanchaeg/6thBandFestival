

const urlParams = new URL(window.location).searchParams;
const reviewId = urlParams.get("id");

function reviewListDetail(reviewId){
    const url = `/api/goods/reviewDetail?id=${reviewId}`;
    fetch(url)
    .then(response => response.json())
    .then((response) => {

        const reviewDetail = response.data.reviewDetail;

        const userAccountName = document.getElementById("userAccountName");
        userAccountName.innerText = reviewDetail.user.account_name || "계정 이름이 등록되지 않았습니다.";

        const userNickName = document.getElementById("userNickName");
        userNickName.innerText = reviewDetail.user.nickname;

        const reviewCreatedAt = document.getElementById("reviewCreatedAt");
        reviewCreatedAt.innerText = reviewDetail.created_at;

        const reviewId = document.getElementById("reviewId");
        reviewId.innerText = reviewDetail.id;

        const rating = document.getElementById("rating");
        const ratingValue = reviewDetail.rating;

        const maxRating = 5;
        let stars = '';

        for (let i = 1; i <= maxRating; i++) {
            if (i <= ratingValue) {
                stars += '★'; 
            } else {
                stars += '☆'; 
            }
        }

        rating.innerHTML = `${stars} (${ratingValue} 점)`;

        const content = document.getElementById("content");
        content.innerText = reviewDetail.content;
        
        const reviewImagesWrapper = document.getElementById("reviewImagesWrapper");
        const reviewImagesContainer = document.getElementById("reviewImagesContainer");
        const noImageMessage = document.getElementById("noImageMessage");

        reviewImagesContainer.style.display = 'none';
        noImageMessage.style.display = 'none';

        reviewImagesContainer.innerHTML = '';

        if (reviewDetail.reviewDetailImages.length > 0) {
            reviewDetail.reviewDetailImages.forEach(image => {
                const imgElement = document.createElement('img');
                imgElement.src = `/images/${image.multiple_image}`;
                imgElement.alt = "Review Image";
                imgElement.style.maxWidth = '40%';
                imgElement.style.marginBottom = '10px';
                imgElement.style.height ='20em';
                reviewImagesContainer.appendChild(imgElement);
            });
            
            reviewImagesContainer.style.display = 'block';
            noImageMessage.style.display = 'none';
        } else {
            reviewImagesWrapper.style.display = 'none';
            reviewImagesContainer.style.display = 'none';
            noImageMessage.innerText = "등록된 이미지가 없습니다.";
            noImageMessage.style.display = 'block';
        }


        const reviewLikes = document.getElementById("reviewLikes");
        reviewLikes.innerText = reviewDetail.reviewLikes.length || 0;

        const reviewRecommends = document.getElementById("reviewRecommends");

        if (reviewDetail.reviewRecommends.length > 0) {
            reviewRecommends.innerText = reviewDetail.reviewRecommends.map(recommend => recommend.recommend).join(" / ");
            deleteRecommend.style.display = 'block';
        } else {
            reviewRecommends.innerText = "등록된 답변이 없습니다.";
            deleteRecommend.style.display = 'none';
        }

        const email = document.getElementById("email");
        email.innerText = reviewDetail.user.email || "이메일주소가 등록되지 않았습니다.";

        const gender = document.getElementById("gender");
        gender.innerText = reviewDetail.user.gender || "성별이 등록되지 않았습니다.";

        const phone = document.getElementById("phone");
        phone.innerText = reviewDetail.user.phone;

        const userCreatedAtElement = document.getElementById("userCreatedAt");
        if (userCreatedAtElement) {
            userCreatedAtElement.innerText = reviewDetail.user.created_at ? new Date(reviewDetail.user.created_at).toLocaleString() : "가입일 정보가 없습니다.";
        }

        const orderId = document.getElementById("orderId");
        orderId.innerText = reviewDetail.order.id;

        const goodsGoodsTitle = document.getElementById("goodsGoodsTitle");
        goodsGoodsTitle.innerText = reviewDetail.goods.goods_title;

        const orderPrice = document.getElementById("orderPrice");
        const price = reviewDetail.order.order_price;
        orderPrice.innerText = `${price.toLocaleString()} 원`;

        const orderCreatedAt = document.getElementById("orderCreatedAt");
        orderCreatedAt.innerText = reviewDetail.order.created_at;

    })
    .catch(error => {
        console.error("Error fetching review details:", error);
    });
}

//판매자 답글
function registerReviewCommend(){
    const reviewRecommendText = document.getElementById("reviewRecommendText");
    const reviewId = urlParams.get("id");

    if (reviewRecommendText.value === "") {
        alert("답글을 입력해주세요.");
        return;
    }

    const url ="/api/goods/insertReviewRecommend";
    fetch(url, {
        method: "post",
        headers: {
            "Content-Type" : "application/x-www-form-urlencoded"
        },
        body: `goods_review_id=${reviewId}&recommend=${reviewRecommendText.value}`
    })
    .then(response => response.json())
    .then(response => {
        reviewRecommendText.value ="";
        
        if (response.result == "success") {
            alert("답글이 성공적으로 등록되었습니다.");
        } else {
            alert("답글 등록에 실패했습니다. 다시 시도해주세요.");
        }
        reviewListDetail(reviewId);
    })
    ;
}

document.addEventListener('DOMContentLoaded', () => {
    // URL에서 goodsReviewId 값을 가져오기
    const urlParams = new URLSearchParams(window.location.search);
    const goodsReviewId = urlParams.get('id');

    if (goodsReviewId) {
        document.querySelector('.delete-button').addEventListener('click', () => {
            deleteReviewRecommendations(goodsReviewId);
        });
    } else {
        console.error('goodsReviewId not found in the URL');
    }
});

function deleteReviewRecommendations(goodsReviewId) {
    console.log('Deleting review with ID:', goodsReviewId);
    fetch(`/api/goods/deleteReviewRecommendations?goodsReviewId=${goodsReviewId}`, {
        method: 'DELETE'
    })
    .then(response => response.json())
    .then(data => {
        if (data.result === 'success') {
            console.log('삭제 성공:', data.message);
        } else {
            console.error('삭제 실패:', data.message);
        }
        reviewListDetail(reviewId);
    })
    .catch(error => console.error('Error:', error));
}

window.addEventListener("DOMContentLoaded", () => {
    reviewListDetail(reviewId);
});

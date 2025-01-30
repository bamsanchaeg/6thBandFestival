const urlParams = new URL(window.location.href).searchParams;
const goodsId = urlParams.get("id");

let myId = null;

function setSessionId(){
    const url = "/api/userGoods/getSessionUserId";
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        myId = response.data.id;
        reloadHeart(); 
    });
}

function toggleWish(){
    if(myId == null){
        if(confirm("로그인 하셔야 이용 가능합니다. 로그인 페이지로 이동하시겠습니까?")){
            location.href = "/user/loginPage";
        } else {
            return;
        }
    }

    const heartElement = this;
    const heartId = heartElement.getAttribute("data-heart-id");
    const url = heartElement.classList.contains("fill-1")
        ? `/api/userGoods/unWish?goods_id=${goodsId}&heart_id=${heartId}`
        : `/api/userGoods/wish?goods_id=${goodsId}&heart_id=${heartId}`;

    fetch(url)
        .then(response => response.json())
        .then(response => {
            if(response.data.isWishLiked){
                heartElement.classList.add("fill-1");
            } else{
                heartElement.classList.remove("fill-1");
            }

            reloadHeart();
            updateWishCount();
        })
        .catch(error => console.error('Error toggling wish:', error));
}

function reloadHeart(){
    if(myId == null){
        return;
    }
    
    const url = "/api/userGoods/isWishLiked?goods_id=" + goodsId;
    fetch(url)
    .then(response => response.json())
    .then(response => {
            const heartElements = document.querySelectorAll('[data-heart-id]');
            heartElements.forEach(heartElement => {
                if (response.data.isWishLiked) {
                    heartElement.classList.add("fill-1");
                } else {
                    heartElement.classList.remove("fill-1");
                }
                heartElement.onclick = toggleWish;
            });
        })

        .catch(error => console.error('Error:', error))
        ;
}

function updateWishCount() {
    const url = `/api/userGoods/totalWishLikes?goods_id=${goodsId}`;
    
    fetch(url)
        .then(response => response.json())
        .then(response => {
            const countElement = document.querySelector(".wishCount");
            if (countElement) {
                countElement.innerText = response.data.count;
            } else {
                console.error('Wish count element not found'); 
            }
        })
        .catch(error => console.error('Error updating wish count:', error));
}

function reloadReviewList(){
    const url = "/api/userGoods/getReviewList?goods_id=" + goodsId;
    
    fetch(url)
    .then(response => response.json())
    .then(response => {

        const reviewListBox = document.getElementById("reviewListBox");
        reviewListBox.innerHTML = "";

        const reviewWrapperTemplate = document.querySelector("#template .reviewWrapper");
        
        response.data.reviewList.forEach(e =>{
            const newReviewWrapper = reviewWrapperTemplate.cloneNode(true);
            
            const reviewRating = newReviewWrapper.querySelector(".reviewRating");
            const rating = e.goodsReviewDto.rating; 
            reviewRating.innerHTML = ''; 
            for (let i = 1; i <= 5; i++) { 
                const star = document.createElement('span'); 
                star.className = 'star'; 
                star.innerText = i <= rating ? '★' : '☆'; 
                reviewRating.appendChild(star);
            }

            const userImage = newReviewWrapper.querySelector(".userImage");
            userImage.src = `/images/${e.userImage}`;

            const reviewUserNickName = newReviewWrapper.querySelector(".reviewUserNickName");
            reviewUserNickName.innerText = e.nickName;

            const reviewDate = newReviewWrapper.querySelector(".reviewDate");
            const date = new Date(e.goodsReviewDto.created_at);
            reviewDate.innerText = `${date.getFullYear()}.${date.getMonth()+1}.${date.getDate()}`;

            const reviewGoodsTitle = newReviewWrapper.querySelector(".reviewGoodsTitle");
            reviewGoodsTitle.innerText = e.goodsTitle;

            const reviewContent = newReviewWrapper.querySelector(".reviewContent");
            reviewContent.innerText = e.goodsReviewDto.content;

            const reviewDetailImage = newReviewWrapper.querySelector(".reviewDetailImage");
            reviewDetailImage.innerHTML = '';
         
            // GPT 요약
            const review_GPT = newReviewWrapper.querySelector('.review_GPT');
            const reviewLine = newReviewWrapper.querySelector('.reviewLine');

            // 기준 날짜 설정
            const cutOffDate = new Date('2024-08-25');

            if (e.goodsGptReviewDto && e.goodsGptReviewDto.gpt_Review) {
                reviewLine.style.display = 'block';
                review_GPT.style.display = 'block';
                review_GPT.textContent = e.goodsGptReviewDto.gpt_Review;
            } else {
                const reviewDate = new Date(e.goodsReviewDto.created_at);

                if (reviewDate < cutOffDate) {
                    review_GPT.style.display = 'none'; // GPT 요약 부분 숨기기
                    reviewLine.style.display = 'none';
                } else {
                    review_GPT.style.display = 'block'; // GPT 요약 부분 표시
                    reviewLine.style.display = 'block';
                    review_GPT.textContent = "GPT 요약 내용이 없습니다.";
                }
            }

            e.detailImages.forEach(detailImage => {
                const imgElement = document.createElement('img');
                imgElement.src = `/images/${detailImage}`;
                imgElement.className = 'img-fluid reviewImageSZ';
                reviewDetailImage.appendChild(imgElement);
            });

            const reviewLikeCountElement = newReviewWrapper.querySelector(".reviewLikeCount");
            reviewLikeCountElement.id = `reviewLikeCount-${e.goodsReviewDto.id}`;

            reloadReviewLikeCount(e.goodsReviewDto.id);

            const reviewHeart = newReviewWrapper.querySelector(".review-heart");
            if (e.reviewLiked == true) {
                reviewHeart.classList.add("fill-1");
                reviewHeart.onclick = () =>{ toggleReviewLike(e.goodsReviewDto.id)};
            } else {
                reviewHeart.classList.remove("fill-1");
                reviewHeart.onclick = () =>{ toggleReviewLike(e.goodsReviewDto.id)};
            }
            reloadReviewLikeCount(e.goodsReviewDto.id);

            if(myId != null && e.userId == myId){
                const reviewDeleteButton = newReviewWrapper.querySelector(".reviewDeleteButton");
                reviewDeleteButton.classList.remove("d-none");
                reviewDeleteButton.setAttribute("onclick", `deleteReview(${e.goodsReviewDto.id})`);
            }

            reviewListBox.appendChild(newReviewWrapper);
            reloadReviewCount();
            reloadReviewAvg();

            // 리뷰 답글 불러오기
            loadReviewReplies(e.goodsReviewDto.id, newReviewWrapper);

            if (myId != null && e.userId == myId) {
                const reviewDeleteButton = newReviewWrapper.querySelector(".reviewDeleteButton");
                reviewDeleteButton.classList.remove("d-none");
                reviewDeleteButton.setAttribute("onclick", `deleteReview(${e.goodsReviewDto.id})`);
            }

            reviewListBox.appendChild(newReviewWrapper);
            reloadReviewCount();
            reloadReviewAvg();
        
        });
    
    })

}

// 리뷰에 대한 답글 불러오기 함수
function loadReviewReplies(reviewId, reviewWrapper) {
    const url = `/api/userGoods/getReviewReplies?reviewId=${reviewId}`;
    
    fetch(url)
    .then(response => response.json())
    .then(response => {
        const replies = response.data;
        const replyWrapper = reviewWrapper.querySelector(".replyWrapper");

        replyWrapper.innerHTML = '';

        if (replies.length === 0) {
            return;
        }

        replies.forEach(reply => {
            const replyElement = document.createElement('div');
            replyElement.className = 'reviewReply';

            const replyContent = document.createElement('p');
            replyContent.innerText = reply.recommend;

            const replyDate = document.createElement('span');
            const date = new Date(reply.created_at);
            replyDate.innerText = `${date.getFullYear()}.${date.getMonth()+1}.${date.getDate()}`;

            replyElement.appendChild(replyContent);
            replyElement.appendChild(replyDate);

            replyWrapper.appendChild(replyElement);
        });
    });
}

//리뷰 카운트
function reloadReviewCount(){
    const url = "/api/userGoods/getCountReviewByGoodsId?goods_id=" + goodsId;
    fetch(url)
    .then(response => response.json())
    .then(response => {
        const reviewCountElements = document.querySelectorAll(".reviewCount");
        const reviewCount = response.data.count;

        reviewCountElements.forEach(element => {
            if(reviewCount == 0){
                element.innerText = "0";
                noReviewsMessage.style.display = "block";
            } else {
                element.innerText = reviewCount;
                noReviewsMessage.style.display = "none";
            }
            
        });
    });
}

//리뷰 좋아요 카운트
function reloadReviewLikeCount(reviewId){
    const url = "/api/userGoods/totalReviewLikes?review_id=" + reviewId;
    fetch(url)
    .then(response => response.json())
    .then(response => {
        const reviewLikeCountElement = document.getElementById(`reviewLikeCount-${reviewId}`);
        if (reviewLikeCountElement) {
            reviewLikeCountElement.innerText = response.data.reviewCount;
        }
    })
    ;
}

//리뷰 총 평점(해당상품)
function reloadReviewAvg(){
    const url = "/api/userGoods/getAvgReviewRatingByGoodsId?goods_id=" + goodsId;
    fetch(url)
    .then(response => response.json())
    .then(response => {
        const avgRating = parseFloat(response.data.avg).toFixed(1);
        const averageRatingElements = document.querySelectorAll(".averageRating");

        averageRatingElements.forEach(averageRating => {
            averageRating.innerHTML = ''; 

            for (let i = 1; i <= 5; i++) {
                const avgStar = document.createElement('span');
                avgStar.className = 'avgStar';
                avgStar.innerText = i <= avgRating ? '★' : '☆'; 
                averageRating.appendChild(avgStar);
            }

            const avgText = document.createElement('span');
            avgText.className = 'avgText';
            avgText.innerText = avgRating; 
            averageRating.appendChild(avgText);
            
        });
    });
}

//리뷰좋아요 상태 변경
function toggleReviewLike(reviewId) {
    if (!reviewId) {
        console.error('Review ID is not defined');
        return;
    }

    if (myId == null) {
        if (confirm("로그인 하셔야 이용 가능합니다. 로그인 페이지로 이동하시겠습니까?")) {
            location.href = "/user/loginPage";
        } else {
            return;
        }
    }

    const url = `/api/userGoods/toggleReviewLike?review_id=${reviewId}`;
    fetch(url)
        .then(response => response.json())
        .then(response => {
            reloadReviewList();
        })
        .catch(error => console.error('Error:', error));
}

function deleteReview(id){
    const url = "/api/userGoods/deleteReview?id=" + id;

    fetch(url)
    .then(response => response.json())
    .then(response => {
        reloadReviewList();
        reloadReviewCount();
        reloadReviewAvg();
    });
}

// 물품 구매 함수
document.addEventListener("DOMContentLoaded", function() {
const minCount = 1;
const maxCount = 20;
const itemCountInput = document.getElementById("itemCount");
const decreaseBtn = document.getElementById("decreaseBtn");
const increaseBtn = document.getElementById("increaseBtn");
const priceDisplay = document.getElementById("priceDisplay");
const totalPriceSpan = document.getElementById("totalPrice");
const totalPriceSpan2 = document.getElementById("totalPrice2");

// HTML data 속성에서 단가 가져오기
const unitPrice = parseFloat(priceDisplay.getAttribute("data-unit-price")); 

// 수량 조절 함수
function adjustCount(amount) {
    let currentCount = parseInt(itemCountInput.value, 10);
    let newCount = Math.max(minCount, Math.min(maxCount, currentCount + amount));
    itemCountInput.value = newCount;
    updateTotalPrice(newCount);
}

// 총 가격 업데이트 함수
function updateTotalPrice(count){
    let totalPrice = count * unitPrice
    totalPriceSpan.textContent = totalPrice.toLocaleString() + '원'
    totalPriceSpan2.textContent = totalPrice.toLocaleString() + '원' //굿즈가격 * 갯수 + 배송비
}

updateTotalPrice(parseInt(itemCountInput.value, 10));

increaseBtn.addEventListener("click", () => adjustCount(1));
decreaseBtn.addEventListener("click", () => adjustCount(-1));

});


document.querySelectorAll(".goodsDetailText").forEach(function(element){
    element.addEventListener("click", function() {
        this.classList.toggle("text-decoration-underline");
        this.classList.toggle("fw-bold"); 
        this.classList.toggle("text-primary");
    });
});

function confirmAddToCart() {
    return confirm('쇼핑백에 상품을 담으시겠습니까?');
}

window.addEventListener("DOMContentLoaded", () => {
    setSessionId();
    reloadReviewList();
    reloadReviewCount();
    reloadReviewAvg();
    updateWishCount();
});

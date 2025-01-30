let myId = null;

function reloadUserGoodsList(){
    const url = "/api/userGoods/getUserGoodsList"
    fetch(url)
    .then(response => response.json())
    .then(response => {
        const userGoodsListBox = document.getElementById("userGoodsListBox");
        userGoodsListBox.innerHTML = "";

        const goodsWrapperTemplate  = document.querySelector("#template .goodsWrapper");
        
        response.data.goodsList.forEach(e => {
            const newGoodsWrapper = goodsWrapperTemplate.cloneNode(true);

            const goodsImage = newGoodsWrapper.querySelector(".goodsImage");
            goodsImage.src = `/images/${e.image}`;

            const goodsTitle = newGoodsWrapper.querySelector(".goodsTitle");
            goodsTitle.innerText = e.goods_title;

            const goodsPriceElement = newGoodsWrapper.querySelector(".goodsPrice");
            const goodsDiscountElement = newGoodsWrapper.querySelector(".goodsDiscount");

            let price = e.price;
            let discount = e.discount || 0;

            let discountedPrice = price * (1 - discount / 100);

            discountedPrice = Math.round(discountedPrice / 10) * 10;

            if (discount) {
                goodsDiscountElement.innerText = discount + "%";
            } else {
                goodsDiscountElement.innerText = "0%";
            }

            goodsPriceElement.innerText = discountedPrice.toLocaleString() + "원";

            const data = newGoodsWrapper.querySelector(".data");
            data.href=`/goods/goodsDetailPage?id=${e.id}`;

            // 위시 카운트
            fetch(`/api/userGoods/totalWishLikes?goods_id=${e.id}`)
                .then(response => response.json())
                .then(wishData => {
                    const goodsWishCountElement = newGoodsWrapper.querySelector(".goodsWishCount");
                    
                    const goodsWishCount = wishData.data.count;
                    goodsWishCountElement.innerText = goodsWishCount !== undefined ? goodsWishCount : '0';
                    
                })
                .catch(() => {
                    const goodsWishCountElement = newGoodsWrapper.querySelector(".goodsWishCount");
                    goodsWishCountElement.innerText = '0';

                    });

            // 리뷰 카운트 및 평점 가져오기
            fetch(`/api/userGoods/getGoodsReviewSummary?goods_id=${e.id}`)
                .then(response => response.json())
                .then(reviewData => {
                    const reviewCountElement = newGoodsWrapper.querySelector(".goodsReviewCount");
                    const avgRatingElement = newGoodsWrapper.querySelector(".goodsRatingCount");

                    //리뷰개수, 평점
                    const reviewCount = reviewData.data.reviewCount;
                    const avgRating = reviewData.data.avgRating;

                    if(reviewCount == 0){
                            reviewCountElement.innerText = '0';
                            avgRatingElement.innerText = '0.0';
                        } else {
                            reviewCountElement.innerText = reviewCount;
                            avgRatingElement.innerText = avgRating.toFixed(1);
                        }
                    })

                .catch(() => {
                    const reviewCountElement = newGoodsWrapper.querySelector(".goodsReviewCount");
                    const avgRatingElement = newGoodsWrapper.querySelector(".goodsRatingCount");
                    
                    reviewCountElement.innerText = '0';
                    avgRatingElement.innerText = '0.0';
                    });

            reloadHeart(e.id, newGoodsWrapper);
            userGoodsListBox.appendChild(newGoodsWrapper);

        });
    });
     
}

function setSessionId(){
        const url = "/api/userGoods/getSessionUserId";
        fetch(url)
        .then(response => response.json())
        .then((response) => {
            myId = response.data.id;
            reloadUserGoodsList();
        });
    }

function reloadHeart(goodsId, newGoodsWrapper) {
    
    const url = `/api/userGoods/isWishLiked?goods_id=${goodsId}`;
    const heart = newGoodsWrapper.querySelector(".heart");

    if (!heart) {
    console.error('Heart element not found');
    return;
    }


    fetch(url)
        .then(response => response.json())
        .then(response => { 
            if (response.result === 'fail' && response.redirectUrl) {
                // 로그인 안 된 경우 리디렉션 처리
                window.location.href = response.redirectUrl;
            } else{
            const isLiked = response.data.isWishLiked;
                if (isLiked) {
                    heart.classList.add("fill-1");
                } else {
                    heart.classList.remove("fill-1");
                }
                heart.onclick = () => toggleWish(goodsId, isLiked, heart);
            }
        })
        .catch(error => {
            console.error('Error loading heart status:', error);
        });

}

function toggleWish(goodsId, isLiked, heartElement) {
    
    if (myId == null) {
        if (confirm("로그인 하셔야 이용 가능합니다. \n로그인 페이지로 이동하시겠습니까?")) {
            location.href = "/user/loginPage";
        } else {
            return;
        }
    }

    const url = isLiked ? 
    `/api/userGoods/unWish?goods_id=${goodsId}` : 
    `/api/userGoods/wish?goods_id=${goodsId}`;

    if (isLiked) {
        heartElement.classList.remove("fill-1");
    } else {
        heartElement.classList.add("fill-1");
    }

    heartElement.onclick = () => toggleWish(goodsId, !isLiked, heartElement);
    updateWishCount(goodsId, isLiked ? -1 : 1, heartElement);
    fetch(url)
        .then(response => response.json())
        .then(response => {
            if (response.data.success) {
                updateWishCount(goodsId, isLiked ? -1 : 1, heartElement);
            }
        })
        .catch(error => {
            console.error('Error toggling wish:', error);
            if (isLiked) {
                heartElement.classList.add("fill-1");
            } else {
                heartElement.classList.remove("fill-1");
            }
        });
}

//하트 클릭시 위시카운트 수 업데이트
function updateWishCount(goodsId, change, heartElement){
    const goodsWishCountElement = heartElement.closest('.goodsWrapper').querySelector('.goodsWishCount');
    const currentCount = parseInt(goodsWishCountElement.innerText, 10) || 0;
    goodsWishCountElement.innerText = currentCount + change;
}

window.addEventListener("DOMContentLoaded", () => {
    setSessionId();
});

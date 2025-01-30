
const urlParams = new URL(window.location).searchParams;
const goodsId = urlParams.get("id");

function goodsListDetail(goodsId){
    const url = `/api/goods/getGoodsDetail?id=${goodsId}`;
    fetch(url)
    .then(response => response.json())
    .then((response) => {

        const goodsAll = response.data.goodsAll[0];
        const goodsDiscountAll = response.data.goodsDiscounts[0];

        if (!goodsAll) {
            console.error("No goods data found");
            return;
        }

        const goodsIdElement = document.getElementById("goodsId");
        if (goodsIdElement) {
            goodsIdElement.innerText = goodsAll.id;
        }

        const goodsCreatedAtElement = document.getElementById("goodsCreatedAt");
        if (goodsCreatedAtElement) {
            goodsCreatedAtElement.innerText = goodsAll.created_at ? new Date(goodsAll.created_at).toLocaleString() : "N/A";
        }

        const goodsCategoryElement = document.getElementById("goodsCategory");
        if (goodsCategoryElement) {
            goodsCategoryElement.innerText = goodsAll.goodsCategoryDto ? goodsAll.goodsCategoryDto.name : "N/A";
        }

        const goodsTitle = document.getElementById("goodsTitle");
        goodsTitle.innerText = goodsAll.goods_title;
        
        const goods_image = document.getElementById("goods_image");
        goods_image.src = `/images/${goodsAll.image}`;

        const formattedCount = new Intl.NumberFormat().format(goodsAll.goods_count);
        goodsCount.innerText = `${formattedCount} 개`;

        // 원가 설정
        const formattedPrice = new Intl.NumberFormat().format(goodsAll.price);
        document.getElementById("goodsPrice").innerText = `${formattedPrice} 원`;

        window.originalPrice = goodsAll.price;
        window.goodsId = goodsId; 

        const goodsDiscountElement = document.getElementById("goodsDiscount");
        const discountInput = document.getElementById("discountInput");
        const applyDiscountBtn = document.getElementById("applyDiscountBtn");

        if (goodsDiscountElement) {
            if (goodsDiscountAll && goodsDiscountAll.discount) {
                goodsDiscountElement.innerText = `${goodsDiscountAll.discount} %`;
                applyDiscount(goodsDiscountAll.discount);

                discountInput.disabled = true;
                applyDiscountBtn.disabled = true;
                applyDiscountBtn.innerText = "할인율 이미 등록됨";
            } else {
                goodsDiscountElement.innerText = "0 %";
                discountInput.disabled = false;
                applyDiscountBtn.disabled = false;
                applyDiscountBtn.innerText = "할인율 등록";
            }
        }

        const goodsCreated_at = document.getElementById("goodsCreated_at");
        goodsCreated_at.innerText = goodsAll.created_at;

        const goodsCreated_atElement = document.getElementById("goodsCreated_at");
        if (goodsCreated_atElement) {
            goodsCreated_atElement.innerText = goodsAll.created_at ? new Date(goodsAll.created_at).toLocaleString() : "N/A";
        }

        const goodsDetail = document.getElementById("goodsDetail");
        goodsDetail.innerText = goodsAll.detail;

        const ImagesWrapper = document.querySelector(".ImagesWrapper");
        const goodsDetailImageContainer = document.getElementById("goods_detailImageContainer");

        if (goodsDetailImageContainer) {
            const goodsDetailImageDtoList = goodsAll.goodsDetailImageDtoList;
        
            if (goodsDetailImageDtoList && goodsDetailImageDtoList.length > 0) {
                goodsDetailImageDtoList.forEach(item => {
                    const imgElement = document.createElement('img');
                    imgElement.src = `/images/${item.multiple_image}`;
                    imgElement.style.marginRight = '10px';
                    goodsDetailImageContainer.appendChild(imgElement);
                });
            } else {
                imagesWrapper.style.display = 'none';
                goodsDetailImageContainer.innerText = "등록된 이미지가 없습니다.";
                goodsDetailImageContainer.style.marginTop = '5px';
            }
        }

    })
    .catch(error => {
        console.error("Error fetching goods details:", error);
    });
}

// 할인율 적용 함수
function applyDiscount(discount) {
    if (window.originalPrice === undefined) {
        console.error('원가가 설정되지 않았습니다.');
        return;
    }
const discountedPrice = window.originalPrice * (1 - (discount / 100));
const roundedDiscountedPrice = Math.round(discountedPrice / 10) * 10;
const formattedDiscountedPrice = roundedDiscountedPrice.toLocaleString();
  
document.getElementById("finalPrice").innerText = `${formattedDiscountedPrice} 원`;
}

// 할인율 입력 및 등록 버튼 클릭 처리
document.getElementById("applyDiscountBtn").addEventListener("click", () => {
    const discountInput = document.getElementById("discountInput").value;
    const discount = parseFloat(discountInput);

    if (isNaN(discount) || discount < 0 || discount > 100) {
        alert('유효한 할인율을 입력하세요.');
        return;
    }

    const url = '/api/goods/discounts';
    const data = {
        goods_id: window.goodsId,
        discount: discount
    };

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then((response) => {
        if (response.result === 'success') {
            alert('할인율이 등록되었습니다.');
            goodsListDetail(window.goodsId); 
        } else {
            alert('할인율 등록에 실패했습니다.');
        }
    })
    .catch(error => {
        console.error("Error applying discount:", error);
    });
});

window.addEventListener("DOMContentLoaded", () => {
    goodsListDetail(goodsId);
});

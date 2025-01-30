
let myId = null;

function setSessionId(){
    const url = "/api/userGoods/getSessionUserId";
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        myId = response.data.id;
        reloadWishList();
    });
}

function reloadWishList(){

    if(!myId){
        alert("로그인 후 이용가능합니다.");
        return;
    }

    console.log(myId);

    const url = `/api/userGoods/getWishList?user_id=${myId}`;

    fetch(url)
    .then(response => response.json())
    .then(response => {

        const wishListBox = document.getElementById("wishListBox");
        wishListBox.innerHTML = "";

        const wishWrapperTemplate = document.querySelector("#wishTemplate .col-4");

            if (response.data.wishList.length === 0) {
                wishListBox.innerHTML = `
                    <div class="col-12 text-center py-5 new-fs-9">
                        위시리스트에 담은 상품이 없습니다.
                    </div>
                `;
            } else {

                response.data.wishList.forEach(e => {
                const newWishWrapper = wishWrapperTemplate.cloneNode(true);

                const wishImage = newWishWrapper.querySelector(".wishImage")
                wishImage.src = `/images/${e.goodsDto.image}`;

                const wishTitle = newWishWrapper.querySelector(".wishTitle");
                wishTitle.innerText = e.goodsDto.goods_title;

                const discount = newWishWrapper.querySelector(".discount");
                let discountRate = 0;
                
                if (e.goodsDiscountList.length > 0) {
                    discountRate = e.goodsDiscountList[0].discount;
                    discount.innerText = `${discountRate}%`;
                } else {
                    discount.innerText = '0%';
                }

                const discountedPrice = parseFloat(e.goodsDto.price) * (1 - discountRate / 100);
                
                const formattedWishPrice = `${discountedPrice.toLocaleString('en-US')} 원`;

                const wishPrice = newWishWrapper.querySelector(".wishPrice");
                wishPrice.innerText = formattedWishPrice;

                const goodsId = newWishWrapper.querySelector(".goodsId");
                goodsId.href = '/goods/goodsDetailPage?id=' + e.goodsDto.id;

                wishListBox.appendChild(newWishWrapper);
            });
        }
    })
    .catch(error => {
        console.error('Error fetching wishlist:', error);
    });
}

window.addEventListener("DOMContentLoaded", () => {
        setSessionId();
    });


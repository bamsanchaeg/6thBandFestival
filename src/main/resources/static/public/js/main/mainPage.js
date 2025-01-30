//슬라이드 함수, 분석 필요
document.addEventListener("DOMContentLoaded", function () {
    // Festival 이미지 슬라이드 인 처리
    const festivalImages = document.querySelectorAll('.img-fluid-festival');
    const slideElements = document.querySelectorAll('.slide-in-elementFestival');

    festivalImages.forEach(function (img) {
        img.addEventListener('load', function () {
            img.classList.add('img-slide-in');
            slideInTextElements();
        });

        // 만약 이미지가 캐시로 인해 이미 로드된 상태라면
        if (img.complete) {
            img.classList.add('img-slide-in');
            slideInTextElements();
        }
    });

    function slideInTextElements() {
        slideElements.forEach(function (element, index) {
            setTimeout(function () {
                element.classList.add('slide-in-active');
            }, (index + 1) * 500); // 각 요소가 순차적으로 0.5초 간격으로 슬라이드되게 설정
        });
    }

    // 일반 이미지 슬라이드 인 처리
    const catImages = document.querySelectorAll('.img-Cat');

    const observer = new IntersectionObserver((entries, observer) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('img-slide-in');
                observer.unobserve(entry.target); // 애니메이션이 한 번만 실행되도록 옵저버 해제
            }
        });
    }, {
        threshold: 0.1 // 요소가 10% 이상 보일 때 애니메이션 트리거
    });

    catImages.forEach(image => {
        observer.observe(image); // 각 이미지를 옵저버에 등록
    });
});


function getInformationGoodsAndRentalItems() {
    const url = "api/main/getProductInformation";
    fetch(url)
        .then(response => response.json())
        .then(response => {

            const goods = response.data.goodsList;
            const rentalItem = response.data.rentalItems;

            //굿즈리스트
            const goodsListBox = document.getElementById("goodsListBox");
            goodsListBox.innerHTML = "";

            const goodsWrapperTemplete = document.querySelector("#templeteGoods .goodsListWrapper");

            //렌탈아이템 리스트
            const rentalItemListBox = document.getElementById("rentalItemListBox");
            rentalItemListBox.innerHTML = "";

            const rentalItemWrapperTemplete = document.querySelector("#templeteRental .rentalItemWrapper");

            for (let e of goods) {

                const newGoodsWrapper = goodsWrapperTemplete.cloneNode(true);

                //굿즈 아이디
                const goodsId = newGoodsWrapper.querySelector(".goodsId");
                goodsId.href = '/goods/goodsDetailPage?id=' + e.id;

                const goodsTitle = newGoodsWrapper.querySelector(".goodsTitle");
                goodsTitle.innerText = e.goods_title;

                const goodsImage = newGoodsWrapper.querySelector(".goodsImage");
                goodsImage.src = '/images/' + e.image;

                const goodsPrice = newGoodsWrapper.querySelector(".goodsPrice");
                goodsPrice.innerText = e.price.toLocaleString();


                goodsListBox.appendChild(newGoodsWrapper);


            }

            for (let rental of rentalItem) {

                const newRentalWrapper = rentalItemWrapperTemplete.cloneNode(true);

                //굿즈 아이디
                const itemId = newRentalWrapper.querySelector(".itemId");
                itemId.href = '/rental/details?id=' + rental.id;

                const itemTitle = newRentalWrapper.querySelector(".itemTitle");
                itemTitle.innerText = rental.title;

                const itemImage = newRentalWrapper.querySelector(".itemImage");
                itemImage.src = '/images/' + rental.image;

                const itemPrice = newRentalWrapper.querySelector(".itemPrice");
                itemPrice.innerText = rental.price.toLocaleString();

                rentalItemListBox.appendChild(newRentalWrapper);

            }

        })
}

window.addEventListener("DOMContentLoaded", () => {
    getInformationGoodsAndRentalItems();
})

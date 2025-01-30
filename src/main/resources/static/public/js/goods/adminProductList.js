function reloadGoodsList(){
    const url = "/api/goods/getGoodsList";
    fetch(url)
    .then(response => response.json())
    .then(response => {

        const goodsListBox = document.getElementById("goodsListBox");
        goodsListBox.innerHTML = "";

        const goodsWrapperTemplate = document.querySelector("#template .goodsWrapper");

        for(e of response.data.goodsList){
            const newGoodsWrapper = goodsWrapperTemplate.cloneNode(true);

            const goodsId = newGoodsWrapper.querySelector(".goodsId");
            goodsId.innerText = e.goodsDto.id;

            const goodsName = newGoodsWrapper.querySelector(".goodsName");
            goodsName.innerText = e.goodsCategoryDto.name;

            const goodsTitle = newGoodsWrapper.querySelector(".goodsTitle");
            goodsTitle.innerText = e.goodsDto.goods_title;

            const goodsImage = newGoodsWrapper.querySelector(".goodsImage");
            goodsImage.src = `/images/${e.goodsDto.image}`;

            const goodsCount = newGoodsWrapper.querySelector(".goodsCount");
            goodsCount.innerText = e.goodsDto.goods_count.toLocaleString() + "개";

            const goodsPrice = newGoodsWrapper.querySelector(".goodsPrice");
            goodsPrice.innerText = e.goodsDto.price.toLocaleString() + "원";

            const goodsCreatedAt = newGoodsWrapper.querySelector(".goodsCreatedAt");
            const date = new Date(e.goodsDto.created_at);
            goodsCreatedAt.innerText = `${date.getFullYear()}.${date.getMonth()+1}.${date.getDate()}`;

            const goodsDetailLink = newGoodsWrapper.querySelector(".goodsDetailLink");
            goodsDetailLink.href=`/admin/goods/adminProductDetail?id=${e.goodsDto.id}`

            const goodsDetail = newGoodsWrapper.querySelector(".goodsDetail");
            goodsDetail.onclick= () => {reloadGoodsList(e.goodsDto.id)}; 

            const goodsButton = newGoodsWrapper.querySelector(".goodsButton");
            goodsButton.classList.remove("hidden");

            const buttonDelete = newGoodsWrapper.querySelector(".buttonDelete");
            buttonDelete.setAttribute("onclick", `deleteGoods(${e.goodsDto.id})`);

            goodsListBox.appendChild(newGoodsWrapper);
        }
    })
    ;
}

function deleteGoods(id){
    const url = "/api/goods/deleteGoodsId?id=" + id;

    fetch(url)
    .then(response => response.json())
    .then(response => {
        reloadGoodsList();
        countGoods();
    })
    ;
}

function countGoods(){
    const url = "/api/goods/countGoods";

    fetch(url)
    .then(response => response.json())
    .then(response => {
        document.getElementById("goodsCountDisplay").innerText = response.data.countGoods
        reloadGoodsList();
    })
    ;
}

window.addEventListener("DOMContentLoaded", () => {
    reloadGoodsList();
    countGoods();
});

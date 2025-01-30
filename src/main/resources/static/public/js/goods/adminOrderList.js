function reloadOrderList(pageNumber){

    if(!pageNumber) {
        pageNumber = 1;
    }

    renderPageNumbers(pageNumber);

    const url = "/api/goods/getOrderList?p=" + pageNumber;
    fetch(url)
    .then(response => response.json())
    .then(response => {
        const orderListBox = document.getElementById("orderListBox");
        orderListBox.innerHTML = "";

        const orderWrapperTemplate = document.querySelector("#template .orderWrapper");

        for(let e of response.data.orderList){
            const newOrderWrapper = orderWrapperTemplate.cloneNode(true);

            const orderDetailLink = newOrderWrapper.querySelector(".orderDetailLink");
            orderDetailLink.href=`/admin/goods/adminOrderListDetail?order_id=${e.id}`

            const orderDetail = newOrderWrapper.querySelector(".orderDetail");
            orderDetail.onclick= () => {openOrderListDetail(e.id)}; 
            const orderId = newOrderWrapper.querySelector(".orderId");
            orderId.innerText = e.id;

            const accountName = newOrderWrapper.querySelector(".accountName");
            accountName.innerText = e.user_name;

            const orderGoodsId = newOrderWrapper.querySelector(".orderGoodsId");
            orderGoodsId.innerText = e.goods_id;

            const orderCount = newOrderWrapper.querySelector(".orderCount");
            const formattedCount = new Intl.NumberFormat().format(e.goods_order_count);
            orderCount.innerText = `${formattedCount} 개`;

            const orderPrice = newOrderWrapper.querySelector(".orderPrice");
            const formattedPrice = new Intl.NumberFormat().format(e.order_price);
            orderPrice.innerText = `${formattedPrice} 원`;

            const orderAddress = newOrderWrapper.querySelector(".orderAddress");
            orderAddress.innerText = e.address;

            const orderCreatedAt = newOrderWrapper.querySelector(".orderCreatedAt");
            orderCreatedAt.innerText = e.created_at;

            const orderStatus = newOrderWrapper.querySelector(".orderStatus");
            const statusCircle = orderStatus.querySelector(".statusCircle");
            const statusText = orderStatus.querySelector(".statusText");
        
            const status = e.order_status;
        
            statusText.innerText = status;
        
            switch (status) {
                case '결제완료':
                    statusCircle.style.backgroundColor = 'orangered';
                    break;
                case '배송준비중':
                    statusCircle.style.backgroundColor = 'forestgreen';
                    break;
                case '배송중':
                    statusCircle.style.backgroundColor = 'gold';
                    break;
                case '배송완료':
                    statusCircle.style.backgroundColor = 'darkgray';
                    break;
                default:
                    statusCircle.style.backgroundColor = 'gray';
                    break;
            }

            orderListBox.appendChild(newOrderWrapper);
        
        }
    })

    .catch(error => {
        console.error("Error reloading order list data", error);
    });

}

function orderCount(){
    const url = "/api/goods/orderCount";

    fetch(url)
    .then(response => response.json())
    .then(response => {
        document.getElementById("orderCountDisplay").innerText = response.data.orderCount
    })
    ;
}


function renderPageNumbers(pageNumber) {

    if(!pageNumber) {
        pageNumber = 1;
    }

    const url = "/api/goods/getStartAndEndPageNumber";

    fetch(url)
    .then(response => response.json())
    .then(response => {
        console.log(response);

        document.querySelector("#paginationWrapper").innerHTML = "";

        const newPrevPageItem = document.querySelector("#template .page-item").cloneNode(true);
        newPrevPageItem.querySelector(".page-link").innerText = "<";
        document.querySelector("#paginationWrapper").appendChild(newPrevPageItem);

        if(response.data.startPage <= 1){
            newPrevPageItem.querySelector(".page-link").classList.add("disabled");
        }else{
            newPrevPageItem.querySelector(".page-link").setAttribute("onclick", `reloadOrderList(${response.data.startPage - 1})`);
        }

        for(let i = response.data.startPage ; i <= response.data.endPage ; i++) {
            const newPageItem = document.querySelector("#template .page-item").cloneNode(true);
            newPageItem.querySelector(".page-link").innerText = i;
            newPageItem.querySelector(".page-link").setAttribute("onclick", `reloadOrderList(${i})`);
            document.querySelector("#paginationWrapper").appendChild(newPageItem);

            if(pageNumber == i) {
                newPageItem.classList.add("active");
            }


        }

        const newNextPageItem = document.querySelector("#template .page-item").cloneNode(true);
        newNextPageItem.querySelector(".page-link").innerText = ">";
        document.querySelector("#paginationWrapper").appendChild(newNextPageItem);

        if(response.data.endPage >= response.data.lastPageNumber){
            newNextPageItem.querySelector(".page-link").classList.add("disabled");
        }else{
            newNextPageItem.querySelector(".page-link").setAttribute("onclick", `reloadOrderList(${response.data.endPage + 1})`);
        }

    })
    ;

}


window.addEventListener("DOMContentLoaded", () => {
    renderPageNumbers();
    reloadOrderList(1);
    orderCount();
});



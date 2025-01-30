
const urlParams = new URL(window.location).searchParams;
const orderId = urlParams.get("order_id");

function orderListDetail(order_id){
    const url = `/api/goods/getOrderListDetail?order_id=${orderId}`;
    fetch(url)
    .then(response => response.json())
    .then((response) => {

        const orderDetail = response.data.orderListDetail;

        const orderDetailId = document.getElementById("orderDetailId");
        orderDetailId.innerText = orderDetail.id;

        const orderCreatedAt = document.getElementById("orderCreatedAt");
        orderCreatedAt.innerText = orderDetail.created_at;

        const orderUserName = document.getElementById("orderUserName");
        orderUserName.innerText = orderDetail.user_name;

        const receiver = document.getElementById("receiver");
        receiver.innerText = orderDetail.receiver;

        const address = document.getElementById("address");
        address.innerText = orderDetail.address;

        const phone = document.getElementById("phone");
        phone.innerText = orderDetail.phone;

        const order_status = document.getElementById("order_status");
        order_status.innerText = orderDetail.order_status;

        const goods_title = document.getElementById("goods_title");
        goods_title.innerText = orderDetail.goodsDto.goods_title;

        const goods_image = document.getElementById("goods_image");
        goods_image.src = `/images/${orderDetail.goodsDto.image}`;

        const goods_order_count = document.getElementById("goods_order_count");
        goods_order_count.innerText = orderDetail.goods_order_count;

        const order_price = document.getElementById("order_price");
        order_price.innerText = `${orderDetail.order_price.toLocaleString()} 원`;

        const payment_info = document.getElementById("payment_info");
        payment_info.innerText = orderDetail.payment_info;

        const detail = document.getElementById("detail");
        detail.innerText = orderDetail.request_details;

    })
    .catch(error => {
        console.error("Error fetching order details:", error);
    });
}

function updateOrderStatus(){
    const selectedStatus = document.getElementById("statusSelect").value;
    const orderId = urlParams.get("order_id");

    const url ="/api/goods/updateOrderStatus";
    fetch(url, {
        method: "post",
        headers: {
            "Content-Type" : "application/x-www-form-urlencoded"
        },
        body: `id=${orderId}&order_status=${selectedStatus}`
    })
    .then(response => response.json())
    .then((response) => {
        alert("주문상태 변경을 완료하였습니다.");
        document.getElementById("order_status").innerText = selectedStatus;
    })
    .catch(error => {
        console.error("Error updating order status:", error);
        alert("주문상태 변경에 실패했습니다. 다시 시도해 주세요.");
    });
}

window.addEventListener("DOMContentLoaded", () => {
    orderListDetail();
});

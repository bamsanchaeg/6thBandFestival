// 티켓 구매내역 모달
function bookingInfoModal(booking_id) {
    const url = "/six/user/getMyBookingInfo?id=" + booking_id
    fetch(url)
        .then(response => response.json())
        .then((response) => {
            const myTicketQrCode = document.getElementById("myTicketQrCode");
            const qrUrl = `https://festival.null-pointer-exception.com/ticket/qrcodeCheckPage?id=${booking_id}`
            myTicketQrCode.src = `https://api.qrserver.com/v1/create-qr-code/?data=${qrUrl}&size=150x150`

            const myTicketUuidModalSpan = document.getElementById("myTicketUuidModalSpan");
            myTicketUuidModalSpan.innerText = response.data.bookingInfo.booking_id;

            const myTicketBookingDayModalSpan = document.getElementById("myTicketBookingDayModalSpan");
            const bookingDay = response.data.bookingInfo.booking_day.substring(0, 10);
            myTicketBookingDayModalSpan.innerText = bookingDay;

            const myTicketBuyDayModalSpan = document.getElementById("myTicketBuyDayModalSpan");
            const buyDay = response.data.bookingInfo.buy_day.substring(0, 10);
            myTicketBuyDayModalSpan.innerText = buyDay;

            const myTicketAdultQuantityModalSpan = document.getElementById("myTicketAdultQuantityModalSpan");
            myTicketAdultQuantityModalSpan.innerText = response.data.bookingInfo.adult_quantity;

            const myTicketYouthQuantityModalSpan = document.getElementById("myTicketYouthQuantityModalSpan");
            myTicketYouthQuantityModalSpan.innerText = response.data.bookingInfo.youth_quantity;

            const isUse = response.data.bookingInfo.is_use;

            const myTicketIsUseModalSpan = document.getElementById("myTicketIsUseModalSpan");
            if (isUse == 'Y') {
                myTicketIsUseModalSpan.innerText = '관람 완료'
            } else {
                myTicketIsUseModalSpan.innerText = '미관람'
            }

            const myBookingInfoModal = bootstrap.Modal.getOrCreateInstance("#myBookingInfoModal");
            myBookingInfoModal.show();
        });

}
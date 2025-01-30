// 구매 상세 오프캔버스
function bookingInfoOffCanvas(bookingId) {
    const url = `/api/admin/ticket/getBookingAdmin?id=${bookingId}`

    fetch(url)
        .then(response => response.json())
        .then(response => {
            const bookingUserProfileImg = document.getElementById("bookingUserProfileImg");
            bookingUserProfileImg.src = `/images/${response.data.bookingInfo.profile_img}`

            const bookingUserNameSpan = document.getElementById("bookingUserNameSpan");
            bookingUserNameSpan.innerText = response.data.bookingInfo.name;

            const bookingUserPhoneSpan = document.getElementById("bookingUserPhoneSpan");
            const e = response.data.bookingInfo;
            bookingUserPhoneSpan.innerText = e.phone.substring(0, 3) + '-' + e.phone.substring(3, 7) + '-' + e.phone.substring(7);

            const bookingUserEmailSpan = document.getElementById("bookingUserEmailSpan");
            bookingUserEmailSpan.innerText = response.data.bookingInfo.email;

            const bookingTicketNumberSpan = document.getElementById("bookingTicketNumberSpan");
            bookingTicketNumberSpan.innerText = response.data.bookingInfo.id;

            const bookingTicketTypeSpan = document.getElementById("bookingTicketTypeSpan");
            bookingTicketTypeSpan.innerText = response.data.bookingInfo.day_type;

            const bookingTicketDaySpan = document.getElementById("bookingTicketDaySpan");
            bookingTicketDaySpan.innerText = bookingDateFormat(response.data.bookingInfo.booking_day);

            const bookingTicketAdultCountSpan = document.getElementById("bookingTicketAdultCountSpan");
            bookingTicketAdultCountSpan.innerText = response.data.bookingInfo.adult_quantity;

            const bookingTicketYouthCountSpan = document.getElementById("bookingTicketYouthCountSpan");
            bookingTicketYouthCountSpan.innerText = response.data.bookingInfo.youth_quantity;

            const bookingTicketBuyDaySpan = document.getElementById("bookingTicketBuyDaySpan");
            bookingTicketBuyDaySpan.innerText = bookingDateFormat(response.data.bookingInfo.created_at);

            const bookingTicketTotalPriceSpan = document.getElementById("bookingTicketTotalPriceSpan");
            bookingTicketTotalPriceSpan.innerText = response.data.bookingInfo.total_price.toLocaleString();

            const bookingTicketIsPaymentSpan = document.getElementById("bookingTicketIsPaymentSpan");
            if (response.data.bookingInfo.payment_status === 'Y') {
                bookingTicketIsPaymentSpan.innerText = "결제 완료됨"
            } else {
                bookingTicketIsPaymentSpan.innerText = "결제 실패함"
            }

            const bookingTicketIsUseSpan = document.getElementById("bookingTicketIsUseSpan");
            if (response.data.bookingInfo.is_use === 'Y') {
                bookingTicketIsUseSpan.innerText = "사용 완료됨"
            } else {
                bookingTicketIsUseSpan.innerText = "사용 대기중"
            }

        })
    const bookingInfoOffcanvas = bootstrap.Offcanvas.getOrCreateInstance("#bookingInfoOffcanvas");
    bookingInfoOffcanvas.show();
}

function bookingDateFormat(bookingDay) {
    const bookingDate = new Date(bookingDay);

    let day = bookingDate.getDate();
    let month = bookingDate.getMonth() + 1;
    // .toString().slice(-2) 연도의 마지막 2자리뽑기
    let year = bookingDate.getFullYear().toString().slice(-2);

    // 두 자릿수로 맞추기 위해 day와 month가 10보다 작을 경우 앞에 0을 추가
    day = day < 10 ? '0' + day : day;
    month = month < 10 ? '0' + month : month;

    return `${year}.${month}.${day}`;
}
// window.addEventListener("DOMContentLoaded", () => {
//
// });
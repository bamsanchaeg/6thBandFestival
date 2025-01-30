const urlParams = new URL(window.location.href).searchParams;
const bookingId = urlParams.get("id");

// QR code 리로드
function reloadQrCheck() {
    const url = `/api/admin/ticket/qrCheck?id=${bookingId}`
    fetch(url)
        .then(response => response.json())
        .then(response => {
            const bookingQrImg = document.getElementById("bookingQrImg");
            const qrUrl = `https://festival.null-pointer-exception.com/ticket/qrcodeCheckPage?id=${bookingId}`
            bookingQrImg.src = `https://api.qrserver.com/v1/create-qr-code/?data=${qrUrl}&size=150x150`

            const bookingNameSpan = document.getElementById("bookingNameSpan");
            bookingNameSpan.innerText = response.data.qrInfo.name;

            const bookingPhoneSpan = document.getElementById("bookingPhoneSpan");
            bookingPhoneSpan.innerText = response.data.qrInfo.phone.substring(0, 3) + '-' + response.data.qrInfo.phone.substring(3, 7) + '-' + response.data.qrInfo.phone.substring(7);

            const bookingCheckButton = document.getElementById("bookingCheckButton");

            if(response.data.qrInfo.is_use == "N") {
                bookingCheckButton.innerText = "입장 확인";
            } else {
                bookingCheckButton.classList.add("disabled")
                bookingCheckButton.innerText = "입장 완료된 티켓입니다."
            }

            const bookingInfoSpan = document.getElementById("bookingInfoSpan");
            if(response.data.qrInfo.day_type == "AllDay") {
                bookingInfoSpan.innerText = `${response.data.qrInfo.festival_name} - ${response.data.qrInfo.day_type} Ticket`;
            } else {
                const bookingDay = dateFormatQr(response.data.qrInfo.booking_day);
                bookingInfoSpan.innerText = `${response.data.qrInfo.festival_name} - ${bookingDay} Ticket`;
            }

        });
}

function dateFormatQr(formatDay) {
    let bookingDate = new Date(formatDay);

    let day = bookingDate.getDate();
    let month = bookingDate.getMonth() + 1;

    // 두 자릿수로 맞추기 위해 day와 month가 10보다 작을 경우 앞에 0을 추가
    day = day < 10 ? '0' + day : day;
    month = month < 10 ? '0' + month : month;

    return `${month}.${day}`;
}

function entryCheck() {
    const url =  `/api/ticket/updateIsUse?id=${bookingId}`;

    fetch(url)
        .then(response => response.json())
        .then(response => {
            reloadQrCheck();
        });
}

window.addEventListener("DOMContentLoaded", () => {
    reloadQrCheck();
});
let myId = null;

// 아이디 세팅
function setSessionId() {
    const url = "/api/ticket/getSessionUserId";

    fetch(url)
        .then(response => response.json())
        .then((response) => {
            myId = response.data.id;
        });
}

// 로그인 체크
function loginCheck () {
    if(myId == null) {
        if(confirm("로그인 후 이용 가능합니다. 로그인 페이지로 이동하시겠습니까?")) {
            location.href="/user/loginPage";
        }
        return;
    }
}

// 토글로 아이콘 바뀌게
function showCollapseOneDay() {
    const collapseElement = document.getElementById("oneDayTicket");
    const collapseIconOneDay = document.getElementById("collapseIconOneDay");
    const collapseIconOneDayDown = document.getElementById("collapseIconOneDayDown");
    const isCollapsed = collapseElement.classList.contains('show');

    // bootstrap.Collapse.getInstance 모달, 오프캔버스 사용법과 같음!! 이렇게 하면 애니메이션 유지 가능
    if (isCollapsed) {
        bootstrap.Collapse.getInstance(collapseElement).hide();
        collapseIconOneDay.classList.remove('d-none');
        collapseIconOneDayDown.classList.add('d-none');
    } else {
        bootstrap.Collapse.getOrCreateInstance(collapseElement).show();
        collapseIconOneDay.classList.add('d-none');
        collapseIconOneDayDown.classList.remove('d-none');
    }
}

function showCollapseAllDay() {
    const collapseElement = document.getElementById("threeDayTicket");
    const collapseIconOneDay = document.getElementById("collapseIconAllDay");
    const collapseIconOneDayDown = document.getElementById("collapseIconAllDayDown");
    const isCollapsed = collapseElement.classList.contains('show');

    if (isCollapsed) {
        bootstrap.Collapse.getInstance(collapseElement).hide();
        collapseIconOneDay.classList.remove('d-none');
        collapseIconOneDayDown.classList.add('d-none');
    } else {
        bootstrap.Collapse.getOrCreateInstance(collapseElement).show();
        collapseIconOneDay.classList.add('d-none');
        collapseIconOneDayDown.classList.remove('d-none');
    }
}

// url에서 id값 가져오기
const urlParams = new URL(window.location.href).searchParams;
const ticketId = urlParams.get("id");
const InfoUrl = "/api/ticket/getTicketInfo?id=" + ticketId;

let adultQuantity = 0;
let youthQuantity = 0;
const maxTotalQuantity = 2;

const remainingQuantityElement = document.getElementById('remainingQuantity');
const remainingQuantity = parseInt(remainingQuantityElement.getAttribute('data-quantity'));

let dayType = null;
let bookingDay = 0;
let adultPrice = 0;
let youthPrice = 0;
let totalTicketPrice = 0;

let discount = 0;
let earlyBirdCount = 0;
let earlyBirdRemainingCount = 0;

function getPrice() {
    fetch(InfoUrl)
        .then(response => response.json())
        .then((response) => {
            adultPrice = response.data.ticketInfo.adult_price;
            youthPrice = response.data.ticketInfo.youth_price;
            dayType = response.data.ticketInfo.day_type;

            // js에서 날짜 변환
            const ticketDay = response.data.ticketInfo.booking_day;

            const date = new Date(ticketDay);
            const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
            const day = String(date.getDate()).padStart(2, '0');

            // 형식 지정
            bookingDay = `${month}/${day}`;

            if(response.data.ticketInfo.remaining_quantity != 0) {
                const isEarlyBird = document.getElementById("isEarlyBird");
                isEarlyBird.classList.remove("d-none")

                const earlyBirdDiscountRemainingQuantity = document.getElementById("earlyBirdDiscountRemainingQuantity");
                earlyBirdDiscountRemainingQuantity.innerText = response.data.ticketInfo.remaining_quantity;

                earlyBirdRemainingCount = response.data.ticketInfo.remaining_quantity
                discount = response.data.ticketInfo.discount;
            } else {
                discount = 0;
            }
        });
}

function updateQuantity() {
    document.getElementById('adultQuantity').textContent = adultQuantity;
    document.getElementById('youthQuantity').textContent = youthQuantity;

    if (discount != 0) {
        earlyBirdCount = youthQuantity + adultQuantity
    }
    updateTotalPrice();
    buttonCheck();
}

function updateTotalPrice() {

    if(discount == 0) {
        totalTicketPrice = (adultQuantity * adultPrice) + (youthQuantity * youthPrice);
        document.getElementById('totalPrice').textContent = totalTicketPrice.toLocaleString();
    } else {
        totalTicketPrice = ((adultQuantity * adultPrice) + (youthQuantity * youthPrice)) * (1 - discount);
        document.getElementById('totalPrice').textContent = totalTicketPrice.toLocaleString();
        document.getElementById('totalPrice').classList.add("text-primary");
        document.getElementById('totalPrice').classList.add("fw-bold");
    }

}

function adultMinus() {
    if (adultQuantity > 0) {
        adultQuantity--;
        updateQuantity();
    }
}

function adultPlus() {

    if (discount != 0) {
        if ((adultQuantity + youthQuantity) >= earlyBirdRemainingCount) {
            alert(`얼리버드 티켓의 남은 수량은 ${earlyBirdRemainingCount}장입니다.`);
        } else if (adultQuantity + youthQuantity < maxTotalQuantity) {
            adultQuantity++;
            updateQuantity();
        } else if (adultQuantity + youthQuantity >= maxTotalQuantity) {
            alert("티켓 구매는 최대 2장까지 가능합니다.");
        }
    } else {
        if ((adultQuantity + youthQuantity) >= remainingQuantity) {
            alert(`SOLD OUT! 
남은 티켓 수 : ${remainingQuantity}`);
        } else if (adultQuantity + youthQuantity < maxTotalQuantity) {
            adultQuantity++;
            updateQuantity();
        } else if (adultQuantity + youthQuantity >= maxTotalQuantity) {
            alert("티켓 구매는 최대 2장까지 가능합니다.");
        }
    }
}

function youthMinus() {
    if (youthQuantity > 0) {
        youthQuantity--;
        updateQuantity();
    }
}

function youthPlus() {

    if (discount != 0) {
        if ((adultQuantity + youthQuantity) >= earlyBirdRemainingCount) {
            alert(`얼리버드 티켓의 남은 수량은 ${earlyBirdRemainingCount}장입니다.`);
        } else if (adultQuantity + youthQuantity < maxTotalQuantity) {
            youthQuantity++;
            updateQuantity();
        } else if (adultQuantity + youthQuantity >= maxTotalQuantity) {
            alert("티켓 구매는 최대 2장까지 가능합니다.");
        }
    } else {
        if ((adultQuantity + youthQuantity) >= remainingQuantity) {
            alert(`SOLD OUT! 
남은 티켓 수 : ${remainingQuantity}`);
        } else if (adultQuantity + youthQuantity < maxTotalQuantity) {
            youthQuantity++;
            updateQuantity();
        } else if (adultQuantity + youthQuantity >= maxTotalQuantity) {
            alert("티켓 구매는 최대 2장까지 가능합니다.");
        }
    }


}

// 티켓 구매 생성
function createBooking() {
    const url = "/api/ticket/createBooking";
    // console.log("adultQuantity : " + adultQuantity);
    // console.log("youthQuantity : " + youthQuantity);
    // console.log("totalTicketPrice : " + totalTicketPrice);
    // console.log("myId : " + myId);
    // console.log("ticketId : " + ticketId);
    // console.log("day_type : " + dayType);
    // console.log("bookingDay : " + bookingDay);

    if (myId == null) {
        return;
    }

    const isBooking = confirm(
        `[${dayType}] 티켓을 구매하시겠습니까?
adult Ticket : ${adultQuantity}, youth Ticket : ${youthQuantity}
총 가격 : ${totalTicketPrice.toLocaleString()} 원`
    );

    if (!isBooking) {
        return;
    }

    fetch(url, {
        method: "post",
        headers: {
            "Content-Type" : "application/x-www-form-urlencoded"
        },
        body: `user_id=${myId}&info_id=${ticketId}&adult_quantity=${adultQuantity}&youth_quantity=${youthQuantity}&total_price=${totalTicketPrice}`
    })
        .then(response => response.json())
        .then(response => {
            console.log("구매완료");
            bookingCheckModalOpen();
            updateRemainingQuantity();

            if (isMobile()) {
                location.href = response.data.next_redirect_mobile_url;
            } else {
                location.href = response.data.next_redirect_pc_url;
            }

        });
}

function bookingCheckModalOpen() {
    const ticketTypeModalSpan = document.getElementById("ticketTypeModalSpan");
    ticketTypeModalSpan.innerText = `[${dayType}]`;

    const ticketBookingDayModalSpan = document.getElementById("ticketBookingDayModalSpan");
    ticketBookingDayModalSpan.innerText = `${bookingDay}`;

    const adultTicketModalSpan = document.getElementById("adultTicketModalSpan");
    adultTicketModalSpan.innerText = `${adultQuantity}`;

    const youthTicketModalSpan = document.getElementById("youthTicketModalSpan");
    youthTicketModalSpan.innerText = `${youthQuantity}`;

    const bookingCheckModal = bootstrap.Modal.getOrCreateInstance("#bookingCheckModal");
    bookingCheckModal.show();
}

function goToHome() {
    location.href="/";
}

function goToMyPage() {
    location.href="/user/myTicketsPage?id=" + myId;
}

function updateRemainingQuantity() {
    const url = "/api/ticket/updateRemainingQuantity";
    fetch(url, {
        method: "post",
        headers: {
            "Content-Type" : "application/x-www-form-urlencoded"
        },
        body: `count=${earlyBirdCount}&id=${ticketId}`
    })
        .then(response => response.json())
        .then(response => {

        });
}

// 모바일 / 티켓 구분
function isMobile() {
    const userAgent = navigator.userAgent || navigator.vendor || window.opera;
//     모바일 기기 탐지
    return (/android/i.test(userAgent) || /iPad|iPhone|ipod/.test(userAgent) && !window.MSStream);
}

function infoModalOpen() {
    const infoModal = bootstrap.Modal.getOrCreateInstance("#infoModal");
    infoModal.show();
}

function infoModalClose() {
    const infoModal = bootstrap.Modal.getOrCreateInstance("#infoModal");
    infoModal.hide();
}

// 버튼 활성화
function buttonCheck() {
    const buyButton = document.getElementById('buyButton');

    if ((adultQuantity + youthQuantity) === 0 ) {
        buyButton.disabled = true;
    } else {
        buyButton.disabled = false;
    }
}

window.addEventListener("DOMContentLoaded", () => {
    setSessionId();
    getPrice();
    infoModalOpen();
    buttonCheck();
});
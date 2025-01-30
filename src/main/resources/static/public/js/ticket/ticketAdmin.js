const urlParams = new URL(window.location.href).searchParams;
const ticketId = urlParams.get("id");

// 모달 열기 (.show)
function modalOpen() {
    const festivalInfoUrl = '/api/admin/ticket/getFestivalAllList';
    fetch(festivalInfoUrl)
        .then(response => response.json())
        .then((response) => {
            const selectFestivalId = document.getElementById("selectFestivalId");
            const festivalListTemplate = document.querySelector("#festivalListTemplate .festivalListWrapper");

            // defaultInfo? select엔 placeholder가 없으므로 placeholder 역할
            // defaultInfo 복사.. 이거 안하면 innerHTML에서 다 날아가서 defaultInfo가 없어짐
            const defaultInfo = selectFestivalId.querySelector(".defaultInfo");

            // selectBox 초기화
            selectFestivalId.innerHTML = "";

            // defaultInfo 옵션 다시 추가
            if (defaultInfo) {
                selectFestivalId.appendChild(defaultInfo);
            }

            // 반복문 돌려서 페스티벌 id랑 name가져옴
            for (const e of response.data.festivalList) {
                const festivalListWrapper = festivalListTemplate.cloneNode(true); // true 안쪽까지 다 복사

                const festivalList = festivalListWrapper.querySelector(".festivalList");
                // option에 value값 지정
                festivalList.value = e.id;
                festivalList.text = e.festival_name;

                selectFestivalId.appendChild(festivalList);
            }
            selectFestivalId.addEventListener('change', isSelectChange);

            // 작성 중 모달 닫을 경우, 초기화 안해주면 그 값 그대로 가지고있음
            const inputBookingDay = document.getElementById("inputBookingDay");
            inputBookingDay.value = '';

            const inputTotalQuantity = document.getElementById("inputTotalQuantity");
            inputTotalQuantity.value = '';

            const inputAdultPrice = document.getElementById("inputAdultPrice");
            inputAdultPrice.value = '';

            const inputYouthPrice = document.getElementById("inputYouthPrice");
            inputYouthPrice.value = '';

            const inputOpenDate = document.getElementById("inputOpenDate");
            inputOpenDate.value = '';

            const inputDeadLindDate = document.getElementById("inputDeadLindDate");
            inputDeadLindDate.value = '';
        });

    const ticketModal = bootstrap.Modal.getOrCreateInstance("#ticketModal");
    ticketModal.show();
}

// 모달 닫기 (.hide)
function modalClose() {
    const ticketModal = bootstrap.Modal.getOrCreateInstance("#ticketModal");
    ticketModal.hide();
}

// 페스티벌의 정보를 담을 전역 변수
let festivalInfo = {};

// UTC -> KST..
function toKST(dateString) {
    const date = new Date(dateString);
    date.setHours(date.getHours() + 9);
    return date;
}

// 페스티벌에 따른 티켓 날짜 변경
function isSelectChange() {
    const selectFestivalId = document.getElementById("selectFestivalId");
    // console.log("값 : " + selectFestivalId.value);

    // id값으로 해당 페스티벌 정보 가져오기
    const url = `/api/admin/ticket/getFestivalById?id=${selectFestivalId.value}`;
    fetch(url)
        .then(response => response.json())
        .then((response) => {
            festivalInfo = response.data.festivalInfo;

            const minDate = toKST(festivalInfo.starting_date).toISOString();
            const maxDate = toKST(festivalInfo.end_date).toISOString();

            const inputBookingDay = document.getElementById("inputBookingDay");
            const oneDay = document.getElementById("1Day");
            const allDay = document.getElementById("AllDay");

            if (festivalInfo.starting_date && festivalInfo.end_date) {
                if (oneDay.checked) {
                    // 1일권
                    inputBookingDay.min = minDate.substring(0, 10);
                    inputBookingDay.max = maxDate.substring(0, 10);
                } else if (allDay.checked) {
                    // 3일권
                    inputBookingDay.min = minDate.substring(0, 10);
                    inputBookingDay.max = minDate.substring(0, 10);
                }
            }

            // substring.. yyyy-MM-ddTHH:mm 형식으로 뽑기 위한..
            const inputOpenDate = document.getElementById("inputOpenDate");
            inputOpenDate.max = festivalInfo.starting_date.substring(0, 16);

            const inputDeadLindDate = document.getElementById("inputDeadLindDate");
            inputDeadLindDate.max = festivalInfo.starting_date.substring(0, 16);
        });
}

// 티켓 등록
function ticketSubmit() {
    const ticketRegisterForm = document.getElementById("ticketRegisterForm");

    // 유효성 검사
    const selectFestivalId = document.getElementById("selectFestivalId");
    const selectedFestival = selectFestivalId.options[selectFestivalId.selectedIndex].value
    if (selectedFestival == 0) {
        alert("페스티벌을 선택해 주세요.");
        return;
    }

    const inputBookingDay = document.getElementById("inputBookingDay");
    if (inputBookingDay.value == '') {
        alert("페스티벌 날짜를 선택해 주세요.");
        return;
    }

    const inputTotalQuantity = document.getElementById("inputTotalQuantity");
    if (inputTotalQuantity.value == '') {
        alert("티켓 수량을 입력해 주세요.");
        return;
    }

    const inputAdultPrice = document.getElementById("inputAdultPrice");
    if (inputAdultPrice.value == '') {
        alert("성인 티켓 가격을 입력해 주세요.");
        return;
    }

    const inputYouthPrice = document.getElementById("inputYouthPrice");
    if (inputYouthPrice.value == '') {
        alert("청소년 티켓 가격을 입력해 주세요.");
        return;
    }

    const inputOpenDate = document.getElementById("inputOpenDate");
    if (inputOpenDate.value == '') {
        alert("티켓 오픈일을 선택해 주세요.");
        return;
    }

    const inputDeadLindDate = document.getElementById("inputDeadLindDate");
    if (inputDeadLindDate.value == '') {
        alert("티켓 마감일을 선택해 주세요.");
        return;
    }

    // submit 무조건...formData로......
    const formData = new FormData(ticketRegisterForm);

    const url = '/api/admin/ticket/createTicketInfo'
    fetch(url, {
        headers: {
            // 'Content-Type': 'multipart/form-data'
        },
        method: 'POST',
        cache: 'no-cache',
        body: formData
        })
        .then(response => response.json())
        .then((response) => {
            // ticketRegisterForm.submit();
            // submit.. submit하면 자동으로 새로고침 되는데 얘가..form데이터를 잡고있음...  모달 닫고 새로고침 강제로 해줌
            modalClose();
            location.reload();
        });
}

// 삭제 확인
function deleteCheck(event) {
    // confirm... true(확인) / false(취소) 로 값을 줌
    const isdelete = confirm("티켓 정보를 삭제하시겠습니까?");

    // 확인을 클릭하지 않은 경우
    if (!isdelete) {
        // 동작 안되게
        event.preventDefault();
    }
}

// 할인 등록창 보이게
function showDiscount() {
    const discountDNone = document.getElementById("discountDNone");
    // toggle.. 지정한 클래스가 존재하면 제거하고, 존재하지 않으면 추가하는 기능
    discountDNone.classList.toggle("d-none")
}

// 할인 등록 모달 보이게 + 티켓아이디세팅
function registerDiscount(ticketId) {
    const earlyBirdRegisterModal = bootstrap.Modal.getOrCreateInstance("#earlyBirdRegisterModal");
    earlyBirdRegisterModal.show();

    const hiddenTicketId = document.getElementById("hiddenTicketId");
    hiddenTicketId.value = ticketId;
}

// 할인 등록
function createDiscount() {
    const url = '/api/admin/ticket/createDiscount';

    const discountForm = document.getElementById("discountForm")
    const formData = new FormData(discountForm);

    console.log('??');

    fetch(url, {
        method: "post",
        body: formData
    })
        .then(response => response.json())
        .then(response => {
            console.log("할인 등록");
            showMessage('할인 등록이 완료되었습니다.');
        });
}

// 할인 정보 모달
function discountInfo(ticketId) {
    const url = `/api/admin/ticket/getDiscountInfo?id=${ticketId}`
    fetch(url)
        .then(response => response.json())
        .then(response => {
            const discountIdSpan = document.getElementById("discountIdSpan");
            discountIdSpan.innerText = ticketId;

            const discountPercentSpan = document.getElementById("discountPercentSpan");
            discountPercentSpan.innerText = response.data.discount.discount * 100;

            const discountRemainingQuantitySpan = document.getElementById("discountRemainingQuantitySpan");
            discountRemainingQuantitySpan.innerText = response.data.discount.remaining_quantity;

            const discountQuantitySpan = document.getElementById("discountQuantitySpan");
            discountQuantitySpan.innerText = response.data.discount.discount_quantity;

            const discountDeleteSpan = document.getElementById("discountDeleteSpan");
            discountDeleteSpan.onclick = ()=> {deleteDiscount(response.data.discount.id)};
        })

    const earlyBirdInfoModal = bootstrap.Modal.getOrCreateInstance("#earlyBirdInfoModal");
    earlyBirdInfoModal.show();
}

// 할인 삭제
function deleteDiscount(discountId) {
    const url = `/api/admin/ticket/deleteDiscount?id=${discountId}`;

    const isdelete = confirm("할인 정보를 삭제하시겠습니까?");

    if (!isdelete) {
        return;
    }

    fetch(url)
        .then(response => response.json())
        .then(response => {
            location.reload();
        })
}

// 알림창
function showMessage(text) {
    const reviewUpdateDoneModal = bootstrap.Modal.getOrCreateInstance("#reviewUpdateDoneModal");
    reviewUpdateDoneModal.show();

    const testAlert = document.getElementById("testAlert")
    testAlert.innerText = text;

    setTimeout(() => {
        reviewUpdateDoneModal.hide();
        location.reload();
    }, 1000);

}

document.addEventListener('DOMContentLoaded', function () {
    const deleteLinks = document.querySelectorAll('.deleteLink');
    deleteLinks.forEach(function (link) {
        link.addEventListener('click', deleteCheck);
    });
});
/********** bootstrap **********/

// 캔바스 열기
function showCanvas(data) {
    const offcanvasWrapper = document.getElementById("offcanvasWrapper");
    const offcanvasBody = offcanvasWrapper.querySelector(".offcanvas-body");
    offcanvasBody.innerHTML = "";
    offcanvasBody.appendChild(data);

    const offcanvasInstance = bootstrap.Offcanvas.getOrCreateInstance(offcanvasWrapper);
    offcanvasInstance.show();
}

// 캔바스 닫기
function closeCanvas() {
    const offcanvasWrapper = document.getElementById("offcanvasWrapper");
    const offcanvasBody = offcanvasWrapper.querySelector(".offcanvas-body");
    offcanvasBody.innerHTML = "";

    const offcanvasInstance = bootstrap.Offcanvas.getOrCreateInstance(offcanvasWrapper);
    offcanvasInstance.hide();
}

// 모달 호출
function showModal(data, title="", footer="") {
    const modalElement = document.querySelector("#modalWrapper");
    const modalInstance = bootstrap.Modal.getOrCreateInstance(modalElement);
    const modalBody = modalElement.querySelector(".modal-body");

    const modalTitle = modalElement.querySelector(".modal-title");
    const modalFooter = modalElement.querySelector(".modal-footer");
    
    // 모달 컨텐츠
    modalBody.innerHTML = "";
    modalBody.appendChild(data);

    // 모달 헤더, 푸터
    if(title) {
        modalTitle.innerText = title;
    }
    if(footer) {
        modalFooter.innerHTML = "";
        modalFooter.appendChild(footer);
    }
    
    // 호출
    modalInstance.show();
}

// 모달 닫기
function closeModal(event) {
    const target = event.target;

    const modalElement = target.closest("#modalWrapper");
    const modalInstance = bootstrap.Modal.getOrCreateInstance(modalElement);
    const modalBody = modalElement.querySelector(".modal-body");
    modalBody.innerHTML = "";

    modalInstance.hide();
}


/********** utils **********/

// 날짜 포맷 : yy.HH.dd ..
function formatDateTime(dateString, type=1) {
    const date = new Date(dateString); // Date 객체로 변환
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    if(type == 1) {
        return `${year}.${month}.${day} ${hours}:${minutes}:${seconds}`;
    }else if(type == 2) {
        return `${year}.${month}.${day}`;
    }else if(type == 3) {
        return `${year}-${month}-${day} ${hours}:${minutes}`;
    }
}

// 1천자리 마다 콤마 찍기
function formatNumberComma(number) {
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

// 콤마를 제거
function removeCommas(numberString) {
    return numberString.replace(/,/g, '');
}

// 안내 박스
function printNotice(text) {
    const newNoticeBox = document.createElement("div");
    newNoticeBox.classList.add("fixed-notice-box");
    const newNoticeText = document.createElement("div");
    newNoticeText.classList.add("notice-text");
    newNoticeText.innerText = text;
    newNoticeBox.appendChild(newNoticeText);

    const wrapper = document.querySelector(".wrapper");
    wrapper.appendChild(newNoticeBox);

    // 그리고 제거
    setTimeout(() => {
        newNoticeBox.style.opacity = '0';
        newNoticeBox.addEventListener('transitionend', () => {
            newNoticeBox.remove();
        }, {once: true});
    }, 800);
}

// 접속 기기 구분
function isMobile() {
    var userAgent = navigator.userAgent || navigator.vendor || window.opera;
    // 모바일 기기 탐지
    return (/android/i.test(userAgent) || /iPad|iPhone|iPod/.test(userAgent) && !window.MSStream);
}
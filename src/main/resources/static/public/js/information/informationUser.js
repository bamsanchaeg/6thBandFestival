/**사용자페이지 공용기능**/


//페이지 리로드
function reloadPage(){
    location.reload(true);
}

//페스티벌 메인 페이지로 돌아가기
function backToFestivalList(){
    window.location.href="/information/mainPage";
}

function backToHistoryMain(){
    window.location.href="/information/festivalHistory";
}

//라인업에서 뒤로가기
function backToPreviousPage(){
    history.back();
}

//날짜변환함수..
function formDate(dateString){
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = ('0' + (date.getMonth() + 1)).slice(-2);
    const day = ('0' + date.getDate()).slice(-2);
    return `${year}/${month}/${day}`;
}

function backToFestivalDetailPage(){
    window.location.href="/admin/festivalDetailPage?id" + festivalId;
}

// 캔버스 열기
function showCanvasBubble(data) {
    const offcanvasWrapper = document.getElementById("offcanvasWrapper");
    const offcanvasBody = offcanvasWrapper.querySelector(".offcanvas-body");
    offcanvasBody.innerHTML = "";
    offcanvasBody.appendChild(data);

    const offcanvasInstance = bootstrap.Offcanvas.getOrCreateInstance(offcanvasWrapper);
    offcanvasInstance.show();
}

// 캔버스 닫기
function closeCanvasBubble() {
    const offcanvasWrapper = document.getElementById("offcanvasWrapper");
    const offcanvasBody = offcanvasWrapper.querySelector(".offcanvas-body");
    offcanvasBody.innerHTML = "";

    const offcanvasInstance = bootstrap.Offcanvas.getOrCreateInstance(offcanvasWrapper);
    offcanvasInstance.hide();
}

//sethours참고
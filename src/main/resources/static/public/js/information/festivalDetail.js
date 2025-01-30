const urlParams = new URL(window.location.href).searchParams;
const festivalId = urlParams.get("id");
const festivalUrl = "/api/information/festivalDetailPageForAdmin?id=" + festivalId;
console.log(festivalId);

//전역변수
let FestivalLocation;

function gertFestivalDetailForAdmin() {

    fetch(festivalUrl)
        .then(response => response.json())
        .then(response => {
            const e = response.data.festivalData;

            const festivalName = document.getElementById("festivalName");
            festivalName.innerText = e.festivalDetail.festival_name;
            console.log(e.festivalDetail.festival_name);
            const festivalLocation = document.getElementById("festivalLocation");
            festivalLocation.innerText = e.festivalDetail.festival_location;

            FestivalLocation = e.festivalDetail.festival_location;

            const posterImage = document.getElementById("posterImage");
            posterImage.src = '/images/' + e.festivalDetail.thumbnail;

            //페스티벌 소개
            const festivalIntro = document.getElementById("festivalIntro");
            festivalIntro.innerText = e.festivalDetail.festival_content;

            const festivalCaution = document.getElementById("festivalCaution");
            festivalCaution.innerText = e.festivalDetail.festival_caution;

            const festivalReservationMethod = document.getElementById("festivalReservationMethod");
            festivalReservationMethod.innerText = e.festivalDetail.festival_reservationMethod;

            const festivalAgeLimit = document.getElementById("festivalAgeLimit");
            festivalAgeLimit.innerText = e.festivalDetail.age_limit;

            const festivalDelete = document.getElementById("festivalDelete");
            festivalDelete.href = '/admin/information/festivalDeleteProcess?id=' + e.festivalDetail.id;
            festivalDelete.onclick = function () {
                event.preventDefault();
                confirmDeletion('/admin/information/festivalDeleteProcess?id=' + e.festivalDetail.id);
            };

            const festivalUpdate = document.getElementById("festivalUpdate");
            festivalUpdate.href = '/admin/information/updateRegisteredFestivalPage?id=' + e.festivalDetail.id;


            //새창으로 이동하는 주소를 생성하는 것을 의미한다. 이동은 성공했으나 Controller쪽에서 주소 설정이 잘못되어서 수정함.
            const registerLineUplink = document.getElementById("registerLineUplink");
            registerLineUplink.href = "/admin/information/registerPerformanceDate?id=" + e.festivalDetail.id;


        });


}

function confirmDeletion(url) {
    if (confirm("정말로 삭제하시겠습니까?삭제된 데이터는 복구할 수 없습니다.")) {
        window.location.href = "/api/information/festivalListPageForAdmin";
    }
}

let map;

//복붙하지말고 이해하기...지피티믿지마
document.addEventListener('DOMContentLoaded', function () {
    const tabs = document.querySelectorAll('.nav-link');
    const tabContents = document.querySelectorAll('.tab-content');

    tabs.forEach(tab => {
        tab.addEventListener('click', function (event) {
            event.preventDefault(); // 링크 기본 동작 방지
            if (tab.classList.contains('disabled')) return; // Disabled 탭 무시

            // 모든 탭을 비활성화
            tabs.forEach(t => t.classList.remove('active'));
            // 모든 콘텐츠를 숨김
            tabContents.forEach(tc => tc.classList.remove('active'));

            // 현재 탭을 활성화
            tab.classList.add('active');
            // 해당 콘텐츠를 표시
            const activeTabContent = document.getElementById(tab.dataset.tab);
            activeTabContent.classList.add('active');
        });
    });
});

function getAMap() {
    const tabEl = document.querySelector('a[data-tab="tab4"]');
    console.log("Tab shown event triggered", tabEl);


    //왜 탭전환이 안되냐..
    tabEl.addEventListener('click', function () {
        console.log("Tab shown event triggered2", tabEl);
        getKakaoMap();

    });


}


function getKakaoMap() {

    //카카오맵 API

    var geocoder = new kakao.maps.services.Geocoder();
    var mapContainer = document.getElementById('map')

    let xVar;
    let yVar;

    var callback = function (result, status) {
        if (status === kakao.maps.services.Status.OK) {
            console.log(result);
            xVar = result[0].x;
            yVar = result[0].y;

            console.log("x축 값", xVar);
            console.log("y축 값", yVar);

            var mapOption = {
                center: new kakao.maps.LatLng(yVar, xVar), // 지도의 중심좌표
                level: 3, // 지도의 확대 레벨
                mapTypeId: kakao.maps.MapTypeId.ROADMAP // 지도종류
            };

            // 지도를 생성한다
            var map = new kakao.maps.Map(mapContainer, mapOption);

            // 지도에 마커를 생성하고 표시한다
            var marker = new kakao.maps.Marker({
                position: new kakao.maps.LatLng(yVar, xVar), // 마커의 좌표
                map: map // 마커를 표시할 지도 객체

            });

            // 탭이 전환된 후에 지도 크기를 재설정하고 중심을 다시 설정
        }
    };

    geocoder.addressSearch(FestivalLocation, callback);

}


//function 밖에 있어야함.
window.addEventListener("DOMContentLoaded", () => {
    gertFestivalDetailForAdmin()
})
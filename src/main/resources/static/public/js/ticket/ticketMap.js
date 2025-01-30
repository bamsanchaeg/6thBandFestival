let coords;

// 모달 열기
function mapModalOpen() {
    fetch(InfoUrl)
        .then(response => response.json())
        .then((response) => {
            const locationMapModal = bootstrap.Modal.getOrCreateInstance(document.getElementById('locationMapModal'));
            locationMapModal.show();

            // 모달에 애니메이션이 있음!! 초기 크기가 있고 커지는 방식인데 초기 크기에서
            // map을 로딩하므로 애니메이션이 끝난 후에 맵 로드하기
            document.getElementById('locationMapModal').addEventListener('shown.bs.modal', function () {
                geocodeAddress(`${response.data.ticketInfo.festival_location}`, function(coords) {
                    initMap(coords);
                });
            });

        });
}

// 맵api 생성
function initMap() {
    const mapContainer = document.getElementById('map');
    const mapOption = {
        center: coords,
        level: 3
    };

    const map = new kakao.maps.Map(mapContainer, mapOption);

    const marker = new kakao.maps.Marker({
        map: map,
        position: coords
    });
}

// 주소를 좌표로 바꿔주는..
function geocodeAddress(address, callback) {
    const geocoder = new kakao.maps.services.Geocoder();
    geocoder.addressSearch(address, function(result, status) {
        if (status === kakao.maps.services.Status.OK) {
            coords = new kakao.maps.LatLng(result[0].y, result[0].x);
            callback(coords);
        }
    });
}
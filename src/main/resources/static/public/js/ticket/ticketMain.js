// 공유 모달
function shareModalOpen(id) {
    const shareUrl = document.getElementById("shareUrl");
    shareUrl.innerText = `https://festival.null-pointer-exception.com/ticket/reviewDetailPage?id=${id}`;

    const twitterShareIcon = document.getElementById('twitterShareIcon');
    twitterShareIcon.onclick = () => { shareTwitter(`${id}`) };

    const facebookShareIcon = document.getElementById('facebookShareIcon');
    facebookShareIcon.onclick = () => { shareFacebook(`${id}`) };

    const kakaoShareIcon = document.getElementById('kakaoShareIcon');
    kakaoShareIcon.onclick = () => { shareKakao(`${id}`) };

    const naverShareIcon = document.getElementById('naverShareIcon');
    naverShareIcon.onclick = () => { naverShare(`${id}`) };

    const urlCopyIcon = document.getElementById('urlCopyIcon');
    urlCopyIcon.onclick = () => { urlCopy(`${id}`) };

    const shareModal = bootstrap.Modal.getOrCreateInstance("#shareModal");
    shareModal.show();
}

// 트위터 공유
function shareTwitter(id) {
    const sendText = `6th Band에 들어오셔서 Festival 후기를 확인하세요!`; // 전달할 텍스트
    const sendUrl = `https://festival.null-pointer-exception.com/ticket/reviewDetailPage?id=${id}`; // 전달할 URL
    window.open('https://twitter.com/intent/tweet?text=' + sendText + '&url=' + sendUrl);
}

// 페이스북 공유
function shareFacebook(id) {
    const sendUrl = `https://festival.null-pointer-exception.com/ticket/reviewDetailPage?id=${id}`; // 전달할 URL
    window.open("https://www.facebook.com/sharer/sharer.php?u=" + sendUrl);
}

// 카카오톡 공유
function shareKakao(id) {
    const url = "/api/ticket/getKakaoKey";

    fetch(url)
        .then(response => response.json())
        .then((response) => {
            Kakao.init(response.data.kakao);
            // key 값을 가져오는지 확인
            console.log("카카오 key 입력 : " + Kakao.isInitialized());

            const currentURL = `https://festival.null-pointer-exception.com/ticket/reviewDetailPage?id=${id}`; // 전달할 URL

            console.log("id : " , id);

            Kakao.Link.sendCustom({
                templateId: 111754, // 실제 템플릿 ID로 대체
                templateArgs: {
                    bid : id,
                    // url: currentURL,
                }
            });


        });
}

// 네이버 공유
function naverShare(id) {
    const currentURL = `https://festival.null-pointer-exception.com/ticket/reviewDetailPage?id=${id}`; // 전달할 URL
    const title = '6th Band Festival'
    var shareURL = "https://share.naver.com/web/shareView?url=" + currentURL + "&title=" + title;
    document.location = shareURL;
}

// url 복사
function urlCopy(id) {
    const currentURL = `https://festival.null-pointer-exception.com/ticket/reviewDetailPage?id=${id}`; // 전달할 URL

    const textArea  = document.createElement('textarea');
    textArea.value = currentURL;
    console.log(textArea.value);
    document.body.appendChild(textArea);
    textArea.select();
    navigator.clipboard.writeText(currentURL);
    // document.execCommand('copy');
    document.body.removeChild(textArea);

    alert('공유 링크가 복사되었습니다.')
}

/* 카카오 테스트
function shareKakao(id) {
    Kakao.init('3f9085383206dd2ce63595e15d737601');

    const currentURL = `https://festival.null-pointer-exception.com/ticket/reviewDetailPage?id=${id}`; // 전달할 URL

    // 제품 타이틀을 가져오는 부분
    var productTitleElement = document.querySelector('p.prod_top');
    var productTitle = productTitleElement ? productTitleElement.innerText : '';

    // 제품 설명을 가져오는 부분
    var productSummaryElement = document.querySelector('pre');
    var productSummary = productSummaryElement ? productSummaryElement.innerText : '';

    // 제품 이미지를 가져오는 부분
    var productImageElement = document.querySelector('.swiper-slide img');
    var productImageUrl = productImageElement ? productImageElement.getAttribute('src') : '';

    Kakao.Link.sendDefault({
        objectType: 'feed',
        content: {
            title: productTitle,
            description: productSummary,
            imageUrl: productImageUrl,
            link: {
                mobileWebUrl: currentURL,
                webUrl: currentURL,
            },
        },
        buttons: [
            {
                title: '자세히 보기',
                link: {
                    mobileWebUrl: currentURL,
                    webUrl: currentURL,
                },
            },
        ],
        // 카카오톡 미설치 시 카카오톡 설치 경로이동
        installTalk: true,
    });
}
*/
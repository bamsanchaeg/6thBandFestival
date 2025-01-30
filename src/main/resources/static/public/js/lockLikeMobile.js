// 가장 큰 문제 사항: 크기와 마진을 조절 가능하게 할 시에 fixed top, fixed bottom이 조절 가능 할 것인가
// 현실적: 그냥 그대로 fixed top, fixed bottom 최상단 하단에 놓고... 크기 조절 -> 문제점: body 사이즈 변화에 대응해야 하나?

// 확인된 문제: absolute 문제 - 충분히 모아 볼 것 (은비)

// 주요 세팅값
const llmSettingValues = {
    autoHeight: true,                           // 높이 최대 크기
    fixedHeightRatio: 16,                       // 높이 배율
    fixedWidthRatio: 9,                         // 너비 배율
    commonRatio: 3.3,                           // 크기 배율
    wrapperBackgroundColor: "#FFFFFF",          // 가두는 상자 배경 색
    bodyBackgroundColor: "#EEEEEE",             // body 배경 색
    wrapperTopMargin: 0,                        // wrapper top margin
    wrapperScrollWidth: 'none',                 // wrapper 스크롤 너비: ['none' | 'thin' | 'initial']
    anotherScrollWidth: 'thin',                 // 그 외 스크롤 너비: ['none' | 'thin' | 'initial'] // thin이 가장 적당 할 듯
};

// 왼쪽 요약 페이지 세팅값(em)
const llmLeftSummarySettingValues = {
    left: 3,                                    // auto 옵션 만들어야 되나
    top: 3,
    backgroundColor: "#FFF",
    width: 15,
    height: 40,
};

// 오른쪽 요약 페이지 세팅값(em)
const llmRightSummarySettingValues = {
    right: 3,                                    // auto 옵션 만들어야 되나
    top: 3,
    backgroundColor: "#FFF",
    width: 15,
    height: 40,
};


const leftSummaryHtml = `
<div class="row">
<div class="col fw-bold">왼쪽 제목... 블라블라...</div>
</div>
<div class="row">
<div class="col">레이아웃 테스트</div>
</div>
`;

const rightSummaryHtml = `
<div class="row">
<div class="col fw-bold">오른쪽 제목... 블라블라...</div>
</div>
<div class="row">
<div class="col">레이아웃 테스트</div>
</div>
`;



window.addEventListener("DOMContentLoaded", () => {
      // 모바일인 경우
    if (/Mobi|Android/i.test(navigator.userAgent)) {
        return;
    }

    initializeBody();
    createMoblieWrapper();
    createLeftSummary();
    createRightSummary();
});

function createLeftSummary() {
    const summaryWrapper = document.createElement("div");
    summaryWrapper.style.position = "absolute";
    summaryWrapper.style.left = `${llmLeftSummarySettingValues.left}em`;
    summaryWrapper.style.top = `${llmLeftSummarySettingValues.top}em`;
    summaryWrapper.style.backgroundColor = llmLeftSummarySettingValues.backgroundColor;
    summaryWrapper.style.width = `${llmLeftSummarySettingValues.width}em`;
    summaryWrapper.style.height = `${llmLeftSummarySettingValues.height}em`;


    summaryWrapper.innerHTML = leftSummaryHtml;

    document.body.appendChild(summaryWrapper);

}

function createRightSummary() {
    const summaryWrapper = document.createElement("div");
    summaryWrapper.style.position = "absolute";
    summaryWrapper.style.right = `${llmRightSummarySettingValues.right}em`;
    summaryWrapper.style.top = `${llmRightSummarySettingValues.top}em`;
    summaryWrapper.style.backgroundColor = llmRightSummarySettingValues.backgroundColor;
    summaryWrapper.style.width = `${llmRightSummarySettingValues.width}em`;
    summaryWrapper.style.height = `${llmRightSummarySettingValues.height}em`;

    summaryWrapper.innerHTML = rightSummaryHtml;

    document.body.appendChild(summaryWrapper);
}


function initializeBody() {
    document.body.style.backgroundColor = llmSettingValues.bodyBackgroundColor;
    document.body.style.overflow = 'hidden'; // 바디 스크롤 없애기
}

function createMoblieWrapper() {
    const mobileWrapper = document.createElement("div");
    initializeMoblieWrapper(mobileWrapper); // 모바일 박스 초기화
    moveToMoblieWrapper(mobileWrapper); // 모바일 박스로 전체 이동
    lockFixedDiv(mobileWrapper, mobileWrapper.style.width); // fixed 속성 박스들 가운데로 가두기

    // 전체 스크롤 너비 세팅
    if(llmSettingValues.anotherScrollWidth) {
        setScrollWidth(mobileWrapper, llmSettingValues.anotherScrollWidth);
    }

    // 가두는 래퍼 스크롤 기능만 남기고 바는 없애기
    mobileWrapper.style.scrollbarWidth = llmSettingValues.wrapperScrollWidth;

    // 기타 예외 사항 처리

    return mobileWrapper;
}

function initializeMoblieWrapper(mobileWrapper){
    mobileWrapper.style.width = llmSettingValues.fixedWidthRatio * llmSettingValues.commonRatio + "em" ;

    if(llmSettingValues.autoHeight){
        mobileWrapper.style.height = window.innerHeight + "px";
    }else{
        mobileWrapper.style.height = llmSettingValues.fixedHeightRatio * llmSettingValues.commonRatio + "em" ;
    }

    mobileWrapper.style.margin = `${llmSettingValues.wrapperTopMargin}em auto`;
    mobileWrapper.style.overflow = "auto";

    mobileWrapper.style.backgroundColor = llmSettingValues.wrapperBackgroundColor;
}

function moveToMoblieWrapper(mobileWrapper){
    while(document.body.childNodes.length != 0){
        mobileWrapper.appendChild(document.body.childNodes[0]);
    }

    document.body.appendChild(mobileWrapper);
}

function lockFixedDiv(targetElement, width) {

    if(isFixed(targetElement)
        || isModal(targetElement)
        || isVerticalOffcanvas(targetElement)
    ){
        targetElement.style.width = width;
        // targetElement.style.margin = "0 auto"; // 민지가 찾아냄... 난 이게 왜 문제인지 전혀 모르겠다, 현영 - 오히려 이게 있는게 더 잘 맞음

        // 민지 - 모달에 left 속성 원래 있나??
        targetElement.style.left = "auto";
        targetElement.style.right = "auto";
    }

    // 재귀
    for(element of targetElement.children){
        lockFixedDiv(element, width);
    }
}

function setScrollWidth(targetElement, width){

    targetElement.style.scrollbarWidth = width;

    // 재귀
    for(element of targetElement.children){
        setScrollWidth(element, width);
    }
}

function isFixed(element){
    return getComputedStyle(element).position == 'fixed' ||
        (element.getAttribute("class") && element.getAttribute("class").includes("fixed"));
}

function isModal(element){
    for(cls of element.classList) {
        if(cls == 'modal') return true;
    }
    return false;
}

function isVerticalOffcanvas(element){
    for(cls of element.classList) {
        if(cls == 'offcanvas-top' || cls == 'offcanvas-bottom') return true;
    }
    return false;
}
/********** 후원 : 후원한 프로젝트 목록 **********/

// 후원 프로젝트 : 데이터 생성
function getPledgesData(pledgeList) {
    // 데이터가 없을 때
    if(pledgeList.reservationStatusList.length === 0 && pledgeList.successStatusList.length === 0 && pledgeList.failStatusList.length === 0) {
        const newContainer = document.createElement("div");
        newContainer.classList.add("container");
        const emptyBox = document.createElement("div");
        emptyBox.classList.add("list-none-box", "border");
        const emptyBoxText = document.createElement("p");
        emptyBoxText.innerText = "후원한 프로젝트가 없습니다.";
        emptyBox.appendChild(emptyBoxText);
        newContainer.appendChild(emptyBox);
        return newContainer;
    }

    const newProjectData = document.createElement("div");

    // 진행중
    if(pledgeList.reservationStatusList.length > 0) {
        const projectSection = getPledgeSection(pledgeList.reservationStatusList, "후원 진행중", 1);
        newProjectData.appendChild(projectSection);
    }
    // 성공
    if(pledgeList.successStatusList.length > 0) {
        const projectSection = getPledgeSection(pledgeList.successStatusList, "후원 성공", 2);
        newProjectData.appendChild(projectSection);
    }
    // 실패
    if(pledgeList.failStatusList.length > 0) {
        const projectSection = getPledgeSection(pledgeList.failStatusList, "후원 실패", 3);
        newProjectData.appendChild(projectSection);
    }

    return newProjectData;
}

// 후원 프로젝트 : 섹션 태그 생성
function getPledgeSection(projectListData, titleStr="", type="") { // type : 1 진행중, 2 성공, 3 실패
    const projectSection = document.querySelector("#template-wrapper .project-section");
    const projectRow = projectSection.querySelector(".project-row");

    const newSection = projectSection.cloneNode(true);
    const titleText = newSection.querySelector(".project-title .title-text");
    titleText.innerText = titleStr;
    const titleCount = newSection.querySelector(".project-title .count");
    titleCount.innerText = `(${projectListData.length})`;
    
    const projectList = newSection.querySelector(".project-list");
    projectList.innerHTML = "";

    for(project of projectListData) {
        const newProjectRow = projectRow.cloneNode(true);
        const thumbnailImg = newProjectRow.querySelector(".image-box .img");
        thumbnailImg.src = `/images/${project.supportData.thumbnail_location}`;
        const imgHref = newProjectRow.querySelector(".image-box a");
        imgHref.href = `/funding/pledgeDetail?id=${project.supportData.id}`;

        const supportDate = newProjectRow.querySelector(".support-date");
        supportDate.innerText = `후원일 ${formatDateTime(project.supportData.support_at, 2)}`;

        const supportId = newProjectRow.querySelector(".support-id");
        supportId.innerText = `후원번호 ${project.supportData.id}`;

        const projectTitle = newProjectRow.querySelector(".title > a");
        projectTitle.innerText = project.supportData.project_title;
        projectTitle.href = `/funding/pledgeDetail?id=${project.supportData.id}`;

        // 아이템 for
        if(project.rewardDataList.length > 0) {
            const rewardNameBox = newProjectRow.querySelector(".reward-name-box");
            rewardNameBox.innerHTML = "";
            for(projectReward of project.rewardDataList) {
                const newRewardName = document.createElement("div");
                newRewardName.innerText = `${projectReward.projectRewardDto.title}(x${projectReward.supportRewardDto.quantity})`;

                rewardNameBox.appendChild(newRewardName);
            }
        }

        const supportStatus = newProjectRow.querySelector(".support-status");
        const status = supportStatus.querySelector(".status");
        const date = supportStatus.querySelector(".date");

        if(type == 1) {
            status.innerText = "후원 마감일 ";
            date.innerText = formatDateTime(project.supportData.end_at, 2);
        }else if(type == 2) {  // 변경 필요
            console.log(project.supportData.delivery_status);
            if(project.supportData.delivery_status == "대기중") {
                status.innerText = `선물 전달 ${project.supportData.delivery_status}`;
            }else{
                status.innerText = "선물 전달 완료일 ";
                date.innerText = formatDateTime(project.supportData.recipient_at, 2); // 테이블 필드 변경해야 맞지 사실..?
            }
        }else if(type == 3) {
            status.innerText = "결제 취소 ";
        }

        const supportPrice = newProjectRow.querySelector(".price-box > p");
        supportPrice.innerText = `${formatNumberComma(project.totalPrice)}원 ${project.supportData.pay_status}`;

        const reviewBox = newProjectRow.querySelector(".review-box");
        if(type == 2 && project.supportData.delivery_status == "전달완료" && !project.isReviewWrite) { // 전달 완료 + 작성 후기 없을 때 
            const reviewButton = reviewBox.querySelector("button");
            reviewButton.setAttribute("onclick", `showListReviewWrite(${project.supportData.id})`); // 후기 작성 유무에 따라 출력 필요
        }else{
            reviewBox.remove();
        }

        projectList.appendChild(newProjectRow);
    }

    return newSection;
}

// 후원 프로젝트 : 호출 및 출력
function loadPledges() {
    const pledgesContent = document.querySelector(".pledges-content");
    if(!pledgesContent) { return; }
    const pledgeWrapper = pledgesContent.querySelector(".pledges-content .pledges-wrapper");

    const url = "/api/funding/pledgesData";
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        if(response.result == "fail") {
            alert(response.data.msg);
            window.location.href = "/user/loginPage";
            return;
        }

        const pledgeList = response.data; // 목록 데이터
        const pledgeCount = response.data.totalCount; // 카운팅

        // 개수 출력
        const pledgesCount = pledgesContent.querySelector("#list-count");
        pledgesCount.innerText = pledgeCount;

        // 데이터 출력
        pledgeWrapper.innerHTML = "";
        const pledgeData = getPledgesData(pledgeList);
        pledgeWrapper.appendChild(pledgeData);
    });
}

// 후기 작성 폼 열기 : 상세 후기 함수랑 같이 쓸 수는 없을가..
async function showListReviewWrite(supportId) {
    const reviewWrapper = document.querySelector("#template-wrapper .review-wrapper");
    const newReviewWrapper = reviewWrapper.cloneNode(true);

    const reviewWriteForm = newReviewWrapper.querySelector(".review-write-form");
    reviewWriteForm.id = "review-write-form";

    const btnWrite = reviewWriteForm.querySelector(".btn-write-review");
    btnWrite.setAttribute("onclick", `writeListReview(${supportId})`);

    const keywordBox = reviewWriteForm.querySelector(".keyword-box");
    const reviewKeywordList = await getReviewKeywordList();

    keywordBox.appendChild(reviewKeywordList); // 리뷰 키워드
    showCanvas(newReviewWrapper);
    selectedReviewKeyword();
}

// 후기 작성 처리 : 리스트 - 상세 후기함수랑 같이 쓸 수는 어
function writeListReview(supportId) {
    const reviewWriteForm = document.querySelector(".review-wrapper #review-write-form");

    // 키워드 선택
    const keywords = reviewWriteForm.querySelectorAll("input[name='review_keyword']");
    let checkedCount = 0;
    keywords.forEach(keyword => {
        if(keyword.checked == true) checkedCount++;
    });
    if(checkedCount < 1) {
        alert("후기 키워드를 한 개이상 선택해주세요.");
        return;
    }

    // 후기 작성
    const reviewContent = reviewWriteForm.querySelector("[name='content']");
    if(reviewContent.value == "" || !reviewContent.value) {
        alert("후기 내용을 작성해주세요.");
        reviewContent.focus();
        return;
    }

    const formData = new FormData(reviewWriteForm);
    formData.append("support_id", supportId);

    // 파일 첨부 확인 : 첨부하지 않은 경우 null 처리
    if(!formData.get("uploadFiles") || formData.get("uploadFiles").name === "") {
        formData.set("uploadFiles", null);
    }
    
    const url = "/api/funding/registerProjectReview";
    fetch(url, {
        method: "POST",
        body: formData
    })
    .then(response => response.json())
    .then((response) => {
        printNotice("후기 작성이 완료되었습니다.");
        closeCanvas();
        loadPledges();
    });
}


/********** 후원 내역 상세 **********/

// 후기 키워드 가져오기
async function getReviewKeywordList() {
    // 동기 처리 
    const url = "/api/funding/getReviewKeywordList";
    const response = await fetch(url);
    const responseData = await response.json();

    const newKeywordList = document.createElement("ul");
    newKeywordList.classList.add("keword-list", "d-flex", "flex-wrap");

    for(reviewKeyword of responseData.data.reviewKewordList) {
        const newLi = document.createElement("li");
        newLi.classList.add("me-2", "mb-2");

        const newCheckbox = document.createElement("input");
        newCheckbox.type = "checkbox";
        newCheckbox.name = "review_keyword";
        newCheckbox.value = reviewKeyword.id;
        newCheckbox.classList.add("btn-check", "review-keyword");
        newCheckbox.id = `review-keyword${reviewKeyword.id}`;
        newCheckbox.autocomplete = "off";
        
        const newLabel = document.createElement("label");
        newLabel.classList.add("btn", "btn-outline-primary", "btn-review-keyword", "py-1", "px-2", "rounded-pill", "new-fs-75");
        newLabel.innerText = reviewKeyword.keyword;
        newLabel.setAttribute("for", `review-keyword${reviewKeyword.id}`);
    
        newLi.appendChild(newCheckbox);
        newLi.appendChild(newLabel);
        newKeywordList.appendChild(newLi);
    }
    return newKeywordList;
}

// 후기 작성 폼 열기
async function showReviewWrite() {
    const reviewWrapper = document.querySelector("#template-wrapper .review-wrapper");
    const newReviewWrapper = reviewWrapper.cloneNode(true);

    const reviewWriteForm = newReviewWrapper.querySelector(".review-write-form");
    reviewWriteForm.id = "review-write-form";

    const keywordBox = reviewWriteForm.querySelector(".keyword-box");
    const reviewKeywordList = await getReviewKeywordList();

    keywordBox.appendChild(reviewKeywordList); // 리뷰 키워드
    showCanvas(newReviewWrapper);
    selectedReviewKeyword();
}

// 후기 키워드 선택 개수 확인
function selectedReviewKeyword() {
    const reviewWriteForm = document.querySelector(".review-wrapper #review-write-form");
    const keywords = reviewWriteForm.querySelectorAll("input[name='review_keyword']");
    const noneKeyword = reviewWriteForm.querySelector(".review-keyword[value='9']")
    
    keywords.forEach(keyword => {
        keyword.addEventListener("click", () => {
            if (keyword === noneKeyword) { // 해당 없음을 선택한 경우 : 다른건 선택 불가
                keywords.forEach(k => {
                    if (k !== noneKeyword) {
                        k.checked = false;
                    }
                });
                noneKeyword.checked = true;
            } else {
                noneKeyword.checked = false;
    
                const selectedCount = Array.from(keywords).filter(keyword => keyword.checked).length;
                // 최대 선택 개수 초과 시 선택 해제
                if (selectedCount > 5) {
                    alert("키워드는 최대 5개 까지만 선택할 수 있습니다.");
                    event.target.checked = false;
                }
            }
        });

    });
}

// 후기 작성 처리 : 상세
function writeReview() {
    const urlParams = new URL(window.location.href).searchParams;
    const supportId = urlParams.get("id"); // 후원 번호

    const reviewWriteForm = document.querySelector(".review-wrapper #review-write-form");

    // 키워드 선택
    const keywords = reviewWriteForm.querySelectorAll("input[name='review_keyword']");
    let checkedCount = 0;
    keywords.forEach(keyword => {
        if(keyword.checked == true) checkedCount++;
    });
    if(checkedCount < 1) {
        alert("후기 키워드를 한 개이상 선택해주세요.");
        return;
    }

    // 후기 작성
    const reviewContent = reviewWriteForm.querySelector("[name='content']");
    if(reviewContent.value == "" || !reviewContent.value) {
        alert("후기 내용을 작성해주세요.");
        reviewContent.focus();
        return;
    }

    const formData = new FormData(reviewWriteForm);
    formData.append("support_id", supportId);

    // 파일 첨부 확인 : 첨부하지 않은 경우 null 처리
    if(!formData.get("uploadFiles") || formData.get("uploadFiles").name === "") {
        formData.set("uploadFiles", null);
    }

    const url = "/api/funding/registerProjectReview";
    fetch(url, {
        method: "POST",
        body: formData
    })
    .then(response => response.json())
    .then((response) => {
        printNotice("후기 작성이 완료되었습니다.");
        closeCanvas();
        
        // 후기 버튼 제거 - 새로고침은 조금..
        const reviewBox = document.querySelector(".project-info-box .review-box");
        reviewBox.remove();
    });
}


/********** 관심 프로젝트 **********/

// 관심 : 데이터 생성
function getLikedList(data) {
    // 데이터 자체가 없으면
    if(data.totalCount === 0) {
        const emptyBox = document.createElement("div");
        emptyBox.classList.add("list-none-box", "border");
        const emptyBoxText = document.createElement("p");
        emptyBoxText.innerText = "관심 프로젝트가 없습니다.";
        emptyBox.appendChild(emptyBoxText);
        return emptyBox;
    }

    const likeDataList = data.projectLikeDataList;

    const projectListBox = document.createElement("div");
    projectListBox.classList.add("project-list");

    for(likeData of likeDataList) {
        const newProjectRow = document.createElement("div");
        newProjectRow.classList.add("project-row");
    
        const newHref = document.createElement("a");
        newHref.href = `/funding/detailPage?id=${likeData.projectData.id}`;
        
        const newImageBox = document.createElement("div");
        newImageBox.classList.add("image-box");
        const newImgPBox = document.createElement("div");
        newImgPBox.classList.add("img-p-box");
        if(likeData.projectData.thumbnail_location == null || likeData.projectData.thumbnail_location == "") {
            const newImg = document.createElement("div");
            newImg.classList.add("material-symbols-outlined", "img", "no-img");
            newImg.innerText = "broken_image";
            newImgPBox.appendChild(newImg);
        }else{
            const newImg = document.createElement("img");
            newImg.classList.add("img", "image-thumbnail");
            newImg.alt = "썸네일 이미지";
            newImg.src = `/images/${likeData.projectData.thumbnail_location}`;
            newImgPBox.appendChild(newImg);
        }
        newImageBox.appendChild(newImgPBox);
    
        const newContentBox = document.createElement("div");
        newContentBox.classList.add("content-box");
    
        const newCreator = document.createElement("div");
        newCreator.classList.add("creator");
        newCreator.innerText = likeData.projectData.creator_name;
        const newTitle = document.createElement("div");
        newTitle.classList.add("title");
        newTitle.innerText = likeData.projectData.project_title;
    
        const newFundingStatus = document.createElement("div");
        newFundingStatus.classList.add("funding-status");
        const newLeft = document.createElement("div");
        newLeft.classList.add("left");
        const newFundingPercent = document.createElement("div");
        newFundingPercent.classList.add("funding-percent");
        newFundingPercent.innerText = (likeData.pledgeStatus && likeData.pledgeStatus.percentage != null) ? `${likeData.pledgeStatus.percentage}%` : "0%";
        if(likeData.projectData.status == '성공' || likeData.projectData.status == '무산') {
            newFundingPercent.style.color = "#555";
        }
        const newFundingAmount = document.createElement("div");
        newFundingAmount.classList.add("funding-amount");
        newFundingAmount.innerText = (likeData.pledgeStatus && likeData.pledgeStatus.total_amount != null) ? `${formatNumberComma(likeData.pledgeStatus.total_amount)}원` : "0원";
        newLeft.appendChild(newFundingPercent);
        newLeft.appendChild(newFundingAmount);
        const newRestDay = document.createElement("div");
        newRestDay.classList.add("rest-day");
        const newDaySpan = document.createElement("span");
        if(likeData.projectData.rest_day > 0) {
            newDaySpan.innerText = `${likeData.projectData.rest_day}일 남음`;
        }else if(likeData.projectData.rest_day == 0){ // 0일 미만
            newDaySpan.innerText = "오늘 마감";
            newDaySpan.classList.add("text-primary");
        }else{ // 마감 이후
            newDaySpan.innerText = `펀딩 ${likeData.projectData.status}`;
            newDaySpan.classList.add("text-secondary");
        }
        newRestDay.appendChild(newDaySpan);
        newFundingStatus.appendChild(newLeft);
        newFundingStatus.appendChild(newRestDay);
    
        const newPercentBar = document.createElement("div");
        newPercentBar.classList.add("funding-percentbar");
        const newBar = document.createElement("div");
        newBar.classList.add("bar");
        if(likeData.pledgeStatus && likeData.pledgeStatus.percentage != null) {
            if(likeData.pledgeStatus.percentage >= 100) { // 바 비율
                newBar.style.width = "100%";
            }else{
                newBar.style.width = `${likeData.pledgeStatus.percentage}%`;
            }
        }else{
            newBar.style.width = "0%";
        }
        if(likeData.projectData.status == '성공') {
            newBar.style.background = "#555";
        }else if(likeData.projectData.status == '무산') {
            newBar.style.width = "0%";
        }
        newPercentBar.appendChild(newBar);
    
        newContentBox.appendChild(newCreator);
        newContentBox.appendChild(newTitle);
        newContentBox.appendChild(newFundingStatus);
        newContentBox.appendChild(newPercentBar);
    
        newHref.appendChild(newImageBox);
        newHref.appendChild(newContentBox);
        newProjectRow.appendChild(newHref);
        projectListBox.appendChild(newProjectRow);
    }

    return projectListBox;
}

// 관심 : 호출 및 출력 - 상품리스트랑 유사합니다..
function loadLikedList() {
    const projectWrapper = document.querySelector(".like-content .project-wrapper");
    if(!projectWrapper) return;

    const url = `/api/funding/getLikedProjectList`;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        if(response.result == 'fail'){
            alert(response.data.msg);
            location.href = "/user/loginPage";
            return;
        }

        const totalCount = response.data.totalCount; // 프로젝트 개수
        const totalCountBox = document.querySelector(".list-count .total-count");
        totalCountBox.innerText = totalCount;

        const likedData = getLikedList(response.data);
        projectWrapper.appendChild(likedData);
    });
}


/********** 함수 호출 **********/

// 페이지 로딩시
window.addEventListener('DOMContentLoaded', function() {
    // 후원한 프로젝트
    loadPledges();

    // 관심 프로젝트
    loadLikedList();
});
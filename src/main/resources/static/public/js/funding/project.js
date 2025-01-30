/* 만든 프로젝트 / 상품목록, 상세, 주문 */
/********** 만든 프로젝트 **********/

// 프로젝트 목록
let projectStatus = ''; // 진행 상태

// 빈 목록 태그
function listEmptyBox(text) {
    const emptyBox = document.createElement("div");
    emptyBox.classList.add("list-none-box");
    const emptyBoxText = document.createElement("p");
    emptyBoxText.innerText = text;
    emptyBox.appendChild(emptyBoxText);
    return emptyBox;
}

// 만든 프로젝트 : 데이터 생성
function getProjectCreated(projectList, projectCount) {
    // 데이터 자체가 없으면
    if(projectList.length === 0) {
        return listEmptyBox("프로젝트가 없습니다.");
    }

    const newProjectList = document.createElement("div");
    newProjectList.classList.add("project-list");

    for(project of projectList) {
        const newProjectRow = document.createElement("div");
        newProjectRow.classList.add("project-row");

        const newImageBox = document.createElement("div");
        newImageBox.classList.add("image-box");
        const newProjectHref = document.createElement("a");
        newProjectHref.classList.add("d-block");
        newProjectHref.href = `/funding/projectEditorManagement?id=${project.id}`;

        const newImgBox = document.createElement("div");
        newImgBox.classList.add("img-p-box");
        if(project.thumbnail_location == null || project.thumbnail_location == "") {
            const newNoImg = document.createElement("div");
            newNoImg.classList.add("material-symbols-outlined", "img", "no-img");
            newNoImg.innerText = "broken_image";
            newImgBox.appendChild(newNoImg);
        }else{
            const newImg = document.createElement("img");
            newImg.classList.add("img");
            newImg.src = `/images/${project.thumbnail_location}`; // 이미지 경로 넣어야 함
            newImg.alt = "대표 썸네일";
            newImgBox.appendChild(newImg);
        }
        newProjectHref.appendChild(newImgBox);
        newImageBox.appendChild(newProjectHref);
        
        const newTextBox = document.createElement("div");
        newTextBox.classList.add("text-box");

        const newStatusBox = document.createElement("div");
        newStatusBox.classList.add("status");
        const newStatus = document.createElement("span");
        newStatus.innerText = project.status;
        if(project.status == "심사중") {
            newStatus.classList.add("bg-danger", "new-text-white");
        }else if(project.status == "반려") {
            newStatus.classList.add("bg-warning", "new-text-white");
        }else if(project.status == "진행중") {
            newStatus.classList.add("bg-primary", "new-text-white");
        }else if(project.status == "무산") {
            newStatus.classList.add("bg-secondary", "new-text-white");
        }else if(project.status == "성공") {
            newStatus.classList.add("bg-success", "new-text-white");
        }
        newStatusBox.appendChild(newStatus);
        const newTitle = document.createElement("div");
        newTitle.classList.add("title");
        newTitle.innerText = project.project_title;
        const newDesc = document.createElement("div");
        newDesc.classList.add("desc");
        newDesc.innerText = project.project_desc;

        const newBtnBox = document.createElement("div");
        newBtnBox.classList.add("btn-box");
        const newBtnEditor = document.createElement("a");
        newBtnEditor.classList.add("project-btn", "project-editor");
        newBtnEditor.href = `/funding/projectEditorManagement?id=${project.id}`;
        newBtnEditor.innerText = "관리";
        newBtnBox.appendChild(newBtnEditor);
        // 진행중 이전에만 삭제 가능하도록.. > 나중에는 비공개 요청 추가 하는 방향
        if(project.status != '진행중' && project.status != '성공' && project.status != '무산') {
            const newBtnDelete = document.createElement("a");
            newBtnDelete.classList.add("project-btn", "project-delete");
            newBtnDelete.setAttribute("onclick", `deleteProject(${project.id})`);
            newBtnDelete.innerText = "삭제";
            newBtnBox.appendChild(newBtnDelete);
        }
        newTextBox.appendChild(newStatusBox);
        newTextBox.appendChild(newTitle);
        newTextBox.appendChild(newDesc);
        newTextBox.appendChild(newBtnBox);

        newProjectRow.appendChild(newImageBox);
        newProjectRow.appendChild(newTextBox);
        newProjectList.appendChild(newProjectRow);
    }

    return newProjectList;
}

// 만든 프로젝트 : 호출 및 출력
function loadProjectCreated() {
    const projectBoxContainer = document.querySelector(".project-box .container");
    if(!projectBoxContainer) return;

    const url = `/api/funding/getProjectCreated?status=${encodeURIComponent(projectStatus)}`;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        const projectList = response.data.projectCreatedList; // 프로젝트 개수
        const projectCount = response.data.projectCount; // 선물 개수

        projectBoxContainer.innerHTML = "";

        // 데이터 가져오기
        const projectData = getProjectCreated(projectList, projectCount);
        projectBoxContainer.appendChild(projectData);
    });
}

// 상태 탭 검색
function searchProjectStatus() {
    const tabs = document.querySelectorAll(".status-tab .tab-list li");
    if(!tabs) return;

    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            projectStatus = tab.getAttribute('data-status');
            setActiveTab(tab);
            loadProjectCreated();
        })
    });
}

// 상단 탭 active
function setActiveTab(activeTab) {
    const tabs = document.querySelectorAll(".status-tab .tab-list li");
    tabs.forEach(tab => {
        tab.classList.remove("on");
    });
    activeTab.classList.add("on");
}

// 만든 프로젝트 : 삭제
function deleteProject(id) {
    if(!confirm("프로젝트를 삭제하시겠습니까? \n삭제한 프로젝트는 복구가 불가합니다.")) {
        return;
    }

    url = `/api/funding/deleteProjectData?id=${id}`;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        loadProjectCreated();
        printNotice("삭제가 완료되었습니다.");
    });
}

/********** 펀딩 : 메인 페이지 **********/

// 마감 임박 : 카운팅
function endCountdown() {
    const countdown = document.querySelector(".section05 .countdown");
    if(!countdown) { return; }

    // 현재 시간
    const now = new Date();
    // 자정 시간(다음 날 00:00:00)
    const tomorrow = new Date();
    tomorrow.setHours(24, 0, 0, 0); // 자정 설정
    // 자정까지 남은 시간 계산
    const timeLeft = tomorrow - now;
    // 밀리초를 시간, 분, 초로 변환
    const hours = Math.floor((timeLeft / (1000 * 60 * 60)) % 24);
    const minutes = Math.floor((timeLeft / (1000 * 60)) % 60);
    const seconds = Math.floor((timeLeft / 1000) % 60);

    // 화면에 출력
    countdown.innerHTML = hours.toString().padStart(2, '0') + ":" + minutes.toString().padStart(2, '0') + ":" + seconds.toString().padStart(2, '0');

    // 1초 마다 갱신
    setTimeout(endCountdown, 1000);
}


/********** 프로젝트 목록 : 상품 목록 **********/

let discoverPage = 1; // 페이지
let isLoading = false;
let sort = 1; // 정렬

// 목록 : 데이터 생성
function getDiscoverList(discoverList) {
    const projectListBox = document.createElement("div");
    projectListBox.classList.add("project-list");

    for(discover of discoverList) {
        const newProjectRow = document.createElement("div");
        newProjectRow.classList.add("project-row");
    
        const newHref = document.createElement("a");
        newHref.href = `/funding/detailPage?id=${discover.projectData.id}`;
        
        const newImageBox = document.createElement("div");
        newImageBox.classList.add("image-box");
        const newImgPBox = document.createElement("div");
        newImgPBox.classList.add("img-p-box");
        if(discover.projectData.thumbnail_location == null || discover.projectData.thumbnail_location == "") {
            const newImg = document.createElement("div");
            newImg.classList.add("material-symbols-outlined", "img", "no-img");
            newImg.innerText = "broken_image";
            newImgPBox.appendChild(newImg);
        }else{
            const newImg = document.createElement("img");
            newImg.classList.add("img", "image-thumbnail");
            newImg.alt = "썸네일 이미지";
            newImg.src = `/images/${discover.projectData.thumbnail_location}`;
            newImgPBox.appendChild(newImg);
        }
        newImageBox.appendChild(newImgPBox);
    
        const newContentBox = document.createElement("div");
        newContentBox.classList.add("content-box");
    
        const newCreator = document.createElement("div");
        newCreator.classList.add("creator");
        newCreator.innerText = discover.projectData.creator_name;
        const newTitle = document.createElement("div");
        newTitle.classList.add("title");
        newTitle.innerText = discover.projectData.project_title;
    
        const newFundingStatus = document.createElement("div");
        newFundingStatus.classList.add("funding-status");
        const newLeft = document.createElement("div");
        newLeft.classList.add("left");
        const newFundingPercent = document.createElement("div");
        newFundingPercent.classList.add("funding-percent");
        newFundingPercent.innerText = (discover.pledgeStatus && discover.pledgeStatus.percentage != null) ? `${discover.pledgeStatus.percentage}%` : "0%"; // 퍼센트 계산 출력 필요
        if(discover.projectData.status == '성공' || discover.projectData.status == '무산') {
            newFundingPercent.style.color = "#555";
        }
        const newFundingAmount = document.createElement("div");
        newFundingAmount.classList.add("funding-amount");
        newFundingAmount.innerText = (discover.pledgeStatus && discover.pledgeStatus.total_amount != null) ? `${formatNumberComma(discover.pledgeStatus.total_amount)}원` : "0원"; // 모인금액 계산 출력 필요
        newLeft.appendChild(newFundingPercent);
        newLeft.appendChild(newFundingAmount);
        const newRestDay = document.createElement("div");
        newRestDay.classList.add("rest-day");
        const newDaySpan = document.createElement("span");
        if(discover.projectData.rest_day > 0) {
            newDaySpan.innerText = `${discover.projectData.rest_day}일 남음`;
        }else if(discover.projectData.rest_day == 0){ // 0일 미만
            newDaySpan.innerText = "오늘 마감";
            newDaySpan.classList.add("text-primary");
        }else{ // 마감 이후
            newDaySpan.innerText = `펀딩 ${discover.projectData.status}`;
            newDaySpan.classList.add("text-secondary");
        }
        newRestDay.appendChild(newDaySpan);
        newFundingStatus.appendChild(newLeft);
        newFundingStatus.appendChild(newRestDay);
    
        const newPercentBar = document.createElement("div");
        newPercentBar.classList.add("funding-percentbar");
        const newBar = document.createElement("div");
        newBar.classList.add("bar");
        if(discover.pledgeStatus && discover.pledgeStatus.percentage != null) {
            if(discover.pledgeStatus.percentage >= 100) { // 바 비율
                newBar.style.width = "100%";
            }else{
                newBar.style.width = `${discover.pledgeStatus.percentage}%`;
            }
        }else{
            newBar.style.width = "0%";
        }
        if(discover.projectData.status == '성공') {
            newBar.style.background = "#555";
        }else if(discover.projectData.status == '무산') {
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

// 목록 : 정렬 기능
function sortDiscover(order) {
    // order : 1 최신순 / 2 최다 후원순 / 3 최다 금액순 / 4 마감 임박순

    discoverPage = 1; // page 초기화
    sort = order;

    // 태그 비우고 호출
    const projectWrapper = document.querySelector(".discover-content .project-wrapper");
    projectWrapper.innerHTML = "";
    loadDiscoverList();

    // 정렬명 변경
    const btnSortText = document.querySelector(".list-sort .btn-sort-text");
    const sortText = event.target.innerText;
    btnSortText.innerText = sortText;
}

// 목록 : 호출 및 출력
function loadDiscoverList() {
    const projectWrapper = document.querySelector(".discover-content .project-wrapper");
    if(!projectWrapper) return;

    const urlParams = new URL(window.location.href).searchParams;
    let category = urlParams.get("category");
    if(category == null) {
        category = "";
    }

    isLoading = true;

    const url = `/api/funding/getDiscoverList?category=${category}&page=${discoverPage}&sort=${sort}`;
    fetch(url, {
        method: "get",
        headers: {"Content-Type" : "application/json"}
    })
    .then(response => response.json())
    .then((response) => {
        const discoverDataList = response.data.projectDataList; // 리스트 데이터
        const totalCount = response.data.totalCount; // 프로젝트 개수
        const totalCountBox = document.querySelector(".discover-content .total-count");
        totalCountBox.innerText = totalCount;

        // 데이터 자체가 없으면
        if(totalCount === 0) {
            const emptyBox = listEmptyBox("프로젝트가 없습니다.");
            projectWrapper.innerHTML = "";
            projectWrapper.appendChild(emptyBox);
            return;
        }

        // 로딩될 데이터가 없으면
        if(discoverDataList.length === 0) {
            printNotice("더이상 프로젝트가 없습니다.");
            return;
        }

        const discoverData = getDiscoverList(discoverDataList);
        projectWrapper.appendChild(discoverData);

        isLoading = false;
        discoverPage++; // 다음 페에지 로드 준비
    });
}

// 목록 : 무한 스크롤
function handleScroll() {
    if((window.innerHeight + window.scrollY) >= (document.documentElement.scrollHeight - 100) && !isLoading) { // 문서 height 때문에 변경
        loadDiscoverList();
    }
}

// 목록 : 카테고리 활성화
function setActiveCategory() {
    const url = window.location.href;
    const urlParams = new URL(window.location.href).searchParams;
    const category = urlParams.get("category") || "";

    if(url.includes('discover')) { // 상품 리스트에서만 적용
        const categoryA = document.querySelectorAll(".category-list li a");
        categoryA.forEach((a) => {
            const href = a.getAttribute("href");
            const urlCategory = new URLSearchParams(href.split('?')[1]).get('category') || "";

            // 카테고리 활성화
            if(urlCategory == category) {
                a.parentElement.classList.add("on");
                // 카테고리 명도 같이 변경
                const categoryTitle = document.querySelector(".sub-title .title-text");
                categoryTitle.innerText = a.innerText;
            } else {
                a.parentElement.classList.remove("on");
            }
        })
    }
}


/********** 프로젝트 상세 **********/

// 상세 출력 탭
function changeProjectDetail() {
    const tabs = document.querySelectorAll(".sub-detail-content .detail-tab-box .tab-list li");
    if(!tabs) return;

    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            projectDetail = tab.getAttribute("data-detail");
            setActiveDetailTab(tab);
            setActiveDetailContent(projectDetail);
        })
    });
}

// 상세 탭 active
function setActiveDetailTab(activeTab) {
    const tabs = document.querySelectorAll(".sub-detail-content .detail-tab-box .tab-list li");
    tabs.forEach(tab => {
        tab.classList.remove("active");
    });
    activeTab.classList.add("active");
}

// 상세 컨텐츠 active
function setActiveDetailContent(detail) {
    const details = document.querySelectorAll(".sub-detail-content .project-detail-box .project-wrapper");
    details.forEach(detail => {
        detail.classList.remove("active");
    });
    const activeDetail = `${detail}-wrapper`;
    document.getElementById(activeDetail).classList.add("active");

    if(detail == 'review') {
        loadReviewList();
    }else if(detail == "community") {
        loadCommunityList();
    }else if(detail == "update") {
        loadUpdateList(); // 주석 이따 풀기
    }
}


/********** 프로젝트 상세 : 업데이트 **********/

// 업데이트 : 작성폼 열기
function showUpdateForm() {
    const updateWrtieBox = document.querySelector("#template-wrapper .update-template .update-write-box");
    const newUpdateWrtieBox = updateWrtieBox.cloneNode(true);

    const submitButton = document.createElement("button");
    submitButton.type = "button";
    submitButton.classList.add("btn", "btn-primary");
    submitButton.innerText = "작성";
    submitButton.onclick = () => writeUpdate(event);

    showModal(newUpdateWrtieBox, "업데이트 작성", submitButton);
}

// 업데이트 : 작성 처리
function writeUpdate(event) {
    const target = event.target;
    const modalWrapper = target.closest("#modalWrapper");
    
    // 내용 작성 확인
    const updateContent = modalWrapper.querySelector("[name='update_content']");
    if(updateContent.value == '' || !updateContent.value) {
        alert("업데이트 내용을 작성해주세요.");
        updateContent.focus();
        return;
    }

    const formData = new FormData();
    formData.append("project_id", getProjectId());
    formData.append("content", updateContent.value);

    const url = "/api/funding/registerUpdate";
    fetch(url, {
        method: "POST",
        body: formData
    })
    .then(response => response.json())
    .then((response) => {
        printNotice("업데이트 작성이 완료되었습니다.");
        closeModal(event);

        loadUpdateList();
    });
}

// 업데이트 : 댓글 작성
function writeUpdateComment(updateId) {
    const updateView = document.querySelector("#update-wrapper .update-view");

    const cmmentContent = updateView.querySelector("[name='comment_content']");
    if(cmmentContent.value == '' || !cmmentContent.value) {
        alert("댓글 내용을 작성해주세요.");
        cmmentContent.focus();
        return;
    }

    const formData = new FormData();
    formData.append("update_id", updateId);
    formData.append("content", cmmentContent.value);

    const url = "/api/funding/registerUpdateComment";
    fetch(url, {
        method: "POST",
        body: formData
    })
    .then(response => response.json())
    .then((response) => {
        printNotice("댓글 작성이 완료되었습니다.");

        loadUpdateView(updateId);
    });
}

// 업데이트 : 게시물 상세 - 데이터 생성
function showUpdateView(data) {
    const updateData = data.projectUpdateData; // 업데이트 본문
    const updateCommentList = data.projectUpdateCommentList; // 댓글 리스트

    const updateView = document.querySelector("#template-wrapper .update-template .update-view");
    const newUpdateBox = document.createElement("div");

    // 새로 생성
    const newUpdateView = updateView.cloneNode(true);

    // 본문 정보
    const newProfileImage = newUpdateView.querySelector(".creator-profile .profile-image");
    newProfileImage.src = `/images/${updateData.projectCreatorDto.profile_location}`;
    const newNickname = newUpdateView.querySelector(".creator-profile .nickname");
    newNickname.innerText = updateData.projectCreatorDto.creator_name;
    const newDataBox = newUpdateView.querySelector(".creator-profile .date-box");
    newDataBox.innerText = formatDateTime(updateData.projectUpdateDto.created_at, 3);
    const newContentBox = newUpdateView.querySelector(".update-content .content-box");
    newContentBox.innerText = updateData.projectUpdateDto.content;

    // 댓글
    const commentCount = newUpdateView.querySelector(".comment-info span");
    commentCount.innerText = data.commentListCount; // 댓글 개수
    
    const commentRow = newUpdateView.querySelector(".comment-list .comment-row");
    const commentList = newUpdateView.querySelector(".comment-list");
    commentList.innerHTML = "";

    if(updateCommentList.length > 0 ){
        for(commentData of updateCommentList) {
            const newCommentRow = commentRow.cloneNode(true);

            if(commentData.userDto.profile_img != null) {
                const newCommentProfileImage = newCommentRow.querySelector(".profile-box .profile-image");
                newCommentProfileImage.src = `/images/${commentData.userDto.profile_img}`;
            }
            
            const newCommentNickname = newCommentRow.querySelector(".profile .nickname");
            newCommentNickname.innerText = commentData.userDto.nickname;
            const newCommentDataBox = newCommentRow.querySelector(".update-date .date-box");
            newCommentDataBox.innerText = formatDateTime(commentData.updateComment.created_at, 3);
            
            const newCommentContentBox = newCommentRow.querySelector(".comment-content .content-box");
            newCommentContentBox.innerText = commentData.updateComment.content;

            commentList.appendChild(newCommentRow);
        }
    }else{
        const emptyBox = document.createElement("div");
        emptyBox.classList.add("list-none-box", "border-bottom", "py-5");
        emptyBox.innerText = "댓글이 없습니다.";
        commentList.appendChild(emptyBox);
    }

    // 댓글 작성 폼 : 후원자, 창작자만 가능합니다..
    const commentWriteForm = newUpdateView.querySelector(".comment-write-form");
    if(data.isSupporter || data.isCreator) {
        const btnCommentWrite = commentWriteForm.querySelector(".btn-comment-write");
        btnCommentWrite.onclick = () => writeUpdateComment(updateData.projectUpdateDto.id);
    }else{
        commentWriteForm.remove();
    }

    newUpdateBox.appendChild(newUpdateView);

    return newUpdateBox;
}

// 업데이트 : 게시물 상세 - 출력
function loadUpdateView(updateId) {
    const updateList = document.querySelector("#update-wrapper .update-list");
    if(!updateList) return;

    const url = `/api/funding/projectUpdateData?id=${updateId}`;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        updateList.innerHTML = "";

        const updateData = showUpdateView(response.data);
        updateList.appendChild(updateData);
    });
}

// 업데이트 : 데이터 생성
function getUpdateList(data) {
    const updateDataList = data.projectUpdateDataList;
    const updateCount = data.projectUpdateListCount;

    // 데이터 자체가 없으면
    if(updateCount === 0) {
        const emptyBox = document.createElement("div");
        emptyBox.classList.add("list-none-box");
        const emptyIcon = document.createElement("span");
        emptyIcon.classList.add("material-symbols-outlined", "icon-draft-order");
        emptyIcon.innerText = "draft_orders";
        const emptyBoxText = document.createElement("p");
        emptyBoxText.innerText = "아직 게시글이 없습니다.";
        emptyBox.appendChild(emptyIcon);
        emptyBox.appendChild(emptyBoxText);
        return emptyBox;
    }

    const updateRow = document.querySelector("#template-wrapper .update-template .update-row");
    const newUpdateBox = document.createElement("div");

    for(updateData of updateDataList) {
        const newUpdateRow = updateRow.cloneNode(true);

        const newProfileImage = newUpdateRow.querySelector(".profile-box .profile-image");
        newProfileImage.src = `/images/${updateData.creatorDto.profile_location}`;
        
        const newNickname = newUpdateRow.querySelector(".profile .nickname");
        newNickname.innerText = updateData.creatorDto.creator_name;
        
        const updateContent = newUpdateRow.querySelector(".update-content");
        updateContent.setAttribute("onclick", `loadUpdateView(${updateData.projectUpdateDto.id})`);
        updateContent.setAttribute("data-update-id", updateData.projectUpdateDto.id);

        const newContentBox = newUpdateRow.querySelector(".update-content .content-box");
        newContentBox.innerText = updateData.projectUpdateDto.content;

        const newDataBox = newUpdateRow.querySelector(".update-date .date-box");
        newDataBox.innerText = formatDateTime(updateData.projectUpdateDto.created_at, 2);
        const newCommentCount = newUpdateRow.querySelector(".update-date .comment-box");
        newCommentCount.innerText = `댓글 ${updateData.commentCount}개`;

        newUpdateBox.appendChild(newUpdateRow);
    }

    return newUpdateBox;
}

// 업데이트 : 목록 출력
function loadUpdateList() {
    const updateList = document.querySelector("#update-wrapper .update-list");
    if(!updateList) return;

    const url = `/api/funding/projectUpdateDataList?project_id=${getProjectId()}`;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        updateList.innerHTML = "";

        const updateData = getUpdateList(response.data);
        updateList.appendChild(updateData);

        // 높이 확인 후 cover 적용
        addUpdateCover();
    });
}

// 업데이트 : 목록 게시물 높이 확인 후 cover 적용
function addUpdateCover() {
    const updateContentBox = document.querySelectorAll("#update-wrapper .update-list .content-box");
    updateContentBox.forEach(content => {
        if(content.offsetHeight >= 240) { // 240px 이상인 경우
            const updateContent = content.closest(".update-content");
            const updateId = updateContent.getAttribute("data-update-id");
            
            // cover
            const newContentCover = document.createElement("div");
            newContentCover.classList.add("content-cover");
            updateContent.appendChild(newContentCover);

            // btn
            const updateMore = document.createElement("div");
            updateMore.classList.add("update-more", "mt-1");

            const moreButton = document.createElement("button");
            moreButton.type = "button";
            moreButton.classList.add("new-btn-none", "d-flex", "align-items-center", "mx-auto", "py-1", "px-5", "border", "rounded-pill", "new-fs-8", "text-dark");

            const moreIcon = document.createElement("span");
            moreIcon.classList.add("material-symbols-outlined", "fw-light", "fs-6");
            moreIcon.innerText = "keyboard_arrow_down";
            const moreText = document.createElement("span");
            moreText.innerText = "더보기";
            // 링크 걸기
            moreButton.onclick = () => loadUpdateView(updateId);

            moreButton.appendChild(moreIcon);
            moreButton.appendChild(moreText);
            updateMore.appendChild(moreButton);
            updateContent.after(updateMore);
        }
    });
}


/********** 프로젝트 상세 : 커뮤니티 **********/

// 커뮤니티 : 작성
function writeCommunity() {
    const communityWrapper = document.querySelector("#community-wrapper");

    // 내용 작성 확인
    const communityContent = communityWrapper.querySelector("[name='community_content']");
    if(communityContent.value == '' || !communityContent.value) {
        alert("커뮤니티 내용을 작성해주세요.");
        communityContent.focus();
        return;
    }

    const formData = new FormData();
    formData.append("project_id", getProjectId());
    formData.append("content", communityContent.value);

    const url = "/api/funding/registerCommnunity";
    fetch(url, {
        method: "POST",
        body: formData
    })
    .then(response => response.json())
    .then((response) => {
        printNotice("커뮤니티 작성이 완료되었습니다.");
        communityContent.value = ""; // 리셋

        loadCommunityList();
    });
}

// 커뮤니티 : 댓글 폼 생성
function writeComment(id) {
    // 작성폼 생성
    const newCommentForm = document.createElement("div");
    newCommentForm.classList.add("comment-form", "mt-2");

    const newDFlex = document.createElement("div");
    newDFlex.classList.add("d-flex");

    const newTextarea = document.createElement("textarea");
    newTextarea.name = "comment-content";
    newTextarea.classList.add("flex-grow-1", "p-2", "border", "rounded-2", "outline-none", "resize-none", "new-fs-85");
    newTextarea.style.height = "55px";
    const newButton = document.createElement("button");
    newButton.type = "button";
    newButton.classList.add("new-btn-none", "ms-2", "px-3", "rounded-2", "new-bg-gray", "new-fs-85");
    newButton.innerText = "작성";
    newButton.setAttribute("onclick", `writeCommunityComment(${id})`);

    newDFlex.appendChild(newTextarea);
    newDFlex.appendChild(newButton);
    newCommentForm.appendChild(newDFlex);

    // 다른 작성폼 삭제
    const commentForms = document.querySelectorAll(".community-list .comment-form");
    for(form of commentForms) {
        form.remove();
    }

    // 작성폼 추가
    const target = event.target;
    const communityCol = target.closest(".community-col");
    communityCol.appendChild(newCommentForm);
    
}

// 커뮤니티 : 댓글 작성
function writeCommunityComment(id) {

    const target = event.target;
    const commentForm = target.closest(".comment-form");

    const commentContent = commentForm.querySelector("[name='comment-content']");
    if(commentContent.value == '' || !commentContent.value) {
        alert("댓글 내용을 작성해주세요.");
        commentContent.focus();
        return;
    }

    const formData = new FormData();
    formData.append("community_id", id);
    formData.append("content", commentContent.value);

    const url = "/api/funding/registerCommunityComment";
    fetch(url, {
        method: "POST",
        body: formData
    })
    .then(response => response.json())
    .then((response) => {
        printNotice("커뮤니티 댓글 작성이 완료되었습니다.");

        loadCommunityList();
    });
}

// 커뮤니티 : 데이터 생성
function getCommunityList(data) {
    const communityDataList = data.projectCommunityDataList;
    const communityCount = data.projectCommunityListCount;

    // 데이터 자체가 없으면
    if(communityCount === 0) {
        const emptyBox = document.createElement("div");
        emptyBox.classList.add("list-none-box");
        const emptyIcon = document.createElement("span");
        emptyIcon.classList.add("material-symbols-outlined", "icon-draft-order");
        emptyIcon.innerText = "draft_orders";
        const emptyBoxText = document.createElement("p");
        emptyBoxText.innerText = "아직 게시글이 없습니다.";
        emptyBox.appendChild(emptyIcon);
        emptyBox.appendChild(emptyBoxText);
        return emptyBox;
    }

    const communityRow = document.querySelector("#template-wrapper .cummunity-template .community-row");
    const newCommunityBox = document.createElement("div");

    for(communityData of communityDataList) {
        const newCommunityRow = communityRow.cloneNode(true);

        if(communityData.userDto.profile_img != null) {
            const newProfileImage = newCommunityRow.querySelector(".profile-box .profile-image");
            newProfileImage.src = `/images/${communityData.userDto.profile_img}`;
        }
        
        const newNickname = newCommunityRow.querySelector(".profile .nickname");
        newNickname.innerText = communityData.userDto.nickname;
        
        const newContentBox = newCommunityRow.querySelector(".community-content .content-box");
        newContentBox.innerText = communityData.projectCommunityDto.content;
        
        const newDataBox = newCommunityRow.querySelector(".community-date .date-box");
        newDataBox.innerText = formatDateTime(communityData.projectCommunityDto.created_at, 3);

        // 창작자인 경우에만
        const newCommentBtn = newCommunityRow.querySelector(".btn-community-comment");
        if(data.isCreator) {
            newCommentBtn.setAttribute("onclick", `writeComment(${communityData.projectCommunityDto.id})`);
        }else{
            newCommentBtn.remove();
        }

        // 답글 확인 : 창작자만 가능 > 창작자 정보로 출력
        const communityComment = newCommunityRow.querySelector(".community-comment");
        communityComment.remove();

        if(communityData.commentDataList.length > 0) {
            for(commentData of communityData.commentDataList) {
                const newCommunityComment = communityComment.cloneNode(true);

                if(commentData.creatorDto.profile_location != null) {
                    const newProfileImage = newCommunityComment.querySelector(".profile-box .profile-image");
                    newProfileImage.src = `/images/${commentData.creatorDto.profile_location}`;
                }
            
                const newNickname = newCommunityComment.querySelector(".profile .nickname");
                newNickname.innerText = commentData.creatorDto.creator_name;
                const newContentBox = newCommunityComment.querySelector(".community-content .content-box");
                newContentBox.innerText = commentData.commentDto.content;
                const newDataBox = newCommunityComment.querySelector(".community-date .date-box");
                newDataBox.innerText = formatDateTime(commentData.commentDto.created_at, 2);

                newCommunityRow.appendChild(newCommunityComment);
            }
        }
        
        newCommunityBox.appendChild(newCommunityRow);
    }

    return newCommunityBox;
}

// 커뮤니티 : 목록 출력
function loadCommunityList() {
    const commnuityList = document.querySelector("#community-wrapper .community-list");
    if(!commnuityList) return;

    const url = `/api/funding/projectCommunityData?project_id=${getProjectId()}`;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        commnuityList.innerHTML = "";

        const communityData = getCommunityList(response.data);
        commnuityList.appendChild(communityData);
    });
}


/********** 프로젝트 상세 : 후기 **********/

// 후기 : 데이터 생성
function getReviewList(reviewResponseData) {
    console.log(reviewResponseData);
    // 데이터 자체가 없으면
    if(reviewResponseData.projectReviewListCount === 0) {
        const emptyBox = document.createElement("div");
        emptyBox.classList.add("list-none-box");
        const emptyIcon = document.createElement("span");
        emptyIcon.classList.add("material-symbols-outlined", "icon-draft-order");
        emptyIcon.innerText = "draft_orders";
        const emptyBoxText = document.createElement("p");
        emptyBoxText.innerText = "아직 등록된 후기가 없습니다.";
        emptyBox.appendChild(emptyIcon);
        emptyBox.appendChild(emptyBoxText);
        return emptyBox;
    }

    const reviewRow = document.querySelector("#template-wrapper .review-template .review-row");
    const newReviewBox = document.createElement("div");

    for(reviewData of reviewResponseData.projectReviewDataList) {
        const newReviewRow = reviewRow.cloneNode(true);

        if(reviewData.userDto.profile_img != null) {
            const newProfileImage = newReviewRow.querySelector(".profile-box .profile-image");
            newProfileImage.src = `/images/${reviewData.userDto.profile_img}`;
        }
        
        const newNickname = newReviewRow.querySelector(".profile .nickname");
        newNickname.innerText = reviewData.userDto.nickname;
        
        const newImageList = newReviewRow.querySelector(".review-content .image-list");
        if(reviewData.reviewFileList.length > 0) {
            for(reviewFile of reviewData.reviewFileList) {
                const newLi = document.createElement("li");
                newLi.classList.add("me-2");
                
                const newImgBox = document.createElement("div");
                newImgBox.classList.add("overflow-hidden", "img-p-box", "new-pb-100");
                newImgBox.style.width = "95px";
                const newImg = document.createElement("img");
                newImg.classList.add("img", "image-thumbnail");
                newImg.alt = "리뷰 이미지";
                newImg.src = `/images/${reviewFile.location}`;

                newImgBox.appendChild(newImg);
                newLi.appendChild(newImgBox);
                newImageList.appendChild(newLi);
            }
        }else{
            newImageList.remove();
        }

        const newContentBox = newReviewRow.querySelector(".review-content .content-box");
        newContentBox.innerText = reviewData.projectReview.content;
        
        const newKeywordList = newReviewRow.querySelector(".review-keyword-box .keyword-list"); //
        if(reviewData.selectedKeywordList.length > 0) { // 필수라서 없을 수는 없음
            for(keyword of reviewData.selectedKeywordList) {
                const newLi = document.createElement("li");
                newLi.classList.add("me-2", "mb-2", "py-1", "px-2", "rounded-pill", "new-bg-gray", "fw-medium");
                newLi.innerText = keyword.keyword;
                newKeywordList.appendChild(newLi);
            }
        }

        // GPT 요약
        const newReviewSummary = newReviewRow.querySelector(".review-summary-box");
        const newReviewSummaryText = newReviewSummary.querySelector(".review-summary");
        if(reviewData.projectReviewGpt) {
            newReviewSummaryText.innerText = reviewData.projectReviewGpt.gpt_content;
        }else{
            newReviewSummary.remove();
        }

        const newDataBox = newReviewRow.querySelector(".review-date .date-box");
        newDataBox.innerText = formatDateTime(reviewData.projectReview.created_at, 2);

        newReviewBox.appendChild(newReviewRow);
    }

    return newReviewBox;
}

// 후기 : 목록 출력
function loadReviewList() {
    const reviewList = document.querySelector("#review-wrapper .review-list");
    if(!reviewList) return;

    const url = `/api/funding/projectReviewData?project_id=${getProjectId()}`;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        reviewList.innerHTML = "";

        const reviewData = getReviewList(response.data);
        reviewList.appendChild(reviewData);
    });
}


/********** 프로젝트 상세 : 좋아요 **********/

// 프로젝트 id 가져오기
function getProjectId() {
    const urlParams = new URL(window.location.href).searchParams;
    const projectId = urlParams.get("id");
    return projectId;
}

// 로그인 세션 가져오기
let myId = null;
function setSessionUserId() {
    const url = "/six/user/getSessionUserId";
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        if(response.data.id != null) {
            myId = response.data.id;
            console.log("로그인 myId: "+myId);
        }
    });
}

// 로그인 확인
function checkLogin() {
    if(myId == null) {
        if(confirm("로그인 후 이용이 가능합니다. 로그인 하시겠습니까?")) {
            location.href = "/user/loginPage";
        }
        return;
    }
    return true;
}

// 좋아요 
function projectLike() {
    if(!checkLogin()) return;

    const url = `/api/funding/projectLike?project_id=${getProjectId()}`;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        reloadProjectLiked();
        printNotice("관심 프로젝트에 추가되었습니다.");
    });
}

// 좋아요 취소
function projectUnLike() {
    if(!checkLogin()) return;

    const url = `/api/funding/projectUnLike?project_id=${getProjectId()}`;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        reloadProjectLiked();
        printNotice("취소되었습니다.");
    });
} 

// 좋아요 여부 + count 적용
function reloadProjectLiked() {
    if(myId == null) return;

    const url = `/api/funding/projectIsLiked?project_id=${getProjectId()}`;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        const btnLike = document.querySelector(".pledge-btn-box .btn-like");
        const btnIcon = document.querySelector(".pledge-btn-box .like-icon");
        const btnCount = document.querySelector(".pledge-btn-box .like-count");

        if(response.data.projectIsLiked) {
            btnIcon.classList.add("text-danger", "fill-1");
            btnLike.setAttribute("onclick", "projectUnLike()");
        }else{
            btnIcon.classList.remove("text-danger", "fill-1");
            btnLike.setAttribute("onclick", "projectLike()");
        }
        
        btnCount.innerText = response.data.projectLikeCount; // 좋아요 수
    });
}


/********** 프로젝트 상세 : 선물 선택 창 - 후원하기 **********/

// 후원하기 버튼 클릭
function openPledgeBtn() {
    // 선택창 first 섹션 초기화
    resetFirstRewardWrapper();

    // 옵션 선택 핸들러 초기화
    activeRewardItemOption();

    // 오프캔버스 열기
    const offcanvasInstance = bootstrap.Offcanvas.getOrCreateInstance(offcanvasWrapper);
    offcanvasInstance.show();
}

// 후원버튼 닫기
function closePledgeCanvas() {
    // 선택창 first 섹션 초기화
    resetFirstRewardWrapper();
    // 옵션 선택 핸들러 초기화
    activeRewardItemOption();
    // 오프캔버스 닫기
    const offcanvasInstance = bootstrap.Offcanvas.getOrCreateInstance(offcanvasWrapper);
    offcanvasInstance.hide();
}

// 섹션 이동 : first 로
function toFirst(rewardWrapper) {
    const rewardBtnBox = rewardWrapper.querySelector(".reward-pledge-btn-box");
    if(rewardBtnBox) {
        deletePledgeButton();
    }
    
    rewardWrapper.classList.remove("second");
    rewardWrapper.classList.add("first");
}

// 섹션 이동 : second 로
function toSecond(rewardWrapper) {
    rewardWrapper.classList.remove("first");
    rewardWrapper.classList.add("second");
}

// 후원 버튼 생성
function createPledgeButton() {
    const offcanvasWrapper = document.querySelector(".offcanvas-pledge");
    const rewardWrapper = offcanvasWrapper.querySelector(".reward-wrapper");

    const newRewardBtnBox = document.createElement("div");
    newRewardBtnBox.classList.add("reward-btn-box", "reward-pledge-btn-box", "new-fixed-bottom");
    const newContainer = document.createElement("div");
    newContainer.classList.add("container");
    const newPledgeBtn = document.createElement("button");
    newPledgeBtn.classList.add("pledge-btn", "reward-btn");
    newPledgeBtn.type = "button";
    newPledgeBtn.setAttribute("onclick", "fundingPledgeForm()");
    const newPriceSpan = document.createElement("span");
    newPriceSpan.classList.add("fw-semibold");

    // 텍스트 노드 생성
    const textNode1 = document.createTextNode('총 ');
    const textNode2 = document.createTextNode('원 후원하기');

    newPledgeBtn.appendChild(textNode1);
    newPledgeBtn.appendChild(newPriceSpan);
    newPledgeBtn.appendChild(textNode2);
    newContainer.appendChild(newPledgeBtn);
    newRewardBtnBox.appendChild(newContainer);

    const pledgeRewardList = rewardWrapper.querySelector("#pledge-reward-list");
    pledgeRewardList.appendChild(newRewardBtnBox);
}

// 후원 버튼 : 삭제
function deletePledgeButton() {
    setTimeout(() => { // 240926 마지막 컨텐츠 삭제 오류 수정
        const offcanvasWrapper = document.querySelector(".offcanvas-pledge");
        const rewardWrapper = offcanvasWrapper.querySelector(".reward-wrapper");
        const pledgeBtnBox = rewardWrapper.querySelector(".reward-pledge-btn-box");
        pledgeBtnBox.remove();
    }, 0);
}

// 선택창 first 섹션 초기화
function resetFirstRewardWrapper() {
    const offcanvasWrapper = document.querySelector(".offcanvas-pledge");
    const rewardWrapper = offcanvasWrapper.querySelector(".reward-wrapper");

    toFirst(rewardWrapper);
    
    const radioButtons = rewardWrapper.querySelectorAll("input[name='rewards']");
    radioButtons.forEach(radio => {
        radio.checked = false;

        const optionBoxs = radio.closest(".reward-row").querySelectorAll(".option-select-box");
        if(optionBoxs) {
            optionBoxs.forEach(box => {
                box.classList.add("d-none");
                const optionSelect = box.querySelector("select[name='option_id']");
                if(optionSelect) { optionSelect.value = ""; }
                const optionContent = box.querySelector("[name='option_content']");
                if(optionContent) { optionContent.value = ""; }
            });
        }
    });
}

// 선물 아이템 옵션 선택 핸들러 : 옵션 있는 선물 아이템의 경우 selectbox 출력됨
function activeRewardItemOption() {
    const rewardWrapper = document.querySelector(".reward-wrapper");
    const radioButtons = rewardWrapper.querySelectorAll("input[name='rewards']");
    
    radioButtons.forEach(radio => {
        radio.addEventListener('change', (event) => {
            rewardWrapper.querySelectorAll(".option-select-box").forEach(box => {
                box.classList.add("d-none");
            });

            const selectedRadio = event.target;
            optionSelectBoxs = selectedRadio.closest(".reward-row").querySelectorAll(".option-select-box");
            if(optionSelectBoxs) {
                optionSelectBoxs.forEach(selectBox => {
                    selectBox.classList.remove("d-none");
                });
            }
        });
    });
}

// 선물 선택하기 : 후원 전
function selectRewardBtn() {
    
    const target = event.target;
    const rewardWrapper = target.closest(".reward-wrapper");

    // 선물 선택 확인
    const selectedRadio = rewardWrapper.querySelector('input[name="rewards"]:checked');
    if(!selectedRadio) {
        alert("선물을 선택해 주세요.");
        return;
    }

    const optionSelectBoxs = selectedRadio.closest('.reward-row').querySelectorAll('.option-select-box');
    let optionIdArray = []; // 옵션 id 배열
    let optionContentArray = []; // 주관식 옵션 내용 배열

    if (optionSelectBoxs) {
        // 옵션 선택 확인
        for(let i=0; i<optionSelectBoxs.length; i++) {
            const selectElement = optionSelectBoxs[i].querySelector('.option-box');
            if (selectElement && selectElement.value === "") {
                alert('선물의 옵션을 확인해주세요.');
                selectElement.focus();
                return;
            }
        }

        // 선택한 옵션 배열 만들기
        optionSelectBoxs.forEach(option => {
            const optionIdBox = option.querySelector("[name='option_id']");
            optionIdArray.push(optionIdBox.value);

            const optionContentBox = option.querySelector("[name='option_content']");
            if(optionContentBox) {
                optionContentArray.push(optionContentBox.value);
            }else{ // 옵션이 있으면 content 도 무조건 개수에 맞게 추가..!
                optionContentArray.push("");
            }
        })
    }

    // 선택한 선물 추가
    addSelectedReward(selectedRadio.value, optionIdArray, optionContentArray);

    toSecond(rewardWrapper);
}

// 선택한 선물 삭제하기
function deleteSelectedReward() {
    const target = event.target;
    const targetRewardRow = target.closest(".reward-row");
    targetRewardRow.remove();

    printNotice("선물이 삭제되었습니다.");

    // 마지막이면은?
    const formRewardRow = document.querySelectorAll("#pledge-reward-form .reward-row");
    if(formRewardRow.length === 0) {
        resetFirstRewardWrapper();
    }

    calcRewardTotalPrice();
}

// 선택한 선물 토탈 금액 출력
function calcRewardTotalPrice() {
    const rewardBtnBox = document.querySelector("#pledge-reward-list .reward-pledge-btn-box");
    if(!rewardBtnBox) {
        createPledgeButton();
    }

    const formRewardRows = document.querySelectorAll("#pledge-reward-form .reward-row");

    let totalPrice = 0;
    formRewardRows.forEach(row => {
        const rewardAmountStr = row.querySelector(".reward-price").textContent;
        const rewardAmount = parseInt(rewardAmountStr.replace(/[^0-9]/g, ''), 10); // 숫자 외 모든 문자 제거 후 정수 변환
        const rewardQuantity = parseInt(row.querySelector("[name='quantity[]']").value, 10);

        const rewardPrice = rewardAmount * rewardQuantity;

        totalPrice += rewardPrice;
    });

    const pledgeBtnSpan = document.querySelector("#pledge-reward-list .pledge-btn span");
    pledgeBtnSpan.innerText = formatNumberComma(totalPrice);
}

// 상품 수량 변경
function calcRewardQuantity() {
    function updateQuantity(button, delta) {
        const quantityInput = button.parentElement.querySelector("[name='quantity[]']");
        let currentQuantity = parseInt(quantityInput.value, 10);

        if(isNaN(currentQuantity)) {
            currentQuantity = 1;
        }
        
        currentQuantity += delta;

        if(currentQuantity < 1) {
            alert("최소 한 개 이상 주문이 가능합니다.");
            currentQuantity = 1;
        }

        quantityInput.value = currentQuantity;
        calcRewardTotalPrice();
    }

    document.querySelectorAll(".plus-btn").forEach(button => {
        button.onclick = () => updateQuantity(button, 1);
    });

    document.querySelectorAll(".minus-btn").forEach(button => {
        button.onclick = () => updateQuantity(button, -1);
    });

    calcRewardTotalPrice();
}

// 선택한 선물 담기
function addSelectedReward(rewardId, optionIds, optionContents) {

    // 기선택 항목에 존재 할 경우
    const existFormRow = document.querySelectorAll("#pledge-reward-form .reward-row");
    for(let row of existFormRow) {
        const existRewardId = row.querySelector("[name='reward_id[]']").value;
        const existOptionId = row.querySelector("[name='option_id[]']").value;
        const existOptionContent = row.querySelector("[name='option_content[]']").value;

        if(existRewardId == rewardId && existOptionId == optionIds && existOptionContent == optionContents) {
            const existQuantity = row.querySelector("[name='quantity[]']");
            existQuantity.value = parseInt(existQuantity.value) + 1;
            
            calcRewardQuantity();

            return;
        }
    };

    const pledgeRewardList = document.getElementById("pledge-reward-list");

    // 선물정보 단일 값 가져오기 => optionContents 는 후원시 저장만 하면 됨
    const url = "/api/funding/projectRewardData";
    fetch(url, {
        method: "post",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: `id=${rewardId}&optionIds=${optionIds.join('&optionIds=')}` // &optionContents=${optionContents.join('&optionContents=')}
    })
    .then(response => response.json())
    .then((response) => {
        const rewardData = response.data.projectRewardData;
        
        const rewardRowTemplate = document.querySelector("#template-wrapper .reward-template .reward-row");
        const newRewardRow = rewardRowTemplate.cloneNode(true);

        // 넘길 input 값
        const nameRewardId = newRewardRow.querySelector("[name='reward_id[]']");
        nameRewardId.value = rewardId;
        const nameOptionIds = newRewardRow.querySelector("[name='option_id[]']");
        nameOptionIds.value = optionIds;
        const nameOptionContent = newRewardRow.querySelector("[name='option_content[]']");
        nameOptionContent.value = optionContents;

        // 선물 정보 출력
        const rewardTitle = newRewardRow.querySelector(".reward-title");
        rewardTitle.innerText = rewardData.projectRewardDto.title;
        const rewardPrice = newRewardRow.querySelector(".reward-price");
        rewardPrice.innerText = `${formatNumberComma(rewardData.projectRewardDto.amount)}원`;

        const rewardSelectedItem = newRewardRow.querySelector(".reward-selected-item");
        rewardSelectedItem.innerHTML = "";

        // 아이템 이 있는 경우
        if(rewardData.projectItemDataList.length > 0) { 
            const newUl = document.createElement("ul");

            let optionIndext = 0; // 옵션 개수만큼 +
            for(rewardItemData of rewardData.projectItemDataList){
                const newLi = document.createElement("li");

                const newSelectedItemName = document.createElement("p");
                newSelectedItemName.classList.add("selected-item-name");
                newSelectedItemName.innerText = rewardItemData.projectItemDto.name;
                newLi.appendChild(newSelectedItemName);

                // 옵션이 있는 경우
                if(rewardItemData.projectItemOption) { 
                    const newSelectedOption = document.createElement("p");
                    newSelectedOption.classList.add("selected-option");
                    if(rewardItemData.projectItemDto.option_type == '1') { // 주관식 옵션
                        newSelectedOption.innerText = `옵션 : ${optionContents[optionIndext]}`; // content 는 옵션 개수 만큼 넣었기 때문
                    }else if(rewardItemData.projectItemDto.option_type == '2') { // 객관식 옵션
                        newSelectedOption.innerText = `옵션 : ${rewardItemData.projectItemOption.option_name}`;
                    }
                    newLi.appendChild(newSelectedOption);
                    optionIndext++; // 옵션이 있으면 증가
                }

                newUl.appendChild(newLi);
            }
            rewardSelectedItem.appendChild(newUl);
        }

        const deleteButton = newRewardRow.querySelector(".delete-btn");
        deleteButton.onclick = () => deleteSelectedReward();

        const pledgeRewardForm = pledgeRewardList.querySelector("#pledge-reward-form");
        pledgeRewardForm.appendChild(newRewardRow);

        calcRewardQuantity();
    });
}

// 선물 선택창으로 이동
function changeRewardList() {
    resetFirstRewardWrapper();
}

// 주문서로 이동
function fundingPledgeForm() {
    const form = document.getElementById("pledge-reward-form");

    // 선물을 선택했는지 확인
    const rewardIds = form.querySelectorAll("[name='reward_id[]']");
    if(!rewardIds.length) {
        alert("선물을 선택해주세요.");
        return;
    }

    // 선물 id 유무 확인
    for(let i=0; i<rewardIds.length; i++) {
        const rewardValue = rewardIds[i].value;
        if (rewardValue === "") {
            alert('선물을 선택해주세요...');
            return;
        }
    }

    // pledgeDto 리스트로 만들기
    const pledgeDtoList = [];

    const formRewardRows = form.querySelectorAll(".reward-row");
    formRewardRows.forEach((row) => {
        const rewardId = row.querySelector("[name='reward_id[]']").value;
        const optionId = row.querySelector("[name='option_id[]']").value.split(',').map(id => id.trim());
        const optionContent = row.querySelector("[name='option_content[]']").value.split(',').map(content => content.trim()); // 옵션 개수만큼 넣어놨기 때문에 개수만큼 잘라 넣음
        const quantity = row.querySelector("[name='quantity[]']").value;

        // 각 반복에서 새로운 Dto 만들기
        const pledgeDto = {
            reward_id: parseInt(rewardId, 10),
            option_id: optionId,
            option_content: optionContent,
            quantity: parseInt(quantity, 10)
        }

        pledgeDtoList.push(pledgeDto);
    });
    
    // project_id 추가
    const urlParams = new URL(window.location.href).searchParams;
    const projectId = urlParams.get("id");
    if(!projectId) {
        alert("프로젝트 ID 를 확인할 수 없습니다.");
        return;
    }

    // DB 에 대기로 저장 - Dto 있음
    const pledgeData = {
        projectId: projectId,
        pledgeDtoList: pledgeDtoList
    };

    const url = "/api/funding/createProjectSupport";
    fetch(url, {
        method: "POST",
        headers: {
            "Content-Type" : "application/json"
        },
        body: JSON.stringify(pledgeData)
    })
    .then(response => response.json())
    .then(response => {
        if(response.result == "fail") {
            alert(response.data.msg);
            window.location.href = "/user/loginPage";
            return;
        }

        // 후원하기 페이지로 이동
        window.location.href = "/funding/pledgePage";
    });
}

// 후원하기 : 진행
function submitFundingPledge() {
    const pledgeForm = document.getElementById("projectPledgeForm");
    if(! projectPledgeForm) return;

    // 배송지 확인
    const recipientName = pledgeForm.recipient_name;
    if(recipientName.value == "" || !recipientName.value) {
        alert("수령인을 입력해주세요.");
        recipientName.focus();
        return;
    }
    const addressDetail = pledgeForm.address_detail;
    if(addressDetail.value == "" || !addressDetail.value) {
        alert("배송지를 입력해주세요.");
        addressDetail.focus();
        return;
    }
    const recipientPhone = pledgeForm.recipient_phone;
    if(recipientPhone.value == "" || !recipientPhone.value) {
        alert("배송지 연락처를 입력해주세요.");
        recipientPhone.focus();
        return;
    }

    // 결제 수단 확인
    const isPaymethod = pledgeForm.querySelector('input[name="paymethod"]:checked');
    if(!isPaymethod) {
        alert("결제 수단을 선택해주세요.");
        return;
    }

    // 동의 확인
    const pledgeAgrees = pledgeForm.querySelectorAll(".pledge-agree");
    let isAgree = true;
    pledgeAgrees.forEach(agree => {
        if(!agree.checked) {
            isAgree = false;
        }
    })
    if(!isAgree) {
        alert("후원 진행 안내사항에 관하여 동의해주셔야 후원이 가능합니다.");
        pledgeAgrees[0].focus();
        return;
    }

    const formData = new FormData(pledgeForm);

    // DB 저장 후 카카오 페이 결제 팝업창 연결
    const url = "/api/funding/submitProjectPledge";
    fetch(url, {
        method: "POST",
        body: formData
    })
    .then(responst => responst.json())
    .then((response) => {
        if(response.result == "fail") {
            alert(response.data.msg);
            window.location.href = "/funding";
            return;
        }
        // window.location.href = "/funding/pledgeCompleted";
        
        // pc, mob 구분
        if (isMobile()) {
            window.location.href = response.data.next_redirect_mobile_url;
        }else{
            window.location.href = response.data.next_redirect_pc_url;
        }
    });
}


/********** 후원하기 : 주문서 **********/

// 주문하는 프로젝트, 선물 정보 출력 :: local storage 쓸 때 쓴거..
function loadPledgeData() {
    let totalPrice = 0;
    const projectPledgeForm = document.getElementById("projectPledgeForm");
    if(!projectPledgeForm) return;

    const projectId = JSON.parse(localStorage.getItem("projectId"));
    const pledgeDtoList = JSON.parse(localStorage.getItem("pledgeDtoList")); // Local Storage 에서 가져옴

    const pledgeData = {
        projectId: projectId,
        pledgeDtoList: pledgeDtoList
    }

    const url = "/api/funding/getProjectPledgeData";
    fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(pledgeData)
    })
    .then(response => response.json())
    .then((response) => {
        const projectData = response.data.projectData;
        const rewardDataList = response.data.rewardDataList;

        const projectPledgeForm = document.getElementById("projectPledgeForm");
        
        // 프로젝트 정보
        const thumbnailImg = projectPledgeForm.querySelector(".thumbnail-box .img");
        thumbnailImg.src = `/images/${projectData.thumbnail_location}`;
        const categoryName = projectPledgeForm.querySelector(".category-name");
        categoryName.innerText = projectData.category_name;
        const creatorName = projectPledgeForm.querySelector(".creator-name");
        creatorName.innerText = projectData.creator_name;
        const projectTitle = projectPledgeForm.querySelector(".project-title");
        projectTitle.innerText = projectData.project_title;

        // 선물 정보 > 넘기는건 저장된 local storage 활용하기
        const pledgeRewardWrapper = projectPledgeForm.querySelector(".pledge-reward-wrapper");

        const pledgeRewardList = pledgeRewardWrapper.querySelector(".pledge-reward-list");
        pledgeRewardList.innerHTML = "";

        for(const rewardData of rewardDataList) {
            const newLi = document.createElement("li");
            newLi.classList.add("mb-2");

            // 선물 이름
            const newRewardTitle = document.createElement("div");
            newRewardTitle.classList.add("reward-title");
            const newTitleText = document.createElement("span");
            newTitleText.innerText = rewardData.projectRewardDto.title;
            const newQuantity = document.createElement("span");
            newQuantity.classList.add("font-roboto");
            newQuantity.innerText = ` (x${rewardData.quantity})`;
            newRewardTitle.appendChild(newTitleText);
            newRewardTitle.appendChild(newQuantity);
            newLi.appendChild(newRewardTitle);

            // 선택한 아이템 : 있을 경우
            if(rewardData.projectItemDataList.length > 0) {
                const newItemBox = document.createElement("div");
                newItemBox.classList.add("item-box", "mt-1");
                
                for(const rewardItemData of rewardData.projectItemDataList) {
                    const newItemRow = document.createElement("div");
                    newItemRow.classList.add("item-row", "list-big-dot", "ps-3");
                    const newItemName = document.createElement("div");
                    newItemName.classList.add("item-name", "new-fs-85");
                    newItemName.innerText = rewardItemData.projectItemDto.name;
                    newItemRow.appendChild(newItemName);

                    // 옵션이 잇는 경우
                    if(rewardItemData.projectItemOption) { // 객관식
                        const newOptionName = document.createElement("div");
                        newOptionName.classList.add("option-name", "new-fs-8", "text-body-secondary");
                        newOptionName.innerText = `옵션 : ${rewardItemData.projectItemOption.option_name}`;
                        newItemRow.appendChild(newOptionName);
                    }else if(rewardItemData.projectItemContent) { // 주관식
                        const newOptionName = document.createElement("div");
                        newOptionName.classList.add("option-name", "new-fs-8", "text-body-secondary");
                        newOptionName.innerText = `옵션 : ${rewardItemData.projectItemContent}`;   
                        newItemRow.appendChild(newOptionName);
                    }
                    newItemBox.appendChild(newItemRow);
                }

                newLi.appendChild(newItemBox);
            }

            // 예상 전달일
            const newExpectedDate = document.createElement("div");
            newExpectedDate.classList.add("expected-date", "d-flex", "mt-1");
            const newColAuto = document.createElement("div");
            newColAuto.classList.add("col-auto", "py-1", "px-2", "new-bg-gray");
            const newExpectedSpan = document.createElement("span");
            newExpectedSpan.classList.add("text-body-secondary");
            newExpectedSpan.innerText = "예상 전달일";
            const newExpectedText = document.createElement("span");
            newExpectedText.classList.add("ps-1", "text-primary");
            newExpectedText.innerText = formatDateTime(rewardData.rewardExpectedDate.expected_date, 2);
            newColAuto.appendChild(newExpectedSpan);
            newColAuto.appendChild(newExpectedText);
            newExpectedDate.appendChild(newColAuto);
            newLi.appendChild(newExpectedDate);

            pledgeRewardList.appendChild(newLi);
        }
    });
}


/********** 함수 호출 **********/

// 페이지 로딩시
window.addEventListener('DOMContentLoaded', function() {
    // 메인 : 마감임박 - 카운트다운 시작
    endCountdown();

    // 만든프로젝트에서만
    searchProjectStatus(); // 탭 버튼 이벤트 설정
    loadProjectCreated();

    // 프로젝트 목록 - 상품 리스트
    setActiveCategory();
    loadDiscoverList();

    // 프로젝트 상세
    changeProjectDetail();

    // 프로젝트 후원 : 주문서
    // loadPledgeData();

    // 로그인 확인
    setSessionUserId();
});
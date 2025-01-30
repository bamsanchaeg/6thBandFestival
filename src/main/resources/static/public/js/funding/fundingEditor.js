/********** 펀딩 프로젝트 : 글자수, 버튼 **********/

// 글자 수 카운팅
function setupCharacterCount(inputId, maxLength, minLength=1) {
    const input = document.getElementById(inputId);
    const charCount = input.closest(".peditor-box").querySelector(".peditor-text-count");
    const initialValue = input.value;

    // 초기 설정
    updateCharactorCount();

    // 텍스트 입력시
    input.addEventListener("input", updateCharactorCount);

    function updateCharactorCount() {
        const textLength = input.value.length;
        charCount.textContent = `${textLength}/${maxLength}`;

        if (textLength < minLength || textLength > maxLength) {
            offPeditorBtn();
            input.classList.add("peditor-required-border");
            charCount.style.color = '#da4a49';
        } else {
            onPeditorBtn();
            input.classList.remove("peditor-required-border");
            charCount.style.color = '#868e96';
        }
    }
}

// 버튼 활성화
function onPeditorBtn() {
    const peditorBtn = document.getElementById("peditor-btn");
    peditorBtn.disabled = false;
}

// 버튼 비활성화
function offPeditorBtn() {
    const peditorBtn = document.getElementById("peditor-btn");
    peditorBtn.disabled = true;
}

/********** 펀딩 프로젝트 : 등록 **********/

// 카테고리 선택 동작
function handleCategoryChage(event) {
    const target = event.target;

    // textarea 유무 확인
    const startFirstContainer = document.querySelector("#project-start-first > .container");
    if(!startFirstContainer.querySelector("#project-desc")) {
        startFirstContainer.appendChild(projectDescFormTag());

        // 한번 실행 후 이벤트 리스너 제거
        projectCategory.removeEventListener("change", handleCategoryChage);
    }

    // 글자 수 세기 : 프로젝트 요약
    setupCharacterCount("project-desc", 50, 10);
}
const projectCategory = document.getElementById("project-catetory");
projectCategory.addEventListener("change", handleCategoryChage);

// 프로젝트 소개 form : 이것도 생성을 굳이 하는게 맞을가..
function projectDescFormTag() {
    const newPeditorBox = document.createElement("div");
    newPeditorBox.classList.add("peditor-box", "paditor-mt");

    const newPeditorTitle = document.createElement("div");
    newPeditorTitle.classList.add("peditor-title");
    newPeditorTitle.innerText = "프로젝트를 간단하게 소개해주세요.";

    const newPeditorDesc = document.createElement("div");
    newPeditorDesc.classList.add("peditor-desc");
    newPeditorDesc.innerText = "나중에 수정 가능하니 편하게 작성해주세요.";
    
    const newPeditorContent = document.createElement("div");
    newPeditorContent.classList.add("peditor-content");
    const newPeditorTextarea = document.createElement("textarea");
    newPeditorTextarea.classList.add("peditor-namebox", "peditor-textarea");
    newPeditorTextarea.id = "project-desc";
    newPeditorTextarea.placeholder = "프로젝트 요약을 입력해주세요.";
    newPeditorTextarea.name = "project_desc";
    newPeditorContent.appendChild(newPeditorTextarea);

    const newTextCount = document.createElement("div");
    newTextCount.classList.add("peditor-text-count");
    newTextCount.innerText = "0/50";

    // 합치기
    newPeditorBox.appendChild(newPeditorTitle);
    newPeditorBox.appendChild(newPeditorDesc);
    newPeditorBox.appendChild(newPeditorContent);
    newPeditorBox.appendChild(newTextCount);

    return newPeditorBox;
}

// 동의 체크박스 이벤트 등록
function setupAgreeCheckbox() {
    const projectStartSecond = document.getElementById("project-start-second");
    const projectStartAgrees = projectStartSecond.querySelectorAll(".project-start-agree");
    
    projectStartAgrees.forEach(agreeBox => {
        agreeBox.addEventListener("change", checkStartSecond);
    });
}

// 전체 동의 여부 확인
function checkStartSecond() {
    const projectStartSecond = document.getElementById("project-start-second");
    const projectStartAgrees = projectStartSecond.querySelectorAll(".project-start-agree");

    let allChecked = true; // 전체 동의 여부

    projectStartAgrees.forEach(agreeBox => {
        if(!agreeBox.checked) { allChecked = false; }
    });

    if(allChecked) {
        onPeditorBtn();
    }else{
        offPeditorBtn();
    }
}

// 프로젝트 생성 : step
let isAgreeCheckboxSetup = false; // 동의 체크박스 이벤트 등록 여부
let currentStep = 1; // 현재 step

function projectStart() {
    const projectStartFirst = document.getElementById("project-start-first");
    const projectStartSecond = document.getElementById("project-start-second");
    const peditorBtn = document.querySelector(".main-project .peditor-btn");

    if(currentStep == 1) {
        projectStartFirst.classList.remove("active");
        projectStartSecond.classList.add("active");
        
        // 버튼 활성화 확인 : 동의항목으로
        if(!isAgreeCheckboxSetup) {
            setupAgreeCheckbox();
            isAgreeCheckboxSetup = true;
        }
        checkStartSecond();
        peditorBtn.innerText = "시작하기";
        currentStep = 2;

    } else {
        // 프로젝트 생성
        const projectStartFist = document.getElementById("project-start-first");
        const categoryId = projectStartFirst.querySelector("#project-catetory");
        const projectDesc = projectStartFirst.querySelector("#project-desc");

        if(categoryId.value == "" || !categoryId.value) {
            alert("프로젝트 카테고리를 선택해주세요.");
            categoryId.focus();
            return;
        }

        if(projectDesc.value == "" || !projectDesc.value) {
            alert("프로젝트 소개를 입력해주세요.");
            projectDesc.focus();
            return;
        }

        const url = "/api/funding/registerProject";
        fetch(url, {
            method: "post",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: `category_id=${categoryId.value}&project_desc=${projectDesc.value}`
        })
        .then(response => response.json())
        .then((response) => {
            if(response.result == "success") {
                window.location.href = `/funding/projectEditorManagement?id=${response.data.projectId}`;
            }else{
                if(response.data.msg) {
                    alert(response.data.msg);
                    window.location.href = "/user/loginPage";
                }
            }
        });
    }
}

// 프로젝트 생성 : 뒤로가기
function backStartStep() {
    if(currentStep == 2) {
        const projectStartFirst = document.getElementById("project-start-first");
        const projectStartSecond = document.getElementById("project-start-second");
        const peditorBtn = document.querySelector(".main-project .peditor-btn");
        
        projectStartFirst.classList.add("active");
        projectStartSecond.classList.remove("active");

        // 버튼 활성화 확인 : 텍스트 입력값으로
        setupCharacterCount("project-desc", 50, 10);
        peditorBtn.innerText = "다음으로";
        currentStep = 1;
    }
}

const urlParams = new URL(window.location.href).searchParams;
const projectId = urlParams.get("id");

if(!projectId || projectId == "") {
    alert("잘못된 접근입니다.");
    window.location.htef = "/";
}

/********** 프로젝트 기획 **********/

// 글자 수 카운팅
let isOverCount = false;
let overInputName = null;

function setupCharacterCount(inputSetting) {
    const charCount = inputSetting.closest(".peditor-box").querySelector(".peditor-text-count");
    const maxLength = inputSetting.dataset.maxlength ? parseInt(inputSetting.dataset.maxlength, 10) : Number.MAX_SAFE_INTEGER;
    const minLength = inputSetting.dataset.minlength ? parseInt(inputSetting.dataset.minlength, 10) : 1;

    // 초기 설정
    updateCharactorCount();

    // 텍스트 입력시
    inputSetting.addEventListener("input", updateCharactorCount);

    function updateCharactorCount() {
        const textLength = inputSetting.value.length;
        charCount.textContent = `${textLength}/${maxLength}`;

        if (textLength < minLength || textLength > maxLength) {
            inputSetting.classList.add("peditor-required-border");
            charCount.style.color = '#da4a49';
            isOverCount = true;
            overInputName = inputSetting;
            return 
        } else {
            inputSetting.classList.remove("peditor-required-border");
            charCount.style.color = '#868e96';
            isOverCount = false;
        }
    }
}

// 시작 시간 출력
const setStartTimeOption = () => {
    const selectBox = document.getElementById("start_time");
    if(!selectBox) {
        return;
    }

    const startTime = 9 * 60; // 6:00를 분 단위로
    const endTime = 18 * 60; // 18:00를 분 단위로
    const interval = 30; // 간격
    const times = [];
    const oldStartAt = document.getElementById("old_start_at").value; // 기존에 설정한 시간

    for (let i = startTime; i <= endTime; i += interval) {
        const hours = Math.floor(i / 60); // 시간 계산
        const minutes = i % 60; // 분 계산

        const formattedTime = `${hours < 10 ? '0' : ''}${hours}:${minutes < 10 ? '0' : ''}${minutes}`; // 시간분 생성
        times.push(formattedTime);
    }

    times.forEach(time => {
        const option = document.createElement("option");
        option.text = time;
        option.value = time;
        if(time == oldStartAt) {
            option.selected = true; 
        }
        selectBox.add(option);
    });
}

// 목표금액 계산
function setAmountCalc() {
    const inputAmount = document.getElementById("expect-amount-number"); // 목표 금액
    if(!inputAmount) {
        return;
    }

    const calcAmount = document.querySelector("#peditor-form .calc-amount"); // 실수령액
    const calcFees = document.querySelector("#peditor-form .fees-amount"); // 수수료

    const amountBox = document.querySelector("#peditor-form .amount-box");
    const newFeedback = document.createElement("div");
    newFeedback.classList.add("feedback-msg");

    // 숫자만 남기고 콤마 추가
    function allowOnlyNumbers() {
        const cleanedValue = inputAmount.value.replace(/[^0-9]/g, ''); // 입력된 값에서 숫자만 남기기
        const formattedValue = parseInt(cleanedValue, 10).toLocaleString(); // 정수로 변환 및 콤마 추가
        inputAmount.value = isNaN(parseInt(cleanedValue, 10)) ? '' : formattedValue;
    }

    // 수수료와 실수령액을 계산
    function validateAndCalculate() {
        allowOnlyNumbers();
        const value = parseInt(inputAmount.value.replace(/,/g, ''), 10);

        if (isNaN(value)) {
            newFeedback.textContent = '필수 항목입니다.';
            amountBox.after(newFeedback);

            calcAmount.textContent = "0원";
            calcFees.textContent = "0원";
        } else {
            if(value < 500000) {
                newFeedback.textContent = "50만원 이상의 금액을 입력해주세요.";
                amountBox.after(newFeedback);
            }else{
                newFeedback.remove();
            }

            const fee = Math.round(value * 0.08); // 수수료 반올림
            const netAmount = value - fee;
            calcAmount.innerText = `${netAmount.toLocaleString()}원`;
            calcFees.innerText = `${fee.toLocaleString()}원`;
        }
    }

    // input 이벤트 리스너 추가
    inputAmount.addEventListener('input', validateAndCalculate);
    // 페이지 로드시 적용
    validateAndCalculate();
}

// 첨부파일 : 목록 생성
function getDescFileData(fileDataList, oldName) {
    const newDiv = document.createElement("div");
    newDiv.classList.add("file-preview-box");

    if(fileDataList.length === 0) { return newDiv; }

    const newFileTitle = document.createElement("p");
    newFileTitle.classList.add("mt-2", "new-fs-85");
    const newSpan = document.createElement("span");
    newSpan.classList.add("fw-medium");
    newSpan.innerText = "[등록된 파일 목록]";
    const newSpanDb = document.createElement("span");
    newSpanDb.classList.add("new-fs-75", "text-body-tertiary");
    newSpanDb.innerText = " *DB에서만 삭제 및 등록됩니다";
    newFileTitle.appendChild(newSpan);
    newFileTitle.appendChild(newSpanDb);

    const newFilePreview = document.createElement("div");
    newFilePreview.classList.add("peditor-file-preview", "d-flex", "align-items-center", "flex-wrap", "mt-2");

    for(fileData of fileDataList) {
        const newPreviewRow = document.createElement("div");
        newPreviewRow.classList.add("file-preview-row", "col-2", "position-relative");

        const newOldLocation = document.createElement("input");
        newOldLocation.type = "hidden";
        newOldLocation.name = oldName;
        newOldLocation.value = fileData.location;

        const newALink = document.createElement("a");
        newALink.classList.add("img-p-box", "new-pb-90", "me-2", "mb-2");
        newALink.target = "_blank";
        newALink.href = `/images/${fileData.location}`;
        const newImg = document.createElement("img");
        newImg.classList.add("img", "image-thumbnail");
        newImg.alt = "파일 이미지";
        newImg.src = `/images/${fileData.location}`;
        newALink.appendChild(newImg);

        const newBtnBox = document.createElement("div");
        newBtnBox.classList.add("position-absolute");
        newBtnBox.style.right = "10px";
        newBtnBox.style.bottom = "10px";
        const newDeleteBtn = document.createElement("button");
        newDeleteBtn.classList.add("btn-file-delete", "new-btn-none", "bg-dark", "rounded-pill", "material-symbols-outlined", "text-white", "fw-light", "fs-6");
        newDeleteBtn.style.padding = "2px";
        newDeleteBtn.innerText = "close";
        newDeleteBtn.type = "button";
        newDeleteBtn.setAttribute("onclick", "fileDeleteAction()");
        newBtnBox.appendChild(newDeleteBtn);

        newPreviewRow.appendChild(newOldLocation);
        newPreviewRow.appendChild(newALink);
        newPreviewRow.appendChild(newBtnBox);

        newFilePreview.appendChild(newPreviewRow);
    }

    newDiv.appendChild(newFileTitle);
    newDiv.appendChild(newFilePreview);

    return newDiv;
}

// 첨부파일 : 삭제 및 로드
function reloadDescFileData(fileBox, fileListData, oldName) {
    const filePreviewBox = fileBox.querySelector(".file-preview-box");
    if(filePreviewBox) {
        filePreviewBox.remove();
    }

    const descFileData = getDescFileData(fileListData, oldName);
    fileBox.appendChild(descFileData);

    // 버튼 활성화 확인 및 처리
    checkBtnActivation('peditor-form', 'peditor-btn');
}

// 저장 버튼 : 기본 정보, 펀딩 계획, 프로젝트 계획
function projectUpdate() {

    const form = document.getElementById("peditor-form");
    // FormData 객체 생성
    const formData = new FormData(form);
    formData.append('id', projectId); // 프로젝트 id

    // 기본 정보 : 필수값은 유효성 확인 
    if(form.category_id) {
        if(form.category_id.value == "" || !form.category_id.value) {
            alert("카테고리를 선택해주세요.");
            form.category_id.focus();
            return;
        } 
    }
    if(form.project_title) {
        if(form.project_title.value == "" || !form.project_title.value) {
            alert("프로젝트 제목을 입력해주세요.");
            console.log(form.project_title);
            form.project_title.focus();
            return;
        } 
    }
    if(form.project_desc) {
        if(form.project_desc.value == "" || !form.project_desc.value) {
            alert("프로젝트 제목을 입력해주세요.");
            form.project_desc.focus();
            return;
        } 
    }
    if(form.uploadFiles) {
        if(!form.old_thumbnail_location.value && (form.uploadFiles.value == "" || !form.uploadFiles.value)) {
            alert("프로젝트 대표 이미지를 입력해주세요.");
            form.uploadFiles.focus();
            return;
        } 
    }

    // 펀딩 계획
    let start_at = null;
    let end_at = null;
    // 목표금액 : 콤마 때문에 name 값 다르게 지정
    if(form.expect_amount_number) {
        if(form.expect_amount_number.value == "" || !form.expect_amount_number.value) {
            alert("목표 금액을 입력해주세요.");
            form.expect_amount_number.focus();
            return;
        }
        if(form.expect_amount_number.value && removeCommas(form.expect_amount_number.value) < 500000) {
            alert("50만원 이상의 금액을 입력해주세요.");
            form.expect_amount_number.focus();
            return;
        }
        formData.append('expect_amount', removeCommas(form.expect_amount_number.value)); // 목표금액 : 콤마 제거
    }
    if(form.start_date) {
        if(form.start_date.value == "" || !form.start_date.value) {
            alert("시작일을 입력해주세요.");
            form.start_date.focus();
            return;
        }
        start_at = form.start_date.value;
    }
    if(form.start_time) {
        if(form.start_time.value == "" || !form.start_time.value) {
            alert("시작 시간을 입력해주세요.");
            form.start_time.focus();
            return;
        } 
        start_at += " " + form.start_time.value;
        formData.append('start_at', start_at); // 시작 시간
    }
    if(form.end_date) {
        if(form.end_date.value == "" || !form.end_date.value) {
            alert("종료일을 입력해주세요.");
            form.end_date.focus();
            return;
        } 
        end_at = form.end_date.value + ' 23:59:59';
        formData.append('end_at', end_at); // 종료 시간
    }

    // 프로젝트 계획
    if(form.project_introduce) {
        if(form.project_introduce.value == "" || !form.project_introduce.value) {
            alert("프로젝트 소개를 입력해주세요.");
            form.project_introduce.focus();
            return;
        } 
    }
    if(form.budget_desc) {
        if(form.budget_desc.value == "" || !form.budget_desc.value) {
            alert("프로젝트 예산을 입력해주세요.");
            form.budget_desc.focus();
            return;
        }
    }
    if(form.schedule_desc) {
        if(form.schedule_desc.value == "" || !form.schedule_desc.value) {
            alert("프로젝트 일정을 입력해주세요.");
            form.schedule_desc.focus();
            return;
        }
    }
    if(form.team_desc) {
        if(form.team_desc.value == "" || !form.team_desc.value) {
            alert("프로젝트 팀 소개를 입력해주세요.");
            form.team_desc.focus();
            return;
        }
    }
    if(form.reward_desc) {
        if(form.reward_desc.value == "" || !form.reward_desc.value) {
            alert("프로젝트 선물 설명을 입력해주세요.");
            form.reward_desc.focus();
            return;
        }
    }

    // 글자수 확인
    if(isOverCount) {
        alert("글자 수에 맞게 입력해주세요.");
        overInputName.focus();
        return;
    }

    // 파일 첨부 확인 : 첨부하지 않은 경우 null 처리
    let isDescFile = false;
    if(form.introduceFiles) {
        isDescFile = true;
        if(!formData.get("introduceFiles") || formData.get("introduceFiles").name === "") { // 프로젝트 소개
            formData.set("introduceFiles", null);
        }
    }
    if(form.teamdescFiles) {
        isDescFile = true;
        if(!formData.get("teamdescFiles") || formData.get("teamdescFiles").name === "") { // 팀소개
            formData.set("teamdescFiles", null);
        }
    }
    if(form.rewarddescFiles) {
        isDescFile = true;
        if(!formData.get("rewarddescFiles") || formData.get("rewarddescFiles").name === "") { // 선물 설명
            formData.set("rewarddescFiles", null);
        }
    }
    formData.append('isDescFile', isDescFile); // 파일 포함 여부

    const url = "/api/funding/updateProject";
    fetch(url, {
        method: "post",
        body: formData
    })
    .then(response => response.json())
    .then((response) => {
        if(response.result == 'success') {
            console.log("수정 완료");

            // 썸네일 변경된 경우
            if(form.uploadFiles) {
                if(form.uploadFiles.value && (form.old_thumbnail_location.value != form.uploadFiles.value)) {
                    const uploadThumbnail = response.data.uploadThumbnail;

                    form.old_thumbnail_location.value = uploadThumbnail;
                    const thumbnailLink = form.querySelector(".upload-link");
                    thumbnailLink.href = `/images/${uploadThumbnail}`;
                    const thumbnailImg = form.querySelector(".upload-thumbnail");
                    thumbnailImg.src = `/images/${uploadThumbnail}`;

                    form.uploadFiles.value = "";
                }
            }

            // 첨부파일 변경시
            if(form.introduceFiles) {
                const introduceFileBox = form.querySelector(".introduce-file-box");
                const introduceFileList = response.data.projectDescFileData.introduceFileList;
                reloadDescFileData(introduceFileBox, introduceFileList, "old_introduce_location");
                form.introduceFiles.value = "";
            }
            if(form.teamdescFiles) {
                const teamdescFileBox = form.querySelector(".teamdesc-file-box");
                const teamdescFileList = response.data.projectDescFileData.teamdescFileList;
                reloadDescFileData(teamdescFileBox, teamdescFileList, "old_teamdesc_location");
                form.teamdescFiles.value = "";
            }
            if(form.rewarddescFiles) {
                const rewarddescFileBox = form.querySelector(".rewarddesc-file-box");
                const rewarddescFileList = response.data.projectDescFileData.rewarddescFileList;
                reloadDescFileData(rewarddescFileBox, rewarddescFileList, "old_rewarddesc_location");
                form.rewarddescFiles.value = "";
            }

        }else{
            console.log("수정 실패");

        }
    });
    
}

// 저장 버튼 : 창작자
function projectCreator() {

    const form = document.getElementById("peditor-form");
    const formData = new FormData(form);
    // console.log(form.bank_name);

    // 필수값은 유효성 확인 
    if(form.creator_name) {
        if(form.creator_name.value == "" || !form.creator_name.value) {
            alert("창작자 이름을 입력해주세요.");
            form.creator_name.focus();
            return;
        } 
    }
    if(form.uploadProfile) {
        if(!form.old_profile_location.value && (form.uploadProfile.value == "" || !form.uploadProfile.value)) {
            alert("프로필 이미지를 입력해주세요.");
            form.uploadProfile.focus();
            return;
        } 
    }
    if(form.introduce) {
        if(form.introduce.value == "" || !form.introduce.value) {
            alert("창작자 소개를 입력해주세요.");
            form.introduce.focus();
            return;
        } 
    }
    if(form.bank_name) {
        if(form.bank_name.value == "" || !form.bank_name.value) {
            alert("거래 은행을 선택해주세요.");
            form.bank_name.focus();
            return;
        } 
    }
    if(form.account_holder) {
        if(form.account_holder.value == "" || !form.account_holder.value) {
            alert("예금주명을 입력해주세요.");
            form.account_holder.focus();
            return;
        } 
    }
    if(form.account_number) {
        if(form.account_number.value == "" || !form.account_number.value) {
            alert("계좌번호를 입력해주세요.");
            form.account_number.focus();
            return;
        } 
    }
    if(form.account_birth) {
        if(form.account_birth.value == "" || !form.account_birth.value) {
            alert("예금주 생년월일을 입력해주세요.");
            form.account_birth.focus();
            return;
        } 
    }

    // 글자수 확인
    if(isOverCount) {
        alert("글자 수에 맞게 입력해주세요.");
        overInputName.focus();
        return;
    }

    const url = "/api/funding/updateCreator";
    fetch(url, {
        method: "post",
        body: formData
    })
    .then(response => response.json())
    .then((response) => {
        if(response.result == 'success') {
            console.log("수정 완료");

        }else{
            console.log("수정 실패");
            alert(response.data.msg);
            window.location.href = "/user/loginPage";
        }
    });
}

// 버튼 활성화 : 수정 여부 확인
function checkBtnActivation(formId, buttonId) {
    const form = document.getElementById(formId);
    const submitButton = document.getElementById(buttonId);

    const enableButton = () => {
        submitButton.disabled = false;
    }

    const disableButton = () => {
        submitButton.disabled = true;
    }

    // 작성폼에 이벤트 리스너 추가
    const elements = form.querySelectorAll('input, textarea, select');
    elements.forEach((e) => {
        e.addEventListener('input', enableButton);
        if (e.tagName.toLowerCase() == 'select') {
            e.addEventListener('change', enableButton);
        }
    });

    // 파일 삭제 버튼에 이벤트 리스너 추가
    const btns = form.querySelectorAll(".peditor-file-preview .btn-file-delete");
    btns.forEach((e) => {
        e.addEventListener('click', enableButton);
    })

    // 폼 제출 이벤트 리스너 추가
    submitButton.addEventListener('click', (event) => {
        if(!submitButton.disabled) {
            console.log('submit 확인');
        }
        disableButton();
    });

    // 페이지 로드시 버튼 비활성화
    disableButton();
}

// 파일 삭제 버튼
function fileDeleteAction() {
    const target = event.target;
    const filePreviewRow = target.closest(".file-preview-row");
    filePreviewRow.remove();
}

// 문서 로드 후 실행
window.addEventListener('DOMContentLoaded', function () {
    // 카운팅 입력창 확인
    const inputs = document.querySelectorAll(".peditor-setting");
    inputs.forEach(input => {
        setupCharacterCount(input);
    });

    // 시작시간 옵션 설정
    setStartTimeOption();

    // 목표 금액 계산
    setAmountCalc();

    // 버튼 활성화 확인 및 처리
    checkBtnActivation('peditor-form', 'peditor-btn');
    
    // 숫자 입력 필드 초기화
    // setNumbericInput('.only-number-input');
});
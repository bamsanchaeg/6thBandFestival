const urlParams = new URL(window.location.href).searchParams;
const projectId = urlParams.get("id");

if(!projectId || projectId == "") {
    alert("잘못된 접근입니다.");
    window.location.htef = "/";
}

let isProductAvailable = true; // 아이템 유무 확인

// 페이지 로딩시
document.addEventListener('DOMContentLoaded', function() {
    changePage("reward");
});


// 페이지 변경
function changePage(type) {
    const rewardTab = document.querySelectorAll(".reward-tab-list li");
    rewardTab.forEach(tabLi => {
        tabLi.classList.remove("active");
    });
    document.getElementById(type + "-tab").classList.add("active");

    const rewardCon = document.querySelectorAll(".main-reward .reward-con");
    rewardCon.forEach(con => {
        con.classList.remove("active");
    });
    document.getElementById(type + "-con").classList.add('active');

    // 리스트 호출
    if(type == "reward"){
        checkProjectItem(); // 아이템 유무 확인
        loadRewardList();
    }else if(type == "item"){
        loadItemList();
    }
}

// 아이템 유무 확인
function checkProjectItem() {
    const url = `/api/funding/checkProjectItem?id=${projectId}`;
    fetch(url)
    .then((response) => response.json())
    .then((response) => {
        const reWardBtn = document.getElementById("reward-btn");
        if(response.data.checkProjectItem < 1) {
            isProductAvailable = false; // 아이템 없음
            reWardBtn.innerText = "아이템 먼저 만들기";
        }else{
            isProductAvailable = true;
            reWardBtn.innerText = "선물 만들기";
        }
    });
}


/******** 아이템 : 작성 ********/

// 작성폼 열기
function showItem() {
    const itemWriteTemplate = document.querySelector("#template .item-write-box");
    const newItemWriteWrapper = itemWriteTemplate.cloneNode(true);

    showCanvas(newItemWriteWrapper);
}

// 옵션 타입 선택
function selectOptionType(type=0) {
    // 기존 태그 삭제 => type == 0 은 무관해짐
    const peditorBox = document.getElementById("peditor-option-box");
    if(peditorBox) {
        peditorBox.remove();
    }

    // 옵션 창은 생성해서 추가 
    if(type == 1){ // 옵션 : 주관식
        const newPeditorBox = document.createElement("div");
        newPeditorBox.classList.add("peditor-box", "paditor-mt");
        newPeditorBox.id = "peditor-option-box";

        const newPeditorSubTitle = document.createElement("div");
        newPeditorSubTitle.classList.add("peditor-sub-title");
        newPeditorSubTitle.innerText = "옵션 항목";

        const newItemOptionContent = document.createElement("div");
        newItemOptionContent.classList.add("item-option-content", "peditor-sub-content");
        const newSubjectiveOption = document.createElement("textarea");
        newSubjectiveOption.classList.add("peditor-namebox", "peditor-textarea");
        newSubjectiveOption.name = "subjective_option";
        newSubjectiveOption.id = "subjective-option";
        newSubjectiveOption.placeholder = "예) 각인할 메시지를 입력해주세요.";

        // 조립하기
        newItemOptionContent.appendChild(newSubjectiveOption);
        newPeditorBox.appendChild(newPeditorSubTitle);
        newPeditorBox.appendChild(newItemOptionContent);
        // 최종삽입
        const target = event.target;
        const targetPeditorBox = target.closest(".peditor-box");
        targetPeditorBox.after(newPeditorBox);

    }else if(type == 2){ // 옵션 : 객관식
        const newPeditorBox = document.createElement("div");
        newPeditorBox.classList.add("peditor-box", "paditor-mt");
        newPeditorBox.id = "peditor-option-box";

        const newPeditorSubTitle = document.createElement("div");
        newPeditorSubTitle.classList.add("peditor-sub-title");
        newPeditorSubTitle.innerText = "옵션 항목";

        const newPeditorSubDesc = document.createElement("div");
        newPeditorSubDesc.classList.add("peditor-sub-desc");
        newPeditorSubDesc.innerText = "입력 완료 후 Enter 키를 누르면 옵션이 생성됩니다.";

        const newItemOptionContent = document.createElement("div");
        newItemOptionContent.classList.add("item-option-content", "peditor-sub-content");
        const newMultipleOption = document.createElement("textarea");
        newMultipleOption.classList.add("peditor-namebox", "peditor-textarea");
        newMultipleOption.id = "multiple-option";
        newMultipleOption.placeholder = "옵션 항목을 입력해주세요.";
        const newNotice = document.createElement("div");
        newNotice.classList.add("option-count-notice");
        newNotice.innerText = "2개 이상의 옵션 항목을 만들어 주세요.";
        const newOptionList = document.createElement("div");
        newOptionList.classList.add("option-list");
        newOptionList.id = "option-list";


        // 조립하기
        newItemOptionContent.appendChild(newMultipleOption);
        newItemOptionContent.appendChild(newNotice);
        newItemOptionContent.appendChild(newOptionList);

        newPeditorBox.appendChild(newPeditorSubTitle);
        newPeditorBox.appendChild(newPeditorSubDesc);
        newPeditorBox.appendChild(newItemOptionContent);

        // 최종삽입
        const target = event.target;
        const targetPeditorBox = target.closest(".peditor-box");
        targetPeditorBox.after(newPeditorBox);
        
        insertTextOption(); // 태그 생성 함수 실행
    }
}

// 옵션 tag 생성
function createOptionTag(data) {
    const newOption = document.createElement("div");
    newOption.classList.add("option");
    const newOptionValue = document.createElement("div");
    newOptionValue.classList.add("value");
    newOptionValue.innerText = data; // value
    newOption.appendChild(newOptionValue);
    const newOptionDelete = document.createElement("div");
    newOptionDelete.classList.add("option-delete", "material-symbols-outlined");
    newOptionDelete.innerText = "close";
    newOption.appendChild(newOptionDelete);

    return newOption;
}

// 옵션 tag 항목 추가
function insertTextOption() {
    const textInput = document.getElementById("multiple-option");
    const tagList = document.getElementById("option-list");

    textInput.addEventListener('keypress', function(event) {
        if(event.key === 'Enter' || event.keyCode === 13){
            event.preventDefault(); // 기본 동작 방지
            const inputValue = textInput.value.trim();

            if(inputValue === '') return;

            // 이미 추가된 태그인지 확인
            const existingTags = document.querySelectorAll("#option-list .option .value");
            let isDuplicate = false; // 복제
            existingTags.forEach(tag => {
                if(tag.innerText === inputValue) {
                    isDuplicate = true;
                    alert("중복된 옵션 선택값이 있습니다.");
                    return;
                }
            });

            if(!isDuplicate) { // 새 태그 생성 삽입
                const newTag = createOptionTag(inputValue);
                tagList.appendChild(newTag);
                textInput.value = '';

                deleteOptionTag(); // 태그 삭제 기능 호출
                showMinCountNotice();
            }
        }
    })
}

// 옵션 tag 항목 삭제
function deleteOptionTag() {
    const btnDeletOption = document.querySelectorAll(".option-delete");
    btnDeletOption.forEach(btn => {
        btn.addEventListener('click', function(event) {
            const target = event.target;
            const optionTag = target.closest(".option");
            optionTag.remove();

            showMinCountNotice();
        });
    });
}

// 옵션 최소 개수 안내 출력
function showMinCountNotice() {
    const tagList = document.getElementById("option-list");
    const tags = tagList.querySelectorAll(".option");
    const optionNotice = document.querySelector(".option-count-notice");

    if(tags.length < 2) {
        const newNotice = document.createElement("div");
        newNotice.classList.add("option-count-notice");
        newNotice.innerText = "2개 이상의 옵션 항목을 만들어 주세요.";
        if(!optionNotice) {
            tagList.before(newNotice);
        }
    }else{
        if(optionNotice) {
            optionNotice.remove();
        }
    }
}

// 아이템 작성 초기화
function resetItem() {
    const itemWriteBox = document.querySelector("#offcanvasWrapper .item-write-box");
    if(itemWriteBox) {
        const inputs = itemWriteBox.getElementsByTagName('input');
        Array.from(inputs).forEach(input => {
            if(input.type == 'text') {
                input.value = '';
            }else if(input.type == 'radio') {
                input.checked = false;
            }
        });

        selectOptionType();
    }
}

// 아이템 생성
function createItem() {
    const itemWriteBox = document.querySelector("#offcanvasWrapper .item-write-box");

    const itemName = itemWriteBox.querySelector("#item-name");
    if(itemName.value == "" || !itemName.value) {
        alert("아이템 이름을 입력해주세요.");
        itemName.focus();
        return;
    }

    //옵션 조건
    const inputTypes = itemWriteBox.querySelectorAll("input[name='option_type']");
    let isChecked = false;
    let optionType = "";
    inputTypes.forEach(input => {
        if(input.checked) {
            isChecked = true;
            optionType = input.value; // 선택된 옵션 타입
        }
    });

    // 옵션 항목
    let optionArray = [];
    if(!isChecked) {
        alert("옵션 조건을 선택해주세요.");
        return;

    }else{
        if(optionType == 1) {
            const subjectiveOption = itemWriteBox.querySelector("#subjective-option");
            if(subjectiveOption.value == "" || !subjectiveOption.value) {
                alert("옵션 항목을 입력해주세요.");
                subjectiveOption.focus();
                return;
            }
            optionArray.push(subjectiveOption.value);

        }else if(optionType == 2) {
            const optionList = itemWriteBox.querySelector("#option-list");
            const options = optionList.querySelectorAll(".option");
            if(options.length < 2) {
                alert("2개 이상의 옵션 항목을 만들어 주세요.");
                const multipleOption = itemWriteBox.querySelector("#multiple-option");
                multipleOption.focus();
                return;
            }

            options.forEach(option => {
                let optionValue = option.querySelector(".value").innerText;
                optionArray.push(optionValue);
            });
        }
    }

    const url = "/api/funding/registerProjectItem";
    
    fetch(url, {
        method: "post",
        headers: {
            // "Content-Type": "application/json"
            "Content-Type": "application/x-www-form-urlencoded"
        },
        // body: JSON.stringify(data)
        body: `project_id=${projectId}&name=${itemName.value.trim()}&option_type=${optionType}&options=${optionArray.join('&options=')}`
    })
    .then(response => response.json())
    .then((response) => {
        closeCanvas();
        loadItemList(); // 아이템 목록 로드
        printNotice("등록되었습니다.");
    });
}

/******** 아이템 : 목록 ********/

// 아이템 목록 : 아이템 항목 생성
function getItemList(itemList, itemCount) {
    // 데이터 자체가 없으면
    if(itemCount === 0) {
        const emptyBox = document.createElement("div");
        emptyBox.classList.add("list-none-box");
        const emptyBoxText = document.createElement("p");
        emptyBoxText.innerText = "만든 아이템이 없습니다.";
        emptyBox.appendChild(emptyBoxText);

        return emptyBox;
    }

    const newItemList = document.createElement("div");
    newItemList.classList.add("item-list");

    for(item of itemList) {
        const newItemRow = document.createElement("div");
        newItemRow.classList.add("item-row");

        const newItemHref = document.createElement("a");
        // newItemHref.href = "#" // 수정폼으로 링크 넣어야 함 !@!

        const newItemTitle = document.createElement("div");
        newItemTitle.classList.add("item-title");
        newItemTitle.innerText = item.projectItemDto.name;
        const newOptionType = document.createElement("div");
        newOptionType.classList.add("option-type");
        newOptionType.innerText = printOptionType(item.projectItemDto.option_type);
        
        const newOptionList = document.createElement("ul");
        // 옵션 출력
        if(item.itemOptionList.length > 0) {
            newOptionList.classList.add("option-list");
            for(option of item.itemOptionList) {
                const newOption = document.createElement("li");
                newOption.innerText = option.option_name;
                newOptionList.appendChild(newOption);
            }
        }

        newItemHref.appendChild(newItemTitle);
        newItemHref.appendChild(newOptionType);
        newItemHref.appendChild(newOptionList);

        const newBtnDelete = document.createElement("div");
        newBtnDelete.classList.add("material-symbols-outlined", "btn-delete");
        newBtnDelete.innerText = "delete";
        newBtnDelete.setAttribute("onclick", `deleteItem(${item.projectItemDto.id})`);

        newItemRow.appendChild(newItemHref);
        newItemRow.appendChild(newBtnDelete);
        newItemList.appendChild(newItemRow);
    }
    
    return newItemList;
}

// 아이템 옵션타입명 출력
function printOptionType(type) {
    let typeText = "";

    if(type == 0){
        typeText = "옵션 없음";
    }else if(type == 1){
        typeText = "주관식 옵션"
    }else if(type == 2){
        typeText = "객관식 옵션";
    }
    
    return typeText;
}

// 아이템 목록 : 호출 및 출력
function loadItemList() {
    const url = `/api/funding/projectItemDataList?project_id=${projectId}`;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        const itemDataList = response.data.projectItemDataList; // 상품 리스트
        const totalItemCount = response.data.totalItemCount; // 상품 개수

        const itemListContainer = document.querySelector("#item-con .container");
        itemListContainer.innerHTML = "";

        // 데이터 가져오기
        const itemData = getItemList(itemDataList, totalItemCount);
        itemListContainer.appendChild(itemData);
    })
}

// 아이템 삭제 + 옵션 + 관계 도 같이
function deleteItem(id) {
    if(!confirm("이 아이템을 삭제하시겠습니까? 삭제하면 해당 아이템이 포함된 선물에서도 삭제됩니다.")) {
        return;
    }

    const url = `/api/funding/deleteProjectItem?id=${id}`;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        loadItemList();
        printNotice("삭제가 완료되었습니다.");
    });
}


/******** 선물 : 작성 ********/

// 프로젝트 마감일 가져오기
function getProjectEndAt() {
    url = `/api/funding/projectIdAndEndAt?id=${projectId}`;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        const endAtDate = new Date(response.data.projectEndAtData.end_at);
        const endAt = endAtDate.toLocaleDateString('en-CA');

        const projectEndAt = document.querySelector("#offcanvasWrapper .project-end-at");
        projectEndAt.innerText = endAt;

        return endAt;
    });
}

// 작성폼 열기
function showReward() {
    // 아이템이 없으면
    if(!isProductAvailable) { 
        changePage("item");
        return; 
    }

    const rewardWriteTemplate = document.querySelector("#template .reward-write-box");
    const newRewardWriteWrapper = rewardWriteTemplate.cloneNode(true);
    
    const rewardForm = newRewardWriteWrapper.querySelector("form");
    rewardForm.id = "reward-form";

    getProjectEndAt(); // 마감일
    // 캔바스 열기
    showCanvas(newRewardWriteWrapper);
    // 수량제한 함수 실행
    isLimitCkeck();
}

// 모달용 : 선물 아이템 목록
function getSelectItemList() {
    const selectedIdInput = document.getElementById("item-id-list");
    let selectIds = [];

    if(selectedIdInput) {
        selectIds = selectedIdInput.value.split(",");
    }

    const url = `/api/funding/projectItemDataList?project_id=${projectId}`;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        const itemDataList = response.data.projectItemDataList; // 상품 리스트 데이타

        // 아이템 목록
        const newItemSelecBox = document.createElement("div");
        newItemSelecBox.classList.add("item-select-box");
        
        const newUl = document.createElement("ul");

        for(item of itemDataList) {
            const itemDto = item.projectItemDto;
            const optionType = printOptionType(itemDto.option_type);

            const newLi = document.createElement("li");
            const newLabel = document.createElement("label");
            newLabel.classList.add("form-check-label");
            newLabel.for = `item${itemDto.id}`;

            const newCheck = document.createElement("input");
            newCheck.classList.add("form-check-input");
            newCheck.type = "checkbox";
            newCheck.name = "item_id";
            newCheck.id = `item${itemDto.id}`;
            newCheck.value = itemDto.id;
            // 선택된 아이템인 경우
            if(selectIds.includes(itemDto.id.toString())){
                newCheck.checked = true;
            }

            const newInfo = document.createElement("div");
            newInfo.classList.add("form-check-info");
            const newItemName = document.createElement("p");
            newItemName.classList.add("item-name");
            newItemName.innerText = `${itemDto.name} (${optionType})`;
            newInfo.appendChild(newItemName);

            newLabel.appendChild(newCheck);
            newLabel.appendChild(newInfo);
            newLi.appendChild(newLabel);
            newUl.appendChild(newLi);
        }

        newItemSelecBox.appendChild(newUl);

        // 푸터
        const newAddButton = document.createElement("button");
        newAddButton.type = "button";
        newAddButton.setAttribute("onclick", "btnSelectItem(event)");
        newAddButton.classList.add("btn", "btn-primary", "btn-select-item");
        newAddButton.innerText = "선택 완료";

        // 모달 노출
        showModal(newItemSelecBox, "선물 아이템 목록", newAddButton);
    })
}

// 선물 아이템 선택값 : 전달
async function btnSelectItem(event) {
    const itemSelectBox = document.querySelector(".item-select-box");
    const items = itemSelectBox.querySelectorAll("[name='item_id']");
    let checkedItems = [];

    items.forEach(item => {
        if(item.checked) {
            checkedItems.push(item.value);
        }
    });

    const selectItemList = await addSelectItem(checkedItems);

    const peditorItemBox = document.querySelector(".reward-write-box .peditor-item-box");
    peditorItemBox.innerHTML = "";
    if(selectItemList) {
        peditorItemBox.appendChild(selectItemList);
    }

    closeModal(event);
}

// 선물 아이템 선택값 : 출력
async function addSelectItem(idList) {
    if(idList.length <= 0) {
        console.log("선택한 항목 없음");
        return;
    }

    const url = "/api/funding/selectItemDataList";
    const response = await fetch(url, {
        method: "post",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: `id=${idList.join('&id=')}`
    });
    const responseData = await response.json();

    const selectItemList = responseData.data.selectItemDataList; // 선택한 아이템 리스트

    const newItemList = document.createElement("div");
    newItemList.classList.add("item-list");

    const idListInput = document.createElement("input");
    idListInput.type = "hidden";
    idListInput.name = "itemIdList";
    idListInput.id = "item-id-list";
    idListInput.value = idList.join(','); // 쉼표로 잘라 넣기
    newItemList.appendChild(idListInput);
    
    for(const item of selectItemList) {
        const itemDto = item.projectItemDto;

        const newItemRow = document.createElement("div");
        newItemRow.classList.add("item-row");
    
        const newItemTitle = document.createElement("div");
        newItemTitle.classList.add("item-title");
        newItemTitle.innerText = itemDto.name;
        const newOptionType = document.createElement("div");
        newOptionType.classList.add("option-type");
        newOptionType.innerText = printOptionType(itemDto.option_type);
        
        const newOptionList = document.createElement("ul");
        if(item.itemOptionList.length > 0) {
            newOptionList.classList.add("option-list");
            for(const option of item.itemOptionList) {
                const newOptionLi = document.createElement("li");
                newOptionLi.innerText = option.option_name;
                newOptionList.appendChild(newOptionLi);
            }
        }
    
        const newBtnDelete = document.createElement("div");
        newBtnDelete.classList.add("material-symbols-outlined", "btn-delete");
        newBtnDelete.onclick = () => deleteSelectItem(itemDto.id);
        newBtnDelete.innerText = "delete";

        newItemRow.appendChild(newItemTitle);
        newItemRow.appendChild(newOptionType);
        newItemRow.appendChild(newOptionList);
        newItemRow.appendChild(newBtnDelete);
        newItemList.appendChild(newItemRow);
    }

    return newItemList;
}

// 선택한 아이템 삭제
function deleteSelectItem(id) {
    const target = event.target;
    const thisRow = target.closest(".item-row");
    thisRow.remove();

    const idListInput = document.getElementById("item-id-list");
    if(idListInput) {
        let idList = idListInput.value.split(","); // 배열로 변환
        idList = idList.filter(item => item != id.toString()); // id 삭제
        idListInput.value = idList.join(","); // 배열을 다시 문자열로 변환

        if(idListInput.value === "") {
            idListInput.remove();
        }
    }
}

// 수량 제한 checkbox
function isLimitCkeck() {
    const isLimitRadio = document.querySelectorAll("#offcanvasWrapper input[name='is_limit']");
    const countLimitInput = document.querySelector("#offcanvasWrapper input[name='count_limit']");

    isLimitRadio.forEach(radio => {
        radio.addEventListener("change", function(){
            if(this.value == "T") {
                countLimitInput.disabled = false;
                countLimitInput.value = 1;
            }else if(this.value == "F") {
                countLimitInput.disabled = true;
                countLimitInput.value = 0;
            }
        });
    });
}

// 상품 작성 초기화
function resetReward() {
    const rewardWriteBox = document.querySelector("#offcanvasWrapper .reward-write-box");
    if(rewardWriteBox) {
        // 작성폼
        const inputs = rewardWriteBox.getElementsByTagName('input');
        Array.from(inputs).forEach(input => {
            if(input.type == 'text' || input.type == 'number') {
                input.value = '';
            }else if(input.type == 'radio') {
                input.checked = false;
            }
        });

        // 선택 상품 초기화
        const selectItemBox = rewardWriteBox.querySelector(".peditor-item-box");
        selectItemBox.innerHTML = "";
    }
}

// 선물 생성
function createReward() {
    const form = document.getElementById("reward-form");
    const formData = new FormData(form);
    formData.append('project_id', projectId);
        
    const rewardTitle = form.title;
    if(rewardTitle.value == "" || !rewardTitle.value) {
        alert("선물 이름을 입력해주세요.");
        rewardTitle.focus();
        return;
    }
    if(rewardTitle.value.length > 50) {
        alert("선물 이름은 50글자 이내로 입력해주세요.");
        rewardTitle.focus();
        return;
    }

    // 수량 제한
    const isLimitRadio = form.is_limit;
    let isLimitCheck = false; // 체크 유무
    let isLimitValue = "";

    isLimitRadio.forEach(radio => {
        if(radio.checked) {
            isLimitCheck = true;
            isLimitValue = radio.value;
        }
    });

    if(!isLimitCheck) {
        alert("수량 제한 여부를 설정해주세요.");
        isLimitRadio[0].focus();
        return;
    }
    if(isLimitValue == "T") { // 있음 일 경우
        const countLimitInput = form.count_limit;
        if(countLimitInput.value <= 0 || countLimitInput.value == "" || !countLimitInput.value){
            alert("수량 제한이 있는 경우 선물 수량을 1개 이상으로 설정해주세요.");
            countLimitInput.focus();
            return;
        }
    }

    // 예상 전달일
    const expectDate = form.expect_date;
    if(expectDate.value == "" || !expectDate.value) {
        alert("예상 전달일을 입력해주세요.");
        expectDate.focus();
        return;
    }

    // 배송 여부
    const isDelivery = form.is_delivery;
    let isDeliveryChecked = false;
    isDelivery.forEach(radio => {
        if(radio.checked) {
            isDeliveryChecked = true;
        }
    });
    
    if(!isDeliveryChecked) {
        alert("배송 여부를 선택해주세요.");
        isDelivery[0].focus();
        return;
    }

    // 선물 금액
    const awardAmount = form.amount;
    if(awardAmount.value == "" || !awardAmount.value) {
        alert("선물 금액을 입력해주세요.");
        awardAmount.focus();
        return;
    }
    if(awardAmount.value < 1000) {
        alert("선물 금액은 1000원 이상 입력해주세요.");
        awardAmount.focus();
        return;
    }
 
    const url = "/api/funding/registerProjectReward";

    fetch(url, {
        method: "post",
        body: formData
    })
    .then(response => response.json())
    .then((response) => {
        closeCanvas();
        loadRewardList();
        printNotice("등록되었습니다.");
    });
}

/******** 선물 : 목록 ********/

// 선물 목록 : 선물 항목 생성
function getRewardList(rewardList, rewardCount) {
    // 데이터 자체가 없으면 
    if(rewardCount === 0){
        const emptyBox = document.createElement("div");
        emptyBox.classList.add("list-none-box");
        const emptyBoxText = document.createElement("p");
        emptyBoxText.innerText = "만든 선물이 없습니다.";
        emptyBox.appendChild(emptyBoxText);

        return emptyBox;
    }

    const newRewardList = document.createElement("div");
    newRewardList.classList.add("reward-list");

    for(reward of rewardList) {
        const newRewardRow = document.createElement("div");
        newRewardRow.classList.add("item-row", "reward-row");

        const newRewardHref = document.createElement("a");
        // newItemHref.href = "#" // 수정폼으로 링크 넣어야 함 !@!

        const newRewardAmount = document.createElement("div");
        newRewardAmount.classList.add("reward-amount");
        const newAmount = document.createElement("span");
        newAmount.classList.add("amout");
        newAmount.innerText = formatNumberComma(reward.projectRewardDto.amount);
        const newAmountText = document.createElement("span");
        newAmountText.innerText = "원+";
        newRewardAmount.appendChild(newAmount);
        newRewardAmount.appendChild(newAmountText);

        const newRewardTitle = document.createElement("div");
        newRewardTitle.classList.add("reward-title");
        newRewardTitle.innerText = reward.projectRewardDto.title;

        const newItemList = document.createElement("ul");
        // 상품 리스트
        if(reward.projectItemDataList.length > 0) {
            newItemList.classList.add("option-list");
            for(item of reward.projectItemDataList) {
                const newItem = document.createElement("li");
                newItem.innerText = item.projectItemDto.name;
                newItemList.appendChild(newItem);
            }
        }

        const newExpectDate = document.createElement("div");
        newExpectDate.classList.add("expect-date");
        const newExpectDateText = document.createElement("span");
        newExpectDate.appendChild(newExpectDateText);

        newRewardHref.appendChild(newRewardAmount);
        newRewardHref.appendChild(newRewardTitle);
        newRewardHref.appendChild(newItemList);
        newRewardHref.appendChild(newExpectDate);

        const newBtnDelete = document.createElement("div");
        newBtnDelete.classList.add("material-symbols-outlined", "btn-delete");
        newBtnDelete.innerText = "delete";
        newBtnDelete.setAttribute("onclick", `deleteReward(${reward.projectRewardDto.id})`);

        newRewardRow.appendChild(newRewardHref);
        newRewardRow.appendChild(newBtnDelete);
        newRewardList.appendChild(newRewardRow);
    }

    return newRewardList;
}

// 선물 목록 : 호출 및 출력
function loadRewardList() {
    const url = `/api/funding/projectRewardDataList?project_id=${projectId}`;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        const rewardDataList = response.data.projectRewardDataList; // 선물 리스트
        const totalrewardCount = response.data.totalrewardCount; // 선물 개수

        const rewardListContainer = document.querySelector("#reward-con .container");
        rewardListContainer.innerHTML = "";

        // 데이터 가져오기
        const rewardData = getRewardList(rewardDataList, totalrewardCount);
        rewardListContainer.appendChild(rewardData);
    });
}

// 선물 삭제 + 선물 아이템(관계) 삭제
function deleteReward(id) {
    if(!confirm("이 선물을 삭제하시겠습니까?")) {
        return;
    }

    const url = `/api/funding/deleteProjectReward?id=${id}`;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        loadRewardList();
        printNotice("삭제가 완료되었습니다.");
    });
}
/********** 프로젝트 관리 : 헤더 **********/

// 프로젝트 심사요청
function requestApproval(id) {
    if(!confirm("프로젝트 심사요청을 하시겠습니까?")) {
        return;
    }

    const url = `/api/funding/approvalProcess?id=${id}`;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        if(response.result == "fail") {
            alert("심사요청이 불가합니다. 프로젝트 작성 상태 또는 현재 상태를 확인해주세요.");
            return;
        }

        changeManagementButton("심사 중");
        printNotice("심사요청이 완료되었습니다.");
    });
}

// 프로젝트 관리 헤더 버튼 변경
function changeManagementButton(text) {
    const buttonBox = document.querySelector(".management-header .management-btn-box");

    const newButton = document.createElement("button");
    newButton.type = "button";
    newButton.disabled = true;
    newButton.classList.add("project-submit-btn");
    newButton.innerText = text;

    buttonBox.innerHTML = "";
    buttonBox.appendChild(newButton);
}

/********** 프로젝트 관리 : 대시보드 **********/

// 프로젝트 id 가져오기
function getProjectId() {
    const urlParams = new URL(window.location.href).searchParams;
    const projectId = urlParams.get("id");
    return projectId;
}

// labels 추출
function getLabels(data, feild){
    return data.map(stat => stat[feild]);
}

// data 추출
function getData(data, feild) {
    return data.map(stat => stat[feild]);
}

// chart config
function getChart(id, type, data, options) {
    const ctx = document.getElementById(id);

    new Chart(ctx, {
        type: type,
        data: data,
        options: options
    });
}

// chart box templete
function getChartBoxTemplete(id) {
    // 템플릿 복사
    const chartBox = document.querySelector("#template .chart-wrapper .chart-box");
    const newChartBox = chartBox.cloneNode(true);

    const chartCanvas = newChartBox.querySelector(".chart-canvas");
    chartCanvas.id = id; // chart 의 id 값 필수

    return newChartBox;
}

// emptyBox 만들기
function getEmptyChart(chartId, iconText) {
    const chartGenderSupportBox = document.querySelector(chartId);
    chartGenderSupportBox.classList.add("py-3", "new-bg-gray", "rounded-1", "text-center");
    chartGenderSupportBox.innerHTML = "";

    const newChartIcon = document.createElement("div");
    newChartIcon.classList.add("material-symbols-outlined", "icon-draft-order", "fw-bold");
    newChartIcon.innerText = iconText;
    const newChartText = document.createElement("div");
    newChartText.classList.add("new-fs-8", "text-body-secondary");
    newChartText.innerText = "집계 내역이 없습니다.";

    chartGenderSupportBox.appendChild(newChartIcon);
    chartGenderSupportBox.appendChild(newChartText);
}

// 통계 데이터 주입 : 취합본
function getStatisticsByProject() {
    const url = `/api/funding/getDashboardStatistics?project_id=${getProjectId()}`;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        // 주간 일자별 게시물 개수
        const dailyAmount = response.data.dailyAmount;
        // 너비 조정
        if(dailyAmount.length > 7) {
            const chartBox = document.querySelector(".chart-daily-amount .chart-box");
            chartBox.style.width = "700px";
        }
        const dayData = {
            labels: getLabels(dailyAmount, "day_label"),
            datasets: [{
                label: '누적 후원액',
                data: getData(dailyAmount, "cumulative_amount"),
                borderColor: '#ffb0b0',
                backgroundColor: '#ff6d96',
                borderWidth: 1
            }]
        }
        const dayOptions = {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top',
                }    
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        precision: 0
                    }
                }
            },
        }
        getChart("chartDailyAmount", "line", dayData, dayOptions);

        // 후원자 남여 비율
        const genderSupport = response.data.genderSupport;
        if(genderSupport.length === 0) {
            // 생성한 차트 박스 삭제 후 새로 주입
            getEmptyChart(".chart-gender-support", "data_usage");

        }else{
            const chartBox = document.querySelector(".chart-gender-support .chart-box");
            chartBox.classList.add("p-1", "mx-auto", "max-width-80");

            const genderData = {
                labels: getLabels(genderSupport, "gender"),
                datasets: [{
                    label: '후원자 성별 비율',
                    data: getData(genderSupport, "gender_percentage"),
                    backgroundColor: [
                        'rgb(174,196,255)',
                        'rgb(255,160,160)',
                    ],
                    borderWidth: 1
                }]
            }
            const genderOptions = {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    },
                },
            }
            getChart("chartGenderSupport", "doughnut", genderData, genderOptions);    
        }
        
        // 후원자 나이대 비율
        const ageSupport = response.data.ageGroupSupport;
        if(ageSupport.length === 0) {
            // 생성한 차트 박스 삭제 후 새로 주입
            getEmptyChart(".chart-age-support", "bar_chart_off");

        }else{
            const ageData = {
                labels: getLabels(ageSupport, "age_group"),
                datasets: [{
                    label: '후원자 나이대 비율',
                    data: getData(ageSupport, "age_group_percentage"),
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.5)',
                        'rgba(255, 159, 64, 0.5)',
                        'rgba(255, 205, 86, 0.5)',
                        'rgba(75, 192, 192, 0.5)',
                        'rgba(54, 162, 235, 0.5)',
                        'rgba(153, 102, 255, 0.5)',
                        'rgba(201, 203, 207, 0.5)'
                    ],
                    borderWidth: 1
                }]
            }
            const ageOptions = {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    },
                },
            }
            getChart("chartAgeSupport", "bar", ageData, ageOptions);
        }
    });
}

// 통계 출력
function showStatistics() {
    const dashboardCon = document.querySelector(".dashboard-con");
    if(!dashboardCon) return;

    // 차트 박스 : 일일 누적 후원금
    const chartDailyAmountBox = document.querySelector(".chart-daily-amount");
    chartDailyAmountBox.appendChild(getChartBoxTemplete("chartDailyAmount"));

    // 차트 박스 : 후원자 성별 비율
    const chartGenderSupportBox = document.querySelector(".chart-gender-support");
    chartGenderSupportBox.appendChild(getChartBoxTemplete("chartGenderSupport"));

    // 차트 박스 : 후원자 나이대 비율
    const chartAgeSupportBox = document.querySelector(".chart-age-support");
    chartAgeSupportBox.appendChild(getChartBoxTemplete("chartAgeSupport"));

    // 차트 호출
    getStatisticsByProject();
}

/********** 프로젝트 관리 : 후원자 관리 **********/

// 후원자 관리 - 목록 : 데이터 생성
function getBackersList(backersList, backersCount) {
    const backersTable = document.querySelector("#template-wrapper .backers-table");

    const newBackersList = document.createElement("div");
    newBackersList.classList.add("backers-list");

    for(backers of backersList) {
        const newBackersTable = backersTable.cloneNode(true);
        
        const backersHref = newBackersTable.querySelector(".backers-href");
        backersHref.setAttribute("onclick", `showBackersDetail(${backers.support_id})`);
        const supportId = newBackersTable.querySelector(".support-id");
        supportId.innerText = backers.support_id;

        const backersAccountName = newBackersTable.querySelector(".backers-account-name");
        backersAccountName.innerText = backers.account_name;
        const backersReward = newBackersTable.querySelector(".backers-reward");
        backersReward.innerText = backers.reward_title;
        const backersTotalPrice = newBackersTable.querySelector(".backers-total-price");
        backersTotalPrice.innerText = `${formatNumberComma(backers.total_amount)}원`;
        const backersSupportAt = newBackersTable.querySelector(".backers-support-at");
        backersSupportAt.innerText = formatDateTime(backers.support_at);
        const backersPayStatus = newBackersTable.querySelector(".backers-pay-status span");
        if(backers.pay_status == "결제완료") {
            backersPayStatus.classList.add("status-success");
        }else{
            backersPayStatus.classList.add("status-fail");
        }
        backersPayStatus.innerText = backers.pay_status;

        const backersDeliveryStatus = newBackersTable.querySelector(".backers-delivery-status");
        if(backers.delivery_status == "대기중") {
            backersDeliveryStatus.classList.add("bg-body-secondary");
        }else{
            backersDeliveryStatus.classList.add("bg-success", "text-white");
        }
        backersDeliveryStatus.innerText = backers.delivery_status;

        newBackersList.appendChild(newBackersTable);
    }

    return newBackersList;
}

// 후원자 관리 - 목록 : 호출 및 출력
function loadBackersList() {
    const backersBox = document.querySelector(".main-management .backers-box");
    if(!backersBox) return;

    const urlParams = new URL(window.location.href).searchParams;
    const projectId = urlParams.get("id");
    if(!projectId) {
        alert("프로젝트 ID 를 확인할 수 없습니다.");
        window.location.href = "/funding";
        return;
    }

    const url = `/api/funding/getBackersDataList?project_id=${projectId}`;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        const backersList = response.data.backersDataList;
        const backersCount = response.data.backersListCount;

        const totalCount = document.querySelector(".main-management .backers-count");
        totalCount.innerText = backersCount;

        // 데이터 자체가 없으면
        if(backersCount === 0) {
            const emptyBox = document.createElement("div");
            emptyBox.classList.add("list-none-box", "mt-2", "mx-2", "mb-2", "py-5", "bg-white", "new-fs-9", "text-center", "text-secondary");
            const emptyBoxText = document.createElement("p");
            emptyBoxText.innerText = "프로젝트 후원이 없습니다.";
            emptyBox.appendChild(emptyBoxText);
            backersBox.innerHTML = "";
            backersBox.appendChild(emptyBox);
            return;
        }

        backersBox.innerHTML = "";

        // 데이터 가져오기
        const backersData = getBackersList(backersList, backersCount);
        backersBox.appendChild(backersData);
    });
}

// 후원 상세 정보 보기
function showBackersDetail(supportId) {
    const backersWrapper = document.querySelector("#template-wrapper .backers-wrapper");
    if(!backersWrapper) { return; }

    const url = `/api/funding/getBackersDetailData?support_id=${supportId}`;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        const supportData = response.data.supportData; // 후원 정보
        const userData = response.data.userInfo; // 회원 정보
        const rewardDataList = response.data.projectRewardDataList; // 선물 구성 정보

        const newBackersWrapper = backersWrapper.cloneNode(true);

        if(response.data.projectData.project_status == '무산') {
            const rewardDeliveryBox = newBackersWrapper.querySelector(".reward-delivery-box");
            rewardDeliveryBox.remove();
        }else{
            const statusOptions = newBackersWrapper.querySelectorAll(".delivery-status option");
            statusOptions.forEach(option => {
                if(option.value == supportData.supportDto.delivery_status) {
                    option.selected = true;
                }
            });
            const btnDelivery = newBackersWrapper.querySelector(".btn-delivery-status");
            btnDelivery.setAttribute("onclick", `updateDeliveryStatus(${supportId})`);
        }

        // 후원자 정보
        const newAccountName = newBackersWrapper.querySelector(".account-name");
        newAccountName.innerText = userData.account_name;
        const newUserPhone = newBackersWrapper.querySelector(".user-phone");
        newUserPhone.innerText = userData.phone;
        const newUserEmail = newBackersWrapper.querySelector(".user-email");
        newUserEmail.innerText = userData.email;
        // 후원 정보
        const newSupportId = newBackersWrapper.querySelector(".support-id");
        newSupportId.innerText = supportData.supportDto.id;
        const newSupportAt = newBackersWrapper.querySelector(".support-at");
        newSupportAt.innerText = formatDateTime(supportData.supportDto.support_at, 2);
        
        // 선물 구성 :: 생성 적용
        const pledgeRewardList = newBackersWrapper.querySelector(".pledge-reward-list");
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
            newQuantity.innerText = ` (x${rewardData.supportRewardDto.quantity})`;
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
            const newExpectedText = document.createElement("span");
            newExpectedText.classList.add("ps-1");
            if(supportData.supportDto.delivery_status == '전달완료') {
                newExpectedSpan.innerText = "전달 완료일";
                newExpectedText.innerText = formatDateTime(supportData.supportDto.delivery_at, 2);
            }else{
                newExpectedSpan.classList.add("text-body-secondary");
                newExpectedSpan.innerText = "예상 전달일";

                newExpectedText.classList.add("text-primary");
                newExpectedText.innerText = formatDateTime(rewardData.rewardExpectedDate.expected_date, 2);
            }
            
            newColAuto.appendChild(newExpectedSpan);
            newColAuto.appendChild(newExpectedText);
            newExpectedDate.appendChild(newColAuto);
            newLi.appendChild(newExpectedDate);

            pledgeRewardList.appendChild(newLi);
        }

        // 선물 금액
        const rewardTotalPrice = newBackersWrapper.querySelector(".reward-total-price > span");
        rewardTotalPrice.innerText = formatNumberComma(response.data.supportTotalPirce);
        // 결제 정보
        const newPayMethod = newBackersWrapper.querySelector(".pay-method");
        newPayMethod.innerText = supportData.supportDto.payment_type;
        const newTotalAmount = newBackersWrapper.querySelector(".total-amount");
        newTotalAmount.innerText = `${formatNumberComma(response.data.supportTotalPirce)}원`;
        const newPayStatus = newBackersWrapper.querySelector(".pay-status");
        newPayStatus.innerText = supportData.supportDto.pay_status;
        // 배송 정보
        const deliveryInfoBox = newBackersWrapper.querySelector(".delivery-info-box");
        if(supportData.supportDeliveryDto != null) { // 배송 정보가 있을 경우
            const newRecipientName = newBackersWrapper.querySelector(".recipient-name");
            newRecipientName.innerText = supportData.supportDeliveryDto.recipient_name;
            const newRecipientPhone = newBackersWrapper.querySelector(".recipient-phone");
            newRecipientPhone.innerText = supportData.supportDeliveryDto.recipient_phone;
            const newRecipientAddress = newBackersWrapper.querySelector(".address-detail");
            newRecipientAddress.innerText = supportData.supportDeliveryDto.address_detail;
        }else{
            deliveryInfoBox.remove();
        }

        showCanvas(newBackersWrapper);
    });
}

// 선물 전달 상태 변경
function updateDeliveryStatus(supportId) {
    const deileryStatusForm = event.target.closest(".deileryStatusForm");
    const deliveryStatus = deileryStatusForm.querySelector("select[name='delivery_status'] option:checked");

    if(!deliveryStatus.value || deliveryStatus.value == "") {
        printNotice("선물 전달 상태를 선택해주세요.");
        deliveryStatus.focus();
        return;
    }

    const url = `/api/funding/updateSupportDeliveryStatus?support_id=${supportId}&delivery_status=${encodeURIComponent(deliveryStatus.value)}`;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        printNotice("선물 전달 상태가 변경되었습니다.");

        loadBackersList();
    });

}

/********** 함수 호출 **********/
window.addEventListener('DOMContentLoaded', function() {
    // 후원자 목록
    loadBackersList();

    // 대시보드 : 통계 출력
    showStatistics();
});
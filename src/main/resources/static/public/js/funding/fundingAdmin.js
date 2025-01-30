/********** 프로젝트 관리 **********/

// 프로젝트 심사 진행
function approvalSend(id) {
    const form = document.getElementById("approval-form");
    if(!form) return;

    const formData = new FormData(form);

    const urlParams = new URL(window.location.href).searchParams;
    const projectId = urlParams.get("id");
    formData.append("project_id", projectId); // 프로젝트 id

    const result = form.result;
    if(result.value == "" || !result.value) {
        alert("검토 결과를 선택해주세요.");
        result.focus();
        return;
    }

    const content = form.content;
    if(content.value == "" || !content.value) {
        alert("반려 사유 요약을 입력해주세요.");
        content.focus();
        return;
    }

    const mailSubject = form.mail_subject;
    const mailContent = form.mail_content;
    if(mailSubject.value == "" || mailContent == "") {
        if(!confirm("메일 내용을 입력하지 않으실 경우 메일이 발송되지 않습니다. 검토 진행을 계속하시겠습니까?")) {
            return;
        }
    }

    const url = `/api/admin/funding/approvalStatus`;
    fetch(url, {
        method: "post",
        body: formData
    })
    .then(response => response.json())
    .then((response) => {
        console.log("심사 진행 완료");

        // 작성폼 포맷
        const inputs = form.querySelectorAll('input, select, textarea');
        inputs.forEach((input) => {
            if(input.type == "text" || input.type == "textarea") {
                input.value = "";
            }else if(input.type == "select") {
                input.selected = false;
            }
        })

        // 단계 변경
        const projectStatus = document.querySelector(".status-box #project-status");
        projectStatus.innerText = result.value;

        // 승인인 경우
        if(result.value == '승인') {
            const approvalBox = document.querySelector(".approval-box");
            approvalBox.remove();
        }

        // 이력 출력
        loadApprovalHistory(projectId);    
    });
}

// 이력 로드
function loadApprovalHistory() {
    const historyTableBody = document.querySelector(".history-box .table-body");
    if(!historyTableBody) return;

    const urlParams = new URL(window.location.href).searchParams;
    const projectId = urlParams.get("id");

    url = `/api/admin/funding/approvalHistory?project_id=${projectId}`;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        const approvalData = response.data.approvalHistory;
        historyTableBody.innerHTML = "";
        
        // 데이터가 없는 경우
        if(approvalData.length == 0) {
            const newColBox = document.createElement("div");
            newColBox.classList.add("new-py-45", "border-bottom", "text-center");
            newColBox.innerText = "심사 이력이 없습니다.";
            historyTableBody.appendChild(newColBox);
            return;
        }
        
        for(approval of approvalData) {
            const newColBox = document.createElement("div");
            newColBox.classList.add("d-flex", "border-bottom");

            const newDate = document.createElement("div");
            newDate.classList.add("col-2", "py-1", "px-2", "text-center");
            newDate.innerText = formatDateTime(approval.created_at);
            const newResult = document.createElement("div");
            newResult.classList.add("col-2", "py-1", "px-2", "border-start", "text-center");
            newResult.innerText = approval.result;
            const newContent = document.createElement("div");
            newContent.classList.add("col-8", "py-1", "px-2", "border-start", "second-border-top");
            newContent.innerText = approval.content;

            newColBox.appendChild(newDate);
            newColBox.appendChild(newResult);
            newColBox.appendChild(newContent);
            historyTableBody.appendChild(newColBox);
        }
    });
}


/********** 정산 관리 **********/

// 정산 단계 변경
function settlementStatusProcess() {
    const form = document.getElementById("settlementStatusForm");
    if(!form) return;

    const formData = new FormData(form);

    const urlParams = new URL(window.location.href).searchParams;
    const settlementId = urlParams.get("id");
    formData.append("id", settlementId); // 정산 id

    if(!confirm("정산 처리를 선택하셨습니다. \n정산 내역과 명세서를 확인 후 진행해주세요. \n정산을 계속하시겠습니까?")) {
        return;
    }

    const url = `/api/admin/funding/updateSettlementStatus`;
    fetch(url, {
        method: "post",
        body: formData
    })
    .then(response => response.json())
    .then((response) => {
        console.log("단계 변경 완료");

        const settlementDto = response.data.settlementDto.settlementDetails;

        // 상태 출력 변경
        const settlementStatus = document.querySelector(".settlement-box .settlement-status"); // 정산 상태
        settlementStatus.innerText = settlementDto.settlement_status;
        
        const settlementDate = document.querySelector(".settlement-box .settlement-date"); // 정산 완료일
        const modalSettlementStatus = document.querySelector("#pledgeModal .settlement-date");

        if(settlementDto.settlement_status == '정산완료') {
            settlementStatus.classList.remove("text-danger");
            settlementStatus.classList.add("text-success");
            settlementDate.innerText = formatDateTime(settlementDto.settlement_date, 3);
            modalSettlementStatus.innerText = formatDateTime(settlementDto.settlement_date, 3);
        }else{
            settlementStatus.classList.remove("text-success");
            settlementStatus.classList.add("text-danger");
            settlementDate.innerText = "-";
            modalSettlementStatus.innerText = "정산 전";
        }
    });
}


/********** 문서 로딩 후 실행 **********/

window.addEventListener('DOMContentLoaded', function() {
    loadApprovalHistory();
});
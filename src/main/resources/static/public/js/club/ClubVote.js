function registVote(){
    window.location.href = "/club/registVote?id="+clubId;
}

function voteRegist() {
    const urlParams = new URL(window.location.href).searchParams;
    const clubId = urlParams.get("id");
    
    const form = document.getElementById("voteRegist");
    const voteInfoRows = form.querySelectorAll(".voteRrow");
    
    if(voteInfoRows.length < 2){
        alert("질문을 2개 이상 등록 해주세요.");
        return;
    }

    const restDay = document.getElementById("restDay");
    if(restDay.innerText == '0'){
        alert("당일은 입력 불가능 합니다. 다른 날을 선택해주세요");
        return;
    }
    const formData = new FormData(form);

    const voteFormList = [];

    voteInfoRows.forEach((row) => {      
        const question = row.querySelector("[name='question_content']").value;


        const inputFile = row.querySelector("[name='uploadFiles']"); // 파일 리스트 가져오기  

        if(inputFile && inputFile.files.length >0){
            formData.append(`files`, inputFile.files[0]); // 파일 추가
        }

        voteFormList.push({
            question_content: question,
            club_id: clubId
        });
    });

    let voteContentError = false;
    
    voteFormList.forEach((e) => {
        if(e.question_content == ''){
            voteContentError = true; 
            return;
        }    
    })

    if(voteContentError) {
        alert("내용을 입력해주세요");
        return;
    }

    const voteRequestJson = JSON.stringify(voteFormList);
    formData.append("voteRequest", new Blob([voteRequestJson], { type: "application/json" }));

    const questionTitleInput = document.querySelector("[name='question_title']");
    const questionTitle = questionTitleInput.value;

        if(questionTitle== ''){
            alert("제목을 입력해주세요");
            return;
        }

    const finalDateInput = document.querySelector("[name='final_date']");
    const finalDate = finalDateInput.value;
    const questionTitleDto = {
        question_title: questionTitle,  
        final_date: finalDate,
        club_id: clubId
    }

    const questionTitleJson = JSON.stringify(questionTitleDto);
    formData.append("questionTitle", new Blob([questionTitleJson], { type: "application/json" }));


    const url = "/api/club/voteRegist";
    fetch(url, {
        method: "POST",
        body: formData
    })
    .then(response => response.json())
    .then(response => {
        if (response.result === "success") {
            window.location.href = "/club/detail/vote?id=" + clubId;
        }
    })
}


function chairmanMember(){
    const url = "/api/club/memberAndResidentExist?id="+clubId+"&user_id="+myId;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        if(!response.data.residentInfo.residentExist && !response.data.residentInfo.memberExist){
            const voteRegistButton = document.getElementById("voteRegistButton");
            if (voteRegistButton) {
                voteRegistButton.addEventListener("click",function(event) {
                    // event.preventDefault();
                    alert("모임 가입하여야 이용 가능합니다. 모임가입을 해주세요.")//ㄹ확인버튼 클릭하면 넘어감~~~~~
                        location.href="/club/detail/home?id="+clubId;
                        
                    
                })
                return;
                // writeBoard.classList.remove("d-none");
            }
            

        }
    })



}

function notMemberExist(){

    const url = "/api/club/memberAndResidentExist?id="+clubId+"&user_id="+myId;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        const mainVoteContent = document.getElementById("mainVoteContent");
        const notMemberSee = document.getElementById("notMemberSee");

        if(!response.data.residentInfo.residentExist && !response.data.residentInfo.memberExist){
            
            if(mainVoteContent){
                mainVoteContent.style.display="none";
                notMemberSee.classList.remove("d-none");

            }
        }
        else{
            if(mainVoteContent){
                mainVoteContent.style.display="block";

            }
        }
    })
}



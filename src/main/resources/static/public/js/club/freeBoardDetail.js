const urlParams = new URL(window.location.href).searchParams;
const boardId = urlParams.get("id");
const clubId = urlParams.get("clubId");

let myId =null;

function setSessionId(){
    const url = "/six/user/getSessionUserId";
    fetch(url)
    .then(response => response.json())
    .then((response)=>{
        myId = response.data.id;
        notMemberExist();
        existLike();
    })
};

function commentRegist(){
    const inputComment = document.getElementById("inputComment");

    const url = "/api/club/registBoardComment";
    fetch(url,{
        method:"post",
        headers:{
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: `club_board_id=${boardId}&club_member_id=${myId}&content=${inputComment.value}&club_id=${clubId}`
         
        
    })
    .then(response => response.json())
    .then((response) => {
        inputComment.value="";
        commentList();

    });
}

function boardDetail(){
    const url = "/api/club/clubBoardDetailPage?clubId="+clubId+"&id="+boardId;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        const boardDetailCategory = document.getElementById("boardDetailCategory");
        boardDetailCategory.innerText= response.data.clubBoardDetailPage.categoryName;

        const boardDetailTitle = document.getElementById("boardDetailTitle");
        boardDetailTitle.innerText= response.data.clubBoardDetailPage.clubBoardDto.title;

        const boardDetailNickname = document.getElementById("boardDetailNickname");
        boardDetailNickname.innerText= response.data.clubBoardDetailPage.userDto.nickname;

        const boarDetailRegist = document.getElementById("boarDetailRegist");
        const date = new Date(response.data.clubBoardDetailPage.clubBoardDto.created_at);
        function formatTwo(num) {
                return num < 10 ? '0' + num : num.toString();
            }
        
        boarDetailRegist.innerText= `${date.getFullYear().toString().slice(-2)}.${formatTwo(date.getMonth()+1)}.${formatTwo(date.getDate())} ${formatTwo(date.getHours())}:${formatTwo(date.getMinutes())}`;

        const boardDetailProfile = document.getElementById("boardDetailProfile");
        const img = document.createElement('img');
        img.src = `/images/${response.data.clubBoardDetailPage.userDto.profile_img}`;
        boardDetailProfile.appendChild(img);

        img.style.width="3.5em";
        img.style.height="3.5em";
        img.style.borderRadius="50%";
        
        const boardDetailReadCount = document.getElementById("boardDetailReadCount");
        boardDetailReadCount.innerText= response.data.clubBoardDetailPage.clubBoardDto.read_count;

        const boardDetailContent = document.getElementById("boardDetailContent");
        boardDetailContent.innerText= response.data.clubBoardDetailPage.clubBoardDto.content;
        
        const userWrite = document.getElementById("userWrite");
        if(myId != null && myId==response.data.clubBoardDetailPage.clubBoardDto.club_member_id){
            userWrite.classList.remove('d-none');
        }

        const userRead = document.getElementById("userRead");
        if(myId != null && myId!=response.data.clubBoardDetailPage.clubBoardDto.club_member_id){
            userRead.classList.remove('d-none');
        }
        
        const imageWrapper = document.querySelector(".imageWrapper");
        imageWrapper.innerHTML="";

        const imageTemplate = document.querySelector("#template .imageTemplate");
        for(e of response.data.clubBoardDetailPage.clubBoardDetailImage){

            const newImageTemplate = imageTemplate.cloneNode(true);
            const imageContainer = newImageTemplate.querySelector(".imageContainer");
            const img = document.createElement('img');
            img.src=`/images/${e.location}`;
            
            img.style.width=`100%`;
            img.style.height=`100%`;
            imageContainer.appendChild(img);

            imageWrapper.appendChild(newImageTemplate);
        }

        
    })
}

function goToList(){
    window.location.href = '/club/detail/freeboard?id='+clubId;

}


function updateContent(){
    window.location.href = '/club/update/content?club_id='+clubId+"&id="+boardId;
}
// 좋아요 클릭
function like(){

    if(myId == null){
            if(confirm("로그인 하셔야 이용 가능합니다. 로그인 페이지로 이동하시겠습니까?")){
                location.href = "/user/loginPage";
            }
            return;
        }

    const url = "/api/club/boardLikeClick";

    fetch(url,{
        method:"post",
        headers:{
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: `club_member_id=${myId}&club_board_id=${boardId}&club_id=${clubId}`
    })
    .then(response => response.json())
    .then((response) => {
        existLike();
        likeTotalCount();
    })
}

function unLike(){
    if(myId == null){
            if(confirm("로그인 하셔야 이용 가능합니다. 로그인 페이지로 이동하시겠습니까?")){
                location.href = "/user/loginPage";
            }
            return;
        }
    const url = "/api/club/boardDislikeClick";

    fetch(url,{
        method:"post",
        headers:{
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: `club_member_id=${myId}&club_board_id=${boardId}&club_id=${clubId}`
    })
    .then(response => response.json())
    .then((response) => {
        existLike();
        likeTotalCount();
    })
}



function existLike(){
    const url = "/api/club/existBoardLike?club_member_id="+myId+"&club_board_id="+boardId+"&club_id="+clubId;

    fetch(url)
    .then(response => response.json())
    .then((response) => {
        const like = document.getElementById("like");

        if(response.data.existLike == true){
            like.classList.add("bi-heart-fill");
            like.classList.remove("bi-heart");

            like.setAttribute("onclick","unLike()");
        }
        else{
            like.classList.remove("bi-heart-fill");
            like.classList.add("bi-heart")

            like.setAttribute("onclick","like()");
        }

    })

}
function likeTotalCount(){
    const url = "/api/club/likeTotalCount?board_id="+boardId;

    fetch(url)
    .then(response => response.json())
    .then((response) => {
        const likeTotalCount = document.getElementById("likeTotalCount");
        likeTotalCount.innerText=response.data.likeTotalCount;
    })
}



function commentList(){
    const url = "/api/club/boardCommentList?club_board_id="+boardId+"&club_id="+clubId;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        const commentListBox = document.querySelector(".commentListBox");
        commentListBox.innerHTML="";

        const commentWrapperTemplate = document.querySelector("#commentTemplate .commentWrapper");
        for(e of response.data.boardCommentList){
        
            const newCommentWrapperTemplate = commentWrapperTemplate.cloneNode(true);

            const commentProfile = newCommentWrapperTemplate.querySelector(".commentProfile");
            const img = document.createElement('img');
            img.src=`/images/${e.userDto.profile_img}`;
            commentProfile.appendChild(img);
            img.style.width="3em";
            img.style.height="3em";
            img.style.borderRadius="50%";
            
            const userNickname = newCommentWrapperTemplate.querySelector(".userNickname");
            userNickname.innerText=e.userDto.nickname;

            const commentContent = newCommentWrapperTemplate.querySelector(".commentContent");
            commentContent.innerText = e.commentList.content;

            const commentDate = newCommentWrapperTemplate.querySelector(".commentDate");
            const date = new Date(e.commentList.created_at);
            function formatTwo(num) {
                return num < 10 ? '0' + num : num.toString();
            }
        
            commentDate.innerText= `${date.getFullYear()}.${formatTwo(date.getMonth()+1)}.${formatTwo(date.getDate())} ${formatTwo(date.getHours())}:${formatTwo(date.getMinutes())}`;

            // Gpt
            const gptContent = newCommentWrapperTemplate.querySelector(".gptContent");

            if (e.chatGptReviewDto && e.chatGptReviewDto.gpt_Review) {
                gptContent.innerText=e.chatGptReviewDto.gpt_Review;
            } else {
                gptContent.innerText = "GPT 요약 내용이 없습니다.";
            }


            if(myId != null && e.commentList.club_member_id == myId){

                const commentButton = newCommentWrapperTemplate.querySelector(".commentButton");
                commentButton.classList.remove("hidden");
                const deleteComment = newCommentWrapperTemplate.querySelector(".deleteComment");
                deleteComment.setAttribute("onclick",`deleteComment(${e.commentList.id})`);
                
                const updateCommentProcess = newCommentWrapperTemplate.querySelector(".updateCommentProcess");
                updateCommentProcess.setAttribute("onclick",`updateComment(${e.commentList.id})`);
            
            }

            commentListBox.appendChild(newCommentWrapperTemplate);
        }


        
    })
}

// Gpt
function GptContent(commentId){
    const url = "/api/club/GptContent?id="+commentId;
    fetch(url)
    .then(response => response.json())
    .then((response)=>{
    })
    
}
// // // Gpt
function chatGptCommentExist(commentId){
    const url = "/api/club/chatGptCommentExist?comment_id="+commentId;
    fetch(url)
    .then(response => response.json())
    .then((response)=>{
        if(response.data.chatGptCommentExist == true){
            return;
        }
        else{
            GptContent(commentId);
        }
    })

}


function showUpdateComment(){
    const updateList = document.querySelectorAll("#commentListBox .updateRow");
    for(e of updateList){
        e.classList.add("d-none");
    }
    //const target = event.target; 부모 의 타겟으로 상속하기위해 target을 사용!!!!
    //const commentWrapper = target.closest(".commentWrapper"); 이런것 처럼!!!!!!!

    const target = event.target;
    const commentWrapper = target.closest(".commentWrapper");
    const text = commentWrapper.querySelector(".commentContent").innerText;
    commentWrapper.querySelector(".commentUpdateText").value=text;

    const updateRow = commentWrapper.querySelector(".updateRow");
    updateRow.classList.remove("d-none");


}

function deleteComment(commentId){
    const url = "/api/club/deleteComment?id="+commentId;

    fetch(url)
    .then(response => response.json())
    .then((response) => {
        commentList();

    })
}

function updateComment(commentId){
    const target = event.target;
    const commentWrapper = target.closest(".commentWrapper");
    const text = commentWrapper.querySelector(".commentUpdateText").value;
    const url = "/api/club/updateComment";

    fetch(url,{
        method: "post",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded" // 나중에 필요시 multipart/formdata, json
        },
        body: `id=${commentId}&content=${text}`
    })
    .then(response => response.json())
    .then((response) => {
        commentList();
    })

}

function deleteContent(){
    window.location.href = "/club/delete/content?id="+boardId+"&club_id="+clubId;
}
function goToHome(){
    
    location.href="/club/detail/home?id="+clubId;
}

function showDeleteConfirmModal() {
    const deleteConfirmModal = bootstrap.Modal.getOrCreateInstance("#deleteConfirmModal");
    deleteConfirmModal.show();
}

function freeBoardReport(){
    const freeBoardReportModal = bootstrap.Modal.getOrCreateInstance("#freeBoardReportModal");
    freeBoardReportModal.show();
}


function registBoardReport(){
    if(myId == null){
        if(confirm("로그인 하셔야 이용 가능합니다.")){
            location.href = "/user/loginPage";
        }
        return;
    }
    const boardReportReason = document.getElementById("boardReportReason");
    const url = "/api/club/registBoardReport";
    fetch(url,{
        method:"post",
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded' // URL 인코딩된 데이터를 보냄을 명시
        },
        body: `club_board_id=${boardId}&report_reason=${boardReportReason.value}&club_id=${clubId}`

    })
    .then(response => response.json())
    .then(response => {
        boardReportReason.value = '';
        boardReportModalClose();
        showMessage();

    })
}

function boardReportModalClose() {
    const freeBoardReportModal = bootstrap.Modal.getOrCreateInstance("#freeBoardReportModal");
    freeBoardReportModal.hide();
}


function showMessage() {
    const reportSuccessModal = bootstrap.Modal.getOrCreateInstance("#reportSuccessModal");
    reportSuccessModal.show();

    setTimeout(() => {
        reportSuccessModal.hide();
    }, 1500);

}


window.addEventListener("DOMContentLoaded",() => {
    setSessionId();
    boardDetail();
    likeTotalCount();
    commentList();
});
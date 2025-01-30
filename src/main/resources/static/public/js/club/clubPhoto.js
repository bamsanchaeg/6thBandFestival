// 사진첩 등록하기
function registPhotoBook(){
    window.location.href = '/club/registPhotoBook?id='+clubId;
}

function photoBookLikeExist(e, newPhotoWrapper){
    const url = "/api/club/photoBookLikeExist?club_photo_id=" + e.clubPhotoDto.id + "&club_member_id=" + myId;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        const heart = newPhotoWrapper.querySelector(".heart");


        if (response.data.photoBookLikeExist === false) {
            heart.onclick = () => likePhoto(e.clubPhotoDto.id, heart,newPhotoWrapper);
            
        } else {
            heart.classList.add("fill-1", 'text-danger');
            heart.onclick = () => unLikePhoto(e.clubPhotoDto.id, heart,newPhotoWrapper);
        }
    });
}

function likePhoto(photoId, heartElement,newWrapper){
    const url = "/api/club/memberAndResidentExist?id="+clubId+"&user_id="+myId;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        if (!response.data.residentInfo.residentExist && !response.data.residentInfo.memberExist) {
            alert("권한이 없습니다. 모임가입을 해주세요");
            location.href = "/club/detail/home?id=" + clubId;
        } 
        else {

            const url = "/api/club/likePhoto?club_photo_id=" + photoId + "&club_member_id=" + myId;
            fetch(url)
            .then(response => response.json())
            .then((response) => {
                heartElement.classList.add("fill-1", 'text-danger');
                increaseLikeCount(newWrapper);
                heartElement.onclick = () => unLikePhoto(photoId, heartElement,newWrapper);
            });
        }
    })

}

function unLikePhoto(photoId, heartElement,newWrapper){
    const url = "/api/club/memberAndResidentExist?id="+clubId+"&user_id="+myId;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        if (!response.data.residentInfo.residentExist && !response.data.residentInfo.memberExist) {
            alert("권한이 없습니다. 모임가입을 해주세요");
            location.href = "/club/detail/home?id=" + clubId;
        } 
        else {
            const url = "/api/club/unLikePhoto?club_photo_id=" + photoId + "&club_member_id=" + myId;
            fetch(url)
            .then(response => response.json())
            .then((response) => {
                heartElement.classList.remove("fill-1", 'text-danger');
                decreaseLikeCount(newWrapper);

                heartElement.onclick = () => likePhoto(photoId, heartElement,newWrapper);
            });
        }
    })
}

function increaseLikeCount(newWrapper){
    const heartCount = newWrapper.querySelector(".heartCount");
    heartCount.innerText = parseInt(heartCount.innerText)+1;
}

function decreaseLikeCount(newWrapper){
    const heartCount = newWrapper.querySelector(".heartCount");
    heartCount.innerText = parseInt(heartCount.innerText)-1;
}
// 사진첩 댓글 모달열기
function photoCommentModalOpen(photoId) {
    console.log(photoId);
    const commentModal = new bootstrap.Modal(document.getElementById('commentModal')); // 수정된 부분
    photoCommentList(photoId);
    const photoCommentButton = document.getElementById("photoCommentButton");
    photoCommentButton.onclick = () => {photoCommentRegist(photoId)};
    commentModal.show();

// commentButton.onclick = () => photoCommentRegist(photoId); 부분을 추가하는 이유는, 
// 모달이 열리고 나서 사용자가 댓글 작성 버튼을 클릭할 때 photoCommentRegist 함수가 호출되도록 하기 위함입니다. 
// 이를 통해, 사용자가 댓글을 작성하고 "댓글 작성" 버튼을 클릭하면 해당 댓글과 함께 photoId가 서버로 전송됩니다.
    
}


// 사진첩 사진 모달열기
// function photoModalOpen(photoId) {
//     const photoModal = bootstrap.Modal.getOrCreateInstance("#photoModal");
//     photoCommentList(photoId);
//     // const photoCommentButton = document.getElementById("photoCommentButton");
//     // photoCommentButton.onclick = () => {photoCommentRegist(photoId)};
//     commentWrite();
//     photoModal.show();
// // commentButton.onclick = () => photoCommentRegist(photoId); 부분을 추가하는 이유는, 
// // 모달이 열리고 나서 사용자가 댓글 작성 버튼을 클릭할 때 photoCommentRegist 함수가 호출되도록 하기 위함입니다. 
// // 이를 통해, 사용자가 댓글을 작성하고 "댓글 작성" 버튼을 클릭하면 해당 댓글과 함께 photoId가 서버로 전송됩니다.
    
// }

function chairmanMember(){
    const url = "/api/club/memberResident?club_id="+clubId+"&user_id="+myId;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        if(response.data.memberClubRegist != null){
            
            const writeBoard = document.getElementById("photoRegistButton");
            if (photoRegistButton) {
                photoRegistButton.classList.remove("d-none");
            }
                
        }


    });

    const memberUrl = "/api/club/isMemberRight?club_id="+clubId+"&user_id="+myId;
    fetch(memberUrl)
    .then(response => response.json())
    .then((response) => {
        if(response.data.isMemberRight != null){
            const writeBoard = document.getElementById("photoRegistButton");
            if (photoRegistButton) {
                photoRegistButton.classList.remove("d-none");
            }
                
        }
    })
}


function chairmanMember(){
    const url = "/api/club/memberAndResidentExist?id="+clubId+"&user_id="+myId;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        if(!response.data.residentInfo.residentExist && !response.data.residentInfo.memberExist){
            const photoRegistButton = document.getElementById("photoRegistButton");
            if (photoRegistButton) {
                photoRegistButton.addEventListener("click",function(event) {

                    alert("모임 가입하여야 이용 가능합니다. 모임가입을 해주세요.")
                        location.href="/club/detail/home?id="+clubId;
                })
            
            }
            

        }
    })



}


function commentWrite(){
    if(myId == null){
        const commentTextarea = document.getElementById("inputPhotoComment");
        const photoCommentButton = document.getElementById("photoCommentButton");
        photoCommentButton.disabled=true;
        commentTextarea.placeholder = "댓글 권한이 없습니다.";
        commentTextarea.disabled = true;
    }
            
    const url = "/api/club/clubAndRegistMember?user_id="+myId+"&club_id="+clubId;
    fetch(url)
    .then(response => response.json())
    .then((response)=>{
        if(response.data.clubAndRegistMember == false){
            const commentTextarea = document.getElementById("inputPhotoComment");
            commentTextarea.placeholder = "댓글 권한이 없습니다.";
            commentTextarea.disabled = true;


            
        }           

    })

    
}


function notMemberExist(){

    const url = "/api/club/memberAndResidentExist?id="+clubId+"&user_id="+myId;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        const commentIcon = document.querySelectorAll(".commentIcon");
        const heartIcon = document.querySelectorAll(".heartIcon");  
        if(!response.data.residentInfo.residentExist && !response.data.residentInfo.memberExist){
            
            commentIcon.forEach(icon => {
                icon.addEventListener('click', (event)=>{
                    event.preventDefault();
                    event.stopPropagation();

                    alert("권한이 없습니다. 모임가입을 해주세요");
                    location.href="/club/detail/home?id="+clubId;

                })
            })
        }

    })
}

function photoCommentRegist(photoId){
    const inputPhotoComment = document.getElementById("inputPhotoComment");

    const url = "/api/club/photoCommentRegist";
    fetch(url, {
        method: "post",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: `club_photo_id=${photoId}&club_member_id=${myId}&content=${inputPhotoComment.value}&club_id=${clubId}`
    })
    .then(response => response.json())
    .then((response) => {

        inputPhotoComment.value="";
        photoCommentList(photoId);
    })
}

function photoCommentList(photoId){
    const url = "/api/club/photoCommentList?club_photo_id="+photoId+"&club_id="+clubId;
    fetch(url)
    .then(response => response.json())
    .then((response)=>{
        const photoCommentListBox = document.querySelector(".photoCommentListBox");
        photoCommentListBox.innerHTML="";

        const photoCommentTemplate = document.querySelector("#photoCommentTemplate .photoCommentWrapper");

        for(e of response.data.photoCommentList){
            const newPhotoCommentTemplate = photoCommentTemplate.cloneNode(true);

            const imageWrapper = newPhotoCommentTemplate.querySelector(".imageWrapper");
            const img = document.createElement('img');
            img.src=`/images/${e.userDto.profile_img}`;
            img.style.height = '60px';
            img.style.width = '60px';
            img.style.borderRadius='50%';

            imageWrapper.appendChild(img);

            const userNickname = newPhotoCommentTemplate.querySelector(".userNickname");
            userNickname.innerText = e.userDto.nickname;

            const userAuthority = newPhotoCommentTemplate.querySelector(".userAuthority");
            if(e.clubDto.user_id == e.photoCommentDto.club_member_id){
                userAuthority.classList.remove("d-none");
            }
            

            const photoCommentContent = newPhotoCommentTemplate.querySelector(".photoCommentContent");
            photoCommentContent.innerText = e.photoCommentDto.content;

            const photoCommentDate = newPhotoCommentTemplate.querySelector(".photoCommentDate");
            const date = new Date(e.photoCommentDto.created_at);
            
            function formatTwo(num) {
                return num < 10 ? '0' + num : num.toString();
            }
        
            photoCommentDate.innerText= `${date.getFullYear()}.${formatTwo(date.getMonth()+1)}.${formatTwo(date.getDate())} ${formatTwo(date.getHours())}:${formatTwo(date.getMinutes())}`;

            if(myId != null && myId == e.photoCommentDto.club_member_id){
                const editComment = newPhotoCommentTemplate.querySelector(".editComment");
                editComment.classList.remove("d-none");
            }

            photoCommentListBox.appendChild(newPhotoCommentTemplate);
        }
        
    })
    

}
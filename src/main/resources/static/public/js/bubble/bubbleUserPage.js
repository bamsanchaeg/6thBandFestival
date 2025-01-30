/**버블 유저 개인페이지(세션)에 해당하는 자바스크립트 코드**/

const urlParams = new URL(window.location.href).searchParams;
const userId = urlParams.get("id");
const userUrl = "/api/bubble/bubbleUserMyPage"
console.log( userId);



function getUserInformationForUserPage(){
    fetch(userUrl)
        .then(response => response.json())
        .then(response =>{



            const e = response.data.userInfo;
            //배열에 들어가 있어서 아래와 같이 명시함

            const userName = document.getElementById("userName");
            userName.innerText = e.userId.account_name;
            console.log("유저네임", userName);

            // const accountName = document.getElementById("accountName");
            // accountName.innerText = e.userId.account_name;
            // console.log("계정", accountName);


            const postCount = document.getElementById("postCount");
            postCount.innerText = e.userId.post_count;

            const userProfileImg = document.getElementById("userProfileImg");
            userProfileImg.src = '/images/' + e.userId.profile_img;

            const following = document.getElementById("following");
            following.innerText = e.userId.following;

            const follower = document.getElementById("follower");
            follower.innerText = e.userId.follower;
            console.log("팔로워출력",follower);

            //postListBox의 Html을 비워놓겠다.
            const postBox = document.getElementById("postBox");
            postBox.innerHTML = "";

            const Artist = document.getElementById("Artist");
            if (e.userId.artist === 'Y') { // e.artistStatus가 'Y'인 경우

                // 아이콘을 추가할 HTML 생성
                const iconElement = document.createElement('span');
                iconElement.className = 'material-symbols-outlined';
                iconElement.innerText = 'check_circle'; // 아이콘 이름 설정

                // 아이콘을 Artist 요소에 추가
                Artist.appendChild(iconElement);
            }



            /**
             *    1.	newPostWrapper 사용: 복제된 노드(newPostWrapper)의 자식 요소를 수정하도록 변경했습니다. 이 부분이 핵심입니다.
             *    2.    기존 코드는 원본 템플릿(postimageWrapperTemplete)의 요소를 수정하고 있었기 때문에, 결과적으로 마지막 이미지 정보만 덮어쓰여지고 있었습니다.
             *    3.	postBox에 newPostWrapper 추가: 수정된 newPostWrapper를 postBox에 추가하도록 했습니다.**/

            const images = e.postImages;
            console.log("이미지",images);

            // 작성된 게시물이 없을 경우 "작성된 게시물이 없습니다" 문구 추가
            if (images.length === 0) {
                const noPostsMessage = document.createElement("div");
                noPostsMessage.innerText = "작성한 게시물이 없습니다.";
                noPostsMessage.className = "no-posts-message"; // 스타일 적용을 위한 클래스
                postBox.appendChild(noPostsMessage);
            } else {
                const postimageWrapperTemplete = document.querySelector("#templete .postListWrapper");


                for (let image of images) {
                    // 새로운 포스트 래퍼 복사
                    const newPostWrapper = postimageWrapperTemplete.cloneNode(true);
                    newPostWrapper.classList.remove("d-none");

                    // 새로운 포스트 래퍼 안의 요소 수정
                    const postThumbnail = newPostWrapper.querySelector(".postThumbnail");
                    postThumbnail.src = '/images/' + image.image_url;

                    const postDetail = newPostWrapper.querySelector(".postDetail");
                    postDetail.href = '/bubble/bubbleDetailPage?id=' + image.post_id;

                    // 수정된 새로운 포스트 래퍼를 postBox에 추가
                    postBox.appendChild(newPostWrapper);
                }
            }
        })
}

let receiverId = null;

function createMessenger(userId){
    receiverId = userId;
    console.log("receiver", userId);
    const url = "/bubble/bubbleChatPage?id=" + receiverId;
    window.location = url;
}

// 오버레이를 보이게 하는 함수
function showOverlay() {
    document.getElementById('overlay').style.display = 'block';
}

// 오버레이를 숨기는 함수
function hideOverlay() {
    document.getElementById('overlay').style.display = 'none';
}




//function 밖에 있어야함.
window.addEventListener("DOMContentLoaded",()=>{
    getUserInformationForUserPage();
    //유저 아이디 가져오기, 유효성 검사. 이 부분 나중에 배포 전에 다 돌려놓고 테스트
    // setSessionId();
})
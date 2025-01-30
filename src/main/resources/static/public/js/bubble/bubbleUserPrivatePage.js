/**버블 유저 개인 페이지에 해당하는 자바스크립트 코드**/


const urlParams = new URL(window.location.href).searchParams;
const userId = urlParams.get("id");
//유저 개인 정보 페이지, 유저가 쓴 게시물하고 이미지까지 다 불러옴, 여기서 썸네일을 어떻게 풀어내는지가 중요할듯
const userPrivateUrl = "/api/bubble/bubbleUserPrivatePage?following_id=" + userId;

function getUserInformationForUserPage(){
    fetch(userPrivateUrl)
        .then(response => response.json())
        .then(response =>{
            const e = response.data.userInfo;
            //배열에 들어가 있어서 아래와 같이 명시함, 리스트의 경우 배열번호로 명시해야함(getElementById의 경우)
            //자료구조를 맵 형식으로 바꿈
            console.log("e 출력",e);

            // const userName = document.getElementById("userName");
            // userName.innerText = e.userId.name;

            const accountName = document.getElementById("accountName");
            accountName.innerText = e.userId.account_name;

            const postCount = document.getElementById("postCount");
            postCount.innerText = e.userId.post_count;

            const following = document.getElementById("following");
            following.innerText = e.userId.following;

            const userProfileImg = document.getElementById("userProfileImg");
            userProfileImg.src = '/images/' + e.userId.profile_img;

            const Artist = document.getElementById("Artist");
            if (e.userId.artist === 'Y') { // e.artistStatus가 'Y'인 경우

                // 아이콘을 추가할 HTML 생성
                const iconElement = document.createElement('span');
                iconElement.className = 'material-symbols-outlined';
                iconElement.innerText = 'check_circle'; // 아이콘 이름 설정

                // 아이콘을 Artist 요소에 추가
                Artist.appendChild(iconElement);
            }


            const follower = document.getElementById("follower");
            follower.innerText = e.userId.follower;
            console.log("팔로워출력",follower);

            // 팔로우/언팔로우 버튼 설정
            const userFollow = document.getElementById("userFollow");
            const followButton = userFollow.querySelector('button');
            const followingId = response.data.userInfo.userId.user_id;
            let isFollowing = response.data.followStatement.follow;

            userFollow.setAttribute("following_id", followingId);
            followButton.textContent = isFollowing ? "언팔로우하기" : "팔로우하기";
            followButton.classList.toggle('unfollow', isFollowing);
            followButton.classList.toggle('follow', !isFollowing);
            followButton.addEventListener('click',function (){
                const userId = userFollow.getAttribute("following_id");
                console.log("userId:", userId); // userId 값 확인
                console.log("세션출력",response.data.sessionStatement);

                if(response.data.sessionStatement.sessionNull === null){
                    alert("권한이 없습니다.");
                    return;
                }

                if (!userId) {
                    console.error("following_id 속성이 설정되어 있지 않습니다.");
                    return;
                }

                if(isFollowing){
                    fetch(`/api/bubble/unfollowUser`, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: `following_id=${userId}`
                    })
                        .then(response => {
                            console.log("Unfollow response status:", response.status); // 응답 상태 확인
                            return response.json();
                        })
                        .then(data => {
                            console.log("Unfollow response:", data); // 응답 확인
                            if (data.result === "success") {
                                // 성공적으로 언팔로우 한 경우
                                followButton.textContent = "팔로우하기";
                                followButton.classList.remove('unfollow');
                                followButton.classList.add('follow');
                                isFollowing = false; // 상태 업데이트
                                location.reload();
                            } else {
                                console.error("Unfollow failed:", data);
                            }
                        })
                        .catch(error => {
                            console.error("Error unfollowing:", error);
                        });
                }else{
                    fetch(`/api/bubble/followUser?following_id=${userId}`,{method:'POST'})
                        .then(response => response.json())
                        .then(data =>{
                            if(data.result === "success"){
                                // 성공적으로 팔로우 한 경우
                                followButton.textContent = "언팔로우하기";
                                followButton.classList.remove('follow');
                                followButton.classList.add('unfollow');
                                isFollowing = true; // 상태 업데이트
                                location.reload();
                            }
                        }) .catch(error => {
                        console.error("Error following:", error);
                    });
                }

            })



            const receiver = document.getElementById("receiver");
            //여기서 이미 채팅방 생성되었으면 링크로 보내주고, 아니면 새로 만들기 해야할듯
            receiver.onclick =() => createMessenger(userId);




            const images = e.postImages;
            console.log("이미지",images);

            const postListBox = document.getElementById("postListBox");
            postListBox.innerHTML = "";

            if (images.length === 0) {
                // 게시물이 없는 경우 문구 추가
                const noPostsMessage = document.createElement("div");
                noPostsMessage.innerText = "작성된 게시물이 없습니다.";
                noPostsMessage.className = "no-posts-message"; // 스타일 적용을 위한 클래스
                postListBox.appendChild(noPostsMessage);
            } else {
                const postWrapperTemplete = document.querySelector("#templete .postListWrapper");

                // create_date를 기준으로 오름차순 정렬
                images.sort((a, b) => new Date(a.create_date) - new Date(b.create_date));

                for (let image of images) {
                    const newPostWrapper = postWrapperTemplete.cloneNode(true);
                    newPostWrapper.classList.remove("d-none");

                    const postThumbnail = newPostWrapper.querySelector(".postThumbnail");
                    postThumbnail.src = '/images/' + image.image_url;

                    const postDetail = newPostWrapper.querySelector(".postDetail");
                    postDetail.href = '/bubble/bubbleDetailPage?id=' + image.post_id;

                    postListBox.appendChild(newPostWrapper);
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

//function 밖에 있어야함.
window.addEventListener("DOMContentLoaded",()=>{
    getUserInformationForUserPage();
})
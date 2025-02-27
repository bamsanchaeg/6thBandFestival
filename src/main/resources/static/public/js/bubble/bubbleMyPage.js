function getSubscribePostList() {
    const url = "/api/bubble/followingBubblePage"

    fetch(url)
        .then(response => response.json())
        .then(response => {


            const postListBox = document.getElementById("postListBox");
            postListBox.innerHTML = "";

            if (!response.data.postList || response.data.postList.length === 0) {
                // 로그인은 했으나 구독한 계정이 없는 경우
                const noSubscriptionMessage = document.createElement('div');
                noSubscriptionMessage.innerHTML = "<p>현재 구독하고있는 계정이 없습니다.</p><p>좋아하는 아티스트를 추가해보세요.</p>";
                noSubscriptionMessage.className = "no-subscription-message"; // 스타일을 적용할 수 있도록 클래스 추가
                postListBox.appendChild(noSubscriptionMessage);
            }


            // // response.data 및 postList 유효성 확인
            // if (response.data && response.data.sessionId) {
            //     // 사용자가 로그인한 경우
            //
            //
            // }else{

            const postWrapperTemplete = document.querySelector("#templete .postListWrapper");
            console.log('데이터 출력', response.data.postList);

            for (let e of response.data.postList) {

                const newPostWrapper = postWrapperTemplete.cloneNode(true);
                newPostWrapper.classList.remove('d-none');

                const postContent = newPostWrapper.querySelector(".postContent");
                postContent.innerText = e.postDto.content;


                const artistStatement = newPostWrapper.querySelector(".artistStatement");
                if (artistStatement) {
                    if (e.postDto.artist === 'Y') { // e.artistStatus가 'Y'인 경우
                        // 아이콘을 추가할 HTML 생성
                        const iconElement = document.createElement('span');
                        iconElement.className = 'material-symbols-outlined';
                        iconElement.innerText = 'check_circle'; // 아이콘 이름 설정

                        // 아이콘을 artistStatement 요소에 추가
                        artistStatement.appendChild(iconElement);
                    }
                    // 'Y'가 아닌 경우에는 아이콘을 추가하지 않지만, col 요소는 그대로 유지됨
                }

                // 프로필 이미지를 처리하는 로직
                const profilePic = newPostWrapper.querySelector(".profilePic");// 프로필 이미지를 감싸는 컨테이너 선택
                profilePic.src = '/images/' + e.postDto.profile_img;

                const userPostPrivatePage = newPostWrapper.querySelector(".userPostPrivatePage")
                userPostPrivatePage.href = './bubbleUserPrivatePage?id=' + e.postDto.user_id;

                const postImage = newPostWrapper.querySelector(".postImage");
                postImage.src = '/images/' + e.postDto.image_url;

                const accountName = newPostWrapper.querySelector(".accountName");
                accountName.innerText = e.postDto.account_name;

                const postingDate = newPostWrapper.querySelector(".postingDate");
                postingDate.innerText = formatDate(e.postDto.created_at);

                const bubbleLikecount = newPostWrapper.querySelector(".bubbleLike");
                const likeCount = e.postDto.like_count;
                if (likeCount === 0) {
                    // like_count가 0일 경우 요소를 숨깁니다.
                    bubbleLikecount.style.display = 'none';
                } else {
                    // like_count가 0이 아닐 경우 텍스트를 설정하고 요소를 표시
                    bubbleLikecount.innerText = "like" + " " + e.postDto.like_count;
                    bubbleLikecount.style.display = 'block'; // 혹시 요소가 숨겨져 있을 경우를 대비해 다시 표시
                }

                //댓글 갯수 세기
                const commentCount = newPostWrapper.querySelector(".commentCount");
                const numberOfComment = e.numberOfComment;
                if (numberOfComment === 0) {
                    commentCount.style.display = 'none';
                } else {
                    commentCount.innerText = "comments" + " " + e.numberOfComment;
                }

                const postHeart = newPostWrapper.querySelector(".postHeart");
                postHeart.setAttribute('data-post-id', e.postDto.post_id); // 포스트 ID를 데이터 속성에 저장
                // LikeOrUnLike 상태에 따른 버튼 스타일 설정
                if (e.LikeOrUnLike) {
                    postHeart.classList.add('liked'); // 이미 좋아요를 누른 상태로 표시
                } else {
                    postHeart.classList.remove('liked'); // 좋아요를 누르지 않은 상태로 표시
                }

                // 클릭 이벤트 설정
                postHeart.addEventListener('click', function () {
                    const isLiked = postHeart.classList.contains('liked');
                    const postId = postHeart.getAttribute('data-post-id');
                    console.log("게시물 아이디", postId);

                    if (isLiked) {
                        // 좋아요 상태였으면 취소
                        undoLike(postId);
                        postHeart.classList.remove('liked'); // 빈 하트로 변경
                    } else {
                        // 좋아요 상태가 아니었으면 좋아요
                        doLike(postId);
                        postHeart.classList.add('liked'); // 채워진 하트로 변경
                    }
                });


                // offcanvas를 여는 부분, 이 부분을 클릭할때마다 postId 할당
                const chatBubble = newPostWrapper.querySelector(".chat_bubble");
                chatBubble.onclick = () => showOffcanvas(e.postDto.post_id);


                postListBox.appendChild(newPostWrapper);

            }

            // }


        })

}

let currentPostId = null; // 현재 선택된 게시물 ID를 저장할 변수

function showOffcanvas(postId) {
    currentPostId = postId; // 클릭한 게시물의 postId 저장
    console.log("postId : ", postId);
    var offcanvas = new bootstrap.Offcanvas(document.getElementById("offcanvasBottom"));
    offcanvas.show();

    loadComments(postId);
}

function loadComments(postId) {
    const url = `/api/bubble/getCommentListByPostId?id=${postId}`;
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json();
        })
        .then(comments => {
            if (comments.result === "success") {
                const commentData = comments.data.CommentList || [];
                console.log("comments", commentData);

                const commentList = document.querySelector(".comment-list");
                if (!commentList) {
                    console.error('No element found with class "comment-list"');
                    return;
                }
                commentList.innerHTML = "";

                commentData.forEach(item => {
                    if (item.comment) {
                        const {comment} = item;
                        const commentElement = document.createElement("div");
                        commentElement.className = "comment";
                        commentElement.innerHTML = `
                        <div class="row">
                            <div class="col postComments">
                             <div><strong class="commentUserId">${comment.account_name}</strong></div>
                                <div class="commentContents">${comment.content || ''}</div>
                                <div class="text-muted">${new Date(comment.created_at).toLocaleString()}</div>
                            </div>
                        </div>
                    `;
                        commentList.appendChild(commentElement);
                    } else if (item.commentCount !== undefined) {
                        // You can use commentCount if needed
                        console.log('Comment Count:', item.commentCount);
                    } else {
                        console.warn('Invalid item structure:', item);
                    }
                });

                if (commentList.innerHTML === "") {
                    commentList.innerHTML = "<p>No comments available.</p>";
                }
            } else {
                console.error('Failed to load comments:', comments.reason);
                alert('Failed to load comments. Please try again later.');
            }
        })
        .catch(error => {
            console.error('Error fetching comments:', error);
            alert('An error occurred while fetching comments.');
        });
}

// 오버레이를 보이게 하는 함수
function showOverlay() {
    document.getElementById('overlay').style.display = 'block';
}

// 오버레이를 숨기는 함수
function hideOverlay() {
    document.getElementById('overlay').style.display = 'none';
}

//좋아요 작성
function doLike(postId) {
    currentPostId = postId;
    const url = "/api/bubble/createLike?id=" + currentPostId;
    console.log("post_id : ", postId);
    fetch(url)
        .then(response => response.json())
        .then(response => {
            console.log("After fetch response:", response); // 응답 확인 후 로그 찍기
            // 좋아요 성공 시 하트 모양 변경
            location.reload();
        }).catch(error => console.error("Error during fetch:", error)); // 오류가 발생할 경우 로그 찍기

}


//좋아요 취소
function undoLike(postId) {
    currentPostId = postId; // 클릭한 게시물의 postId 저장
    console.log("postId : ", postId);

    const url = "/api/bubble/deleteLike?post_id=" + currentPostId;
    fetch(url)
        .then(response => response.json())
        .then(response => {
            console.log("After fetch response:", response); // 응답 확인 후 로그 찍기
            // 좋아요 성공 시 하트 모양 변경
            location.reload();
        }).catch(error => console.error("Error during fetch:", error)); // 오류가 발생할 경우 로그 찍기
}

//댓글작성
function registerCommentForSubscribe() {

    //링크를 넣어주겠습니다, 여기 코드 잘못됨 체크해야함
    //post 방식은 데이터를 get처럼 링크로 보내는게 아니기 때문에 body에다 보내야함
    //백틱문법의 $표시 타임리프랑 구분 조심
    const inputComment = document.getElementById("inputComment");
    if (!inputComment) {
        alert("댓글 내용을 입력해 주세요.");
        return;
    }

    const url = "/api/bubble/registerComment";
    fetch(url, {
        method: "post",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: `post_id=${currentPostId}&content=${inputComment.value}`
    })
        .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text(); // 응답을 텍스트로 받아서 확인
            }
        )
        .then(text => {

            if (text) {
                try {
                    const data = JSON.parse(text); // 텍스트가 존재하면 JSON으로 파싱
                    console.log('Success:', data);
                    inputComment.value = ""; // 입력 필드 비우기

                    // 작성한 댓글을 새로고침하여 불러오기
                    loadComments(currentPostId);
                } catch (e) {
                    console.error('Parsing error:', e);
                }
            } else {
                console.warn('Empty response');
            }
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}


// 월 이름 배열 생성
const monthNames = [
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
];

// 날짜 변환 함수
function formatDate(isoDateString) {
    // Date 객체로 변환
    const date = new Date(isoDateString);
    const day = date.getDate(); // 일
    const monthIndex = date.getMonth(); // 월 인덱스 (0부터 시작)
    const monthName = monthNames[monthIndex]; // 월 이름

    return `${monthName} ${day}`;
}


window.addEventListener("DOMContentLoaded", () => {
    getSubscribePostList();
})
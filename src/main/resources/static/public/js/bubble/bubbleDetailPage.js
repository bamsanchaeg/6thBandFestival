const urlParams = new URL(window.location.href).searchParams;
const articleId = urlParams.get("id");
const url = '/api/bubble/getBubbleDetail?id=' + articleId;

function getBubbleDetail() {
    fetch(url)
        .then(response => response.json())
        .then(response => {

            const e = response.data.bubbleDetail

            const accountName = document.getElementById("accountName");
            accountName.innerText = e.bubbleDetail.account_name;

            const accountNameTitle = document.getElementById("accountNameTitle");
            accountNameTitle.innerText = e.bubbleDetail.account_name;

            const bubbleImage = document.getElementById("bubbleImage");
            bubbleImage.src = '/images/' + e.bubbleDetail.image_url;

            const profilePic = document.getElementById("profilePic");
            profilePic.src = '/images/' + e.bubbleDetail.profile_img;

            const userPostPrivatePage = document.getElementById("userPostPrivatePage")
            userPostPrivatePage.href = './bubbleUserPrivatePage?id=' + e.bubbleDetail.user_id;

            const artistStatement = document.getElementById("artistStatement");
            if (artistStatement) {
                if (e.bubbleDetail.artist === 'Y') { // e.artistStatus가 'Y'인 경우
                    // 아이콘을 추가할 HTML 생성
                    const iconElement = document.createElement('span');
                    iconElement.className = 'material-symbols-outlined';
                    iconElement.innerText = 'check_circle'; // 아이콘 이름 설정

                    // 아이콘을 artistStatement 요소에 추가
                    artistStatement.appendChild(iconElement);
                }
                // 'Y'가 아닌 경우에는 아이콘을 추가하지 않지만, col 요소는 그대로 유지됨
            }

            const bubbleLikecount = document.getElementById("bubbleLike");
            const likeCount = e.bubbleDetail.like_count;
            if (likeCount === 0) {
                // like_count가 0일 경우 요소를 숨깁니다.
                bubbleLikecount.style.display = 'none';
            } else {
                // like_count가 0이 아닐 경우 텍스트를 설정하고 요소를 표시
                bubbleLikecount.innerText = "like" + " " + e.bubbleDetail.like_count;
                bubbleLikecount.style.display = 'block'; // 혹시 요소가 숨겨져 있을 경우를 대비해 다시 표시
            }

            const commentCount = document.getElementById("commentCount");
            const numberOfComment = e.numberOfComment;
            if (numberOfComment === 0) {
                commentCount.style.display = 'none';
            } else {
                commentCount.innerText = "comment" + " " + e.numberOfComment;
            }

            const postHeart = document.getElementById("postHeart");
            postHeart.setAttribute('data-post-id', e.bubbleDetail.post_id); // 포스트 ID를 데이터 속성에 저장
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
            const chatBubble = document.getElementById("chat_bubble");
            chatBubble.onclick = () => showOffcanvas(e.bubbleDetail.post_id);


            const createAt = document.getElementById("createAt");
            createAt.innerText = formatDate(e.bubbleDetail.created_at);

            const bubbleContent = document.getElementById("bubbleContent");
            bubbleContent.innerText = e.bubbleDetail.content;

        })
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

//댓글작성
function registerComment() {

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


function loadComments(articleId) {
    const url = `/api/bubble/getCommentListByPostId?id=${articleId}`;
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
                    commentList.innerHTML = "<p>작성된 댓글이 없습니다. 새로운 댓글을 작성해보세요.</p>";
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

function showOffcanvas(articleId) {
    currentPostId = articleId; // 클릭한 게시물의 postId 저장
    console.log("postId : ", articleId);
    var offcanvas = new bootstrap.Offcanvas(document.getElementById("offcanvasBottom"));
    offcanvas.show();

    loadComments(articleId);
}

window.addEventListener("DOMContentLoaded", () => {
    getBubbleDetail();
})
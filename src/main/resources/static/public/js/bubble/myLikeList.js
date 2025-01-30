/**세션 유저가 좋아요 한 게시물 리스트 출력**/

const url = '/api/bubble/selectMyLikePost';
fetch(url)
    .then(response => response.json())
    .then(response => {

        const postListBox = document.getElementById("postListBox");
        postListBox.innerHTML = "";

        const postWrapperTemplete = document.querySelector("#templete .postListWrapper");

        console.log('데이터 출력', response.data.postList);

        if (!response.data.postList || response.data.postList.length === 0) {
            const noPostMessage = document.createElement('div');
            noPostMessage.innerText = "최근 좋아요한 게시물이 없습니다.";
            noPostMessage.className = "no-post-message"; // 스타일을 적용할 수 있도록 클래스 추가
            postListBox.appendChild(noPostMessage);
            return;
        }

        for (let e of response.data.postList) {

            const newPostWrapper = postWrapperTemplete.cloneNode(true);

            const postContent = newPostWrapper.querySelector(".postContent");
            postContent.innerText = e.postDto.content;

            const postImage = newPostWrapper.querySelector(".postImage");
            postImage.src = '/images/' + e.postDto.image_url;

            const postDetail = newPostWrapper.querySelector(".postDetail");
            postDetail.href = 'bubble/bubbleDetailPage?id=' + e.postDto.id;

            const likeTime = newPostWrapper.querySelector(".likeTime");
            likeTime.innerText = formatDate(e.postDto.likeTime);

            postListBox.appendChild(newPostWrapper);
        }


    })
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



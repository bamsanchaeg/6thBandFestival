let currentPage = 1;
const itemsPerPage = 10;
const maxPageButtons = 5;

function boardList(page,order){
    currentPage = page;

    const url = `/api/club/boardListAppear?club_id=${clubId}&page=${page}&order=${order}`;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        const boardListBox = document.getElementById("boardListBox");
        boardListBox.innerHTML="";

        const boardWrapperTemplate = document.querySelector("#boardTemplate .boardWrapper")
        for(let e of response.data.clubBoardList){
            const newBoardWrapperTemplate = boardWrapperTemplate.cloneNode(true);
            const categoryName = newBoardWrapperTemplate.querySelector(".categoryName");
            categoryName.innerText = e.categoryName;
            const imageContainer = newBoardWrapperTemplate.querySelector(".imageContainer");

            (function(boardId) {
                newBoardWrapperTemplate.addEventListener('click', function() {                         
                    // 클릭 시 이동할 링크 설정
                    window.location.href = `/club/board/detail?id=${boardId}`+"&clubId="+clubId;
                    
                });
            })(e.clubBoardDto.id); // 클로저 내부에서 e.clubBoardDto.id 값을 boardId로 전달

            if (e.clubBoardDetailImage.length > 0) {
                const img = document.createElement('img');
                img.src = `/images/${e.clubBoardDetailImage[0].location}`;
                img.classList.add("custom-image");
                img.classList.add("rounded-2")
                imageContainer.appendChild(img);

            }

            const boardTitle = newBoardWrapperTemplate.querySelector(".boardTitle");
            boardTitle.innerText=e.clubBoardDto.title;

            const boardWriteUser = newBoardWrapperTemplate.querySelector(".boardWriteUser");
            boardWriteUser.innerText=e.userDto.nickname;
            
            const boardReadCount = newBoardWrapperTemplate.querySelector(".boardReadCount");
            boardReadCount.innerText=e.clubBoardDto.read_count;

            const clubAuthority = newBoardWrapperTemplate.querySelector(".clubAuthority");

            if(e.clubDto.user_id == e.clubBoardDto.club_member_id){
                clubAuthority.classList.remove("d-none");
            }
            
            const boardWriteTime = newBoardWrapperTemplate.querySelector(".boardWriteTime");
            const date = new Date(e.clubBoardDto.created_at);

            const now = new Date();
            const postedDate = new Date(e.clubBoardDto.created_at);
            const diffInSeconds = Math.floor((now - postedDate) / 1000);

            const minutes = Math.floor(diffInSeconds / 60);
            const hours = Math.floor(minutes / 60);
            
            if (diffInSeconds < 60) {
                boardWriteTime.innerText='방금 전';
            } else if (minutes < 60) {
                boardWriteTime.innerText= `${minutes}분 전`;
            } else if (hours < 24) {
                boardWriteTime.innerText= `${hours}시간 전`;
            }   
            else{
                function formatTwo(num) {
                    return num < 10 ? '0' + num : num.toString();
                }
                boardWriteTime.innerText= `${date.getFullYear().toString()}.${formatTwo(date.getMonth()+1)}.${formatTwo(date.getDate())}`;
            }

            const commentCount = newBoardWrapperTemplate.querySelector(".commentCount");
            commentCount.innerText = e.commentCount;


            boardListBox.appendChild(newBoardWrapperTemplate);

        }
        setupPagination(response.data.boardCount, itemsPerPage, page,order);


    });
}


function rangeList(order,event){
    boardList(currentPage, order);  
    const rangeName = document.getElementById("rangeName");
    const rangeText = event.target.innerText;
    rangeName.textContent  = rangeText;
}

function setupPagination(totalItems, itemsPerPage, currentPage,order) {
    const pageCount = Math.ceil(totalItems / itemsPerPage);
    const pagination = document.getElementById("pagination");
    const nextPageElement = document.getElementById("nextPage");


    // 기존 페이지 번호 삭제
    const pageItems = pagination.querySelectorAll(".page-number");
    pageItems.forEach(item => item.remove());

    // 페이지 번호 생성
    for (let i = 1; i <= pageCount; i++) {
        const li = document.createElement("li");
        li.classList.add("page-item", "page-number");
        if (i === currentPage) {
            li.classList.add("active");
        }

        const a = document.createElement("a");
        
        a.href = "#";
        a.textContent = i;
        a.addEventListener("click", (e) => {
            e.preventDefault();
            boardList(i,order);  // 해당 페이지로 이동
        });

        li.appendChild(a);
        // page-link부트스트랩 pagination요소
        if(li.classList.contains("active")){
            a.classList.add("page-link" ,"text-light");

        }
        else{
            a.classList.add("page-link" ,"text-dark");

        }

            pagination.insertBefore(li, nextPageElement);
        
    }

    // 이전/다음 버튼 활성화/비활성화
    const prevPageItem = document.getElementById('prevPage').parentNode;
    const nextPageItem = document.getElementById('nextPage').parentNode;

    prevPageItem.classList.toggle('disabled', currentPage === 1);
    nextPageItem.classList.toggle('disabled', currentPage === pageCount);

    const prevPage = document.getElementById('prevPage');
    const nextPage = document.getElementById('nextPage');

    prevPage.onclick = (e) => {
        if (currentPage > 1) {
            boardList(currentPage - 1, order); // 이전 페이지로 이동
        }
    };

    nextPage.onclick = (e) => {
        if (currentPage < pageCount) {
            boardList(currentPage + 1, order); // 다음 페이지로 이동
        }
    };
}

function writeFreeboard(){
    window.location.href = '/club/boardWritePage?id='+clubId;

}



function residentInfo(){
    const url = "/api/club/memberResidentInfo?club_id="+clubId;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        
        const residentName = document.querySelector("#residentInfo .residentName");
        residentName.innerText= response.data.residentInfo.userDto.nickname;

        const imageResidentContainer = document.querySelector(".imageResidentContainer");
        const img = document.createElement('img');
        img.src = `/images/${response.data.residentInfo.userDto.profile_img}`;
        imageResidentContainer.appendChild(img);

        img.style.width="3.5em";
        img.style.height="3.5em";
        img.style.borderRadius="50%";

        const residentRegistTime = document.querySelector("#residentInfo .residentRegistTime");
        const date = new Date(response.data.residentInfo.clubDto.created_at);
        const padZero = (num) => num.toString().padStart(2, '0');

        residentRegistTime.innerText= `${date.getFullYear()}.${date.getMonth()+1}.${date.getDate()}`;
        
    })
}

function chairmanMember(){


    const url = "/api/club/memberAndResidentExist?id="+clubId+"&user_id="+myId;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        if(!response.data.residentInfo.residentExist && !response.data.residentInfo.memberExist){
            const writeBoard = document.getElementById("writeBoard");
            if (writeBoard) {
                writeBoard.addEventListener("click",function(event) {
                    event.preventDefault();
                    alert("모임 가입해야 이용 가능합니다. 모임가입을 해주세요.")
                        location.href="/club/detail/home?id="+clubId;
                        
                })
                
            }
            const inputComment = document.getElementById("inputComment");
            inputComment.placeholder = "댓글 작성 권한이 없습니다.";
            inputComment.disabled = true;

            const commentRegistButton = document.getElementById("commentRegistButton");
            commentRegistButton.disabled = true;

        }
    })

}


function notMemberExist(){

    const url = "/api/club/memberAndResidentExist?id="+clubId+"&user_id="+myId;
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        const mainContent = document.getElementById("mainContent");
        const notMemberSee = document.getElementById("notMemberSee");

        if(!response.data.residentInfo.residentExist && !response.data.residentInfo.memberExist){
            
            if(mainContent){
                mainContent.style.display="none";
                notMemberSee.classList.remove("d-none");

            }
        }
        else{
            if(mainContent){
                mainContent.style.display="block";

            }
        }
    })
}


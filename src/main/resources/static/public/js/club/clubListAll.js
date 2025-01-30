let myId = "";

function setSessionUserId(){
    const url = "/six/user/getSessionUserId";
    fetch(url)
    .then(response => response.json())
    .then((response) => {
        myId= response.data.id;
    })
}

    function memberLoginCheck() {
        if(myId == null) {
            if(confirm("로그인 후 이용 가능합니다. 로그인 페이지로 이동하시겠습니까?")) {
                location.href="user/registerPage";
            }
            return;
        }
    }


function categoryClick(id) {
    const url = "/api/club/appearClubCategoryList?id="+id;
    fetch(url)
    .then(response => response.json())
    .then((response) => {               
        const clubListBox = document.getElementById("clubListBox");
        clubListBox.innerHTML="";

        if (response.data.clubList.length === 0) {
            clubListBox.innerText="등록된 모임이 없습니다";
            clubListBox.classList.add("text-center","py-5");
        }
        else{
            clubListBox.classList.remove("text-center","py-5");
            const clubWrapperTemplate = document.querySelector("#clubTemplate .clubWrapper")
            for(let e of response.data.clubList){
                const newClubWrapperTemplate = clubWrapperTemplate.cloneNode(true);
                
                const imageContainer = newClubWrapperTemplate.querySelector(".imageContainer");

                const clubName = newClubWrapperTemplate.querySelector(".clubName");
                clubName.innerText = e.clubDto.club_name;

                (function(clubId) {

                    newClubWrapperTemplate.addEventListener('click', function() {
                        // 클릭 시 이동할 링크 설정
                        window.location.href = `/club/clubDetailPage?id=${clubId}`;
                    });
                    })(e.clubDto.id); // 클로저 내부에서 e.clubBoardDto.id 값을 boardId로 전달

                const img = document.createElement('img');
                img.src = `/images/${e.clubDto.club_logo}`;
                img.classList.add("img-fluid","rounded-4","image-thumbnail");
                img.style.width="70px";
                img.style.height="70px";
                imageContainer.appendChild(img);
                    
                const clubExplanation = newClubWrapperTemplate.querySelector(".clubExplanation");
                clubExplanation.innerText = e.clubDto.club_explanation;
                const memberCount = newClubWrapperTemplate.querySelector(".memberCount");
                memberCount.innerText=e.userCount;
                
                const maxMembers = newClubWrapperTemplate.querySelector(".maxMembers");
                maxMembers.innerText=e.clubDto.max_members;

                const clubLocation = newClubWrapperTemplate.querySelector(".clubLocation");
                clubLocation.innerText = e.clubDto.club_location;


                clubListBox.appendChild(newClubWrapperTemplate);
            }
        }
        
    })
}

function clubRegist(){
    window.location.href = '/club/clubRegistPage';

}

window.addEventListener("DOMContentLoaded",() => {
    setSessionUserId();
    categoryClick(0);
});
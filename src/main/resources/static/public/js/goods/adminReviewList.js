function reloadReviewList(pageNumber){

    if(!pageNumber) {
        pageNumber = 1;
    }

    renderPageNumbers(pageNumber);


    const url = "/api/goods/getReviewList?p=" + pageNumber;
    fetch(url)
    .then(response => response.json())
    .then(response => {
        const reviewListBox = document.getElementById("reviewListBox");
        reviewListBox.innerHTML = "";

        const reviewWrapperTemplate = document.querySelector("#template .reviewWrapper");

        for(let e of response.data.reviewList){
            const newReviewWrapper = reviewWrapperTemplate.cloneNode(true);

            const reviewId = newReviewWrapper.querySelector(".reviewId");
            reviewId.innerText = e.id;

            const reviewDetailLink = newReviewWrapper.querySelector(".reviewDetailLink");
            reviewDetailLink.href=`/admin/goods/adminReviewListDetail?id=${e.id}`

            const reviewDetail = newReviewWrapper.querySelector(".reviewDetail");
            reviewDetail.onclick= () => {openReviewListDetail(e.id)}; 

            const userName = newReviewWrapper.querySelector(".userName");
            userName.innerText = e.user.name;

            const userNickName = newReviewWrapper.querySelector(".userNickName");
            userNickName.innerText = e.user.nickname;

            const reviewRating = newReviewWrapper.querySelector(".reviewRating");
            reviewRating.innerText = e.rating;

            const goodsTitle = newReviewWrapper.querySelector(".goodsTitle");
            goodsTitle.innerText = e.goods.goods_title;

            const reviewContent = newReviewWrapper.querySelector(".reviewContent");
            reviewContent.innerText = e.content;

            const userPhone = newReviewWrapper.querySelector(".userPhone");
            userPhone.innerText = e.user.phone;

            const reviewCreatedAt = newReviewWrapper.querySelector(".reviewCreatedAt");
            reviewCreatedAt.innerText = e.created_at;

            const reviewDeleteButton = newReviewWrapper.querySelector(".deleteReview");
            reviewDeleteButton.addEventListener("click", function() {
                deleteReview(e.id);
            });

            reviewListBox.appendChild(newReviewWrapper);
        
        }
    })

    .catch(error => {
        console.error("Error reloading review list data", error);
    });

}

function reviewCount(){
    const url = "/api/goods/reviewCount";

    fetch(url)
    .then(response => response.json())
    .then(response => {
        document.getElementById("reviewCountDisplay").innerText = response.data.reviewCount
        reloadReviewList();
    })
    ;
}

function deleteReview(id){
    const url = `/api/goods/deleteReview?id=${id}`;

    fetch(url)
    .then(response => response.json())
    .then(response => {
        reloadReviewList();
        reviewCount();
    })
    .catch(error => console.error('Error:', error));
    ;

}


function renderPageNumbers(pageNumber) {

    if(!pageNumber) {
        pageNumber = 1;
    }

    const url = "/api/goods/getReviewStartAndEndPageNumber";

    fetch(url)
    .then(response => response.json())
    .then(response => {
        console.log(response);

        document.querySelector("#paginationWrapper").innerHTML = "";

        const newPrevPageItem = document.querySelector("#template .page-item").cloneNode(true);
        newPrevPageItem.querySelector(".page-link").innerText = "<";
        document.querySelector("#paginationWrapper").appendChild(newPrevPageItem);

        if(response.data.startPage <= 1){
            newPrevPageItem.querySelector(".page-link").classList.add("disabled");
        }else{
            newPrevPageItem.querySelector(".page-link").setAttribute("onclick", `reloadReviewList(${response.data.startPage - 1})`);
        }

        for(let i = response.data.startPage ; i <= response.data.endPage ; i++) {
            const newPageItem = document.querySelector("#template .page-item").cloneNode(true);
            newPageItem.querySelector(".page-link").innerText = i;
            newPageItem.querySelector(".page-link").setAttribute("onclick", `reloadReviewList(${i})`);
            document.querySelector("#paginationWrapper").appendChild(newPageItem);

            if(pageNumber == i) {
                newPageItem.classList.add("active");
            }


        }

        const newNextPageItem = document.querySelector("#template .page-item").cloneNode(true);
        newNextPageItem.querySelector(".page-link").innerText = ">";
        document.querySelector("#paginationWrapper").appendChild(newNextPageItem);

        if(response.data.endPage >= response.data.lastPageNumber){
            newNextPageItem.querySelector(".page-link").classList.add("disabled");
        }else{
            newNextPageItem.querySelector(".page-link").setAttribute("onclick", `reloadReviewList(${response.data.endPage + 1})`);
        }

    })
    ;

}

window.addEventListener("DOMContentLoaded", () => {
    reloadReviewList();
    reviewCount();
});


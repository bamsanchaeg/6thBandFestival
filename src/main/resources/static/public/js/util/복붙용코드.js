/* 페이지네이션 js - 권은진 (모르는 거 있으시면 저한테 오세염)
// 전역변수 세팅
let currentPage = 1; 현재 페이지로 사용 할 변수
const itemsPerPage = 15; 한 화면에 뽑고 싶은 리스트 수
const maxPageButtons = 5; 한 세트에 출력할 최대 페이지 수

// reload..List(page = 1) { (리스트 출력하는 함수) 안에 for문 끝나는 시점에 넣기
for (...) {}
setupPagination(response.data.(select count(*) from Table)로 service & mapper 만들어둬야함, itemsPerPage, page);
}

// 페이지 세팅
function setupPagination(totalItems, itemsPerPage, currentPage) {
    const pageCount = Math.ceil(totalItems / itemsPerPage);
    const backPageButton = document.getElementById("backPageButton");
    const nextPageButton = document.getElementById("nextPageButton");

    // 함수 부를 때 초기화 (컨테이너로 한 번 감싸서 innerHTML=''로 해도 됨)
    document.querySelectorAll('.pageLi').forEach(li => li.remove());

    backPageButton.classList.toggle('disabled', currentPage === 1);
    backPageButton.onclick = (e) => {
        //  preventDefault 페이지가 맨 위로 스크롤되는 현상을 방지
        // e.preventDefault();
        if (currentPage > 1) {
            // Math.max 주어진 인자 중 가장 큰 값을 반환
            reloadUserList(Math.max(1, currentPage - maxPageButtons));
        }
    };

    nextPageButton.classList.toggle('disabled', currentPage === pageCount);
    nextPageButton.onclick = (e) => {
        // e.preventDefault();
        if (currentPage < pageCount) {
            // Math.min 주어진 인자 중 가장 작은 값을 반환
            reloadUserList(Math.min(pageCount, currentPage + maxPageButtons));
        }
    };


    for (let i = 1; i <= pageCount; i++) {
        const li = document.createElement('li');
        li.classList.add('page-item', 'pageLi');
        if (i === currentPage) {
            li.classList.add('active');
        }
        const a = document.createElement('a');
        a.classList.add('page-link');
        a.classList.add('new-text-black');
        a.innerText = i;
        a.onclick = (e) => {
            // e.preventDefault();
            reloadUserList(i);
        };
        li.appendChild(a);
        nextPageButton.parentElement.before(li);
    }
}
-------------------------------------------------------------------
html.. <div class="main py-4 px-4"> 안에, 제일 밑에 넣기
    <div class="row mt-4">
        <div class="col">
            <nav aria-label="Page navigation">
                <ul class="pagination justify-content-center">
                    <li class="page-item">
                        <a class="page-link new-text-black rounded-0" id="backPageButton" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>

                    <li class="page-item">
                        <a class="page-link new-text-black rounded-0" id="nextPageButton" aria-label="Next">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                </ul>
            </nav>
        </div>
    </div>
*/

/* 날짜 포맷 24.07.26 형식으로 반환 - 권은진 (모르는 거 있으시면 저한테 오세염)
function dateFormat(formatDay) {
    const myDate = new Date(formatDay);

    let day = myDate.getDate();
    let month = myDate.getMonth() + 1;
    // .toString().slice(-2) 연도의 마지막 2자리뽑기
    let year = myDate.getFullYear().toString().slice(-2);

    // 두 자릿수로 맞추기 위해 day와 month가 10보다 작을 경우 앞에 0을 추가
    day = day < 10 ? '0' + day : day;
    month = month < 10 ? '0' + month : month;

    return `${year}.${month}.${day}`;
}
*/
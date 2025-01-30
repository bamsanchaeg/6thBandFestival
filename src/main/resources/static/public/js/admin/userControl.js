// 날짜 포맷
function dateFormat(formatDay) {
    let myDate = new Date(formatDay);

    let day = myDate.getDate();
    let month = myDate.getMonth() + 1;
    // 연도의 마지막 2자리
    let year = myDate.getFullYear().toString().slice(-2);

    // 두 자릿수로 맞추기 위해 day와 month가 10보다 작을 경우 앞에 0을 추가
    day = day < 10 ? '0' + day : day;
    month = month < 10 ? '0' + month : month;

    return `${year}.${month}.${day}`;
}

let currentPage = 1;
const itemsPerPage = 15;
const maxPageButtons = 5;

// 유저 리스트
function reloadUserList(page = 1) {
    currentPage = page;
    const url = `/api/admin/userList?page=${page}`
    fetch(url)
        .then(response => response.json())
        .then(response => {
            const userListBox = document.getElementById("userListBox");
            userListBox.innerHTML = "";

            const userListWrapperTemplate = document.querySelector("#userListTemplate .userListWrapper");

            for (const e of response.data.userList) {
                const userListWrapper = userListWrapperTemplate.cloneNode(true);

                const userIdSpan = userListWrapper.querySelector(".userIdSpan");
                userIdSpan.innerText = e.id;

                const userAccountNameSpan = userListWrapper.querySelector(".userAccountNameSpan");
                userAccountNameSpan.innerText = e.account_name;

                const userNickNameSpan = userListWrapper.querySelector(".userNickNameSpan");
                userNickNameSpan.innerText = e.nickname;

                const userBirthSpan = userListWrapper.querySelector(".userBirthSpan");
                userBirthSpan.innerText = dateFormat(e.birth);

                const userEmailSpan = userListWrapper.querySelector(".userEmailSpan");
                userEmailSpan.innerText = e.email;

                const userPhoneNumberSpan = userListWrapper.querySelector(".userPhoneNumberSpan");
                userPhoneNumberSpan.innerText = e.phone.substring(0, 3) + '-' + e.phone.substring(3, 7) + '-' + e.phone.substring(7);;

                const userCreatedAtSpan = userListWrapper.querySelector(".userCreatedAtSpan");
                userCreatedAtSpan.innerText = dateFormat(e.created_at);

                const userIsActiveSpan = userListWrapper.querySelector(".userIsActiveSpan");
                if (e.is_active === 'N') {
                    userIsActiveSpan.classList.add("badge");
                    userIsActiveSpan.classList.add("text-bg-danger");
                    userIsActiveSpan.classList.add("fw-normal");
                    userIsActiveSpan.innerText = "활동 정지"
                }

                const userDisabledSpan = userListWrapper.querySelector(".userDisabledSpan");
                if (e.is_active === 'Y') {
                    userDisabledSpan.innerText = 'toggle_on';
                    userDisabledSpan.classList.remove('text-danger');
                    userDisabledSpan.classList.add('text-success');
                    userDisabledSpan.onclick = () => {disableUser(e.id)};
                } else {
                    userDisabledSpan.innerText = 'toggle_off';
                    userDisabledSpan.classList.add('fill-1');
                    userDisabledSpan.onclick = ()=> {ableUser(e.id)};
                }

                userListBox.appendChild(userListWrapper);
            }
            // 페이지 셋팅을 위한 파라미터 넘겨주기 (총 개수, 뽑고싶은 개수, 현재 페이지)
            setupPagination(response.data.userCount, itemsPerPage, page);

    });
}

// 페이지 셋팅 ..
function setupPagination(totalItems, itemsPerPage, currentPage) {
    const pageCount = Math.ceil(totalItems / itemsPerPage);
    const backPageButton = document.getElementById("backPageButton");
    const nextPageButton = document.getElementById("nextPageButton");

    // 로드 될 때 초기화 (컨테이너로 한 번 감싸서 innerHTML='' 초기화 해도 됨)
    document.querySelectorAll('.pageLi').forEach(li => li.remove());

    backPageButton.classList.toggle('disabled', currentPage === 1);
    backPageButton.onclick = (e) => {
        //  preventDefault 페이지가 맨 위로 스크롤되는 현상을 방지 (스크롤을 길게 뽑아놨을 경우 유용할 듯?)
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

// 유저 비활성화
function disableUser(id) {
    if(!confirm("해당 회원을 비활성화하시겠습니까?")) {
        return;
    }
    const url = `/api/admin/disableUserById?id=${id}`
    fetch(url)
        .then(response => response.json())
        .then(response => {
            // console.log('ddddddddd22');
            showMessage('계정 비활성화가 완료되었습니다.');
        });
}

// 유저 활성화
function ableUser(id) {
    if(!confirm("해당 회원을 활성화하시겠습니까?")) {
        return;
    }
    const url = `/api/admin/ableUserById?id=${id}`
    fetch(url)
        .then(response => response.json())
        .then(response => {
            // console.log('ddddddddd1');
            showMessage('계정 활성화가 완료되었습니다.');
        });
}

// 알림창
function showMessage(text) {
    // console.log('dfasdfasdfaef');
    const reviewUpdateDoneModal = bootstrap.Modal.getOrCreateInstance("#reviewUpdateDoneModal");
    console.log("모달 " +reviewUpdateDoneModal );
    reviewUpdateDoneModal.show();

    const testAlert = document.getElementById("testAlert")
    // console.log("텍스ㅡㅌ : " + testAlert);
    testAlert.innerText = text;

    setTimeout(() => {
        reviewUpdateDoneModal.hide();
        reloadUserList(1);
    }, 1000);

}

window.addEventListener("DOMContentLoaded", () => {
    reloadUserList(currentPage);
});
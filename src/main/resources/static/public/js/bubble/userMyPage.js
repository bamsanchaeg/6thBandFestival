function setSessionId() {
    const url = "/api/bubble/getSessionUserId";
    //필수
    fetch(url)
        .then(response => response.json())//then은 콜백, 응답한 시점
        .then(response => {
            const e = response.data
            if (response && e[0]) {

            } else {
            }
        })
}
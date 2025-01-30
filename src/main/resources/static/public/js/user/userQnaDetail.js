const urlParams = new URL(window.location.href).searchParams;
const qnaId = urlParams.get("id");

function reloadDetailPage() {
    const url = `/api/service/getQnADetail?id=${qnaId}`;

    fetch(url)
        .then(response => response.json())
        .then((response) => {
            const qnaContentSpan = document.getElementById('qnaContentSpan');
            qnaContentSpan.innerText = response.data.qna.content;

            const qnaStatusSpan = document.getElementById('qnaStatusSpan');
            if (response.data.qna.status === '답변 완료') {
                qnaStatusSpan.classList.add('text-success');
            }

            const qnaAnswerContentSpan = document.getElementById('qnaAnswerContentSpan');
            qnaAnswerContentSpan.innerText = response.data.qnaAnswer.content;
        });
}

window.addEventListener("DOMContentLoaded", () => {
    reloadDetailPage();
});
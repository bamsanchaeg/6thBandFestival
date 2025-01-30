async function voteGenderSelect(radioOptionId,clubId,voteId){
    const url = `/api/vote/voteStatistics?club_id=${clubId}&vote_id=${voteId}&option_id=${radioOptionId}`;
    const response = await fetch(url);
    const data = await response.json();
    const femaleVote = data.data.femaleVote;
    const maleVote = data.data.maleVote;
    console.log("이거확인!!"+maleVote);

    const genderStatisticsBox = document.getElementById("genderStatisticsBox");
    // genderStatisticsBox.innerHTML = "";
    const allGraps = document.querySelectorAll(".genderStatisticsWrapper");
    allGraps.forEach((graph) => graph.style.display = "none");
    let targetGraph = document.getElementById(`genderStatisticsWrapper-${radioOptionId}`);
    if (targetGraph) {
        targetGraph.remove();
    }

    const genderStatisticsWrapperTemplate = document.querySelector("#genderStatisticsTemplate .genderStatisticsWrapper");
    targetGraph  = genderStatisticsWrapperTemplate.cloneNode(true);
    targetGraph.id=`genderStatisticsWrapper-${radioOptionId}`;
    genderStatisticsBox.appendChild(targetGraph);

    targetGraph.style.display = "block";

    const chartData = {
        labels: [
            '여성',
            '남성'
        ],
        datasets: [{
            label: '투표 수',
            data: [femaleVote, maleVote],
            backgroundColor: [
                'rgb(255,160,160)',
                'rgb(174,196,255)'
            ],
            hoverOffset: 4
        }]
    };
    const genderStatistics = targetGraph.querySelector(".genderStatistics").getContext('2d');
    new Chart(genderStatistics, {
        type: 'pie', // 도넛 차트
        data: chartData,
        options: {
            responsive: true,
            animation: {
                duration: 1000, // 애니메이션 지속 시간 (밀리초)
                easing: 'linear', // 애니메이션 이징 함수
                animateRotate: true
            },
            plugins: {
                legend: {
                    position: 'top',
                },
                title: {
                    display: true,
                    text: `선택지${radioOptionId}번 - 성별 선택 비율`
                }
            }
        },
    });


}
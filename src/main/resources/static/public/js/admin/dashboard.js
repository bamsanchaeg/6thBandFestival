/************** 통계 ************/

// labels 추출
function getLabels(data, feild){
    return data.map(stat => stat[feild]);
}

// data 추출
function getData(data, feild) {
    return data.map(stat => stat[feild]);
}

// chart config
function getChart(id, type, data, options) {
    const ctx = document.getElementById(id);

    new Chart(ctx, {
        type: type,
        data: data,
        options: options
    });
}

// chart box templete
function getChartBoxTemplete(id) {
    // 템플릿 복사
    const chartBox = document.querySelector("#template .chart-wrapper .chart-box");
    const newChartBox = chartBox.cloneNode(true);

    const chartCanvas = newChartBox.querySelector(".chart-canvas");
    chartCanvas.id = id; // chart 의 id 값 필수

    return newChartBox;
}

// 통계 데이터 주입 : 취합본
function getStatisticsByProject() {
    const url = `/api/admin/weeklySales`;
    fetch(url)
    .then(response => response.json())
    .then((response) => {

        // 주간 매출 현황
        const weeklyChartBox = document.querySelector(".chart-weekly-sales .chart-box");
        weeklyChartBox.style.minHeight = "25rem";

        const weeklySales = response.data.weeklySales;
        const salesDate = {
            labels: getLabels(weeklySales, "date"),
            datasets: [
                {
                    label: '굿즈 매출액',
                    data: getData(weeklySales, "order_price"),
                    borderColor: 'rgba(153, 102, 255, 0.8)',
                    backgroundColor: 'rgba(153, 102, 255, 0.5)',
                    borderWidth: 1
                },{
                    label: '대여 매출액',
                    data: getData(weeklySales, "rental_price"),
                    borderColor: 'rgba(54, 162, 235, 0.8)',
                    backgroundColor: 'rgba(54, 162, 235, 0.5)',
                    borderWidth: 1
                },{
                    label: '티켓 매출액',
                    data: getData(weeklySales, "ticket_price"),
                    borderColor: 'rgba(75, 192, 192, 0.8)',
                    backgroundColor: 'rgba(75, 192, 192, 0.5)',
                    borderWidth: 1
                },{
                    label: '펀딩 수수료',
                    data: getData(weeklySales, "funding_fee"),
                    borderColor: 'rgba(255, 159, 64, 0.8)',
                    backgroundColor: 'rgba(255, 159, 64, 0.5)',
                    borderWidth: 1
                },{
                    label: '전체 매출액',
                    data: getData(weeklySales, "total_price"),
                    borderColor: 'rgba(255, 109, 150, 0.8)',
                    backgroundColor: 'rgba(255, 109, 150, 0.1)',
                    borderWidth: 1,
                    type: 'bar'
                }
            ]
        }
        const salesOptions = {
            maintainAspectRatio: false, // 리사이징 오류? 방지
            responsive: true,
            plugins: {
                legend: {
                    position: 'top',
                }    
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        precision: 0
                    }
                }
            },
        }
        getChart("chartWeeklySales", "line", salesDate, salesOptions);

        // 플랫폼 접속 비율 : 가라 데이터...
        const visitChartBox = document.querySelector(".chart-visit-platform .chart-box");
        visitChartBox.classList.add("p-3", "mx-auto");
        visitChartBox.style.maxWidth = "80%";

        const genderData = {
            labels: ['iOS', 'AOS', 'WEB'],
            datasets: [{
                label: '플랫폼 접속 비율',
                data: ['20', '28', '52'],
                backgroundColor: [
                    // 'rgba(255, 159, 64, 0.65)',
                    'rgba(255, 99, 132, .65)',
                    'rgba(54, 162, 235, .65)',
                    'rgba(75, 192, 192, .65)',
                ],
                borderWidth: 1
            }]
        }
        const genderOptions = {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top',
                },
            },
        }
        getChart("chartVisitPlatform", "doughnut", genderData, genderOptions);    

    });
}

// 통계 출력
function showStatistics() {
    // 차트 박스 : 주간 매출 현황
    const chartDailyAmountBox = document.querySelector(".chart-weekly-sales");
    chartDailyAmountBox.appendChild(getChartBoxTemplete("chartWeeklySales"));

    // 플랫폼 비율
    const chartVisitPlatformBox = document.querySelector(".chart-visit-platform");
    chartVisitPlatformBox.appendChild(getChartBoxTemplete("chartVisitPlatform"));

    // 차트 호출
    getStatisticsByProject();
}

window.addEventListener('DOMContentLoaded', function() {
    // 대시보드 : 통계 출력
    showStatistics();
});
// 일 매출 그래프
function dailySales() {
    const url = '/api/admin/ticket/getDailySales';
    fetch(url)
        .then(response => response.json())
        .then(response => {
            // 배열의 각 요소에 대해 주어진 함수를 호출하여 결과를 모아 새로운 배열을 만들기
            const labels = response.data.dailySales.map(item => item.date);
            const totalSales = response.data.dailySales.map(item => item.total_price);

            const ctx = document.getElementById('dailySalesCanvas').getContext('2d');
            const dailySalesCanvas = new Chart(ctx, {
                type: 'line', // 차트 종류
                data: {
                    labels: labels,
                    datasets: [{
                        label: '일 매출',
                        data: totalSales,
                        fill: false,
                        borderColor: 'rgb(255,109,150)',
                        tension: 0.1
                    }]
                },
                options: {
                    scales: {
                        x:  {
                            type: 'category',
                            title: {
                                display: true,
                                text: 'Day'
                            }
                        },
                    }
                }
            });
        })
}

// 월 매출 그래프
function monthlySales() {
    const url = '/api/admin/ticket/getMonthlySales';
    fetch(url)
        .then(response => response.json())
        .then(response => {
            // 배열의 각 요소에 대해 주어진 함수를 호출하여 결과를 모아 새로운 배열을 만들기
            const labels = response.data.monthlySales.map(item => item.month);
            const totalSales = response.data.monthlySales.map(item => item.total_price);

            const ctx = document.getElementById('monthlySalesCanvas').getContext('2d');
            const monthlySalesCanvas = new Chart(ctx, {
                type: 'line', // 차트 종류
                data: {
                    labels: labels,
                    datasets: [{
                        label: '월 매출',
                        data: totalSales,
                        fill: false,
                        borderColor: 'rgb(255,109,150)',
                        tension: 0.1
                    }]
                },
                options: {
                    scales: {
                        x:  {
                            type: 'category',
                            title: {
                                display: true,
                                text: 'Month'
                            }
                        },
                    }
                }
            });
        })
}

// 연 매출 그래프
function annualSales() {
    const url = '/api/admin/ticket/getAnnualSales';
    fetch(url)
        .then(response => response.json())
        .then(response => {
            // 배열의 각 요소에 대해 주어진 함수를 호출하여 결과를 모아 새로운 배열을 만들기
            const labels = response.data.annualSales.map(item => item.year);
            const totalSales = response.data.annualSales.map(item => item.total_price);

            const ctx = document.getElementById('annualSalesCanvas').getContext('2d');
            const annualSalesCanvas = new Chart(ctx, {
                type: 'line', // 차트 종류
                data: {
                    labels: labels,
                    datasets: [{
                        label: '연 매출',
                        data: totalSales,
                        fill: false,
                        borderColor: 'rgb(255,109,150)',
                        // tension: 0.1,
                        // showLine: totalSales.length > 1, // 데이터 포인트가 하나일 경우 선을 숨기고 점만 표시
                        // pointBackgroundColor: 'rgb(255,109,150)',
                        // pointBorderColor: 'rgb(255,109,150)',
                        // pointRadius: 5, // 점의 크기를 설정
                        // pointHoverRadius: 7 // 점을 호버할 때 크기 설정
                    }]
                },
                options: {
                    spanGaps: true, // 데이터 간의 간격을 무시하고 점을 표시
                    scales: {
                        x:  {
                            type: 'category',
                            title: {
                                display: true,
                                text: 'Year'
                            }
                        },
                    }
                }
            });
        })
}

// 페스티벌 목록 select 만들기
function loadFestival() {
    const festivalInfoUrl = '/api/admin/ticket/getFestivalAllList';
    fetch(festivalInfoUrl)
        .then(response => response.json())
        .then((response) => {
            const festivalIdSelect = document.getElementById("festivalIdSelect");
            const festivalListTemplate = document.querySelector("#festivalListTemplate .festivalListWrapper");
            const defaultInfo = festivalIdSelect.querySelector(".defaultInfo");

            festivalIdSelect.innerHTML = "";

            if (defaultInfo) {
                festivalIdSelect.appendChild(defaultInfo);
            }

            for (const e of response.data.festivalList) {
                const festivalListWrapper = festivalListTemplate.cloneNode(true); // true 안쪽까지 다 복사

                const festivalList = festivalListWrapper.querySelector(".festivalList");
                festivalList.value = e.id;
                festivalList.text = e.festival_name;

                festivalIdSelect.appendChild(festivalList);
            }
            festivalIdSelect.addEventListener('change', isFestivalChange);
        });
}

// 페스티벌을 변경하면
function isFestivalChange() {
    const festivalIdSelect = document.getElementById("festivalIdSelect");
    const festivalId = festivalIdSelect.value;
    // console.log("페스티벌 아이디" + festivalId);
    dayTypeRatio(festivalId);
    oneDayTicketsAgeSales(festivalId);
    allDayTicketsAgeSales(festivalId);
    // oneDayTicketsGenderSales(festivalId);

}

// 1일권 3일권 판매 비율
function dayTypeRatio(festivalId) {
    const url = `/api/admin/ticket/getDayTypeRatio?id=${festivalId}`

    fetch(url)
        .then(response => response.json())
        .then((response) => {
            const dayTypeRatioBox = document.getElementById("dayTypeRatioBox");
            dayTypeRatioBox.innerHTML = "";
            dayTypeRatioBox.classList.add("py-3");
            dayTypeRatioBox.classList.add("shadow-sm");
            dayTypeRatioBox.classList.add("border");

            const oneDayTickets = response.data.oneDay;
            const allDayTickets = response.data.allDay;

            const ratioWrapperTemplate = document.querySelector("#ratioTemplate .ratioWrapper");
            const ratioWrapper = ratioWrapperTemplate.cloneNode(true);

            const data = {
                labels: [
                    '1일권',
                    '3일권'
                ],
                datasets: [{
                    label: '티켓 판매수',
                    data: [oneDayTickets, allDayTickets],
                    backgroundColor: [
                        'rgb(255,144,177)',
                        'rgb(255,183,183)'
                    ],
                    hoverOffset: 4
                }]
            };

            const dayTypeRatio = ratioWrapper.querySelector('.dayTypeRatio').getContext('2d');
            const dayTypeRatioChart = new Chart(dayTypeRatio, {
                type: 'doughnut', // 도넛 차트
                data: data,
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            position: 'top',
                        },
                        title: {
                            display: true,
                            text: '티켓 판매 비율'
                        }
                    }
                },
            });

            dayTypeRatioBox.appendChild(ratioWrapper);
        })
}

// 1일권 나이대
function oneDayTicketsAgeSales(festivalId) {
    const url = `/api/admin/ticket/getOneDaySales?id=${festivalId}`

    fetch(url)
        .then(response => response.json())
        .then((response) => {
            const oneDayAgeInfoBox = document.getElementById("oneDayAgeInfoBox");
            oneDayAgeInfoBox.innerHTML = "";
            oneDayAgeInfoBox.classList.add("pt-3");
            // oneDayAgeInfoBox.classList.add("me-3");
            oneDayAgeInfoBox.classList.add("shadow-sm");
            oneDayAgeInfoBox.classList.add("border");

            const ageGroups = response.data.age.map(item => {
                if (item.age === 0) return '10세 미만';
                if (item.age === 10) return '10대';
                if (item.age === 20) return '20대';
                if (item.age === 30) return '30대';
                if (item.age === 40) return '40대';
                if (item.age === 50) return '50대';
                if (item.age === 60) return '60대';
                if (item.age === 70) return '70대';
                if (item.age === 80) return '80대';
            });
            const ageCount = response.data.age.map(item => item.age_count);
            const female = response.data.female;
            const male = response.data.male;

            const oneDayWrapperTemplate = document.querySelector("#oneDayTemplate .oneDayWrapper");
            const oneDayWrapper = oneDayWrapperTemplate.cloneNode(true);

            const oneDayAgeInfo = oneDayWrapper.querySelector('.oneDayAgeInfo').getContext('2d');
            const dayTypeRatioChart = new Chart(oneDayAgeInfo, {
                type: 'bar', // Bar chart
                data: {
                    labels: ageGroups,
                    datasets: [{
                        label: '티켓 판매 수',
                        data: ageCount,
                        backgroundColor: 'rgba(255,91,122,0.59)',
                        borderColor: 'rgba(255,64,100,0.71)',
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    },
                    responsive: true,
                    plugins: {
                        legend: {
                            position: 'top',
                        },
                        title: {
                            display: true,
                            text: '1일권 - 나이대 판매 비율'
                        }
                    }
                }
            });
            oneDayAgeInfoBox.appendChild(oneDayWrapper);
            oneDayTicketsGenderSales(female, male);
        })
}

// 1일권 성별
function oneDayTicketsGenderSales(female, male) {
    const oneDayGenderInfoBox = document.getElementById("oneDayGenderInfoBox");
    oneDayGenderInfoBox.innerHTML = "";
    oneDayGenderInfoBox.classList.add("pt-1")
    oneDayGenderInfoBox.classList.add("shadow-sm")
    oneDayGenderInfoBox.classList.add("border")


    const oneDayGenderTemplateWrapper = document.querySelector("#oneDayGenderTemplate .oneDayGenderWrapper");
    const oneDayGenderWrapper = oneDayGenderTemplateWrapper.cloneNode(true);

    const data = {
        labels: [
            '여성',
            '남성'
        ],
        datasets: [{
            label: '티켓 판매수',
            data: [female, male],
            backgroundColor: [
                'rgb(255,160,160)',
                'rgb(174,196,255)'
            ],
            hoverOffset: 4
        }]
    };

    const oneDayGenderRatio = oneDayGenderWrapper.querySelector('.oneDayGenderRatio').getContext('2d');
    const dayTypeRatioChart = new Chart(oneDayGenderRatio, {
        type: 'pie', // 도넛 차트
        data: data,
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top',
                },
                title: {
                    display: true,
                    text: '1일권 - 성별 판매 비율'
                }
            }
        },
    });

    oneDayGenderInfoBox.appendChild(oneDayGenderWrapper);
}

// 나이대
function allDayTicketsAgeSales(festivalId) {
    const url = `/api/admin/ticket/getAllDaySales?id=${festivalId}`

    fetch(url)
        .then(response => response.json())
        .then((response) => {
            const allDayAgeInfoBox = document.getElementById("allDayAgeInfoBox");
            allDayAgeInfoBox.innerHTML = "";
            allDayAgeInfoBox.classList.add("pt-3");
            // allDayAgeInfoBox.classList.add("me-3");
            allDayAgeInfoBox.classList.add("shadow-sm");
            allDayAgeInfoBox.classList.add("border");

            const ageGroups = response.data.age.map(item => {
                if (item.age === 0) return '10세 미만';
                if (item.age === 10) return '10대';
                if (item.age === 20) return '20대';
                if (item.age === 30) return '30대';
                if (item.age === 40) return '40대';
                if (item.age === 50) return '50대';
                if (item.age === 60) return '60대';
                if (item.age === 70) return '70대';
                if (item.age === 80) return '80대';
            });
            const ageCount = response.data.age.map(item => item.age_count);
            const female = response.data.female;
            const male = response.data.male;

            const allDayWrapperTemplate = document.querySelector("#allDayTemplate .allDayWrapper");
            const allDayWrapper = allDayWrapperTemplate.cloneNode(true);

            const allDayAgeInfo = allDayWrapper.querySelector('.allDayAgeInfo').getContext('2d');
            const dayTypeRatioChart = new Chart(allDayAgeInfo, {
                type: 'bar', // Bar chart
                data: {
                    labels: ageGroups,
                    datasets: [{
                        label: '티켓 판매 수',
                        data: ageCount,
                        backgroundColor: 'rgba(255,91,122,0.59)',
                        borderColor: 'rgba(255,64,100,0.71)',
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    },
                    responsive: true,
                    plugins: {
                        legend: {
                            position: 'top',
                        },
                        title: {
                            display: true,
                            text: '3일권 - 나이대 판매 비율'
                        }
                    }
                }
            });
            allDayAgeInfoBox.appendChild(allDayWrapper);
            allDayTicketsGenderSales(female, male);
        })
}

// 성별
function allDayTicketsGenderSales(female, male) {
    const allDayGenderInfoBox = document.getElementById("allDayGenderInfoBox");
    allDayGenderInfoBox.innerHTML = "";
    allDayGenderInfoBox.classList.add("pt-1")
    allDayGenderInfoBox.classList.add("shadow-sm")
    allDayGenderInfoBox.classList.add("border")

    const allDayGenderTemplateWrapper = document.querySelector("#allDayGenderTemplate .allDayGenderWrapper");
    const allDayGenderWrapper = allDayGenderTemplateWrapper.cloneNode(true);

    const data = {
        labels: [
            '여성',
            '남성'
        ],
        datasets: [{
            label: '티켓 판매수',
            data: [female, male],
            backgroundColor: [
                'rgb(255,160,160)',
                'rgb(174,196,255)'
            ],
            hoverOffset: 4
        }]
    };

    const allDayGenderRatio = allDayGenderWrapper.querySelector('.allDayGenderRatio').getContext('2d');
    const dayTypeRatioChart = new Chart(allDayGenderRatio, {
        type: 'pie',
        data: data,
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top',
                },
                title: {
                    display: true,
                    text: '3일권 - 나이대 판매 비율'
                }
            }
        },
    });

    allDayGenderInfoBox.appendChild(allDayGenderWrapper);
}

window.addEventListener("DOMContentLoaded", () => {
    loadFestival()
    dailySales();
    monthlySales();
    annualSales();
});
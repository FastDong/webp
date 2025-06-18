// main.js 파일의 전체 내용

/**
 * =================================
 * 1. 탭(Tab) 기능 관련 로직
 * =================================
 */
document.addEventListener('DOMContentLoaded', () => {
    const tabs = ['board', 'stock', 'game'];

    // 탭을 활성화하고 스타일을 변경하는 함수
    function activateTab(tabId) {
        tabs.forEach(t => {
            const content = document.getElementById(`tab-${t}`);
            const btn = document.getElementById(`tab-${t}-btn`);

            if (content && btn) {
                content.classList.add('hidden');
                btn.classList.remove('bg-white', 'text-main-bg', 'border-b-4');
                btn.classList.add('bg-gray-700', 'text-white');
            }
        });

        const activeContent = document.getElementById(`tab-${tabId}`);
        const activeBtn = document.getElementById(`tab-${tabId}-btn`);

        if (activeContent && activeBtn) {
            activeContent.classList.remove('hidden');
            activeBtn.classList.remove('bg-gray-700', 'text-white');
            activeBtn.classList.add('bg-white', 'text-main-bg', 'border-b-4');
        }
    }

    // 페이지 로드 시 마지막으로 선택한 탭을 활성화
    const savedTab = localStorage.getItem('selectedTab');
    if (savedTab && tabs.includes(savedTab)) {
        activateTab(savedTab);
    } else {
        activateTab('board'); // 기본값은 '게시판'
    }

    // 각 탭 버튼에 클릭 이벤트 추가
    tabs.forEach(tabId => {
        const btn = document.getElementById(`tab-${tabId}-btn`);
        if(btn) {
            btn.addEventListener('click', () => {
                activateTab(tabId);
                localStorage.setItem('selectedTab', tabId);
            });
        }
    });

    /**
     * =================================
     * 2. 실시간 주식 및 자산 업데이트 관련 로직
     * =================================
     */
    setInterval(updateStockPrices, 3000); // 3초마다 업데이트

    // 게임 메뉴 이벤트도 DOM 로드 후 바로 연결
    setGameMenuEvents();
});


// 숫자를 원화 형식의 문자열로 변환하는 헬퍼 함수
function formatKoreanWon(number) {
    if (typeof number !== 'number') return '0원';
    return new Intl.NumberFormat('ko-KR').format(number) + '원';
}

// 실시간 주식 정보 업데이트 함수 (통합 버전)
async function updateStockPrices() {
    try {
        const response = await fetch('/api/stocks');
        if (!response.ok) return;
        const latestStockList = await response.json();

        const priceMap = new Map(latestStockList.map(stock => [stock.stockName, stock]));

        // '주식' 탭 카드 내용 업데이트 (탭이 보일 때만)
        const stockTabContent = document.getElementById('tab-stock');
        if (!stockTabContent.classList.contains('hidden')) {
            latestStockList.forEach(stock => {
                const priceElement = document.querySelector(`#stock-card-${stock.stockId} .stock-price`);
                const changeElement = document.querySelector(`#stock-card-${stock.stockId} .stock-change`);
                if (priceElement && changeElement) {
                    priceElement.textContent = stock.price.toLocaleString('ko-KR') + '원';
                    const amount = stock.priceChangeAmount;
                    const rate = stock.priceChangeRate.toFixed(2);
                    changeElement.innerHTML = `<span>${amount >= 0 ? '▲' : '▼'}</span> <span>${Math.abs(amount).toLocaleString('ko-KR')}</span> <span class="block">(${rate}%)</span>`;
                    changeElement.className = 'ml-4 w-28 text-sm stock-change';
                    if (amount > 0) changeElement.classList.add('text-red-500');
                    else if (amount < 0) changeElement.classList.add('text-blue-500');
                    else changeElement.classList.add('text-gray-400');
                }
            });
        }

        // '내정보' 패널 총 자산 현황 업데이트 (항상 실행)
        updatePortfolioSummary(priceMap);

    } catch (error) {
        console.error('주식 정보를 업데이트하는 중 오류 발생:', error);
    }
}

// 총 자산 현황 업데이트 함수
function updatePortfolioSummary(priceMap) {
    let newTotalValue = 0;
    let totalPurchasePrice = 0;
    const ownedStockItems = document.querySelectorAll('.owned-stock-item');
    if (ownedStockItems.length === 0) return;

    ownedStockItems.forEach(item => {
        const stockName = item.dataset.stockName;
        const quantity = parseInt(item.dataset.quantity, 10);
        const purchasePrice = parseInt(item.dataset.purchasePrice, 10);
        const currentStockData = priceMap.get(stockName);
        if (currentStockData) newTotalValue += currentStockData.price * quantity;
        else newTotalValue += purchasePrice;
        totalPurchasePrice += purchasePrice;
    });

    const totalProfitLoss = newTotalValue - totalPurchasePrice;
    const totalProfitLossRate = totalPurchasePrice === 0 ? 0 : (totalProfitLoss / totalPurchasePrice) * 100;

    const totalValueDisplay = document.getElementById('total-value-display');
    const totalPnlAmountDisplay = document.getElementById('total-pnl-amount-display');
    const totalPnlRateDisplay = document.getElementById('total-pnl-rate-display');
    if (!totalValueDisplay) return;

    totalValueDisplay.textContent = formatKoreanWon(newTotalValue);
    totalPnlAmountDisplay.textContent = `${totalProfitLoss > 0 ? '+' : (totalProfitLoss < 0 ? '-' : '')}${formatKoreanWon(Math.abs(totalProfitLoss))}`;
    totalPnlRateDisplay.textContent = `(${totalProfitLossRate.toFixed(2)}%)`;

    const elementsToColor = [totalValueDisplay, totalPnlAmountDisplay, totalPnlRateDisplay];
    elementsToColor.forEach(el => {
        el.classList.remove('text-red-600', 'text-blue-600', 'text-gray-800', 'text-gray-500');
        if (totalProfitLoss > 0) el.classList.add('text-red-600');
        else if (totalProfitLoss < 0) el.classList.add('text-blue-600');
        else el.classList.add(el.id === 'total-value-display' ? 'text-gray-800' : 'text-gray-500');
    });
}

/**
 * =================================
 * 3. 게임 기능 관련 로직
 * =================================
 */
function setGameMenuEvents() {
    const evenOddBtn = document.getElementById('btn-even-odd');
    const updownBtn = document.getElementById('btn-updown');
    if (evenOddBtn) evenOddBtn.onclick = renderEvenOddGame;
    if (updownBtn) updownBtn.onclick = renderUpDownGame;
}

// 홀짝 게임 UI 및 로직
function renderEvenOddGame() {
    document.getElementById('game-content').innerHTML = `
	    <button id="btn-back" class="mb-2 text-blue-400 underline text-xs">← 뒤로</button>
	    <div class="mb-2 text-base font-semibold">홀짝게임</div>
	    <div class="mb-2 text-sm">도전 포인트: <span id="eo-point">10</span></div>
	    <input type="number" id="eo-input" class="border p-1 rounded w-20 text-sm" placeholder="숫자">
	    <div class="flex gap-1 mt-2">
	      <button id="even-btn" class="bg-blue-500 text-white px-2 py-1 rounded text-xs">홀</button>
	      <button id="odd-btn" class="bg-blue-500 text-white px-2 py-1 rounded text-xs">짝</button>
	    </div>
	    <div id="eo-result" class="mt-2 text-sm"></div>
	    <div id="eo-action" class="mt-2"></div>
	  `;
    let streak = 0;
    let point = 10;
    let gameActive = true;

    document.getElementById('btn-back').onclick = renderGameMenu;

    function play(choice) {
        if (!gameActive) return;
        const num = parseInt(document.getElementById('eo-input').value, 10);
        if (isNaN(num)) {
            document.getElementById('eo-result').innerText = '숫자를 입력하세요.';
            return;
        }
        const isEven = num % 2 === 0;
        const win = (choice === '홀' && !isEven) || (choice === '짝' && isEven);
        if (win) {
            streak += 1;
            document.getElementById('eo-result').innerText = `정답! 연승: ${streak}, 포인트: ${point}`;
            document.getElementById('eo-action').innerHTML = `
	        <button id="eo-stop" class="bg-gray-200 text-gray-700 px-2 py-1 rounded text-xs">멈추기</button>
	        <button id="eo-double" class="bg-yellow-400 text-white px-2 py-1 rounded text-xs ml-1">2배 도전</button>
	      `;
            gameActive = false;
            document.getElementById('eo-stop').onclick = function() {
                updateGamePoint(point);
            };
            document.getElementById('eo-double').onclick = function() {
                point *= 2;
                document.getElementById('eo-point').innerText = point;
                document.getElementById('eo-result').innerText = '';
                document.getElementById('eo-action').innerHTML = '';
                gameActive = true;
            };
        } else {
            document.getElementById('eo-result').innerText = '실패! 포인트 획득 실패';
            document.getElementById('eo-action').innerHTML = '';
            streak = 0;
            point = 10;
        }
    }

    document.getElementById('even-btn').onclick = () => play('홀');
    document.getElementById('odd-btn').onclick = () => play('짝');
}

// 업다운 게임 UI 및 로직
function renderUpDownGame() {
    document.getElementById('game-content').innerHTML = `
	    <button id="btn-back" class="mb-2 text-green-400 underline text-xs">← 뒤로</button>
	    <div class="mb-2 text-base font-semibold">UPDOWN 게임</div>
	    <div class="mb-2 text-sm">남은 기회: <span id="ud-chance">12</span> / 포인트: <span id="ud-point">4096</span></div>
	    <input type="number" id="ud-input" class="border p-1 rounded w-20 text-sm" placeholder="1~100">
	    <button id="ud-guess" class="bg-green-500 text-white px-2 py-1 rounded text-xs ml-1">확인</button>
	    <div id="ud-result" class="mt-2 text-sm"></div>
	  `;
    let answer = Math.floor(Math.random() * 100) + 1;
    let chance = 12;
    let point = 4096;
    let finished = false;

    document.getElementById('btn-back').onclick = renderGameMenu;

    document.getElementById('ud-guess').onclick = function() {
        if (finished) return;
        const guess = parseInt(document.getElementById('ud-input').value, 10);
        if (isNaN(guess) || guess < 1 || guess > 100) {
            document.getElementById('ud-result').innerText = '1~100 사이의 숫자 입력!';
            return;
        }
        if (guess === answer) {
            document.getElementById('ud-result').innerText = `정답! ${point}포인트 획득!`;
            updateGamePoint(point);
            finished = true;
        } else {
            chance -= 1;
            point = Math.max(1, Math.floor(point / 2));
            document.getElementById('ud-chance').innerText = chance;
            document.getElementById('ud-point').innerText = point;
            document.getElementById('ud-result').innerText = guess < answer ? 'UP!' : 'DOWN!';
            if (chance === 0) {
                document.getElementById('ud-result').innerText = `실패! 1포인트 획득`;
                updateGamePoint(1);
                finished = true;
            }
        }
    };
}

// 게임 메뉴로 돌아가기
function renderGameMenu() {
    document.getElementById('game-content').innerHTML = `
	    <div class="text-gray-600 text-base mb-4">게임을 선택하세요.</div>
	    <div class="flex gap-2">
	      <button id="btn-even-odd" class="px-3 py-2 bg-blue-500 text-white rounded text-sm">홀짝</button>
	      <button id="btn-updown" class="px-3 py-2 bg-green-500 text-white rounded text-sm">UPDOWN</button>
	    </div>
	    <div class="mt-4 text-sm text-gray-700">
	      내 포인트: <span id="current-point">${document.getElementById('current-point') ? document.getElementById('current-point').innerText : 0}</span>
	    </div>
	  `;
    setGameMenuEvents();
}

// Ajax로 포인트 업데이트하고, 성공 시 메인으로 리다이렉트
function updateGamePoint(delta) {
    fetch('/game/point', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({delta: delta})
    })
        .then(res => {
            if (!res.ok) throw new Error('서버 오류');
            return res.json();
        })
        .then(data => {
            // 안전하게 요소 체크 후 갱신
            const pointElem = document.getElementById('current-point');
            if (pointElem) pointElem.innerText = data.point;
            window.location.href = '/';
        })
        .catch(err => { alert(err.message); });
}

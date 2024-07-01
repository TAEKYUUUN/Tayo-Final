document.addEventListener('DOMContentLoaded', function() {
	const starButtons = document.querySelectorAll('.star_btn');

	starButtons.forEach(function(starBtn) {
		const starImg = starBtn.querySelector('.star_img');

		starBtn.addEventListener('click', function() {
			if (starImg.src === 'https://flow.team/flow-renewal/assets/images/icons/icon_star.png?v=c253f2c3b6c7b36ebe1fbb7ba4c300802671771e') {
				starImg.src = 'https://flow.team/flow-renewal/assets/images/icons/icon_star_on.png?v=0b1344b90c03f4a1cec8afc4927837b54841744f';
			} else {
				starImg.src = 'https://flow.team/flow-renewal/assets/images/icons/icon_star.png?v=c253f2c3b6c7b36ebe1fbb7ba4c300802671771e';
			}
		});
	});
});

// 왼쪽 side 바 '새 프로젝트' 클릭 시 createNewProejct로 이동
document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('div_new_project').addEventListener('click', function() {
        var href = this.getAttribute('data-href');
        location.href = href;
    });
});

// 왼쪽 side 바 '대시보드' 클릭 시 dashboard로 이동
document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('div_toDashboard').addEventListener('click', function() {
        var href = this.getAttribute('data-href');
        location.href = href;
    });
});

// 왼쪽 side 바 '내 프로젝트' 클릭 시 projectlist로 이동
document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('div_toProjectlist').addEventListener('click', function() {
        var href = this.getAttribute('data-href');
        location.href = href;
    });
});

// 왼쪽 side 바 '회사 공개 프로젝트' 클릭 시 companyOpenProject로 이동
document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('div_toCompanyOpenProject').addEventListener('click', function() {
        var href = this.getAttribute('data-href');
        location.href = href;
    });
});
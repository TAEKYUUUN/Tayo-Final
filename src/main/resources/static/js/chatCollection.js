document.addEventListener('DOMContentLoaded', function() {
	const menuItems = document.querySelectorAll('.menu-btns li');
	const saveButton = document.querySelector('.js-save-btn');
	const checkMenuItems = document.querySelectorAll('.check-menu-item');
	const moreBtns = document.querySelectorAll('.js-collection-more-button');
	const collectionNoticeItems = document.querySelectorAll('.collection-notice-item');
	const collectionLinkItems = document.querySelectorAll('.collection-link-item');
	const collectionFileItems = document.querySelectorAll('.collection-file-item');
	const collectionImgItems = document.querySelectorAll('.collection-img-item');
	const checkSelectAlert = document.querySelector('.js-select-alert');
	const selectCountSpan = document.querySelector('.js-select-count');
	const selectCancelBtn = document.querySelector('.js-select-cancle');
	const iconFile = document.querySelectorAll('.icon-file');

	iconFile.forEach(icon => {
		const fileType = icon.innerText;

		if (fileType === 'application/haansofthwp') {
			icon.classList.add('icon-hwp');
		} else if (fileType === 'application/vnd.openxmlformats-officedocument.presentationml.presentation') {
			icon.classList.add('icon-ppt');
		} else if (fileType === 'application/msword') {
			icon.classList.add('icon-etc');
		} else if (fileType === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet') {
			icon.classList.add('icon-excel');
		} else if (fileType === 'application/pdf') {
			icon.classList.add('icon-pdf');
		} else if (fileType === 'application/x-zip-compressed') {
			icon.classList.add('icon-zip');
		}
	});

	function updateSelectAlert() {
		const selectedCount = document.querySelectorAll('.check-menu-item.on').length;
		if (selectedCount > 0) {
			selectCountSpan.textContent = `${selectedCount}개 선택`;
			checkSelectAlert.style.display = 'flex';
		} else {
			checkSelectAlert.style.display = 'none';
		}
	}

	// 각 버튼 클릭시 그 버튼에 해당하는 공지,링크,파일,이미지 띄워주는 스크립트
	menuItems.forEach((item) => {
		item.addEventListener('click', function() {

			checkMenuItems.forEach((item) => {
				item.classList.remove('on');
			});
			updateSelectAlert(); // 모든 선택 해제 후 알림 업데이트

			menuItems.forEach((el) => el.classList.remove('on'));

			this.classList.add('on');

			if (this.id === 'chat-image-btn' || this.id === 'chat-file-btn') {
				saveButton.style.display = 'block';
			} else {
				saveButton.style.display = 'none';
			}

			if (this.id === 'chat-notice-btn') {
				collectionNoticeItems.forEach((notice) => {
					notice.classList.add('on');
				});
			} else {
				collectionNoticeItems.forEach((notice) => {
					notice.classList.remove('on');
				});
			}

			if (this.id === 'chat-link-btn') {
				collectionLinkItems.forEach((notice) => {
					notice.classList.add('on');
				});
			} else {
				collectionLinkItems.forEach((notice) => {
					notice.classList.remove('on');
				});
			}

			if (this.id === 'chat-file-btn') {
				collectionFileItems.forEach((notice) => {
					notice.classList.add('on');
				});
			} else {
				collectionFileItems.forEach((notice) => {
					notice.classList.remove('on');
				});
			}

			if (this.id === 'chat-image-btn') {
				collectionImgItems.forEach((notice) => {
					notice.classList.add('on');
				});
			} else {
				collectionImgItems.forEach((notice) => {
					notice.classList.remove('on');
				});
			}
		});
	});


	checkMenuItems.forEach((item) => {
		item.addEventListener('click', function() {
			this.classList.toggle('on');
			updateSelectAlert(); // 'on' 클래스 토글 후 알림 업데이트
		});
	});

	moreBtns.forEach((btn) => {
		const moreContents = btn.nextElementSibling;

		btn.addEventListener('click', () => {
			moreContents.style.display = 'block';
		});

		moreContents.addEventListener('mouseleave', () => {
			moreContents.style.display = 'none';
		});

	});
	selectCancelBtn.addEventListener('click', () => {
		checkMenuItems.forEach((item) => {
			item.classList.remove('on');
		});
		updateSelectAlert(); // 모든 선택 해제 후 알림 업데이트
	});


	updateSelectAlert();
});


const alarmIsRead = function(alarmIdx) {
	$.ajax({
		url: "/AlarmRead",
		type: 'PUT',
		contentType: 'application/json',
		data: JSON.stringify(alarmIdx),
		success: function() {

		},
		error: function(error) {
			alert(error.responseText);
			console.log(error.responseText);
		}
	});
}

document.addEventListener('DOMContentLoaded', function() {

	// 프로젝트 알림 클릭 이벤트 리스너
	function setupProjectAlarm() {
		const projectAlarm = document.querySelectorAll(`[id*="${'projectAlarm'}"]`);
		projectAlarm.forEach(div => {
			div.parentElement.addEventListener('click', () => {
				const afterMove = div.firstElementChild.value;
				window.location.href = '/projectFeed/' + afterMove;
			});
		});
	}

	const projectAlarm = document.querySelectorAll(`[id*="${'projectAlarm'}"]`);
	const postAlarm = document.querySelectorAll(`[id*="${'postAlarm'}"]`);

	postAlarm.forEach(div => {
		div.parentElement.addEventListener('click', () => {
			const anchor = div.firstElementChild.value;
			const moveAfter = div.querySelectorAll('input')[1].value;
			const alarmIdx = div.querySelector('#alarmIdx').value;
			alarmIsRead(alarmIdx);
			div.parentElement.classList.remove('unreadAlarm');
			window.location.href = '/projectFeed/' + moveAfter + '#postIdx_' + anchor;
		})
	})
	projectAlarm.forEach(div => {
		div.parentElement.addEventListener('click', () => {
			const afterMove = div.firstElementChild.value;
			window.location.href = '/projectFeed/' + afterMove;
		})
	})

	// 페이지 이동 관련 기능
	function setupNavigation() {
		const navigationMappings = {
			'new_prj_default': 'createNewProject로 이동',
			'div_new_project': 'createNewProject로 이동',
			'div_toDashboard': 'dashboard로 이동',
			'div_toProjectlist': 'projectlist로 이동',
			'div_toCompanyOpenProject': 'companyOpenProject로 이동'
		};

		Object.keys(navigationMappings).forEach(id => {
			const element = document.getElementById(id);
			if (element) {
				element.addEventListener('click', function() {
					var href = this.getAttribute('data-href');
					location.href = href;
				});
			}
		});
	}

	// 알림 관련 이벤트 리스너
	function setupAlarmListeners() {
		document.querySelector('#unread').addEventListener('click', () => {
			document.querySelector('#allalarm').classList.remove('allOrUnread');
			document.querySelector('#unread').classList.add('allOrUnread');
			document.querySelector('#unreadUl').style.removeProperty('display');
			document.querySelector('#readUl').style.display = 'none';
		});

		document.querySelector('#allalarm').addEventListener('click', () => {
			document.querySelector('#unread').classList.remove('allOrUnread');
			document.querySelector('#allalarm').classList.add('allOrUnread');
			document.querySelector('#unreadUl').style.display = 'none';
			document.querySelector('#readUl').style.removeProperty('display');
		});

		document.querySelector('#div_top_alarm').addEventListener('click', () => {
			document.querySelector('#alarmPopup').style.removeProperty('display');
		});

		document.querySelector('#exitlogoedit').addEventListener('click', () => {
			document.querySelector('#alarmPopup').style.display = 'none';
		});
	}

	// 상단 프로필 메시지 설정
	function setupProfileMessage() {
		function getGreetingMessage() {
			const now = new Date();
			const hours = now.getHours();
			let message = "";

			if (hours >= 3 && hours < 7) {
				message = "님 잔잔한 새벽입니다.🌛";
			} else if (hours >= 7 && hours < 12) {
				message = "님 활기찬 오전입니다.🌞";
			} else if (hours >= 12 && hours < 20) {
				message = "님 즐거운 오후입니다.😊";
			} else {
				message = "님 아늑한 저녁입니다.✨";
			}

			return message;
		}

		const profileSpan = document.querySelector("#div_profile span");
		if (profileSpan) {
			const memberName = profileSpan.textContent.trim();
			profileSpan.textContent = memberName + getGreetingMessage();
		}
	}

	// 위젯 메뉴 아이콘 클릭 시 크기 변경
	function setupWidgetMenuIcons() {
		const widgetMenuIcons = document.querySelectorAll('.widget_menu_icon');
		widgetMenuIcons.forEach(widgetMenuIcon => {
			const changeWidgetSize = widgetMenuIcon.querySelector('.change_widget_size');
			widgetMenuIcon.addEventListener('click', () => {
				changeWidgetSize.style.display = (changeWidgetSize.style.display === 'none' || changeWidgetSize.style.display === '') ? 'block' : 'none';
			});
		});
	}

	// 프로젝트 항목 클릭 시 이동
	function setupProjectItems() {
		const projectItems = document.querySelectorAll('.div_prj_item');
		projectItems.forEach(item => {
			item.addEventListener('click', function() {
				var href = this.getAttribute('data-href');
				var projectIdx = item.firstElementChild.value;
				location.href = href + '/' + projectIdx;
			});
		});
	}

	// 채팅 창 관련 이벤트 리스너
	function setupChatWindow() {
		const openChatWindowBtn = document.getElementById('open-chat-window-btn');
		const closeChatWindowBtn = document.getElementById('close-chat-window-btn');
		const chatWindow = document.getElementById('chat-window-js');
		const chatListItems = document.querySelectorAll('.chat-list-item');

		function openChatWindow(chatRoomUrl) {
			window.open(chatRoomUrl, 'ChatWindow', 'width=450,height=800');
		}

		if (openChatWindowBtn && closeChatWindowBtn && chatWindow) {
			openChatWindowBtn.addEventListener('click', () => {
				chatWindow.style.display = 'flex';
			});

			closeChatWindowBtn.addEventListener('click', () => {
				chatWindow.style.display = 'none';
			});

			chatListItems.forEach(item => {
				const chatIdx = item.querySelector('input').value;
				const form = document.createElement('form');
				form.action = 'chatRoom/' + chatIdx;
				form.method = 'post';

				while (item.firstChild) {
					form.appendChild(item.firstChild);
				}
				form.className = item.className;
				item.parentNode.replaceChild(form, item);

				form.addEventListener('click', (event) => {
					event.preventDefault();
					openChatWindow(form.getAttribute('action'));
				});
			});
		}
	}

	//////////////////////////// 채팅 멤버 초대(추가) ////////////////////////////////////
	const openInvite = document.getElementById('open_invite');
	const nothingMemberButton = document.querySelector('.nothing_btn');
	const inviteModal = document.getElementById("invite-participants-modal");
	const closeInviteButton = document.querySelector('.invite-modal-header .close');
	const inviteModalBtn = document.querySelector('.invite-modal-footer .invite_submit_btn');

	openInvite.addEventListener('click', openInviteModal);
	closeInviteButton.addEventListener('click', closeInviteModal);

	function openInviteModal() {
		inviteModal.style.display = "block";
	}

	function closeInviteModal() {
		inviteModal.style.display = "none";
	}

	document.querySelectorAll('#employeeList li').forEach(item => {
		item.addEventListener('click', event => {
			const id = item.getAttribute('data-id');
			const name = item.getAttribute('data-name');
			toggleInviteSelectedList(id, name);
		});
	});

	// 선택된 리스트 따로 표시
	function toggleInviteSelectedList(id, name) {
		try {
			const selectedList = document.getElementById('selectedList');
			const existingItem = document.querySelector(`#selectedList li[data-id="${id}"]`);
			const itemToUpdate = document.querySelector(`#employeeList li[data-id="${id}"] .check_invite img`);

			if (!selectedList) {
				throw new Error("selectedList 요소를 찾을 수 없습니다.");
			}

			if (!itemToUpdate) {
				throw new Error(`itemToUpdate 요소를 찾을 수 없습니다. data-id=${id}`);
			}

			if (existingItem) {
				existingItem.remove();
				itemToUpdate.src = 'https://flow.team/flow-renewal/assets/images/icons/icon_check.png?v=7f39425e224a53bff0043caff9f6446b14c0f667';
			} else {
				const li = document.createElement('li');
				li.setAttribute('data-id', id);
				li.classList.add('selected_member');
				li.innerHTML = `<img src="https://flow.team/flow-renewal/assets/images/profile-default.png"/>
                                <strong style="margin-left:2px;">${name}</strong>
                                <button class="remove-btn" data-id="${id}" style="position:absolute; right:10px; color:#000;">X</button>`;

				selectedList.appendChild(li);

				li.querySelector('.remove-btn').addEventListener('click', event => {
					removeFromSelectedList(id);
				});

				itemToUpdate.src = 'https://flow.team/flow-renewal/assets/images/icons/icon_check_on.png?v=8a10086b9d33ff65ead56b67a69de154fcbe2c4a';
			}
		} catch (error) {
			console.error(error.message);
		}
	}

	function removeFromSelectedList(id) {
		const item = document.querySelector(`#selectedList li[data-id="${id}"]`);
		if (item) {
			item.remove();
		}

		const itemToUpdate = document.querySelector(`#employeeList li[data-id="${id}"] .check_invite img`);
		if (itemToUpdate) {
			itemToUpdate.src = 'https://flow.team/flow-renewal/assets/images/icons/icon_check.png?v=7f39425e224a53bff0043caff9f6446b14c0f667';
		}
	}

	document.querySelectorAll('#employeeList li').forEach(item => {
		item.addEventListener('mouseover', event => {
			const img = item.querySelector('.check_invite img');
			const id = item.getAttribute('data-id');
			const selectedItem = document.querySelector(`#selectedList li[data-id="${id}"]`);
			if (img && !selectedItem) {
				img.src = 'https://flow.team/flow-renewal/assets/images/icons/icon_check_hover.png?v=5244c8bcd5a25963296eb4e51fc2a65bcc3dc376';
			}
		});
		item.addEventListener('mouseout', event => {
			const img = item.querySelector('.check_invite img');
			const id = item.getAttribute('data-id');
			const selectedItem = document.querySelector(`#selectedList li[data-id="${id}"]`);
			if (img && !selectedItem) {
				img.src = 'https://flow.team/flow-renewal/assets/images/icons/icon_check.png?v=7f39425e224a53bff0043caff9f6446b14c0f667';
			}
		});
	});

	function inviteParticipants(event) {
		event.preventDefault();


		const employeeList = document.getElementById('employeeList');

		const pushMyId = employeeList.getAttribute('push-my-id');

		const selectedList = document.getElementById('selectedList');
		if (!selectedList) {
			console.error('selectedList 요소를 찾을 수 없습니다.');
			return;
		}

		const members = [pushMyId];
		selectedList.querySelectorAll('li').forEach(item => {
			members.push(
				item.getAttribute('data-id'),
			);
		});



		$.ajax({
			url: "/chatRoomCreate",
			type: 'POST',
			contentType: 'application/json',
			data: JSON.stringify(members),
			success: function(response) {
				const requestData = JSON.parse(response);
				const { chatIdx, memberName } = requestData;
				closeInviteModal()
				const chatList = document.querySelector('.chat-list');
				if (chatList) {
					const firstChild = chatList.firstElementChild;
					const str = `<div class="chat-list-item">
									<input type="hidden" value="${chatIdx}" />
									<img src="https://via.placeholder.com/40" alt="User" />
									<div class='flex-box'>
									    <div>
									      <span class='chat-name'></span>
									      <div class='default-chat-name'>
									        <span class='chat-name'>
									          <span>${memberName}</span>
									        </span>
									      </div>
									    </div>
									    <span class='chat-last-content'>메시지 없음</span>
									  </div>
									<div class="mini-mode-chattng-type-3">
										<div class="mini-mode-chattng-type-text-2" >없음</div>
									</div>
								</div>`
					if (firstChild) {
						firstChild.insertAdjacentHTML('beforebegin', str);
					} else {
						// 만약 chatList에 자식 요소가 없다면, 첫 번째 요소로 추가
						chatList.innerHTML = str;
					}
				}
			},
			error: function(error) {
				alert(error.responseText);
				console.log(error.responseText);
			}
		});

	}

	document.querySelector('.invite_submit_btn').addEventListener('click', inviteParticipants);




	// 초기화 함수 호출
	function initialize() {
		setupProjectAlarm();
		setupNavigation();
		setupAlarmListeners();
		setupProfileMessage();
		setupWidgetMenuIcons();
		setupProjectItems();
		setupChatWindow();
	}

	initialize();
});

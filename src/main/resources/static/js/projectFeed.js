document.addEventListener('DOMContentLoaded', function() {
	const createPostArea = document.getElementById('create_post_area');
	const modal = document.querySelector('.modal');
	const backgroundFull = document.getElementById('div_backgroundfull');
	const closeBtn = document.querySelector('.close-btn');
	const tabs = document.querySelectorAll('.tabs');
	const tabTypeInput = document.getElementById('tabType');

	function resetModalFields() {
		const fields = modal.querySelectorAll('input[type="text"], input[type="hidden"], input[type="datetime-local"], input[type="checkbox"], textarea');
		fields.forEach(field => {
			field.value = '';
			if (field.type === 'checkbox') {
				field.checked = false;
			}
		});
		const hiddenFields = modal.querySelectorAll('input[type="hidden"]');
		hiddenFields.forEach(hiddenField => {
			hiddenField.value = '0';
		});
	}

	createPostArea.addEventListener('click', function() {
		modal.style.display = 'block';
		backgroundFull.style.display = 'block';
		tabTypeInput.value = 1;
		resetModalFields();
	});

	closeBtn.addEventListener('click', function() {
		modal.style.display = 'none';
		backgroundFull.style.display = 'none';
	});

	tabs.forEach(tab => {
		tab.addEventListener('click', function() {
			tabs.forEach(t => t.style = ""); // Reset styles for all tabs
			this.style.color = 'black';
			this.style.borderBottom = '2px solid #666';
			resetModalFields(); // Reset fields when switching tabs
			const tabType = document.getElementById('tabType');
			if (this.classList.contains('tab-paragraph')) {
				tabType.value = 1;
			} else if (this.classList.contains('tab-task')) {
				tabType.value = 2;
			} else if (this.classList.contains('tab-schedule')) {
				tabType.value = 3;
			} else if (this.classList.contains('tab-todo')) {
				tabType.value = 4;
			} else if (this.classList.contains('tab-vote')) {
				tabType.value = 5;
			}
		});
	});

	function addTaskContent() {
		const modalContent = document.querySelector('.modal-content');
		modalContent.style.minHeight = '592px';
		modalContent.style.overflowY = 'auto';
		modal.style.height = '780px';

		const taskContent = `
			<div class="input-group1">
				<input type="text" id="title" name="title" placeholder="제목을 입력하세요." />
			</div>
			<div class="input-group2" style="max-height: 400px;">
				<input type="hidden" id="hiddenCondition" name="condition" value="1">
				<div class="task_icon_box1">
					<img src="https://flow.team/flow-renewal/assets/images/icons/icon-post-status.svg?v=b3a5b7f86f05f658d1ce954c34f8c33a61ea8873" style="margin-right: 10px;"/>
					<button type="button" class="task_state_btn request active" name="condition" value="1">요청</button>
					<button type="button" class="task_state_btn progress" name="condition" value="2">진행</button>
					<button type="button" class="task_state_btn feedback" name="condition" value="3">피드백</button>
					<button type="button" class="task_state_btn completion" name="condition" value="4">완료</button>
					<button type="button" class="task_state_btn hold" name="condition" value="5">보류</button>
				</div>
				<div class="task_icon_box2">
					<img src="https://flow.team/flow-renewal/assets/images/icons/icon-post-worker.svg?v=2bd86654bf591d842c49c9a76d76c11f1507ce8d" style="margin-right: 16px;"/>
					<button type="button" class="update_btn" name="manager">담당자 추가</button>
				</div>
				<div class="task_icon_box1">
					<img src="https://flow.team/flow-renewal/assets/images/icons/icon-post-date.svg?v=cfae9e268527a6f7007fe81fd49cab2c9659eea3" style="margin-right:16px;"/>
					<button type="button" class="update_btn" name="endDate">마감일 추가</button>
				</div>
				<div class="div_textarea" style="height:270px;">
					<textarea id="content" name="content" placeholder="내용을 입력하세요." style="height:270px;"></textarea>
				</div>
				<div class="subtask_header">
					<span class="subtask_title" style="font-size:14px;">
						<img src="/img/subtask_icon.png"/>
						하위업무
					</span>
				</div>
				<div id="taskContainer">              
					<button type="button" class="add_lowertask" id="addLowerTask">추가</button>
				</div>
			</div>
		`;
		modalContent.innerHTML = taskContent;

		const taskStateButtons = document.querySelectorAll('.task_state_btn');
		taskStateButtons.forEach(button => {
			button.addEventListener('click', function() {
				taskStateButtons.forEach(btn => btn.classList.remove('active'));
				this.classList.add('active');
				document.getElementById('hiddenCondition').value = this.value;
			});
		});

		document.getElementById('addLowerTask').addEventListener('click', function() {
			addLowerTask();
		});

		function addLowerTask() {
			const taskContainer = document.getElementById('taskContainer');
			const lowerTasks = taskContainer.querySelectorAll('.input_lowertask');

			if (lowerTasks.length >= 5) {
				alert('하위 업무는 최대 5개까지만 추가할 수 있습니다.');
				return;
			}

			const newTaskDiv = document.createElement('div');
			newTaskDiv.className = 'input_lowertask';

			const requestButton = document.createElement('button');
			requestButton.type = 'button';
			requestButton.className = 'lowertask_state_btn request active';
			requestButton.name = 'lowerTaskConditions';
			requestButton.value = '1';
			requestButton.textContent = '요청';
			newTaskDiv.appendChild(requestButton);

			const inputField = document.createElement('input');
			inputField.type = 'text';
			inputField.className = 'lowertask_name';
			inputField.name = 'lowerTaskNames';
			inputField.placeholder = '업무명 입력(Enter로 업무 연속 등록 가능)';
			newTaskDiv.appendChild(inputField);

			inputField.addEventListener('keypress', function(event) {
				if (event.key === 'Enter') {
					event.preventDefault();
					addLowerTaskAndFocus();
				}
			});

			const miniSubworkIcon = document.createElement('div');
			miniSubworkIcon.className = 'mini_subwork_icon';

			const miniSubworkImgDiv1 = document.createElement('div');
			miniSubworkImgDiv1.className = 'mini_subwork_img_div';
			const miniSubworkImg1 = document.createElement('img');
			miniSubworkImg1.className = 'mini_subwork_img2';
			miniSubworkImg1.name = 'lowerTaskManagers';
			miniSubworkImg1.src = 'https://flow.team/flow-renewal/assets/images/icons/icon-post-worker.svg?v=dab609f4e3114ab334c0216bd41d1a6e27b6503a';
			miniSubworkImgDiv1.appendChild(miniSubworkImg1);

			const miniSubworkImgDiv2 = document.createElement('div');
			miniSubworkImgDiv2.className = 'mini_subwork_img_div';
			const miniSubworkImg2 = document.createElement('img');
			miniSubworkImg2.className = 'mini_subwork_img2';
			miniSubworkImg2.name = 'lowerTaskEndDates';
			miniSubworkImg2.src = 'https://flow.team/flow-renewal/assets/images/icons/icon-post-date.svg?v=118ef1e91b7ced5f275a138083d2ddd7cf94773d';
			miniSubworkImgDiv2.appendChild(miniSubworkImg2);

			miniSubworkIcon.appendChild(miniSubworkImgDiv1);
			miniSubworkIcon.appendChild(miniSubworkImgDiv2);

			const hiddenInput = document.createElement('input');
			hiddenInput.type = 'hidden';
			hiddenInput.className = 'lowertask_state_value';
			hiddenInput.name = 'lowerTaskConditions';
			hiddenInput.value = requestButton.value; // 초기 값 설정
			newTaskDiv.appendChild(hiddenInput);

			const removeButton = document.createElement('button');
			removeButton.textContent = 'x';
			removeButton.className = 'remove-btn';
			removeButton.style.position = 'relative'; // 스타일 설정을 명확히
			removeButton.style.right = '-37px'; // 기존 스타일 유지

			removeButton.addEventListener('click', function() {
				newTaskDiv.remove();
			});

			newTaskDiv.appendChild(miniSubworkIcon);
			newTaskDiv.appendChild(removeButton);
			taskContainer.insertBefore(newTaskDiv, document.getElementById('addLowerTask'));

			return inputField;
		}

		function addLowerTaskAndFocus() {
			const newInput = addLowerTask();
			if (newInput) {
				newInput.focus();
			}
		}
	}

	function addParagraphContent() {
		const modalContent = document.querySelector('.modal-content');
		modalContent.style.minHeight = '';
		modalContent.style.overflowY = '';
		modal.style.height = '780px';

		const paragraphContent = `
			<div class="input-group1">
				<input type="text" id="title" name="title" placeholder="제목을 입력하세요." />
			</div>
			<div class="input-group2" style="min-height: 465px; margin-bottom:34px">
				<textarea id="content" name="content" placeholder="내용을 입력하세요."></textarea>
			</div>
		`;
		modalContent.innerHTML = paragraphContent;
	}

	function addScheduleContent() {
		const modalContent = document.querySelector('.modal-content');
		modalContent.style.minHeight = '592px';
		modalContent.style.overflowY = 'auto';
		modal.style.height = '780px';

		const scheduleContent = `
			<div class="input-group1">
				<input type="text" id="title" name="title" placeholder="제목을 입력하세요." />
			</div>
			<div class="input-group2" style="max-height: 400px;">
				<div class="task_icon_box1">
					<img src="https://flow.team/flow-renewal/assets/images/icons/icon-post-date.svg?v=cfae9e268527a6f7007fe81fd49cab2c9659eea3" style="margin-right: 16px;" />
					<input type="datetime-local" id="start" name="startDatetime"/>&ensp;-&ensp; 
					<input type="datetime-local" id="end" name="endDatetime"/>
					<input type="checkbox" id="allDay" name="allDay"/>
					<label for="allDay">종일</label>
				</div>
				<div class="task_icon_box2">
					<img src="https://flow.team/flow-renewal/assets/images/icons/icon-post-worker.svg?v=2bd86654bf591d842c49c9a76d76c11f1507ce8d" style="margin-right: 16px;"/>
					<button type="button" class="update_btn">참석자 추가</button>
				</div>
				<div class="task_icon_box1">
					<img src="https://flow.team/flow-renewal/assets/images/icons/icon-post-place.svg?v=4475443d9f93bbbc02e906dbdac60bcfd1fd021d" style="margin-right : 16px;"/>
					<input type="text" placeholder="장소를 입력하세요" id="place" name="place"/>
				</div>
				<div class="div_textarea">
					<textarea id="content" name="content" placeholder="내용을 입력하세요."></textarea>
				</div>
			</div>
		`;
		modalContent.innerHTML = scheduleContent;

		document.getElementById('allDay').addEventListener('change', function() {
			const startInput = document.getElementById('start');
			const endInput = document.getElementById('end');
			if (this.checked) {
				startInput.type = 'date';
				startInput.name = 'startDate';
				endInput.type = 'date';
				endInput.name = 'endDate';
			} else {
				startInput.type = 'datetime-local';
				startInput.name = 'startDatetime';
				endInput.type = 'datetime-local';
				endInput.name = 'endDatetime';
			}
		});
	}

	function addTodoContent() {
		const modalContent = document.querySelector('.modal-content');
		modalContent.style.minHeight = '592px';
		modalContent.style.overflowY = 'auto';
		modal.style.height = '780px';

		const todoContent = `
			<div class="input-group1">
				<input type="text" id="title" name="title" placeholder="제목을 입력하세요." />
			</div>
			<div class="input-group2" style="max-height: 400px;">
				<div class="div_todo_edit">
					<input type="text" placeholder="할 일 추가 (Enter 또는 Tab) / 최대 60자" class="todo_input" id="todoNames" name="todoNames"/>
					<div class="mini_subwork_icon">
						<div class="mini_subwork_img_div"><img class="mini_subwork_img2" src="https://flow.team/flow-renewal/assets/images/icons/icon-post-worker.svg?v=dab609f4e3114ab334c0216bd41d1a6e27b6503a"></div>
						<div class="mini_subwork_img_div"><img class="mini_subwork_img2" src="https://flow.team/flow-renewal/assets/images/icons/icon-post-date.svg?v=118ef1e91b7ced5f275a138083d2ddd7cf94773d"></div>
					</div>
				</div>
			</div>
		`;
		modalContent.innerHTML = todoContent;

		function createNewTodoEdit() {
			const newDiv = document.createElement('div');
			newDiv.className = 'div_todo_edit';

			const newInput = document.createElement('input');
			newInput.type = 'text';
			newInput.placeholder = '할 일 추가 (Enter 또는 Tab) / 최대 60자';
			newInput.className = 'todo_input';
			newInput.name = 'todoNames';
			newInput.id = 'todoNames';

			const miniSubworkIcon = document.createElement('div');
			miniSubworkIcon.className = 'mini_subwork_icon';

			const miniSubworkImgDiv1 = document.createElement('div');
			miniSubworkImgDiv1.className = 'mini_subwork_img_div';
			const miniSubworkImg1 = document.createElement('img');
			miniSubworkImg1.className = 'mini_subwork_img2';
			miniSubworkImg1.src = 'https://flow.team/flow-renewal/assets/images/icons/icon-post-worker.svg?v=dab609f4e3114ab334c0216bd41d1a6e27b6503a';
			miniSubworkImgDiv1.appendChild(miniSubworkImg1);

			const miniSubworkImgDiv2 = document.createElement('div');
			miniSubworkImgDiv2.className = 'mini_subwork_img_div';
			const miniSubworkImg2 = document.createElement('img');
			miniSubworkImg2.className = 'mini_subwork_img2';
			miniSubworkImg2.src = 'https://flow.team/flow-renewal/assets/images/icons/icon-post-date.svg?v=118ef1e91b7ced5f275a138083d2ddd7cf94773d';
			miniSubworkImgDiv2.appendChild(miniSubworkImg2);

			miniSubworkIcon.appendChild(miniSubworkImgDiv1);
			miniSubworkIcon.appendChild(miniSubworkImgDiv2);

			const removeButton = document.createElement('button');
			removeButton.textContent = 'x';
			removeButton.className = 'remove-btn';
			removeButton.addEventListener('click', () => {
				newDiv.remove();
			});

			newDiv.appendChild(newInput);
			newDiv.appendChild(miniSubworkIcon);
			newDiv.appendChild(removeButton);

			document.querySelector('.input-group2').appendChild(newDiv);

			newInput.addEventListener('keydown', handleKeyDown);

			newInput.focus();
		}

		function handleKeyDown(event) {
			if (event.key === 'Enter' || event.key === 'Tab') {
				event.preventDefault();
				createNewTodoEdit();
			}
		}

		const initialInput = document.querySelector('.todo_input');
		initialInput.addEventListener('keydown', handleKeyDown);
	}

	function addVoteContent() {
		const modalContent = document.querySelector('.modal-content');
		modalContent.style.minHeight = '592px';
		modalContent.style.overflowY = 'auto';
		modal.style.height = '780px';

		const voteContent = `
			<div class="input-group1">
				<input type="text" id="title" name="title" placeholder="제목을 입력하세요." />
			</div>
			<div class="input-group2" style="max-height: 400px;">
				<input type="text" id="vote_detail" name="voteDetail" placeholder="투표에 관한 설명 입력(옵션)"/>
				<div class="div_voteitem_edit">
					<input type="text" placeholder="항목 추가 (Enter 또는 Tab) / 최대 60자" class="voteitem_input" id="voteitem" name="voteItems"/>
				</div>
				<div class="div_voteitem_edit">
					<input type="text" placeholder="항목 추가 (Enter 또는 Tab) / 최대 60자" class="voteitem_input" id="voteitem" name="voteItems"/>
				</div>
				<div class="voteContainer">              
					<button type="button" class="add_voteitem" id="addVoteitem">투표 항목 추가</button>
				</div>
				<div class="vote_option_container">
					<div class="endDate vote_option">
						<div class="option_name">
							<img src="/img/vote_enddate.png"/>
							<span>투표 마감일</span>
						</div>
						<button type="button" id="add_voteEndDate">마감일 추가</button>
					</div>
					<div class="pluralVote vote_option">
						<div class="option_name">
							<img src="/img/vote_doublecheck.png"/>
							<span>복수 투표</span>
						</div>
						<label class="switch" style="margin-top:4px;">
							<input type="hidden" name="isplural" value="0">
							<input type="checkbox" name="isplural" value="1" checked>
							<span class="slider round"></span>
						</label>
					</div>
					<div class="anonymousVote vote_option">
						<div class="option_name">
							<img src="/img/vote_anonymous.png"/>
							<span>익명 투표</span>
						</div>
						<label class="switch" style="margin-top:4px;">
							<input type="hidden" name="isanonymous" value="0">
							<input type="checkbox" name="isanonymous" value="1" checked>
							<span class="slider round"></span>
						</label>
					</div>
				</div>
			</div>
		`;
		modalContent.innerHTML = voteContent;

		const voteContainer = document.querySelector('.voteContainer');
		const addVoteitemBtn = document.getElementById('addVoteitem');

		function createNewVoteItemEdit() {
			const newDiv = document.createElement('div');
			newDiv.className = 'div_voteitem_edit';

			const newInput = document.createElement('input');
			newInput.type = 'text';
			newInput.placeholder = '항목 추가 (Enter 또는 Tab) / 최대 60자';
			newInput.className = 'voteitem_input';
			newInput.name = 'voteItems';

			const removeButton = document.createElement('button');
			removeButton.textContent = 'x';
			removeButton.className = 'remove-btn';
			removeButton.style.right = '-17px';
			removeButton.addEventListener('click', () => {
				newDiv.remove();
			});

			newDiv.appendChild(newInput);
			newDiv.appendChild(removeButton);

			voteContainer.parentNode.insertBefore(newDiv, voteContainer);
		}

		addVoteitemBtn.addEventListener('click', createNewVoteItemEdit);
	}

	// Tab event listeners
	const taskTab = document.querySelector('.tab-task');
	taskTab.addEventListener('click', addTaskContent);

	const paragraphTab = document.querySelector('.tab-paragraph');
	paragraphTab.addEventListener('click', addParagraphContent);

	const scheduleTab = document.querySelector('.tab-schedule');
	scheduleTab.addEventListener('click', addScheduleContent);

	const todoTab = document.querySelector('.tab-todo');
	todoTab.addEventListener('click', addTodoContent);

	const voteTab = document.querySelector('.tab-vote');
	voteTab.addEventListener('click', addVoteContent);

	const form = document.querySelector('form');
	form.addEventListener('submit', function() {
		// 복수 투표 체크박스와 hidden input
		const isPluralCheckbox = document.querySelector('input[name="isplural"][type="checkbox"]');
		const isPluralHidden = document.querySelector('input[name="isplural"][type="hidden"]');

		// 익명 투표 체크박스와 hidden input
		const isAnonymousCheckbox = document.querySelector('input[name="isanonymous"][type="checkbox"]');
		const isAnonymousHidden = document.querySelector('input[name="isanonymous"][type="hidden"]');

		// 체크박스 상태에 따라 hidden input 값 업데이트
		isPluralHidden.value = isPluralCheckbox.checked ? '1' : '0';
		isAnonymousHidden.value = isAnonymousCheckbox.checked ? '1' : '0';
	});

	// 왼쪽 사이드 바 클릭 이벤트
	document.querySelectorAll('#div_new_project, #div_toDashboard, #div_toProjectlist, #div_toCompanyOpenProject').forEach(item => {
		item.addEventListener('click', function() {
			var href = this.getAttribute('data-href');
			location.href = href;
		});
	});

	// Modal functions for inviting participants
	const inviteButton = document.querySelector('#div_invite_prjmem img');
	const inviteModal = document.getElementById("invite-participants-modal");
	const closeInviteButton = document.querySelector('.invite-modal-header .close');
	const inviteModalBtn = document.querySelector('.invite-modal-footer .invite_submit_btn');

	inviteButton.addEventListener('click', openInviteModal);
	closeInviteButton.addEventListener('click', closeInviteModal);
	inviteModalBtn.addEventListener('click', inviteParticipants);

	function openInviteModal() {
		inviteModal.style.display = "block";
		backgroundFull.style.display = "block";
	}

	function closeInviteModal() {
		inviteModal.style.display = "none";
		backgroundFull.style.display = "none";
	}

	document.querySelectorAll('#employeeList li').forEach(item => {
		item.addEventListener('click', event => {
			const id = item.getAttribute('data-id');
			const name = item.getAttribute('data-name');
			toggleSelectedList(id, name);
		});
	});

	function toggleSelectedList(id, name) {
		const selectedList = document.getElementById('selectedList');
		const existingItem = document.querySelector(`#selectedList li[data-id="${id}"]`);
		const itemToUpdate = document.querySelector(`#employeeList li[data-id="${id}"] .check_invite img`);

		if (existingItem) {
			// 항목이 이미 선택된 경우 리스트에서 제거
			existingItem.remove();
			itemToUpdate.src = 'https://flow.team/flow-renewal/assets/images/icons/icon_check.png?v=7f39425e224a53bff0043caff9f6446b14c0f667';
		} else {
			// 항목이 선택되지 않은 경우 리스트에 추가
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
		const url = window.location.href;
		const currentProjectIdx = url.split('/projectFeed/')[1];


		const selectedList = document.getElementById('selectedList');
		if (!selectedList) {
			console.error('selectedList 요소를 찾을 수 없습니다.');
			return;
		}

		const members = [];
		selectedList.querySelectorAll('li').forEach(item => {
			members.push({
				memberIdx: item.getAttribute('data-id'),
			});
		});
		

		fetch(`/inviteParticipants/${currentProjectIdx}`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(members)
		})
			.then(response => response.json())
			.then(data => {
				closeInviteModal();
				location.reload();
			})
			.catch((error) => {
				console.error('오류:', error);
			});
	}

	document.querySelector('.invite_submit_btn').addEventListener('click', inviteParticipants);
});

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
    
    function initializeFlatpickr() {
		// Initialize flatpickr
		var datePicker = flatpickr("#datepicker", {
			enableTime: false,
			dateFormat: "Y-m-d",
		});

		// Get the modal
		var calendarmodal = document.getElementById("div_calendar_modal");

		// Get the button that opens the modal
		var btn = document.getElementById("endDate");

		// Get the span to show selected date
		var selectedDateText = document.getElementById("selectedDateText");

		// Get the hidden input to store selected date
		var hiddenSelectedDate = document.getElementById("hiddenSelectedDate");

		// Get the <span> element that closes the modal
		var span = document.getElementsByClassName("close1")[0];

		// When the user clicks the button, open the modal and show datepicker
		btn.onclick = function() {
			calendarmodal.style.display = "block";
			datePicker.open(); // Open the date picker
		}

		// When the user clicks on <span> (x), close the modal
		span.onclick = function() {
			calendarmodal.style.display = "none";
		}

		// When the user clicks anywhere outside of the modal, close it
		window.onclick = function(event) {
			if (event.target == calendarmodal) {
				calendarmodal.style.display = "none";
			}
		}

		// Handle the date selection
		document.getElementById("selectDate").onclick = function(event) {
			event.preventDefault(); // form 제출 막음

			var selectedDate = document.getElementById("datepicker").value;
			var dayOfWeek = new Date(selectedDate).toLocaleDateString('ko-KR', { weekday: 'short' });
			selectedDateText.textContent = `${selectedDate} (${dayOfWeek}) 까지`;
			selectedDateText.style.display = "inline"; // Show the selected date text
			selectedDateText.style.marginLeft = '15px';
			btn.style.display = "none"; // Hide the button
			hiddenSelectedDate.value = selectedDate; // Set the hidden input value
			calendarmodal.style.display = "none";
		}
	}

	function initializeTaskStateButtons() {
		const taskStateButtons = document.querySelectorAll('.task_state_btn');
		taskStateButtons.forEach(button => {
			button.addEventListener('click', function() {
				taskStateButtons.forEach(btn => btn.classList.remove('active'));
				this.classList.add('active');
				document.getElementById('hiddenCondition').value = this.value;
			});
		});
	}

	function initializeLowerTaskHandlers() {
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
			removeButton.style.right = '-116px'; // 기존 스타일 유지

			removeButton.addEventListener('click', function() {
				newTaskDiv.remove();
			});

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
	
	function initializeTodoInputHandlers() {
		function createNewTodoEdit() {
			const newDiv = document.createElement('div');
			newDiv.className = 'div_todo_edit';

			const newInput = document.createElement('input');
			newInput.type = 'text';
			newInput.placeholder = '할 일 추가 (Enter 또는 Tab) / 최대 60자';
			newInput.className = 'todo_input';
			newInput.name = 'todoNames';
			newInput.id = 'todoNames';

			const removeButton = document.createElement('button');
			removeButton.textContent = 'x';
			removeButton.className = 'remove-btn';
			removeButton.addEventListener('click', () => {
				newDiv.remove();
			});

			newDiv.appendChild(newInput);
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

    createPostArea.addEventListener('click', function() {
        modal.style.display = 'block';
        backgroundFull.style.display = 'block';
        resetModalFields();
        tabTypeInput.value = 1; // 기본값 설정
        updateModalContent(1);
        
        // Reset styles for all tabs
        tabs.forEach(t => t.style = "");
        
        // Set style for the default tab (tab-paragraph)
        const paragraphTab = document.querySelector('.tab-paragraph');
        paragraphTab.style.color = 'black';
        paragraphTab.style.borderBottom = '2px solid #666';
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
            let tabType = 1;
            if (this.classList.contains('tab-paragraph')) {
                tabType = 1;
            } else if (this.classList.contains('tab-task')) {
                tabType = 2;
            } else if (this.classList.contains('tab-schedule')) {
                tabType = 3;
            } else if (this.classList.contains('tab-todo')) {
                tabType = 4;
            } else if (this.classList.contains('tab-vote')) {
                tabType = 5;
            }
            tabTypeInput.value = tabType;
            updateModalContent(tabType);
        });
    });

    function updateModalContent(tabType) {
        if (tabType === 1) {
            addParagraphContent();
        } else if (tabType === 2) {
            addTaskContent();
            resetManagerSelection();
        } else if (tabType === 3) {
            addScheduleContent();
        } else if (tabType === 4) {
            addTodoContent();
            resetManagerSelection();
        } else if (tabType === 5) {
            addVoteContent();
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

	function addTaskContent() {
		const modalContent = document.querySelector('.modal-content');
		modalContent.style.minHeight = '592px';
		modalContent.style.overflowY = 'auto';

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
            <div class="task_icon_box3">
                <img src="https://flow.team/flow-renewal/assets/images/icons/icon-post-worker.svg?v=2bd86654bf591d842c49c9a76d76c11f1507ce8d" style="margin-right: 16px;"/>
                <button type="button" class="update_btn" name="addmanager" id="addmanager">담당자 추가</button>
            </div>
            <div class="task_icon_box1">
                <img src="https://flow.team/flow-renewal/assets/images/icons/icon-post-date.svg?v=cfae9e268527a6f7007fe81fd49cab2c9659eea3" style="margin-right:16px;"/>
                <span id="selectedDateText" style="display:none; font-size:14px;"></span>
                <input type="hidden" id="hiddenSelectedDate" name="selectedDate">
                <button type="button" class="update_btn" id="endDate" name="endDate">마감일 추가</button>
                <div id="div_calendar_modal" class="calendar-modal">
                    <div class="calendar-modal-content">
                        <span class="close1">&times;</span>
                        <input type="text" id="datepicker" placeholder="날짜를 선택하세요">
                        <button id="selectDate">선택</button>
                    </div>
                </div>
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

		document.getElementById("addmanager").addEventListener("click", function(event) {
			event.stopPropagation();
			var modal = document.getElementById("div_add_manager_modal");
			modal.style.display = "block";
		});

		document.getElementById('managerForm').addEventListener('submit', function(event) {
			event.preventDefault();
			var modal = document.getElementById("div_add_manager_modal");
			const selectedManager = document.querySelector('.manager_item[data-id]');

			if (selectedManager) {
				const id = selectedManager.getAttribute('data-id');
				const name = selectedManager.querySelector('.manager_name').innerText;

				// 담당자 추가 버튼을 숨기고 선택된 담당자를 표시하는 HTML 추가
				const managerSelection = document.querySelector('.task_icon_box3');
				console.log('Manager Selection:', managerSelection); // 요소가 올바르게 선택되었는지 확인
				managerSelection.innerHTML = `
                <img src="https://flow.team/flow-renewal/assets/images/icons/icon-post-worker.svg?v=2bd86654bf591d842c49c9a76d76c11f1507ce8d" style="margin-right: 16px;"/>
                <span class="manager_item">
                    <span class="selected_profile">
                        <img src="https://flow.team/flow-renewal/assets/images/profile-default.png" />
                    </span>
                    <span class="manager_name">${name}</span>
                </span>
                <input type="hidden" name="managerIdx" value="${id}">
            `;
				console.log('InnerHTML 설정 완료'); // innerHTML이 정상적으로 설정되었는지 확인
				
				// 강제로 재렌더링 유도
		        managerSelection.style.display = 'none';
		        managerSelection.offsetHeight; // 강제로 리플로우(reflow) 발생
		        managerSelection.style.display = 'flex';
			}
			modal.style.display = "none";
		});

		initializeFlatpickr();
		initializeTaskStateButtons();
		initializeLowerTaskHandlers();
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
                    <button type="button" class="update_btn" style="margin:0; padding:0; color:#555;">프로젝트 모든 구성원</button>
                </div>
                <div class="task_icon_box1" style="display:flex; flex-direction:column; align-items:flex-start;">
                	<div style="display:flex; width:100%;">
	                    <img src="https://flow.team/flow-renewal/assets/images/icons/icon-post-place.svg?v=4475443d9f93bbbc02e906dbdac60bcfd1fd021d" style="margin-right : 16px;"/>
	                    <input type="text" placeholder="장소를 입력하세요" id="place" name="place" style="width:100%;"/>
					</div>                	
                    <div id="map" style="height: 200px; width: 100%; margin-top: 10px; display:none;"></div>
                    <!-- Hidden fields to store additional place information -->
			        <input type="hidden" id="place-id" name="place_id">
					<input type="hidden" id="place-lat" name="place_lat">
					<input type="hidden" id="place-lng" name="place_lng">
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

		const todoContent = `
        <div class="input-group1">
            <input type="text" id="title" name="title" placeholder="제목을 입력하세요." />
        </div>
        <div class="input-group2" style="max-height: 400px;">
            <div class="todo_icon_box2">
                <img src="https://flow.team/flow-renewal/assets/images/icons/icon-post-worker.svg?v=2bd86654bf591d842c49c9a76d76c11f1507ce8d" style="margin-right: 16px;"/>
                <button type="button" class="update_btn" name="addmanager" id="addmanager">담당자 추가</button>
            </div>
            <div class="todo_icon_box1" style="margin-bottom:30px;">
                <img src="https://flow.team/flow-renewal/assets/images/icons/icon-post-date.svg?v=cfae9e268527a6f7007fe81fd49cab2c9659eea3" style="margin-right:16px;"/>
                <span id="selectedDateText" style="display:none; font-size:14px;"></span>
                <input type="hidden" id="hiddenSelectedDate" name="selectedDate">
                <button type="button" class="update_btn" id="endDate" name="endDate">마감일 추가</button>
                <div id="div_calendar_modal" class="calendar-modal">
                    <div class="calendar-modal-content">
                        <span class="close1">&times;</span>
                        <input type="text" id="datepicker" placeholder="날짜를 선택하세요">
                        <button id="selectDate">선택</button>
                    </div>
                </div>
            </div>
            <div class="div_todo_edit">
                <input type="text" placeholder="할 일 추가 (Enter 또는 Tab) / 최대 60자" class="todo_input" id="todoNames" name="todoNames"/>
            </div>
        </div>
    `;
		modalContent.innerHTML = todoContent;

		document.getElementById("addmanager").addEventListener("click", function(event) {
			event.stopPropagation();
			var modal = document.getElementById("div_add_manager_modal");
			modal.style.display = "block";
			modal.style.top = '237px';
		});
		
		document.getElementById('managerForm').addEventListener('submit', function(event) {
			event.preventDefault();
			var modal = document.getElementById("div_add_manager_modal");
			const selectedManager = document.querySelector('.manager_item[data-id]');
			if (selectedManager) {
				const id = selectedManager.getAttribute('data-id');
				const name = selectedManager.querySelector('.manager_name').innerText;

				// 담당자 추가 버튼을 숨기고 선택된 담당자를 표시하는 HTML 추가
				const managerSelection = document.querySelector('.todo_icon_box2');
				console.log(managerSelection);
				managerSelection.innerHTML = `
                <img src="https://flow.team/flow-renewal/assets/images/icons/icon-post-worker.svg?v=2bd86654bf591d842c49c9a76d76c11f1507ce8d" style="margin-right: 16px;"/>
                <span class="manager_item">
                    <span class="selected_profile">
                        <img src="https://flow.team/flow-renewal/assets/images/profile-default.png" />
                    </span>
                    <span class="manager_name">${name}</span>
                </span>
                <input type="hidden" name="managerIdx" value="${id}">
            `;
			}
			modal.style.display = "none";
		});

		initializeFlatpickr();
		initializeTodoInputHandlers();
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
    
    // 프로젝트 멤버 추가(초대)
    // Modal functions for inviting participants
    const inviteButton = document.querySelector('#div_invite_prjmem img');
    const nothingMemberButton = document.querySelector('.nothing_btn');
    const inviteModal = document.getElementById("invite-participants-modal");
    const closeInviteButton = document.querySelector('.invite-modal-header .close');
    const inviteModalBtn = document.querySelector('.invite-modal-footer .invite_submit_btn');
	
	if(nothingMemberButton != null){
		nothingMemberButton.addEventListener('click', openInviteModal);
	}
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
            toggleInviteSelectedList(id, name);
        });
    });

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


	///////////////////////// 댓글 관련 //////////////////////////////////////
    // 댓글 등록
    document.querySelectorAll('#commentInput').forEach(commentInput => {
        const submitComment = document.getElementById('submitComment');

        function submitCommentFunction() {
            const postId = commentInput.getAttribute('data-post-id');
            const commentContent = commentInput.value.trim();
            if (commentContent) {
                fetch('/comments', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        post: {
                            postIdx: postId,
                        },
                        contents: commentContent
                    })
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        // 댓글 추가 후 처리 로직 (예: 댓글 리스트 갱신)
                        location.reload(); // 페이지를 새로고침하여 댓글 리스트 갱신
                    } else {
                        console.log(data);
                        alert('댓글 등록에 실패했습니다.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('댓글 등록 중 오류가 발생했습니다.');
                });
            }
        }

        commentInput.addEventListener('keydown', function(event) {
            if (event.key === 'Enter' && !event.shiftKey) {
                event.preventDefault();
                submitCommentFunction();
            }
        });

        submitComment.addEventListener('click', function() {
            submitCommentFunction();
        });
    });
    
    ////////////////////////  포스트 옵션 ///////////////////////////////
	// 포스트 옵션 (게시글 삭제, 게시글 수정)
	document.querySelectorAll('.post-option-toggle').forEach(function(toggle) {
		toggle.addEventListener('click', function() {
			const id = this.getAttribute('data-id');
			const optionUl = document.getElementById('postOptionUl' + id);

			// 모든 .post_option_ul 요소를 숨깁니다.
			document.querySelectorAll('.post_option_ul').forEach(function(ul) {
				ul.style.display = 'none';
			});

			// 클릭한 요소의 .post_option_ul을 표시합니다.
			optionUl.style.display = 'block';
		});
	});
    
    
    ////////////////////////  담당자 지정  ///////////////////////////////

    // 모달 외부를 클릭하면 닫히도록 설정

	document.querySelector('#div_backgroundfull').addEventListener('click', (event)=>{
		const managermodal = document.getElementById("div_add_manager_modal");
		 if(managermodal.style.display === '' ||managermodal.style.display === 'none'){
			document.querySelector('#postModal').style.display='none';
			document.querySelector('#div_backgroundfull').style.display='none';
		}
		if (!managermodal.contains(event.target) && managermodal.style.display === 'block') {
            managermodal.style.display = "none";
        }
       
	})
	document.querySelector('#postModal').addEventListener('click', (event) =>{
		const managermodal = document.getElementById("div_add_manager_modal");
		if (!managermodal.contains(event.target) && managermodal.style.display === 'block') {
            managermodal.style.display = "none";
        }
	})
    // 담당자 리스트에서 선택 시 처리
    
    function toggleAddManagerSelectedList(id, name) {
        console.log("toggleSelectedList 호출됨:", id, name); // 디버깅용 로그
        const managerList = document.querySelector('.selectManager');
        const existingSelectedManager = document.querySelector('.selectManager .manager_item');
        const existingItem = document.querySelector(`.can_manager_li[data-id="${id}"] .check_add_manager img`);

        // 기존 선택된 담당자가 있을 경우 해제
        if (existingSelectedManager) {
            const existingId = existingSelectedManager.getAttribute('data-id');
            const existingItemImg = document.querySelector(`.can_manager_li[data-id="${existingId}"] .check_add_manager img`);
            if (existingItemImg) {
                existingItemImg.src = 'https://flow.team/flow-renewal/assets/images/icons/icon_check.png?v=7f39425e224a53bff0043caff9f6446b14c0f667';
            }
            // 기존 선택된 담당자가 managerList의 자식 요소인 경우 제거
            if (managerList.contains(existingSelectedManager)) {
                managerList.removeChild(existingSelectedManager);
            }
        }

        // 새로운 담당자를 선택
        const newManager = document.createElement('span');
        newManager.classList.add('manager_item');
        newManager.setAttribute('data-id', id);
        newManager.style.display = 'inline-flex';
        newManager.innerHTML = `
            <span class="selected_profile">
                <img src="https://flow.team/flow-renewal/assets/images/profile-default.png" />
            </span>
            <span class="manager_name">${name}</span>
        `;
        managerList.appendChild(newManager);
        if (existingItem) {
            existingItem.src = 'https://flow.team/flow-renewal/assets/images/icons/icon_check_on.png?v=8a10086b9d33ff65ead56b67a69de154fcbe2c4a';
        }
    }

    document.querySelectorAll('.can_manager_li').forEach(item => {
        item.addEventListener('click', event => {
            const id = item.getAttribute('data-id');
            const name = item.getAttribute('data-name');
            toggleAddManagerSelectedList(id, name);
        });
    });
    
    // 선택된 담당자 초기화 
    function resetManagerSelection() {
            // 초기화 작업 수행
            const managerList = document.querySelector('.selectManager');
            while (managerList.firstChild) {
                managerList.removeChild(managerList.firstChild);
            }
            const checkedIcons = document.querySelectorAll('.can_manager_li .check_add_manager img');
            checkedIcons.forEach(icon => {
                icon.src = 'https://flow.team/flow-renewal/assets/images/icons/icon_check.png?v=7f39425e224a53bff0043caff9f6446b14c0f667';
            });
        }
	
	function observeTaskIconBox2() {
        const targetNode = document.body;
        const config = { childList: true, subtree: true };

        const callback = function(mutationsList, observer) {
            for (let mutation of mutationsList) {
                if (mutation.type === 'childList') {
                    const managerSelection = document.querySelector('.task_icon_box2');
                    if (managerSelection) {
                        console.log('task_icon_box2 element found');
                        observer.disconnect(); // 요소가 발견되면 observer를 중지합니다.
                        initializeFormSubmit();
                    }
                }
            }
        };

        const observer = new MutationObserver(callback);
        observer.observe(targetNode, config);
    }
	
	
	////////////////////// 구글 api ////////////////////////////
	let map;
	let marker;
	let autocomplete;
	let googleMapsScriptLoaded = false;

	function loadScript(src, callback) {
		if (document.querySelector(`script[src="${src}"]`)) {
			callback();
			return;
		}
		const script = document.createElement('script');
		script.src = src;
		script.async = true;
		script.defer = true;
		script.onload = callback;
		document.head.appendChild(script);
	}

	window.initialize = function() {
		console.log("Google Maps API initialized");
		initPostMaps();
		initAutocomplete();
	}

	function initAutocomplete() {
		const placeInput = document.getElementById('place');
		if (!placeInput) return;

		autocomplete = new google.maps.places.Autocomplete(placeInput);
		autocomplete.addListener('place_changed', handlePlaceSelect);
	}

	function handlePlaceSelect() {
		const place = autocomplete.getPlace();
		if (!place.geometry || !place.geometry.location) {
			alert("No details available for input: '" + place.name + "'");
			return;
		}

		document.getElementById('map').style.display = 'block';

		if (!map) {
			map = new google.maps.Map(document.getElementById('map'), {
				center: place.geometry.location,
				zoom: 17
			});
		} else {
			map.setCenter(place.geometry.location);
		}

		if (marker) {
			marker.setMap(null);
		}

		marker = new google.maps.Marker({
			position: place.geometry.location,
			map: map
		});

		document.getElementById('place-id').value = place.place_id || '';
		document.getElementById('place-lat').value = place.geometry.location.lat() || '';
		document.getElementById('place-lng').value = place.geometry.location.lng() || '';
	}

	function initPostMap(mapId, lat, lng) {
		console.log(`Initializing map for ${mapId} with lat: ${lat}, lng: ${lng}`);
		const mapElement = document.getElementById(mapId);
		if (!mapElement) {
			console.log(`Map element with id ${mapId} not found`);
			return;
		}

		const map = new google.maps.Map(mapElement, {
			center: { lat: lat, lng: lng },
			zoom: 17
		});

		new google.maps.Marker({
			position: { lat: lat, lng: lng },
			map: map
		});
	}

	function initPostMaps() {
		console.log("Initializing post maps");
		const mapElements = document.querySelectorAll('.place_map');
		mapElements.forEach(mapElement => {
			const mapId = mapElement.id;
			const lat = parseFloat(mapElement.getAttribute('data-lat'));
			const lng = parseFloat(mapElement.getAttribute('data-lng'));
			if (!isNaN(lat) && !isNaN(lng)) {
				initPostMap(mapId, lat, lng);
			} else {
				console.log(`Invalid lat/lng for mapId ${mapId}: ${lat}, ${lng}`);
			}
		});
	}

	document.querySelector('.tab-schedule').addEventListener('click', function() {
		setTimeout(() => {
			if (!googleMapsScriptLoaded) {
				loadScript('https://maps.googleapis.com/maps/api/js?key=AIzaSyBFrzV9XNxdBu76GcroDg3Fvun36TrvBlA&libraries=places&callback=initialize', () => {
					googleMapsScriptLoaded = true;
				});
			} else {
				initialize();
			}
		}, 500);
	});

	if (!googleMapsScriptLoaded) {
		loadScript('https://maps.googleapis.com/maps/api/js?key=AIzaSyBFrzV9XNxdBu76GcroDg3Fvun36TrvBlA&libraries=places&callback=initialize', () => {
			googleMapsScriptLoaded = true;
		});
	} else {
		initialize();
	}
	
});

function deletePost(postIdx) {
		if (confirm('정말 이 게시글을 삭제하시겠습니까?')) {
			fetch(`/${postIdx}`, {
				method: 'DELETE',
				headers: {
					'Content-Type': 'application/json'
				}
			})
				.then(response => {
					if (response.ok) {
						alert('게시글이 삭제되었습니다.');
						// 삭제 후 페이지 새로고침 또는 게시글 목록 갱신
						window.location.reload();
					} else {
						alert('게시글 삭제에 실패했습니다.');
					}
				})
				.catch(error => {
					console.error('Error:', error);
					alert('게시글 삭제 중 오류가 발생했습니다.');
				});
		}
	}
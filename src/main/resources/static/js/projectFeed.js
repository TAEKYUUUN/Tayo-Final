document.addEventListener('DOMContentLoaded', function() {
    const createPostArea = document.getElementById('create_post_area');
    const modal = document.querySelector('.modal');
    const backgroundFull = document.getElementById('div_backgroundfull');
    const closeBtn = document.querySelector('.close-btn');
    const tabs = document.querySelectorAll('.tabs');
    const tabTypeInput = document.getElementById('tabType');

	// Post 작성 modal 초기화
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
    
    // 할일 마감일추가 modal 초기화
	function initializeFlatpickrForTodo() {
		var datePicker = flatpickr("#datepicker", {
			enableTime: false,
			dateFormat: "Y-m-d",
		});

		var calendarmodal = document.getElementById("div_calendar_modal");
		var btn = document.getElementById("endDate");
		var selectedDateText = document.getElementById("selectedDateText");
		var hiddenSelectedDate = document.getElementById("hiddenSelectedDate");
		var span = document.getElementsByClassName("close1")[0];

		btn.onclick = function() {
			var calendarModalContent = document.querySelector(".calendar-modal-content");
			calendarModalContent.style.margin = "7.6% auto";
			calendarmodal.style.display = "block";
			datePicker.open(); // Open the date picker
		}

		span.onclick = function() {
			calendarmodal.style.display = "none";
		}

		window.onclick = function(event) {
			if (event.target == calendarmodal) {
				calendarmodal.style.display = "none";
			}
		}

		document.getElementById("selectDate").onclick = function(event) {
			event.preventDefault();
			var selectedDate = document.getElementById("datepicker").value;
			var dayOfWeek = new Date(selectedDate).toLocaleDateString('ko-KR', { weekday: 'short' });
			selectedDateText.textContent = `${selectedDate} (${dayOfWeek}) 까지`;
			selectedDateText.style.display = "inline";
			selectedDateText.style.marginLeft = '15px';
			btn.style.display = "none";
			hiddenSelectedDate.value = selectedDate;
			calendarmodal.style.display = "none";
		}
	}

	// 업무 마감일 추가 modal 초기화
	function initializeFlatpickrForTask() {
		var datePicker = flatpickr("#datepicker", {
			enableTime: false,
			dateFormat: "Y-m-d",
		});

		var calendarmodal = document.getElementById("div_calendar_modal");
		var btn = document.getElementById("endDate");
		var selectedDateText = document.getElementById("selectedDateText");
		var hiddenSelectedDate = document.getElementById("hiddenSelectedDate");
		var span = document.getElementsByClassName("close1")[0];

		btn.onclick = function() {
			calendarmodal.style.display = "block";
			datePicker.open();
		}

		span.onclick = function() {
			calendarmodal.style.display = "none";
		}

		window.onclick = function(event) {
			if (event.target == calendarmodal) {
				calendarmodal.style.display = "none";
			}
		}

		document.getElementById("selectDate").onclick = function(event) {
			event.preventDefault();
			var selectedDate = document.getElementById("datepicker").value;
			var dayOfWeek = new Date(selectedDate).toLocaleDateString('ko-KR', { weekday: 'short' });
			selectedDateText.textContent = `${selectedDate} (${dayOfWeek}) 까지`;
			selectedDateText.style.display = "inline";
			selectedDateText.style.marginLeft = '15px';
			btn.style.display = "none";
			hiddenSelectedDate.value = selectedDate;
			calendarmodal.style.display = "none";
		}
	}
	
	// Vote 마감일추가 modal 초기화
	function initializeFlatpickrForVote() {
		var datePicker = flatpickr("#datepicker", {
			enableTime: false,
			dateFormat: "Y-m-d",
		});

		var calendarmodal = document.getElementById("div_calendar_modal");
		var btn = document.getElementById("add_voteEndDate");
		var selectedDateText = document.getElementById("selectedDateText");
		var hiddenSelectedDate = document.getElementById("hiddenSelectedDate");
		var span = document.getElementsByClassName("close1")[0];

		btn.onclick = function() {
			var calendarModalContent = document.querySelector(".calendar-modal-content");
			calendarModalContent.style.margin = "36% 44%";
			calendarmodal.style.display = "block";
			span.style.right = '97px';
			datePicker.open(); // Open the date picker
		}

		span.onclick = function() {
			calendarmodal.style.display = "none";
		}

		window.onclick = function(event) {
			if (event.target == calendarmodal) {
				calendarmodal.style.display = "none";
			}
		}

		document.getElementById("selectDate").onclick = function(event) {
			event.preventDefault();
			var selectedDate = document.getElementById("datepicker").value;
			var dayOfWeek = new Date(selectedDate).toLocaleDateString('ko-KR', { weekday: 'short' });
			selectedDateText.textContent = `${selectedDate} (${dayOfWeek}) 까지`;
			selectedDateText.style.display = "inline";
			selectedDateText.style.marginLeft = '15px';
			btn.style.display = "none";
			hiddenSelectedDate.value = selectedDate;
			calendarmodal.style.display = "none";
		}
	}
	
	// Task 하위 업무 추가
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
	
	// Todo 할일 추가
	function initializeTodoInputHandlers() {
		function createNewTodoEdit() {
			const taskContainer = document.querySelector('.input-group2');
			const currentTasks = taskContainer.querySelectorAll('.div_todo_edit');

			if (currentTasks.length >= 5) {
				alert('할 일은 최대 5개까지만 추가할 수 있습니다.');
				return;
			}

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

			taskContainer.appendChild(newDiv);

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

	// 글 작성 클릭시 -> Post 작성 modal 열리게
    createPostArea.addEventListener('click', function() {
        modal.style.display = 'block';
        backgroundFull.style.display = 'block';
        resetModalFields();
        tabTypeInput.value = 1; // 기본값 설정
        updateModalContent(1);
        
        tabs.forEach(t => t.style = "");
        
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
	
	// Post = 'Paragraph'
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

	// Post = 'Task'
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
			modal.style.right = '881px';
			modal.style.top = '288px';
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

		initializeFlatpickrForTask();
		initializeTaskStateButtons();
		initializeLowerTaskHandlers();
	}

	// // Post = 'Schedule'
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

	// // Post = 'Todo'
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

		initializeFlatpickrForTodo();
		initializeTodoInputHandlers();
	}

	// Post = 'Vote'
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
                        <span id="selectedDateText" style="display:none; font-size:14px;"></span>
                        <input type="hidden" id="hiddenSelectedDate" name="selectedDate">
                        <button type="button" id="add_voteEndDate">마감일 추가</button>
                        <div id="div_calendar_modal" class="calendar-modal">
		                    <div class="calendar-modal-content">
		                        <span class="close1">&times;</span>
		                        <input type="text" id="datepicker" placeholder="날짜를 선택하세요">
		                        <button id="selectDate">선택</button>
		                    </div>
               			</div>
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
			const voteItemArea = document.querySelector('.input-group2');
			const currentVoteItems = voteItemArea.querySelectorAll('.div_voteitem_edit');

			if (currentVoteItems.length >= 5) {
				alert('투표 항목은 최대 5개까지만 추가할 수 있습니다.');
				return;
			}

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
			
			newInput.addEventListener('keydown', handleKeyDown);
        	newInput.focus();
		}
		
		function handleKeyDown(event) {
			if (event.key === 'Enter' || event.key === 'Tab') {
				event.preventDefault();
				createNewVoteItemEdit();
			}
		}

		document.querySelectorAll('.voteitem_input').forEach(input => {
			input.addEventListener('keydown', handleKeyDown);
		});

		addVoteitemBtn.addEventListener('click', createNewVoteItemEdit);
		
		initializeFlatpickrForVote();
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
    
    //////////////////////////// 프로젝트 멤버 초대(추가) ////////////////////////////////////
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


	//////////////////////////////////// 댓글 관련 //////////////////////////////////////
	// 댓글 등록, 삭제
	document.querySelectorAll('.cmt_input').forEach(commentInput => {
        const submitComment = commentInput.closest('.post_comment_input_area').querySelector('#submitComment');

        function fetchComments(postIdx, commentInput) {
            fetch(`/comments?postIdx=${postIdx}`)
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        updateCommentsList(data.comments, commentInput);
                        const commentsSection = commentInput.closest('.post_box');
                        if (commentsSection) {
                            updateCommentCount(commentsSection, data.comments.length);
                        }
                    } else {
                        console.error('댓글을 불러오는데 실패했습니다:', data);
                    }
                })
                .catch(error => {
                    console.error('오류:', error);
                });
        }

        function updateCommentsList(comments, commentInput) {
            const commentsSection = commentInput.closest('.post_box');
            if (!commentsSection) {
                console.error('comments-section을 찾을 수 없습니다.');
                return;
            }

            const commentsContainer = commentsSection.querySelector('.post_comment_list');
            if (!commentsContainer) {
                console.error('댓글 컨테이너를 찾을 수 없습니다.');
                return;
            }
            commentsContainer.innerHTML = '';

            comments.forEach(comment => {
                if (comment.member) {
                    const commentElement = document.createElement('li');
                    commentElement.classList.add('post_comment_item');
                    commentElement.innerHTML = `
                        <div class="comment_profile">
                            <img src="https://flow.team/flow-renewal/assets/images/profile-default.png" />
                        </div>
                        <div class="comment_container">
                            <div class="comment_user_area">
                                <span class="user_name">${comment.member.name}</span>
                                <span class="record_date">${new Date(comment.writeTime).toLocaleString()}</span>
                                <span class="comment_react_on" data-comment-id="${comment.commentsIdx}" style="${comment.isLiked ? '' : 'display:none;'}">
                                    <em class="comment_like_cancel on">좋아요 취소</em>
                                    ${comment.likeCount > 0 ? `<span class="comment_like_cnt">${comment.likeCount}</span>` : ''}
                                </span>
                                <span class="comment_react" data-comment-id="${comment.commentsIdx}" style="${comment.isLiked ? 'display:none;' : ''}">
                                    <em class="comment_like">좋아요</em>
                                    ${comment.likeCount > 0 ? `<span class="comment_like_cnt">${comment.likeCount}</span>` : ''}
                                </span>
                                <button class="delete_comment_btn" data-comment-id="${comment.commentsIdx}" style="${String(comment.member.memberIdx) === String(user.id) ? '' : 'display:none;'}">삭제</button>
                            </div>
                            <div>${comment.contents}</div>
                        </div>
                    `;
                    commentsContainer.appendChild(commentElement);
                } else {
                    console.error('comment.member is null or undefined', comment);
                }
            });
        }

        function updateCommentCount(commentsSection, count) {
            const commentCountElement = commentsSection.querySelector('#commentCount');
            if (commentCountElement) {
                commentCountElement.textContent = count;
            } else {
                console.error('commentCountElement를 찾을 수 없습니다.');
            }
        }

        function submitCommentFunction() {
            const postIdx = commentInput.getAttribute('data-post-id');
            const commentContent = commentInput.value.trim();
            if (commentContent) {
                const payload = {
                    postIdx: postIdx,
                    contents: commentContent
                };

                console.log('Sending payload:', JSON.stringify(payload)); // 디버깅을 위해 추가

                fetch('/comments', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(payload)
                })
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status}`);
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.success) {
                        fetchComments(postIdx, commentInput); // 댓글을 불러와서 업데이트
                        commentInput.value = ''; // 입력 필드 초기화
                        const commentsSection = commentInput.closest('.post_box');
                        if (commentsSection) {
                            const currentCount = parseInt(commentsSection.querySelector('#commentCount').textContent, 10);
                            updateCommentCount(commentsSection, currentCount + 1); // 댓글 수 증가
                        }
                    } else {
                        console.log(data);
                        alert('댓글 등록에 실패했습니다.');
                    }
                })
                .catch(error => {
                    console.error('오류:', error);
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

    // 이벤트 위임을 사용하여 삭제 버튼 이벤트 리스너 추가
    document.body.addEventListener('click', function(event) {
        if (event.target.classList.contains('delete_comment_btn')) {
            event.stopPropagation(); // 이벤트 전파 중지
            event.preventDefault(); // 기본 동작 막기
            const commentId = event.target.getAttribute('data-comment-id');
            const postBox = event.target.closest('.post_box');
            const commentInput = postBox.querySelector('.cmt_input');
            deleteComment(commentId, commentInput);
        }
    }, true); // 캡처링 단계에서 이벤트 리스너 추가
	

	////////////////////////////////////////  포스트 옵션 ////////////////////////////////////////////
	
	// 포스트 옵션 (게시글 삭제, 게시글 수정)
	document.querySelectorAll('.post-option-toggle').forEach(function(toggle) {
		toggle.addEventListener('click', function(event) {
			event.stopPropagation(); // 이벤트 버블링을 막습니다.
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

	document.addEventListener('click', function(event) {
		document.querySelectorAll('.post_option_ul').forEach(function(ul) {
			ul.style.display = 'none';
		});
	});

	// ul 내부를 클릭했을 때 이벤트가 버블링되지 않도록.
	document.querySelectorAll('.post_option_ul').forEach(function(ul) {
		ul.addEventListener('click', function(event) {
			event.stopPropagation();
		});
	});

	// 게시글 수정 버튼 클릭 이벤트 리스너
	document.querySelectorAll('.post_option_li').forEach(function(item) {
		item.addEventListener('click', function() {
			if (this.textContent.trim() === '게시글 수정') {
				const postIdx = this.closest('ul').getAttribute('data-post-idx');
				const projectIdx = this.closest('ul').getAttribute('data-project-idx');
				backgroundFull.style.display = 'block';
				openEditModal(projectIdx, postIdx);
			}
		});
	});

	// 모달 닫기 버튼 클릭 이벤트 리스너
	document.querySelectorAll('.close-btn').forEach(function(btn) {
		btn.addEventListener('click', function() {
			document.getElementById('postReviseModal').style.display = 'none';
			document.getElementById('postModal').style.display = 'none';
			backgroundFull.style.display = 'none';
		});
	});

	// 하위 업무 상태에 따른 클래스 반환
	function getTaskConditionClass(condition) {
		switch (condition) {
			case '1': return 'request';
			case '2': return 'progress';
			case '3': return 'feedback';
			case '4': return 'completion';
			case '5': return 'hold';
			default: return 'request';
		}
	}

	// 하위 업무 상태에 따른 텍스트 반환
	function getTaskConditionText(condition) {
		switch (condition) {
			case '1': return '요청';
			case '2': return '진행';
			case '3': return '피드백';
			case '4': return '완료';
			case '5': return '보류';
			default: return '요청';
		}
	}
	
	// Task 수정 시 담당자 변경 Modal
	function initializeManagerModalForEditTask() {
		const modal = document.getElementById("div_add_manager_modal");
		const button = document.getElementById("editManager");
		const managerNameElement = document.getElementById("managerName");
		const managerIdxElement = document.getElementById("managerIdx");

		button.addEventListener("click", function(event) {
			event.stopPropagation();
			modal.style.display = "block";
			if (position) {
				modal.style.right = '846px';
				modal.style.top = '241px';
			}
		});

		document.getElementById("managerForm").addEventListener("submit", function(event) {
			event.preventDefault();
			const selectedManager = document.querySelector('.manager_item[data-id]');
			if (selectedManager) {
				const id = selectedManager.getAttribute('data-id');
				const name = selectedManager.querySelector('.manager_name').innerText;
				managerIdxElement.value = id;
				managerNameElement.innerText = name;
			}
			modal.style.display = "none";
		});

		window.onclick = function(event) {
			if (event.target == modal) {
				modal.style.display = "none";
			}
		};
	}

	// Task 수정 시 마감일 변경 modal
	function initializeEndDateModalForEditTask() {
		const modal = document.getElementById("div_calendar_modal");
		const button = document.getElementById("endDate");
		const datePicker = document.getElementById("taskDatepicker");
		const selectedDateText = document.getElementById("selectedDateText");
		const taskEndDateSpan = document.getElementById("taskEndDate");

		console.log(taskEndDateSpan);
		
		flatpickr(datePicker, {
			enableTime: false,
			dateFormat: "Y-m-d",
		});

		button.onclick = function() {
			modal.style.display = "block";
			modal.style.left = '34px';
			modal.style.top = '159px';
			datePicker._flatpickr.open();
		};

		modal.querySelector(".close1").onclick = function() {
			modal.style.display = "none";
		};

		window.onclick = function(event) {
			if (event.target == modal) {
				modal.style.display = "none";
			}
		};

		document.getElementById("selectDate").onclick = function(event) {
			event.preventDefault();
			const selectedDate = datePicker.value;
			const dayOfWeek = new Date(selectedDate).toLocaleDateString('ko-KR', { weekday: 'short' });
			selectedDateText.textContent = `${selectedDate} (${dayOfWeek}) 까지`;
			selectedDateText.style.display = "inline";
			selectedDateText.style.marginLeft = '15px';
			button.style.display = "none";
			taskEndDateSpan.style.display = 'none';
			hiddenSelectedDate.value = selectedDate;
			modal.style.display = "none";
		};
	}


	// Todo 수정 시 담당자 modal
	function initializeManagerModalForEditTodo() {
		const modal = document.getElementById("div_add_manager_modal");
		const button = document.getElementById("todoEditManager");
		const managerNameElement = document.getElementById("todoManagerName");
		const managerIdxElement = document.getElementById("todoManagerIdx");

		button.addEventListener("click", function(event) {
			event.stopPropagation();
			modal.style.display = "block";
			modal.style.right = '846px';
			modal.style.top = '189px';
		});

		document.getElementById("managerForm").addEventListener("submit", function(event) {
			event.preventDefault();
			const selectedManager = document.querySelector('.manager_item[data-id]');
			if (selectedManager) {
				const id = selectedManager.getAttribute('data-id');
				const name = selectedManager.querySelector('.manager_name').innerText;
				managerIdxElement.value = id;
				managerNameElement.innerText = name;
			}
			modal.style.display = "none";
		});

		window.onclick = function(event) {
			if (event.target == modal) {
				modal.style.display = "none";
			}
		};
	}

	// Todo 수정 시 마감일 변경 Modal
	function initializeEndDateModalForEditTodo() {
		const modal = document.getElementById("todoDiv_calendar_modal");
		const button = document.getElementById("todoEndDate");
		const datePicker = document.getElementById("todoDatepicker");
		const selectedDateText = document.getElementById("todoSelectedDateText");
		const todoDeadLineSpan = document.getElementById("todoDeadLine");
		
		console.log(todoDeadLineSpan);
		flatpickr(datePicker, {
			enableTime: false,
			dateFormat: "Y-m-d",
		});

		button.onclick = function() {
			modal.style.display = "block";
			modal.style.left = '32px';
			modal.style.top = '106px';
			datePicker._flatpickr.open();
		};

		modal.querySelector(".close1").onclick = function() {
			modal.style.display = "none";
		};

		window.onclick = function(event) {
			if (event.target == modal) {
				modal.style.display = "none";
			}
		};

		document.getElementById("todoSelectDate").onclick = function(event) {
			event.preventDefault();
			const selectedDate = datePicker.value;
			const dayOfWeek = new Date(selectedDate).toLocaleDateString('ko-KR', { weekday: 'short' });
			selectedDateText.textContent = `${selectedDate} (${dayOfWeek}) 까지`;
			selectedDateText.style.display = "inline";
			selectedDateText.style.marginLeft = '15px';
			button.style.display = "none";
			todoDeadLineSpan.style.display = "none";
			modal.style.display = "none";
		};
	}

	// Task 수정 시 하위 업무 추가 및 핸들러 초기화
	function initializeLowerTaskHandlersForEditTask() {
		document.getElementById('addLowerTask').addEventListener('click', function() {
			addLowerTask();
		});

		function addLowerTask(name = '', condition = '1') {
			const taskContainer = document.querySelector('#taskContainer .subtask_ul');
			const lowerTasks = taskContainer.querySelectorAll('.subtask_li');

			if (lowerTasks.length >= 5) {
				alert('하위 업무는 최대 5개까지만 추가할 수 있습니다.');
				return;
			}

			const newTaskLi = document.createElement('li');
			newTaskLi.className = 'subtask_li';

			newTaskLi.innerHTML = `
            <div class="subtask_li_div" style="width:95%;">
                <input type="hidden" class="lowertask_state_value" name="lowerTaskConditions" value="${condition}" />
                <button type="button" class="subtask_state_btn ${getTaskConditionClass(condition)}" value="${condition}">${getTaskConditionText(condition)}</button>
                <div class="subtask_input">
                    <input type="text" class="lowertask_name" name="lowerTaskNames" value="${name}" placeholder="업무명 입력(Enter로 업무 연속 등록 가능)" style="border:none;"/>
                </div>
                <button type="button" class="remove-btn" style="position: relative; right: -190px;">x</button>
            </div>
        `;

			const inputField = newTaskLi.querySelector('.lowertask_name');

			inputField.addEventListener('keypress', function(event) {
				if (event.key === 'Enter') {
					event.preventDefault();
					addLowerTaskAndFocus();
				}
			});

			newTaskLi.querySelector('.remove-btn').addEventListener('click', function() {
				newTaskLi.remove();
			});

			newTaskLi.querySelector('.subtask_state_btn').addEventListener('click', function() {
				const currentCondition = this.value;
				const newCondition = (currentCondition === '1') ? '2' : '1'; // Assuming you have only two conditions (1 and 2)
				this.value = newCondition;
				this.textContent = getTaskConditionText(newCondition);
				this.className = `subtask_state_btn ${getTaskConditionClass(newCondition)}`;
				newTaskLi.querySelector('.lowertask_state_value').value = newCondition;
			});

			taskContainer.appendChild(newTaskLi);

			return inputField;
		}

		function addLowerTaskAndFocus() {
			const newInput = addLowerTask();
			if (newInput) {
				newInput.focus();
			}
		}

		// 데이터 로드 후 하위 업무 추가
		function loadLowerTasks(lowerTasks) {
			const lowerTaskContainer = document.querySelector('#taskContainer .subtask_ul');
			lowerTaskContainer.innerHTML = '';
			lowerTasks.forEach(task => {
				addLowerTask(task.lowerTaskName, task.lowerTaskCondition);
			});
		}

		// 외부에서 호출할 수 있도록 loadLowerTasks 함수를 반환
		return {
			loadLowerTasks
		};
	}
	
	// Todo 수정 시 할 일 추가 및 핸들러 초기화
	function initializeTodoHandlersForEditTodo() {
		document.getElementById('addTodoButton').addEventListener('click', function() {
			addTodoItem();
		});

		function addTodoItem(name = '') {
			const todoListContainer = document.getElementById('todoListContainer');
			const todoItems = todoListContainer.querySelectorAll('.div_todo_edit');

			// 최대 5개 제한
			if (todoItems.length >= 5) {
				alert('할 일은 최대 5개까지 추가할 수 있습니다.');
				return null;
			}

			const todoDiv = document.createElement('div');
			todoDiv.className = 'div_todo_edit';

			todoDiv.innerHTML = `
            <input type="text" placeholder="할 일 추가 (Enter 또는 Tab) / 최대 60자" class="todo_input" name="todoNames" value="${name}" />
            <button type="button" class="removeTodoButton">x</button>
        `;

			todoListContainer.appendChild(todoDiv);

			todoDiv.querySelector('.removeTodoButton').addEventListener('click', function() {
				todoDiv.remove();
			});

			const inputField = todoDiv.querySelector('.todo_input');
			inputField.addEventListener('keypress', function(event) {
				if (event.key === 'Enter') {
					event.preventDefault();
					addTodoItemAndFocus();
				}
			});

			return inputField;
		}

		function addTodoItemAndFocus() {
			const newInput = addTodoItem();
			if (newInput) {
				newInput.focus();
			}
		}

		// 데이터 로드 후 할 일 추가
		function loadTodoItems(todoItems) {
			const todoListContainer = document.getElementById('todoListContainer');
			todoListContainer.innerHTML = '';
			todoItems.forEach(item => {
				addTodoItem(item.todoName, item.isFinished);
			});
		}

		// 외부에서 호출할 수 있도록 loadTodoItems 함수를 반환
		return {
			loadTodoItems
		};
	}

	
	// 게시물 수정 클릭 시 모달 열기 함수
	function openEditModal(projectIdx, postIdx) {
		fetch(`/projectFeed/${projectIdx}/edit/${postIdx}`)
			.then(response => response.json())
			.then(data => {
				document.getElementById('projectIdx').value = projectIdx;
				document.getElementById('postIdx').value = postIdx;
				document.getElementById('editTabType').value = data.fileType;
				document.getElementById('editOpenRange').value = data.openRange;

				document.getElementById('paragraphFields').style.display = 'none';
				document.getElementById('taskFields').style.display = 'none';
				document.getElementById('scheduleFields').style.display = 'none';
				document.getElementById('todoFields').style.display = 'none';
				document.getElementById('voteFields').style.display = 'none';

				if (data.fileType === 1) { // Paragraph
					document.getElementById('paragraphFields').style.display = 'block';
					document.getElementById('editTitle').value = data.title;
					document.getElementById('editContent').value = data.content;
				} else if (data.fileType === 2) { // Task
					document.getElementById('taskFields').style.display = 'block';
					document.getElementById('editTaskTitle').value = data.taskName;
					document.getElementById('editTaskContent').value = data.content;
					document.getElementsByName('condition').forEach(btn => {
						if (btn.value == data.condition) {
							btn.classList.add('active');
						} else {
							btn.classList.remove('active');
						}
					});

					const managerNameElement = document.getElementById('managerName');
					if (managerNameElement) {
						managerNameElement.innerText = data.managerName;
					} else {
						console.error('Manager name element not found.');
					}

					document.getElementById('managerIdx').value = data.managerIdx;
					document.getElementById('taskEndDate').innerText = new Date(data.taskEndDate).toISOString().slice(0, 10);

					const taskHandlers = initializeLowerTaskHandlersForEditTask();
					taskHandlers.loadLowerTasks(data.lowerTasks);

					initializeTaskStateButtons();
					initializeManagerModalForEditTask();
					initializeEndDateModalForEditTask();
				} else if (data.fileType === 3) { // Schedule
					document.getElementById('scheduleFields').style.display = 'block';
					document.getElementById('title').value = data.title;
					document.getElementById('start').value = new Date(data.startDatetime).toISOString().slice(0, 16);
					document.getElementById('end').value = new Date(data.endDatetime).toISOString().slice(0, 16);
					document.getElementById('place').value = data.place;
					document.getElementById('content').value = data.content;
					document.getElementById('place-id').value = data.place_id;
					document.getElementById('place-lat').value = data.place_lat;
					document.getElementById('place-lng').value = data.place_lng;

					// 장소 및 지도 초기화
					if (data.place_lat && data.place_lng) {
						initPostMap('map', parseFloat(data.place_lat), parseFloat(data.place_lng));
					}

					// Google Maps API 로드 및 초기화
					if (!googleMapsScriptLoaded) {
						loadScript('https://maps.googleapis.com/maps/api/js?key=YOUR_API_KEY&libraries=places&callback=initialize', () => {
							googleMapsScriptLoaded = true;
						});
					} else {
						initialize();
					}
				} else if (data.fileType === 4) { // Todo
					document.getElementById('todoFields').style.display = 'block';
					document.getElementById('todoTitle').value = data.title;
					document.getElementById('todoManagerName').innerText = data.managerName;
					document.getElementById('todoManagerIdx').value = data.managerIdx;
					document.getElementById('todoDeadLine').innerText = new Date(data.deadLine).toISOString().slice(0, 10);

					const todoHandlers = initializeTodoHandlersForEditTodo();
					todoHandlers.loadTodoItems(data.todoNames);

					initializeManagerModalForEditTodo();
					initializeEndDateModalForEditTodo();
				} else if (data.fileType === 5) { // Vote
					document.getElementById('voteFields').style.display = 'block';
					document.getElementById('voteDetail').value = data.voteDetail;
					document.getElementById('voteEnddate').value = new Date(data.voteEnddate).toISOString().slice(0, 10);
					document.getElementById('isplural').checked = data.isplural;
					document.getElementById('isanonymous').checked = data.isanonymous;
					const voteItemsContainer = document.getElementById('voteItems');
					voteItemsContainer.innerHTML = '';
					data.voteItems.forEach(item => {
						const itemDiv = document.createElement('div');
						itemDiv.className = 'vote-item';
						itemDiv.innerHTML = `<input type="text" name="voteItems" value="${item.itemName}" />`;
						voteItemsContainer.appendChild(itemDiv);
					});
				}

				document.getElementById('postReviseModal').style.display = 'block';
			})
			.catch(error => console.error('Error:', error));
	}


	function initializeTaskStateButtons() {
		document.querySelectorAll('.task_state_btn').forEach(button => {
			button.addEventListener('click', function() {
				document.querySelectorAll('.task_state_btn').forEach(btn => btn.classList.remove('active'));
				this.classList.add('active');
				document.getElementById('hiddenCondition').value = this.value;
			});
		});
	}

	// 폼 제출 이벤트 리스너 추가
	document.getElementById('postEditForm').addEventListener('submit', function(event) {
		event.preventDefault();

		const formData = new FormData(this);
		const projectIdx = document.getElementById('projectIdx').value;
		const postIdx = document.getElementById('postIdx').value;
		const tabType = document.getElementById('editTabType').value;

		console.log('projectIdx:', projectIdx);
		console.log('postIdx:', postIdx);
		console.log('tabType:', tabType);

		if (!projectIdx || !postIdx || !tabType) {
			alert('프로젝트 ID, 게시글 ID 또는 탭 타입이 설정되지 않았습니다.');
			return;
		}

		let data = {
			projectIdx,
			postIdx,
			tabType
		};

		if (tabType === '1') {
			data.title = formData.get('title') || document.getElementById('editTitle').value;
			data.content = formData.get('content') || document.getElementById('editContent').value;
		} else if (tabType === '2') {
			data.title = formData.get('title') || document.getElementById('editTaskTitle').value;
			data.content = formData.get('content') || document.getElementById('editTaskContent').value;
			data.condition = formData.get('condition') || document.querySelector('.task_state_btn.active').value;
			data.managerIdx = formData.get('managerIdx') || document.getElementById('managerIdx').value;
			data.selectedDate = formData.get('taskDatepicker') || document.getElementById('taskEndDate').innerText;
			data.lowerTaskConditions = [];
			data.lowerTaskNames = [];

			formData.forEach((value, key) => {
				if (key.startsWith('lowerTaskConditions')) {
					data.lowerTaskConditions.push(value);
				} else if (key.startsWith('lowerTaskNames')) {
					data.lowerTaskNames.push(value);
				}
			});
		} else if (tabType === '3') {
			data.title = formData.get('title') || document.getElementById('title').value;
			data.startDatetime = formData.get('startDatetime') || document.getElementById('start').value;
			data.endDatetime = formData.get('endDatetime') || document.getElementById('end').value;
			data.place = formData.get('place') || document.getElementById('place').value;
			data.place_id = formData.get('place_id') || document.getElementById('place-id').value;
			data.place_lat = formData.get('place_lat') || document.getElementById('place-lat').value;
			data.place_lng = formData.get('place_lng') || document.getElementById('place-lng').value;
			data.content = formData.get('content') || document.getElementById('content').value;

			data.startDatetime = new Date(data.startDatetime).getTime();
			data.endDatetime = new Date(data.endDatetime).getTime();
		} else if (tabType === '4') {
			data.title = formData.get('todoTitle') || document.getElementById('title').value;
			data.managerIdx = formData.get('todoManagerIdx') || document.getElementById('todoManagerIdx').value;
			data.deadLine = formData.get('todoDatepicker') || document.getElementById('todoDeadLine').value;
			data.todoNames = [];
			formData.forEach((value, key) => {
				if (key.startsWith('todoNames')) {
					data.todoNames.push(value);
				}
			});
		} else if (tabType === '5') {
			data.title = formData.get('title') || document.getElementById('title').value;
			data.voteDetail = formData.get('voteDetail') || document.getElementById('vote_detail').value;
			data.voteEnddate = formData.get('voteEnddate') || document.getElementById('voteEnddate').value;
			data.isplural = formData.get('isplural') || document.querySelector('input[name="isplural"]:checked').value;
			data.isanonymous = formData.get('isanonymous') || document.querySelector('input[name="isanonymous"]:checked').value;
			data.voteItems = [];
			formData.forEach((value, key) => {
				if (key.startsWith('voteItems')) {
					data.voteItems.push(value);
				}
			});
		}

		console.log('Collected Data:', JSON.stringify(data));
		const url = `/projectFeed/${projectIdx}/edit/${postIdx}`;
		fetch(url, {
			method: 'PUT',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(data)
		})
			.then(response => {
				if (!response.ok) {
					return response.json().then(error => { throw new Error(error.message); });
				}
				return response.json();
			})
			.then(data => {
				if (data.success) {
					document.getElementById('postReviseModal').style.display = 'none';
					location.reload();
				} else {
					alert('게시글 수정에 실패했습니다.');
				}
			})
			.catch(error => console.error('Error:', error));
	});
	
	
    /////////////////////////////////////  담당자 지정  ///////////////////////////////////////

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

	
	////////////////////////////////////// 구글 api ////////////////////////////////////
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
	
	///////////////////////////////////////////// 댓글, 포스트 반응 ///////////////////////////////////////////
	const postReactions = document.querySelectorAll('.post_reaction');

    postReactions.forEach(postReaction => {
        postReaction.addEventListener('click', function(event) {
            event.stopPropagation();
            const postReactSelection = this.closest('.post_bottom_area').querySelector('.post_react_selection');
            postReactSelection.style.display = 'block';
        });
    });

	document.addEventListener('click', function(event) {
		postReactions.forEach(postReaction => {
			const postReactSelection = postReaction.closest('.post_bottom_area').querySelector('.post_react_selection');
			if (!postReactSelection.contains(event.target) && !postReaction.contains(event.target)) {
				postReactSelection.style.display = 'none';
			}
		});
	});

	//////////////////////////////////////// 댓글 좋아요 설정 //////////////////////////////////////////
	const commentReacts = document.querySelectorAll('.comment_react');

	commentReacts.forEach(commentReact => {
		commentReact.addEventListener('click', async function() {
			const commentIdx = this.getAttribute('data-comment-id');

			try {
				const response = await fetch(`/checkCommentLike/${commentIdx}`, {
					method: 'POST',
					headers: {
						'Content-Type': 'application/json'
					},
					body: JSON.stringify({}) // 빈 바디 전송
				});

				const data = await response.json();
				if (data.success) {
					const nowComment = commentReact.closest('.comment_container').querySelector('.comment_react');
					const newComment = commentReact.closest('.comment_container').querySelector('.comment_react_on');
					const nowlikeCnt = nowComment.querySelector('.comment_like_cnt');
					const newlikeCnt = newComment.querySelector('.comment_like_cnt');

					if (nowlikeCnt) {
						nowlikeCnt.textContent = parseInt(nowlikeCnt.textContent) + 1;
						if (newlikeCnt) {
							newlikeCnt.textContent = nowlikeCnt.textContent;
						} else {
							const newSpan = document.createElement('span');
							newSpan.classList.add('comment_like_cnt');
							newSpan.textContent = nowlikeCnt.textContent;
							newComment.appendChild(newSpan);
						}
					} else {
						const newSpan = document.createElement('span');
						newSpan.classList.add('comment_like_cnt');
						newSpan.textContent = 1;
						newComment.appendChild(newSpan);
					}
					nowComment.style.display = 'none';
					newComment.style.display = 'inline'; // 스타일을 명확히 설정합니다.
				} else {
					alert('좋아요를 처리하는데 실패했습니다. 에러 메시지: ' + (data.error || '알 수 없는 오류'));
				}
			} catch (error) {
				console.error('Error:', error);
				alert('좋아요를 처리하는데 실패했습니다.');
			}
		});
	});

	///////////////////////////////////////// 댓글 좋아요 취소 //////////////////////////////////////////////
	const commentReactsCancel = document.querySelectorAll('.comment_react_on');

	commentReactsCancel.forEach(commentReactCancel => {
		commentReactCancel.addEventListener('click', async function() {
			const commentIdx = this.getAttribute('data-comment-id');

			try {
				const response = await fetch(`/cancelCommentLike/${commentIdx}`, {
					method: 'DELETE',
					headers: {
						'Content-Type': 'application/json'
					},
					body: JSON.stringify({}) // 빈 바디 전송
				});

				const data = await response.json();
				if (data.success) {
					const newComment = commentReactCancel.closest('.comment_container').querySelector('.comment_react');
					const nowComment = commentReactCancel.closest('.comment_container').querySelector('.comment_react_on');
					const nowlikeCnt = nowComment.querySelector('.comment_like_cnt');
					const newlikeCnt = newComment.querySelector('.comment_like_cnt');

					if (parseInt(nowlikeCnt.textContent) === 1) {
						if (newlikeCnt) newlikeCnt.remove();
						nowlikeCnt.remove();
					} else {
						nowlikeCnt.textContent = parseInt(nowlikeCnt.textContent) - 1;
						if (newlikeCnt) {
							newlikeCnt.textContent = nowlikeCnt.textContent;
						} else {
							const newSpan = document.createElement('span');
							newSpan.classList.add('comment_like_cnt');
							newSpan.textContent = nowlikeCnt.textContent;
							newComment.appendChild(newSpan);
						}
					}
					nowComment.style.display = 'none';
					newComment.style.display = 'inline'; // 스타일을 명확히 설정합니다.
				} else {
					alert('좋아요 취소를 처리하는데 실패했습니다. 에러 메시지: ' + (data.error || '알 수 없는 오류'));
				}
			} catch (error) {
				console.error('Error:', error);
				alert('좋아요 취소를 처리하는데 실패했습니다.');
			}
		});
	});
	
	////////////////////////////////////////// 할 일 체크 ///////////////////////////////////////////////
	const iconCheckBoxes = document.querySelectorAll('.icon_checkbox');
	const projectIdx = document.querySelector('#projectIdx').value;
	
	iconCheckBoxes.forEach(function(checkbox) {
		checkbox.addEventListener('click', function() {
			const postIdx = checkbox.previousElementSibling.value;
			const todoItem = this.closest('.todo_item');
			if (!todoItem) {
				console.error('todo_item element not found');
				return;
			}

			const todonameIdx = todoItem.getAttribute('data-todo-name-id');
			if (!todonameIdx) {
				console.error('data-todo-name-id attribute not found');
				return;
			}

			const isDone = this.classList.contains('on') ? 0 : 1;

			fetch(`/todo/update/${todonameIdx}?isDone=${isDone}`, {
				method: 'PUT',
				headers: {
					'Content-Type': 'application/x-www-form-urlencoded',
				},
			})
				.then(response => {
					if (!response.ok) {
						return response.json().then(data => {
							throw new Error(data.message || 'Failed to update todo status');
						});
					}
					return response.json();
				})
				.then(data => {
					if (data.success) {
						this.classList.toggle('on');
							window.location.href = projectIdx + "#postIdx_" + postIdx;
					} else {
						alert(data.message);
					}
				})
				.catch(error => {
					console.error(error);
				});
		});
	});

	
// 'DOMContentLoaded' 끝나는 시점
});


///////////////////////////////////// 게시글 삭제 //////////////////////////////////////
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

///////////////////////////// Task Condition 변경 ///////////////////////////////////////
function updateTaskCondition(button, newCondition) {
    const postIdx = Number(button.closest('.task_icon_box1').getAttribute('data-post-idx'));
    const nowActiveButton = button.closest('.task_icon_box1').querySelector('.active');
    const request = button.closest('.task_icon_box1').querySelector('.request');
    const progress = button.closest('.task_icon_box1').querySelector('.progress');
    const feedback = button.closest('.task_icon_box1').querySelector('.feedback');
    const completion = button.closest('.task_icon_box1').querySelector('.completion');
    const hold = button.closest('.task_icon_box1').querySelector('.hold');

	// 현재 active 상태의 조건을 확인
	let currentCondition;
	if (request.classList.contains('active')) currentCondition = 1;
	else if (progress.classList.contains('active')) currentCondition = 2;
	else if (feedback.classList.contains('active')) currentCondition = 3;
	else if (completion.classList.contains('active')) currentCondition = 4;
	else if (hold.classList.contains('active')) currentCondition = 5;

	// 새로운 상태와 현재 상태가 같으면 아무 것도 하지 않음
	if (currentCondition === newCondition) {
		return;
	}
	
    fetch('/updateTaskCondition', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ postIdx: postIdx, newCondition: newCondition })
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            return response.json().then(err => { throw new Error(err.message); });
        }
    })
    .then(data => {
        alert(data.message);
        switch (newCondition) {
            case 1:
                nowActiveButton.classList.remove('active');
                request.classList.add('active');
                break;
            case 2:
                nowActiveButton.classList.remove('active');
                progress.classList.add('active');
                break;
            case 3:
                nowActiveButton.classList.remove('active');
                feedback.classList.add('active');
                break;
            case 4:
                nowActiveButton.classList.remove('active');
                completion.classList.add('active');
                break;
            case 5:
                nowActiveButton.classList.remove('active');
                hold.classList.add('active');
                break;
        }
    })
    .catch(error => {
        alert(error.message);
        console.error('Error:', error);
    });
}

document.querySelectorAll('.your-button-class').forEach(button => {
    button.addEventListener('click', function() {
        const newCondition = this.getAttribute('data-new-condition');
        updateTaskCondition(this, newCondition);
    });
});

////////////////////////////// Schedule Attendance 변경 /////////////////////////////////
function checkScheduleAttendance(button, attendance) {
    // 이미 'on' 클래스가 있는 버튼을 클릭한 경우 동작 중지
    if (button.classList.contains('on')) {
        return;
    }

    const postIdx = button.closest('.attend_button_area').getAttribute('data-post-idx');

    fetch('/checkScheduleAttendance', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `postIdx=${postIdx}&attendance=${attendance}`
    })
    .then(response => response.json())
    .then(data => {
        alert(data.message);

        // 모든 버튼에서 'on' 클래스 제거
        const buttons = button.closest('.attend_button_area').querySelectorAll('.attend_btn');
        buttons.forEach(btn => btn.classList.remove('on'));

        // 클릭된 버튼에 'on' 클래스 추가
        button.classList.add('on');
    })
    .catch(error => {
        alert('Failed to update attendance status.');
        console.error('Error:', error);
    });
}

function deleteComment(commentId, commentInput) {
	if (confirm('정말로 이 댓글을 삭제하시겠습니까?')) {
		fetch(`/comments/${commentId}`, {
			method: 'DELETE',
			headers: {
				'Content-Type': 'application/json',
			}
		})
			.then(response => {
				if (!response.ok) {
					throw new Error(`HTTP error! status: ${response.status}`);
				}
				return response.json();
			})
			.then(data => {
				console.log('Received response:', data); // 디버깅을 위해 추가
				if (data.success) {
					// 댓글을 삭제한 후 댓글 목록을 다시 불러옵니다.
					const postIdx = commentInput.getAttribute('data-post-id');
					fetchComments(postIdx, commentInput);
				} else {
					console.error('댓글 삭제에 실패했습니다:', data);
					alert('댓글 삭제에 실패했습니다.');
				}
			})
			.catch(error => {
				console.error('댓글 삭제 중 오류가 발생했습니다:', error);
				alert('댓글 삭제 중 오류가 발생했습니다.');
			});
	}
}

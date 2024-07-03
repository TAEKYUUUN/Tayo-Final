document.addEventListener('DOMContentLoaded', function() {
    const createPostArea = document.getElementById('create_post_area');
    const modal = document.querySelector('.modal');
    const backgroundFull = document.getElementById('div_backgroundfull');
    const closeBtn = document.querySelector('.close-btn');
    const tabs = document.querySelectorAll('.tabs');

    createPostArea.addEventListener('click', function() {
        modal.style.display = 'block';
        backgroundFull.style.display = 'block';
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
        });
    });

    const taskTab = document.querySelector('.tab-task');
    taskTab.addEventListener('click', function() {
        const modalContent = document.querySelector('.modal-content');
        modalContent.style.minHeight = '592px';
        modalContent.style.overflowY = 'auto';
        modal.style.height = '780px';
        
        const taskContent = `
        <div class="input-group1">
            <input type="text" id="title" name="title" placeholder="제목을 입력하세요." />
        </div>
        <div class="input-group2" style="max-height: 400px;">
            <div class="task_icon_box1">
                <img src="https://flow.team/flow-renewal/assets/images/icons/icon-post-status.svg?v=b3a5b7f86f05f658d1ce954c34f8c33a61ea8873" style="margin-right: 10px;"/>
                <button type="button" class="task_state_btn request active">요청</button>
                <button type="button" class="task_state_btn progress">진행</button>
                <button type="button" class="task_state_btn feedback">피드백</button>
                <button type="button" class="task_state_btn completion">완료</button>
                <button type="button" class="task_state_btn hold">보류</button>
            </div>
            <div class="task_icon_box2">
                <img src="https://flow.team/flow-renewal/assets/images/icons/icon-post-worker.svg?v=2bd86654bf591d842c49c9a76d76c11f1507ce8d" style="margin-right: 16px;"/>
                <button type="button" class="update_btn">담당자 추가</button>
            </div>
            <div class="task_icon_box1">
                <img src="https://flow.team/flow-renewal/assets/images/icons/icon-post-date.svg?v=cfae9e268527a6f7007fe81fd49cab2c9659eea3" style="margin-right:16px;"/>
                <button type="button" class="update_btn">마감일 추가</button>
            </div>
            <div class="div_textarea" style="height:270px;">
                <textarea id="content" name="content" placeholder="내용을 입력하세요." style="height:270px;"></textarea>
            </div>
            <div class="subtask_header">
                <span class="subtask_title" style="font-size:14px;">
                <img src="img/subtask_icon.png"/>
                    하위업무
                </span>
            </div>
            <div id="taskContainer">              
                <button type="button" class="add_lowertask" id="addLowerTask">추가</button>
            </div>
        </div>
        `;
        document.querySelector('.modal-content').innerHTML = taskContent;

        const taskButtons = document.querySelectorAll('.task_state_btn');
        taskButtons.forEach(button => {
            button.addEventListener('click', function() {
                taskButtons.forEach(btn => btn.classList.remove('active'));
                this.classList.add('active');
            });
        });

        document.getElementById('addLowerTask').addEventListener('click', function() {
            const newTaskDiv = document.createElement('div');
            newTaskDiv.className = 'input_lowertask';
            const requestButton = document.createElement('button');
            requestButton.type = 'button';
            requestButton.className = 'lowertask_state_btn request active';
            requestButton.textContent = '요청';
            newTaskDiv.appendChild(requestButton);
            const inputField = document.createElement('input');
            inputField.type = 'text';
            inputField.className = 'lowertask_name';
            inputField.placeholder = '업무명 입력(Enter로 업무 연속 등록 가능)';
            newTaskDiv.appendChild(inputField);
            const taskContainer = document.getElementById('taskContainer');
            taskContainer.insertBefore(newTaskDiv, document.getElementById('addLowerTask'));
        });
    });

    const paragraphTab = document.querySelector('.tab-paragraph');
    paragraphTab.addEventListener('click', function() {
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
        document.querySelector('.modal-content').innerHTML = paragraphContent;
    });

    const scheduleTab = document.querySelector('.tab-schedule');
    scheduleTab.addEventListener('click', function() {
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
                <input type="datetime-local" id="start" name="start"/>&ensp;-&ensp; 
                <input type="datetime-local" id="end" name="end"/>
                <input type="checkbox" id="allDay" name="allDay"/>
                <label for="allDay">종일</label>
            </div>
            <div class="task_icon_box2">
                <img src="https://flow.team/flow-renewal/assets/images/icons/icon-post-worker.svg?v=2bd86654bf591d842c49c9a76d76c11f1507ce8d" style="margin-right: 16px;"/>
                <button type="button" class="update_btn">담당자 추가</button>
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
        document.querySelector('.modal-content').innerHTML = scheduleContent;

        document.getElementById('allDay').addEventListener('change', function() {
            const startInput = document.getElementById('start');
            const endInput = document.getElementById('end');
            if (this.checked) {
                startInput.type = 'date';
                endInput.type = 'date';
            } else {
                startInput.type = 'datetime-local';
                endInput.type = 'datetime-local';
            }
        });
    });

    const todoTab = document.querySelector('.tab-todo');
    todoTab.addEventListener('click', function() {
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
                <input type="text" placeholder="할 일 추가 (Enter 또는 Tab) / 최대 60자" class="todo_input" id="todoName" name="todoName"/>
                <span class="todo_manager_profile">
                    <i><img src="https://flow.team/flow-renewal/assets/images/profile-default.png" alt="Profile" /></i>
                </span>
            </div>
        </div>
        `;
        document.querySelector('.modal-content').innerHTML = todoContent;

        function createNewTodoEdit() {
            const newDiv = document.createElement('div');
            newDiv.className = 'div_todo_edit';

            const newInput = document.createElement('input');
            newInput.type = 'text';
            newInput.placeholder = '할 일 추가 (Enter 또는 Tab) / 최대 60자';
            newInput.className = 'todo_input';
            newInput.name = 'todoName';

            const newSpan = document.createElement('span');
            newSpan.className = 'todo_manager_profile';

            const newIcon = document.createElement('i');
            const newImg = document.createElement('img');
            newImg.src = 'https://flow.team/flow-renewal/assets/images/profile-default.png';
            newImg.alt = 'Profile';

            newIcon.appendChild(newImg);
            newSpan.appendChild(newIcon);

            const removeButton = document.createElement('button');
            removeButton.textContent = 'x';
            removeButton.className = 'remove-btn';
            removeButton.addEventListener('click', () => {
                newDiv.remove();
            });

            newDiv.appendChild(newInput);
            newDiv.appendChild(newSpan);
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
    });

    const voteTab = document.querySelector('.tab-vote');
    voteTab.addEventListener('click', function() {
        const modalContent = document.querySelector('.modal-content');
        modalContent.style.minHeight = '592px';
        modalContent.style.overflowY = 'auto';
        modal.style.height = '780px';

        const voteContent = `
        <div class="input-group1">
            <input type="text" id="title" name="title" placeholder="제목을 입력하세요." />
        </div>
        <div class="input-group2" style="max-height: 400px;">
            <input type="text" id="vote_detail" name="vote_detail" placeholder="투표에 관한 설명 입력(옵션)"/>
            <div class="div_voteitem_edit">
                <input type="text" placeholder="항목 추가 (Enter 또는 Tab) / 최대 60자" class="voteitem_input" id="voteitem" name="voteitem"/>
            </div>
            <div class="div_voteitem_edit">
                <input type="text" placeholder="항목 추가 (Enter 또는 Tab) / 최대 60자" class="voteitem_input" id="voteitem" name="voteitem"/>
            </div>
            <div class="voteContainer">              
                <button type="button" class="add_voteitem" id="addVoteitem">투표 항목 추가</button>
            </div>
            <div class="vote_option_container">
                <div class="endDate vote_option">
                    <div class="option_name">
                        <img src="img/vote_enddate.png"/>
                        <span>투표 마감일</span>
                    </div>
                    <button type="button" id="add_voteEndDate">마감일 추가</button>
                </div>
                <div class="pluralVote vote_option">
                    <div class="option_name">
                        <img src="img/vote_doublecheck.png"/>
                        <span>복수 투표</span>
                    </div>
                    <label class="switch" style="margin-top:4px;">
                        <input type="checkbox" name="pluralVote" value="1" checked>
                        <span class="slider round"></span>
                    </label>
                </div>
                <div class="anonymousVote vote_option">
                    <div class="option_name">
                        <img src="img/vote_anonymous.png"/>
                        <span>익명 투표</span>
                    </div>
                    <label class="switch" style="margin-top:4px;">
                        <input type="checkbox" name="anonymousVote" value="1" checked>
                        <span class="slider round"></span>
                    </label>
                </div>
            </div>
        </div>
        `;
        document.querySelector('.modal-content').innerHTML = voteContent;

        const voteContainer = document.querySelector('.voteContainer');
        const addVoteitemBtn = document.getElementById('addVoteitem');

        function createNewVoteItemEdit() {
            const newDiv = document.createElement('div');
            newDiv.className = 'div_voteitem_edit';

            const newInput = document.createElement('input');
            newInput.type = 'text';
            newInput.placeholder = '항목 추가 (Enter 또는 Tab) / 최대 60자';
            newInput.className = 'voteitem_input';
            newInput.name = 'voteitem';

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
    });
    
    // 왼쪽 사이드 바 '새 프로젝트' 클릭 시 createNewProject로 이동
    document.getElementById('div_new_project').addEventListener('click', function() {
        var href = this.getAttribute('data-href');
        location.href = href;
    });

    // 왼쪽 사이드 바 '대시보드' 클릭 시 dashboard로 이동
    document.getElementById('div_toDashboard').addEventListener('click', function() {
        var href = this.getAttribute('data-href');
        location.href = href;
    });

    // 왼쪽 사이드 바 '내 프로젝트' 클릭 시 projectlist로 이동
    document.getElementById('div_toProjectlist').addEventListener('click', function() {
        var href = this.getAttribute('data-href');
        location.href = href;
    });

    // 왼쪽 사이드 바 '회사 공개 프로젝트' 클릭 시 companyOpenProject로 이동
    document.getElementById('div_toCompanyOpenProject').addEventListener('click', function() {
        var href = this.getAttribute('data-href');
        location.href = href;
    });
});

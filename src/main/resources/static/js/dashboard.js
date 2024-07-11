

const alarmIsRead = function(alarmIdx) {
	$.ajax({
		url: "/AlarmRead",
		type: 'PUT',
		contentType: 'application/json',
		data:JSON.stringify(alarmIdx),
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
	
	postAlarm.forEach(div =>{
		div.parentElement.addEventListener('click', ()=>{
			const anchor = div.firstElementChild.value;
			const moveAfter = div.querySelectorAll('input')[1].value;
			const alarmIdx = div.querySelector('#alarmIdx').value;
			alarmIsRead(alarmIdx);
			div.parentElement.classList.remove('unreadAlarm');
			window.location.href = '/projectFeed/' + moveAfter +'#postIdx_' + anchor;
		})
	})
	projectAlarm.forEach(div =>{
		div.parentElement.addEventListener('click', ()=>{
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

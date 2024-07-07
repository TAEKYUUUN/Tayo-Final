document.addEventListener('DOMContentLoaded', function() {
    // 페이지 이동 관련 기능
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

    // 상단 div_profile에 시간에 따른 메시지
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

    // 위젯 메뉴 아이콘 클릭 시 크기 변경
    const widgetMenuIcons = document.querySelectorAll('.widget_menu_icon');
    widgetMenuIcons.forEach(function(widgetMenuIcon) {
        const changeWidgetSize = widgetMenuIcon.querySelector('.change_widget_size');
        widgetMenuIcon.addEventListener('click', function() {
            changeWidgetSize.style.display = (changeWidgetSize.style.display === 'none' || changeWidgetSize.style.display === '') ? 'block' : 'none';
        });
    });

    // 프로젝트 항목 클릭 시 projectFeed2로 이동
    const projectItems = document.querySelectorAll('.div_prj_item');
    projectItems.forEach(function(item) {
        item.addEventListener('click', function() {
            var href = this.getAttribute('data-href');
            var projectIdx = item.firstElementChild.value;
            location.href = href + '/' + projectIdx;
        });
    });

    // 채팅 창 기능
    const openChatWindowBtn = document.getElementById('open-chat-window-btn');
    const closeChatWindowBtn = document.getElementById('close-chat-window-btn');
    const chatWindow = document.getElementById('chat-window-js');
    const chatListItems = document.querySelectorAll('.chat-list-item');

    if (openChatWindowBtn && closeChatWindowBtn && chatWindow) {
        openChatWindowBtn.addEventListener('click', () => {
            chatWindow.style.display = 'flex';
        });

        closeChatWindowBtn.addEventListener('click', () => {
            chatWindow.style.display = 'none';
        });
    }

    function openChatWindow(chatRoomUrl) {
        window.open(chatRoomUrl, 'ChatWindow', 'width=450,height=800');
    }

    chatListItems.forEach((item) => {
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
});

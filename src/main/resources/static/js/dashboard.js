document.addEventListener('DOMContentLoaded', function() {
    // '새 프로젝트' 클릭 시 createNewProject로 이동
    document.getElementById('new_prj_default').addEventListener('click', function() {
        var href = this.getAttribute('data-href');
        location.href = href;
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

    // 상단 div_profile 에 시간에 따른 메시지
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
    const memberName = profileSpan.textContent.trim();
    profileSpan.textContent = memberName + getGreetingMessage();

    // 위젯 메뉴 아이콘 클릭 시 크기 변경
    const widgetMenuIcons = document.querySelectorAll('.widget_menu_icon');
    widgetMenuIcons.forEach(function(widgetMenuIcon) {
        const changeWidgetSize = widgetMenuIcon.querySelector('.change_widget_size');
        widgetMenuIcon.addEventListener('click', function() {
            if (changeWidgetSize.style.display === 'none' || changeWidgetSize.style.display === '') {
                changeWidgetSize.style.display = 'block';
            } else {
                changeWidgetSize.style.display = 'none';
            }
        });
    });

    // 프로젝트 항목 클릭 시 projectFeed2로 이동
    var projectItems = document.querySelectorAll('.div_prj_item');
    projectItems.forEach(function(item) {
        item.addEventListener('click', function() {
            var href = this.getAttribute('data-href');
            var projectIdx = item.firstElementChild.value;
            location.href = href + '/' + projectIdx;
        });
    });
});

// 왼쪽 side 바 '새 프로젝트' 클릭 시 createNewProejct로 이동
document.addEventListener('DOMContentLoaded', function () {
  document
    .getElementById('div_new_project')
    .addEventListener('click', function () {
      var href = this.getAttribute('data-href');
      location.href = href;
    });
});

// 왼쪽 side 바 '대시보드' 클릭 시 dashboard로 이동
document.addEventListener('DOMContentLoaded', function () {
  document
    .getElementById('div_toDashboard')
    .addEventListener('click', function () {
      var href = this.getAttribute('data-href');
      location.href = href;
    });
});

// 왼쪽 side 바 '내 프로젝트' 클릭 시 projectlist로 이동
document.addEventListener('DOMContentLoaded', function () {
  document
    .getElementById('div_toProjectlist')
    .addEventListener('click', function () {
      var href = this.getAttribute('data-href');
      location.href = href;
    });
});

// 왼쪽 side 바 '회사 공개 프로젝트' 클릭 시 companyOpenProject로 이동
document.addEventListener('DOMContentLoaded', function () {
  document
    .getElementById('div_toCompanyOpenProject')
    .addEventListener('click', function () {
      var href = this.getAttribute('data-href');
      location.href = href;
    });
});

// 상단 div_profile 에 시간에 따른 메시지
function getGreetingMessage() {
  const now = new Date();
  const hours = now.getHours();
  let message = '';

  if (hours >= 3 && hours < 7) {
    message = '님 잔잔한 새벽입니다.🌛';
  } else if (hours >= 7 && hours < 12) {
    message = '님 활기찬 오전입니다.🌞';
  } else if (hours >= 12 && hours < 20) {
    message = '님 즐거운 오후입니다.😊';
  } else {
    message = '님 아늑한 저녁입니다.✨';
  }

  return message;
}

document.addEventListener('DOMContentLoaded', function () {
  const profileSpan = document.querySelector('#div_profile span');
  const memberName = profileSpan.textContent.trim();
  profileSpan.textContent = memberName + getGreetingMessage();
});

document.addEventListener('DOMContentLoaded', function () {
  const widgetMenuIcons = document.querySelectorAll('.widget_menu_icon');

  widgetMenuIcons.forEach(function (widgetMenuIcon) {
    const changeWidgetSize = widgetMenuIcon.querySelector(
      '.change_widget_size'
    );

    widgetMenuIcon.addEventListener('click', function () {
      if (
        changeWidgetSize.style.display === 'none' ||
        changeWidgetSize.style.display === ''
      ) {
        changeWidgetSize.style.display = 'block';
      } else {
        changeWidgetSize.style.display = 'none';
      }
    });
  });
});

document.addEventListener('DOMContentLoaded', () => {
  const openChatWindowBtn = document.getElementById('open-chat-window-btn');
  const closeChatWindowBtn = document.getElementById('close-chat-window-btn');
  const chatWindow = document.getElementById('chat-window-js');
  const chatListItems = document.querySelectorAll('.chat-list-item');

  // 채팅 창 열기 버튼 클릭 이벤트 리스너 추가
  openChatWindowBtn.addEventListener('click', () => {
    chatWindow.style.display = 'flex';
  });

  // 채팅 창 닫기 버튼 클릭 이벤트 리스너 추가
  closeChatWindowBtn.addEventListener('click', () => {
    chatWindow.style.display = 'none';
  });

  // 채팅 방을 새 창으로 여는 함수
  function openChatWindow(chatRoomUrl) {
    window.open(chatRoomUrl, 'ChatWindow', 'width=450,height=800');
  }

  // 각 채팅 리스트 항목을 form 요소로 대체하고 이벤트 리스너 추가
  chatListItems.forEach((item) => {
	const chatIdx = item.querySelector('input').value;
    // 새로운 form 요소 생성
    const form = document.createElement('form');
    form.action = 'chatRoom/' + chatIdx; // 원하는 URL로 action 설정
    form.method = 'post'; // 원하는 HTTP 메서드로 method 설정

    // item의 자식 노드들을 form으로 이동
    while (item.firstChild) {
      form.appendChild(item.firstChild);
    }

    // item의 클래스 이름을 form에 복사
    form.className = item.className;

    // item을 form으로 대체
    item.parentNode.replaceChild(form, item);

    // form 요소에 click 이벤트 리스너 추가
    form.addEventListener('click', (event) => {
      event.preventDefault();
      openChatWindow(form.getAttribute('action')); // form의 action 속성을 URL로 사용
    });
  });
});

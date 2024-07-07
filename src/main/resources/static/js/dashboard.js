document.addEventListener('DOMContentLoaded', function() {
	
	document.querySelector('#unread').addEventListener('click', ()=>{
		document.querySelector('#allalarm').classList.remove('allOrUnread');
		document.querySelector('#unread').classList.add('allOrUnread');
	})
	document.querySelector('#allalarm').addEventListener('click', ()=>{
		document.querySelector('#unread').classList.remove('allOrUnread');
		document.querySelector('#allalarm').classList.add('allOrUnread');
	})
	
	document.querySelector('#div_top_alarm').addEventListener('click', ()=>{
		document.querySelector('#alarmPopup').style.removeProperty('display');
	})
	document.querySelector('#exitlogoedit').addEventListener('click', ()=>{
		document.querySelector('#alarmPopup').style.display='none';
	})
	
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

// 왼쪽 side 바 '새 프로젝트' 클릭 시 createNewProejct로 이동
document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('div_new_project').addEventListener('click', function() {
        var href = this.getAttribute('data-href');
        location.href = href;
    });
});

// 왼쪽 side 바 '대시보드' 클릭 시 dashboard로 이동
document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('div_toDashboard').addEventListener('click', function() {
        var href = this.getAttribute('data-href');
        location.href = href;
    });
});

// 왼쪽 side 바 '내 프로젝트' 클릭 시 projectlist로 이동
document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('div_toProjectlist').addEventListener('click', function() {
        var href = this.getAttribute('data-href');
        location.href = href;
    });
});

// 왼쪽 side 바 '회사 공개 프로젝트' 클릭 시 companyOpenProject로 이동
document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('div_toCompanyOpenProject').addEventListener('click', function() {
        var href = this.getAttribute('data-href');
        location.href = href;
    });
});
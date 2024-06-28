function setCheckboxValues() {
    var companyProjectCheckbox = document.getElementById('companyProject');
    var needConfirmCheckbox = document.getElementById('needConfirm');

    companyProjectCheckbox.value = companyProjectCheckbox.checked ? 1 : 0;
    needConfirmCheckbox.value = needConfirmCheckbox.checked ? 1 : 0;
 	alert("제출완료");
    return true;
}
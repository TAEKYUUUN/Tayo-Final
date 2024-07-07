document
	.getElementById('copyTextButton')
	.addEventListener('click', function() {
		// 복사할 텍스트가 있는 div 요소를 선택합니다.
		const textToCopy = document.querySelector('.text-to-copy').innerText;

		// 텍스트를 임시로 저장할 textarea 요소를 생성합니다.
		const tempTextArea = document.createElement('textarea');
		tempTextArea.value = textToCopy;

		// 임시 textarea 요소를 body에 추가합니다.
		document.body.appendChild(tempTextArea);

		// 텍스트를 선택하고 복사합니다.
		tempTextArea.select();
		document.execCommand('copy');

		// 임시 textarea 요소를 body에서 제거합니다.
		document.body.removeChild(tempTextArea);

		alert('텍스트가 복사되었습니다: ' + textToCopy);
	});
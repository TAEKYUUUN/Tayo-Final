document.addEventListener('DOMContentLoaded', ()=>{
			const copybutton = document.querySelector('#copybutton');
			const sendbutton = document.querySelector('#sendbutton');
			const toSend = document.querySelector('#toSend');
			const toSendDetail = toSend.querySelectorAll('li');
			
			toSendDetail.forEach(input => {
				input.addEventListener('change', (event)=>{
					let inputvalue = event.target.value;
					let email_regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i;
					if(email_regex.test(inputvalue) && inputvalue !== ""){
						event.target.classList.remove('errorMail');
						event.target.nextElementSibling.src="https://flow.team/design2/img_rn/memb2/ico_chk_ok.png"
					}else if(inputvalue===""){
						event.target.classList.remove('errorMail');
						event.target.nextElementSibling.src="";
					}else{
						event.target.classList.add('errorMail');
						event.target.nextElementSibling.src="https://flow.team/design2/img_rn/memb2/ico_chk_error.png"
					}
				})
			})
			
			sendbutton.addEventListener('click', (event)=>{
				let notEmailCount = 0;
				
				toSendDetail.forEach(input => {
					let inputvalue = input.querySelector('input').value;
					let email_regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i;
					if(!email_regex.test(inputvalue) && inputvalue !==""){
						notEmailCount++;
					}
				})
				
				if(notEmailCount === 0){
					toSendDetail.forEach(input =>{
						input.querySelector('input').value = "";
						input.querySelector('img').src = "";
					})
					alert('전송 완료');
				}else{
					alert('이메일 양식을 확인해주세요');
				}
			})
			
			copybutton.addEventListener('click', (event)=>{
				const inputElement = event.target.previousElementSibling;
				const inputValue= inputElement.value;
				
				navigator.clipboard.writeText(inputValue).then(()=>{
					alert("클립보드에 복사되었습니다.");
				}).catch(err =>{
					console.error('복사에 실패했습니다.', err)
				})
				
			})
			
		})
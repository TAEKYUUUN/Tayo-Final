
const rowCount = function(){
	let totalAmount = 0;
	let availableAmount = 0;
	let unavailableAmount = 0;
	const everyTr = document.querySelectorAll('tbody tr');
	
	everyTr.forEach(tr => {
		let name = tr.querySelector('input[name*="name"]');
		let email =  tr.querySelector('input[name*="email"]');
		totalAmount ++;
		
		if(!name.classList.contains("invalidValue") && !email.classList.contains("invalidValue")){
			availableAmount ++;
		}else{
			unavailableAmount ++;
		}
	});
	document.querySelector('#total_amount').innerHTML = '전체 ' + totalAmount + '건수';
	document.querySelector('#available_amount').innerHTML = '( ' + availableAmount + '개 등록 가능';
	document.querySelector('#unavailable_amount').innerHTML =  unavailableAmount + '건수';
}



document.addEventListener('DOMContentLoaded', ()=>{
		document.addEventListener('keydown', (event) => {
	       if (event.key === 'Enter') {
	           event.preventDefault();
	       }
	   });
	
			const selectAllCheckbox = document.querySelectorAll('#checkAllUser');
			const addrow = document.querySelector('#addrow')
			const deleterow = document.querySelector('#deleterow');
			
			const allowcancel = document.querySelector('#allowcancel');
			const allowsave = document.querySelector('#allowsave');
			const exitallow = document.querySelector('#exitallow');
			
			const addNewMember = document.querySelector('#addNewMember');
			
			document.querySelector('#allowsave').addEventListener('click', submitForm);
			
			addNewMember.addEventListener('click',(event)=>{
				const rowAmount = document.querySelectorAll('tbody td');
				let validtest = document.querySelectorAll('.invalidValue');
				
				
				if(rowAmount.length >0 && validtest.length ===0){
					document.querySelector('#div_backgroundfull').style.removeProperty('display');
					document.querySelector('#div_allowpopup').style.removeProperty('display');
				}else{
					alert('입력값을 다시 확인해주세요');	
				}
			})
			
			allowcancel.addEventListener('click',(event)=>{
				document.querySelector('#div_backgroundfull').style.display="none";
				document.querySelector('#div_allowpopup').style.display="none";
			});
			exitallow.addEventListener('click',(event)=>{
				document.querySelector('#div_backgroundfull').style.display="none";
				document.querySelector('#div_allowpopup').style.display="none";
			});
			allowsave.addEventListener('click',(event)=>{
				document.querySelector('#div_backgroundfull').style.display="none";
				document.querySelector('#div_allowpopup').style.display="none";

				
			});
			
			
			
			selectAllCheckbox.forEach(input =>{
				input.addEventListener('change', (event)=>{
					const selectedtable = input.closest('table');
					const everyinput = selectedtable.querySelectorAll('tbody input[type="checkbox"]');
					const ischecked = event.target.checked;
					
					everyinput.forEach(checkbox =>{
							checkbox.checked = ischecked;
					})
				})
				
			})
			let newCount = 0;
			addrow.addEventListener('click', (event) =>{
				const inserttable = document.querySelector('table');
				const inserttbody = document.querySelector('tbody');
				
				const emptyRow = inserttable.querySelector('tbody tr:empty, tbody tr:not(:has(td))');
				    if (emptyRow) {
				        emptyRow.remove();
				    }
				  
				let newRow = inserttbody.insertRow();
				let cells = [];
				let cell1 = newRow.insertCell();
				cell1.innerHTML = '<input type="checkbox">';
				let namecell = newRow.insertCell();
				namecell.innerHTML = '<input type="text" class="invalidValue" id="newName" name="name'+ newCount+'">';
				cells.push(namecell);
				let emailcell = newRow.insertCell();
				emailcell.innerHTML = '<input type="text" class="invalidValue" id="newEmail"name="email'+ newCount+'">';
				cells.push(emailcell);
				for(let i = 4; i<=7; i++){
					let cell = newRow.insertCell();
					cell.innerHTML = '<input type="text" name="col'+i+ newCount+'">';
					cells.push(cell);
				}
				
				const newName = document.querySelector('input[name="name'+newCount+'"]');
				const newEmail = document.querySelector('input[name="email'+newCount+'"]');
				newName.addEventListener('change', (event) =>{
					let inputvalue = event.target.value;
					if(inputvalue !== ""){
						event.target.classList.remove('invalidValue');
					}else{
						event.target.classList.add('invalidValue');
					}
					rowCount();
				});
				newEmail.addEventListener('change', (event) =>{
					let inputvalue = event.target.value;
					let email_regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i;
					if(email_regex.test(inputvalue)){
						event.target.classList.remove('invalidValue');
					}else{
						event.target.classList.add('invalidValue');
					}
					rowCount();
				});
				newCount ++;
				rowCount();
			})
			
			
			deleterow.addEventListener('click', (event) =>{
				const table = document.querySelector('table');
				const everyinput = table.querySelectorAll('tbody input[type="checkbox"]');
				let checkedelements = [];
				everyinput.forEach((input,index) =>{
					if(input.checked){
						checkedelements.push(input.closest('tr'));			
					}
				})
				checkedelements.forEach(row =>{
					row.parentNode.removeChild(row);
				})
				rowCount();
			})
		})
		
		const submitForm = function(event) {
		    event.preventDefault();
		    
		    const members = [];
		    const rows = document.querySelectorAll('#memberTable tbody tr');
		    
		    rows.forEach(row => {
		        const member = {
		            name: row.querySelector('input[name*="name"]').value,
		            email: row.querySelector('input[name*="email"]').value,
		            phone: row.querySelector('input[name*="col4"]').value,
					organization: {
						organizationIdx: 1,
						organizationName: row.querySelector('input[name*="col5"]').value
					},
		            rankName: row.querySelector('input[name*="col6"]').value,
		            phoneCompany: row.querySelector('input[name*="col7"]').value,
		        };
		        members.push(member);
		    });
		    
		    const data = members;
		    
		    fetch('/Admin/memberAddAll', {
		        method: 'POST',
		        headers: {
		            'Content-Type': 'application/json',
		        },
		        body: JSON.stringify(data), // corrected the typo
		    })
		    .then(response =>{ 
				if (response.redirected) {
				            window.location.href = response.url;
				        }
				 response.json()})
		    .then(data => {
		        console.log('Success:', data);
		    })
		    .catch((error) => {
		        console.log('Error:', error);
		    });
		};
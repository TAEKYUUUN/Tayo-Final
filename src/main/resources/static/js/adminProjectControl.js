document.addEventListener('DOMContentLoaded', () => {
	const everyProject = document.querySelectorAll('[id*="projectList"]')
	const exitbutton = document.querySelector('#exitbutton');
	const exitbutton2 = document.querySelector('#exitbutton2');
	const cancelbutton = document.querySelector('#cancelbutton');
	const cancelbutton2 = document.querySelector('#cancelbutton2');
	const memberLittle = document.querySelector('#memberLittle');

	memberLittle.addEventListener('click', () => {
		document.querySelector('#div_projectInfopopupSmall').style.removeProperty('display');
		document.querySelector('#div_backgroundfull').style.zIndex = '11500';
	})
	cancelbutton2.addEventListener('click', () => {
		document.querySelector('#div_backgroundfull').style.zIndex = '9000';
		document.querySelector('#div_projectInfopopupSmall').style.display = 'none';
	})
	exitbutton2.addEventListener('click', () => {
		document.querySelector('#div_backgroundfull').style.zIndex = '9000';
		document.querySelector('#div_projectInfopopupSmall').style.display = 'none';
	})
	exitbutton.addEventListener('click', () => {
		document.querySelector('#div_backgroundfull').style.display = 'none';
		document.querySelector('#div_projectInfopopup').style.display = 'none';
	})
	cancelbutton.addEventListener('click', () => {
		document.querySelector('#div_backgroundfull').style.display = 'none';
		document.querySelector('#div_projectInfopopup').style.display = 'none';
	})

	everyProject.forEach(tr => {
		tr.addEventListener('click', () => {
			document.querySelector('#div_backgroundfull').style.removeProperty('display');
			document.querySelector('#div_projectInfopopup').style.removeProperty('display');
			showProjectDetails(tr.firstElementChild.innerHTML);
		})
	})

})

function showProjectDetails(projectIdx) {

	$.ajax({
		url: "/Admin/adminProjectControl",
		type: 'POST',
		contentType: 'application/json',
		data: JSON.stringify({ projectIdx: projectIdx }),
		success: function(response) {
			document.querySelector('#outlinenone').value = response.projectName;
			const popupTbody = document.querySelectorAll('#popupTbody tr');
			const popupTbodysmall = document.querySelectorAll('#popupTbodysmall tr');
			popupTbody.forEach(tr => {
				tr.remove();
			})
			popupTbodysmall.forEach(tr => {
				tr.remove();
			})
			response.projectMemberList.forEach((e) => {
				if (e.isManager === 1) {
					let tr = document.createElement('tr');
					let td = document.createElement('td');
					let td2 = document.createElement('td');
					let td3 = document.createElement('td');
					let td4 = document.createElement('td');
					let td5 = document.createElement('td');
					let td6 = document.createElement('td');
					td.textContent = e.member.rankName;
					tr.appendChild(td);
					td2.textContent = e.member.name;
					tr.appendChild(td2);
					td3.textContent = e.member.email;
					tr.appendChild(td3);
					td4.textContent = e.member.organization.organizationName;
					tr.appendChild(td4);
					td5.textContent = '관리자 [해제]';
					td5.classList.add('noMoreManager');
					tr.appendChild(td5);
					document.querySelector('#popupTbody').appendChild(tr);
					td6.textContent = e.projectMemberIdx;
					td6.style.display='none';
					tr.appendChild(td6);
					
				} else {
					let tr = document.createElement('tr');
					let td = document.createElement('td');
					let td2 = document.createElement('td');
					let td3 = document.createElement('td');
					let td4 = document.createElement('td');
					let td5 = document.createElement('td');
					td.textContent = e.member.name;
					tr.appendChild(td);
					td2.textContent = e.member.email;
					tr.appendChild(td2);
					td3.textContent = 'N';
					tr.appendChild(td3);
				}

			})
			const noMoreManagerButton = document.querySelector('#popupTbody').querySelectorAll('.noMoreManager');
			noMoreManagerButton.forEach(td => {
				if(noMoreManagerButton.length >1){
					td.addEventListener('click', (event) => {
						event.stopPropagation();
						event.currentTarget.parentElement.remove();
					})
				}
			})
		},
		error: function(error) {
			alert(error.responseText);
			console.log(error.responseText);
		}
	});

}

const submitForm = function(event) {
		    event.preventDefault();
		    
		    const members = [];
		    const rows = document.querySelectorAll('#popupTbody tr');
		    
		    rows.forEach(row => {
		        const member = {
		            isManager: row.querySelector('td')[4].value,
					isManager: row.querySelector('td')[5].value,
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
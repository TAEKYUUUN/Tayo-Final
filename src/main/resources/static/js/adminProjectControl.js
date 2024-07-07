document.addEventListener('DOMContentLoaded', () => {
	const everyProject = document.querySelectorAll('[id*="projectList"]')
	const exitbutton = document.querySelector('#exitbutton');
	const exitbutton2 = document.querySelector('#exitbutton2');
	const cancelbutton = document.querySelector('#cancelbutton');
	const cancelbutton2 = document.querySelector('#cancelbutton2');
	const memberLittle = document.querySelector('#memberLittle');
	const savebutton = document.querySelector('#savebutton');
	
	savebutton.addEventListener('click', ()=>{
		submitForm();
	})
	
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
					let td7 = document.createElement('td');
					let td8 = document.createElement('td');
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
					td7.textContent = response.projectIdx;
					td7.style.display = 'none';
					tr.appendChild(td7);
					td8.textContent = e.member.memberIdx;
					td8.style.display='none';
					tr.appendChild(td8);
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
const submitForm = function() {
    const members = [];
    const rows = document.querySelectorAll('#popupTbody tr');
    rows.forEach(row => {
        const projectMember = {
            projectMemberIdx: row.querySelectorAll('td')[5].textContent,
            project:{
				projectName: document.querySelector('#outlinenone').value,
	            projectIdx: row.querySelectorAll('td')[6].textContent,
			},
            member: {
                memberIdx: row.querySelectorAll('td')[7].textContent,
            }
        };
        members.push(projectMember);
    });
    
    $.ajax({
        url: "/Admin/adminProjectControl",
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(members),
        success: function(response) {
			document.querySelector('#div_backgroundfull').style.display = 'none';
			document.querySelector('#div_projectInfopopup').style.display = 'none';
        },
        error: function(error) {
            console.log('Error:', error);
        }
    });
};
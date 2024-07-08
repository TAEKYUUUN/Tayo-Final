document.addEventListener('DOMContentLoaded', () => {
	const everyProject = document.querySelectorAll('[id*="projectList"]')
	const exitbutton = document.querySelector('#exitbutton');
	const exitbutton2 = document.querySelector('#exitbutton2');
	const cancelbutton = document.querySelector('#cancelbutton');
	const cancelbutton2 = document.querySelector('#cancelbutton2');
	const memberLittle = document.querySelector('#memberLittle');
	const savebutton = document.querySelector('#savebutton');
	let currentProjectElement = null;
	
	
	savebutton.addEventListener('click', ()=>{
		submitForm(currentProjectElement);
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
			currentProjectElement = tr;
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
					if(e.member.organization != null){
						td4.textContent = e.member.organization.organizationName;
					}
					tr.appendChild(td4);
					td5.innerHTML = '<span>관리자 [해제]</span>';
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
					let td6 = document.createElement('td');
					let td7 = document.createElement('td');
					let td8 = document.createElement('td');
					td.textContent = e.member.rankName;
					tr.appendChild(td);
					td.style.display = 'none';
					td2.textContent = e.member.name;
					tr.appendChild(td2);
					td3.textContent = e.member.email;
					tr.appendChild(td3);
					if (e.member.organization != null) {
						td4.textContent = e.member.organization.organizationName;
					}
					tr.appendChild(td4);
					td4.style.display = 'none';
					td5.innerHTML = '<span>비관리자 [선택]</span>';
					td5.classList.add('makeManager');
					tr.appendChild(td5);
					document.querySelector('#popupTbody').appendChild(tr);
					td6.textContent = e.projectMemberIdx;
					td6.style.display = 'none';
					tr.appendChild(td6);
					td7.textContent = response.projectIdx;
					td7.style.display = 'none';
					tr.appendChild(td7);
					td8.textContent = e.member.memberIdx;
					td8.style.display = 'none';
					tr.appendChild(td8);
					document.querySelector('#popupTbodysmall').appendChild(tr);
				}

			})
			 setEventListeners();
		},
		error: function(error) {
			alert(error.responseText);
			console.log(error.responseText);
		}
	});

}

function setEventListeners() {
    const noMoreManagerButtons = document.querySelectorAll('#popupTbody .noMoreManager');
    noMoreManagerButtons.forEach(td => {
        const span = td.querySelector('span');
        if (span) {
            const removeManager = function(event) {
                if (document.querySelector('#popupTbody').querySelectorAll('.noMoreManager').length > 1) {
                    noManager(event);
                    span.removeEventListener('click', removeManager);
                    addManagerEvent(td.parentElement);
                }
            };
            span.addEventListener('click', removeManager);
        }
    });

    const makeManagerButtons = document.querySelectorAll('#popupTbodysmall .makeManager');
    makeManagerButtons.forEach(td => {
        const span = td.querySelector('span');
        if (span) {
            const addManager = function(event) {
                yesManager(event);
                span.removeEventListener('click', addManager);
                removeManagerEvent(td.parentElement);
            };
            span.addEventListener('click', addManager);
        }
    });
}

function addManagerEvent(tr) {
    const span = tr.querySelector('span');
    if (span) {
        const addManager = function(event) {
            yesManager(event);
            span.removeEventListener('click', addManager);
            removeManagerEvent(tr);
        };
        span.addEventListener('click', addManager);
    }
}

function removeManagerEvent(tr) {
    const span = tr.querySelector('span');
    if (span) {
        const removeManager = function(event) {
            if (document.querySelector('#popupTbody').querySelectorAll('.noMoreManager').length > 1) {
                noManager(event);
                span.removeEventListener('click', removeManager);
                addManagerEvent(tr);
            }
        };
        span.addEventListener('click', removeManager);
    }
}

const yesManager = function(event) {
    const movingTarget = event.currentTarget.parentElement.parentElement;
    const movingTargetChild = movingTarget.children;
    movingTargetChild[4].innerHTML = '<span>관리자 [해제]</span>';
    movingTargetChild[4].classList.add('noMoreManager');
    movingTargetChild[4].classList.remove('makeManager');
    movingTargetChild[3].style.removeProperty('display');
    movingTargetChild[0].style.removeProperty('display');
    document.querySelector('#popupTbody').appendChild(movingTarget);
}

const noManager = function(event) {
    event.stopPropagation();
    const movingTarget = event.currentTarget.parentElement.parentElement;
    const movingTargetChild = movingTarget.children;
    movingTargetChild[4].innerHTML = '<span>비관리자 [선택]</span>';
    movingTargetChild[4].classList.remove('noMoreManager');
    movingTargetChild[4].classList.add('makeManager');
    movingTargetChild[3].style.display = 'none';
    movingTargetChild[0].style.display = 'none';
    document.querySelector('#popupTbodysmall').appendChild(movingTarget);
}


const submitForm = function(currentProjectElement) {
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
				name: row.querySelectorAll('td')[1].textContent,
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
			const targetTr = currentProjectElement.querySelectorAll('td');
			targetTr[2].innerHTML = '';
			for(let i = 0; i<response.length; i++){
				const newManager = document.createElement('div');
				const newManagerName = document.createElement('span');
				newManagerName.innerHTML = response[i].member.name;
				newManager.appendChild(newManagerName);
				targetTr[2].appendChild(newManager);
			}
			
        },
        error: function(error) {
            console.log('Error:', error);
        }
    });
};

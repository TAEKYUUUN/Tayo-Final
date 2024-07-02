document.addEventListener('DOMContentLoaded', () => {
	const addOrganization = document.querySelector('#addOrganization');
	const organizationList = document.querySelector('#div_orgcontents');
	const targetOrganization = document.querySelectorAll('#organizationLists');
	
	const openAllChild = document.querySelectorAll('#openAllChild');
	const closeAllChild = document.querySelectorAll('#closeAllChild');
	
	const changeOrganizationName = document.querySelector('#changeOrganizationName');
	
	const changedOrganizationName = document.querySelectorAll('#changedOrganizationName');
	
	document.addEventListener('keydown', (event)=> {
	    if (event.key === 'Enter') {
	        updateOrganizationName();
	    }
	})
	
	changedOrganizationName.forEach(input =>{
		input.addEventListener('change',  (event)=>{
			updateOrganizationName();
		})
	})
	
	changeOrganizationName.addEventListener('click', (event)=>{
		event.stopPropagation();
		const changeTarget = document.querySelector('.targetOrganization');
		if(changeTarget){
				changeTarget.querySelector('input').style.removeProperty('display');
				changeTarget.querySelector('p').style.display = "none";
				//document.addEventListener('click', handleClickOutside);

		}
	})
	
	openAllChild.forEach(button => {
		button.addEventListener('click', (event)=>{
			event.stopPropagation(); 
			event.currentTarget.parentElement.nextElementSibling.style.removeProperty('display');
			event.currentTarget.nextElementSibling.style.removeProperty('display');
			event.currentTarget.style.display="none";
		})
	})
	closeAllChild.forEach(button => {
		button.addEventListener('click', (event)=>{
			event.stopPropagation(); 
			event.currentTarget.parentElement.nextElementSibling.style.display="none";
			event.currentTarget.previousElementSibling.style.removeProperty('display');
			event.currentTarget.style.display="none";
		})
	})
	targetOrganization.forEach(li => {
		li.addEventListener('click', (event)=>{
			targetOrganization.forEach(li =>{
				li.classList.remove('targetOrganization');
			})
			event.currentTarget.classList.add('targetOrganization');
		});
	})
	
	addOrganization.addEventListener('click', ()=>{
	
		const newOrganization = document.createElement('div');
		newOrganization.innerHTML ='<input type="text" id="newOrgInput" style="margin-left:20px;">'
		if(organizationList.querySelector('span')){
			organizationList.querySelector('span').remove();
		}
		const direction = document.querySelector(".targetOrganization");
		if(!document.querySelector('#newOrgInput')){
			if(direction){
				direction.parentNode.appendChild(newOrganization);
			}else{
				document.querySelector('#organizationAllList').appendChild(newOrganization);
			}
		}
		document.querySelector('#newOrgInput').addEventListener('change', (event)=>{
			if(event.currentTarget.value !== "null"){
				let resultofistargeted = istargeted();
				let upperIdx = (resultofistargeted === undefined)?0:resultofistargeted;
				createNewOrganization(upperIdx);
			}
		});
	})	
})

const handleClickOutside = function (event) {
				    const specificArea = document.querySelector('.targetOrganization'); // 특정 영역의 클래스를 사용합니다
				    if (specificArea && !specificArea.contains(event.target)) {
				        // 특정 영역 이외를 클릭했을 때 실행할 코드
				        specificArea.querySelector('input').style.display = "none";
				        specificArea.querySelector('p').style.removeProperty('display');
				        // 이벤트 리스너를 제거합니다
				        document.removeEventListener('click', handleClickOutside);
				    }
				};


const istargeted = function () {
	const targetOrganization = document.querySelectorAll('#organizationLists');
	let targetIdx;
	targetOrganization.forEach(li => {
		if(li.classList.contains('targetOrganization')){
			targetIdx = li.querySelector('#organizationIdx').innerHTML;
		}
	})
	return targetIdx;
}

function updateOrganizationName(){
			const newName = document.querySelector('.targetOrganization').querySelector('input').value;
			var organizationData = {
					action:"update",
			        organizationName:newName,
					organizationIdx: document.querySelector('.targetOrganization').querySelector('#organizationIdx').innerHTML,
			 };
		    
		    $.ajax({
		        url:"/Admin/AdminOrganization",
		        type:'POST',
		        contentType:'application/json',
		        data:JSON.stringify(organizationData),
		        success:function(response){
					const changeTarget = document.querySelector('.targetOrganization');
					if(changeTarget){
						changeTarget.querySelector('input').style.display = "none";
						changeTarget.querySelector('p').innerHTML = newName;
						changeTarget.querySelector('p').style.removeProperty('display');
					}
		        },
		        error: function(error){
		            alert(error.responseText);
					console.log(error.responseText);
		        }
		    });
		}



function createNewOrganization(istargeted){
		    var organizationData = {
				action:"create",
		        organizationName: $("#newOrgInput").val(),
				upperOrganization: istargeted,
		    };
		    
		    $.ajax({
		        url:"/Admin/AdminOrganization",
		        type:'POST',
		        contentType:'application/json',
		        data:JSON.stringify(organizationData),
		        success:function(response){
					$("#newOrgInput").remove(); // 입력 필드를 제거합니다.
					
					let maxOrganization = response.reduce((prev, current) => (prev.organizationIdx > current.organizationIdx) ? prev : current);
					
					const newLi = document.createElement('li');
					const newUl = document.createElement('ul');
									  
					newLi.id = 'organization' + maxOrganization.organizationIdx;
					
					if(maxOrganization.upperOrganization == null){
						newLi.innerHTML = '<div id="organizationLists">' +
															  '<p>' + maxOrganization.organizationName + '</p>' +
															  '<p id="organizationIdx" style="display:none;">' + maxOrganization.organizationIdx + '</p>' +
															  '</div>';
						newUl.innerHTML = '<li class="organizationList" id="organization'+maxOrganization.organizationIdx+'">' +
					                             '<div id="organizationLists">' +
					                             '<p>' + maxOrganization.organizationName + '</p>' +
					                             '<p id="organizationIdx" style="display:none;">' + maxOrganization.organizationIdx + '</p>' +
					                             '</div>' +
					                             '</li>';
					}else{
						newLi.innerHTML = '<div><div><div id="organizationLists">' +
																  '<p>' + maxOrganization.organizationName + '</p>' +
																  '<p id="organizationIdx" style="display:none;">' + maxOrganization.organizationIdx + '</p>' +
																  '</div></div></div>';
						newUl.innerHTML = '<li id="organization'+maxOrganization.organizationIdx+'">' +
				                             '<div><div><div id="organizationLists">' +
				                             '<p>' + maxOrganization.organizationName + '</p>' +
				                             '<p id="organizationIdx" style="display:none;">' + maxOrganization.organizationIdx + '</p>' +
				                             '</div></div></div>' +
				                             '</li>';
						 newLi.style.paddingLeft = 0;
					}
					newUl.style.paddingLeft='20px';
					const direction = document.querySelector(".targetOrganization");
					
					if(direction){
	                  if(direction.parentElement.querySelector('ul')){
	                     direction.parentElement.querySelector('ul').appendChild(newLi);
	                  }else{
	                     direction.parentElement.append(newUl);
	                  }
	               }else{
					  newLi.classList.add('organizationList');
	                  document.querySelector('#organizationAllList').appendChild(newLi);
	               }
					const targetOrganization = document.querySelectorAll('#organizationLists');
						
						
						
						targetOrganization.forEach(li => {
							li.addEventListener('click', (event)=>{
								targetOrganization.forEach(li =>{
									li.classList.remove('targetOrganization');
								})
								event.currentTarget.classList.add('targetOrganization');
							});
						})
		        },
		        error: function(error){
		            alert(error.responseText);
					console.log(error.responseText);
		        }
		    });
		}
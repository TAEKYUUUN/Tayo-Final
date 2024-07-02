document.addEventListener('DOMContentLoaded', () => {
	const addOrganization = document.querySelector('#addOrganization');
	const organizationList = document.querySelector('#div_orgcontents');
	const targetOrganization = document.querySelectorAll('#organizationLists');
	
	const openAllChild = document.querySelectorAll('#openAllChild');
	const closeAllChild = document.querySelectorAll('#closeAllChild');
	
	const changeOrganizationName = document.querySelector('#changeOrganizationName');
	
	const changedOrganizationName = document.querySelectorAll('#changedOrganizationName');
	const deleteOrganization = document.querySelector('#deleteOrganization');
	const cancelOrganzation = document.querySelector('#cancelOrganzation');
	
	const clearOrganization = document.querySelector('#clearOrganization');
	
	clearOrganization.addEventListener('click', ()=>{
		clearOrganizations();
	})
	
	cancelOrganzation.addEventListener('click', ()=>{
		if(document.querySelector('#NewOrgDiv')){
			document.querySelector('#NewOrgDiv').remove();
		}
		const changeTarget = document.querySelector('.targetOrganization');
		if(changeTarget){
			changeTarget.querySelector('input').style.display = "none";
			changeTarget.querySelector('p').style.removeProperty('display');
			changeTarget.classList.remove('targetOrganization');
		}
	})
	
	deleteOrganization.addEventListener('click', ()=>{
		deleteTheOrganization();
	})
	
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
			const everyInput = document.querySelectorAll('changedOrganizationName');
			let changingNumber = 0;
			
			everyInput.forEach(input => {
				if(input.style.display !== 'none'){
					changingNumber ++;
				}
			})
			
			if(!document.querySelector('#newOrgInput') && changingNumber === 0){
				targetOrganization.forEach(li =>{
					li.classList.remove('targetOrganization');
				})
				event.currentTarget.classList.add('targetOrganization');
			}
		});
	})
	
	addOrganization.addEventListener('click', ()=>{
	
		const newOrganization = document.createElement('div');
		newOrganization.setAttribute('id', 'NewOrgDiv');
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


function clearOrganizations(){
			var organizationData = {
					action:"clear",
			 };
		    
		    $.ajax({
		        url:"/Admin/AdminOrganization",
		        type:'POST',
		        contentType:'application/json',
		        data:JSON.stringify(organizationData),
		        success:function(response){
					while (document.querySelector('#organizationAllList').firstChild) {
					       document.querySelector('#organizationAllList').removeChild(document.querySelector('#organizationAllList').firstChild);
					   }
					const noOrg = document.createElement('span');
					noOrg.innerHTML = '부서 등록이 되어 있지 않습니다.';
					document.querySelector('#div_orgcontents').appendChild(noOrg);
		        },
		        error: function(error){
		            alert(error.responseText);
					console.log(error.responseText);
		        }
		    });
		}






function deleteTheOrganization(){
			var organizationData = {
					action:"delete",
					organizationIdx: document.querySelector('.targetOrganization').querySelector('#organizationIdx').innerHTML,
			 };
		    
		    $.ajax({
		        url:"/Admin/AdminOrganization",
		        type:'POST',
		        contentType:'application/json',
		        data:JSON.stringify(organizationData),
		        success:function(response){
					if(document.querySelector('.targetOrganization').parentElement.tagName.toLowerCase() === 'div'){
						const todelete = document.querySelector('.targetOrganization').parentElement.parentElement.parentElement;
						if(todelete.parentElement.childElementCount === 1){
							todelete.parentElement.previousElementSibling.querySelector('button').remove();
							todelete.parentElement.previousElementSibling.querySelector('button').remove();
						}
						todelete.remove();
						
					}else{
						const todelete = document.querySelector('.targetOrganization').parentElement;
						if(todelete.parentElement.childElementCount === 1){
							todelete.parentElement.previousElementSibling.querySelector('button').remove();
							todelete.parentElement.previousElementSibling.querySelector('button').remove();
						}
						todelete.remove();
					}
		        },
		        error: function(error){
		            alert(error.responseText);
					console.log(error.responseText);
		        }
		    });
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
					$("#NewOrgDiv").remove(); // 입력 필드를 제거합니다.
					
					let maxOrganization = response.reduce((prev, current) => (prev.organizationIdx > current.organizationIdx) ? prev : current);
					
					const newLi = document.createElement('li');
					const newUl = document.createElement('ul');
									  
					newLi.id = 'organization' + maxOrganization.organizationIdx;
					let inputAndButton = '<input type = "text" style="display:none;" id="changedOrganizationName" value="'+maxOrganization.organizationName+'">';
					
					if(maxOrganization.upperOrganization == null){
						newLi.innerHTML = '<div id="organizationLists"  style="display:flex">' +
															  '<p>' + maxOrganization.organizationName + '</p>' +
															  '<p id="organizationIdx" style="display:none;">' + maxOrganization.organizationIdx + '</p>' +
															  inputAndButton +
															  '</div>';
						newUl.innerHTML = '<li class="organizationList" id="organization'+maxOrganization.organizationIdx+'">' +
					                             '<div id="organizationLists" style="display:flex">' +
					                             '<p>' + maxOrganization.organizationName + '</p>' +
					                             '<p id="organizationIdx" style="display:none;">' + maxOrganization.organizationIdx + '</p>' +
												 inputAndButton +
					                             '</div>' +
					                             '</li>';
					}else{
						newLi.innerHTML = '<div><div><div id="organizationLists"  style="display:flex">' +
																  '<p>' + maxOrganization.organizationName + '</p>' +
																  '<p id="organizationIdx" style="display:none;">' + maxOrganization.organizationIdx + '</p>' +
																  inputAndButton +
																  '</div></div></div>';
						newUl.innerHTML = '<li id="organization'+maxOrganization.organizationIdx+'">' +
				                             '<div><div><div id="organizationLists" style="display:flex">' +
				                             '<p>' + maxOrganization.organizationName + '</p>' +
				                             '<p id="organizationIdx" style="display:none;">' + maxOrganization.organizationIdx + '</p>' +
											 inputAndButton +
				                             '</div></div></div>' +
				                             '</li>';
						 newLi.style.paddingLeft = 0;
					}
					newUl.style.paddingLeft='20px';
					const direction = document.querySelector(".targetOrganization");
					const newplus = document.createElement('button');
					const newminus = document.createElement('button');
					newplus.setAttribute('id', 'openAllChild');
					newplus.style.display='none';
					newplus.innerHTML = '+';
					newminus.setAttribute('id', 'closeAllChild');
					newminus.innerHTML = '-';
					if(direction){
	                  if(direction.parentElement.querySelector('ul')){
	                     direction.parentElement.querySelector('ul').appendChild(newLi);
						 if(!direction.parentElement.querySelector('ul').previousElementSibling.previousElementSibling.querySelector('button')){
							 direction.parentElement.querySelector('ul').previousElementSibling.previousElementSibling.appendChild(newplus);
							 direction.parentElement.querySelector('ul').previousElementSibling.previousElementSibling.appendChild(newminus);
						 }
	                  }else{
	                     direction.parentElement.append(newUl);
						 if(!direction.parentElement.querySelector('div').querySelector('button')){
		                     direction.parentElement.querySelector('div').appendChild(newplus);
		                     direction.parentElement.querySelector('div').appendChild(newminus);
						 }
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
						
						const openAllChild = document.querySelectorAll('#openAllChild');
						const closeAllChild = document.querySelectorAll('#closeAllChild');

						openAllChild.forEach(button => {
							button.addEventListener('click', (event)=>{
								event.stopPropagation(); 
								event.currentTarget.parentElement.nextElementSibling.nextElementSibling.style.removeProperty('display');
								event.currentTarget.nextElementSibling.style.removeProperty('display');
								event.currentTarget.style.display="none";
							})
						})
						closeAllChild.forEach(button => {
							button.addEventListener('click', (event)=>{
								event.stopPropagation(); 
								event.currentTarget.parentElement.nextElementSibling.nextElementSibling.style.display="none";
								event.currentTarget.previousElementSibling.style.removeProperty('display');
								event.currentTarget.style.display="none";
							})
						})	
		        },
		        error: function(error){
		            alert(error.responseText);
					console.log(error.responseText);
		        }
		    });
		}
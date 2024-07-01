document.addEventListener('DOMContentLoaded', () => {
	const addOrganization = document.querySelector('#addOrganization');
	const organizationList = document.querySelector('#div_orgcontents');
	const targetOrganization = document.querySelectorAll('#organizationLists');
	
	
	
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
		newOrganization.innerHTML ='<input type="text" id="newOrgInput">'
		if(organizationList.querySelector('span')){
			organizationList.querySelector('span').remove();
		}
		const direction = document.querySelector(".targetOrganization");
		if(direction){
			direction.parentNode.insertBefore(newOrganization, direction.nextSibling);
		}else{
			document.querySelector('#organizationAllList').appendChild(newOrganization);
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

function createNewOrganization(istargeted){
		    var organizationData = {
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
					console.log(maxOrganization.organizationName);
					var newLi = $(
					            '<li class="organizationList">' +
					            '<div id="organizationLists">' +
					            '<p>' + maxOrganization.organizationName + '</p>' +
					            '<p id="organizationIdx" style="display:none;">' + maxOrganization.organizationIdx + '</p>' +
					            '</div>' +
					            '</li>'
					        );
					var newUlLi = $(
						'<ul>' +
						'<li class="organizationList">' +
						'<div id="organizationLists">' +
						'<p>' + maxOrganization.organizationName + '</p>' +
						'<p id="organizationIdx" style="display:none;">' + maxOrganization.organizationIdx + '</p>' +
						'</div>' +
						'</li>' +
						'</ul>'
					)
						
					
					const direction = document.querySelector(".targetOrganization");
					if(direction){
						if(direction.querySelector('ul')){
							direction.querySelector('ul').append(newLi);
						}else{
							direction.append(newUlLi);
						}
					}else{
						document.querySelector('#organizationAllList').appendChild(newLi);
					}
		        },
		        error: function(error){
		            alert(error.responseText);
					console.log(error.responseText);
		        }
		    });
		}
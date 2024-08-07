const popupappear = function(){
			document.querySelector('#div_backgroundfull').style.removeProperty('display');
			document.querySelector('.insertpopup').style.removeProperty('display');
		}
		const popupdisappear = function(){
			document.querySelector('#div_backgroundfull').style.display="none";
			document.querySelector('.insertpopup').style.display="none";
		}
		const popupClean = function(){
			const popupValue = document.querySelector('.insertpopup').querySelectorAll('input');
			popupValue.forEach(input =>{
				input.value='';
			})
		}
		const editpopupappear = function(){
			document.querySelector('#div_backgroundfull').style.removeProperty('display');
			document.querySelector('.editpopup').style.removeProperty('display');
		}
		const editpopupdisappear = function(){
			document.querySelector('#div_backgroundfull').style.display="none";
			document.querySelector('.editpopup').style.display="none";
		}
		
		document.addEventListener('DOMContentLoaded', ()=>{
			const headtab = document.querySelectorAll('#div_headtab > a');
			const tables = document.querySelectorAll('#div_table > table');
			const selectAllCheckbox = document.querySelectorAll('#checkAllUser');
			const memberInsert = document.querySelector('#div_buttonsA2'); 
			const insertButton = document.querySelector('#addbutton1');
			const exitlogoedit = document.querySelectorAll('#exitlogoedit');
			const savebutton = document.querySelector('#savebutton1');
			const cancelbutton = document.querySelector('#cancelbutton1');
			const savebutton2 = document.querySelector('#savebutton2');
			const cancelbutton2 = document.querySelector('#cancelbutton2');
			const tableRow = document.querySelectorAll('tbody > tr');
			
			const editPopup = document.querySelector('.editpopup');
			
			const openOrgSelector = document.querySelector('#openOrgSelector');
			const editopenOrgSelector = document.querySelector('#editopenOrgSelector');
			const orgPopup = document.querySelector('#div_orgpopup');
			
			const noChangeOrg = document.querySelector('#noChangeOrg');
			
			const organizationSave = document.querySelector('#organizationSave');
			
			const openAllChild = document.querySelectorAll('#openAllChild');
			const closeAllChild = document.querySelectorAll('#closeAllChild');
			const targetOrganization = document.querySelectorAll('#organizationLists');
			
			const resetPassword = document.querySelector('#resetPassword');
			
			const banmembers = document.querySelectorAll('#BannedFromCompany');
			const notBanmembers = document.querySelectorAll('#NotBannedFromCompany');
			
			const managerFromNow = document.querySelectorAll('#managerFromNow');
			const noMoreManager = document.querySelectorAll('#noMoreManager');
			
			const allowMember = document.querySelectorAll('#allowMember');
			const denyMember = document.querySelectorAll('#denyMember');
			
			const noMoreCompanyMember = document.querySelectorAll('#noMoreCompanyMember');
			
			const cancelSelect = document.querySelector('#cancelSelect');
			const changeSelectMemberOrg = document.querySelector('#changeSelectMemberOrg');
			
			const organizationSaveAll = document.querySelector('#organizationSaveAll');
		
			document.querySelector('#noChangeOrgAll').addEventListener('click', ()=>{
				document.querySelector('#div_backgroundfull').style.display='none';
				document.querySelector('#div_orgpopupAll').style.display='none';
			})
			
			organizationSaveAll.addEventListener('click', ()=>{
				const checkboxInTable = document.querySelectorAll('#sortableTable input[type="checkbox"]:not(#checkAllUser)');
				checkboxInTable.forEach(input=>{
					if(input.checked === true){
						let memberIdx = input.parentElement.previousElementSibling.value;
						let organizationIdx = document.querySelector('.targetOrganization').querySelectorAll('p')[1].innerHTML;
						organizationChange(memberIdx, organizationIdx);
					}
				})
				location.reload(true);
			})
			
						
			changeSelectMemberOrg.addEventListener('click', ()=>{
				document.querySelector('#div_backgroundfull').style.removeProperty('display');
				document.querySelector('#div_orgpopupAll').style.removeProperty('display');
			})
			
			let selectedcounts = 0;
			document.querySelectorAll('#sortableTable input[type="checkbox"]:not(#checkAllUser)').forEach(input=>{
				input.addEventListener('click', (event)=>{
					event.stopPropagation();
					document.querySelector('#SetOrganizationAll').style.removeProperty('display');
					if(input.checked === true){
						selectedcounts ++;
						document.querySelector('#selectedCount').textContent=selectedcounts;
					}else{
						selectedcounts --;
						document.querySelector('#selectedCount').textContent=selectedcounts;
						if(selectedcounts === 0){
							document.querySelector('#SetOrganizationAll').style.display='none';
						}
					}
				
				})
			})


			selectAllCheckbox.forEach(input =>{
				input.addEventListener('change',(event)=>{
					const selectedtable = input.closest('table');
					const everyinput = selectedtable.querySelectorAll('tbody input[type="checkbox"]');
					const ischecked = event.target.checked;
					
					everyinput.forEach(checkbox =>{
						checkbox.checked = ischecked;
						if(checkbox.checked === true){
							selectedcounts ++;
							document.querySelector('#SetOrganizationAll').style.removeProperty('display');
						}else{
							selectedcounts =0;
							document.querySelector('#SetOrganizationAll').style.display='none';
						}
					})
					document.querySelector('#selectedCount').textContent=selectedcounts;
					
				})
			})

			
			cancelSelect.addEventListener('click', () => {
				document.querySelector('#SetOrganizationAll').style.display = 'none';
				document.querySelectorAll('#sortableTable input[type="checkbox"]').forEach(input =>{
					input.checked = false;
				})
				selectedcounts =0;
			})
			
			noMoreCompanyMember.forEach(span=>{
				span.addEventListener('click', (event)=>{
					event.stopPropagation();
					const memberIdx = event.currentTarget.parentElement.parentElement.firstElementChild.value;
					noMoreCompanyMemberfunction(memberIdx);
					event.currentTarget.parentElement.parentElement.remove();
					document.querySelector('#invalidMembers').textContent --;
				})
			})
			
			denyMember.forEach(span =>{
				span.addEventListener('click', (event)=>{
					event.stopPropagation();
					const memberIdx = event.currentTarget.parentElement.parentElement.firstElementChild.value;
					denyMemberfunction(memberIdx);
					location.reload(true);
				})
			}) 
			
			allowMember.forEach(span =>{
				span.addEventListener('click',(event)=>{
					event.stopPropagation();
					const memberIdx = event.currentTarget.parentElement.parentElement.firstElementChild.value;
					allowMemberfunction(memberIdx);
					location.reload(true);
				})
			})
			
			managerFromNow.forEach(span =>{
				span.addEventListener('click', (event) => {
					event.stopPropagation();
					const memberIdx = event.currentTarget.parentElement.parentElement.firstElementChild.value;
					managerOrNot(memberIdx);
					event.currentTarget.style.display='none';
					event.currentTarget.previousElementSibling.style.display='none';
					event.currentTarget.previousElementSibling.previousElementSibling.style.removeProperty('display');
					event.currentTarget.previousElementSibling.previousElementSibling.previousElementSibling.style.removeProperty('display');
				})
			})
			
			
			noMoreManager.forEach(span =>{
				span.addEventListener('click', (event)=>{
					event.stopPropagation();
					const memberIdx = event.currentTarget.parentElement.parentElement.firstElementChild.value;
					managerOrNot(memberIdx);
					event.currentTarget.nextElementSibling.style.removeProperty('display');
					event.currentTarget.nextElementSibling.nextElementSibling.style.removeProperty('display');
					event.currentTarget.style.display = 'none';
					event.currentTarget.previousElementSibling.style.display = 'none';
				})
			})
			
			notBanmembers.forEach(span =>{
				span.addEventListener('click', (event) => {
					event.stopPropagation();
					const memberIdx = event.currentTarget.parentElement.parentElement.firstElementChild.value;
					banMember(memberIdx);
					const NotBannedTbody = document.querySelector('#NotBannedTbody');
					event.currentTarget.parentElement.parentElement.querySelectorAll('td')[15].remove();
					const checkboxTd = document.createElement('td');
					checkboxTd.innerHTML='<input type="checkbox" style="margin:0; cursor:pointer;">';
					event.currentTarget.parentElement.parentElement.insertBefore(checkboxTd,event.currentTarget.parentElement.parentElement.querySelectorAll('td')[0]);
					NotBannedTbody.appendChild(event.currentTarget.parentElement.parentElement);
					event.currentTarget.parentElement.style.display='none';
					event.currentTarget.parentElement.parentElement.querySelector('#BannedFromCompany').parentElement.style.removeProperty('display');
					document.querySelector('#normalMembers').textContent ++;
					document.querySelector('#invalidMembers').textContent --;
				})
			})
			banmembers.forEach(span=>{
				span.addEventListener('click', (event)=>{
					event.stopPropagation(); 
					if(event.currentTarget.parentElement.nextElementSibling.nextElementSibling.firstElementChild.style.display ==='none'){
						const memberIdx = event.currentTarget.parentElement.parentElement.firstElementChild.value;
						banMember(memberIdx);
						const bannedTbody = document.querySelector('#bannedTbody');
						event.currentTarget.parentElement.parentElement.querySelectorAll('td')[0].remove();
						const deleteTd = document.createElement('td');
						deleteTd.innerHTML = '<span style="color:red">삭제</span>';
						event.currentTarget.parentElement.parentElement.appendChild(deleteTd);
						event.currentTarget.parentElement.style.display='none';
						event.currentTarget.parentElement.nextElementSibling.nextElementSibling.querySelectorAll('span')[2].style.display='none';
						event.currentTarget.parentElement.nextElementSibling.nextElementSibling.querySelectorAll('span')[3].style.display='none';
						event.currentTarget.parentElement.parentElement.querySelector('#NotBannedFromCompany').parentElement.style.removeProperty('display');
						bannedTbody.appendChild(event.currentTarget.parentElement.parentElement);
						if(document.querySelector('#noBannedMember')){
							document.querySelector('#noBannedMember').remove();
						}
						document.querySelector('#normalMembers').textContent --;
						document.querySelector('#invalidMembers').textContent ++;
					}else{
						alert('관리자는 이용중지 시킬 수 없습니다.');
					}
				})
			})
			
			
			resetPassword.addEventListener('click', (event)=>{
				let theEmail = event.currentTarget.parentElement.previousElementSibling.querySelector('input').value;
				passwordReset(theEmail);
			})
			
			targetOrganization.forEach(li => {
					li.addEventListener('click', (event)=>{
						targetOrganization.forEach(li =>{
							li.classList.remove('targetOrganization');
						})
						event.currentTarget.classList.add('targetOrganization');
					});
				})
			
			
			document.querySelectorAll('#div_orgcontents ul').forEach(ul => {
			    ul.style.listStyle = 'none';
			});
			
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
			
			
			organizationSave.addEventListener('click', ()=>{
				orgPopup.style.display="none";
				const targetOrg = document.querySelector('.targetOrganization')
				if(targetOrg){ // 뭐가 떠있는지에 따라 edit인지 create인지 차이 비교. 이후 controller에도 수정할 것
					if(document.querySelector('.insertpopup').style.display === 'none'){
						document.querySelector('#editpopupOrg').value=targetOrg.querySelector('p').innerHTML;
						document.querySelector('#editpopupOrgIdx').value=targetOrg.querySelectorAll('p')[1].innerHTML;
					}else{
						document.querySelector('#popupOrg').value=targetOrg.querySelector('p').innerHTML;
						document.querySelector('#popupOrgIdx').value=targetOrg.querySelectorAll('p')[1].innerHTML;
					}
					
				}
			})
			
			noChangeOrg.addEventListener('click', ()=>{
				//document.querySelector('.editpopup').style.removeProperty('display');
				orgPopup.style.display="none";
			})
			
			openOrgSelector.addEventListener('click',(event)=>{
				event.stopPropagation();
				//document.querySelector('.insertpopup').style.display="none"
				orgPopup.style.removeProperty('display');
			})
			
			editopenOrgSelector.addEventListener('click', (event)=>{
				event.stopPropagation();
				//document.querySelector('.editpopup').style.display="none"
				orgPopup.style.removeProperty('display');
			})
			
			tableRow.forEach(tr =>{
				tr.addEventListener('click', (event)=>{
					editpopupappear();
					editPopup.querySelector('#editpopupName').value = event.currentTarget.querySelector('.trName').textContent;
					editPopup.querySelector('#editpopupEmail').value = event.currentTarget.querySelector('.trId').textContent;
					editPopup.querySelector('#editpopupRank').value = event.currentTarget.querySelector('.trRankName').textContent;
					editPopup.querySelector('#editpopupOrg').value = event.currentTarget.querySelector('.trOrganizationName').textContent;
					editPopup.querySelector('#editpopupPhone').value = event.currentTarget.querySelector('.trPhone').textContent;
				})
			})

			
			
			cancelbutton2.addEventListener('click', ()=>{
				editpopupdisappear();
			})		
			
			cancelbutton.addEventListener('click', ()=>{
				popupdisappear();
				popupClean();
			})
			
			exitlogoedit.forEach(a =>{
				a.addEventListener('click', ()=>{
					popupdisappear();
					editpopupdisappear();
					popupClean();
				})
			})
			
			insertButton.addEventListener('click', ()=>{
				popupappear();
			})
			
			
			memberInsert.addEventListener('click', ()=>{
				const memberInsertlist = document.querySelector('#newbuttons');
				const listdisplay = memberInsertlist.style.display;
				if(listdisplay == "none"){
					memberInsertlist.style.removeProperty('display');
				}else{
					memberInsertlist.style.display="none";
				}
			})
			
			headtab.forEach((a,index)=>{
				a.addEventListener('click',(event)=>{
					headtab.forEach(a=>{
						a.classList.remove('selectedheadtab');
					})
					event.target.classList.add('selectedheadtab');
					tables.forEach(table =>{
						table.style.display="none";
					})
                    tables[index].style.removeProperty('display');
				})
			})
		})

		document.addEventListener("DOMContentLoaded", function() {
		    sortTable(2); // 기본 정렬 컬럼 인덱스 설정 (2번째 컬럼: 이름)
		    sortTable(2); // 기본 정렬 컬럼 인덱스 설정 (2번째 컬럼: 이름)
		});
		function isDate(value) {
		    return !isNaN(Date.parse(value));
		}
		function sortTable(columnIndex) {
		    let table = document.getElementById("sortableTable");
		    let rows = Array.from(table.rows).slice(1); // header row 제외
		    let currentSortOrder = table.getAttribute("data-sort-order");
		    let sortOrder = (table.getAttribute("data-sort-column") == columnIndex && currentSortOrder === "asc") ? "desc" : "asc";

		    rows.sort((rowA, rowB) => {
				let cellA = rowA.cells[columnIndex].innerText.toLowerCase().trim();
				let cellB = rowB.cells[columnIndex].innerText.toLowerCase().trim();

				// Treat empty cells as null
				if (cellA === "") cellA = null;
				if (cellB === "") cellB = null;

				// Check if the column contains date/time
				if (cellA && cellB && isDate(cellA) && isDate(cellB)) {
					cellA = new Date(cellA);
					cellB = new Date(cellB);
					return sortOrder === "asc" ? cellA - cellB : cellB - cellA;
				}

				if (cellA === null && cellB === null) return 0;
				if (cellA === null) return sortOrder === "asc" ? 1 : -1;
				if (cellB === null) return sortOrder === "asc" ? -1 : 1;

				if (cellA < cellB) return sortOrder === "asc" ? -1 : 1;
				if (cellA > cellB) return sortOrder === "asc" ? 1 : -1;
				return 0;
		    });

		    table.setAttribute("data-sort-order", sortOrder);
		    table.setAttribute("data-sort-column", columnIndex);

		    rows.forEach(row => table.tBodies[0].appendChild(row));

		    updateSortedColumnIndicator(columnIndex, sortOrder);
		}

		function updateSortedColumnIndicator(columnIndex, sortOrder) {
		    let thElements = document.querySelectorAll("#sortableTable th");
		    thElements.forEach((th, index) => {
		        let aTag = th.querySelector("a");
		        if (aTag) {
		            if (index === columnIndex) {
		                th.classList.add("sorted");
		                th.classList.remove("asc", "desc");
		                th.classList.add(sortOrder);
		                aTag.style.display = "inline-block";
		            } else {
		                th.classList.remove("sorted", "asc", "desc");
		                aTag.style.display = "none";
		            }
		        }
		    });
		}

		const organizationChange = function(memberIdx, organizationIdx) {
			let newOrg =[memberIdx, organizationIdx]
			$.ajax({
				url: "/Admin/organizationChange",
				type: 'POST',
				contentType: 'application/json',
				data: JSON.stringify(newOrg),
				success: function(response) {
					console.log(response);
				},
				error: function(error) {
					alert(error.responseText);
					console.log(error.responseText);
				}
			});
		}
		const noMoreCompanyMemberfunction = function(memberIdx) {

			$.ajax({
				url: "/Admin/noMoreCompanyMember",
				type: 'POST',
				contentType: 'application/json',
				data: JSON.stringify(memberIdx),
				success: function(response) {
					console.log(response);
				},
				error: function(error) {
					alert(error.responseText);
					console.log(error.responseText);
				}
			});
		}
		const denyMemberfunction = function(memberIdx) {

			$.ajax({
				url: "/Admin/DenyMember",
				type: 'POST',
				contentType: 'application/json',
				data: JSON.stringify(memberIdx),
				success: function(response) {
					console.log(response);
				},
				error: function(error) {
					alert(error.responseText);
					console.log(error.responseText);
				}
			});
		}

		
		
const allowMemberfunction = function(memberIdx) {

	$.ajax({
		url: "/Admin/AllowMember",
		type: 'POST',
		contentType: 'application/json',
		data: JSON.stringify(memberIdx),
		success: function(response) {
			console.log(response);
		},
		error: function(error) {
			alert(error.responseText);
			console.log(error.responseText);
		}
	});
}

		
const managerOrNot = function(memberIdx) {

	$.ajax({
		url: "/Admin/AdminMemberManager",
		type: 'POST',
		contentType: 'application/json',
		data: JSON.stringify(memberIdx),
		success: function(response) {
			console.log(response);
		},
		error: function(error) {
			alert(error.responseText);
			console.log(error.responseText);
		}
	});
}

const banMember = function(memberIdx) {
	
	$.ajax({
		url: "/Admin/AdminMemberBan",
		type: 'POST',
		contentType: 'application/json',
		data: JSON.stringify(memberIdx),
		success: function(response) {
			console.log(response);
		},
		error: function(error) {
			alert(error.responseText);
			console.log(error.responseText);
		}
	});
}

const passwordReset = function(theEmail) {

	$.ajax({
		url: "/Admin/memberList",
		type: 'PUT',
		contentType: 'application/json',
		data:JSON.stringify(theEmail),
		success: function(response) {
			alert("비밀번호 초기화 완료");
		},
		error: function(error) {
			alert(error.responseText);
			console.log(error.responseText);
		}
	});
}

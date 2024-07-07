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
			
			
			selectAllCheckbox.forEach(input =>{
				input.addEventListener('change',(event)=>{
					const selectedtable = input.closest('table');
					const everyinput = selectedtable.querySelectorAll('tbody input[type="checkbox"]');
					const ischecked = event.target.checked;
					
					everyinput.forEach(checkbox =>{
						checkbox.checked = ischecked;
					})
				})
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

document.addEventListener('DOMContentLoaded', () => {
		    const modbuttons = document.querySelectorAll('.modbutton');
		    const savebuttons = document.querySelectorAll('.savebutton');
		    const cancelbuttons = document.querySelectorAll('.cancelbutton');
		    let input1value;
		    const checked = document.querySelectorAll('input[name="승인"]');
			const acceptedURL = document.querySelector('#div_acceptURL');
			
			const logoinsert = document.querySelector('#div_logocontrol');
			const logocancel = document.querySelector('#logocancel');
			const logosave = document.querySelector('#logosave');
			const exitlogoedit = document.querySelector('#exitlogoedit');
			
			const allowcancel = document.querySelector('#allowcancel');
			const allowsave = document.querySelector('#allowsave');
			const exitallow = document.querySelector('#exitallow');
			
			logoinsert.addEventListener('click',(event)=>{
				document.querySelector('#div_backgroundfull').style.display="block";
				document.querySelector('#div_logoinsertpopup').style.display="block";
			})
			logocancel.addEventListener('click',(event)=>{
				document.querySelector('#div_backgroundfull').style.display="none";
				document.querySelector('#div_logoinsertpopup').style.display="none";
			})
			logosave.addEventListener('click',(event)=>{
				document.querySelector('#div_backgroundfull').style.display="none";
				document.querySelector('#div_logoinsertpopup').style.display="none";
			})
			exitlogoedit.addEventListener('click',(event)=>{
				document.querySelector('#div_backgroundfull').style.display="none";
				document.querySelector('#div_logoinsertpopup').style.display="none";
			})
			
			
			checked.forEach((radio) =>{
				radio.addEventListener('change',() =>{
					const allowed = document.querySelector('input[name="승인"]:checked');
					if(allowed.value === "승인불필요"){
						acceptedURL.style.display="inline-block";
					}else{
						document.querySelector('#div_allowpopup').style.display="block";
						document.querySelector('#div_backgroundfull').style.display="block";
					}
				});
			});
			
			
			allowcancel.addEventListener('click',(event)=>{
				document.querySelector('#div_backgroundfull').style.display="none";
				document.querySelector('#div_allowpopup').style.display="none";
				document.querySelector('input[name="승인"][id="승인불필요"]').checked="checked";
			});
			exitallow.addEventListener('click',(event)=>{
				document.querySelector('#div_backgroundfull').style.display="none";
				document.querySelector('#div_allowpopup').style.display="none";
				document.querySelector('input[name="승인"][id="승인불필요"]').checked="checked";
			});
			allowsave.addEventListener('click',(event)=>{
				document.querySelector('#div_backgroundfull').style.display="none";
				document.querySelector('#div_allowpopup').style.display="none";
				acceptedURL.style.display="none";
			});
			
			
		    modbuttons.forEach(modbutton => {
		        modbutton.addEventListener('click', (event) => {
		            input1value = event.currentTarget.previousElementSibling.value;
		            event.currentTarget.style.display = "none";
		            event.currentTarget.nextElementSibling.style.display = "inline-block";
		            event.currentTarget.nextElementSibling.nextElementSibling.style.display = "inline-block";
		            event.currentTarget.previousElementSibling.disabled = false;
		        });
		    });
	
		    savebuttons.forEach(savebutton => {
		        savebutton.addEventListener('click', (event) => {
		            event.currentTarget.previousElementSibling.style.display = "inline-block";
		            event.currentTarget.style.display = "none";
		            event.currentTarget.nextElementSibling.style.display = "none";
		            event.currentTarget.previousElementSibling.previousElementSibling.disabled = "disabled";
		        });
		    });
	
		    cancelbuttons.forEach(cancelbutton => {
		        cancelbutton.addEventListener('click', (event) => {
		            event.currentTarget.previousElementSibling.previousElementSibling.style.display = "inline-block";
		            event.currentTarget.previousElementSibling.style.display = "none";
		            event.currentTarget.style.display = "none";
		            event.currentTarget.previousElementSibling.previousElementSibling.previousElementSibling.value = input1value;
		            event.currentTarget.previousElementSibling.previousElementSibling.previousElementSibling.disabled = "disabled";
		        });
		    });
		});

document.addEventListener("DOMContentLoaded", () => {
    const submitButton = document.querySelector('.submit-button');
    const companyURLInput = document.querySelector('.url-input');

    companyURLInput.addEventListener('input', (event) => { 
        if (event.currentTarget.value !== "") {
            submitButton.style.backgroundColor = '#e134ff';
            submitButton.disabled = false;
        } else {
            submitButton.style.backgroundColor = '#ccc';
            submitButton.disabled = true; 
        }
    });
});
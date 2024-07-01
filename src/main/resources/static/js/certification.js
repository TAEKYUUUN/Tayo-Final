document.addEventListener('DOMContentLoaded', (event) => {
    const inputs = document.querySelectorAll('#otp-form input');

    inputs.forEach((input, index) => {
        input.addEventListener('input', () => {
            if (input.value.length === input.maxLength) {
                if (index < inputs.length - 1) {
                    inputs[index + 1].focus();
                }
            }
        });

        input.addEventListener('keydown', (e) => {
            if (e.key === 'Backspace' && input.value.length === 0 && index > 0) {
                inputs[index - 1].focus();
            }
        });
    });

    inputs[0].focus();
});
function formValidation() {
    const form = document.getElementById('form');
    const name = document.getElementById('name');
    const description = document.getElementById('description');


    // form.addEventListener('submit', e => {
    //     e.preventDefault();
    //     checkInputs();
    // });

        // trim to remove the whitespaces
    let isValid = true;
        const nameValue = name.value.trim();
        const descriptionValue = description.value.trim();
        if (nameValue === '') {
            setErrorFor(name, 'Name cannot be blank');
        } else {
            setSuccessFor(name);
        }
        if (descriptionValue === '') {
            setErrorFor(description, 'Description cannot be blank');
        } else if (!descriptionValue) {
            setErrorFor(description, 'Not a valid description');
        } else {
            setSuccessFor(description);
        }

        return isValid;

        function setErrorFor(input, message) {
            const formControl = input.parentElement;
            const small = formControl.querySelector('small');
            formControl.className = 'form-div error';
            small.innerText = message;
            isValid = false;
        }

        function setSuccessFor(input) {
            const formControl = input.parentElement;
            const small = formControl.querySelector('small');
            formControl.className = 'form-div success';
            small.innerText = '';
        }
}

function formValidation() {
    const name = document.getElementById('name');
    const description = document.getElementById('description');
    let isValid = true;
    const nameValue = name.value.trim();
    const descriptionValue = description.value.trim();
    if (nameValue === '') {
        setErrorFor(name, 'cannot be blank');
    } else {
        setSuccessFor(name);
    }
    if (descriptionValue === '') {
        setErrorFor(description, 'cannot be blank');
    } else if (!descriptionValue) {
        setErrorFor(description, 'Not a valid description');
    } else {
        setSuccessFor(description);
    }
    return isValid;

    function setErrorFor(input, message) {
        const parentElement = input.parentElement;
        const small = parentElement.querySelector('small');
        parentElement.className = 'form-div error-msg';
        small.innerText = message;
        isValid = false;
    }

    function setSuccessFor(input) {
        const parentElement = input.parentElement;
        const small = parentElement.querySelector('small');
        parentElement.className = 'form-div success';
        small.innerText = '';
    }
}

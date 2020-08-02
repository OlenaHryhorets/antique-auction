function formValidation() {
    const name = document.getElementById('name');
    const description = document.getElementById('description');
    const date = document.getElementById('dateTimePicker');
    const checkDate = document.getElementById('checkDate');
    let isValid = true;

    const nameValue = name.value.trim();
    const descriptionValue = description.value.trim();
    const dateValue = date.value.trim();
    if (nameValue === '') {
        setErrorFor(name, 'cannot be blank');
    } else {
        setSuccessFor(name);
    }
    if (descriptionValue === '') {
        setErrorFor(description, 'cannot be blank');
    } else if (descriptionValue.length >= 100) {
        setErrorFor(description, 'is too long! Please enter less than 100 chars');
    } else {
        setSuccessFor(description);
    }
    if (dateValue === '') {
        const small = checkDate.querySelector('small');
        checkDate.className = 'error-msg';
        small.innerText = 'cannot be empty!';
        isValid = false;
    } else {
        const small = checkDate.querySelector('small');
        checkDate.className = 'success';
        small.innerText = 'ok';
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
        small.innerText = 'ok';
    }
}

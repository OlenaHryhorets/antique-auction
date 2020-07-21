function search() {
    const form = document.getElementById('form');
    const name = document.getElementById('name');
    const description = document.getElementById('description');
    const bidDate = document.getElementById('bidDate');


    form.addEventListener('submit', e => {
        e.preventDefault();
        checkInputs();
    });

    function checkInputs() {
        // trim to remove the whitespaces
        const nameValue = name.value.trim();
        const descriptionValue = description.value.trim();
        const bidDateValue = bidDate.value.trim();
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
        if (bidDateValue === '') {
            setErrorFor(bidDate, 'Pick a bidDate');
        } else {
            setSuccessFor(bidDate);
        }

        function setErrorFor(input, message) {
            const formControl = input.parentElement;
            const small = formControl.querySelector('small');
            formControl.className = 'form-control error';
            small.innerText = message;
        }

        function setSuccessFor(input) {
            const formControl = input.parentElement;
            formControl.className = 'form-control success';
        }
    }
}

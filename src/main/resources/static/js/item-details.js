// Set the date we're counting down to
$(document).ready(function () {
    if (dateStringValue.length !== 0) {
        let countDownDate = new Date(dateStringValue).getTime();
        // Update the count down every 1 second
        let x = setInterval(function() {
            // Get today's date and time
            let now = new Date().getTime();
            // Find the distance between now and the count down date
            let distance = countDownDate - now;
            // Time calculations for days, hours, minutes and seconds
            let days = Math.floor(distance / (1000 * 60 * 60 * 24));
            let hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            let minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
            let seconds = Math.floor((distance % (1000 * 60)) / 1000);

            document.getElementById("countDownTimer").innerHTML = days + "d " + hours + "h "
                + minutes + "m " + seconds + "s ";

            if (distance < 0) {
                clearInterval(x);

                document.getElementById("countDownTimer").className = "error-msg";
                document.getElementById("countDownTimer").innerHTML = "Auction is over!";
            }
        }, 1000);
    }
});

$(document).ready(function () {
    if (dateStringValue.length !== 0) {
        let countDownDate = new Date(dateStringValue).getTime();
        let x = setInterval(function () {
            let now = new Date().getTime();
            let distance = countDownDate - now;
            let days = Math.floor(distance / (1000 * 60 * 60 * 24));
            let hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            let minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
            let seconds = Math.floor((distance % (1000 * 60)) / 1000);
            let countDownTimer = document.getElementById("countDownTimer");
            countDownTimer.innerHTML = days + "d " + hours + "h "
                + minutes + "m " + seconds + "s ";
            if (distance < 0) {
                clearInterval(x);
                countDownTimer.className = "error-msg";
                countDownTimer.innerHTML = "Auction is over!";
                document.getElementById("currentPrice").disabled = true;
                document.getElementById("bidsubmit").disabled = true;
            }
        }, 1000);
    }
});

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
            $.ajax({
                type: 'GET',
                url: '/item/status/get/' + itemId,
                // data: { itemId: itemId },
                success: function (data) {
                    // let goodData = JSON.stringify(data);
                    // alert(goodData);
                    // $('#cand').html(data.currentPrice);
                    $('#showCurrentPrice').html(data.currentPrice);
                    if (currentPrice != data.currentPrice) {
                        currentPrice = data.currentPrice;
                        $('#currentPrice').val(data.currentPrice);
                    }

                    let html = '';
                    $.each(data.bidPrices, function (index, value) {
                        html = html + '<div><div>' + value + '</div></div>';
                    });
                    $('.bidHistory').html(html);
                    $('#itemName').html(data.itemName);
                    $('#description-field').html(data.itemDescription);
                        // $('input#currentPrice').prop('min', data.currentPrice);
                    dateStringValue = data.dateStringValue;
                    countDownDate = new Date(dateStringValue).getTime()

                },
            });
            if (distance < 0) {
                clearInterval(x);
                // $.ajax({
                //     type: 'GET',
                //     url: '/item/status/get',
                //     data: { itemId: itemId },
                //     success: function (data) {
                //         let goodData = JSON.stringify(data);
                //         alert(goodData);
                //
                //         // $('#cand').innerText('ffffff');
                //     },
                //
                // });
                $.ajax({
                    type: 'GET',
                    url: '/item/status/get/' + itemId,
                    // data: { itemId: itemId },
                    success: function (data) {
                        $('#bidSection').html(data.finalPriceUserName + ' ' + data.finalPrice);
                    },
                });
                countDownTimer.className = "error-msg";
                countDownTimer.innerHTML = "Auction is over!";
                document.getElementById("currentPrice").disabled = true;
                document.getElementById("bidsubmit").disabled = true;
            }
        }, 1000);
    }
});

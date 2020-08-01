$(document).ready(function () {
    setInterval(function () {
        $.ajax({
            type: 'GET',
            url: '/user/status/get/' + login,
            success: function (data) {
                let allBids = '';
                $.each(data.items, function (index, value) {
                    let str = value.statusName.toLowerCase();
                    let e = '<div class="form-div" style="align-items: center; border: 1px #8d6647 solid">' +
                        '<div class="btn" onclick="window.location.href=\'/item/details/' + value.id + '\';">'
                        + value.name + '</div>' + '<div class="' + str + '">'
                        + value.statusName + '</div></div>';
                    allBids = allBids + e;
                });
                $('#bidItemsList').html(allBids);

                let awardedBids = '';
                $.each(data.awardedItems, function (index, value) {
                    let e = '<div class="form-div" style="align-items: center; border: 1px #8d6647 solid">' +
                        '<div>'
                        + value.name + '</div><div><div class="bill-div "> <div>Date: <span>'
                        + value.dateStringValue + '</span></div> <div>Product name: <span>'
                        + value.name + '</span></div><div>Price: <span>$<span>'
                        + value.finalPrice + '</span></span></div><button id="payButton" class="form-submit">Pay</button></div></div></div>';
                    awardedBids = awardedBids + e;
                });
                $('#awardedItemsList').html(awardedBids);
            },
        });
    }, 1000);
});

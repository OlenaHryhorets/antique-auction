$(document).ready(function () {
    $.ajax({
        type: 'GET',
        url: '/user/status/get/' + login,
        // data: { itemId: itemId },
        success: function (data) {
            $.each(data.awardedItems, function (index, value) {

                let e = $('<div class="form-div" style="border: 1px #8d6647 solid">' +
                    '<div class="btn" onclick="window.location.href=\'/item/details/' + value.id + '\';">'
                    + value.name + '</div>' + '<div>' + value.id + '</div></div>');
                $('#awardedList').append(e);
            });
            // $('#awardedList').html(data.items);
        },
    });
});

$(document).ready(function () {
    $.ajax({
        type: 'GET',
        url: '/user/status/get/' + login,
        success: function (data) {
            $.each(data.items, function (index, value) {
                let str = value.statusName.toLowerCase();
                let e = $('<div class="form-div" style="align-items: center; border: 1px #8d6647 solid">' +
                    '<div class="btn" onclick="window.location.href=\'/item/details/' + value.id + '\';">'
                    + value.name + '</div>' + '<div class="' + str + '">'
                    + value.statusName + '</div></div>');
                $('#bidItemsList').append(e);
            });

            $.each(data.awardedItems, function (index, value) {
                let e = $('<div class="form-div" style="align-items: center; border: 1px #8d6647 solid">' +
                    '<div>'
                    + value.name + '</div><div>' + value.finalPrice + '</div></div>');
                $('#awardedItemsList').append(e);
            });
            // $('#awardedList').html(data.items);
        },
    });
});

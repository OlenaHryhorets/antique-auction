
// изменение заголовка страницы
// function changePageTitle(page_title){
//
//     // измение заголовка страницы
//     $('#page-title').text(page_title);
//
//     // измение заголовка вкладки браузера
//     document.title=page_title;
// }

// функция для создания значений формы в формате json
// $.fn.serializeObject = function() {
//     var o = {};
//     var a = this.serializeArray();
//     $.each(a, function() {
//         if (o[this.name] !== undefined) {
//             if (!o[this.name].push) {
//                 o[this.name] = [o[this.name]];
//             }
//             o[this.name].push(this.value || '');
//         } else {
//             o[this.name] = this.value || '';
//         }
//     });
//     return o;
// };


//Pagination

// var
//     // search = $("#search"),
//     control = $("#pagination"),
//     table = $("#table tbody tr"),
//     pageParts = null,
//     perPage = 10;

// search.on("keyup", function() {
//     var value = $(this).val().toLowerCase();
//     table.filter(function() {
//         $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
//     });
//     updPagination();
// });

// control.pagination({
//     itemsOnPage: perPage,
//     cssStyle: "light-theme",
//     onPageClick: function(pageNum) {
//         var start = perPage * (pageNum - 1);
//         var end = start + perPage;
//         if (pageParts) {
//             pageParts.hide()
//                 .slice(start, end).show();
//         }
//     }
// });
//
// function updPagination() {
//     pageParts = table.filter(function() { return $(this).css("display") !== 'none' });
//     pageParts.slice(perPage).hide();
//     control.pagination('selectPage', 1);
//     control.pagination('updateItems', pageParts.length);
//     console.log("ffffuck");
// }
//
// updPagination();

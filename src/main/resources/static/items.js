function search() {
    $.ajax({
        type: "GET",
        url: "/search",
        timeout: 100000,
        cache: false,
        data: {},

    });
}



// jQuery(function(){
//
//     // показать список товаров при первой загрузке
//     showItems();
//
// });
//
// // при нажатии кнопки
// // $(document).on('click', '.read-products-button', function(){
// //     showItems();
// // });
//
// function showItems(){
//     $.getJSON("/items", function(data){
//         // перебор списка возвращаемых данных
//         let itemsHtml = '';
//         $.each(data, function(key, val) {
//             itemsHtml += `<div class="item">
//                 <div class="imageDiv"><img src="image.jpg" style="width:20px; height: 20px;" alt="no image"/></div>
//                 <div sec:authorize="hasRole('ADMIN')" class="name">` + val.name + `</div>
//                 <div class="price">` + val.price + `</div>
//                 <div class="description">` + val.description + `</div>
//                 <button id="bidNow" onClick="window.location.href='/goToItemsDetail?id=` + val.id + `';">Bid Now</button>
//             </div>`
//             // console.log(val.description);
//             // создание новой строки таблицы для каждой записи
//           //  read_products_html+=`
//         // <tr>
//         //
//         //     <td>` + val.name + `</td>
//         //     <td>` + val.price + `</td>
//         //     <td>` + val.category_name + `</td>
//         //
//         //     <!-- кнопки 'действий' -->
//         //     <td>
//         //         <!-- кнопка чтения товара -->
//         //         <button class='btn btn-primary m-r-10px read-one-product-button' data-id='` + val.id + `'>
//         //             <span class='glyphicon glyphicon-eye-open'></span> Просмотр
//         //         </button>
//         //
//         //         <!-- кнопка редактирования -->
//         //         <button class='btn btn-info m-r-10px update-product-button' data-id='` + val.id + `'>
//         //             <span class='glyphicon glyphicon-edit'></span> Редактирование
//         //         </button>
//         //
//         //         <!-- кнопка удаления товара -->
//         //         <button class='btn btn-danger delete-product-button' data-id='` + val.id + `'>
//         //             <span class='glyphicon glyphicon-remove'></span> Удаление
//         //         </button>
//         //     </td>
//         //
//         // </tr>`;
//         });
//         $('#gallery').html(itemsHtml);
//     });
// }
//

$(function() {
    $('.delete-user-btn').on('click', function() {
        let userId = $(this).attr('user-id');
        bootbox.confirm("Do you really want to delete this user?", function(result){
            if (result) {
                $.ajax({
                    url: '/api/users/' + userId,
                    type: 'delete',
                    success: function() {
                        window.location = location.pathname + '?success=User deleted!';
                    },
                    error: function(err) {
                        window.location = location.pathname + '?error=Server error. Please try again!';
                    }
                })
            }
        });
    });

    $('.delete-cat-btn').on('click', function() {
        let catId = $(this).attr('cat-id');
        bootbox.confirm("Do you really want to delete this category?", function(result){
            if (result) {
                $.ajax({
                    url: '/api/categories/' + catId,
                    type: 'delete',
                    success: function() {
                        window.location = location.pathname + '?success=Category deleted!';
                    },
                    error: function(err) {
                        let json = JSON.parse(err.responseText);
                        window.location = location.pathname + '?error=' + json.error;
                    }
                })
            }
        });
    });

    $('.del-prod-btn').on('click', function() {
        let id = $(this).attr('id');
        bootbox.confirm("Do you really want to delete this product?", function (result) {
            if (result) {
                $.ajax({
                    url: '/api/products/' + id,
                    type: 'delete',
                    success: function () {
                        window.location = location.pathname + '?success=Product deleted!';
                    },
                    error: function (err) {
                        let json = JSON.parse(err.responseText);
                        window.location = location.pathname + '?error=' + json.error;
                    }
                })
            }
        });
    });

    $('.add-order-product-btn').on('click', function() {
        let productsOptions = '';
        for(let product of AppGlobals.Common.products) {
            productsOptions += '<option value="' + product['id'] + '">' + product['name'] + '</option>'
        }

        let tbody = $('#orderProductsTableBody');
        let html = '';
        html += '<tr>';
        html += '<td><input type="hidden" class="form-control pname" name="productName[]" readonly></td>';
        html += '<td>' +
                    '<select class="form-control pid" name="productId[]" style="width: 100%" required>' +
                        '<option disabled selected>-- Select product --</option>' +
                        productsOptions +
                    '</select>' +
                '</td>';
        html += '<td><input type="number" class="form-control stock" name="stock[]" readonly></td>';
        html += '<td><input type="number" class="form-control price" name="price[]" readonly></td>';
        html += '<td><input type="number" class="form-control qty" name="qty[]" required></td>';
        html += '<td><input type="number" class="form-control total" name="total[]" readonly></td>';
        html += '<td class="text-center">' +
                    '<a href="#" class="btn btn-danger btn-sm delete-order-product-btn" role="button">' +
                        '<span class="nav-icon fas fa-times" data-toggle="tooltip" title="Delete product"></span>' +
                    '</a>' +
                '</td>';
        html += '</tr>';
        tbody.append(html);
        $('.pid').select2();
    });

    $('.pid').select2();

    $('#orderProductsTableBody').on('click', 'a.delete-order-product-btn', function() {
        let closestTr = $(this).closest('tr');
        closestTr.remove();
    })

    $('#categoriesTable').DataTable();
    $('#usersTable').DataTable();
    $('#productsTable').DataTable();
    $('[data-toggle="tooltip"]').tooltip();
    $('#orderDatePicker').datetimepicker({ format: 'L' });
})

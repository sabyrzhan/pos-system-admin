$(function() {
    $('.add-order-product-btn').on('click', function() {
        appendOrderItem(null);
    });

    $('#orderProductsTableBody').on('change', 'select.pid', function() {
        clearOrderFields();
        let productId = $(this).val();
        let product = AppGlobals.Common.findProductById(productId);
        let parentTr = $(this).closest('tr');
        $(parentTr).find('.stock').val(product.stock);
        $(parentTr).find('.price').val(product.salePrice);
        let quantity = $(parentTr).find('.quantity');
        quantity.val(1);
        quantity.removeAttr('readonly');
        $(parentTr).find('.total').val(product.salePrice);
        calculateOrder();

    })

    $('#orderProductsTableBody').on('change', 'input.quantity', function() {
        let quantity = $(this).val() * 1;
        if (!Number.isInteger(quantity) || quantity <= 0) {
            bootbox.alert("Quantity must be at least 1");
            return false;
        }
        let parentTr = $(this).closest('tr');
        let productId = $(parentTr).find('select.pid').val();
        let product = AppGlobals.Common.findProductById(productId);
        if (!product) {
            bootbox.alert("Product with ID=" + productId + " not found.");
            return false;
        }
        let totalPrice = quantity * product.salePrice;
        $(parentTr).find('.total').val(totalPrice);
        calculateOrder();
    })

    $('#orderProductsTableBody').on('click', 'a.delete-order-product-btn', function() {
        let closestTr = $(this).closest('tr');
        closestTr.remove();
        calculateOrder();
    });

    $('#discount').on('keyup', function() {
        calculateOrder();
    });

    $('#paid').on('keyup', function() {
        calculateOrder();
    });

    $('.cancel-order-btn').on('click', function() {
        let id = $(this).attr('id');
        bootbox.confirm("Do you really want to cancel this order?", function (result) {
            if (result) {
                $.ajax({
                    url: '/api/orders/' + id,
                    type: 'delete',
                    success: function () {
                        window.location = location.pathname + '?success=Order cancelled!';
                    },
                    error: function (err) {
                        let json = JSON.parse(err.responseText);
                        window.location = location.pathname + '?error=' + json.error;
                    }
                })
            }
        });
    });

    function appendOrderItem(orderItem) {
        let productsOptions = '';
        for(let product of AppGlobals.Common.products) {
            if (orderItem && orderItem['productId'] == product['id']) {
                productsOptions += '<option selected value="' + product['id'] + '">' + product['name'] + '</option>'
            } else {
                productsOptions += '<option value="' + product['id'] + '">' + product['name'] + '</option>'
            }
        }

        let tbody = $('#orderProductsTableBody');
        let html = '';
        html += '<tr>';
        html += '<td></td>';
        html += '<td>' +
            '<select class="form-control pid" name="productId[]" style="width: 100%" required>' +
            '<option disabled selected>-- Select product --</option>' +
            productsOptions +
            '</select>' +
            '</td>';
        if (orderItem && AppGlobals.Common.findProductById(orderItem['productId'])) {
            let product = AppGlobals.Common.findProductById(orderItem['productId']);
            html += '<td><input type="number" class="form-control stock" name="stock[]" value="' + product['stock'] + '" readonly></td>';
            html += '<td><input type="number" class="form-control price" name="price[]" value="' + product['salePrice'] + '" readonly></td>';
            let total = orderItem['quantity'] * product['salePrice'];
            html += '<td><input type="number" step="1" min="1" class="form-control quantity" name="quantity[]" value="' + orderItem['quantity'] + '" required></td>';
            html += '<td><input type="number" class="form-control total" name="total[]" value="' + total + '" readonly></td>';
        } else {
            html += '<td><input type="number" class="form-control stock" name="stock[]" readonly></td>';
            html += '<td><input type="number" class="form-control price" name="price[]" readonly></td>';
            html += '<td><input type="number" step="1" min="1" class="form-control quantity" name="quantity[]" readonly required></td>';
            html += '<td><input type="number" class="form-control total" name="total[]" readonly></td>';
        }
        html += '<td class="text-center">' +
            '<a href="#" class="btn btn-danger btn-sm delete-order-product-btn" role="button">' +
            '<span class="nav-icon fas fa-times" data-toggle="tooltip" title="Delete product"></span>' +
            '</a>' +
            '</td>';
        html += '</tr>';
        tbody.append(html);
        $('.pid').select2();
    }

    function clearOrderFields() {
        let inputs = $('.order-fields-container').find('[type="text"],[type="number"]');
        for(let i of inputs) {
            $(i).val('');
        }
    }

    function calculateOrder() {
        let subtotal = $('.total').toArray().reduce((result,item) => result + parseFloat($(item).val()), 0);
        let tax = AppGlobals.Common.calculateTax(subtotal);
        let discount = parseInt($('#discount').val());
        if (Number.isNaN(discount) || discount < 0) {
            discount = 0;
        }
        let paid = parseInt($('#paid').val());
        if (Number.isNaN(paid) || paid < 0) {
            paid = 0;
        }
        let total = subtotal + tax - discount;
        let due = paid - total;

        $('#subtotal').val(subtotal.toFixed(2));
        $('#tax').val(tax.toFixed(2));
        $('#total').val(total.toFixed(2));
        $('#due').val(due);
    }

    if (AppGlobals.Common.order != null) {
        for(let item of AppGlobals.Common.order['items']) {
            appendOrderItem(item);
            calculateOrder();
        }
    }

    $('#orderDatePicker').datetimepicker({ format: 'DD.MM.YYYY' });
    $('#ordersTable').DataTable({
        ordering: false
    });
});

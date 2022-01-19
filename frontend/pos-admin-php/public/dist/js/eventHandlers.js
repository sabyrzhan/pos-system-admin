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

    $('#categoriesTable').DataTable();
    $('#usersTable').DataTable();
    $('#productsTable').DataTable();
    $('[data-toggle="tooltip"]').tooltip();
    $('#orderDatePicker').datetimepicker({ format: 'L' });
})

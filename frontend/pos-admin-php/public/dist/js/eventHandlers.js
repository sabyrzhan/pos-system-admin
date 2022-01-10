$(function() {
    $('.delete-user-btn').on('click', function() {
        let userId = $(this).attr('user-id');
        bootbox.confirm("Do you really want to delete this user?", function(result){
            if (result) {
                $.ajax({
                    url: '/api/users/' + userId,
                    type: 'delete',
                    success: function() {

                    },
                    error: function() {

                    }
                })
            }
        });
    })
})

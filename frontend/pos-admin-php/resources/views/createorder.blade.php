@include('includes.header')
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <div class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1 class="m-0">Create new order</h1>
                </div><!-- /.col -->
            </div><!-- /.row -->
        </div><!-- /.container-fluid -->
    </div>
    <!-- /.content-header -->

    <!-- Main content -->
    <div class="content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-lg-12">
                    <div class="card">
                        <div class="row">
                            <div class="col-lg-6">
                                <div class="card-body">
                                    <div class="form-group">
                                        <label for="name">Customer name</label>
                                        <input type="text" class="form-control" id="customerName" name="customerName" value="" placeholder="Enter name...">
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-6">
                                <div class="card-body">
                                    <div class="form-group">
                                        <label for="name">Date</label>
                                        <div class="input-group date" id="orderDatePicker" data-target-input="nearest">
                                            <div class="input-group-prepend" data-target="#orderDatePicker" data-toggle="datetimepicker">
                                                <div class="input-group-text"><i class="fa fa-calendar"></i></div>
                                            </div>
                                            <input type="text" class="form-control datetimepicker-input" name="orderDate" data-target="#orderDatePicker" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- /.row -->
        </div><!-- /.container-fluid -->
    </div>
    <!-- /.content -->
</div>
<!-- /.content-wrapper -->
@include('includes.foot')

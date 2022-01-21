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
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fa fa-user"></i></div>
                                            </div>
                                            <input type="text" class="form-control" id="customerName" name="customerName" placeholder="Enter name...">
                                        </div>
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
                        <div class="row">
                            <div class="col-lg-12">
                                <div class="card-body">
                                    <div style="overflow-x: auto">
                                        <table class="table table-bordered" id="orderProductsTable">
                                            <thead>
                                            <tr class="text-center">
                                                <th>#</th>
                                                <th>Name</th>
                                                <th>Stock</th>
                                                <th>Price</th>
                                                <th>Quantity</th>
                                                <th>Total</th>
                                                <th>
                                                    <a href="#" class="btn btn-primary btn-sm add-order-product-btn" role="button">
                                                        <span class="nav-icon fas fa-plus" title="Add new product"></span>
                                                    </a>
                                                </th>
                                            </tr>
                                            </thead>
                                            <tbody id="orderProductsTableBody">
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-6">
                                <div class="card-body order-fields-container">
                                    <div class="form-group">
                                        <label for="name">Subtotal</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fa fa-dollar-sign"></i></div>
                                            </div>
                                            <input type="text" class="form-control" id="subtotal" name="subtotal">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="name">Tax (%)</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fa fa-dollar-sign"></i></div>
                                            </div>
                                            <input type="text" class="form-control" id="tax" name="tax">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="name">Discount</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fa fa-dollar-sign"></i></div>
                                            </div>
                                            <input type="number" min="0" step="1" class="form-control" id="discount" name="discount" value="0">
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-6">
                                <div class="card-body">
                                    <div class="form-group">
                                        <label for="name">Total</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fa fa-dollar-sign"></i></div>
                                            </div>
                                            <input type="text" class="form-control" id="total" name="total">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="name">Paid</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fa fa-dollar-sign"></i></div>
                                            </div>
                                            <input type="number" min="0" value="0" class="form-control" id="paid" name="paid">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="name">Due</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fa fa-dollar-sign"></i></div>
                                            </div>
                                            <input type="text" class="form-control" id="due" name="due">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="name">Payment method</label>
                                        <div class="input-group">
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" name="paymentMethod" id="paymentMethodCash" value="CASH">
                                                <label class="form-check-label" for="paymentMethodCash">CASH</label>
                                            </div>
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" name="paymentMethod" id="paymentMethodCard" value="CARD">
                                                <label class="form-check-label" for="paymentMethodCard">CARD</label>
                                            </div>
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" name="paymentMethod" id="paymentMethodCheck" value="CHECK">
                                                <label class="form-check-label" for="paymentMethodCheck">CHECK</label>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="card-footer">
                            <button type="submit" class="btn btn-primary float-right">Create order</button>
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
@include('includes.foot_start')
<script type="text/javascript">
    $(function() {
        @if(isset($products))
            AppGlobals.Common.products = @json($products);
        @endif
    });
</script>
<script src="/dist/js/eventHandlers.orders.create.js"></script>
@include('includes.foot_end')

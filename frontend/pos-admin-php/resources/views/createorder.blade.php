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
                        @if(Session::has('error'))
                            <div class="alert alert-danger" role="alert">
                                {{ Session::get('error') }}
                            </div>
                        @endif
                        @if(Session::has('success'))
                            <div class="alert alert-primary" role="alert">
                                {{ Session::get('success') }}
                            </div>
                        @endif
                        <form action="{{ URL::route('add_order') }}" method="post">
                            @csrf
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
                                                <input type="text" class="form-control datetimepicker-input" name="created" data-target="#orderDatePicker" value="{{ date('d.m.Y') }}" readonly />
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
                                                <input type="text" class="form-control" id="subtotal" name="subtotal" readonly>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="name">Tax (%)</label>
                                            <div class="input-group">
                                                <div class="input-group-prepend">
                                                    <div class="input-group-text"><i class="fa fa-dollar-sign"></i></div>
                                                </div>
                                                <input type="text" class="form-control" id="tax" name="tax" readonly>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="name">Discount</label>
                                            <div class="input-group">
                                                <div class="input-group-prepend">
                                                    <div class="input-group-text"><i class="fa fa-dollar-sign"></i></div>
                                                </div>
                                                <input type="number" min="0" step="1" class="form-control" id="discount" name="discount">
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
                                                <input type="text" class="form-control" id="total" name="total" readonly>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="name">Paid</label>
                                            <div class="input-group">
                                                <div class="input-group-prepend">
                                                    <div class="input-group-text"><i class="fa fa-dollar-sign"></i></div>
                                                </div>
                                                <input type="number" min="0" class="form-control" id="paid" name="paid">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="name">Due</label>
                                            <div class="input-group">
                                                <div class="input-group-prepend">
                                                    <div class="input-group-text"><i class="fa fa-dollar-sign"></i></div>
                                                </div>
                                                <input type="text" class="form-control" id="due" name="due" readonly>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="name">Payment method</label>
                                            <div class="input-group">
                                                <div class="form-check form-check-inline">
                                                    <input class="form-check-input" type="radio" name="paymentType" id="paymentTypeCash" value="CASH">
                                                    <label class="form-check-label" for="paymentTypeCash">CASH</label>
                                                </div>
                                                <div class="form-check form-check-inline">
                                                    <input class="form-check-input" type="radio" name="paymentType" id="paymentTypeCard" value="CREDIT">
                                                    <label class="form-check-label" for="paymentTypeCard">CARD</label>
                                                </div>
                                                <div class="form-check form-check-inline">
                                                    <input class="form-check-input" type="radio" name="paymentType" id="paymentTypeCheck" value="CHECK">
                                                    <label class="form-check-label" for="paymentTypeCheck">CHECK</label>
                                                </div>

                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="card-footer">
                                <button type="submit" class="btn btn-primary float-right">Create order</button>
                            </div>
                        </form>
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

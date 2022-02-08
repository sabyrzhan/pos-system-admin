@include('includes.header')
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <div class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-12">
                    <h1 class="m-0">Details of order: #{{$order['id']}}</h1>
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
                        <div class="row">
                            <div class="col-lg-6">
                                <div class="card-body">
                                    <div class="form-group">
                                        <label for="name">Customer name</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fa fa-user"></i></div>
                                            </div>
                                            <input type="text" class="form-control" id="customerName" name="customerName" value="{{ $order['customerName'] }}" disabled>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-6">
                                <div class="card-body">
                                    <div class="form-group">
                                        <label for="name">Date</label>
                                        <div class="input-group date" data-target-input="nearest">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fa fa-calendar"></i></div>
                                            </div>
                                            <input type="text" class="form-control" name="created" value="{{ date('d.m.Y') }}" disabled />
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
                                                <th>Price</th>
                                                <th>Quantity</th>
                                                <th>Total</th>
                                            </tr>
                                            </thead>
                                            <tbody id="orderProductsTableBody">
                                            @foreach($order['items'] as $item)
                                                <tr>
                                                    <td>{{ $item['id'] }}</td>
                                                    <td>{{ $item['productName'] }}</td>
                                                    <td>{{ $item['price'] }}</td>
                                                    <td>{{ $item['quantity'] }}</td>
                                                    <td>{{ $item['quantity'] * $item['price'] }}</td>
                                                </tr>
                                            @endforeach
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
                                            <input type="text" class="form-control" id="subtotal" name="subtotal" value="{{ $order['subtotal'] }}" readonly>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="name">Tax (%)</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fa fa-dollar-sign"></i></div>
                                            </div>
                                            <input type="text" class="form-control" id="tax" name="tax" value="{{ $order['tax'] }}" readonly>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="name">Discount</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fa fa-dollar-sign"></i></div>
                                            </div>
                                            <input type="number" min="0" step="1" class="form-control" id="discount" name="discount" value="{{ $order['discount'] }}" readonly>
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
                                            <input type="text" class="form-control" id="total" name="total" value="{{ $order['total'] }}" readonly>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="name">Paid</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fa fa-dollar-sign"></i></div>
                                            </div>
                                            <input type="number" min="0" class="form-control" id="paid" name="paid" value="{{ $order['paid'] }}" readonly>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="name">Due</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fa fa-dollar-sign"></i></div>
                                            </div>
                                            <input type="text" class="form-control" id="due" name="due" value="{{ $order['due'] }}" readonly>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="name">Payment method</label>
                                        <div class="input-group">
                                            @foreach($paymentTypes as $paymentType)
                                                <div class="form-check form-check-inline">
                                                    <input class="form-check-input" type="radio" name="paymentType" id="paymentType{{ $paymentType }}" {{ $order['paymentType'] == $paymentType ? 'checked disabled' : 'disabled' }} value="{{ $paymentType }}" >
                                                    <label class="form-check-label" for="paymentType{{$paymentType}}">{{ $paymentType }}</label>
                                                </div>
                                            @endforeach
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

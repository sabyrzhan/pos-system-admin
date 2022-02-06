@include('includes.header')
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <div class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1 class="m-0">Orders</h1>
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
                    <div class="card card-primary card-outline">
                        <div class="card-body">
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
                            @if(Request::get('error'))
                                <div class="alert alert-danger" role="alert">
                                    {{ Request::get('error') }}
                                </div>
                            @endif
                            @if(Request::has('success'))
                                <div class="alert alert-primary" role="alert">
                                    {{ Request::get('success') }}
                                </div>
                            @endif
                            <div style="overflow-x: auto">
                                <table class="table table-bordered table-striped" id="ordersTable">
                                    <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Customer</th>
                                        <th>Order date</th>
                                        <th>Total</th>
                                        <th>Paid</th>
                                        <th>Due</th>
                                        <th>Status</th>
                                        <th>Payment type</th>
                                        <th>Actions</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    @if($orders)
                                        @foreach($orders as $order)
                                            <tr>
                                                <td><span data-toggle="tooltip" title="{{ $order['id'] }}">{{ substr($order['id'], 0, 3) . '...' }}</span></td>
                                                <td>{{ $order['customerName'] }}</td>
                                                <td>{{ $order['created'] }}</td>
                                                <td>{{ $order['total'] }}</td>
                                                <td>{{ $order['paid'] }}</td>
                                                <td>{{ $order['due'] }}</td>
                                                <td>
                                                    @switch($order['status'])
                                                        @case('NEW')
                                                            <span class="badge bg-primary">{{ $order['status'] }}</span>
                                                        @break
                                                        @case('PROCESSING')
                                                            <span class="badge bg-warning">{{ $order['status'] }}</span>
                                                        @break
                                                        @case('DONE')
                                                            <span class="badge bg-green">{{ $order['status'] }}</span>
                                                        @break
                                                        @case('CANCELLED')
                                                            <span class="badge bg-red">{{ $order['status'] }}</span>
                                                        @break
                                                        @default
                                                            <span>{{ $order['status'] }}</span>
                                                        @break
                                                    @endswitch
                                                </td>
                                                <td>{{ $order['paymentType'] }}</td>
                                                <td>
                                                    <a href="{{ URL::route('view_product', [$order['id']]) }}" class="btn btn-primary" role="button">
                                                        <span class="nav-icon fas fa-print" data-toggle="tooltip" title="Print invoice"></span>
                                                    </a>
                                                    <a href="{{ URL::route('view_product', [$order['id']]) }}" class="btn btn-warning" role="button">
                                                        <span class="nav-icon fas fa-eye" data-toggle="tooltip" title="Order details"></span>
                                                    </a>
                                                    <a href="{{ URL::route('add_order_page') . '?copyFrom=' . $order['id'] }}" class="btn btn-info" role="button">
                                                        <span class="nav-icon fas fa-copy" data-toggle="tooltip" title="Create copy"></span>
                                                    </a>
                                                    @if($order['status'] == 'NEW')
                                                        <a href="#" class="btn btn-danger cancel-order-btn" role="button" id="{{ $order['id'] }}">
                                                            <span class="nav-icon fas fa-trash" data-toggle="tooltip" title="Cancel order"></span>
                                                        </a>
                                                    @endif
                                                </td>
                                            </tr>
                                        @endforeach
                                    @else
                                        <tr>
                                            <td colspan="9">No orders found.</td>
                                        </tr>
                                    @endif
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div><!-- /.card -->
                </div>
            </div>
            <!-- /.row -->
        </div><!-- /.container-fluid -->
    </div>
    <!-- /.content -->
</div>
<!-- /.content-wrapper -->
@include('includes.foot_start')
<script src="/dist/js/eventHandlers.orders.create.js"></script>
@include('includes.foot_end')

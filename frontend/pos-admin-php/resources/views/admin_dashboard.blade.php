@include('includes.adminhead')
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <div class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1 class="m-0">Admin Dashboard</h1>
                </div><!-- /.col -->
            </div><!-- /.row -->
        </div><!-- /.container-fluid -->
    </div>
    <!-- /.content-header -->

    <!-- Main content -->
    <div class="content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-lg-6">
                    <div class="card">
                        <div class="card-header">
                            <h5 class="m-0">Orders</h5>
                        </div>
                        <div class="card-body">
                            <ul class="list-group">
                                <li class="list-group-item d-flex justify-content-between align-items-start">
                                    <div class="ms-2 me-auto">
                                        <div class="font-weight-bold">New orders</div>
                                    </div>
                                    <span class="badge bg-green badge-pill">{{ $info['totalNewOrders'] }}</span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between align-items-start">
                                    <div class="ms-2 me-auto">
                                        <div class="font-weight-bold">Total in progress orders</div>
                                    </div>
                                    <span class="badge bg-cyan badge-pill">{{ $info['totalInProgressOrders'] }}</span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between align-items-start">
                                    <div class="ms-2 me-auto">
                                        <div class="font-weight-bold">Total processed</div>
                                    </div>
                                    <span class="badge bg-primary badge-pill">{{ $info['totalProcessedOrders'] }}</span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between align-items-start">
                                    <div class="ms-2 me-auto">
                                        <div class="font-weight-bold">Total cancelled</div>
                                    </div>
                                    <span class="badge bg-warning badge-pill">{{ $info['totalCancelledOrders'] }}</span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between align-items-start">
                                    <div class="ms-2 me-auto">
                                        <div class="font-weight-bold">Total returned</div>
                                    </div>
                                    <span class="badge bg-red badge-pill">{{ $info['totalReturnedOrders'] }}</span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between align-items-start">
                                    <div class="ms-2 me-auto">
                                        <div class="font-weight-bold">Total products</div>
                                    </div>
                                    <span class="badge bg-black badge-pill">{{ $info['totalProducts'] }}</span>
                                </li>
                            </ul>
                            <p>&nbsp;</p>
                            <a href="{{ URL::route('get_orders_page') }}" class="btn btn-primary">Go to orders</a>
                        </div>
                    </div>
                </div>
                <!-- /.col-md-6 -->
                <div class="col-lg-6">
                    <div class="card">
                        <div class="card-header">
                            <h5 class="m-0">Products with top orders</h5>
                        </div>
                        <div class="card-body">
                            <ul class="list-group">
                                @if($info['topProducts'])
                                    @foreach($info['topProducts'] as $name => $count)
                                        <li class="list-group-item d-flex justify-content-between align-items-start">
                                            <div class="ms-2 me-auto">
                                                <div class="font-weight-bold">{{ $name }}</div>
                                            </div>
                                            <span class="badge bg-primary badge-pill">{{ $count }}</span>
                                        </li>
                                    @endforeach
                                @else
                                    No processed orders.
                                @endif
                            </ul>
                            <p>&nbsp;</p>
                            <a href="{{ URL::route('products_page') }}" class="btn btn-primary">Go to Products</a>
                        </div>
                    </div>
                </div>
                <!-- /.col-md-6 -->
            </div>
            <!-- /.row -->
        </div><!-- /.container-fluid -->
    </div>
    <!-- /.content -->
</div>
<!-- /.content-wrapper -->
@include('includes.foot')

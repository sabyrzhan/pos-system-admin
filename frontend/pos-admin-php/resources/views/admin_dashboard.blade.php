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
                                    <span class="badge bg-green badge-pill">10</span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between align-items-start">
                                    <div class="ms-2 me-auto">
                                        <div class="font-weight-bold">Total processed</div>
                                    </div>
                                    <span class="badge bg-primary badge-pill">100</span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between align-items-start">
                                    <div class="ms-2 me-auto">
                                        <div class="font-weight-bold">Total cancelled</div>
                                    </div>
                                    <span class="badge bg-warning badge-pill">200</span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between align-items-start">
                                    <div class="ms-2 me-auto">
                                        <div class="font-weight-bold">Total returned</div>
                                    </div>
                                    <span class="badge bg-red badge-pill">2000</span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between align-items-start">
                                    <div class="ms-2 me-auto">
                                        <div class="font-weight-bold">Total products</div>
                                    </div>
                                    <span class="badge bg-black badge-pill">2000</span>
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
                                <li class="list-group-item d-flex justify-content-between align-items-start">
                                    <div class="ms-2 me-auto">
                                        <div class="font-weight-bold">iPhone 13</div>
                                    </div>
                                    <span class="badge bg-primary badge-pill">1120</span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between align-items-start">
                                    <div class="ms-2 me-auto">
                                        <div class="font-weight-bold">iPhone 12 Pro</div>
                                    </div>
                                    <span class="badge bg-primary badge-pill">1000</span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between align-items-start">
                                    <div class="ms-2 me-auto">
                                        <div class="font-weight-bold">MacBook Pro 16</div>
                                    </div>
                                    <span class="badge bg-primary badge-pill">887</span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between align-items-start">
                                    <div class="ms-2 me-auto">
                                        <div class="font-weight-bold">iPad 8</div>
                                    </div>
                                    <span class="badge bg-primary badge-pill">600</span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between align-items-start">
                                    <div class="ms-2 me-auto">
                                        <div class="font-weight-bold">Google Pixel 6</div>
                                    </div>
                                    <span class="badge bg-primary badge-pill">443</span>
                                </li>
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

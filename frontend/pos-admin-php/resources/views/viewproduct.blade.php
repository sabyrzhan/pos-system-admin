@include('includes.header')
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <div class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1 class="m-0">
                        Product details
                    </h1>
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
                    <div class="card card-primary">
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
                        <form method="post" action="{{  URL::route('add_product') }}">
                            @csrf
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="card-body">
                                        <ul class="list-group">
                                            <li class="list-group-item active">
                                                <div class="font-weight-bold text-center">Product details</div>
                                            </li>
                                            <li class="list-group-item d-flex justify-content-between align-items-start">
                                                <div class="ms-2 me-auto">
                                                    <div class="font-weight-bold">ID</div>
                                                </div>
                                                <span class="badge bg-primary badge-pill">{{ $product['id'] }}</span>
                                            </li>
                                            <li class="list-group-item d-flex justify-content-between align-items-start">
                                                <div class="ms-2 me-auto">
                                                    <div class="font-weight-bold">Name</div>
                                                </div>
                                                <span class="badge bg-primary badge-pill">{{ $product['name'] }}</span>
                                            </li>
                                            <li class="list-group-item d-flex justify-content-between align-items-start">
                                                <div class="ms-2 me-auto">
                                                    <div class="font-weight-bold">Category</div>
                                                </div>
                                                <span class="badge bg-primary badge-pill">{{ $product['category'] }}</span>
                                            </li>
                                            <li class="list-group-item d-flex justify-content-between align-items-start">
                                                <div class="ms-2 me-auto">
                                                    <div class="font-weight-bold">Purchase price</div>
                                                </div>
                                                <span class="badge bg-primary badge-pill">{{ $product['purchasePrice'] }}</span>
                                            </li>
                                            <li class="list-group-item d-flex justify-content-between align-items-start">
                                                <div class="ms-2 me-auto">
                                                    <div class="font-weight-bold">Sale price</div>
                                                </div>
                                                <span class="badge bg-primary badge-pill">{{ $product['salePrice'] }}</span>
                                            </li>
                                            <li class="list-group-item d-flex justify-content-between align-items-start">
                                                <div class="ms-2 me-auto">
                                                    <div class="font-weight-bold">Product sale profit</div>
                                                </div>
                                                <span class="badge bg-green badge-pill">{{ $product['salePrice'] - $product['purchasePrice'] }}</span>
                                            </li>
                                            <li class="list-group-item d-flex justify-content-between align-items-start">
                                                <div class="ms-2 me-auto">
                                                    <div class="font-weight-bold">Stock</div>
                                                </div>
                                                <span class="badge bg-primary badge-pill">{{ $product['stock'] }}</span>
                                            </li>
                                            <li class="list-group-item d-flex justify-content-between align-items-start">
                                                <div class="ms-2 me-auto">
                                                    <div class="font-weight-bold">Description</div>
                                                    {{ $product['description'] }}
                                                </div>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="card-body">
                                        <ul class="list-group">
                                            <li class="list-group-item active">
                                                <div class="font-weight-bold text-center">Product image</div>
                                            </li>
                                            <div class="text-center">
                                                <img width="90%" src="https://res.cloudinary.com/telia-pim/image/upload/v1598898405/pood/apple/sulearvutid/macbook-pro-13-256-gb/hobedane/mxk62ksa/apple-macbook-pro-13-hobedane-pealt.jpg" />
                                            </div>
                                        </ul>
                                    </div>
                                </div>
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
@include('includes.foot')

@include('includes.header')
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <div class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1 class="m-0">Product list</h1>
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
                                <table class="table table-bordered table-striped" id="productsTable">
                                    <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Name</th>
                                        <th>Category</th>
                                        <th>Purchase price</th>
                                        <th>Sale price</th>
                                        <th>Stock</th>
                                        <th>Description</th>
                                        <th>Image</th>
                                        <th>Actions</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    @if($products)
                                        @foreach($products as $prod)
                                            <tr>
                                                <td>{{ $prod['id'] }}</td>
                                                <td>{{ $prod['name'] }}</td>
                                                <td>{{ $prod['category'] }}</td>
                                                <td>{{ $prod['purchasePrice'] }}</td>
                                                <td>{{ $prod['salePrice'] }}</td>
                                                <td>{{ $prod['stock'] }}</td>
                                                <td>{{ $prod['description'] }}</td>
                                                <td>{{ $prod['images'] }}</td>
                                                <td>
                                                    <a href="{{ URL::route('view_product', [$prod['id']]) }}" class="btn btn-primary" role="button">
                                                        <span class="nav-icon fas fa-eye" data-toggle="tooltip" title="View product"></span>
                                                    </a>
                                                    <a href="{{ URL::route('add_product_page') . '?id=' . $prod['id'] }}" class="btn btn-warning" role="button">
                                                        <span class="nav-icon fas fa-pen" data-toggle="tooltip" title="Edit product"></span>
                                                    </a>
                                                    <a href="#" class="btn btn-danger del-prod-btn" role="button" id="{{ $prod['id'] }}">
                                                        <span class="nav-icon fas fa-trash"  data-toggle="tooltip" title="Delete product"></span>
                                                    </a>
                                                </td>
                                            </tr>
                                        @endforeach
                                    @else
                                        <tr>
                                            <td colspan="9">No Products found.</td>
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
@include('includes.foot')

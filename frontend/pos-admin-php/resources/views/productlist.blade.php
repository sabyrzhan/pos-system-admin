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
                                                <a href="{{ URL::route('add_product_page') . '?id=' . $prod['id'] }}" class="btn btn-primary" role="button">
                                                    <span class="nav-icon fas fa-eye" title="View"></span>
                                                </a>
                                                <a href="{{ URL::route('add_product_page') . '?id=' . $prod['id'] }}" class="btn btn-warning" role="button">
                                                    <span class="nav-icon fas fa-pen" title="Edit"></span>
                                                </a>
                                                <a href="#" class="btn btn-danger del-prod-btn" role="button">
                                                    <span class="nav-icon fas fa-trash" title="Delete"></span>
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

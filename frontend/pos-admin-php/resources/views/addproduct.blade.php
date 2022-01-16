@include('includes.header')
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <div class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1 class="m-0">
                        @if(isset($product['id']))
                            Update product
                        @else
                            Add new product
                        @endif
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
                        <div class="card-header">
                            <h3 class="card-title">Product form</h3>
                        </div>
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
                                        <div class="form-group">
                                            <label for="name">Product name</label>
                                            <input type="text" class="form-control" id="name" name="name" value="{{ $product['name'] ?? '' }}" placeholder="Enter name...">
                                        </div>
                                        <div class="form-group">
                                            <label for="category">Category</label>
                                            <select class="custom-select form-control-border" id="categoryId" name="categoryId">
                                                <option disabled selected>-- Choose --</option>
                                                @foreach($categories as $cat)
                                                    <option {{ isset($product) && $product['categoryId'] == $cat['id'] ? 'selected' : '' }} value="{{$cat['id']}}">{{ $cat['name'] }}</option>
                                                @endforeach
                                            </select>
                                        </div>
                                        <div class="form-group">
                                            <label for="purchasePrice">Purchase price</label>
                                            <input type="number" step="1" min="1" class="form-control" id="purchasePrice" name="purchasePrice" value="{{ $product['purchasePrice'] ?? '' }}" placeholder="Enter...">
                                        </div>
                                        <div class="form-group">
                                            <label for="salePrice">Sale price</label>
                                            <input type="number" step="1" min="1" class="form-control" id="salePrice" name="salePrice" value="{{ $product['salePrice'] ?? '' }}" placeholder="Enter...">
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="card-body">
                                        <div class="form-group">
                                            <label for="stock">Stock</label>
                                            <input type="number" step="1" min="1" class="form-control" id="stock" name="stock" value="{{ $product['stock'] ?? '' }}" placeholder="Enter...">
                                        </div>
                                        <div class="form-group">
                                            <label for="description">Description</label>
                                            <textarea class="form-control" rows="3" name="description" name="description" placeholder="Enter ...">{{ $product['description'] ?? '' }}</textarea>
                                        </div>
                                        <div class="form-group">
                                            <label for="description">Product image</label>
                                            <input type="file" class="input-group" name="image" multiple />
                                            <p>upload image</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="card-footer">
                                @if(isset($product['id']))
                                    <input type="hidden" name="id" value="{{ $product['id'] ?? '' }}" />
                                    <button type="submit" class="btn btn-primary">Update</button>
                                @else
                                    <button type="submit" class="btn btn-primary">Add</button>
                                @endif
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

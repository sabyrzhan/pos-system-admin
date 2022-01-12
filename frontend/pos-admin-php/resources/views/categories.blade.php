@include('includes.header')
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <div class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1 class="m-0">Categories</h1>
                </div><!-- /.col -->
            </div><!-- /.row -->
        </div><!-- /.container-fluid -->
    </div>
    <!-- /.content-header -->

    <!-- Main content -->
    <div class="content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-4">
                    <div class="card card-primary">
                        <div class="card-header">
                            <h3 class="card-title">Category form</h3>
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
                        <form method="post" action="{{  URL::route('add_category') }}">
                            @csrf
                            <div class="card-body">
                                <div class="form-group">
                                    <label for="category">Category</label>
                                    <input type="text" class="form-control" id="name" name="name" placeholder="Enter category name">
                                </div>
                            </div>
                            <div class="card-footer">
                                <button type="submit" class="btn btn-primary">Add</button>
                            </div>
                        </form>
                    </div>
                </div>
                <div class="col-md-8">
                    <div class="card card-primary">
                        <div class="card-header">
                            <h3 class="card-title">Category list</h3>
                        </div>
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
                        <table class="table table-striped">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>CATEGORY</th>
                                <th>EDIT</th>
                                <th>DELETE</th>
                            </tr>
                            </thead>
                            <tbody>
                            @if($categories)
                                @foreach($categories as $cat)
                                    <tr>
                                        <td>{{ $cat['id'] }}</td>
                                        <td>{{ $cat['name'] }}</td>
                                        <td>
                                            <a href="#" class="btn btn-danger edit-cat-btn" role="button" cat-id="{{ $cat['id'] }}">
                                                <span class="nav-icon fas fa-pen" title="Edit"></span>
                                            </a>
                                        </td>
                                        <td>
                                            <a href="#" class="btn btn-danger delete-cat-btn" role="button" cat-id="{{ $cat['id'] }}">
                                                <span class="nav-icon fas fa-trash" title="Delete"></span>
                                            </a>
                                        </td>
                                    </tr>
                                @endforeach
                            @else
                                <tr>
                                    <td colspan="5">No categories found.</td>
                                </tr>
                            @endif
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div><!-- /.container-fluid -->
    </div>
    <!-- /.content -->
</div>
<!-- /.content-wrapper -->
@include('includes.foot')

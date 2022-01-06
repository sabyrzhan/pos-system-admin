@if(auth()->user()->isAdmin())
    @include('includes.adminhead')
@else
    @include('includes.userhead')
@endif

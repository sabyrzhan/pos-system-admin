package kz.sabyrzhan.resources;

import kz.sabyrzhan.exceptions.UserAlreadyExistsException;
import kz.sabyrzhan.exceptions.UserNotFoundException;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import static org.jboss.resteasy.reactive.RestResponse.Status.CONFLICT;
import static org.jboss.resteasy.reactive.RestResponse.Status.NOT_FOUND;

public class ExceptionMappers {
    @ServerExceptionMapper
    public RestResponse<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException e) {
        return RestResponse.status(CONFLICT, new ErrorResponse(e.getMessage()));
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> handleUserNotFound(UserNotFoundException e) {
        return RestResponse.status(NOT_FOUND, new ErrorResponse(e.getMessage()));
    }
}

package kz.sabyrzhan.resources;

import kz.sabyrzhan.exceptions.EntityAlreadyExistsException;
import kz.sabyrzhan.exceptions.EntityNotFoundException;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import static org.jboss.resteasy.reactive.RestResponse.Status.CONFLICT;
import static org.jboss.resteasy.reactive.RestResponse.Status.NOT_FOUND;

public class ExceptionMappers {
    @ServerExceptionMapper
    public RestResponse<ErrorResponse> handleEntityAlreadyExists(EntityAlreadyExistsException e) {
        return RestResponse.status(CONFLICT, new ErrorResponse(e.getMessage()));
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> handleEntityNotFound(EntityNotFoundException e) {
        return RestResponse.status(NOT_FOUND, new ErrorResponse(e.getMessage()));
    }
}

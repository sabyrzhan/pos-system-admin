package kz.sabyrzhan.resources;

import kz.sabyrzhan.exceptions.EntityAlreadyExistsException;
import kz.sabyrzhan.exceptions.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import static org.jboss.resteasy.reactive.RestResponse.Status.*;

@Slf4j
public class ExceptionMappers {
    @ServerExceptionMapper
    public RestResponse<ErrorResponse> handleEntityAlreadyExists(EntityAlreadyExistsException e) {
        return RestResponse.status(CONFLICT, new ErrorResponse(e.getMessage()));
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> handleEntityNotFound(EntityNotFoundException e) {
        return RestResponse.status(NOT_FOUND, new ErrorResponse(e.getMessage()));
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> handleException(Exception e) {
        log.error("Internal Server Error: {}", e, e);
        return RestResponse.status(INTERNAL_SERVER_ERROR, new ErrorResponse("Internal Server Error"));
    }
}

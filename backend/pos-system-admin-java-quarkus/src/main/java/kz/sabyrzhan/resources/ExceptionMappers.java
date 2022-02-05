package kz.sabyrzhan.resources;

import kz.sabyrzhan.exceptions.EntityAlreadyExistsException;
import kz.sabyrzhan.exceptions.EntityNotFoundException;
import kz.sabyrzhan.exceptions.InvalidOrderItemsException;
import kz.sabyrzhan.exceptions.InvalidStatusException;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import javax.ws.rs.NotAllowedException;

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

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> handleMethodNotAllowed(NotAllowedException e) {
        return RestResponse.status(METHOD_NOT_ALLOWED, new ErrorResponse("Method not allowed"));
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> handleInvalidOrderItems(InvalidOrderItemsException e) {
        return RestResponse.status(BAD_REQUEST, new ErrorResponse(e.getMessage()));
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        return RestResponse.status(BAD_REQUEST, new ErrorResponse(e.getMessage()));
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> handleInvalidStatusException(InvalidStatusException e) {
        return RestResponse.status(FORBIDDEN, new ErrorResponse(e.getMessage()));
    }
}

package bitxon.micronaut.errorhandler;

import bitxon.common.api.model.error.ErrorResponse;
import bitxon.common.exception.ResourceNotFoundException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

import java.util.List;

@Produces
@Singleton
@Requires(classes = {ResourceNotFoundException.class, ExceptionHandler.class})
public class ResourceNotFoundExceptionHandler implements ExceptionHandler<ResourceNotFoundException, HttpResponse> {
    @Override
    public HttpResponse handle(HttpRequest request, ResourceNotFoundException ex) {
        return HttpResponse
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(List.of(ex.getMessage())));
    }
}

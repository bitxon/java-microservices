package bitxon.micronaut.errorhandler;

import bitxon.common.api.model.error.ErrorResponse;
import bitxon.common.exception.DirtyTrickException;
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
@Requires(classes = {DirtyTrickException.class, ExceptionHandler.class})
public class DirtyTrickExceptionHandler implements ExceptionHandler<DirtyTrickException, HttpResponse> {
    @Override
    public HttpResponse handle(HttpRequest request, DirtyTrickException ex) {
        return HttpResponse
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse(List.of(ex.getMessage())));
    }
}

package bitxon.micronaut.errorhandler;

import bitxon.common.api.model.error.ErrorResponse;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Produces
@Primary // to override default: io.micronaut.validation.exceptions.ConstraintExceptionHandler
@Singleton
@Requires(classes = {ConstraintViolationException.class, ExceptionHandler.class})
public class ConstraintViolationExceptionHandler implements ExceptionHandler<ConstraintViolationException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, ConstraintViolationException ex) {
        var messages = ex.getConstraintViolations().stream()
            .map(error -> String.format("'%s' %s", extractPath(error.getPropertyPath()), error.getMessage()))
            .collect(Collectors.toList());
        return HttpResponse
            .status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(new ErrorResponse(messages));
    }

    private static Object extractPath(Path path) {
        return StreamSupport.stream(path.spliterator(), false)
            .skip(2) // Exclude method and entity names from path
            .map(Path.Node::toString)
            .collect(Collectors.joining("."));
    }
}

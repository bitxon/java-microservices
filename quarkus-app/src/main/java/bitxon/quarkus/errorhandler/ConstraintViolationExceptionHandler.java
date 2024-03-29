package bitxon.quarkus.errorhandler;

import bitxon.common.api.model.error.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Provider
public class ConstraintViolationExceptionHandler implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException ex) {
        var messages = ex.getConstraintViolations().stream()
            .map(error -> String.format("'%s' %s", extractPath(error.getPropertyPath()), error.getMessage()))
            .collect(Collectors.toList());
        return Response
            .status(422)
            .entity(new ErrorResponse(messages)).build();
    }

    private static Object extractPath(Path path) {
        return StreamSupport.stream(path.spliterator(), false)
            .skip(2) // Exclude method and entity names from path
            .map(Path.Node::toString)
            .collect(Collectors.joining("."));
    }
}

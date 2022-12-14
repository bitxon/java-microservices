package bitxon.dropwizard.errorhandler;

import javax.validation.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import bitxon.common.api.model.error.ErrorResponse;
import io.dropwizard.jersey.validation.JerseyViolationException;

@Provider
public class JerseyViolationExceptionHandler implements ExceptionMapper<JerseyViolationException> {
    @Override
    public Response toResponse(JerseyViolationException ex) {
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

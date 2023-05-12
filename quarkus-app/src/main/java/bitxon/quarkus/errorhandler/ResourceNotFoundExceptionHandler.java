package bitxon.quarkus.errorhandler;

import bitxon.common.api.model.error.ErrorResponse;
import bitxon.common.exception.ResourceNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;

@Provider
public class ResourceNotFoundExceptionHandler implements ExceptionMapper<ResourceNotFoundException> {

    @Override
    public Response toResponse(ResourceNotFoundException ex) {
        return Response
            .status(404)
            .entity(new ErrorResponse(List.of(ex.getMessage()))).build();
    }
}

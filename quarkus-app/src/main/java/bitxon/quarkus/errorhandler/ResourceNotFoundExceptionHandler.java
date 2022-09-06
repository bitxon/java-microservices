package bitxon.quarkus.errorhandler;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;

import bitxon.common.api.model.error.ErrorResponse;
import bitxon.common.exception.ResourceNotFoundException;

@Provider
public class ResourceNotFoundExceptionHandler implements ExceptionMapper<ResourceNotFoundException> {

    @Override
    public Response toResponse(ResourceNotFoundException ex) {
        return Response
            .status(404)
            .entity(new ErrorResponse(List.of(ex.getMessage()))).build();
    }
}

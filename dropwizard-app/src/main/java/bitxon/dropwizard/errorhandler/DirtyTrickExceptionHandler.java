package bitxon.dropwizard.errorhandler;

import bitxon.common.api.model.error.ErrorResponse;
import bitxon.common.exception.DirtyTrickException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;

@Provider
public class DirtyTrickExceptionHandler implements ExceptionMapper<DirtyTrickException> {

    @Override
    public Response toResponse(DirtyTrickException ex) {
        return Response
            .status(500)
            .entity(new ErrorResponse(List.of(ex.getMessage()))).build();
    }
}

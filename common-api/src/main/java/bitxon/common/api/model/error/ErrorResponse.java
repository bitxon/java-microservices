package bitxon.common.api.model.error;

import java.util.List;

import lombok.Builder;

@Builder
public record ErrorResponse(
    List<String> errors
) {
}

package bitxon.dropwizard.api.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is copy of common-api model.
 * Dropwizard 4 works with jakarta 3 and requires package name update for annotations
 * 'javax.validation.' -> 'jakarta.validation.'
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoneyTransfer {
    @Min(1)
    @NotNull
    Long senderId;
    @Min(1)
    @NotNull
    Long recipientId;
    @PositiveOrZero
    @NotNull
    Integer moneyAmount;
}

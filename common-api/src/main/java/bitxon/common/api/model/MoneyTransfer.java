package bitxon.common.api.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

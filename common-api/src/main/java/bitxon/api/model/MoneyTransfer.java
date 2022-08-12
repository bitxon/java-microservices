package bitxon.api.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

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
    Integer moneyAmount;
}

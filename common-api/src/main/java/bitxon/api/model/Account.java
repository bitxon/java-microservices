package bitxon.api.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    Long id;
    @NotEmpty
    String email;
    @Pattern(regexp = "USD|EUR|GBP")
    String currency;
    @PositiveOrZero
    Integer moneyAmount;

}

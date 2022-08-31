package bitxon.api.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

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
    @Email
    String email;
    @NotBlank
    String firstName;
    @NotBlank
    String lastName;
    @NotNull
    @Past
    LocalDate dateOfBirth;
    @NotNull
    @Pattern(regexp = "USD|EUR|GBP")
    String currency;
    @NotNull
    @PositiveOrZero
    Integer moneyAmount;

}

package bitxon.common.api.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record Account(
    Long id,
    @NotEmpty
    @Email
    String email,
    @NotBlank
    String firstName,
    @NotBlank
    String lastName,
    @NotNull
    @Past
    LocalDate dateOfBirth,
    @NotNull
    @Pattern(regexp = "USD|EUR|GBP")
    String currency,
    @NotNull
    @PositiveOrZero
    Integer moneyAmount

) {
}

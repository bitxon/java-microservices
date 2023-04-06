package bitxon.quarkus.api.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * This is copy of common-api model.
 * Spring boot 3 works with jakarta 3 and requires package name update for annotations
 * 'javax.validation.' -> 'jakarta.validation.'
 */
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

package bitxon.spring.api.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

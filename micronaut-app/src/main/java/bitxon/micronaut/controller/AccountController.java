package bitxon.micronaut.controller;

import static bitxon.api.constant.Constants.DIRTY_TRICK_HEADER;
import static bitxon.api.constant.Constants.DirtyTrick.FAIL_TRANSFER;

import javax.validation.Valid;
import java.util.List;
import java.util.Random;

import bitxon.api.model.Account;
import bitxon.api.model.MoneyTransfer;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;


@Controller("/accounts")
public class AccountController {

    @Get
    public List<Account> getAll() {
        return List.of();
    }

    @Get("/{id}")
    public Account getById(@PathVariable("id") Long id) {
        return Account.builder().id(id).build();
    }

    @Post
    public Account create(@Body @Valid Account account) {
        return Account.builder()
            .id(new Random().nextLong(0, Long.MAX_VALUE))
            .email(account.getEmail())
            .currency(account.getCurrency())
            .moneyAmount(account.getMoneyAmount())
            .build();
    }

    @Post("/transfers")
    @Status(HttpStatus.NO_CONTENT)
    public void create(@Body @Valid MoneyTransfer transfer,
                       @Header(value = DIRTY_TRICK_HEADER) @Nullable String dirtyTrick) {

    }
}
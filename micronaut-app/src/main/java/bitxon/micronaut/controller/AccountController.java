package bitxon.micronaut.controller;

import static bitxon.common.api.constant.Constants.DIRTY_TRICK_HEADER;
import static bitxon.common.api.constant.Constants.DirtyTrick.FAIL_TRANSFER;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import bitxon.common.api.model.Account;
import bitxon.common.api.model.MoneyTransfer;
import bitxon.micronaut.client.exchange.ExchangeClient;
import bitxon.micronaut.db.AccountDao;
import bitxon.micronaut.mapper.AccountMapper;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.transaction.annotation.ReadOnly;
import io.micronaut.transaction.annotation.TransactionalAdvice;
import lombok.RequiredArgsConstructor;

@ExecuteOn(TaskExecutors.IO)
@Controller("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountDao dao;
    private final AccountMapper mapper;
    private final ExchangeClient exchangeClient;

    @Get
    @ReadOnly
    public List<Account> getAll() {
        return dao.findAll().stream()
            .map(mapper::mapToApi)
            .collect(Collectors.toList());
    }

    @Get("/{id}")
    @ReadOnly
    public Account getById(@PathVariable("id") Long id) {
        return dao.findById(id)
            .map(mapper::mapToApi)
            .orElseThrow(() -> new RuntimeException("Resource not found"));
    }

    @Post
    @TransactionalAdvice
    public Account create(@Body @Valid Account account) {
        return Optional.of(account)
            .map(mapper::mapToDb)
            .map(dao::save)
            .map(mapper::mapToApi)
            .get();
    }

    @Post("/transfers")
    @Status(HttpStatus.NO_CONTENT)
    @TransactionalAdvice
    public void transfer(@Body @Valid MoneyTransfer transfer,
                         @Header(value = DIRTY_TRICK_HEADER) @Nullable String dirtyTrick) {
        var sender = dao.findById(transfer.getSenderId())
            .orElseThrow(() -> new RuntimeException("Sender not found"));

        var recipient = dao.findById(transfer.getRecipientId())
            .orElseThrow(() -> new RuntimeException("Recipient not found"));

        var exchangeRateValue = exchangeClient.getExchangeRate(sender.getCurrency())
            .getRates().getOrDefault(recipient.getCurrency(), 1.0);

        sender.setMoneyAmount(sender.getMoneyAmount() - transfer.getMoneyAmount());
        dao.save(sender);

        if (FAIL_TRANSFER.equals(dirtyTrick)) {
            throw new RuntimeException("Error during money transfer");
        }

        recipient.setMoneyAmount(recipient.getMoneyAmount() + (int) (transfer.getMoneyAmount() * exchangeRateValue));
        dao.save(recipient);

    }


}
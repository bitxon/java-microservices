package bitxon.spring.controller;

import static bitxon.common.api.constant.Constants.DIRTY_TRICK_HEADER;
import static bitxon.common.api.constant.Constants.DirtyTrick.FAIL_TRANSFER;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import bitxon.common.exception.ResourceNotFoundException;
import bitxon.spring.api.model.Account;
import bitxon.spring.api.model.MoneyTransfer;
import bitxon.spring.client.ExchangeClient;
import bitxon.spring.db.AccountDao;
import bitxon.spring.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountDao dao;
    private final AccountMapper mapper;
    private final ExchangeClient exchangeClient;

    @GetMapping
    @Transactional(readOnly = true)
    public List<Account> getAll() {
        return dao.findAll().stream()
            .map(mapper::mapToApi)
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public Account getById(@PathVariable("id") Long id) {
        return dao.findById(id)
            .map(mapper::mapToApi)
            .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
    }

    @PostMapping
    @Transactional
    public Account create(@Valid @RequestBody Account account) {
        return Optional.of(account)
            .map(mapper::mapToDb)
            .map(dao::save)
            .map(mapper::mapToApi)
            .get();
    }

    @PostMapping("/transfers")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void transfer(@Valid @RequestBody MoneyTransfer transfer,
                         @RequestHeader(value = DIRTY_TRICK_HEADER, required = false) String dirtyTrick) {

        var sender = dao.findById(transfer.getSenderId())
            .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));

        var recipient = dao.findById(transfer.getRecipientId())
            .orElseThrow(() -> new ResourceNotFoundException("Recipient not found"));

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

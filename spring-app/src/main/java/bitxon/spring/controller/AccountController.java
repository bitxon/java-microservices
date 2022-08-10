package bitxon.spring.controller;

import javax.validation.Valid;
import java.util.List;

import bitxon.api.model.Account;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @GetMapping
    public List<Account> getAll() {
        return List.of(
            Account.builder().build()
        );
    }

    @GetMapping("/{id}")
    public Account getById(@PathVariable("id") Long id) {
        return Account.builder().id(id).build();
    }

    @PostMapping
    public Account create(@Valid @RequestBody Account account) {
        return account;
    }
}

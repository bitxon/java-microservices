package bitxon.dropwizard.resource;

import static bitxon.api.constant.Constants.DIRTY_TRICK_HEADER;
import static bitxon.api.constant.Constants.DirtyTrick.FAIL_TRANSFER;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import bitxon.api.model.Account;
import bitxon.api.model.MoneyTransfer;
import bitxon.dropwizard.client.exchange.ExchangeClient;
import bitxon.dropwizard.db.AccountDao;
import bitxon.dropwizard.mapper.AccountMapper;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.RequiredArgsConstructor;


@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AccountResource {

    private final AccountDao dao;
    private final AccountMapper mapper;
    private final ExchangeClient exchangeClient;

    @GET
    @UnitOfWork
    public List<Account> getAll() {
        return dao.findAll().stream()
            .map(mapper::mapToApi)
            .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    public Account getById(@PathParam("id") Long id) {
        return dao.findById(id)
            .map(mapper::mapToApi)
            .orElseThrow(() -> new RuntimeException("Resource not found"));
    }

    @POST
    @UnitOfWork
    public Account create(@NotNull @Valid Account account) {
        return Optional.of(account)
            .map(mapper::mapToDb)
            .map(dao::save)
            .map(mapper::mapToApi)
            .get();
    }

    @POST
    @Path("/transfers")
    @UnitOfWork
    public void transfer(@NotNull @Valid MoneyTransfer transfer,
                         @HeaderParam(DIRTY_TRICK_HEADER) String dirtyTrick) {

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

        recipient.setMoneyAmount(recipient.getMoneyAmount() + (int)(transfer.getMoneyAmount() * exchangeRateValue));
        dao.save(recipient);
    }
}

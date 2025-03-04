package bitxon.dropwizard.resource;

import static bitxon.common.api.constant.Constants.DIRTY_TRICK_HEADER;
import static bitxon.common.api.constant.Constants.DirtyTrick.FAIL_TRANSFER;

import bitxon.common.api.model.Account;
import bitxon.common.api.model.MoneyTransfer;
import bitxon.common.exception.DirtyTrickException;
import bitxon.common.exception.ResourceNotFoundException;
import bitxon.dropwizard.client.exchange.ExchangeClient;
import bitxon.dropwizard.db.AccountDao;
import bitxon.dropwizard.mapper.AccountMapper;
import io.dropwizard.hibernate.UnitOfWork;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor(onConstructor_ = @Inject)
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
            .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
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

        var sender = dao.findById(transfer.senderId())
            .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));

        var recipient = dao.findById(transfer.recipientId())
            .orElseThrow(() -> new ResourceNotFoundException("Recipient not found"));

        var exchangeRateValue = exchangeClient.getExchangeRate(sender.getCurrency())
            .rates().getOrDefault(recipient.getCurrency(), 1.0);

        sender.setMoneyAmount(sender.getMoneyAmount() - transfer.moneyAmount());
        dao.save(sender);

        if (FAIL_TRANSFER.equals(dirtyTrick)) {
            throw new DirtyTrickException("Error during money transfer");
        }

        recipient.setMoneyAmount(recipient.getMoneyAmount() + (int)(transfer.moneyAmount() * exchangeRateValue));
        dao.save(recipient);
    }
}

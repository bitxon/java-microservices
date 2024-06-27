package bitxon.quarkus.resource;

import static bitxon.common.api.constant.Constants.DIRTY_TRICK_HEADER;
import static bitxon.common.api.constant.Constants.DirtyTrick.FAIL_TRANSFER;

import bitxon.common.api.model.Account;
import bitxon.common.api.model.MoneyTransfer;
import bitxon.common.exception.DirtyTrickException;
import bitxon.common.exception.ResourceNotFoundException;
import bitxon.quarkus.client.exchange.ExchangeClient;
import bitxon.quarkus.db.AccountDao;
import bitxon.quarkus.mapper.AccountMapper;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/accounts")
//@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AccountResource {

    @Inject
    private AccountDao dao;
    @Inject
    private AccountMapper mapper;
    @Inject
    @RestClient
    private ExchangeClient exchangeClient;

    @GET
    public List<Account> getAll() {
        return dao.findAll().stream()
            .map(mapper::mapToApi)
            .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public Account getById(@PathParam("id") Long id) {
        return dao.findByIdOptional(id)
            .map(mapper::mapToApi)
            .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
    }

    @POST
    @Transactional
    public Account create(@Valid Account account) {
        return Optional.of(account)
            .map(mapper::mapToDb)
            .map(dao::save)
            .map(mapper::mapToApi)
            .get();
    }

    @POST
    @Path("/transfers")
    @Transactional
    public void transfer(@Valid MoneyTransfer transfer,
                         @HeaderParam(DIRTY_TRICK_HEADER) String dirtyTrick) {
        var sender = dao.findByIdOptional(transfer.senderId())
            .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));

        var recipient = dao.findByIdOptional(transfer.recipientId())
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
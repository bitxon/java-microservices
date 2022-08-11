package bitxon.dropwizard.resource;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import bitxon.api.model.Account;
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
}

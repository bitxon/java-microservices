package bitxon.dropwizard.resource;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.List;

import bitxon.api.model.Account;


@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {

    @GET
    public List<Account> getAll() {
        return List.of(
            Account.builder().build()
        );
    }

    @GET
    @Path("/{id}")
    public Account getById(@PathParam("id") Long id) {
        return Account.builder().id(id).build();
    }

    @POST
    public Account create(@NotNull @Valid Account account) {
        return account;
    }
}

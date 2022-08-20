package bitxon.quarkus.resource;

import static bitxon.api.constant.Constants.DIRTY_TRICK_HEADER;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

import bitxon.api.model.Account;
import bitxon.api.model.MoneyTransfer;

@Path("/accounts")
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
    public Account create(Account account) {
        return Account.builder().build();
    }

    @POST
    @Path("/transfers")
    public void transfer(MoneyTransfer transfer,
                         @HeaderParam(DIRTY_TRICK_HEADER) String dirtyTrick) {

    }
}
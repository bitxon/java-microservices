package bitxon.dropwizard.mapper;

import bitxon.dropwizard.api.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AccountMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "moneyAmount", source = "moneyAmount")
    Account mapToApi(bitxon.dropwizard.db.model.Account src);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "moneyAmount", source = "moneyAmount")
    bitxon.dropwizard.db.model.Account  mapToDb(Account src);
}

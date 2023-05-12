package bitxon.micronaut.mapper;

import bitxon.common.api.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    componentModel = MappingConstants.ComponentModel.JSR330
)
public interface AccountMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "moneyAmount", source = "moneyAmount")
    Account mapToApi(bitxon.micronaut.db.model.Account src);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "moneyAmount", source = "moneyAmount")
    bitxon.micronaut.db.model.Account mapToDb(Account src);
}

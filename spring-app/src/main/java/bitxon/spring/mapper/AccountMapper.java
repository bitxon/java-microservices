package bitxon.spring.mapper;

import bitxon.spring.api.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    componentModel = MappingConstants.ComponentModel.SPRING
)
public interface AccountMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "moneyAmount", source = "moneyAmount")
    Account mapToApi(bitxon.spring.db.model.Account src);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "moneyAmount", source = "moneyAmount")
    bitxon.spring.db.model.Account  mapToDb(Account src);
}

package ro.asis.account.service.model.mappers

import org.springframework.stereotype.Component
import ro.asis.account.dto.Account
import ro.asis.account.service.model.entity.AccountEntity
import ro.asis.commons.utils.ModelMapper

@Component
class AccountMapper : ModelMapper<Account, AccountEntity> {
    override fun toApi(source: AccountEntity): Account {
        return Account(
            id = source.id,
            password = source.password,
            email = source.email,
            phoneNumber = source.phoneNumber,
            type = source.type
        )
    }

    override fun toEntity(source: Account): AccountEntity {
        return AccountEntity(
            id = source.id,
            password = source.password,
            email = source.email,
            phoneNumber = source.phoneNumber,
            type = source.type
        )
    }
}

package ro.asis.account.service.model.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import ro.asis.commons.enums.AccountType

@Document(collection = "accounts")
class AccountEntity(
    @Id
    var id: String = ObjectId.get().toHexString(),

    var username: String,
    var password: String,
    var email: String,
    var phoneNumber: String,
    var type: AccountType
)

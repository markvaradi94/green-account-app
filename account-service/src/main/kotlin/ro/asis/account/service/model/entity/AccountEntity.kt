package ro.asis.account.service.model.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import ro.asis.commons.enums.AccountType
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Document(collection = "accounts")
class AccountEntity(
    @Id
    var id: String? = ObjectId.get().toHexString(),

    @NotNull
    @NotBlank(message = "Please enter a valid username")
    var username: String,

    @NotNull
    @NotBlank(message = "Please enter a valid password")
    var password: String,

    @Email
    @NotNull
    @NotBlank(message = "Please enter a valid email address")
    var email: String,

    @NotNull
    @NotBlank(message = "Please enter a valid phone number")
    var phoneNumber: String,

    @NotNull
    @NotBlank
    var type: AccountType
)

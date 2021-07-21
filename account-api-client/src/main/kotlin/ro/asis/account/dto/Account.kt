package ro.asis.account.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.bson.types.ObjectId
import ro.asis.commons.enums.AccountType

data class Account(
    @JsonProperty("id")
    val id: String = ObjectId.get().toHexString(),

    @JsonProperty("password")
    val password: String,

    @JsonProperty("email")
    val email: String,

    @JsonProperty("phoneNumber")
    val phoneNumber: String,

    @JsonProperty("type")
    val type: AccountType
)

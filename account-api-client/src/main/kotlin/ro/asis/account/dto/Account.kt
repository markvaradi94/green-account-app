package ro.asis.account.dto

import com.fasterxml.jackson.annotation.JsonProperty
import ro.asis.commons.enums.AccountType

data class Account(
    @JsonProperty("id")
    val id: String?,

    @JsonProperty("username")
    val username: String,

    @JsonProperty("password")
    val password: String,

    @JsonProperty("email")
    val email: String,

    @JsonProperty("phoneNumber")
    val phoneNumber: String,

    @JsonProperty("type")
    val type: AccountType
)

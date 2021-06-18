package ro.asis.account.dto

import ro.asis.commons.enums.AccountType

data class Account(
    val id: String?,
    val username: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val type: AccountType
)

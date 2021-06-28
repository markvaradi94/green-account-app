package ro.asis.account.events

import com.fasterxml.jackson.annotation.JsonProperty
import ro.asis.account.dto.Account
import ro.asis.commons.enums.AccountType
import ro.asis.commons.enums.EventType
import ro.asis.commons.enums.EventType.MODIFY

data class AccountModifiedEvent(
    @JsonProperty("accountId")
    val accountId: String,

    @JsonProperty("accountType")
    val accountType: AccountType,

    @JsonProperty("eventType")
    val eventType: EventType = MODIFY,

    @JsonProperty("editedAccount")
    val editedAccount: Account
)

package ro.asis.account.events

import com.fasterxml.jackson.annotation.JsonProperty
import ro.asis.account.dto.Account
import ro.asis.commons.enums.AccountType
import ro.asis.commons.enums.EventType
import ro.asis.commons.enums.EventType.MODIFIED

data class AccountEditEvent(
    @JsonProperty("accountId")
    val accountId: String,

    @JsonProperty("accountType")
    val accountType: AccountType,

    @JsonProperty("eventType")
    val eventType: EventType = MODIFIED,

    @JsonProperty("editedAccount")
    val editedAccount: Account
)

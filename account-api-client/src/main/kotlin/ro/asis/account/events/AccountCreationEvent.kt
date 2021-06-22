package ro.asis.account.events

import com.fasterxml.jackson.annotation.JsonProperty
import ro.asis.commons.enums.AccountType
import ro.asis.commons.enums.EventType
import ro.asis.commons.enums.EventType.CREATED

data class AccountCreationEvent(
    @JsonProperty("accountId")
    val accountId: String,

    @JsonProperty("accountType")
    val accountType: AccountType,

    @JsonProperty("eventType")
    val eventType: EventType = CREATED
)

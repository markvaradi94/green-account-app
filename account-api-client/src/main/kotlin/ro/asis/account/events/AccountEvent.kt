package ro.asis.account.events

import ro.asis.account.dto.Account
import ro.asis.commons.enums.EventType

data class AccountEvent(
    private val account: Account,
    private val type: EventType
)

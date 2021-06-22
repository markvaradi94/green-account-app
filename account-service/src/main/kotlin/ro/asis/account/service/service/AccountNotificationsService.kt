package ro.asis.account.service.service

import org.slf4j.LoggerFactory
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import ro.asis.account.events.AccountCreationEvent
import ro.asis.account.events.AccountDeletionEvent
import ro.asis.account.events.AccountEditEvent
import ro.asis.account.service.model.entity.AccountEntity
import ro.asis.account.service.model.mappers.AccountMapper

@Service
class AccountNotificationsService(
    private val mapper: AccountMapper,
    private val rabbitTemplate: RabbitTemplate,
    private val accountExchange: TopicExchange
) {
    companion object {
        private val LOG = LoggerFactory.getLogger(AccountEntity::class.java)
    }

    fun notifyAccountCreated(account: AccountEntity) {
        val event = AccountCreationEvent(
            accountId = account.id!!,
            accountType = account.type
        )

        LOG.info("Sending event $event")
        rabbitTemplate.convertAndSend(accountExchange.name, "green.accounts.new", event)
    }

    fun notifyAccountDeleted(account: AccountEntity) {
        val event = AccountDeletionEvent(
            accountId = account.id!!,
            accountType = account.type
        )

        LOG.info("Sending event $event")
        rabbitTemplate.convertAndSend(accountExchange.name, "green.accounts.delete", event)
    }

    fun notifyAccountEdited(account: AccountEntity) {
        val event = AccountEditEvent(
            accountId = account.id!!,
            accountType = account.type,
            editedAccount = mapper.toApi(account)
        )

        LOG.info("Sending event $event")
        rabbitTemplate.convertAndSend(accountExchange.name, "green.accounts.edit", event)
    }
}

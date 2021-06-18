package ro.asis.account.service.service

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import ro.asis.account.service.model.mappers.AccountMapper

@Service
class AccountNotificationsService(
    private val mapper: AccountMapper,
    private val rabbitTemplate: RabbitTemplate
) {

}

package ro.asis.account.service.bootstrap

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import ro.asis.account.service.model.entity.AccountEntity
import ro.asis.account.service.service.AccountService
import ro.asis.commons.enums.AccountType.*

@Component
class DataLoader(private val service: AccountService) : CommandLineRunner {
    override fun run(vararg args: String?) {
//        service.addAccount(
//            AccountEntity(
//                username = "mazare123",
//                password = "portocala321",
//                email = "test@mail.com",
//                phoneNumber = "0644 8237 123",
//                type = ADMIN
//            )
//        )
//        service.addAccount(
//            AccountEntity(
//                username = "cadin7",
//                password = "volvo4lyf",
//                email = "test@vol.vo",
//                phoneNumber = "0213 235 123",
//                type = PROVIDER
//            )
//        )
//        service.addAccount(
//            AccountEntity(
//                username = "sinkoPelo",
//                password = "kocsisFiu",
//                email = "pel@inho.com",
//                phoneNumber = "0754 854 999",
//                type = CLIENT
//            )
//        )
//        service.addAccount(
//            AccountEntity(
//                username = "guest1234",
//                password = "6541asda",
//                email = "ren@utz.ro",
//                phoneNumber = "5231 456 888",
//                type = GUEST
//            )
//        )
    }

}

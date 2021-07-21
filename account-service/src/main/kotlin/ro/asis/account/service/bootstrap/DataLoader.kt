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
//                password = "portocala321",
//                email = "test@mail.com",
//                phoneNumber = "0742 823 123",
//                type = ADMIN
//            )
//        )
//        service.addAccount(
//            AccountEntity(
//                password = "volvo4lyf",
//                email = "test@vol.vo",
//                phoneNumber = "0755235000",
//                type = PROVIDER
//            )
//        )
//        service.addAccount(
//            AccountEntity(
//                password = "kocsisFiu",
//                email = "pel@inho.com",
//                phoneNumber = "0754854999",
//                type = CLIENT
//            )
//        )
//        service.addAccount(
//            AccountEntity(
//                password = "6541asda",
//                email = "ren@utz.ro",
//                phoneNumber = "0744 456 888",
//                type = CLIENT
//            )
//        )
    }
}

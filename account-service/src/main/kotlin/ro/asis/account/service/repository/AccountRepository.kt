package ro.asis.account.service.repository

import org.springframework.data.mongodb.repository.MongoRepository
import ro.asis.account.service.model.entity.AccountEntity

interface AccountRepository : MongoRepository<AccountEntity, String> {
    fun existsByEmail(email: String): Boolean
}

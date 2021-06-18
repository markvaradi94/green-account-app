package ro.asis.account.service.repository

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import ro.asis.account.service.model.entity.AccountEntity
import ro.asis.commons.enums.AccountType.valueOf
import ro.asis.commons.filters.AccountFilters
import java.util.Optional.ofNullable

@Repository
class AccountDao(private val mongo: MongoTemplate) {
    fun findAccounts(filters: AccountFilters): List<AccountEntity> {
        val query = Query()
        val criteria = buildCriteria(filters)

        if (criteria.isNotEmpty()) query.addCriteria(Criteria().andOperator(*criteria.toTypedArray()))

        return mongo.find(query, AccountEntity::class.java).toList()
    }

    private fun buildCriteria(filters: AccountFilters): List<Criteria> {
        val criteria = mutableListOf<Criteria>()

        ofNullable(filters.id)
            .ifPresent { criteria.add(Criteria.where("id").`is`(it)) }
        ofNullable(filters.username)
            .ifPresent { criteria.add(Criteria.where("username").regex(".*$it.*", "i")) }
        ofNullable(filters.email)
            .ifPresent { criteria.add(Criteria.where("email").regex(".*$it.*", "i")) }
        ofNullable(filters.phoneNumber)
            .ifPresent { criteria.add(Criteria.where("phoneNumber").regex(".*$it.*", "i")) }
        ofNullable(filters.type)
            .ifPresent { criteria.add(Criteria.where("type").`is`(valueOf(it.uppercase()).name)) }

        return criteria
    }
}

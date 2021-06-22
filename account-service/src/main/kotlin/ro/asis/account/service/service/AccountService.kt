package ro.asis.account.service.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.JsonPatch
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ro.asis.account.service.model.entity.AccountEntity
import ro.asis.account.service.repository.AccountDao
import ro.asis.account.service.repository.AccountRepository
import ro.asis.account.service.service.validator.AccountValidator
import ro.asis.commons.exceptions.ResourceNotFoundException
import ro.asis.commons.filters.AccountFilters
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotNull

@Service
class AccountService(
    private val dao: AccountDao,
    private val mapper: ObjectMapper,
    private val validator: AccountValidator,
    private val repository: AccountRepository,
    private val notificationsService: AccountNotificationsService
) {
    companion object {
        private val LOG = LoggerFactory.getLogger(AccountEntity::class.java)
    }

    fun findAllAccounts(filters: AccountFilters): List<AccountEntity> = dao.findAccounts(filters)

    fun findAccount(accountId: String): Optional<AccountEntity> = repository.findById(accountId)

    fun addAccount(@Valid @NotNull newAccount: AccountEntity): AccountEntity {
        validator.validateNewOrThrow(newAccount)
        val dbAccount = repository.save(newAccount)
        notificationsService.notifyAccountCreated(dbAccount)
        return dbAccount
    }

    fun deleteAccount(accountId: String): Optional<AccountEntity> {
        validator.validateExistsOrThrow(accountId)
        val accountToDelete = repository.findById(accountId)
        accountToDelete.ifPresent { deleteExistingAccount(it) }
        return accountToDelete
    }

    fun patchAccount(accountId: String, patch: JsonPatch): AccountEntity {
        validator.validateExistsOrThrow(accountId)

        val dbAccount = getOrThrow(accountId)
        val patchedAccountJson = patch.apply(mapper.valueToTree(dbAccount))
        val patchedAccount = mapper.treeToValue(patchedAccountJson, AccountEntity::class.java)

        validator.validateReplaceOrThrow(accountId, patchedAccount)

        copyAccount(patchedAccount, dbAccount)
        notificationsService.notifyAccountEdited(dbAccount)
        return repository.save(dbAccount)
    }

    private fun copyAccount(newAccount: AccountEntity, dbAccount: AccountEntity) {
        LOG.info("Copying account: $newAccount")
        dbAccount.email = newAccount.email
        dbAccount.phoneNumber = newAccount.phoneNumber
    }

    private fun deleteExistingAccount(account: AccountEntity) {
        LOG.info("Deleting account: $account")
        notificationsService.notifyAccountDeleted(account)
        repository.delete(account)
    }

    private fun getOrThrow(accountId: String): AccountEntity = repository
        .findById(accountId)
        .orElseThrow { ResourceNotFoundException("Could not find account with id $accountId") }
}

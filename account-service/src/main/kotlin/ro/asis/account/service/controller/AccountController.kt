package ro.asis.account.service.controller

import com.github.fge.jsonpatch.JsonPatch
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import ro.asis.account.dto.Account
import ro.asis.account.service.model.entity.AccountEntity
import ro.asis.account.service.model.mappers.AccountMapper
import ro.asis.account.service.service.AccountService
import ro.asis.commons.exceptions.ResourceNotFoundException
import ro.asis.commons.filters.AccountFilters
import javax.validation.Valid

@RestController
@RequestMapping("accounts")
class AccountController(
    private val mapper: AccountMapper,
    private val service: AccountService
) {
    companion object {
        private val LOG = LoggerFactory.getLogger(AccountEntity::class.java)
    }

    @GetMapping
    fun getAllAccounts(fi: AccountFilters): List<Account> = mapper.toApi(service.findAllAccounts(fi))

    @GetMapping("{accountId}")
    fun getAccount(@PathVariable accountId: String): Account = service.findAccount(accountId)
        .map { mapper.toApi(it) }
        .orElseThrow { ResourceNotFoundException("Could not find account with id $accountId") }

    @PostMapping
    fun addAccount(@Valid @RequestBody account: Account): Account =
        mapper.toApi(service.addAccount(mapper.toEntity(account)))

    @PatchMapping("{accountId}")
    fun patchAccount(@PathVariable accountId: String, @RequestBody accountPatch: JsonPatch): Account =
        mapper.toApi(service.patchAccount(accountId, accountPatch))

    @DeleteMapping("{accountId}")
    fun deleteAccount(@PathVariable accountId: String): Account = service.deleteAccount(accountId)
        .map { mapper.toApi(it) }
        .orElseGet { null }
}

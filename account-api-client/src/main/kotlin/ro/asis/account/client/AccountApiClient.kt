package ro.asis.account.client

import com.github.fge.jsonpatch.JsonPatch
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpEntity.EMPTY
import org.springframework.http.HttpMethod.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject
import org.springframework.web.util.UriComponentsBuilder
import ro.asis.account.dto.Account
import ro.asis.commons.filters.AccountFilters
import java.util.*
import java.util.Optional.ofNullable

@Component
class AccountApiClient(
    @Value("\${account-service-location:NOT_DEFINED}")
    private val baseUrl: String,
    private val restTemplate: RestTemplate = RestTemplate()
) {
    companion object {
        private val LOG = LoggerFactory.getLogger(Account::class.java)
    }

    fun getAllAccounts(filters: AccountFilters): List<Account> {
        val url = buildQueriedUrl(filters)
        return restTemplate.exchange(
            url,
            GET,
            EMPTY,
            object : ParameterizedTypeReference<List<Account>>() {}
        ).body ?: listOf()
    }

    fun getAccount(accountId: String): Optional<Account> {
        val url = UriComponentsBuilder.fromHttpUrl(baseUrl)
            .path("/accounts/$accountId")
            .toUriString()

        return ofNullable(restTemplate.getForObject(url, Account::class.java))
    }

    fun addAccount(account: Account): Account {
        val url = UriComponentsBuilder.fromHttpUrl(baseUrl)
            .path("/accounts")
            .toUriString()
        return restTemplate.postForObject(url, account, Account::class)
    }

    fun patchAccount(accountId: String, patch: JsonPatch): Optional<Account> {
        val url = UriComponentsBuilder.fromHttpUrl(baseUrl)
            .path("/accounts/$accountId")
            .toUriString()
        val patchedAccount = HttpEntity(patch)
        return ofNullable(
            restTemplate.exchange(
                url,
                PATCH,
                patchedAccount,
                Account::class.java
            ).body
        )
    }

    fun deleteAccount(accountId: String): Optional<Account> {
        val url = UriComponentsBuilder.fromHttpUrl(baseUrl)
            .path("/accounts/$accountId")
            .toUriString()
        return ofNullable(
            restTemplate.exchange(
                url,
                DELETE,
                EMPTY,
                Account::class.java
            ).body
        )
    }

    private fun buildQueriedUrl(filters: AccountFilters): String {
        val builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
            .path("/accounts/")

        ofNullable(filters.id)
            .ifPresent { builder.queryParam("id", it) }
        ofNullable(filters.email)
            .ifPresent { builder.queryParam("email", it) }
        ofNullable(filters.phoneNumber)
            .ifPresent { builder.queryParam("phoneNumber", it) }
        ofNullable(filters.type)
            .ifPresent { builder.queryParam("type", it) }

        return builder.toUriString()
    }

}

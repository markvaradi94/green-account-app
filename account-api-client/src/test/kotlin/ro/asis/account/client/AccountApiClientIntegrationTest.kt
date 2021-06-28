package ro.asis.account.client

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.web.client.HttpClientErrorException
import ro.asis.account.dto.Account
import ro.asis.commons.enums.AccountType.CLIENT
import ro.asis.commons.enums.AccountType.PROVIDER
import ro.asis.commons.filters.AccountFilters
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


class AccountApiClientIntegrationTest {
    private lateinit var accountClient: AccountApiClient
    private lateinit var filters: AccountFilters

    private var idToDelete: String? = null

    @BeforeEach
    fun setup() {
        accountClient = AccountApiClient("http://localhost:8501")
        filters = AccountFilters()
    }

    @AfterEach
    fun cleanup() {
        if (idToDelete != null) {
            accountClient.deleteAccount(idToDelete.orEmpty())
        }
    }

    @Test
    fun `WHEN requesting all accounts THEN response is correct`() {
        val allAccounts = accountClient.getAllAccounts(filters)

        assertNotNull(allAccounts)
        assertThat(allAccounts).isNotEmpty
    }

    @Test
    fun `WHEN requesting filtered accounts for clients THEN response is correct`() {
        filters = AccountFilters(type = "CLIENT")

        val filteredAccounts = accountClient.getAllAccounts(filters)

        assertNotNull(filteredAccounts)
        filteredAccounts.forEach { assertEquals(CLIENT, it.type) }
    }

    @Test
    fun `WHEN requesting filtered accounts by email THEN response is correct`() {
        val testEmail = "test5@mail.com"
        filters = AccountFilters(email = testEmail)

        val filteredAccounts = accountClient.getAllAccounts(filters)

        assertNotNull(filteredAccounts)
        filteredAccounts.forEach { assertEquals(testEmail, it.email) }
    }

    @Test
    fun `WHEN requesting filtered accounts by phone number THEN response is correct`() {
        val testPhoneNumber = "0744 459 789"
        filters = AccountFilters(phoneNumber = testPhoneNumber)

        val filteredAccounts = accountClient.getAllAccounts(filters)

        assertNotNull(filteredAccounts)
        filteredAccounts.forEach { assertEquals(testPhoneNumber, it.email) }
    }

    @Test
    fun `WHEN requesting an account THEN correct response is received`() {
        val account = Account(
            username = "testUser",
            password = "testPass",
            email = "testing@test51.test",
            phoneNumber = "0744 459 789",
            type = PROVIDER
        )

        val addedAccount = accountClient.addAccount(account)
        idToDelete = addedAccount.id

        val fetchedAccount = accountClient.getAccount(addedAccount.id)
            .orElseGet { null }

        assertThat(fetchedAccount).isNotNull
        assertThat(fetchedAccount.id).isEqualTo(addedAccount.id)
        assertThat(fetchedAccount.username).isEqualTo("testUser")
        assertThat(fetchedAccount.password).isEqualTo("testPass")
        assertThat(fetchedAccount.phoneNumber).isEqualTo("0744 459 789")
        assertThat(fetchedAccount.email).isEqualTo("testing@test51.test")
    }

    @Test
    fun `WHEN requesting an account that doesn't exist THEN exception is thrown`() {
        assertThrows<HttpClientErrorException.NotFound> { accountClient.getAccount("12345") }
    }

    @Test
    fun `WHEN adding a valid new account THEN it is successfully added`() {
        val account = Account(
            username = "testUser",
            password = "testPass",
            email = "testing@test51.test",
            phoneNumber = "0744459789",
            type = PROVIDER
        )

        val addedAccount = accountClient.addAccount(account)
        idToDelete = addedAccount.id

        assertThat(addedAccount).isNotNull
        assertThat(addedAccount.id).isNotNull
        assertThat(addedAccount.username).isEqualTo("testUser")
        assertThat(addedAccount.password).isEqualTo("testPass")
        assertThat(addedAccount.email).isEqualTo("testing@test51.test")
        assertThat(addedAccount.phoneNumber).isEqualTo("0744459789")
        assertThat(addedAccount.type).isEqualTo(PROVIDER)
    }

    @Test
    fun `WHEN adding a new account with empty username THEN exception is thrown`() {
        val account = Account(
            username = "",
            password = "testPass",
            email = "testing@test51.test",
            phoneNumber = "0744 459 789",
            type = PROVIDER
        )

        assertThrows<HttpClientErrorException.BadRequest> { accountClient.addAccount(account) }
    }

    @Test
    fun `WHEN adding a new account with empty email THEN exception is thrown`() {
        val account = Account(
            username = "testUser",
            password = "testPass",
            email = "",
            phoneNumber = "0744 459 789",
            type = PROVIDER
        )

        assertThrows<HttpClientErrorException.BadRequest> { accountClient.addAccount(account) }
    }

    @Test
    fun `WHEN adding a new account with empty password THEN exception is thrown`() {
        val account = Account(
            username = "testUser",
            password = "",
            email = "testing@test51.test",
            phoneNumber = "0744 459 789",
            type = PROVIDER
        )

        assertThrows<HttpClientErrorException.BadRequest> { accountClient.addAccount(account) }
    }

    @Test
    fun `WHEN adding a new account with empty phone number THEN exception is thrown`() {
        val account = Account(
            username = "testUser",
            password = "testPass",
            email = "testing@test51.test",
            phoneNumber = "",
            type = PROVIDER
        )

        assertThrows<HttpClientErrorException.BadRequest> { accountClient.addAccount(account) }
    }

    @Test
    fun `WHEN adding a new account with invalid username THEN exception is thrown`() {
        val account = Account(
            username = "murk",
            password = "testPass",
            email = "testing@test51.test",
            phoneNumber = "0744 459 789",
            type = PROVIDER
        )

        assertThrows<HttpClientErrorException.BadRequest> { accountClient.addAccount(account) }
    }

    @Test
    fun `WHEN adding a new account with invalid email THEN exception is thrown`() {
        val account = Account(
            username = "testUser",
            password = "testPass",
            email = "notGood@email",
            phoneNumber = "0744 459 789",
            type = PROVIDER
        )

        assertThrows<HttpClientErrorException.BadRequest> { accountClient.addAccount(account) }
    }

    @Test
    fun `WHEN adding a new account with invalid password THEN exception is thrown`() {
        val account = Account(
            username = "testUser",
            password = "nope",
            email = "testing@test51.test",
            phoneNumber = "0744 459 789",
            type = PROVIDER
        )

        assertThrows<HttpClientErrorException.BadRequest> { accountClient.addAccount(account) }
    }

    @Test
    fun `WHEN adding a new account with invalid phone number THEN exception is thrown`() {
        val account = Account(
            username = "testUser",
            password = "testPass",
            email = "testing@test51.test",
            phoneNumber = "123 45",
            type = PROVIDER
        )

        assertThrows<HttpClientErrorException.BadRequest> { accountClient.addAccount(account) }
    }

    @Test
    fun `WHEN adding a new account for the same username THEN exception is thrown`() {
        val account = Account(
            username = "testUser",
            password = "testPass",
            email = "testing@test51.test",
            phoneNumber = "0744 459 789",
            type = PROVIDER
        )

        val addedAccount = accountClient.addAccount(account)
        idToDelete = addedAccount.id

        val secondAccount = Account(
            username = "testUser",
            password = "otherPass",
            email = "some@new.email",
            phoneNumber = "0744 123 789",
            type = CLIENT
        )

        assertThrows<HttpClientErrorException.BadRequest> { accountClient.addAccount(secondAccount) }
    }

    @Test
    fun `WHEN adding a new account for the same email THEN exception is thrown`() {
        val account = Account(
            username = "testUser",
            password = "testPass",
            email = "testing@test51.test",
            phoneNumber = "0744 459 789",
            type = PROVIDER
        )

        val addedAccount = accountClient.addAccount(account)
        idToDelete = addedAccount.id

        val secondAccount = Account(
            username = "testUserNew",
            password = "otherPass",
            email = "testing@test51.test",
            phoneNumber = "0744 123 789",
            type = CLIENT
        )

        assertThrows<HttpClientErrorException.BadRequest> { accountClient.addAccount(secondAccount) }
    }

    @Test
    fun `WHEN deleting an account THEN it is correctly deleted`() {
        val account = Account(
            username = "testUser",
            password = "testPass",
            email = "testing@test51.test",
            phoneNumber = "0744 459 789",
            type = PROVIDER
        )

        val addedAccount = accountClient.addAccount(account)

        val deletedAccount = accountClient.deleteAccount(addedAccount.id)
            .orElseGet { null }

        assertThat(deletedAccount).isNotNull
        assertThat(deletedAccount.id).isNotNull
        assertThat(deletedAccount.id).isEqualTo(addedAccount.id)
        assertThat(deletedAccount.username).isEqualTo("testUser")
        assertThat(deletedAccount.password).isEqualTo("testPass")
        assertThat(deletedAccount.email).isEqualTo("testing@test51.test")
        assertThat(deletedAccount.phoneNumber).isEqualTo("0744 459 789")
        assertThat(deletedAccount.type).isEqualTo(PROVIDER)
    }

    @Test
    fun `WHEN deleting an account that doesn't exist THEN exception is thrown`() {
        assertThrows<HttpClientErrorException.BadRequest> { accountClient.deleteAccount("12345") }
    }


    //TODO figure out patch testing

//    @Test
//    fun `WHEN patching an existing account THEN it is correctly modified`() {
//        val account = Account(
//            id = null,
//            username = "testUser",
//            password = "testPass",
//            email = "testing@test51.test",
//            phoneNumber = "0744 459 789",
//            type = PROVIDER
//        )
//
//        val addedAccount = accountClient.addAccount(account)
//        idToDelete = addedAccount.id
//
//        val patchedAccount = Account(
//            id = addedAccount.id,
//            username = addedAccount.username,
//            password = account.password,
//            email = "different@email.com",
//            phoneNumber = "0744 459 789",
//            type = PROVIDER
//        )
//
//        val model = ObjectMapper().readTree("""{ "op":"replace","path":"/email","value":"different@email.com"}""")
//
////        val patchedAccountJson = patch.apply(mapper.valueToTree(dbAccount))
////        val patchedAccount = mapper.treeToValue(patchedAccountJson, AccountEntity::class.java)
//
//        val patch = JsonPatch.fromJson(model)
//
//        val finalAccount = accountClient.patchAccount(addedAccount.id, patch)
//            .orElseGet { null }
//
//        assertThat(finalAccount).isNotNull
//        assertThat(finalAccount.id).isNotNull
//        assertThat(finalAccount.id).isEqualTo(addedAccount.id)
//        assertThat(finalAccount.username).isEqualTo("testUser")
//        assertThat(finalAccount.password).isEqualTo("testPass")
//        assertThat(finalAccount.email).isEqualTo("testing@test51.test")
//        assertThat(finalAccount.phoneNumber).isEqualTo("0744 459 789")
//        assertThat(finalAccount.type).isEqualTo(PROVIDER)
//    }
}

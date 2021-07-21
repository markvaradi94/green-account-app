package ro.asis.account.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper
import ro.asis.account.dto.Account
import ro.asis.account.service.model.entity.AccountEntity
import ro.asis.account.service.repository.AccountRepository
import ro.asis.commons.enums.AccountType.CLIENT
import ro.asis.commons.enums.AccountType.PROVIDER

@SpringBootTest
@AutoConfigureMockMvc
class GreenAccountAppTest(
    @Autowired
    private val mockMvc: MockMvc,

    @Autowired
    private val repository: AccountRepository
) {

    @Test
    fun `WHEN requesting all accounts response is correct`() {
        repository.save(
            AccountEntity(
                id = "123",
                password = "testPass",
                email = "testing@test51.test",
                phoneNumber = "0744 459 789",
                type = PROVIDER
            )
        )

        repository.save(
            AccountEntity(
                id = "567",
                password = "testPass2",
                email = "testing@test52.test",
                phoneNumber = "0733 459 000",
                type = CLIENT
            )
        )

        val mvcResult: MvcResult = mockMvc.perform(get("/accounts"))
            .andDo { print() }
            .andExpect { status().isOk }
            .andReturn()

        val stringResponse = mvcResult.response.contentAsString
        val result: List<Account> = ObjectMapper().readValue(stringResponse, object : TypeReference<List<Account>>() {})

        assertThat(result).isNotEmpty
        assertThat(result).isNotNull
        assertThat(result.size).isEqualTo(2)
        assertThat(result[1])
            .extracting("email", "phoneNumber", "type")
            .containsExactly("testing@test52.test", "0733 459 000", CLIENT)
    }

    @Test
    fun `WHEN requesting an account response is correct`() {
        repository.save(
            AccountEntity(
                id = "123",
                password = "testPass",
                email = "testing@test51.test",
                phoneNumber = "0744 459 789",
                type = PROVIDER
            )
        )

        val mvcResult: MvcResult = mockMvc.perform(get("/accounts/123"))
            .andDo { print() }
            .andExpect { status().isOk }
            .andReturn()

        val stringResponse = mvcResult.response.contentAsString
        val result: Account = ObjectMapper().readValue(stringResponse, Account::class.java)

        assertThat(result).isNotNull
        assertThat(result)
            .extracting("email", "phoneNumber", "type")
            .containsExactly("testing@test51.test", "0744 459 789", PROVIDER)
    }

    @Test
    fun `WHEN adding a new account response is correct`() {
        val account = Account(
            id = "123",
            password = "testPass",
            email = "testing@test51.test",
            phoneNumber = "0744 459 789",
            type = PROVIDER
        )

        mockMvc.perform(
            post("/accounts")
                .contentType(APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(account))
                .accept(APPLICATION_JSON)
        )
            .andDo { print() }
            .andExpect { status().isCreated }
    }

    @Test
    fun `WHEN deleting an account response is correct`() {
        repository.save(
            AccountEntity(
                id = "123",
                password = "testPass",
                email = "testing@test51.test",
                phoneNumber = "0744 459 789",
                type = PROVIDER
            )
        )

        mockMvc.perform(
            delete("/accounts/123")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
        )
            .andExpect { status().is2xxSuccessful }
    }
}

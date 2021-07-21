package ro.asis.account.dto

import com.fasterxml.jackson.databind.ObjectMapper
import ro.asis.commons.enums.AccountType.CLIENT
import kotlin.test.Test
import kotlin.test.assertEquals

class AccountSerializationTest {

    @Test
    fun `WHEN deserializing Account THEN Json string is correct`() {
        val account = Account(
            id = "abc",
            password = "testPassword",
            email = "test@mail.ro",
            phoneNumber = "0711 123 123",
            type = CLIENT
        )

        val result = ObjectMapper().writeValueAsString(account)
        assertEquals(
            result, """
            {"id":"abc","password":"testPassword","email":"test@mail.ro","phoneNumber":"0711 123 123","type":"CLIENT"}
        """.trimIndent()
        )
    }

    @Test
    fun `WHEN converting Json string to Account THEN result is correct`() {
        val json = """
            {"id":"abc","password":"testPassword","email":"test@mail.ro","phoneNumber":"0711 123 123","type":"CLIENT"}
        """.trimIndent()

        val result = ObjectMapper().readValue(json, Account::class.java)
        assertEquals(
            result,
            Account(
                id = "abc",
                password = "testPassword",
                email = "test@mail.ro",
                phoneNumber = "0711 123 123",
                type = CLIENT
            )
        )
    }
}

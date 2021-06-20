package ro.asis.account.service.service.validator

import com.google.i18n.phonenumbers.PhoneNumberUtil
import org.springframework.stereotype.Component
import ro.asis.account.service.model.entity.AccountEntity
import ro.asis.account.service.repository.AccountRepository
import ro.asis.commons.exceptions.ValidationException
import java.util.*
import java.util.Optional.empty
import java.util.Optional.of
import java.util.regex.Pattern
import java.util.regex.Pattern.CASE_INSENSITIVE

@Component
class AccountValidator(
    private val repository: AccountRepository
) {

    fun validateReplaceOrThrow(accountId: String, newAccount: AccountEntity) =
        exists(accountId)
            .or { validate(account = newAccount) }
            .ifPresent { throw it }

    fun validateNewOrThrow(account: AccountEntity) =
        validate(account = account).ifPresent { throw it }

    fun validateExistsOrThrow(accountId: String) = exists(accountId).ifPresent { throw it }

    private fun validate(account: AccountEntity): Optional<ValidationException> {
        invalidPhoneNumber(account).ifPresent { throw it }
        emailAlreadyExistsOrInvalid(account).ifPresent { throw it }
        usernameAlreadyExistsOrInvalid(account).ifPresent { throw it }
        passwordIsValid(account).ifPresent { throw it }
        return empty()
    }

    private fun emailAlreadyExistsOrInvalid(account: AccountEntity): Optional<ValidationException> {
        val emailIsValid = validateEmailAddress(account.email)

        return if (repository.existsByEmail(account.email))
            of(ValidationException("Account with email ${account.email} already exists"))
        else return if (!emailIsValid) of(ValidationException("Email address is not valid"))
        else empty()
    }

    private fun validateEmailAddress(email: String): Boolean {
        val validEmailPattern =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", CASE_INSENSITIVE)
        val matcher = validEmailPattern.matcher(email)
        return matcher.find()
    }

    private fun passwordIsValid(account: AccountEntity): Optional<ValidationException> {
        val passwordIsValid = validatePassword(account.password)
        return if (!passwordIsValid) of(ValidationException("Password must be between 6 and 15 characters long"))
        else empty()
    }

    private fun validatePassword(password: String): Boolean = password.length in (6..15)

    private fun usernameAlreadyExistsOrInvalid(account: AccountEntity): Optional<ValidationException> {
        val usernameIsValid = validateUsername(account.username)
        return if (repository.existsByUsername(account.username))
            of(ValidationException("Account with username ${account.username} already exists"))
        else return if (!usernameIsValid) of(ValidationException("Username must be between 6 and 15 characters long"))
        else empty()
    }

    private fun validateUsername(username: String): Boolean = username.length in (6..15)

    private fun invalidPhoneNumber(account: AccountEntity): Optional<ValidationException> {
        val phoneUtil = PhoneNumberUtil.getInstance()
        val romanianNumberProto = phoneUtil.parse(account.phoneNumber, "RO")

        return if (!phoneUtil.isValidNumber(romanianNumberProto)) of(ValidationException("Phone number is not valid"))
        else empty()
    }

    private fun exists(accountId: String): Optional<ValidationException> {
        return if (repository.existsById(accountId)) empty()
        else of(ValidationException("Account with id $accountId doesn't exist."))
    }
}

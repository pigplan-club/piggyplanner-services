package club.pigplan.piggyplanner.user.domain.model

import java.util.*

data class UserId(val id: UUID)

data class Username(val username: String) {
    init {
        if (username.isEmpty())
            throw UsernameInvalidException()
    }
}

data class Password(val password: String) {
    init {
        if (password.isEmpty())
            throw PasswordInvalidException()
    }
}
package club.pigplan.piggyplanner.user.domain

import java.util.*

data class RegisteredUserCreated(val userId: UUID,
                                 val username: String,
                                 val encryptedPassword: String)
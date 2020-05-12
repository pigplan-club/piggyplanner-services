package club.pigplan.piggyplanner.user.domain

import club.pigplan.piggyplanner.user.domain.model.Password
import club.pigplan.piggyplanner.user.domain.model.UserId
import club.pigplan.piggyplanner.user.domain.model.Username

data class RegisteredUserCreated(val userId: UserId,
                                 val username: Username,
                                 val password: Password)
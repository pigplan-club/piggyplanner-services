package club.pigplan.piggyplanner.user.domain

import club.pigplan.piggyplanner.user.domain.model.EncryptedPassword
import club.pigplan.piggyplanner.user.domain.model.UserId
import club.pigplan.piggyplanner.user.domain.model.Username
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class CreateRegisteredUserCommand(@TargetAggregateIdentifier val userId: UserId,
                                       val username: Username,
                                       val encryptedPassword: EncryptedPassword)
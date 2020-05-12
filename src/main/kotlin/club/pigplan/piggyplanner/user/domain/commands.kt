package club.pigplan.piggyplanner.user.domain

import club.pigplan.piggyplanner.user.domain.model.UserId
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class CreateRegisteredUserCommand(@TargetAggregateIdentifier val userId: UserId,
                                       val username: String,
                                       val password: String)
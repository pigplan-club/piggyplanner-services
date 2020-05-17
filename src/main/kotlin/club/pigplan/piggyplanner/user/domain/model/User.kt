package club.pigplan.piggyplanner.user.domain.model

import club.pigplan.piggyplanner.common.domain.model.Entity
import club.pigplan.piggyplanner.user.domain.CreateRegisteredUserCommand
import club.pigplan.piggyplanner.user.domain.RegisteredUserCreated
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class User() : Entity() {

    @AggregateIdentifier
    private lateinit var userId: UserId
    private lateinit var username: Username
    private var password: Password? = null

    @CommandHandler
    constructor(command: CreateRegisteredUserCommand) : this() {
        AggregateLifecycle.apply(
                RegisteredUserCreated(command.userId,
                        Username(command.username),
                        Password(command.password))
        )
    }

    @EventSourcingHandler
    fun on(event: RegisteredUserCreated) {
        this.userId = event.userId
        this.username = event.username
        this.password = event.password
    }
}
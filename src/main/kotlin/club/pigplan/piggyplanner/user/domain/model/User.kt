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
    private var encryptedEncryptedPassword: EncryptedPassword? = null

    @CommandHandler
    constructor(command: CreateRegisteredUserCommand) : this() {
        AggregateLifecycle.apply(
                RegisteredUserCreated(command.userId.id,
                        command.username.username,
                        command.encryptedPassword.password)
        )
    }

    @EventSourcingHandler
    fun on(event: RegisteredUserCreated) {
        this.userId = UserId(event.userId)
        this.username = Username(event.username)
        this.encryptedEncryptedPassword = EncryptedPassword(event.encryptedPassword)
    }
}
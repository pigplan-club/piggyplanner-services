package club.pigplan.piggyplanner.user.application

import club.pigplan.piggyplanner.user.domain.CreateRegisteredUserCommand
import club.pigplan.piggyplanner.user.domain.model.UserId
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.CompletableFuture

@Service
class UserService(private val commandGateway: CommandGateway) {

    fun create(username: String, password: String): CompletableFuture<UserId> {
        //TODO: validate if username already exists
        //TODO: encode password
        return commandGateway.send(CreateRegisteredUserCommand(
                UserId(UUID.randomUUID()),
                username,
                password
        ))
    }
}
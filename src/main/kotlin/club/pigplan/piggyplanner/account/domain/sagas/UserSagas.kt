package club.pigplan.piggyplanner.account.domain.sagas

import club.pigplan.piggyplanner.account.domain.CreateDefaultAccountCommand
import club.pigplan.piggyplanner.account.domain.model.AccountId
import club.pigplan.piggyplanner.account.domain.model.UserId
import club.pigplan.piggyplanner.user.domain.RegisteredUserCreated
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.SagaLifecycle
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.spring.stereotype.Saga
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

@Saga
class UserSagas {

    @Transient
    @Autowired
    private lateinit var commandGateway: CommandGateway

    @StartSaga
    @SagaEventHandler(associationProperty = "username")
    fun handle(event: RegisteredUserCreated) {
        commandGateway.send<AccountId>(
                CreateDefaultAccountCommand(UserId(UUID.randomUUID())))

        SagaLifecycle.end()
    }
}
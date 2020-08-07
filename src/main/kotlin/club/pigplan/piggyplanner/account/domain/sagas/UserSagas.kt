package club.pigplan.piggyplanner.account.domain.sagas

import club.pigplan.piggyplanner.account.domain.CreateDefaultAccountCommand
import club.pigplan.piggyplanner.account.domain.model.AccountId
import club.pigplan.piggyplanner.account.domain.model.SaverId
import club.pigplan.piggyplanner.user.domain.RegisteredUserCreated
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.SagaLifecycle
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.spring.stereotype.Saga
import org.springframework.beans.factory.annotation.Autowired

@Saga
class UserSagas {

    @Transient
    @Autowired
    private lateinit var commandGateway: CommandGateway

    @StartSaga
    @SagaEventHandler(associationProperty = "username")
    fun handle(event: RegisteredUserCreated) {
        commandGateway.send<AccountId>(
                CreateDefaultAccountCommand(SaverId(event.userId)))

        SagaLifecycle.end()
    }
}
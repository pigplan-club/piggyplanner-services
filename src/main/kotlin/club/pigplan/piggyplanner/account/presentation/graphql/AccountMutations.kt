package club.pigplan.piggyplanner.account.presentation.graphql

import club.pigplan.piggyplanner.account.application.CreateRecord
import club.pigplan.piggyplanner.account.application.DeleteRecord
import club.pigplan.piggyplanner.account.application.ModifyRecord
import club.pigplan.piggyplanner.account.domain.CreateDefaultAccountCommand
import club.pigplan.piggyplanner.account.domain.model.AccountId
import club.pigplan.piggyplanner.account.domain.model.UserId
import club.pigplan.piggyplanner.account.application.RecordDTO
import com.expediagroup.graphql.annotations.GraphQLDescription
import com.expediagroup.graphql.spring.operations.Mutation
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.CompletableFuture

@Component
class AccountMutations(private val createRecord: CreateRecord,
                       private val modifyRecord: ModifyRecord,
                       private val deleteRecord: DeleteRecord,
                       private val commandGateway: CommandGateway) : Mutation {

    @GraphQLDescription("Create new Record")
    fun createRecord(recordDTO: RecordDTO) =
            createRecord.create(recordDTO)

    @GraphQLDescription("Modify an existing Record")
    fun modifyRecord(recordDTO: RecordDTO) =
            modifyRecord.modify(recordDTO)

    @GraphQLDescription("Delete an existing Record")
    fun deleteRecord(accountId: UUID, recordId: UUID) =
            deleteRecord.delete(accountId, recordId)


    //TODO: remove this
    @GraphQLDescription("Create default Account for testing")
    fun createDefaultAccount(): CompletableFuture<AccountId> {
        return commandGateway.send(CreateDefaultAccountCommand(UserId(UUID.randomUUID())))
    }
}


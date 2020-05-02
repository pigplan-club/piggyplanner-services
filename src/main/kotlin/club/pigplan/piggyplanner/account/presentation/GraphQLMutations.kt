package club.pigplan.piggyplanner.account.presentation

import club.pigplan.piggyplanner.account.domain.CreateDefaultAccount
import club.pigplan.piggyplanner.account.domain.CreateRecord
import club.pigplan.piggyplanner.account.domain.DeleteRecord
import club.pigplan.piggyplanner.account.domain.ModifyRecord
import club.pigplan.piggyplanner.account.domain.model.AccountId
import club.pigplan.piggyplanner.account.domain.model.RecordId
import club.pigplan.piggyplanner.account.domain.model.UserId
import com.expediagroup.graphql.annotations.GraphQLDescription
import com.expediagroup.graphql.spring.operations.Mutation
import com.expediagroup.graphql.spring.operations.Query
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.CompletableFuture

@Component
class GraphQLMutations(private val commandGateway: CommandGateway) : Mutation {

    @GraphQLDescription("Create new Record")
    fun createRecord(recordDTO: RecordDTO) : CompletableFuture<Boolean> {
        return commandGateway.send(CreateRecord(
                recordDTO.getAccountId(),
                recordDTO.getRecordId(),
                recordDTO.recordType,
                recordDTO.getCategoryId(),
                recordDTO.getCategoryItemId(),
                recordDTO.getDate(),
                recordDTO.amount,
                recordDTO.memo
        ))
    }

    @GraphQLDescription("Modify an existing Record")
    fun modifyRecord(recordDTO: RecordDTO) : CompletableFuture<Boolean> {
        return commandGateway.send(ModifyRecord(
                recordDTO.getAccountId(),
                recordDTO.getRecordId(),
                recordDTO.recordType,
                recordDTO.getCategoryId(),
                recordDTO.getCategoryItemId(),
                recordDTO.getDate(),
                recordDTO.amount,
                recordDTO.memo
        ))
    }

    @GraphQLDescription("Delete an existing Record")
    fun deleteRecord(accountId: UUID, recordId: UUID) : CompletableFuture<Boolean> {
        return commandGateway.send(DeleteRecord(
                AccountId(accountId),
                RecordId(recordId)
        ))
    }

    //TODO: remove it
    @GraphQLDescription("Create default Account to test")
    fun createDefaultAccount() : CompletableFuture<AccountId> {
        return commandGateway.send(CreateDefaultAccount(UserId(UUID.randomUUID())))
    }
}


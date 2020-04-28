package club.pigplan.piggyplanner.account.application

import club.pigplan.piggyplanner.account.domain.model.AccountId
import club.pigplan.piggyplanner.account.domain.model.RecordId
import club.pigplan.piggyplanner.account.domain.model.UserId
import club.pigplan.piggyplanner.account.domain.operations.CreateDefaultAccount
import club.pigplan.piggyplanner.account.domain.operations.CreateRecord
import club.pigplan.piggyplanner.account.domain.operations.DeleteRecord
import club.pigplan.piggyplanner.account.domain.operations.ModifyRecord
import club.pigplan.piggyplanner.account.presentation.RecordDTO
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.CompletableFuture

@Service
class AccountService(private val commandGateway: CommandGateway,
                     private val queryGateway: QueryGateway) {

    //TODO: Remove this when the saga pattern is implemented
    fun createDefaultAccount(saverId: UUID): CompletableFuture<AccountId> {
        return commandGateway.send(CreateDefaultAccount(UserId(saverId)))
    }

    fun createRecord(recordDTO: RecordDTO): CompletableFuture<Boolean> {
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

    fun modifyRecord(recordDTO: RecordDTO): CompletableFuture<Boolean> {
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

    fun deleteRecord(accountId: UUID, recordId: UUID): CompletableFuture<Boolean> {
        return commandGateway.send(DeleteRecord(
                AccountId(accountId),
                RecordId(recordId)
        ))
    }
}

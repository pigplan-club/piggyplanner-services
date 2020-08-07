package club.pigplan.piggyplanner.account.application

import club.pigplan.piggyplanner.account.domain.CreateRecordCommand
import club.pigplan.piggyplanner.account.domain.DeleteRecordCommand
import club.pigplan.piggyplanner.account.domain.ModifyRecordCommand
import club.pigplan.piggyplanner.account.domain.model.AccountId
import club.pigplan.piggyplanner.account.domain.model.RecordId
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.CompletableFuture

@Service
class RecordService(private val commandGateway: CommandGateway) {
    fun create(recordDTO: RecordDTO): CompletableFuture<Boolean> {
        return commandGateway.send(CreateRecordCommand(
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

    fun modify(recordDTO: RecordDTO): CompletableFuture<Boolean> {
        return commandGateway.send(ModifyRecordCommand(
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

    fun delete(accountId: UUID, recordId: UUID): CompletableFuture<Boolean> {
        return commandGateway.send(DeleteRecordCommand(
                AccountId(accountId),
                RecordId(recordId)
        ))
    }
}
package club.pigplan.piggyplanner.account.application

import club.pigplan.piggyplanner.account.domain.DeleteRecordCommand
import club.pigplan.piggyplanner.account.domain.model.AccountId
import club.pigplan.piggyplanner.account.domain.model.RecordId
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.CompletableFuture

@Service
class DeleteRecord(private val commandGateway: CommandGateway) {
    fun delete(accountId: UUID, recordId: UUID): CompletableFuture<Boolean> {
        return commandGateway.send(DeleteRecordCommand(
                AccountId(accountId),
                RecordId(recordId)
        ))
    }
}
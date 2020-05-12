package club.pigplan.piggyplanner.account.application

import club.pigplan.piggyplanner.account.domain.ModifyRecordCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
class ModifyRecord(private val commandGateway: CommandGateway) {
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
}
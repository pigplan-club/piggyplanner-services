package club.pigplan.piggyplanner.account.application

import club.pigplan.piggyplanner.account.domain.CreateRecordCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
class CreateRecord(private val commandGateway: CommandGateway) {

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
}
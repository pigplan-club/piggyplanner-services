package club.pigplan.piggyplanner.account.presentation.graphql

import club.pigplan.piggyplanner.account.application.RecordDTO
import club.pigplan.piggyplanner.account.application.RecordService
import com.expediagroup.graphql.annotations.GraphQLDescription
import com.expediagroup.graphql.spring.operations.Mutation
import org.springframework.stereotype.Component
import java.util.*

@Component
class AccountMutations(private val recordService: RecordService) : Mutation {

    @GraphQLDescription("Create new Record")
    fun createRecord(recordDTO: RecordDTO) =
            recordService.create(recordDTO)

    @GraphQLDescription("Modify an existing Record")
    fun modifyRecord(recordDTO: RecordDTO) =
            recordService.modify(recordDTO)

    @GraphQLDescription("Delete an existing Record")
    fun deleteRecord(accountId: UUID, recordId: UUID) =
            recordService.delete(accountId, recordId)
}


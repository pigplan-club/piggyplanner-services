package club.pigplan.piggyplanner.account.presentation.graphql

import club.pigplan.piggyplanner.account.application.CreateRecord
import club.pigplan.piggyplanner.account.application.DeleteRecord
import club.pigplan.piggyplanner.account.application.ModifyRecord
import club.pigplan.piggyplanner.account.application.RecordDTO
import com.expediagroup.graphql.annotations.GraphQLDescription
import com.expediagroup.graphql.spring.operations.Mutation
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import java.util.*

@Component
class AccountMutations(private val createRecord: CreateRecord,
                       private val modifyRecord: ModifyRecord,
                       private val deleteRecord: DeleteRecord,
                       private val ctx: ApplicationContext) : Mutation {

    @GraphQLDescription("Create new Record")
    fun createRecord(recordDTO: RecordDTO) =
            createRecord.create(recordDTO)

    @GraphQLDescription("Modify an existing Record")
    fun modifyRecord(recordDTO: RecordDTO) =
            modifyRecord.modify(recordDTO)

    @GraphQLDescription("Delete an existing Record")
    fun deleteRecord(accountId: UUID, recordId: UUID) =
            deleteRecord.delete(accountId, recordId)
}


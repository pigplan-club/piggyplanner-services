package club.pigplan.piggyplanner.account.presentation

import club.pigplan.piggyplanner.account.application.AccountService
import com.expediagroup.graphql.annotations.GraphQLDescription
import com.expediagroup.graphql.spring.operations.Mutation
import com.expediagroup.graphql.spring.operations.Query
import org.springframework.stereotype.Component
import java.util.*

@Component
class AccountMutations(private val accountService: AccountService) : Mutation {

    @GraphQLDescription("Create new Record")
    fun createRecord(recordDTO: RecordDTO) =
            accountService.createRecord(recordDTO)

    @GraphQLDescription("Modify an existing Record")
    fun modifyRecord(recordDTO: RecordDTO) =
            accountService.modifyRecord(recordDTO)

    @GraphQLDescription("Delete an existing Record")
    fun deleteRecord(accountId: UUID, recordId: UUID) =
            accountService.deleteRecord(accountId, recordId)

    //TODO: remove it
    @GraphQLDescription("Create default Account to test")
    fun createDefaultAccount() =
            accountService.createDefaultAccount(UUID.randomUUID())
}

@Component
class Queries : Query {

    //TODO: remove it
    @GraphQLDescription("For testing, remove it after a real query is created")
    fun generateRandomUUID() = UUID.randomUUID()
}

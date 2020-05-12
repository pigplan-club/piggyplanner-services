package club.pigplan.piggyplanner.account.presentation.graphql

import com.expediagroup.graphql.annotations.GraphQLDescription
import com.expediagroup.graphql.spring.operations.Query
import org.springframework.stereotype.Component
import java.util.*

@Component
class AccountQueries : Query {

    //TODO: remove it
    @GraphQLDescription("For testing, remove it after a real query is created")
    fun generateRandomUUID() = UUID.randomUUID()
}

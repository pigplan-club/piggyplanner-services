package club.pigplan.piggyplanner.user.presentation.graphql

import club.pigplan.piggyplanner.user.application.UserService
import com.expediagroup.graphql.annotations.GraphQLDescription
import com.expediagroup.graphql.spring.operations.Mutation
import org.springframework.stereotype.Component

@Component
class UserMutations(private val userService: UserService) : Mutation {

    @GraphQLDescription("Create new Registered User")
    fun createRegisteredUser(username: String, password: String) =
            userService.create(username, password)
}
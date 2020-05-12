package club.pigplan.piggyplanner.user.domain

import club.pigplan.piggyplanner.user.domain.model.*
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

class UserTest {
    private lateinit var fixture: FixtureConfiguration<User>
//    private val passwordEncoder = BCryptPasswordEncoder()

    @BeforeEach
    internal fun setUp() {
        fixture = AggregateTestFixture(User::class.java)
    }

    @Test
    internal fun `Create Registered User should be correct`() {
        val userId = UUID.randomUUID()
//        val passwordEncoded = passwordEncoder.encode("secret")
        val passwordEncoded = "secret"

        val createUser = CreateRegisteredUserCommand(
                userId = UserId(userId),
                username = "user1",
                password = passwordEncoded)

        fixture//.registerInjectableResource(passwordEncoder)
                .givenNoPriorActivity()
                .`when`(createUser)
                .expectSuccessfulHandlerExecution()
                .expectEvents(RegisteredUserCreated(
                        userId = UserId(userId),
                        username = Username("user1"),
                        password = Password(passwordEncoded)))
    }

    @Test
    internal fun `Create Registered User with empty username should throw UsernameInvalidException`() {
        val userId = UUID.randomUUID()
        val createUser = CreateRegisteredUserCommand(userId = UserId(userId), username = "", password = "secret")

        fixture//.registerInjectableResource(passwordEncoder)
                .givenNoPriorActivity()
                .`when`(createUser)
                .expectException(UsernameInvalidException::class.java)
    }

    @Test
    internal fun `Create Registered User with empty password should throw PasswordInvalidException`() {
        val userId = UUID.randomUUID()
        val createUser = CreateRegisteredUserCommand(userId = UserId(userId), username = "user1", password = "")

        fixture//.registerInjectableResource(passwordEncoder)
                .givenNoPriorActivity()
                .`when`(createUser)
                .expectException(PasswordInvalidException::class.java)
    }
}
package club.pigplan.piggyplanner.user.domain

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import club.pigplan.piggyplanner.user.domain.model.*
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
        val encryptedPassword = "secret"

        val createUser = CreateRegisteredUserCommand(
                userId = UserId(userId),
                username = Username("user1"),
                encryptedPassword = EncryptedPassword(encryptedPassword))

        fixture//.registerInjectableResource(passwordEncoder)
                .givenNoPriorActivity()
                .`when`(createUser)
                .expectSuccessfulHandlerExecution()
                .expectEvents(RegisteredUserCreated(
                        userId = userId,
                        username = "user1",
                        encryptedPassword = encryptedPassword))
    }

    @Test
    internal fun `Create Registered User with empty username should throw UsernameInvalidException`() {
        assertThrows<UsernameInvalidException>("Should throw UsernameInvalidException") {
            CreateRegisteredUserCommand(
                    userId = UserId(UUID.randomUUID()),
                    username = Username(""),
                    encryptedPassword = EncryptedPassword("secret"))
        }
    }

    @Test
    internal fun `Create Registered User with empty password should throw PasswordInvalidException`() {
        assertThrows<PasswordInvalidException>("Should throw PasswordInvalidException") {
            CreateRegisteredUserCommand(
                    userId = UserId(UUID.randomUUID()),
                    username = Username("user1"),
                    encryptedPassword = EncryptedPassword(""))
        }
    }
}
package club.pigplan.piggyplanner.account.domain

import club.pigplan.piggyplanner.account.domain.model.Account
import club.pigplan.piggyplanner.account.domain.model.UserId
import club.pigplan.piggyplanner.account.infrastructure.config.ConfigurationProperties
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.axonframework.test.matchers.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class AccountTest {
    private lateinit var fixture: FixtureConfiguration<Account>
    lateinit var configurationProperties: ConfigurationProperties

    @BeforeEach
    internal fun setUp() {
        configurationProperties = ConfigurationProperties("Personal", 2, 2, 2)
        fixture = AggregateTestFixture(Account::class.java)
    }

    @Test
    internal fun `Create a default Account should be correct`() {
        val userId = UUID.randomUUID()
        val createDefaultAccountCommand = CreateDefaultAccountCommand(UserId(userId))

        fixture.registerInjectableResource(configurationProperties)
                .givenNoPriorActivity()
                .`when`(createDefaultAccountCommand)
                .expectSuccessfulHandlerExecution()
                .expectEventsMatching(Matchers.payloadsMatching(Matchers.exactSequenceOf(
                        com.shazam.shazamcrest.matcher.Matchers.sameBeanAs(
                                DefaultAccountCreated(createDefaultAccountCommand.accountId,
                                        UserId(userId),
                                        configurationProperties.defaultAccountName,
                                        configurationProperties.recordsQuotaByMonth,
                                        configurationProperties.categoriesQuota,
                                        configurationProperties.categoryItemsQuota
                                )
                        ))))
                .expectResultMessagePayload(createDefaultAccountCommand.accountId)
    }
}

package club.pigplan.piggyplanner.account.domain.model

import club.pigplan.piggyplanner.account.domain.CreateDefaultAccountCommand
import club.pigplan.piggyplanner.account.domain.DefaultAccountCreated
import club.pigplan.piggyplanner.account.infrastructure.config.ConfigurationProperties
import club.pigplan.piggyplanner.common.domain.model.Entity
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.AggregateLifecycle.createNew
import org.axonframework.modelling.command.AggregateMember
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class Saver() : Entity() {

    @AggregateIdentifier
    private lateinit var saverId: SaverId

    @AggregateMember
    private val accounts = mutableSetOf<AccountId>()

    @CommandHandler
    constructor(command: CreateDefaultAccountCommand, configurationProperties: ConfigurationProperties) : this() {
        createNew(Account::class.java) {
            Account(command.accountId,
                    command.saverId,
                    command.categories,
                    configurationProperties.defaultAccountName,
                    configurationProperties.recordsQuotaByMonth,
                    configurationProperties.categoriesQuota,
                    configurationProperties.categoryItemsQuota)
        }

        AggregateLifecycle.apply(DefaultAccountCreated(command.saverId, command.accountId))
    }

    @EventSourcingHandler
    fun on(event: DefaultAccountCreated) {
        this.saverId = event.saverId
        this.accounts.add(event.accountId)
    }
}

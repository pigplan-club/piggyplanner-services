package club.pigplan.piggyplanner.account.domain.model

import club.pigplan.piggyplanner.account.domain.*
import club.pigplan.piggyplanner.common.domain.model.Entity
import club.pigplan.piggyplanner.common.domain.model.EntityState
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.AggregateMember
import org.axonframework.spring.stereotype.Aggregate
import java.time.LocalDate

@Aggregate(snapshotTriggerDefinition = "accountSnapshotTriggerDefinition")
class Account() : Entity() {

    @AggregateIdentifier
    private lateinit var accountId: AccountId
    private lateinit var saverId: SaverId
    private lateinit var name: String

    private var recordsQuotaByMonth: Int = -1
    private var categoriesQuota: Int = -1
    private var categoryItemsQuota: Int = -1

    @AggregateMember
    private val records = mutableSetOf<Record>()

    @AggregateMember
    private val categories = mutableSetOf<Category>()

    constructor(accountId: AccountId, saverId: SaverId, categories: Set<Category>, accountName: String,
                recordsQuotaByMonth: Int, categoriesQuota: Int, categoryItemsQuota: Int) : this() {

        AggregateLifecycle.apply(NewAccountCreated(
                accountId, saverId, accountName, recordsQuotaByMonth, categoriesQuota, categoryItemsQuota)
        )

        categories.forEach { AggregateLifecycle.apply(CategoryCreated(accountId, it)) }
    }

    @CommandHandler
    fun handle(command: CreateCategoryCommand): Boolean {
        if (categories.filter { it.state == EntityState.ENABLED }.size >= this.categoriesQuota)
            throw CategoriesQuotaExceededException()

        val newCategory = Category(command.categoryId, command.name)
        if (categories.contains(newCategory))
            throw CategoryAlreadyAddedException()

        AggregateLifecycle.apply(CategoryCreated(command.accountId, newCategory))
        return true
    }

    @CommandHandler
    fun handle(command: CreateCategoryItemCommand): Boolean {
        val category = categories.find { category -> category.categoryId == command.categoryId }
                ?: throw CategoryNotFoundException(command.categoryId.id)

        if (category.wasExceededQuota(this.categoryItemsQuota))
            throw CategoryItemsQuotaExceededException()

        val newCategoryItem = CategoryItem(command.categoryItemId, command.name)
        if (category.containsCategoryItem(newCategoryItem))
            throw CategoryItemAlreadyAddedException()

        AggregateLifecycle.apply(CategoryItemCreated(command.accountId,
                command.categoryId, newCategoryItem))

        return true
    }

    @CommandHandler
    fun handle(command: CreateRecordCommand): Boolean {
        if (numberRecordsForSelectedMonth(command.date) >= recordsQuotaByMonth)
            throw RecordsQuotaExceededException()

        if (records.find { record -> record.recordId == command.recordId } != null)
            throw RecordAlreadyAddedException()

        val category = categories.find { category -> category.categoryId == command.categoryId }
                ?: throw CategoryNotFoundException(command.categoryId.id)
        val categoryItem = category.getCategoryItem(command.categoryItemId)
                ?: throw CategoryItemNotFoundException(command.categoryItemId.id)

        AggregateLifecycle.apply(RecordCreated(command.accountId,
                Record(
                        recordId = command.recordId,
                        type = command.recordType,
                        categoryId = category.categoryId,
                        categoryItemId = categoryItem.categoryItemId,
                        date = command.date,
                        amount = RecordAmount(command.amount),
                        memo = command.memo)))
        return true
    }

    @CommandHandler
    fun handle(command: ModifyRecordCommand): Boolean {
        if (records.find { record -> record.recordId == command.recordId } == null)
            throw RecordNotFoundException(command.recordId.id)

        val category = categories.find { category -> category.categoryId == command.categoryId }
                ?: throw CategoryNotFoundException(command.categoryId.id)
        val categoryItem = category.getCategoryItem(command.categoryItemId)
                ?: throw CategoryItemNotFoundException(command.categoryItemId.id)

        AggregateLifecycle.apply(RecordModified(command.accountId,
                Record(
                        recordId = command.recordId,
                        type = command.recordType,
                        categoryId = category.categoryId,
                        categoryItemId = categoryItem.categoryItemId,
                        date = command.date,
                        amount = RecordAmount(command.amount),
                        memo = command.memo)))
        return true
    }

    @CommandHandler
    fun handle(command: DeleteRecordCommand): Boolean {
        if (records.find { record -> record.recordId == command.recordId } == null)
            throw RecordNotFoundException(command.recordId.id)

        AggregateLifecycle.apply(RecordDeleted(command.accountId,
                command.recordId))

        return true
    }

    @EventSourcingHandler
    fun on(event: NewAccountCreated) {
        this.accountId = event.accountId
        this.saverId = event.saverId
        this.name = event.accountName
        this.recordsQuotaByMonth = event.recordsQuotaByMonth
        this.categoriesQuota = event.categoriesQuota
        this.categoryItemsQuota = event.categoryItemsQuota
    }

    @EventSourcingHandler
    fun on(event: CategoryCreated) {
        this.categories.add(event.category)
    }

    @EventSourcingHandler
    fun on(event: RecordCreated) {
        this.records.add(event.record)
    }

    @EventSourcingHandler
    fun on(event: RecordDeleted) {
        this.records.remove(
                Record(event.recordId)
        )
    }

    private fun numberRecordsForSelectedMonth(date: LocalDate): Int {
        val firstDayOfTheMonth = date.minusDays(date.dayOfMonth - 1.toLong())
        val lastDayOfTheMonth = date.minusDays(date.dayOfMonth.toLong()).plusMonths(1)

        return records
                .filter { it.date >= firstDayOfTheMonth && it.date <= lastDayOfTheMonth }
                .size
    }
}

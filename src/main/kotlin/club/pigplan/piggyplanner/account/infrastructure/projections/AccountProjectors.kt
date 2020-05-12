package club.pigplan.piggyplanner.account.infrastructure.projections

import club.pigplan.piggyplanner.account.domain.*
import club.pigplan.piggyplanner.account.domain.model.CategoryItem
import club.pigplan.piggyplanner.account.infrastructure.repository.AccountStore
import club.pigplan.piggyplanner.account.infrastructure.repository.CategoryItemStore
import club.pigplan.piggyplanner.account.infrastructure.repository.CategoryStore
import club.pigplan.piggyplanner.account.infrastructure.repository.RecordStore
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
class AccountProjector(private val accountStore: AccountStore,
                       private val categoryStore: CategoryStore,
                       private val categoryItemStore: CategoryItemStore,
                       private val recordStore: RecordStore) {

    @EventHandler
    fun on(event: DefaultAccountCreated) {
        val accountProjection = AccountProjection(
                event.accountId.id,
                event.accountName,
                event.userId.id,
                listOf()
        )
        accountStore.save(accountProjection)
    }

    @EventHandler
    fun on(event: CategoryCreated) {
        val categoryProjection = CategoryProjection(
                event.category.categoryId.id,
                event.accountId.id,
                event.category.name,
                toCategoryItemProjections(event.category.categoryItems)
        )
        categoryStore.save(categoryProjection)
    }

    @EventHandler
    fun on(event: CategoryItemCreated) {
        val categoryItemProjection = CategoryItemProjection(
                event.categoryItem.categoryItemId.id,
                event.categoryItem.name
        )
        categoryItemStore.save(categoryItemProjection)
    }

    @EventHandler
    fun on(event: RecordCreated) {
        val recordProjection = RecordProjection(
                event.record.recordId.id,
                event.accountId.id,
                event.record.type,
                event.record.categoryId.id,
                event.record.categoryItemId.id,
                event.record.date,
                event.record.amount.value,
                event.record.memo
        )
        recordStore.save(recordProjection)
    }

    @EventHandler
    fun on(event: RecordModified) {
        val recordProjection = RecordProjection(
                event.record.recordId.id,
                event.accountId.id,
                event.record.type,
                event.record.categoryId.id,
                event.record.categoryItemId.id,
                event.record.date,
                event.record.amount.value,
                event.record.memo
        )
        recordStore.save(recordProjection)
    }

    @EventHandler
    fun on(event: RecordDeleted) {
        recordStore.deleteById(event.recordId.id)
    }

    private fun toCategoryItem(categoryItem: CategoryItem): CategoryItemProjection =
            CategoryItemProjection(
                    categoryItem.categoryItemId.id,
                    categoryItem.name
            )

    private fun toCategoryItemProjections(categoryItems: MutableSet<CategoryItem>): Set<CategoryItemProjection> =
            categoryItems.map {
                toCategoryItem(it)
            }.toSet()
}
package club.pigplan.piggyplanner.account.infrastructure.projections

import club.pigplan.piggyplanner.account.domain.*
import club.pigplan.piggyplanner.account.domain.model.CategoryItem
import club.pigplan.piggyplanner.account.infrastructure.repository.AccountRepo
import club.pigplan.piggyplanner.account.infrastructure.repository.CategoryItemRepo
import club.pigplan.piggyplanner.account.infrastructure.repository.CategoryRepo
import club.pigplan.piggyplanner.account.infrastructure.repository.RecordRepo
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
class AccountProjector(private val accountRepo: AccountRepo,
                       private val categoryRepo: CategoryRepo,
                       private val categoryItemRepo: CategoryItemRepo,
                       private val recordRepo: RecordRepo) {

    @EventHandler
    fun on(event: NewAccountCreated) {
        val accountProjection = AccountProjection(
                event.accountId.id,
                event.accountName,
                event.saverId.id,
                listOf()
        )
        accountRepo.save(accountProjection)
    }

    @EventHandler
    fun on(event: CategoryCreated) {
        val categoryProjection = CategoryProjection(
                event.category.categoryId.id,
                event.accountId.id,
                event.category.name,
                toCategoryItemProjections(event.category.categoryItems)
        )
        categoryRepo.save(categoryProjection)
    }

    @EventHandler
    fun on(event: CategoryItemCreated) {
        val categoryItemProjection = CategoryItemProjection(
                event.categoryItem.categoryItemId.id,
                event.categoryItem.name
        )
        categoryItemRepo.save(categoryItemProjection)
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
        recordRepo.save(recordProjection)
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
        recordRepo.save(recordProjection)
    }

    @EventHandler
    fun on(event: RecordDeleted) {
        recordRepo.deleteById(event.recordId.id)
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
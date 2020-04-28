package club.pigplan.piggyplanner.account.domain.operations

import club.pigplan.piggyplanner.account.domain.services.getDefaultCategories
import club.pigplan.piggyplanner.account.domain.model.*
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

data class CreateDefaultAccount(@TargetAggregateIdentifier val userId: UserId) {
    val accountId: AccountId = AccountId(UUID.randomUUID())
    val categories: List<Category> = getDefaultCategories()
}

data class CreateCategory(@TargetAggregateIdentifier val accountId: AccountId,
                          val categoryId: CategoryId,
                          val name: String)

data class CreateCategoryItem(@TargetAggregateIdentifier val accountId: AccountId,
                              val categoryId: CategoryId,
                              val categoryItemId: CategoryItemId,
                              val name: String)

data class CreateRecord(@TargetAggregateIdentifier val accountId: AccountId,
                        val recordId: RecordId,
                        val recordType: RecordType,
                        val categoryId: CategoryId,
                        val categoryItemId: CategoryItemId,
                        val date: LocalDate,
                        val amount: BigDecimal,
                        val memo: String? = "")

data class ModifyRecord(@TargetAggregateIdentifier val accountId: AccountId,
                        val recordId: RecordId,
                        val recordType: RecordType,
                        val categoryId: CategoryId,
                        val categoryItemId: CategoryItemId,
                        val date: LocalDate,
                        val amount: BigDecimal,
                        val memo: String? = "")

data class DeleteRecord(@TargetAggregateIdentifier val accountId: AccountId,
                        val recordId: RecordId)
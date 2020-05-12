package club.pigplan.piggyplanner.account.domain

import club.pigplan.piggyplanner.account.domain.model.*
import club.pigplan.piggyplanner.account.domain.services.getDefaultCategories
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

data class CreateDefaultAccountCommand(@TargetAggregateIdentifier val userId: UserId) {
    val accountId: AccountId = AccountId(UUID.randomUUID())
    val categories: List<Category> = getDefaultCategories()
}

data class CreateCategoryCommand(@TargetAggregateIdentifier val accountId: AccountId,
                                 val categoryId: CategoryId,
                                 val name: String)

data class CreateCategoryItemCommand(@TargetAggregateIdentifier val accountId: AccountId,
                                     val categoryId: CategoryId,
                                     val categoryItemId: CategoryItemId,
                                     val name: String)

data class CreateRecordCommand(@TargetAggregateIdentifier val accountId: AccountId,
                               val recordId: RecordId,
                               val recordType: RecordType,
                               val categoryId: CategoryId,
                               val categoryItemId: CategoryItemId,
                               val date: LocalDate,
                               val amount: BigDecimal,
                               val memo: String? = "")

data class ModifyRecordCommand(@TargetAggregateIdentifier val accountId: AccountId,
                               val recordId: RecordId,
                               val recordType: RecordType,
                               val categoryId: CategoryId,
                               val categoryItemId: CategoryItemId,
                               val date: LocalDate,
                               val amount: BigDecimal,
                               val memo: String? = "")

data class DeleteRecordCommand(@TargetAggregateIdentifier val accountId: AccountId,
                               val recordId: RecordId)
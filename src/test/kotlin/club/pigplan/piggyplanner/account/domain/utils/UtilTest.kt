package club.pigplan.piggyplanner.account.domain.utils

import club.pigplan.piggyplanner.account.domain.*
import club.pigplan.piggyplanner.account.infrastructure.config.ConfigurationProperties
import club.pigplan.piggyplanner.account.domain.model.*
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

class UtilTest {

    companion object {
        private val accountConfigProperties = ConfigurationProperties("Personal", 5, 5, 5)
        private val userId = UUID.randomUUID()
        val accountId: UUID = UUID.randomUUID()
        val category = Category(CategoryId(UUID.randomUUID()), "Utility")
        val category2 = Category(CategoryId(UUID.randomUUID()), "Clothes")
        val categoryItem = CategoryItem(CategoryItemId(UUID.randomUUID()), "Energy")

        fun generateDefaultAccountCreatedEvent(
                newRecordsQuotaByMonth: Int? = accountConfigProperties.recordsQuotaByMonth,
                categoriesQuota: Int? = accountConfigProperties.categoriesQuota,
                categoryItemsQuota: Int? = accountConfigProperties.categoryItemsQuota
        ): NewAccountCreated {
            return NewAccountCreated(AccountId(accountId), SaverId(userId), accountConfigProperties.defaultAccountName,
                    newRecordsQuotaByMonth!!,
                    categoriesQuota!!,
                    categoryItemsQuota!!)
        }

        fun generateCreateRecordCommand(record: Record): CreateRecordCommand {
            return CreateRecordCommand(
                    accountId = AccountId(accountId),
                    recordId = record.recordId,
                    recordType = record.type,
                    categoryId = category.categoryId,
                    categoryItemId = categoryItem.categoryItemId,
                    date = record.date,
                    amount = record.amount.value,
                    memo = record.memo)
        }

        fun generateCategoryItemCreatedEvent() =
                CategoryItemCreated(AccountId(accountId), category.categoryId, categoryItem)

        fun generateCategoryCreatedEvent(categorySelected: Category? = category) =
                CategoryCreated(AccountId(accountId), categorySelected!!)

        fun createRecordForTest(withMemo: Boolean, amount: BigDecimal? = BigDecimal.ONE, date: LocalDate? = LocalDate.now()): Record {
            val recordId = UUID.randomUUID()
            val recordType = RecordType.EXPENSE

            if (withMemo)
                return Record(recordId = RecordId(recordId),
                        type = recordType,
                        categoryId = category.categoryId,
                        categoryItemId = categoryItem.categoryItemId,
                        date = date!!,
                        amount = RecordAmount(amount!!),
                        memo = "test memo")

            return Record(recordId = RecordId(recordId),
                    type = recordType,
                    categoryId = category.categoryId,
                    categoryItemId = categoryItem.categoryItemId,
                    date = date!!,
                    amount = RecordAmount(amount!!))
        }
    }
}
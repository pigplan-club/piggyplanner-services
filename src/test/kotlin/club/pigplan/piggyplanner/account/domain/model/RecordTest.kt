package club.pigplan.piggyplanner.account.domain.model

import club.pigplan.piggyplanner.account.domain.model.utils.UtilTest
import club.pigplan.piggyplanner.account.domain.operations.*
import org.axonframework.modelling.command.AggregateNotFoundException
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.Assert.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

private const val NEW_MEMO = "New memo"

class RecordTest {
    private lateinit var fixture: FixtureConfiguration<Account>

    @BeforeEach
    internal fun setUp() {
        fixture = AggregateTestFixture(Account::class.java)
    }

    @Test
    internal fun `Create a Record should be correct`() {
        val record = UtilTest.createRecordForTest(true)
        val createRecordCommand = UtilTest.generateCreateRecordCommand(record)

        fixture.given(UtilTest.generateDefaultAccountCreatedEvent())
                .andGiven(UtilTest.generateCategoryCreatedEvent())
                .andGiven(UtilTest.generateCategoryItemCreatedEvent())
                .`when`(createRecordCommand)
                .expectSuccessfulHandlerExecution()
                .expectEvents(RecordCreated(AccountId(UtilTest.accountId), record))
                .expectResultMessagePayload(true)
    }

    @Test
    internal fun `Create a Record without memo should be correct`() {
        val record = UtilTest.createRecordForTest(false)
        val createRecordCommand = UtilTest.generateCreateRecordCommand(record)

        fixture.given(UtilTest.generateDefaultAccountCreatedEvent())
                .andGiven(UtilTest.generateCategoryCreatedEvent(UtilTest.category2))
                .andGiven(UtilTest.generateCategoryCreatedEvent())
                .andGiven(UtilTest.generateCategoryItemCreatedEvent())
                .`when`(createRecordCommand)
                .expectSuccessfulHandlerExecution()
                .expectEvents(RecordCreated(AccountId(UtilTest.accountId), record))
                .expectResultMessagePayload(true)
    }

    @Test
    internal fun `Modify an existing Record should be correct`() {
        val record = UtilTest.createRecordForTest(false)
        val recordModified = Record(
                record.recordId,
                type = RecordType.INCOME,
                date = record.date,
                categoryId = record.categoryId,
                categoryItemId = record.categoryItemId,
                amount = RecordAmount(BigDecimal.TEN),
                memo = NEW_MEMO)

        val modifyRecordCommand = ModifyRecord(
                accountId = AccountId(UtilTest.accountId),
                recordId = recordModified.recordId,
                recordType = recordModified.type,
                categoryId = recordModified.categoryId,
                categoryItemId = recordModified.categoryItemId,
                date = recordModified.date,
                amount = recordModified.amount.value,
                memo = recordModified.memo)

        fixture.given(UtilTest.generateDefaultAccountCreatedEvent())
                .andGiven(UtilTest.generateCategoryCreatedEvent(UtilTest.category2))
                .andGiven(UtilTest.generateCategoryCreatedEvent())
                .andGiven(UtilTest.generateCategoryItemCreatedEvent())
                .andGiven(RecordCreated(AccountId(UtilTest.accountId), record))
                .`when`(modifyRecordCommand)
                .expectSuccessfulHandlerExecution()
                .expectEvents(RecordModified(AccountId(UtilTest.accountId), recordModified))
                .expectResultMessagePayload(true)
    }

    @Test
    internal fun `Delete an existing Record should be correct`() {
        val record = UtilTest.createRecordForTest(false)
        val deleteRecordCommand = DeleteRecord(
                accountId = AccountId(UtilTest.accountId),
                recordId = record.recordId)

        fixture.given(UtilTest.generateDefaultAccountCreatedEvent())
                .andGiven(UtilTest.generateCategoryCreatedEvent())
                .andGiven(UtilTest.generateCategoryItemCreatedEvent())
                .andGiven(RecordCreated(AccountId(UtilTest.accountId), record))
                .`when`(deleteRecordCommand)
                .expectSuccessfulHandlerExecution()
                .expectEvents(RecordDeleted(AccountId(UtilTest.accountId), record.recordId))
                .expectResultMessagePayload(true)
    }

    @Test
    internal fun `Create a Record with an invalid amount should throw AmountInvalidException`() {
        assertThrows<AmountInvalidException>("Should throw AmountInvalidException") {
            UtilTest.createRecordForTest(false, BigDecimal.valueOf(0))
        }

        assertThrows<AmountInvalidException>("Should throw AmountInvalidException") {
            UtilTest.createRecordForTest(false, BigDecimal.valueOf(-5))
        }
    }

    @Test
    internal fun `Create a duplicated record should throw RecordAlreadyAddedException`() {
        val record = UtilTest.createRecordForTest(false)

        val createNewRecordCommand = CreateRecord(
                accountId = AccountId(UtilTest.accountId),
                recordId = record.recordId,
                recordType = RecordType.EXPENSE,
                categoryId = UtilTest.category.categoryId,
                categoryItemId = UtilTest.categoryItem.categoryItemId,
                date = LocalDate.now(),
                amount = BigDecimal.ONE,
                memo = "This is another note")

        fixture.given(UtilTest.generateDefaultAccountCreatedEvent())
                .andGiven(UtilTest.generateCategoryCreatedEvent())
                .andGiven(UtilTest.generateCategoryItemCreatedEvent())
                .andGiven(RecordCreated(AccountId(UtilTest.accountId), record))
                .`when`(createNewRecordCommand)
                .expectExceptionMessage("Record duplicated")
                .expectException(RecordAlreadyAddedException::class.java)
    }

    @Test
    internal fun `Create a Record with non existing Account should throw AggregateNotFoundException`() {
        val record = UtilTest.createRecordForTest(true)
        val createRecordCommand = UtilTest.generateCreateRecordCommand(record)

        fixture.givenNoPriorActivity()
                .`when`(createRecordCommand)
                .expectException(AggregateNotFoundException::class.java)
    }

    @Test
    internal fun `Modify a non existing Record should throw RecordNotFoundException`() {
        val record = UtilTest.createRecordForTest(false)
        val recordModified = Record(
                record.recordId,
                type = RecordType.INCOME,
                date = record.date,
                categoryId = record.categoryId,
                categoryItemId = record.categoryItemId,
                amount = RecordAmount(BigDecimal.TEN),
                memo = NEW_MEMO)


        val newRecordId = UUID.randomUUID()
        val modifyRecordCommand = ModifyRecord(
                accountId = AccountId(UtilTest.accountId),
                recordId = RecordId(newRecordId),
                recordType = recordModified.type,
                categoryId = recordModified.categoryId,
                categoryItemId = recordModified.categoryItemId,
                date = recordModified.date,
                amount = recordModified.amount.value,
                memo = recordModified.memo)

        fixture.given(UtilTest.generateDefaultAccountCreatedEvent())
                .andGiven(UtilTest.generateCategoryCreatedEvent())
                .andGiven(UtilTest.generateCategoryItemCreatedEvent())
                .andGiven(RecordCreated(AccountId(UtilTest.accountId), record))
                .`when`(modifyRecordCommand)
                .expectExceptionMessage("Record with id $newRecordId not found")
                .expectException(RecordNotFoundException::class.java)
    }

    @Test
    internal fun `Modify a Record with non existing Account should throw AssertionError`() {
        val record = UtilTest.createRecordForTest(false)
        val recordModified = Record(
                record.recordId,
                type = RecordType.INCOME,
                date = record.date,
                categoryId = record.categoryId,
                categoryItemId = record.categoryItemId,
                amount = RecordAmount(BigDecimal.TEN),
                memo = NEW_MEMO)

        val modifyRecordCommand = ModifyRecord(
                accountId = AccountId(UUID.randomUUID()),
                recordId = recordModified.recordId,
                recordType = recordModified.type,
                categoryId = recordModified.categoryId,
                categoryItemId = recordModified.categoryItemId,
                date = recordModified.date,
                amount = recordModified.amount.value,
                memo = recordModified.memo)

        try {
            fixture.given(UtilTest.generateDefaultAccountCreatedEvent())
                    .andGiven(UtilTest.generateCategoryCreatedEvent())
                    .andGiven(UtilTest.generateCategoryItemCreatedEvent())
                    .andGiven(RecordCreated(AccountId(UtilTest.accountId), record))
                    .`when`(modifyRecordCommand)
        } catch (e: Error) {
            assertNotNull("Expected error message", e.message)
            assertEquals("Expected AssertionError class", e.javaClass, AssertionError::class.java)
        }
    }

    @Test
    internal fun `Delete a non existing Record should throw RecordNotFoundException`() {
        val record = UtilTest.createRecordForTest(false)

        val newRecordId = UUID.randomUUID()
        val deleteRecordCommand = DeleteRecord(
                accountId = AccountId(UtilTest.accountId),
                recordId = RecordId(newRecordId))

        fixture.given(UtilTest.generateDefaultAccountCreatedEvent())
                .andGiven(UtilTest.generateCategoryCreatedEvent())
                .andGiven(UtilTest.generateCategoryItemCreatedEvent())
                .andGiven(RecordCreated(AccountId(UtilTest.accountId), record))
                .`when`(deleteRecordCommand)
                .expectExceptionMessage("Record with id $newRecordId not found")
                .expectException(RecordNotFoundException::class.java)
    }

    @Test
    internal fun `Delete a Record with non existing Account should throw AssertionError`() {
        val record = UtilTest.createRecordForTest(false)

        val deleteRecordCommand = DeleteRecord(
                accountId = AccountId(UUID.randomUUID()),
                recordId = record.recordId)

        try {
            fixture.given(UtilTest.generateDefaultAccountCreatedEvent())
                    .andGiven(UtilTest.generateCategoryCreatedEvent())
                    .andGiven(UtilTest.generateCategoryItemCreatedEvent())
                    .andGiven(RecordCreated(AccountId(UtilTest.accountId), record))
                    .`when`(deleteRecordCommand)
        } catch (e: Error) {
            assertNotNull("Expected error message", e.message)
            assertEquals("Expected AssertionError class", e.javaClass, AssertionError::class.java)
        }
    }

    @Test
    internal fun `Create a Record with non existing Category should throw CategoryNotFoundException`() {
        val record = UtilTest.createRecordForTest(true)
        val createRecordCommand = UtilTest.generateCreateRecordCommand(record)

        fixture.given(UtilTest.generateDefaultAccountCreatedEvent())
                .`when`(createRecordCommand)
                .expectExceptionMessage("Category with id ${UtilTest.category.categoryId.id} not found")
                .expectException(CategoryNotFoundException::class.java)
    }

    @Test
    internal fun `Create a Record with non existing Category Item should throw CategoryItemNotFoundException`() {
        val newCategoryItemId = CategoryItemId(UUID.randomUUID())
        val createRecordCommand = CreateRecord(
                accountId = AccountId(UtilTest.accountId),
                recordId = RecordId(UUID.randomUUID()),
                recordType = RecordType.EXPENSE,
                categoryId = UtilTest.category.categoryId,
                categoryItemId = newCategoryItemId,
                date = LocalDate.now(),
                amount = BigDecimal.ONE)

        fixture.given(UtilTest.generateDefaultAccountCreatedEvent())
                .andGiven(UtilTest.generateCategoryCreatedEvent())
                .`when`(createRecordCommand)
                .expectExceptionMessage("Category Item with id ${newCategoryItemId.id} not found")
                .expectException(CategoryItemNotFoundException::class.java)
    }

    @Test
    internal fun `Modify a Record with non existing Category should throw CategoryNotFoundException`() {
        val record = UtilTest.createRecordForTest(false)
        val recordModified = Record(
                record.recordId,
                type = RecordType.INCOME,
                date = record.date,
                categoryId = UtilTest.category.categoryId,
                categoryItemId = UtilTest.categoryItem.categoryItemId,
                amount = RecordAmount(BigDecimal.TEN),
                memo = NEW_MEMO)

        val modifyRecordCommand = ModifyRecord(
                accountId = AccountId(UtilTest.accountId),
                recordId = recordModified.recordId,
                recordType = recordModified.type,
                categoryId = UtilTest.category.categoryId,
                categoryItemId = UtilTest.categoryItem.categoryItemId,
                date = recordModified.date,
                amount = recordModified.amount.value,
                memo = recordModified.memo)

        fixture.given(UtilTest.generateDefaultAccountCreatedEvent())
                .andGiven(RecordCreated(AccountId(UtilTest.accountId), record))
                .`when`(modifyRecordCommand)
                .expectExceptionMessage("Category with id ${UtilTest.category.categoryId.id} not found")
                .expectException(CategoryNotFoundException::class.java)
    }

    @Test
    internal fun `Modify a Record with non existing Category Item should throw CategoryItemNotFoundException`() {
        val record = UtilTest.createRecordForTest(false)
        val recordModified = Record(
                record.recordId,
                type = RecordType.INCOME,
                date = record.date,
                categoryId = UtilTest.category.categoryId,
                categoryItemId = UtilTest.categoryItem.categoryItemId,
                amount = RecordAmount(BigDecimal.TEN),
                memo = NEW_MEMO)

        val newCategoryItemId = CategoryItemId(UUID.randomUUID())
        val modifyRecordCommand = ModifyRecord(
                accountId = AccountId(UtilTest.accountId),
                recordId = recordModified.recordId,
                recordType = recordModified.type,
                categoryId = recordModified.categoryId,
                categoryItemId = newCategoryItemId,
                date = recordModified.date,
                amount = recordModified.amount.value,
                memo = recordModified.memo)

        fixture.given(UtilTest.generateDefaultAccountCreatedEvent())
                .andGiven(UtilTest.generateCategoryCreatedEvent())
                .andGiven(RecordCreated(AccountId(UtilTest.accountId), record))
                .`when`(modifyRecordCommand)
                .expectExceptionMessage("Category Item with id ${newCategoryItemId.id} not found")
                .expectException(CategoryItemNotFoundException::class.java)
    }

    @Test
    internal fun `Create a Record exceeding quota by month for first day of this month should throw RecordsQuotaExceededException`() {
        val firstDayThisMonth = LocalDate.now().minusDays(LocalDate.now().dayOfMonth - 1.toLong())
        val record = UtilTest.createRecordForTest(true, date = firstDayThisMonth)
        val newRecord = UtilTest.createRecordForTest(true, date = firstDayThisMonth)
        val createNewRecordCommand = UtilTest.generateCreateRecordCommand(newRecord)

        fixture.given(UtilTest.generateDefaultAccountCreatedEvent(newRecordsQuotaByMonth = 1))
                .andGiven(UtilTest.generateCategoryCreatedEvent())
                .andGiven(UtilTest.generateCategoryItemCreatedEvent())
                .andGiven(RecordCreated(AccountId(UtilTest.accountId), record))
                .`when`(createNewRecordCommand)
                .expectExceptionMessage("Records quota exceeded")
                .expectException(RecordsQuotaExceededException::class.java)
    }

    @Test
    internal fun `Create a Record exceeding quota by month for last day of this month should throw RecordsQuotaExceededException`() {
        val lastDayThisMonth = LocalDate.now().minusDays(LocalDate.now().dayOfMonth.toLong()).plusMonths(1)
        val record = UtilTest.createRecordForTest(true, date = lastDayThisMonth)
        val newRecord = UtilTest.createRecordForTest(true, date = lastDayThisMonth)
        val createNewRecordCommand = UtilTest.generateCreateRecordCommand(newRecord)

        fixture.given(UtilTest.generateDefaultAccountCreatedEvent(newRecordsQuotaByMonth = 1))
                .andGiven(UtilTest.generateCategoryCreatedEvent())
                .andGiven(UtilTest.generateCategoryItemCreatedEvent())
                .andGiven(RecordCreated(AccountId(UtilTest.accountId), record))
                .`when`(createNewRecordCommand)
                .expectExceptionMessage("Records quota exceeded")
                .expectException(RecordsQuotaExceededException::class.java)
    }

    @Test
    internal fun `Create a Record evaluating the quota for the specific month should be correct`() {
        val firstDayThisMonth = LocalDate.now().minusDays(LocalDate.now().dayOfMonth.toLong())
        val thisMonthRecord = UtilTest.createRecordForTest(true, date = firstDayThisMonth)
        val lastDayOfLastLastMonth = LocalDate.now().minusDays(LocalDate.now().dayOfMonth.toLong()).minusMonths(1)
        val recordLastLastMonth = UtilTest.createRecordForTest(true, date = lastDayOfLastLastMonth)

        val lastDayOfLastMonth = LocalDate.now().minusDays(LocalDate.now().dayOfMonth.toLong())
        val newRecord = UtilTest.createRecordForTest(true, date = lastDayOfLastMonth)
        val createNewRecordCommand = UtilTest.generateCreateRecordCommand(newRecord)

        fixture.given(UtilTest.generateDefaultAccountCreatedEvent(newRecordsQuotaByMonth = 1))
                .andGiven(UtilTest.generateCategoryCreatedEvent())
                .andGiven(UtilTest.generateCategoryItemCreatedEvent())
                .andGiven(RecordCreated(AccountId(UtilTest.accountId), thisMonthRecord))
                .andGiven(RecordCreated(AccountId(UtilTest.accountId), recordLastLastMonth))
                .`when`(createNewRecordCommand)
                .expectSuccessfulHandlerExecution()
                .expectEvents(RecordCreated(AccountId(UtilTest.accountId), newRecord))
                .expectResultMessagePayload(true)
    }

    @Test
    internal fun `Create a Record for first day of the next month without exceeding quota by month should be correct`() {
        val recordForNow = UtilTest.createRecordForTest(true)
        val firstDayOfNextNextMonth = LocalDate.now().minusDays(LocalDate.now().dayOfMonth - 1.toLong()).plusMonths(2)
        val recordNextNextMonth = UtilTest.createRecordForTest(true, date = firstDayOfNextNextMonth)

        val firstDayNextMonth = LocalDate.now().minusDays(LocalDate.now().dayOfMonth.toLong() - 1).plusMonths(1)
        val newRecord = UtilTest.createRecordForTest(true, date = firstDayNextMonth)
        val createNewRecordCommand = UtilTest.generateCreateRecordCommand(newRecord)

        fixture.given(UtilTest.generateDefaultAccountCreatedEvent(newRecordsQuotaByMonth = 1))
                .andGiven(UtilTest.generateCategoryCreatedEvent())
                .andGiven(UtilTest.generateCategoryItemCreatedEvent())
                .andGiven(RecordCreated(AccountId(UtilTest.accountId), recordForNow))
                .andGiven(RecordCreated(AccountId(UtilTest.accountId), recordNextNextMonth))
                .`when`(createNewRecordCommand)
                .expectSuccessfulHandlerExecution()
                .expectEvents(RecordCreated(AccountId(UtilTest.accountId), newRecord))
                .expectResultMessagePayload(true)
    }

    @Test
    internal fun `Compare Record equality`() {
        val recordId = RecordId(UUID.randomUUID())
        val record1 = Record(recordId = recordId, type = RecordType.EXPENSE, categoryId = UtilTest.category.categoryId, categoryItemId = UtilTest.categoryItem.categoryItemId, date = LocalDate.now(), amount = RecordAmount(BigDecimal.TEN), memo = "test memo")
        val record2 = Record(recordId = recordId, type = RecordType.INCOME, categoryId = UtilTest.category.categoryId, categoryItemId = UtilTest.categoryItem.categoryItemId, date = LocalDate.now(), amount = RecordAmount(BigDecimal.ONE), memo = "test memo 2")
        val record3 = Record(recordId = RecordId(UUID.randomUUID()), type = RecordType.EXPENSE, categoryId = UtilTest.category.categoryId, categoryItemId = UtilTest.categoryItem.categoryItemId, date = LocalDate.now(), amount = RecordAmount(BigDecimal.TEN), memo = "test memo")
        val record4 = UtilTest.createRecordForTest(false)
        assertEquals("Memo should be empty string", record4.memo, "")

        val list = mutableSetOf(record1, record3)

        assertEquals("Same object should be equals", record1, record1)
        assertEquals("Same id should be equals", record1, record2)
        assertNotEquals("Different id should be different", record1, record3)
        assertNotEquals("Different object types should be different", record1, list)
        assertNotEquals("Null object should be different", record1, null)

        assertTrue(list.contains(record2))
        assertFalse(list.contains(record4))

        list.add(record2)
        assertEquals("Same object should not be added", 2, list.size)
    }
}

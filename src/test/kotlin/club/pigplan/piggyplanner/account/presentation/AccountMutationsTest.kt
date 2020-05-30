package club.pigplan.piggyplanner.account.presentation

import club.pigplan.piggyplanner.account.application.RecordDTO
import club.pigplan.piggyplanner.account.domain.model.RecordType
import club.pigplan.piggyplanner.account.presentation.graphql.AccountMutations
import org.axonframework.commandhandling.gateway.CommandGateway
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.CompletableFuture

@SpringBootTest
internal class AccountMutationsTest {

    @MockBean
    private lateinit var commandGateway: CommandGateway

    @Autowired
    private lateinit var accountMutations: AccountMutations

    @Test
    fun createRecord() {
        val future = createCompletableFuture()
        future.complete(true)

        val recordDTO = createRecordDTO()
        val response = accountMutations.createRecord(recordDTO)
        assertNotNull("Expected response not null", response.get())
        assertEquals("Expected response equal to the mocked value", response.get(), future.get())
    }

    @Test
    fun modifyRecord() {
        val future = createCompletableFuture()
        future.complete(true)

        val recordDTO = createRecordDTO()
        val response = accountMutations.modifyRecord(recordDTO)
        assertNotNull("Expected response not null", response.get())
        assertEquals("Expected response equal to the mocked value", response.get(), future.get())
    }

    @Test
    fun deleteRecord() {
        val future = createCompletableFuture()
        future.complete(true)

        val response = accountMutations.deleteRecord(accountId = UUID.randomUUID(), recordId = UUID.randomUUID())
        assertNotNull("Expected response not null", response.get())
        assertEquals("Expected response equal to the mocked value", response.get(), future.get())
    }

    private fun createCompletableFuture(): CompletableFuture<Any> {
        val future = CompletableFuture<Any>()
        Mockito.`when`(commandGateway.send<Any>(any())).thenReturn(future)
        return future
    }

    private fun createRecordDTO() =
            RecordDTO(accountId = UUID.randomUUID(),
                    recordId = UUID.randomUUID(),
                    recordType = RecordType.INCOME,
                    categoryId = UUID.randomUUID(),
                    categoryItemId = UUID.randomUUID(),
                    year = 2020,
                    month = 1,
                    day = 1,
                    amount = BigDecimal.ONE,
                    memo = "")
}

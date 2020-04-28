package club.pigplan.piggyplanner.account.infrastructure.repository

import club.pigplan.piggyplanner.account.domain.model.RecordType
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@Document
data class AccountProjection(val id: UUID,
                             val name: String,
                             val saverId: UUID,
                             val records: List<RecordProjection>)

@Document
data class RecordProjection(val id: UUID,
                            val accountId: UUID,
                            val type: RecordType,
                            val categoryId: UUID,
                            val categoryItemId: UUID,
                            val date: LocalDate,
                            @Field(targetType = FieldType.DECIMAL128)
                            val amount: BigDecimal,
                            val memo: String?)

@Document
data class CategoryProjection(val id: UUID,
                              val accountId: UUID,
                              val name: String,
                              val categoryItems: Set<CategoryItemProjection>)

@Document
data class CategoryItemProjection(val id: UUID,
                                  val name: String)

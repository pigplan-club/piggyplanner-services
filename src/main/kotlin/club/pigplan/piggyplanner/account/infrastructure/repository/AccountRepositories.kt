package club.pigplan.piggyplanner.account.infrastructure.repository

import club.pigplan.piggyplanner.account.infrastructure.repository.AccountProjection
import club.pigplan.piggyplanner.account.infrastructure.repository.CategoryItemProjection
import club.pigplan.piggyplanner.account.infrastructure.repository.CategoryProjection
import club.pigplan.piggyplanner.account.infrastructure.repository.RecordProjection
import org.springframework.data.repository.CrudRepository
import java.util.*

interface AccountStore : CrudRepository<AccountProjection, UUID>

interface RecordStore : CrudRepository<RecordProjection, UUID>

interface CategoryStore : CrudRepository<CategoryProjection, UUID>

interface CategoryItemStore : CrudRepository<CategoryItemProjection, UUID>
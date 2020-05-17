package club.pigplan.piggyplanner.account.infrastructure.repository

import club.pigplan.piggyplanner.account.infrastructure.projections.AccountProjection
import club.pigplan.piggyplanner.account.infrastructure.projections.CategoryItemProjection
import club.pigplan.piggyplanner.account.infrastructure.projections.CategoryProjection
import club.pigplan.piggyplanner.account.infrastructure.projections.RecordProjection
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AccountRepo : CrudRepository<AccountProjection, UUID>

@Repository
interface RecordRepo : CrudRepository<RecordProjection, UUID>

@Repository
interface CategoryRepo : CrudRepository<CategoryProjection, UUID>

@Repository
interface CategoryItemRepo : CrudRepository<CategoryItemProjection, UUID>
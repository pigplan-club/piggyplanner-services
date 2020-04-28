package club.pigplan.piggyplanner.account.domain.services

import club.pigplan.piggyplanner.account.domain.model.Category
import club.pigplan.piggyplanner.account.domain.model.CategoryId
import club.pigplan.piggyplanner.account.domain.model.CategoryItem
import club.pigplan.piggyplanner.account.domain.model.CategoryItemId
import club.pigplan.piggyplanner.account.domain.services.CategoryServices.DefaultCategoriesItems.*
import java.util.*


@Suppress("unused")
internal class CategoryServices {
    enum class DefaultCategoriesItems {
        ELECTRICITY,
        WATER,
        GAS,
        BREAKFAST,
        LUNCH,
        DINNER
    }

    enum class DefaultCategories(val categoryItems: List<DefaultCategoriesItems>) {
        UTILITY(mutableListOf(ELECTRICITY, WATER, GAS)),
        FOOD(mutableListOf(BREAKFAST, LUNCH, DINNER))
    }
}

fun getDefaultCategories(): List<Category> =
        CategoryServices.DefaultCategories.values().map { createDefaultCategory(it) }

private fun createDefaultCategory(defaultCategory: CategoryServices.DefaultCategories): Category {
    val category = Category(CategoryId(UUID.randomUUID()), defaultCategory.name)
    defaultCategory.categoryItems.forEach { item ->
        category.addCategoryItem(
                CategoryItem(
                        CategoryItemId(UUID.randomUUID()),
                        item.name
                ))
    }

    return category
}
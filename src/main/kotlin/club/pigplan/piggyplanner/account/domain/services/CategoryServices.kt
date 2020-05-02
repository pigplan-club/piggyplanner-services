package club.pigplan.piggyplanner.account.domain.services

import club.pigplan.piggyplanner.account.domain.model.Category
import club.pigplan.piggyplanner.account.domain.model.CategoryId
import club.pigplan.piggyplanner.account.domain.model.CategoryItem
import club.pigplan.piggyplanner.account.domain.model.CategoryItemId
import java.util.*


fun getDefaultCategories(): List<Category> =
        DefaultCategories.values().map { createDefaultCategory(it) }

private fun createDefaultCategory(defaultCategory: DefaultCategories): Category {
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
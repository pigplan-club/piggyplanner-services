package club.pigplan.piggyplanner.account.domain.services

import club.pigplan.piggyplanner.account.domain.services.DefaultCategoriesItems.*

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
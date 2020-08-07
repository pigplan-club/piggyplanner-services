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

enum class DefaultCategories(val categoryItems: Set<DefaultCategoriesItems>) {
    UTILITY(mutableSetOf(ELECTRICITY, WATER, GAS)),
    FOOD(mutableSetOf(BREAKFAST, LUNCH, DINNER))
}
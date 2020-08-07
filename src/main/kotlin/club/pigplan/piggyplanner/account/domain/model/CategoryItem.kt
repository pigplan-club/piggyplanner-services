package club.pigplan.piggyplanner.account.domain.model

import club.pigplan.piggyplanner.common.domain.model.Entity

class CategoryItem(val categoryItemId: CategoryItemId) : Entity() {

    lateinit var name: String
        private set

    constructor(categoryItemId: CategoryItemId, name: String) : this(categoryItemId) {
        this.name = name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CategoryItem

        if (categoryItemId != other.categoryItemId) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = categoryItemId.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }

    override fun toString(): String {
        return "CategoryItem(categoryItemId=$categoryItemId, name='$name')"
    }
}

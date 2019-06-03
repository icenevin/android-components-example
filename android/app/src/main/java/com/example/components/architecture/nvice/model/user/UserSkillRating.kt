package com.example.components.architecture.nvice.model.user

import com.example.components.architecture.nvice.util.extension.capitalize

enum class UserSkillRating(
        private val scoreRange: IntRange
) {
    EXCELLENT(95..100),
    VERY_GOOD(85..94),
    GOOD(70..84),
    FAIR(45..69),
    POOR(25..44),
    VERY_POOR(0..24);

    override fun toString(): String {
        return this.name.replace("_", " ").capitalize()
    }

    companion object {
        fun from(score: Int): UserSkillRating = requireNotNull(values().find { score in it.scoreRange })
    }
}
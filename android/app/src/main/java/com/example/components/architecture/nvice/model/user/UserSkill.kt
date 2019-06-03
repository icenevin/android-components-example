package com.example.components.architecture.nvice.model.user

import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.DiffUtil
import org.parceler.Parcel

@Parcel
data class UserSkill(
        public var id: Int? = 0,
        public var name: String? = "",
        public var score: Int? = 0
) {

    fun getScoreRating() = UserSkillRating.from(score
            ?: 0)

    fun getScoreRatingColor() = ColorUtils.HSLToColor(floatArrayOf(score?.times(1.2f)
            ?: 0f, 0.82f, 0.53f))
}
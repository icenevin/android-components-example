package com.example.components.architecture.nvice.model.user

import com.example.components.architecture.nvice.util.DateUtils
import org.parceler.Parcel

@Parcel
data class UserExperience(
        public var id: Int? = 0,
        public var beginDate: String? = "",
        public var endDate: String? = "",
        public var work: String? = "",
        public var description: String? = ""
) {

    fun getBeginYear(): String = if (!beginDate.isNullOrEmpty()) {
        DateUtils.parse(beginDate).year.toString()
    } else {
        "-"
    }

    fun getEndYear(): String = if (!endDate.isNullOrEmpty()) {
        DateUtils.parse(endDate).year.toString()
    } else {
        "-"
    }
}
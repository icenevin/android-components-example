package com.example.components.architecture.nvice.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.v7.util.DiffUtil
import com.example.components.architecture.nvice.util.random
import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter


@Entity
data class User(
        @field:SerializedName("uid")
        @PrimaryKey var id: Int? = null,

        @field:SerializedName("firstName")
        @ColumnInfo(name = "first_name")
        var firstName: String? = "",

        @field:SerializedName("lastName")
        @ColumnInfo(name = "last_name")
        var lastName: String? = "",

        @field:SerializedName("imageProfileUrl")
        @ColumnInfo(name = "avatar_url")
        var avatar: String? = "",

        @field:SerializedName("status")
        @ColumnInfo(name = "status")
        var status: UserStatus? = UserStatus.UNDEFINED
) {
    fun getWorkerID() = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + (10000 + id!!)

    companion object {
        var DIFF_CALLBACK: DiffUtil.ItemCallback<User> = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }
}

enum class UserStatus constructor(val id: Int?) {
    @SerializedName("0")
    PERMANENT(0),

    @SerializedName("1")
    TEMPORARY(1),

    @SerializedName("2")
    UNDEFINED(2);

    fun getValue(): Int? = id

    companion object {
        fun from(id: Int?): UserStatus = requireNotNull(values().find { it.id == id })
        fun random(): UserStatus = from((0..2).random())
    }
}
package com.example.components.architecture.nvice.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.v7.util.DiffUtil
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
@Entity
data class User(
        @field:SerializedName("uid")
        @PrimaryKey public var id: Int? = null,

        @field:SerializedName("staffId")
        public var staffId: String? = "",

        @field:SerializedName("firstName")
        @ColumnInfo(name = "first_name")
        public var firstName: String? = "",

        @field:SerializedName("lastName")
        @ColumnInfo(name = "last_name")
        public var lastName: String? = "",

        @field:SerializedName("birthday")
        @ColumnInfo(name= "birthday")
        public var birthday: String? = "",

        @field:SerializedName("description")
        @ColumnInfo(name= "description")
        public var description: String? = "",

        @field:SerializedName("avatarProfileUrl")
        @ColumnInfo(name = "avatar_url")
        public var avatar: String? = null,

        @field:SerializedName("coverProfileUrl")
        @ColumnInfo(name = "cover_url")
        public var cover: String? = null,

        @field:SerializedName("status")
        @ColumnInfo(name = "status")
        public var status: UserStatus? = UserStatus.UNDEFINED,

        @field:SerializedName("position")
        @ColumnInfo(name= "position")
        public var position: UserPosition? = UserPosition.UNDEFINED
        ) {

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
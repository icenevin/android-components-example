package com.example.components.architecture.nvice.model.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.recyclerview.widget.DiffUtil
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
        @ColumnInfo(name = "birthday")
        public var birthday: String? = "",

        @field:SerializedName("description")
        @ColumnInfo(name = "description")
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
        @ColumnInfo(name = "position")
        public var position: UserPosition? = UserPosition.UNDEFINED,

        @field:SerializedName("skills")
        @ColumnInfo(name = "skills")
        public var skills: MutableList<UserSkill?>? = mutableListOf(),

        @field:SerializedName("experiences")
        @ColumnInfo(name = "experiences")
        public var experiences: MutableList<UserExperience?>? = mutableListOf()
) {

    fun getFullName() = "${this.firstName} ${this.lastName}".trimEnd()
}
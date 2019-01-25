package com.example.components.architecture.nvice.dao

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.persistence.db.SupportSQLiteQuery
import android.arch.persistence.room.*
import com.example.components.architecture.nvice.model.User
import com.example.components.architecture.nvice.model.UserStatus

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun selectAll(): LiveData<MutableList<User>>

    @Query("SELECT * FROM user ORDER BY id")
    fun selectAllOrderById(): LiveData<MutableList<User>>

    @Query("SELECT * FROM user ORDER BY id")
    fun selectAllOrderByIdForPaging(): DataSource.Factory<Int, User>

    @Query("SELECT * FROM user WHERE status IN (:status) ORDER BY id")
    fun selectAllByStatusOrderByIdForPaging(status: List<Int>): DataSource.Factory<Int, User>

    @RawQuery(observedEntities = [User::class])
    fun rawQueryForPaging(query: SupportSQLiteQuery): DataSource.Factory<Int, User>

    @Query("SELECT * FROM user WHERE user.id == :id")
    fun findById(id: Int): LiveData<User>

    @Query("SELECT DISTINCT status FROM user")
    fun selectAllUserStatus(): LiveData<MutableList<UserStatus>>

    @Query("SELECT COUNT(*) FROM user")
    fun count(): Int

    @Query("SELECT MAX(id) FROM user")
    fun selectLatestId(): Int

    @Insert
    fun insertList(list: MutableList<User>)

    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)
}
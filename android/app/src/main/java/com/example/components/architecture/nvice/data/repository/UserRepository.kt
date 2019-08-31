package com.example.components.architecture.nvice.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.components.architecture.nvice.dao.UserDao
import com.example.components.architecture.nvice.data.datasource.DataSourceFactory
import com.example.components.architecture.nvice.data.preference.AppSettingsPreference
import com.example.components.architecture.nvice.model.user.User
import com.example.components.architecture.nvice.util.scheduler.DefaultScheduler
import com.example.components.architecture.nvice.ui.user.UserDataSourceFactory
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
        private val userDao: UserDao,
        private val userDataSourceFactory: UserDataSourceFactory,
        private val appSettingsPreference: AppSettingsPreference
) {

    fun getUserList() = userDataSourceFactory

    fun getUserStatusList() = userDao.selectAllUserStatus()

    fun getUserPagedListByStatus(statusList: List<Int>) {
        userDataSourceFactory.searchByStatus(statusList)
    }

    suspend fun getUserById(id: Int) = userDao.findByIdAsync(id)

    fun getLatestUserId() = userDao.selectLatestId()

    fun addUser(user: User?) = DefaultScheduler.AsyncScheduler.execute { user?.let { userDao.insert(it) } }

    fun updateUser(user: User?) = DefaultScheduler.AsyncScheduler.execute { user?.let { userDao.update(it) } }

    fun deleteUser(user: User) = DefaultScheduler.AsyncScheduler.execute { userDao.delete(user) }

    fun toggleSort() {
        userDataSourceFactory.setDescendingOrder(userDataSourceFactory.queryOrderSortType == DataSourceFactory.SortType.ASC)
    }

    fun searchUser(queue: String) {
        userDataSourceFactory.searchByREGEX(queue)
    }
}